package com.hearthsim.util.tree;

import com.hearthsim.card.Card;
import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.minion.*;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.player.playercontroller.BoardScorer;
import com.hearthsim.util.HearthAction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A tree that keeps track of possible game states
 */
public class HearthTreeNode {

    byte depth_;

    public final BoardModel data_;
    HearthAction action;
    protected double score_;
    private double bestChildScore_;

    protected List<HearthTreeNode> children_;

    private HearthTreeNode parent;

    public HearthTreeNode getParent() {
        return parent;
    }

    private class NodeValPair {
        public final HearthTreeNode node_;
        public final double value_;

        NodeValPair(HearthTreeNode node, double value) {
            node_ = node;
            value_ = value;
        }
    }

    public HearthTreeNode(BoardModel data) {
        this(data, null);
    }

    public HearthTreeNode(BoardModel data, HearthAction action) {
        this(data, action, 0.0);
    }

    private HearthTreeNode(BoardModel data, HearthAction action, double score) {
        this(data, action, score, (byte)0);
    }

    public HearthTreeNode(BoardModel data, HearthAction action, double score, byte depth) {
        this(data, action, score, depth, null);
    }

    public HearthTreeNode(BoardModel data, HearthAction action, double score, byte depth,
            List<HearthTreeNode> children) {
        data_ = data;
        this.action = action;
        score_ = score;
        children_ = children;
        depth_ = depth;
    }

    public HearthAction getAction() {
        return action;
    }

    public void setAction(HearthAction action) {
        this.action = action;
    }

    public byte getDepth() {
        return depth_;
    }

    public void setDepth(byte value) {
        depth_ = value;
    }

    public double getScore() {
        return score_;
    }

    public void setScore(double value) {
        score_ = value;
    }

    public double getBestChildScore() {
        return bestChildScore_;
    }

    public void setBestChildScore(double bestChildScore) {
        this.bestChildScore_ = bestChildScore;
    }

    public HearthTreeNode addChild(HearthTreeNode node) {
        node.setDepth((byte)(depth_ + 1));
        if (children_ == null) {
            children_ = new ArrayList<>();
        }
        children_.add(node);
        node.parent = this;
        return node;
    }

    public void addChildren(Collection<HearthTreeNode> nodes) {
        for (HearthTreeNode node : nodes) {
            this.addChild(node);
        }
    }

    public void clearChildren() {
        if (children_ != null)
            children_.clear();
    }

    public List<HearthTreeNode> getChildren() {
        return children_;
    }

    public void setChildren(List<HearthTreeNode> children) {
        children_ = children;
        for (HearthTreeNode node : children) {
            node.parent = this;
        }
    }

    public int numChildren() {
        if (children_ == null)
            return 0;
        else {
            return children_.size();
        }
    }

    public boolean isLeaf() {
        return children_ == null || children_.size() == 0;
    }

    /**
     * Returns the node below this node that result in the highest value when a given function is applied
     *
     * @param func Function to apply to each node
     * @return
     */
    public HearthTreeNode findMaxOfFunc(BoardScorer ai) {
        NodeValPair nvp = this.findMaxOfFuncImpl(ai);
        return nvp.node_;
    }

    private NodeValPair findMaxOfFuncImpl(BoardScorer ai) {
        if (this.isLeaf())
            return new NodeValPair(this, ai.boardScore(this.data_));

        NodeValPair maxNode = null;
        double maxSoFar = -1.e300;
        for (final HearthTreeNode child : children_) {
            NodeValPair maxOfChild = child.findMaxOfFuncImpl(ai);
            if (maxOfChild.value_ > maxSoFar) {
                maxSoFar = maxOfChild.value_;
                maxNode = maxOfChild;
            }
        }
        return maxNode;
    }

    @Override
    public String toString() {
        String toRet = "{";
        toRet = toRet + "\"data\": " + data_ + ", ";
        toRet = toRet + "\"children\": [";
        if (children_ != null) {
            boolean hasContent = false;
            for (final HearthTreeNode child : children_) {
                toRet = toRet + child + ", ";
                hasContent = true;
            }
            if (hasContent) {
                toRet = toRet.substring(0, toRet.length() - 2);
            }
        }
        toRet = toRet + "]";
        toRet = toRet + "}";
        return toRet;
    }

    public HearthTreeNode notifyMinionDamaged(PlayerSide targetSide, Minion minion) {
        HearthTreeNode toRet = this;
        for (CharacterIndex.CharacterLocation characterLocation : toRet.data_) {
            Minion character = toRet.data_.getCharacter(characterLocation);
            if (!character.isSilenced() && character instanceof MinionDamagedInterface) {
                toRet = ((MinionDamagedInterface)character).minionDamagedEvent(characterLocation.getPlayerSide(), targetSide, minion, toRet);
            }
        }

        return toRet;
    }

    public HearthTreeNode notifyMinionDead(PlayerSide deadMinionPlayerSide, Minion deadMinion) {
        HearthTreeNode toRet = this;
        for (CharacterIndex.CharacterLocation characterLocation : toRet.data_) {
            Minion character = toRet.data_.getCharacter(characterLocation);
            if (!character.isSilenced() && character instanceof MinionDeadInterface) {
                toRet = ((MinionDeadInterface)character).minionDeadEvent(characterLocation.getPlayerSide(), deadMinionPlayerSide, deadMinion, toRet);
            }
        }

        for (Card card : toRet.data_.modelForSide(PlayerSide.CURRENT_PLAYER).getHand()) {
            if (card instanceof MinionDeadInterface) {
                toRet = ((MinionDeadInterface)card).minionDeadEvent(PlayerSide.CURRENT_PLAYER, deadMinionPlayerSide, deadMinion, toRet);
            }
        }

        for (Card card : toRet.data_.modelForSide(PlayerSide.WAITING_PLAYER).getHand()) {
            if (card instanceof MinionDeadInterface) {
                toRet = ((MinionDeadInterface)card).minionDeadEvent(PlayerSide.WAITING_PLAYER, deadMinionPlayerSide, deadMinion, toRet);
            }
        }

        return toRet;
    }

    public HearthTreeNode notifyMinionHealed(PlayerSide targetSide, Minion minion) {
        HearthTreeNode toRet = this;
        for (CharacterIndex.CharacterLocation characterLocation : toRet.data_) {
            Minion character = toRet.data_.getCharacter(characterLocation);
            if (!character.isSilenced() && character instanceof MinionHealedInterface) {
                toRet = ((MinionHealedInterface)character).minionHealedEvent(characterLocation.getPlayerSide(), targetSide, minion, toRet);
            }
        }

        return toRet;
    }

    public HearthTreeNode notifyMinionPlacement(PlayerSide targetSide, Minion minion) {
        HearthTreeNode toRet = this;
        for (CharacterIndex.CharacterLocation characterLocation : toRet.data_) {
            Minion character = toRet.data_.getCharacter(characterLocation);
            if (!character.isSilenced() && character instanceof MinionPlacedInterface) {
                toRet = ((MinionPlacedInterface)character).minionPlacedEvent(characterLocation.getPlayerSide(), targetSide, minion, toRet);
            }
        }

        return toRet;
    }

    public HearthTreeNode notifyMinionPlayed(PlayerSide targetSide, Minion minion) {
        HearthTreeNode toRet = this;
        for (CharacterIndex.CharacterLocation characterLocation : toRet.data_) {
            Minion character = toRet.data_.getCharacter(characterLocation);
            if (!character.isSilenced() && character instanceof MinionPlayedInterface) {
                toRet = ((MinionPlayedInterface)character).minionPlayedEvent(characterLocation.getPlayerSide(), targetSide, minion, toRet);
            }
        }

        return toRet;
    }

    public HearthTreeNode notifyMinionSummon(PlayerSide targetSide, Minion minion) {
        HearthTreeNode toRet = this;
        for (CharacterIndex.CharacterLocation characterLocation : toRet.data_) {
            Minion character = toRet.data_.getCharacter(characterLocation);
            if (!character.isSilenced() && character instanceof MinionSummonedInterface) {
                toRet = ((MinionSummonedInterface)character).minionSummonEvent(characterLocation.getPlayerSide(), targetSide, minion, toRet);
            }
        }

        return toRet;
    }

    public HearthTreeNode notifyHeroAbilityUsed(PlayerSide targetSide, Minion targetCharacter) {
        HearthTreeNode toRet = this;
        ArrayList<BoardModel.MinionPlayerPair> inspiredMinions = new ArrayList<>();
        for (BoardModel.MinionPlayerPair mp : toRet.data_.getAllMinionsFIFOList()) {
            if (mp.getMinion() instanceof MinionWithInspire && !mp.getMinion().isSilenced()) {
                inspiredMinions.add(mp);
            }
        }

        for (BoardModel.MinionPlayerPair mp : inspiredMinions) {
            toRet = ((MinionWithInspire)mp.getMinion()).heroAbilityUsedEvent(mp.getPlayerSide(), targetSide, targetCharacter, toRet);
        }

        return toRet;
    }
}
