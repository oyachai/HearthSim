package com.hearthsim.util.factory;

import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.Deck;
import com.hearthsim.card.Location;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.player.playercontroller.BoardScorer;
import com.hearthsim.util.tree.HearthTreeNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class BoardStateFactoryBase {

    protected final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    protected final Deck deckPlayer0_;

    private final ChildNodeCreator childNodeCreator;

    /**
     * Constructor
     *
     * @param deckPlayer0
     * @param deckPlayer1
     */
    public BoardStateFactoryBase(Deck deckPlayer0, Deck deckPlayer1) {
        this(deckPlayer0, deckPlayer1, new ChildNodeCreatorBase(deckPlayer0, deckPlayer1));
    }

    /**
     * Constructor
     *
     * @param deckPlayer0
     * @param deckPlayer1
     */
    public BoardStateFactoryBase(Deck deckPlayer0, Deck deckPlayer1, ChildNodeCreator creator) {
        deckPlayer0_ = deckPlayer0;

        childNodeCreator = creator;
    }

    protected ArrayList<HearthTreeNode> createChildren(HearthTreeNode boardStateNode) throws HSException {
        ArrayList<HearthTreeNode> nodes = new ArrayList<>();
        nodes.addAll(this.childNodeCreator.createHeroAbilityChildren(boardStateNode));
        nodes.addAll(this.childNodeCreator.createPlayCardChildren(boardStateNode));
        nodes.addAll(this.childNodeCreator.createAttackChildren(boardStateNode));
        return nodes;
    }

    /**
     * Recursively generate all possible moves
     * This function recursively generates all possible moves that can be done starting from a given BoardState.
     * While generating the moves, it applies the scoring function to each BoardState generated, and it will only keep the
     * highest scoring branch.
     * The results are stored in a tree structure and returned as a tree of BoardState class.
     *
     * @param boardStateNode The initial BoardState wrapped in a HearthTreeNode.
     * @param scoreFunc      The scoring function for AI.
     * @return boardStateNode manipulated such that all subsequent actions are children of the original boardStateNode input.
     */
    public abstract HearthTreeNode doMoves(HearthTreeNode boardStateNode, BoardScorer ai) throws HSException;

    /**
     * Handles dead minions
     * For each dead minion, the function calls its deathrattle in the correct order, and then removes the dead minions from the board.
     *
     * @return true if there are dead minions left (minions might have died during deathrattle). false otherwise.
     * @throws HSException
     */
    public static HearthTreeNode handleDeadMinions(HearthTreeNode boardState) {
        HearthTreeNode toRet = boardState;

        // First, remove all the dead minions.  If the dead minions had deathrattles, queue them up.
        List<MinionPlayerLocation> deadMinions = new ArrayList<>();
        Iterator<BoardModel.MinionPlayerPair> minionIter = toRet.data_.getAllMinionsFIFOList().iterator();
        while (minionIter.hasNext()) {
            BoardModel.MinionPlayerPair minionIdPair = minionIter.next();
            if (minionIdPair.getMinion().getTotalHealth() <= 0) {
                // Determine the proper character location
                CharacterIndex leftIndex = boardState.data_.modelForSide(minionIdPair.getPlayerSide()).getIndexForCharacter(minionIdPair.getMinion());
                while (leftIndex != CharacterIndex.HERO && leftIndex != CharacterIndex.UNKNOWN &&
                    boardState.data_.modelForSide(minionIdPair.getPlayerSide()).getCharacter(leftIndex.indexToLeft()).getTotalHealth() <= 0) {
                    leftIndex = leftIndex.indexToLeft();
                }
                deadMinions.add(new MinionPlayerLocation(minionIdPair, new Location<>(minionIdPair.getPlayerSide(), leftIndex)));
            }
        }
        for (MinionPlayerLocation minionPlayerLocation : deadMinions) {
            toRet.data_.removeMinion(minionPlayerLocation.minionPlayerPair);
        }

        // Next, resolve each minion's death sequence
        for (MinionPlayerLocation minionPlayerLocation : deadMinions) {
            PlayerSide playerSide = minionPlayerLocation.minionPlayerPair.getPlayerSide();
            toRet = minionPlayerLocation.minionPlayerPair.getMinion()
                .destroyAndNotify(playerSide, minionPlayerLocation.location.getIndex(), toRet);
        }

        if (toRet.data_.hasDeadMinions())
            return BoardStateFactoryBase.handleDeadMinions(toRet);
        else
            return toRet;
    }

    private static class MinionPlayerLocation {
        public final BoardModel.MinionPlayerPair minionPlayerPair;
        public final Location<CharacterIndex> location;

        public MinionPlayerLocation(BoardModel.MinionPlayerPair minionPlayerPair, Location<CharacterIndex> location) {
            this.minionPlayerPair = minionPlayerPair;
            this.location = location;
        }
    }
}
