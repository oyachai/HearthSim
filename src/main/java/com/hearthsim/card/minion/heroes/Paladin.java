package com.hearthsim.card.minion.heroes;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.SilverHandRecruit;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Paladin extends Hero {

    @Override
    public boolean canBeUsedOn(PlayerSide playerSide, Minion minion, BoardModel boardModel) {
        if(!super.canBeUsedOn(playerSide, minion, boardModel)) {
            return false;
        }
        if (playerSide != PlayerSide.CURRENT_PLAYER) {
            return false;
        }
        if (boardModel.modelForSide(playerSide).isBoardFull()) {
            return false;
        }

        return true;
    }

    /**
     * Use the hero ability on a given target
     *
     * Paladin: summon a 1/1 Silver Hand Recruit
     *
     *
     *
     * @param targetPlayerSide
     * @param boardState
     * @return
     */
    @Override
    public HearthTreeNode useHeroAbility_core(
            PlayerSide targetPlayerSide,
            Minion targetMinion,
            HearthTreeNode boardState,
            Deck deckPlayer0,
            Deck deckPlayer1,
            boolean singleRealizationOnly)
        throws HSException {

        HearthTreeNode toRet = boardState;

        this.hasBeenUsed = true;
        PlayerModel currentPlayer = toRet.data_.getCurrentPlayer();
        currentPlayer.subtractMana(HERO_ABILITY_COST);

        Minion theRecruit = new SilverHandRecruit();
        toRet = theRecruit.summonMinionAtEnd(targetPlayerSide, toRet, deckPlayer0, deckPlayer1, false, singleRealizationOnly);
        return toRet;
    }
}
