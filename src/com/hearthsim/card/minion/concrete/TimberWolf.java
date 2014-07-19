package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Beast;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.tree.HearthTreeNode;

public class TimberWolf extends Beast {

	private static final String NAME = "Timber Wolf";
	private static final byte MANA_COST = 1;
	private static final byte ATTACK = 1;
	private static final byte HEALTH = 1;
	
	private static final boolean TAUNT = false;
	private static final boolean DIVINE_SHIELD = false;
	private static final boolean WINDFURY = false;
	private static final boolean CHARGE = false;
	
	private static final boolean SUMMONED = false;
	private static final boolean TRANSFORMED = false;
	
	public TimberWolf() {
		this(
				MANA_COST,
				ATTACK,
				HEALTH,
				ATTACK,
				(byte)0,
				HEALTH,
				HEALTH,
				TAUNT,
				DIVINE_SHIELD,
				WINDFURY,
				CHARGE,
				false,
				false,
				false,
				SUMMONED,
				TRANSFORMED,
				false,
				false,
				true,
				false
			);
	}
	
	public TimberWolf(	
			byte mana,
			byte attack,
			byte health,
			byte baseAttack,
			byte extraAttackUntilTurnEnd,
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
			boolean destroyOnTurnStart,
			boolean destroyOnTurnEnd,
			boolean isInHand,
			boolean hasBeenUsed) {
		
		super(
			NAME,
			mana,
			attack,
			health,
			baseAttack,
			extraAttackUntilTurnEnd,
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
			destroyOnTurnStart,
			destroyOnTurnEnd,
			isInHand,
			hasBeenUsed);
	}
	
	@Override
	public Object deepCopy() {
		return new TimberWolf(
				this.mana_,
				this.attack_,
				this.health_,
				this.baseAttack_,
				this.extraAttackUntilTurnEnd_,
				this.baseHealth_,
				this.maxHealth_,
				this.taunt_,
				this.divineShield_,
				this.windFury_,
				this.charge_,
				this.hasAttacked_,
				this.hasWindFuryAttacked_,
				this.frozen_,
				this.summoned_,
				this.transformed_,
				this.destroyOnTurnStart_,
				this.destroyOnTurnEnd_,
				this.isInHand_,
				this.hasBeenUsed_);
	}
	
	
	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Override for the temporary buff to attack
	 * 
	 * @param thisCardIndex The index (position) of the card in the hand
	 * @param playerIndex The index of the target player.  0 if targeting yourself or your own minions, 1 if targeting the enemy
	 * @param minionIndex The index of the target minion.
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * 
	 * @return The boardState is manipulated and returned
	 */
	@Override
	protected HearthTreeNode use_core(
			int thisCardIndex,
			int playerIndex,
			int minionIndex,
			HearthTreeNode boardState,
			Deck deck)
		throws HSInvalidPlayerIndexException
	{
		if (hasBeenUsed_) {
			//Card is already used, nothing to do
			return null;
		}
		
		if (playerIndex == 1 || minionIndex == 0)
			return null;
		
		if (boardState.data_.getNumMinions_p0() < 7) {

			if (!charge_) {
				hasAttacked_ = true;
			}
			hasBeenUsed_ = true;
			boardState.data_.placeMinion(0, this, minionIndex - 1);
			boardState.data_.setMana_p0(boardState.data_.getMana_p0() - this.mana_);
			boardState.data_.removeCard_hand(thisCardIndex);
			
			for (Minion minion : boardState.data_.getMinions_p0()) {
				if (minion != this && minion instanceof Beast) {
					minion.setAttack((byte)(minion.getAttack() + 1));
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
	 * @param thisPlayerIndex The player index of this minion
	 * @param thisMinionIndex The minion index of this minion
	 * @param boardState 
	 * @param deck
	 * @throws HSInvalidPlayerIndexException
	 */
	@Override
	public void silenced(int thisPlayerIndex, int thisMinionIndex, HearthTreeNode boardState, Deck deck) throws HSInvalidPlayerIndexException {
		for (Minion minion : boardState.data_.getMinions(thisPlayerIndex)) {
			if (minion != this && minion instanceof Beast) {
				minion.setAttack((byte)(minion.getAttack() - 1));
			}
		}
	}
	
	/**
	 * Called when this minion dies (destroyed)
	 * 
	 * Override for the aura effect
	 * 
	 * @param thisPlayerIndex The player index of this minion
	 * @param thisMinionIndex The minion index of this minion
	 * @param boardState 
	 * @param deck
	 * @throws HSInvalidPlayerIndexException
	 */
	@Override
	public void destroyed(int thisPlayerIndex, int thisMinionIndex, HearthTreeNode boardState, Deck deck) throws HSInvalidPlayerIndexException {
		
		for (Minion minion : boardState.data_.getMinions(thisPlayerIndex)) {
			if (minion != this && minion instanceof Beast) {
				minion.setAttack((byte)(minion.getAttack() - 1));
			}
		}
		super.destroyed(thisPlayerIndex, thisMinionIndex, boardState, deck);
	}
	
	private HearthTreeNode doBuffs(int thisMinionPlayerIndex, int thisMinionIndex, int targetMinionPlayerIndex, int targetMinionIndex, HearthTreeNode boardState, Deck deck) throws HSInvalidPlayerIndexException {
		if (targetMinionPlayerIndex != thisMinionPlayerIndex)
			return boardState;
		Minion minion = boardState.data_.getMinion(thisMinionPlayerIndex, targetMinionIndex - 1);
		if (minion != this && minion instanceof Beast)
			minion.setAttack((byte)(minion.getAttack() + 1));
		return boardState;		
	}
	
	/**
	 * 
	 * Called whenever another minion is placed on board
	 * 
	 * Override for the aura effect
	 *
	 * @param playerIndex The index of the created minion's player.  0 if targeting yourself or your own minions, 1 if targeting the enemy
	 * @param minionIndex The index of the created minion.
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * 
	 * @return The boardState is manipulated and returned
	 */
	@Override
	public HearthTreeNode minionPlacedEvent(int thisMinionPlayerIndex, int thisMinionIndex, int placedMinionPlayerIndex, int placedMinionIndex, HearthTreeNode boardState, Deck deck) throws HSInvalidPlayerIndexException {
		return this.doBuffs(thisMinionPlayerIndex, thisMinionIndex, placedMinionPlayerIndex, placedMinionIndex, boardState, deck);
	}

	/**
	 * 
	 * Called whenever another minion comes on board
	 * 
	 * Override for the aura effect
	 *
	 * @param playerIndex The index of the created minion's player.  0 if targeting yourself or your own minions, 1 if targeting the enemy
	 * @param minionIndex The index of the created minion.
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * 
	 * @return The boardState is manipulated and returned
	 */
	@Override
	public HearthTreeNode minionSummonedEvent(int thisMinionPlayerIndex, int thisMinionIndex, int summonedMinionPlayerIndex, int summeonedMinionIndex, HearthTreeNode boardState, Deck deck) throws HSInvalidPlayerIndexException {
		return this.doBuffs(thisMinionPlayerIndex, thisMinionIndex, summonedMinionPlayerIndex, summeonedMinionIndex, boardState, deck);
	}
	
	/**
	 * 
	 * Called whenever another minion is summoned using a spell
	 * 
	 * @param playerIndex The index of the created minion's player.  0 if targeting yourself or your own minions, 1 if targeting the enemy
	 * @param minionIndex The index of the created minion.
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * 
	 * @return The boardState is manipulated and returned
	 */
	@Override
	public HearthTreeNode minionTransformedEvent(int thisMinionPlayerIndex, int thisMinionIndex, int transformedMinionPlayerIndex, int transformedMinionIndex, HearthTreeNode boardState, Deck deck) throws HSInvalidPlayerIndexException {
		return this.doBuffs(thisMinionPlayerIndex, thisMinionIndex, transformedMinionPlayerIndex, transformedMinionIndex, boardState, deck);
	}
}
