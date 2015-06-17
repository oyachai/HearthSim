package com.hearthsim.event.filter;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public class FilterHand implements FilterHandInterface {
    protected boolean includeEnemyHand() {
        return false;
    }

    protected boolean includeOwnHand() {
        return false;
    }

    protected Class<? extends Card> classFilter() {
        return null;
    }

    protected Minion.MinionTribe tribeFilter() {
        return null;
    }

    protected boolean excludeSource() {
        return true;
    }

    @Override
    public boolean targetMatches(PlayerSide originSide, Card origin, PlayerSide targetSide, Card targetCard, BoardModel board) {
        if (!targetCard.setInHand()) {
            return false; // shouldn't happen
        }

        if (this.excludeSource() && origin == targetCard) { // need reference check to avoid duplicates
            return false;
        }

        if (!this.includeEnemyHand() && originSide != targetSide) {
            return false;
        }

        if (!this.includeOwnHand() && originSide == targetSide) {
            return false;
        }

        if (this.classFilter() != null && !this.classFilter().isAssignableFrom(targetCard.getClass())) {
            return false;
        }

        // TODO could be moved into something like FilterHandMinion
        if (this.tribeFilter() != null && targetCard instanceof Minion && ((Minion) targetCard).getTribe() != this.tribeFilter()) {
            return false;
        }

        return true;
    }

    public static final FilterHand OWN = new FilterHand() {
        @Override
        protected boolean includeOwnHand() {
            return true;
        }
    };

    public static final FilterHand ALL_MINIONS = new FilterHand() {
        @Override
        protected boolean includeEnemyHand() {
            return true;
        }

        @Override
        protected boolean includeOwnHand() {
            return true;
        }

        @Override
        protected Class<? extends Minion> classFilter() {
            return Minion.class;
        }
    };

    public static final FilterHand FRIENDLY_MINIONS = new FilterHand() {
        @Override
        protected boolean includeOwnHand() {
            return true;
        }

        @Override
        protected Class<? extends Minion> classFilter() {
            return Minion.class;
        }
    };

    public static final FilterHand FRIENDLY_SPELLS = new FilterHand() {
        @Override
        protected boolean includeOwnHand() {
            return true;
        }

        @Override
        protected Class<? extends SpellCard> classFilter() {
            return SpellCard.class;
        }
    };
}
