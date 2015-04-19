package com.hearthsim.card.classic.minion.epic;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class BloodKnight extends Minion implements MinionBattlecryInterface {

    public BloodKnight() {
        super();
    }

    /**
     * Battlecry: All minions lose Divine Shield.  Gain +3/+3 for each Shield lost
     */
    @Override
    public EffectCharacter getBattlecryEffect() {
        return new EffectCharacter<Minion>() {
            @Override
            public HearthTreeNode applyEffect(PlayerSide originSide, Minion origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) {
                for (Minion minion : boardState.data_.modelForSide(PlayerSide.CURRENT_PLAYER).getMinions()) {
                    if (minion != origin && minion.getDivineShield()) {
                        minion.setDivineShield(false);
                        origin.setHealth((byte) (origin.getHealth() + 3));
                        origin.setMaxHealth((byte) (origin.getMaxHealth() + 3));
                        origin.setAttack((byte) (origin.getAttack() + 3));
                    }
                }
                for (Minion minion : boardState.data_.modelForSide(PlayerSide.WAITING_PLAYER).getMinions()) {
                    if (minion != origin && minion.getDivineShield()) {
                        minion.setDivineShield(false);
                        origin.setHealth((byte) (origin.getHealth() + 3));
                        origin.setMaxHealth((byte) (origin.getMaxHealth() + 3));
                        origin.setAttack((byte) (origin.getAttack() + 3));
                    }
                }
                return boardState;
            }
        };
    }
}
