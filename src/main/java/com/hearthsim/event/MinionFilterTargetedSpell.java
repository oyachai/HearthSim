package com.hearthsim.event;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public class MinionFilterTargetedSpell extends MinionFilter {
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

    public final static MinionFilterTargetedSpell ALL = new MinionFilterTargetedSpell() {
        @Override
        protected boolean includeEnemyHero() { return true; }

        @Override
        protected boolean includeEnemyMinions() { return true; }

        @Override
        protected boolean includeOwnHero() { return true; }

        @Override
        protected boolean includeOwnMinions() { return true; }
    };

    public final static MinionFilterTargetedSpell ALL_ENEMIES = new MinionFilterTargetedSpell() {
        @Override
        protected boolean includeEnemyHero() { return true; }

        @Override
        protected boolean includeEnemyMinions() { return true; }
    };

    public final static MinionFilterTargetedSpell ALL_FRIENDLIES = new MinionFilterTargetedSpell() {
        @Override
        protected boolean includeOwnHero() { return true; }

        @Override
        protected boolean includeOwnMinions() { return true; }
    };

    public final static MinionFilterTargetedSpell ALL_MINIONS = new MinionFilterTargetedSpell() {
        @Override
        protected boolean includeEnemyMinions() { return true; }

        @Override
        protected boolean includeOwnMinions() { return true; }
    };

    public final static MinionFilterTargetedSpell ALL_HEROES = new MinionFilterTargetedSpell() {
        @Override
        protected boolean includeEnemyHero() { return true; }

        @Override
        protected boolean includeOwnHero() { return true; }
    };

    public final static MinionFilterTargetedSpell ENEMY_MINIONS = new MinionFilterTargetedSpell() {
        @Override
        protected boolean includeEnemyMinions() { return true; }
    };

    public final static MinionFilterTargetedSpell FRIENDLY_MINIONS = new MinionFilterTargetedSpell() {
        @Override
        protected boolean includeOwnMinions() { return true; }
    };

    public final static MinionFilterTargetedSpell SELF = new MinionFilterTargetedSpell() {
        @Override
        protected boolean includeOwnHero() { return true; }
    };

    public final static MinionFilterTargetedSpell OPPONENT = new MinionFilterTargetedSpell() {
        @Override
        protected boolean includeEnemyHero() { return true; }
    };
}
