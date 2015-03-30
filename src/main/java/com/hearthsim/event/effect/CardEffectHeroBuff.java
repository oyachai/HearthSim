package com.hearthsim.event.effect;

import com.hearthsim.card.Card;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class CardEffectHeroBuff extends CardEffectHero {
    private final byte attackDelta;
    private final byte armorDelta;

    public CardEffectHeroBuff(int attackDelta, int armorDelta) {
        this.attackDelta = (byte) attackDelta;
        this.armorDelta = (byte) armorDelta;
    }

    public HearthTreeNode applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, HearthTreeNode boardState) {
        if (this.attackDelta > 0) {
            boardState.data_.modelForSide(targetSide).getHero().addExtraAttackUntilTurnEnd(this.attackDelta);
        }

        if (this.armorDelta > 0) {
            boardState.data_.modelForSide(targetSide).getHero().addArmor(this.armorDelta);
        }
        return boardState;
    }
}
