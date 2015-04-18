package com.hearthsim.event.filter;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public class FilterCharacter implements FilterCharacterInterface {
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

    public final static FilterCharacter ALL = new FilterCharacterTargetedSpell() {
        @Override
        protected boolean includeEnemyHero() {
            return true;
        }

        @Override
        protected boolean includeEnemyMinions() {
            return true;
        }

        @Override
        protected boolean includeOwnHero() {
            return true;
        }

        @Override
        protected boolean includeOwnMinions() {
            return true;
        }
    };

    public final static FilterCharacter ALL_ENEMIES = new FilterCharacter() {
        @Override
        protected boolean includeEnemyHero() {
            return true;
        }

        @Override
        protected boolean includeEnemyMinions() {
            return true;
        }
    };

    public final static FilterCharacter ALL_FRIENDLIES = new FilterCharacter() {
        @Override
        protected boolean includeOwnHero() {
            return true;
        }

        @Override
        protected boolean includeOwnMinions() {
            return true;
        }
    };

    public final static FilterCharacter ALL_MINIONS = new FilterCharacter() {
        @Override
        protected boolean includeEnemyMinions() {
            return true;
        }

        @Override
        protected boolean includeOwnMinions() {
            return true;
        }
    };

    public final static FilterCharacter ALL_HEROES = new FilterCharacter() {
        @Override
        protected boolean includeEnemyHero() {
            return true;
        }

        @Override
        protected boolean includeOwnHero() {
            return true;
        }
    };

    public final static FilterCharacter ENEMY_MINIONS = new FilterCharacter() {
        @Override
        protected boolean includeEnemyMinions() {
            return true;
        }
    };

    public final static FilterCharacter FRIENDLY_MINIONS = new FilterCharacter() {
        @Override
        protected boolean includeOwnMinions() {
            return true;
        }
    };

    public final static FilterCharacter SELF = new FilterCharacter() {
        @Override
        protected boolean includeOwnHero() {
            return true;
        }
    };

    public final static FilterCharacter OPPONENT = new FilterCharacter() {
        @Override
        protected boolean includeEnemyHero() {
            return true;
        }
    };
}
