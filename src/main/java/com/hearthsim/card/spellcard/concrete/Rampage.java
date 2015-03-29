package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.CharacterFilterTargetedSpell;
import com.hearthsim.event.effect.CardEffectCharacterBuffDelta;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public class Rampage extends SpellCard {

    private final static CardEffectCharacter effect = new CardEffectCharacterBuffDelta(3, 3);

    public Rampage() {
        super();

        this.characterFilter = CharacterFilterTargetedSpell.ALL_MINIONS;
    }

    @Override
    public boolean canBeUsedOn(PlayerSide playerSide, Minion minion, BoardModel boardModel) {
        if (!super.canBeUsedOn(playerSide, minion, boardModel)) {
            return false;
        }

        if (minion.getHealth() == minion.getMaxHealth()) {
            return false;
        }

        return true;
    }

    @Override
    protected CardEffectCharacter getEffect() {
        return Rampage.effect;
    }
}
