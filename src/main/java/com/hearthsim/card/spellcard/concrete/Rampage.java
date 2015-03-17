package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Rampage extends SpellCard {

    public Rampage() {
        super();

        this.canTargetEnemyHero = false;
        this.canTargetOwnHero = false;
    }

    @Override
    public boolean canBeUsedOn(PlayerSide playerSide, Minion minion, BoardModel boardModel) {
        if (!super.canBeUsedOn(playerSide, minion, boardModel)) {
            return false;
        }

        if (minion.getHealth() == minion.getMaxHealth()) {
            return false;
        }

        return true;
    }

    @Override
    protected HearthTreeNode use_core(
        PlayerSide side,
        Minion targetMinion,
        HearthTreeNode boardState,
        Deck deckPlayer0,
        Deck deckPlayer1,
        boolean singleRealizationOnly)
        throws HSException {
        HearthTreeNode toRet = super.use_core(side, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);
        if (toRet != null) {
            targetMinion.setAttack((byte)(targetMinion.getAttack() + 3));
            targetMinion.setHealth((byte)(targetMinion.getHealth() + 3));
            targetMinion.setMaxHealth((byte)(targetMinion.getMaxHealth() + 3));
        }
        return toRet;
    }
}
