package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;import com.hearthsim.entity.BaseEntity;
import com.hearthsim.card.minion.Beast;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.CardDrawNode;
import com.hearthsim.util.tree.HearthTreeNode;


public class StarvingBuzzard extends Beast {

	private static final boolean HERO_TARGETABLE = true;
	private static final boolean SUMMONED = false;
	private static final boolean TRANSFORMED = false;
	private static final byte SPELL_DAMAGE = 0;
	
	public StarvingBuzzard() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;
        summoned_ = SUMMONED;
        transformed_ = TRANSFORMED;
	}
	
	/**
	 * 
	 * Called whenever a minion is summoned on the board
	 * 
	 * The buzzard draws a card whenever a Beast is placed on the battlefield
	 * 
	 *
     * @param thisMinionPlayerSide
     * @param summonedMinionPlayerSide
     * @param summonedMinion The summoned minion
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     * @param deckPlayer0 The deck of player0    @return The boardState is manipulated and returned
     * */
	public HearthTreeNode minionSummonedEvent(
			PlayerSide thisMinionPlayerSide,
			PlayerSide summonedMinionPlayerSide,
			Minion summonedMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1)
		throws HSInvalidPlayerIndexException
	{
        if (summonedMinionPlayerSide == PlayerSide.WAITING_PLAYER || thisMinionPlayerSide == PlayerSide.WAITING_PLAYER)
			return boardState;
		
		HearthTreeNode toRet = super.minionSummonedEvent(thisMinionPlayerSide, summonedMinionPlayerSide, summonedMinion, boardState, deckPlayer0, deckPlayer1);
		if (summonedMinion instanceof Beast) { //todo: this might be wrong..
			if (toRet instanceof CardDrawNode) {
				((CardDrawNode) toRet).addNumCardsToDraw(1);
			} else {
				toRet = new CardDrawNode(toRet, 1); //draw one card
			}
		}

		return toRet;
	}
}
