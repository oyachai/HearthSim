package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.CharacterFilterTargetedSpell;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectCharacterBuffDelta;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public class Rampage extends SpellCard {

    private final static CardEffectCharacter effect = new CardEffectCharacterBuffDelta(3, 3);

    private final static CharacterFilter filter = new CharacterFilterTargetedSpell() {
        protected boolean includeEnemyMinions() { return true; }
        protected boolean includeOwnMinions() { return true; }

        @Override
        public boolean targetMatches(PlayerSide originSide, Card origin, PlayerSide targetSide, Minion targetCharacter, BoardModel board) {
            if (!super.targetMatches(originSide, origin, targetSide, targetCharacter, board)) {
                return false;
            }

            if (targetCharacter.getHealth() == targetCharacter.getMaxHealth()) {
                return false;
            }

            return true;
        }
    };

    public Rampage() {
        super();
    }

    @Override
    public CharacterFilter getTargetableFilter() {
        return Rampage.filter;
    }

    @Override
    public CardEffectCharacter getTargetableEffect() {
        return Rampage.effect;
    }
}
