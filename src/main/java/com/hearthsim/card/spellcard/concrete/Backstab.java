package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.CharacterFilterTargetedSpell;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public class Backstab extends SpellDamage {

    private final static CharacterFilter filter = new CharacterFilterTargetedSpell() {
        protected boolean includeEnemyMinions() { return true; }
        protected boolean includeOwnMinions() { return true; }

        @Override
        public boolean targetMatches(PlayerSide originSide, Card origin, PlayerSide targetSide, Minion targetCharacter, BoardModel board) {
            if (!super.targetMatches(originSide, origin, targetSide, targetCharacter, board)) {
                return false;
            }

            if (targetCharacter.getHealth() != targetCharacter.getMaxHealth()) {
                return false;
            }

            return true;
        }
    };

    public Backstab() {
        super();
    }

    @Override
    public CharacterFilter getTargetableFilter() {
        return Backstab.filter;
    }

    @Deprecated
    public Backstab(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }
}
