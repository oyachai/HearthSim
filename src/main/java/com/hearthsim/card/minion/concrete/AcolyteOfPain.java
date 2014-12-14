package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.CardDrawNode;
import com.hearthsim.util.tree.HearthTreeNode;

public class AcolyteOfPain extends Minion {

	private static final boolean HERO_TARGETABLE = true;
	private static final byte SPELL_DAMAGE = 0;
	
	public AcolyteOfPain() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;

	}

	/**
	 * Called when this minion takes damage
	 * 
	 * Draw a card whenever this minion takes damage
	 *  @param damage The amount of damage to take
	 * @param attackPlayerSide The player index of the attacker.  This is needed to do things like +spell damage.
     * @param thisPlayerSide
     * @param boardState
     * @param isSpellDamage True if this is a spell damage   @throws HSException
     * */
	@Override
	public HearthTreeNode takeDamage(
			byte damage,
			PlayerSide attackPlayerSide,
			PlayerSide thisPlayerSide,
			HearthTreeNode boardState,
			Deck deckPlayer0, 
			Deck deckPlayer1,
			boolean isSpellDamage,
			boolean handleMinionDeath)
		throws HSException
	{
		if (!divineShield_) {
			HearthTreeNode toRet = super.takeDamage(damage, attackPlayerSide, thisPlayerSide, boardState, deckPlayer0, deckPlayer1, isSpellDamage, handleMinionDeath);
			if (damage > 0 && thisPlayerSide == PlayerSide.CURRENT_PLAYER) {
				if (toRet instanceof CardDrawNode) {
					((CardDrawNode) toRet).addNumCardsToDraw(1);
				} else {
					toRet = new CardDrawNode(toRet, 1); //draw one card
				}
			} else if (damage > 0) {
				//This minion is an enemy minion.  Let's draw a card for the enemy.  No need to use a StopNode for enemy card draws.
				toRet.data_.drawCardFromWaitingPlayerDeck(1);
			}
			return toRet;
		} else {
			divineShield_ = false;
			return boardState;
		}
	}
}
