package com.hearthsim.card.classic.minion.legendary;

import com.hearthsim.card.Card;
import com.hearthsim.card.CardPlayBeginInterface;
import com.hearthsim.card.basic.spell.Fireball;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class ArchmageAntonidas extends Minion implements CardPlayBeginInterface {

    public ArchmageAntonidas() {
        super();
    }
    /**
     *
     * Called whenever another card is used
     *
     * When you cast a spell, put a Fireball spell into your hand
     *  @param thisCardPlayerSide The player index of the card receiving the event
     * @param cardUserPlayerSide
     * @param usedCard The card that was used
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     * @param deckPlayer0 The deck of player0
     * @param deckPlayer1 The deck of player1
     * @return The boardState is manipulated and returned
     */
    @Override
    public HearthTreeNode onCardPlayBegin(
            PlayerSide thisCardPlayerSide,
            PlayerSide cardUserPlayerSide,
            Card usedCard,
            HearthTreeNode boardState) {
        if (thisCardPlayerSide != PlayerSide.CURRENT_PLAYER)
            return boardState;
        if (inHand)
            return boardState;
        if (usedCard instanceof SpellCard && boardState.data_.getCurrentPlayer().getHand().size() < 10) {
            boardState.data_.getCurrentPlayer().placeCardHand(new Fireball());
        }
        return boardState;
    }
}
