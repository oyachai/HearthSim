package com.hearthsim.event.filter;

import com.hearthsim.card.Card;
import com.hearthsim.card.CharacterIndex;
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

    protected int maxAttack() {
        return -1;
    }

    protected int minAttack() {
        return -1;
    }

    protected boolean excludeSource() {
        return false;
    }

    protected boolean includeOrigin() {
        return false;
    }

    protected Minion.MinionTribe tribeFilter() {
        return null;
    }

    protected Minion.MinionTribe excludeTribe() {
        return null;
    }

    public boolean targetMatches(PlayerSide originSide, Card origin, PlayerSide targetSide, Minion targetCharacter, BoardModel board) {

        if (this.includeOrigin() && targetCharacter == origin) {
            return true;
        }

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

        if (this.excludeTribe() != null && targetCharacter.getTribe() == this.excludeTribe()) {
            return false;
        }

        if (this.maxAttack() >= 0 && targetCharacter.getTotalAttack(board, targetSide) > this.maxAttack()) {
            return false;
        }

        if (this.minAttack() >= 0 && targetCharacter.getTotalAttack(board, targetSide) < this.minAttack()) {
            return false;
        }

        return true;
    }

    public final boolean targetMatches(PlayerSide originSide, Card origin, PlayerSide targetSide, CharacterIndex targetCharacterIndex, BoardModel board) {
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

    public final static FilterCharacter ALL_NON_DEMONS = new FilterCharacter() {
        @Override
        protected boolean includeEnemyMinions() {
            return true;
        }

        @Override
        protected boolean includeOwnMinions() {
            return true;
        }

        @Override
        protected Minion.MinionTribe excludeTribe() {
            return Minion.MinionTribe.DEMON;
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

    public final static FilterCharacter FRIENDLY_OTHER_MINIONS = new FilterCharacter() {
        @Override
        protected boolean includeOwnMinions() {
            return true;
        }
        @Override
        protected boolean excludeSource() {
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

    public final static FilterCharacter ORIGIN = new FilterCharacter() {
        @Override
        protected boolean includeOrigin() {
            return true;
        }
    };
}
