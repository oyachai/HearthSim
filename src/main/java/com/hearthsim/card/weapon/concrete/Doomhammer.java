package com.hearthsim.card.weapon.concrete;


import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.weapon.WeaponCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Doomhammer extends WeaponCard {

    @Override
    protected HearthTreeNode use_core(PlayerSide side, Minion targetMinion, HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1, boolean singleRealizationOnly) throws HSException {
        HearthTreeNode toRet = super.use_core(side, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);
        if (toRet != null) {
            toRet.data_.getHero(side).setWindfury(true);
        }
        return toRet;
    }
}
