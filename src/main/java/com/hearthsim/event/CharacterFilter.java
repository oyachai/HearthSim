package com.hearthsim.event;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public class CharacterFilter {
    protected boolean includeEnemyHero() {
        return false;
    }

    protected boolean includeEnemyMinions() {
        return false;
    }

    protected boolean includeOwnHero() {
        return false;
    }

    protected boolean includeOwnMinions() {
        return false;
    }

    protected boolean excludeSource() {
        return false;
    }

    protected Minion.MinionTribe tribeFilter() {
        return null;
    }

    public boolean targetMatches(PlayerSide originSide, Card origin, PlayerSide targetSide, Minion targetCharacter, BoardModel board) {
        if (this.excludeSource() && targetCharacter == origin) {
            return false;
        }

        if (!this.includeOwnHero() && targetCharacter.isHero() && originSide == targetSide) {
            return false;
        }

        if (!this.includeEnemyHero() && targetCharacter.isHero() && originSide != targetSide) {
            return false;
        }

        if (!this.includeOwnMinions() && !targetCharacter.isHero() && originSide == targetSide) {
            return false;
        }

        if (!this.includeEnemyMinions() && !targetCharacter.isHero() && originSide != targetSide) {
            return false;
        }

        if (this.tribeFilter() != null && targetCharacter.getTribe() != this.tribeFilter()) {
            return false;
        }

        return true;
    }

    public final boolean targetMatches(PlayerSide originSide, Card origin, PlayerSide targetSide, int targetCharacterIndex, BoardModel board) {
        Minion targetCharacter = board.modelForSide(targetSide).getCharacter(targetCharacterIndex);
        return this.targetMatches(originSide, origin, targetSide, targetCharacter, board);
    }

    public final static CharacterFilter ALL = new CharacterFilterTargetedSpell() {
        @Override
        protected boolean includeEnemyHero() { return true; }

        @Override
        protected boolean includeEnemyMinions() { return true; }

        @Override
        protected boolean includeOwnHero() { return true; }

        @Override
        protected boolean includeOwnMinions() { return true; }
    };

    public final static CharacterFilter ALL_ENEMIES = new CharacterFilter() {
        @Override
        protected boolean includeEnemyHero() { return true; }

        @Override
        protected boolean includeEnemyMinions() { return true; }
    };

    public final static CharacterFilter ALL_FRIENDLIES = new CharacterFilter() {
        @Override
        protected boolean includeOwnHero() { return true; }

        @Override
        protected boolean includeOwnMinions() { return true; }
    };

    public final static CharacterFilter ALL_MINIONS = new CharacterFilter() {
        @Override
        protected boolean includeEnemyMinions() { return true; }

        @Override
        protected boolean includeOwnMinions() { return true; }
    };

//    public final static CharacterFilter ALL_HEROES = new CharacterFilter() {
//        @Override
//        protected boolean includeEnemyHero() { return true; }
//
//        @Override
//        protected boolean includeOwnHero() { return true; }
//    };

    public final static CharacterFilter ENEMY_MINIONS = new CharacterFilter() {
        @Override
        protected boolean includeEnemyMinions() { return true; }
    };

    public final static CharacterFilter FRIENDLY_MINIONS = new CharacterFilter() {
        @Override
        protected boolean includeOwnMinions() { return true; }
    };

//    public final static CharacterFilter SELF = new CharacterFilter() {
//        @Override
//        protected boolean includeOwnHero() { return true; }
//    };
//
//    public final static CharacterFilter OPPONENT = new CharacterFilter() {
//        @Override
//        protected boolean includeEnemyHero() { return true; }
//    };
}
