package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.BoardState;
import com.hearthsim.util.HearthTreeNode;

/**
 * Northshire Cleric
 * 
 * @author oyachai
 *
 * This minion is a 1 mana, 1 attack, 3 health minion.
 * Whenever a minion is healed, this minion draws a card for its player.
 *
 */
public class NorthshireCleric extends Minion {

	public NorthshireCleric() {
		this(
				(byte)1,
				(byte)1,
				(byte)3,
				(byte)1,
				(byte)3,
				(byte)3,
				false,
				false,
				false,
				false,
				false,
				false,
				false,
				false,
				false,
				true,
				false
			);
	}
	
	public NorthshireCleric(	
							byte mana,
							byte attack,
							byte health,
							byte baseAttack,
							byte baseHealth,
							byte maxHealth,
							boolean taunt,
							boolean divineShield,
							boolean windFury,
							boolean charge,
							boolean hasAttacked,
							boolean hasWindFuryAttacked,
							boolean frozen,
							boolean summoned,
							boolean transformed,
							boolean isInHand,
							boolean hasBeenUsed) {
		
		super(
			"Northshire Cleric",
			mana,
			attack,
			health,
			baseAttack,
			baseHealth,
			maxHealth,
			taunt,
			divineShield,
			windFury,
			charge,
			hasAttacked,
			hasWindFuryAttacked,
			frozen,
			summoned,
			transformed,
			isInHand,
			hasBeenUsed);
	}
	
	
	/**
	 * 
	 * Called whenever another character (including the hero) is healed
	 * 
	 * @param playerIndex The index of the healed minion's player.  0 if targeting yourself or your own minions, 1 if targeting the enemy
	 * @param minionIndex The index of the healed minion.
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * 
	 * @return The boardState is manipulated and returned
	 */
	public HearthTreeNode<BoardState> minionHealedEvent(
			int thisMinionPlayerIndex,
			int thisMinionIndex,
			int healedMinionPlayerIndex,
			int healedMinionIndex,
			HearthTreeNode<BoardState> boardState,
			Deck deck)
		throws HSInvalidPlayerIndexException
	{

		Card card = deck.drawCard(boardState.data_.getDeckPos(thisMinionPlayerIndex));
		if (card == null) {
			byte fatigueDamage = boardState.data_.getFatigueDamage(thisMinionPlayerIndex);
			boardState.data_.setFatigueDamage(thisMinionPlayerIndex, (byte)(fatigueDamage + 1));
			boardState.data_.getHero(thisMinionPlayerIndex).setHealth((byte)(boardState.data_.getHero(thisMinionPlayerIndex).getHealth() - fatigueDamage));
		} else {
			boardState.data_.placeCard_hand(thisMinionPlayerIndex, card);
			boardState.data_.setDeckPos(thisMinionPlayerIndex, boardState.data_.getDeckPos(thisMinionPlayerIndex) + 1);
		}
	
		
		return boardState;
	}
}
