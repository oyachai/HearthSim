package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.HearthAction;
import com.hearthsim.util.IdentityLinkedList;
import com.hearthsim.util.tree.HearthTreeNode;
import com.hearthsim.util.tree.RandomEffectNode;

public class Soulfire extends SpellDamage {

	public Soulfire() {
		this(false);
	}

	public Soulfire(boolean hasBeenUsed) {
		super((byte)0, (byte)4, hasBeenUsed);
	}

	@Override
	public Object deepCopy() {
		return new Soulfire(this.hasBeenUsed);
	}
	
	/**
	 * 
	 * Discards a random card
	 * 
     * @param side
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     *
     * @return The boardState is manipulated and returned
	 */
	@Override
	public HearthTreeNode useOn(
			PlayerSide side,
			Minion targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1,
			boolean singleRealizationOnly)
		throws HSException
	{
		HearthTreeNode toRet = null;
		
		if (singleRealizationOnly) {
			toRet = super.useOn(side, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);
			IdentityLinkedList<Card> hand = PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getHand();
			if (hand.size() > 0) {
				Card targetCard = hand.get((int)(Math.random() * hand.size()));
				toRet.data_.removeCard_hand(targetCard);
			}
		} else {
			int targetCharacterIndex = targetMinion instanceof Hero ? 0 : side.getPlayer(boardState).getMinions().indexOf(targetMinion) + 1;
			int thisCardIndex = side.getPlayer(boardState).getHand().indexOf(this);
			toRet = new RandomEffectNode(boardState, new HearthAction(HearthAction.Verb.USE_CARD, PlayerSide.CURRENT_PLAYER, 0, side, targetCharacterIndex));
			for (int indx = 0; indx < toRet.data_.getCurrentPlayerHand().size(); ++indx) {
				if (indx != thisCardIndex) {
					HearthTreeNode cNode = new HearthTreeNode((BoardModel)toRet.data_.deepCopy());
					Minion cTargetMinion = cNode.data_.getCurrentPlayerCharacter(targetCharacterIndex);
					Soulfire cCard = (Soulfire)cNode.data_.getCurrentPlayerHand().get(thisCardIndex);
					cNode = cCard.callSuperUseOn(side, cTargetMinion, cNode, deckPlayer0, deckPlayer1, false);
					if (cNode != null) {
						cNode.data_.removeCard_hand(cNode.data_.getCurrentPlayerHand().get(indx < thisCardIndex ? indx : indx - 1));
						toRet.addChild(cNode);
					}
				}
			}
		}
		return toRet;
	}
	
	//To do: find a better way to do this...
	private HearthTreeNode callSuperUseOn(
			PlayerSide side,
			Minion targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1,
			boolean singleRealizationOnly)
		throws HSException
	{
		return super.useOn(side, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);
	}	
}
