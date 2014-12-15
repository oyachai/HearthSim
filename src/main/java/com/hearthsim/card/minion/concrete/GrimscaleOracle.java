package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionPlacedInterface;
import com.hearthsim.exception.HSException;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;


public class GrimscaleOracle extends Minion implements MinionPlacedInterface {

	private static final boolean HERO_TARGETABLE = true;
	private static final byte SPELL_DAMAGE = 0;
	
	public GrimscaleOracle() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;

        this.tribe = MinionTribe.MURLOC;
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
			for (Minion minion : PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getMinions()) {
				if (minion.getTribe() == MinionTribe.MURLOC && minion != this) {
					minion.setAuraAttack((byte)(minion.getAuraAttack() + 1));
				}
			}
			
			for (Minion minion : PlayerSide.WAITING_PLAYER.getPlayer(toRet).getMinions()) {
				if (minion.getTribe() == MinionTribe.MURLOC && minion != this) {
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
     * @throws HSInvalidPlayerIndexException
	 */
	@Override
	public void silenced(PlayerSide thisPlayerSide, BoardModel boardState) throws HSInvalidPlayerIndexException {
		if (!silenced_) {
			for (Minion minion : PlayerSide.CURRENT_PLAYER.getPlayer(boardState).getMinions()) {
				if (minion.getTribe() == MinionTribe.MURLOC && minion != this) {
					minion.setAuraAttack((byte)(minion.getAuraAttack() - 1));
				}
			}
			for (Minion minion : PlayerSide.WAITING_PLAYER.getPlayer(boardState).getMinions()) {
				if (minion.getTribe() == MinionTribe.MURLOC && minion != this) {
					minion.setAuraAttack((byte)(minion.getAuraAttack() - 1));
				}
			}
		}
		this.silenced(thisPlayerSide, boardState);
	}
		
	private HearthTreeNode doBuffs(
            Minion targetMinion,
            HearthTreeNode boardState)
	{
        if (!silenced_ && targetMinion.getTribe() == MinionTribe.MURLOC && targetMinion != this) {
            targetMinion.setAuraAttack((byte) (targetMinion.getAuraAttack() + 1));
        }
        return boardState;
	}

	@Override
	public HearthTreeNode minionPlacedEvent(
			PlayerSide thisMinionPlayerSide,
			PlayerSide summonedMinionPlayerSide,
			Minion summonedMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1)
	{
		HearthTreeNode toRet = boardState;
		return this.doBuffs(summonedMinion, toRet);
	}	
}
