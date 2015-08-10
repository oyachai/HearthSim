package com.hearthsim.event.filter;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public class FilterCharacterTargetedSpell extends FilterCharacter {
    @Override
    public boolean targetMatches(PlayerSide originSide, Card origin, PlayerSide targetSide, Minion targetCharacter, BoardModel board) {
        // do some early checking for very common conditions before calling super
        if (origin.hasBeenUsed()) {
            return false;
        }

        if (!super.targetMatches(originSide, origin, targetSide, targetCharacter, board)) {
            return false;
        }

        if (originSide != targetSide && targetCharacter.isStealthed()) {
            return false;
        }

        if (!targetCharacter.isHeroTargetable()) {
            return false;
        }

        return true;
    }

    public final static FilterCharacterTargetedSpell ALL = new FilterCharacterTargetedSpell() {
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

    public final static FilterCharacterTargetedSpell ALL_ENEMIES = new FilterCharacterTargetedSpell() {
        @Override
        protected boolean includeEnemyHero() {
            return true;
        }

        @Override
        protected boolean includeEnemyMinions() {
            return true;
        }
    };

    public final static FilterCharacterTargetedSpell ALL_FRIENDLIES = new FilterCharacterTargetedSpell() {
        @Override
        protected boolean includeOwnHero() {
            return true;
        }

        @Override
        protected boolean includeOwnMinions() {
            return true;
        }
    };

    public final static FilterCharacterTargetedSpell ALL_MINIONS = new FilterCharacterTargetedSpell() {
        @Override
        protected boolean includeEnemyMinions() {
            return true;
        }

        @Override
        protected boolean includeOwnMinions() {
            return true;
        }
    };

//    public final static CharacterFilterTargetedSpell ALL_HEROES = new CharacterFilterTargetedSpell() {
//        @Override
//        protected boolean includeEnemyHero() {
//            return true;
//        }
//
//        @Override
//        protected boolean includeOwnHero() {
//            return true;
//        }
//    };

    public final static FilterCharacterTargetedSpell ENEMY_MINIONS = new FilterCharacterTargetedSpell() {
        @Override
        protected boolean includeEnemyMinions() {
            return true;
        }
    };

    public final static FilterCharacterTargetedSpell FRIENDLY_MINIONS = new FilterCharacterTargetedSpell() {
        @Override
        protected boolean includeOwnMinions() {
            return true;
        }
    };

    public final static FilterCharacterTargetedSpell SELF = new FilterCharacterTargetedSpell() {
        @Override
        protected boolean includeOwnHero() {
            return true;
        }
    };

    public final static FilterCharacterTargetedSpell OPPONENT = new FilterCharacterTargetedSpell() {
        @Override
        protected boolean includeEnemyHero() {
            return true;
        }
    };
}
