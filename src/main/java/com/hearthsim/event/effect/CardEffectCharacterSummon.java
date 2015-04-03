package com.hearthsim.event.effect;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class CardEffectCharacterSummon extends CardEffectCharacter {
    private Minion minion;

    public CardEffectCharacterSummon(Minion minion) {
        this.minion = minion;
    }

    @Override
    public HearthTreeNode applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) {
        Minion summon = this.minion;
        // if no origin is set then we have no idea whether we are in the original state. copy our base minion and summon a copy.
        // this is used for Minions with RNG battlecries (e.g. Bomb Lobber)
        if (origin == null) {
            summon = (Minion)minion.deepCopy();
        }
        summon.hasBeenUsed(true);
        return summon.summonMinion(targetSide, targetCharacterIndex, boardState, true, false);
    }
}
