package com.hearthsim.event.filter;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public abstract class FilterCharacterTargetedBattlecry extends FilterCharacter {
    @Override
    protected boolean excludeSource() {
        return true;
    }

    protected Card.CardRarity rarityFilter() {
        return null;
    }

    @Override
    public boolean targetMatches(PlayerSide originSide, Card origin, PlayerSide targetSide, Minion targetCharacter, BoardModel board) {
        if (!super.targetMatches(originSide, origin, targetSide, targetCharacter, board)) {
            return false;
        }

        if (this.rarityFilter() != null && targetCharacter.getRarity() != this.rarityFilter()) {
            return false;
        }

        if (originSide != targetSide && targetCharacter.isStealthed()) {
            return false;
        }

        return true;
    }

    public static final FilterCharacterTargetedBattlecry ALL_MINIONS = new FilterCharacterTargetedBattlecry() {
        protected boolean includeEnemyMinions() {
            return true;
        }
        protected boolean includeOwnMinions() {
            return true;
        }
    };

    public static final FilterCharacterTargetedBattlecry ALL_LEGENDARY_MINIONS = new FilterCharacterTargetedBattlecry() {
        protected boolean includeEnemyMinions() {
            return true;
        }
        protected boolean includeOwnMinions() {
            return true;
        }
        @Override
        protected Card.CardRarity rarityFilter() {
            return Card.CardRarity.LEGENDARY;
        }
    };

    public static final FilterCharacterTargetedBattlecry ALL = new FilterCharacterTargetedBattlecry() {
        protected boolean includeEnemyHero() {
            return true;
        }
        protected boolean includeEnemyMinions() {
            return true;
        }
        protected boolean includeOwnHero() {
            return true;
        }
        protected boolean includeOwnMinions() {
            return true;
        }
    };
}
