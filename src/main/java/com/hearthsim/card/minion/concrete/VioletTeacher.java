package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.CardPlayBeginInterface;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class VioletTeacher extends Minion implements CardPlayBeginInterface {

	private static final boolean HERO_TARGETABLE = true;
	private static final byte SPELL_DAMAGE = 0;
	
	public VioletTeacher() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;

	}
	
	/**
	 * 
	 * Called whenever another card is used
	 * 
	 * When you cast a spell, summon a 1/1 Violet Apprentice
	 *  @param thisCardPlayerSide The player index of the card receiving the event
	 * @param cardUserPlayerSide
     * @param usedCard The card that was used
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     * @param deckPlayer0 The deck of player0
     * @param deckPlayer1 The deck of player1
     * @return The boardState is manipulated and returned
	 * @throws HSException 
	 */
	@Override
	public HearthTreeNode onCardPlayBegin(
			PlayerSide thisCardPlayerSide,
			PlayerSide cardUserPlayerSide,
			Card usedCard,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1,
			boolean singleRealizationOnly)
		throws HSException
	{
		HearthTreeNode toRet = boardState;
		if (thisCardPlayerSide != PlayerSide.CURRENT_PLAYER)
			return toRet;
		if (isInHand_)
			return toRet;
        if (usedCard instanceof SpellCard && PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getNumMinions() < 7) {
            Minion newMinion = new VioletApprentice();
            toRet = newMinion.summonMinion(thisCardPlayerSide, this, toRet, deckPlayer0, deckPlayer1, false, singleRealizationOnly);
        }
        return toRet;
	}

}
