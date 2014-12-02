package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;import com.hearthsim.entity.BaseEntity;
import com.hearthsim.card.minion.Beast;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;


public class Leokk extends Beast {

	private static final boolean HERO_TARGETABLE = true;
	private static final boolean SUMMONED = true;
	private static final boolean TRANSFORMED = false;
	private static final byte SPELL_DAMAGE = 0;
	
	public Leokk() {
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
     * @param side
     * @param targetMinion The target minion
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     *
     * @return The boardState is manipulated and returned
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
				if (minion != this) {
					minion.setAuraAttack((byte)(minion.getAuraAttack() + 1));
				}
			}			
		}
		return toRet;
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
     * @param deckPlayer0
     * @param deckPlayer1
     * @throws HSInvalidPlayerIndexException
	 */
	
	public void silenced(PlayerSide thisPlayerSide, BoardModel boardState) throws HSInvalidPlayerIndexException {
		for (BaseEntity minion : boardState.getMinions(thisPlayerSide)) {
			if (minion != this) {
				minion.setAuraAttack((byte)(minion.getAuraAttack() - 1));
			}
		}
		super.silenced(thisPlayerSide, boardState);
	}
		
	private HearthTreeNode doBuffs(
            PlayerSide thisMinionPlayerSide,
            PlayerSide placedMinionPlayerSide,
            BaseEntity placedMinion,
            HearthTreeNode boardState) throws HSInvalidPlayerIndexException {
		if (thisMinionPlayerSide != placedMinionPlayerSide)
			return boardState;
		if (placedMinion != this)
			placedMinion.setAuraAttack((byte)(placedMinion.getAuraAttack() + 1));
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
     * @param summonedMinion The summoned minion
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     * @param deckPlayer0 The deck of player0    @return The boardState is manipulated and returned
     * */
	public HearthTreeNode minionSummonedEvent(
			PlayerSide thisMinionPlayerSide,
			PlayerSide summonedMinionPlayerSide,
			BaseEntity summonedMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1)
		throws HSInvalidPlayerIndexException
	{
		return this.doBuffs(thisMinionPlayerSide, summonedMinionPlayerSide, summonedMinion, boardState);
	}
	
	/**
	 * 
	 * Called whenever another minion is summoned using a spell
	 *  @param thisMinionPlayerSide The player index of this minion
	 * @param transformedMinionPlayerSide
     * @param transformedMinion The transformed minion (the minion that resulted from a transformation)
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     * @param deckPlayer0 The deck of player0    @return The boardState is manipulated and returned
     * */
	public HearthTreeNode minionTransformedEvent(
			PlayerSide thisMinionPlayerSide,
			PlayerSide transformedMinionPlayerSide,
			BaseEntity transformedMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1)
		throws HSInvalidPlayerIndexException
	{
		return this.doBuffs(thisMinionPlayerSide, transformedMinionPlayerSide, transformedMinion, boardState);
	}

}
