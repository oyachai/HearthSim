package com.hearthsim.card.basic.minion;

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
        return (PlayerSide originSide, Minion origin, PlayerSide targetSide, int minionPlacementIndex, HearthTreeNode boardState) -> {
            int numBuffs = boardState.data_.modelForSide(PlayerSide.CURRENT_PLAYER).getNumMinions() - 1; //Don't count the Warlord itself
            this.setAttack((byte) (this.getAttack() + numBuffs));
            this.setHealth((byte) (this.getHealth() + numBuffs));
            this.setMaxHealth((byte) (this.getMaxHealth() + numBuffs));
            return boardState;
        };
    }
}
