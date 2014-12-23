package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionPlacedInterface;
import com.hearthsim.exception.HSException;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;


public class RaidLeader extends Minion implements MinionPlacedInterface {

	private static final boolean HERO_TARGETABLE = true;
	private static final byte SPELL_DAMAGE = 0;
	
	public RaidLeader() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;

	}
	
	/**
	 * Aura: +1 Attack to all friendly minions
	 * 
     * @param targetSide
     * @param targetMinion The target minion (can be a Hero).  The new minion is always placed to the right of (higher index) the target minion.  If the target minion is a hero, then it is placed at the left-most position.
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     * @return The boardState is manipulated and returned
	 * @throws HSException 
	 */
	@Override
	public HearthTreeNode placeMinion(
            PlayerSide targetSide,
			Minion targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1,
			boolean singleRealizationOnly)
		throws HSException
	{		
		HearthTreeNode toRet = super.placeMinion(targetSide, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);
		if (toRet != null) {
			for (Minion minion : targetSide.getPlayer(toRet).getMinions()) {
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
	@Override
	public void silenced(PlayerSide thisPlayerSide, BoardModel boardState) throws HSInvalidPlayerIndexException {
		for (Minion minion : boardState.getMinions(thisPlayerSide)) {
			if (minion != this) {
				minion.setAuraAttack((byte)(minion.getAuraAttack() - 1));
			}
		}
		super.silenced(thisPlayerSide, boardState);
	}
		
	private HearthTreeNode doBuffs(
            PlayerSide thisMinionPlayerSide,
            PlayerSide placedMinionPlayerSide,
            Minion placedMinion,
            HearthTreeNode boardState) {
		if (thisMinionPlayerSide != placedMinionPlayerSide)
			return boardState;
        if (!silenced_ && placedMinion != this) {
            placedMinion.setAuraAttack((byte) (placedMinion.getAuraAttack() + 1));
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
     * @param summonedMinion The summoned minion
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     * @param deckPlayer0 The deck of player0    @return The boardState is manipulated and returned
     * */
	@Override
	public HearthTreeNode minionPlacedEvent(
			PlayerSide thisMinionPlayerSide,
			PlayerSide summonedMinionPlayerSide,
			Minion summonedMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1)
	{
		return this.doBuffs(thisMinionPlayerSide, summonedMinionPlayerSide, summonedMinion, boardState);
	}
	
}
