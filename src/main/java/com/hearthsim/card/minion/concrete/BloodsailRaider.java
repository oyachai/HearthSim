package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.event.effect.CardEffectCharacter;
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
    public CardEffectCharacter getBattlecryEffect() {
        return new CardEffectCharacter<Minion>() {
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
