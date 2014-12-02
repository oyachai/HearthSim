package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;import com.hearthsim.entity.BaseEntity;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.CardDrawNode;
import com.hearthsim.util.tree.HearthTreeNode;


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

	private static final boolean HERO_TARGETABLE = true;
	private static final boolean SUMMONED = false;
	private static final boolean TRANSFORMED = false;
	private static final byte SPELL_DAMAGE = 0;
	
	public NorthshireCleric() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;
        summoned_ = SUMMONED;
        transformed_ = TRANSFORMED;
	}
	
	/**
	 * 
	 * Called whenever another character (including the hero) is healed
	 * 
	 *
     * @param thisMinionPlayerSide
     * @param healedMinionPlayerSide
     * @param healedMinion The healed minion
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     * @param deckPlayer0 The deck of player0
     * @param deckPlayer1 The deck of player1
     * @return The boardState is manipulated and returned
	 */
	public HearthTreeNode minionHealedEvent(
			PlayerSide thisMinionPlayerSide,
			PlayerSide healedMinionPlayerSide,
			BaseEntity healedMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1)
		throws HSInvalidPlayerIndexException
	{
		HearthTreeNode toRet = boardState;
		if (!silenced_) {
			if (thisMinionPlayerSide == PlayerSide.CURRENT_PLAYER) {
				if (boardState instanceof CardDrawNode) {
					((CardDrawNode)toRet).addNumCardsToDraw(1);
				} else {
					toRet = new CardDrawNode(toRet, 1); //draw one card
				}
			} else {
				//This minion is an enemy minion.  Let's draw a card for the enemy.  No need to use a StopNode for enemy card draws.
				toRet.data_.drawCardFromWaitingPlayerDeck(deckPlayer1, 1);
			}
		}
		return toRet;
	}
}
