package com.hearthsim.card.classic.minion.rare;

import com.hearthsim.card.Card;
import com.hearthsim.card.CardPlayBeginInterface;
import com.hearthsim.card.classic.minion.common.VioletApprentice;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class VioletTeacher extends Minion implements CardPlayBeginInterface {

    public VioletTeacher() {
        super();
    }

    /**
     * Called whenever another card is used
     * <p>
     * When you cast a spell, summon a 1/1 Violet Apprentice
     *
     * @param thisCardPlayerSide The player index of the card receiving the event
     * @param cardUserPlayerSide
     * @param usedCard           The card that was used
     * @param boardState         The BoardState before this card has performed its action.  It will be manipulated and returned.
     * @return The boardState is manipulated and returned
     */
    @Override
    public HearthTreeNode onCardPlayBegin(
        PlayerSide thisCardPlayerSide,
        PlayerSide cardUserPlayerSide,
        Card usedCard,
        HearthTreeNode boardState) {
        HearthTreeNode toRet = boardState;
        if (thisCardPlayerSide != PlayerSide.CURRENT_PLAYER)
            return toRet;
        if (inHand)
            return toRet;
        PlayerModel currentPlayer = toRet.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
        if (usedCard instanceof SpellCard && !currentPlayer.isBoardFull()) {
            Minion newMinion = new VioletApprentice();
            toRet = newMinion.summonMinion(thisCardPlayerSide, this, toRet, false);
        }
        return toRet;
    }
}
