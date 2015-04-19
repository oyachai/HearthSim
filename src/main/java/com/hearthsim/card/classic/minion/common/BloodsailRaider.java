package com.hearthsim.card.classic.minion.common;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class BloodsailRaider extends Minion implements MinionBattlecryInterface {

    public BloodsailRaider() {
        super();
    }

    /**
     * Battlecry: Gain Attack equal to the Attack of your weapon
     */
    @Override
    public EffectCharacter getBattlecryEffect() {
        return new EffectCharacter<Minion>() {
            @Override
            public HearthTreeNode applyEffect(PlayerSide originSide, Minion origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) {
                PlayerModel currentPlayer = boardState.data_.getCurrentPlayer();
                if (currentPlayer.getHero().getWeapon() != null) {
                    origin.addAttack(currentPlayer.getHero().getWeapon().getWeaponDamage());
                }
                return boardState;
            }
        };
    }
}
