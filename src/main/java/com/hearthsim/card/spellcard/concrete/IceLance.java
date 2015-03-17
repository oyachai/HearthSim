package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class IceLance extends SpellDamage {

    public IceLance() {
        super();
    }

    /**
     * <b>Freeze</b> a character. If it was already <b>Frozen</b>, deal $4 damage instead.
     */
    @Override
    public HearthTreeNode attack(PlayerSide targetMinionPlayerSide, Minion targetMinion, HearthTreeNode boardState,
                                 Deck deckPlayer0, Deck deckPlayer1) throws HSException {
        HearthTreeNode toRet = boardState;
        if (targetMinion.getFrozen()) {
            toRet = super.attack(targetMinionPlayerSide, targetMinion, toRet, deckPlayer0, deckPlayer1);
        } else {
            targetMinion.setFrozen(true);
        }
        return toRet;
    }
}
