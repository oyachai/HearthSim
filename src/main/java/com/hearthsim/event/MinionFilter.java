package com.hearthsim.event;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public class MinionFilter {
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

    protected Minion.MinionTribe tribeFilter() {
        return null;
    }

    public boolean targetMatches(PlayerSide originSide, Card origin, PlayerSide targetSide, Minion targetCharacter, BoardModel board) {
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

    public final static MinionFilter ALL = new MinionFilterTargetedSpell() {
        @Override
        protected boolean includeEnemyHero() { return true; }

        @Override
        protected boolean includeEnemyMinions() { return true; }

        @Override
        protected boolean includeOwnHero() { return true; }

        @Override
        protected boolean includeOwnMinions() { return true; }
    };

    public final static MinionFilter ALL_ENEMIES = new MinionFilter() {
        @Override
        protected boolean includeEnemyHero() { return true; }

        @Override
        protected boolean includeEnemyMinions() { return true; }
    };

    public final static MinionFilter ALL_FRIENDLIES = new MinionFilter() {
        @Override
        protected boolean includeOwnHero() { return true; }

        @Override
        protected boolean includeOwnMinions() { return true; }
    };

    public final static MinionFilter ALL_MINIONS = new MinionFilter() {
        @Override
        protected boolean includeEnemyMinions() { return true; }

        @Override
        protected boolean includeOwnMinions() { return true; }
    };

    public final static MinionFilter ALL_HEROES = new MinionFilter() {
        @Override
        protected boolean includeEnemyHero() { return true; }

        @Override
        protected boolean includeOwnHero() { return true; }
    };

    public final static MinionFilter ENEMY_MINIONS = new MinionFilter() {
        @Override
        protected boolean includeEnemyMinions() { return true; }
    };

    public final static MinionFilter FRIENDLY_MINIONS = new MinionFilter() {
        @Override
        protected boolean includeOwnMinions() { return true; }
    };

    public final static MinionFilter SELF = new MinionFilter() {
        @Override
        protected boolean includeOwnHero() { return true; }
    };

    public final static MinionFilter OPPONENT = new MinionFilter() {
        @Override
        protected boolean includeEnemyHero() { return true; }
    };
}
