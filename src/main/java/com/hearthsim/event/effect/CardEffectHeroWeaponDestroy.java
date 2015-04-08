package com.hearthsim.event.effect;

import com.hearthsim.card.Card;
import com.hearthsim.event.deathrattle.DeathrattleAction;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class CardEffectHeroWeaponDestroy extends CardEffectHero {
    // -1 means destroy it completely
    private final int durabilityLoss;

    public CardEffectHeroWeaponDestroy() {
        this(-1);
    }

    public CardEffectHeroWeaponDestroy(int durabilityLoss) {
        this.durabilityLoss = durabilityLoss;
    }

    @Override
    public HearthTreeNode applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, HearthTreeNode boardState) {
        DeathrattleAction action = null;
        PlayerModel targetPlayer = boardState.data_.modelForSide(targetSide);

        if (this.durabilityLoss < 0) {
            action = targetPlayer.getHero().destroyWeapon();
        } else {
            boolean hasWeapon = targetPlayer.getHero().getWeapon() != null;
            if (hasWeapon) {
                targetPlayer.getHero().getWeapon().useWeaponCharge(this.durabilityLoss);
                action = targetPlayer.getHero().checkForWeaponDeath();
            }
        }

        if (action != null) {
            boardState = action.performAction(null, targetSide, boardState, false);
        }
        return boardState;
    }

    public static final CardEffectHeroWeaponDestroy DESTROY = new CardEffectHeroWeaponDestroy();
}
