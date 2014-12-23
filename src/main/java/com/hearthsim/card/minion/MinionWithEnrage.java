package com.hearthsim.card.minion;

import com.hearthsim.card.Deck;
import com.hearthsim.exception.HSException;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public abstract class MinionWithEnrage extends Minion {

    protected boolean enraged_;

    protected MinionWithEnrage() {
        super();
    }

	@Override
	public Minion deepCopy() {
        MinionWithEnrage minionWithEnrage = (MinionWithEnrage) super.deepCopy();
        minionWithEnrage.enraged_ = enraged_;
        return  minionWithEnrage;
    }
	
	/**
	 * Turn on enrage
	 * 
	 */
	public abstract void enrage();
	
	/**
	 * Turn off enrage
	 */
	public abstract void pacify();
	
	
	/**
	 * Called when this minion takes damage
	 * 
	 * Override for enrage
	 *  @param damage The amount of damage to take
	 * @param attackPlayerSide The player index of the attacker.  This is needed to do things like +spell damage.
     * @param thisPlayerSide
     * @param boardState
     * @param deckPlayer0 The deck of player0
     * @param isSpellDamage True if this is a spell damage
     * @throws HSInvalidPlayerIndexException
	 */
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
	{		HearthTreeNode toRet = super.takeDamage(damage, attackPlayerSide, thisPlayerSide, boardState, deckPlayer0, deckPlayer1, isSpellDamage, handleMinionDeath);
		this.enrageCheck();
		return toRet;
	}
	
	/**
	 * Called when this minion is healed
	 * 
	 * Always use this function to heal minions
	 *  @param healAmount The amount of healing to take
	 * @param thisPlayerSide
     * @param boardState
     * @param deckPlayer0 The deck of player0   @throws HSInvalidPlayerIndexException
     * */
	@Override
	public HearthTreeNode takeHeal(byte healAmount, PlayerSide thisPlayerSide, HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1) throws HSException {
		HearthTreeNode toRet = super.takeHeal(healAmount, thisPlayerSide, boardState, deckPlayer0, deckPlayer1);
		this.enrageCheck();
		return toRet;
	}
	
	/**
	 * Called when this minion is silenced
	 * 
	 * Always use this function to "silence" minions
	 * 
	 *
     *
     * @param thisPlayerSide
     * @param boardState
     * @param deckPlayer0 The deck of player0
     * @throws HSInvalidPlayerIndexException
	 */
	@Override
	public void silenced(PlayerSide thisPlayerSide, BoardModel boardState) throws HSInvalidPlayerIndexException {
		super.silenced(thisPlayerSide, boardState);
		if (enraged_)
			this.pacify();
	}
	
	private void enrageCheck() {
		if (!silenced_) {
			if (health_ < maxHealth_ && !enraged_) {
				enraged_ = true;
				this.enrage();
			} else if (health_ == maxHealth_ && enraged_) {
				enraged_ = false;
				this.pacify();
			}
		}
	}
}
