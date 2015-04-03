package com.hearthsim.event;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public class CharacterFilterTargetedSpell extends CharacterFilter {
    @Override
    public boolean targetMatches(PlayerSide originSide, Card origin, PlayerSide targetSide, Minion targetCharacter, BoardModel board) {
        // do some early checking for very common conditions before calling super
        if (origin.hasBeenUsed()) {
            return false;
        }

        if (!super.targetMatches(originSide, origin, targetSide, targetCharacter, board)) {
            return false;
        }

        if (originSide != targetSide && targetCharacter.getStealthed()) {
            return false;
        }

        if (!targetCharacter.isHeroTargetable()) {
            return false;
        }

        return true;
    }

    public final static CharacterFilterTargetedSpell ALL = new CharacterFilterTargetedSpell() {
        @Override
        protected boolean includeEnemyHero() { return true; }

        @Override
        protected boolean includeEnemyMinions() { return true; }

        @Override
        protected boolean includeOwnHero() { return true; }

        @Override
        protected boolean includeOwnMinions() { return true; }
    };

    public final static CharacterFilterTargetedSpell ALL_ENEMIES = new CharacterFilterTargetedSpell() {
        @Override
        protected boolean includeEnemyHero() { return true; }

        @Override
        protected boolean includeEnemyMinions() { return true; }
    };

    public final static CharacterFilterTargetedSpell ALL_FRIENDLIES = new CharacterFilterTargetedSpell() {
        @Override
        protected boolean includeOwnHero() { return true; }

        @Override
        protected boolean includeOwnMinions() { return true; }
    };

    public final static CharacterFilterTargetedSpell ALL_MINIONS = new CharacterFilterTargetedSpell() {
        @Override
        protected boolean includeEnemyMinions() { return true; }

        @Override
        protected boolean includeOwnMinions() { return true; }
    };

//    public final static CharacterFilterTargetedSpell ALL_HEROES = new CharacterFilterTargetedSpell() {
//        @Override
//        protected boolean includeEnemyHero() { return true; }
//
//        @Override
//        protected boolean includeOwnHero() { return true; }
//    };

    public final static CharacterFilterTargetedSpell ENEMY_MINIONS = new CharacterFilterTargetedSpell() {
        @Override
        protected boolean includeEnemyMinions() { return true; }
    };

    public final static CharacterFilterTargetedSpell FRIENDLY_MINIONS = new CharacterFilterTargetedSpell() {
        @Override
        protected boolean includeOwnMinions() { return true; }
    };

    public final static CharacterFilterTargetedSpell SELF = new CharacterFilterTargetedSpell() {
        @Override
        protected boolean includeOwnHero() { return true; }
    };

    public final static CharacterFilterTargetedSpell OPPONENT = new CharacterFilterTargetedSpell() {
        @Override
        protected boolean includeEnemyHero() { return true; }
    };
}
