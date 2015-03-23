package com.hearthsim.event;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public class MinionFilterSpellTargetable extends MinionFilter {
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

    public final static MinionFilterSpellTargetable ALL = new MinionFilterSpellTargetable() {
        @Override
        protected boolean includeEnemyHero() { return true; }

        @Override
        protected boolean includeEnemyMinions() { return true; }

        @Override
        protected boolean includeOwnHero() { return true; }

        @Override
        protected boolean includeOwnMinions() { return true; }
    };

    public final static MinionFilterSpellTargetable ALL_ENEMIES = new MinionFilterSpellTargetable() {
        @Override
        protected boolean includeEnemyHero() { return true; }

        @Override
        protected boolean includeEnemyMinions() { return true; }
    };

    public final static MinionFilterSpellTargetable ALL_FRIENDLIES = new MinionFilterSpellTargetable() {
        @Override
        protected boolean includeOwnHero() { return true; }

        @Override
        protected boolean includeOwnMinions() { return true; }
    };

    public final static MinionFilterSpellTargetable ALL_MINIONS = new MinionFilterSpellTargetable() {
        @Override
        protected boolean includeEnemyMinions() { return true; }

        @Override
        protected boolean includeOwnMinions() { return true; }
    };

    public final static MinionFilterSpellTargetable ALL_HEROES = new MinionFilterSpellTargetable() {
        @Override
        protected boolean includeEnemyHero() { return true; }

        @Override
        protected boolean includeOwnHero() { return true; }
    };

    public final static MinionFilterSpellTargetable ENEMY_MINIONS = new MinionFilterSpellTargetable() {
        @Override
        protected boolean includeEnemyMinions() { return true; }
    };

    public final static MinionFilterSpellTargetable FRIENDLY_MINIONS = new MinionFilterSpellTargetable() {
        @Override
        protected boolean includeOwnMinions() { return true; }
    };

    public final static MinionFilterSpellTargetable SELF = new MinionFilterSpellTargetable() {
        @Override
        protected boolean includeOwnHero() { return true; }
    };

    public final static MinionFilterSpellTargetable OPPONENT = new MinionFilterSpellTargetable() {
        @Override
        protected boolean includeEnemyHero() { return true; }
    };
}
