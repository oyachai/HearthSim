package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Bananas extends SpellCard {


    @Deprecated
    public Bananas(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }


    public Bananas() {
        super();

        this.canTargetEnemyHero = false;
        this.canTargetOwnHero = false;
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
            targetMinion.addAttack((byte)1);
            targetMinion.addHealth((byte)1);
            targetMinion.addMaxHealth((byte)1);
        }
        return toRet;
    }
}
