package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;import com.hearthsim.entity.BaseEntity;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.Murloc;
import com.hearthsim.exception.HSException;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;


public class GrimscaleOracle extends Murloc {

	private static final boolean HERO_TARGETABLE = true;
	private static final boolean SUMMONED = false;
	private static final boolean TRANSFORMED = false;
	private static final byte SPELL_DAMAGE = 0;
	
	public GrimscaleOracle() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;
        summoned_ = SUMMONED;
        transformed_ = TRANSFORMED;
	}
	
	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Override for the temporary buff to attack
	 * 
	 *
     *
     * @param targetSide
     * @param targetMinion The target minion (can be a Hero).  The new minion is always placed to the right of (higher index) the target minion.  If the target minion is a hero, then it is placed at the left-most position.
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     * @return The boardState is manipulated and returned
	 * @throws HSException 
	 */
	protected HearthTreeNode summonMinion_core(
            PlayerSide targetSide,
			BaseEntity targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1)
		throws HSException
	{
		HearthTreeNode toRet = super.summonMinion_core(targetSide, targetMinion, boardState, deckPlayer0, deckPlayer1);
		if (toRet != null) {
			
			for (BaseEntity minion : PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getMinions()) {
				if (minion instanceof Murloc && minion != this) {
					minion.setAuraAttack((byte)(minion.getAuraAttack() + 1));
				}
			}
			
			for (BaseEntity minion : PlayerSide.WAITING_PLAYER.getPlayer(toRet).getMinions()) {
				if (minion instanceof Murloc && minion != this) {
					minion.setAuraAttack((byte)(minion.getAuraAttack() + 1));
				}
			}

			return boardState;

		} else {
			return null;
		}
	}
	
	/**
	 * Called when this minion is silenced
	 * 
	 * Override for the aura effect
	 * 
	 *
     *
     * @param thisPlayerSide
     * @param boardState
     * @throws HSInvalidPlayerIndexException
	 */
	
	public void silenced(PlayerSide thisPlayerSide, BoardModel boardState) throws HSInvalidPlayerIndexException {
		if (!silenced_) {
			for (BaseEntity minion : PlayerSide.CURRENT_PLAYER.getPlayer(boardState).getMinions()) {
				if (minion instanceof Murloc && minion != this) {
					minion.setAuraAttack((byte)(minion.getAuraAttack() - 1));
				}
			}
			for (BaseEntity minion : PlayerSide.WAITING_PLAYER.getPlayer(boardState).getMinions()) {
				if (minion instanceof Murloc && minion != this) {
					minion.setAuraAttack((byte)(minion.getAuraAttack() - 1));
				}
			}
		}
		this.silenced(thisPlayerSide, boardState);
	}
		
	private HearthTreeNode doBuffs(
            BaseEntity targetMinion,
            HearthTreeNode boardState)
		throws HSInvalidPlayerIndexException
	{
        if (!silenced_ && targetMinion instanceof Murloc && targetMinion != this) {
            targetMinion.setAuraAttack((byte) (targetMinion.getAuraAttack() + 1));
        }
        return boardState;
	}

	/**
	 * 
	 * Called whenever another minion comes on board
	 * 
	 * Override for the aura effect
	 *
	 *
     * @param thisMinionPlayerSide
     * @param summonedMinionPlayerSide
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     * @return The boardState is manipulated and returned
	 */
	
	public HearthTreeNode minionSummonedEvent(
			PlayerSide thisMinionPlayerSide,
			PlayerSide summonedMinionPlayerSide,
			BaseEntity summonedMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1)
		throws HSInvalidPlayerIndexException
	{
		return this.doBuffs(summonedMinion, boardState);
	}
	
	/**
	 * 
	 * Called whenever another minion is summoned using a spell
	 * 
	 *  @param thisMinionPlayerSide
     * @param transformedMinionPlayerSide
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     * @return The boardState is manipulated and returned
	 */
	
	public HearthTreeNode minionTransformedEvent(
			PlayerSide thisMinionPlayerSide,
			PlayerSide transformedMinionPlayerSide,
			BaseEntity transformedMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1)
		throws HSInvalidPlayerIndexException
	{
		return this.doBuffs(transformedMinion, boardState);
	}
}
