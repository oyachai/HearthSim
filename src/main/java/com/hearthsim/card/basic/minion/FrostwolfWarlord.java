package com.hearthsim.card.basic.minion;

import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class FrostwolfWarlord extends Minion implements MinionBattlecryInterface {

    public FrostwolfWarlord() {
        super();
    }

    /**
     * Battlecry: gain +1/+1 for each friendly minion on the battlefield
     */
    @Override
    public EffectCharacter<Minion> getBattlecryEffect() {
        return (PlayerSide targetSide, CharacterIndex minionPlacementIndex, HearthTreeNode boardState) -> {
            byte numBuffs = (byte) (boardState.data_.modelForSide(PlayerSide.CURRENT_PLAYER).getNumMinions() - 1); //Don't count the Warlord itself
            this.addAttack(numBuffs);
            this.addHealth(numBuffs);
            this.addMaxHealth(numBuffs);
            return boardState;
        };
    }
}
