package com.hearthsim.card.blackrockmountain.minion.common;

import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.HearthAction;
import com.hearthsim.util.tree.HearthTreeNode;
import com.hearthsim.util.tree.RandomEffectNode;

public class FireguardDestroyer extends Minion implements MinionBattlecryInterface {

    public FireguardDestroyer() {
        super();
    }

    @Override
    public EffectCharacter<Minion> getBattlecryEffect() {
        return (PlayerSide targetSide, CharacterIndex minionPlacementIndex, HearthTreeNode boardState) -> {
            CharacterIndex thisMinionIndex = minionPlacementIndex.indexToRight();
            HearthTreeNode toRet = new RandomEffectNode(boardState,
                new HearthAction(HearthAction.Verb.UNTARGETABLE_BATTLECRY, PlayerSide.CURRENT_PLAYER,
                    thisMinionIndex.getInt(),
                    PlayerSide.CURRENT_PLAYER, minionPlacementIndex));
            for (int indx = 1; indx <= 4; ++indx) {
                HearthTreeNode child = new HearthTreeNode(boardState.data_.deepCopy());
                child.data_.getCharacter(PlayerSide.CURRENT_PLAYER, thisMinionIndex).addAttack((byte)indx);
                toRet.addChild(child);
            }
            return toRet;
        };
    }
}
