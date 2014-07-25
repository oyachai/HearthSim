package com.hearthsim.card.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.BoardState;
import com.hearthsim.util.tree.HearthTreeNode;
import com.json.JSONObject;

public class Minion extends Card {
	
	protected boolean taunt_;
	protected boolean divineShield_;
	protected boolean windFury_;
	protected boolean charge_;
	
	protected boolean hasAttacked_;
	protected boolean hasWindFuryAttacked_;
	
	protected boolean frozen_;
	protected boolean silenced_;
	
	protected byte health_;
	protected byte maxHealth_;
	protected byte baseHealth_;
	
	protected byte attack_;
	protected byte baseAttack_;
	protected byte extraAttackUntilTurnEnd_;
	
	protected boolean summoned_;
	protected boolean transformed_;
	
	protected boolean destroyOnTurnStart_;
	protected boolean destroyOnTurnEnd_;

	public Minion(String name, byte mana, byte attack, byte health, byte baseAttack, byte baseHealth, byte maxHealth) {
		this(
				name,
				mana,
				attack,
				health,
				baseAttack,
				baseHealth,
				maxHealth,
				false,
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
				false);
	}
	
	public Minion(	String name,
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
					boolean silenced,
					boolean summoned,
					boolean transformed,
					boolean isInHand,
					boolean hasBeenUsed)
	{
		this(
				name,
				mana,
				attack,
				health,
				baseAttack,
				(byte)0,
				baseHealth,
				maxHealth,
				taunt,
				divineShield,
				windFury,
				charge,
				hasAttacked,
				hasWindFuryAttacked,
				frozen,
				silenced,
				summoned,
				transformed,
				false,
				false,
				isInHand,
				hasBeenUsed);
	}
	
	public Minion(	String name,
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
					boolean silenced,
					boolean summoned,
					boolean transformed,
					boolean destroyOnTurnStart,
					boolean destroyOnTurnEnd,
					boolean isInHand,
					boolean hasBeenUsed) {
		super(name, mana, hasBeenUsed, isInHand);
		attack_ = attack;
		health_ = health;
		taunt_ = taunt;
		divineShield_ = divineShield;
		windFury_ = windFury;
		charge_ = charge;
		hasAttacked_ = hasAttacked;
		baseAttack_ = baseAttack;
		extraAttackUntilTurnEnd_ = extraAttackUntilTurnEnd;
		hasWindFuryAttacked_ = hasWindFuryAttacked;
		frozen_ = frozen;
		silenced_ = silenced;
		baseHealth_ = baseHealth;
		maxHealth_ = maxHealth;
		summoned_ = summoned;
		transformed_ = transformed;
		destroyOnTurnStart_ = destroyOnTurnStart;
		destroyOnTurnEnd_ = destroyOnTurnEnd;
	}
	
	public boolean getTaunt() {
		return taunt_;
	}
	
	public void setTaunt(boolean taunt) {
		taunt_ = taunt;
	}
	
	public byte getHealth() {
		return health_;
	}
	
	public void setHealth(byte health) {
		health_ = health;
	}
	
	public byte getMaxHealth() {
		return maxHealth_;
	}
	
	public void setMaxHealth(byte health) {
		maxHealth_ = health;
	}
	
	public byte getBaseHealth() {
		return baseHealth_;
	}
	
	public void setBaseHealth(byte health) {
		baseHealth_ = health;
	}
	
	public byte getAttack() {
		return attack_;
	}
	
	public void setAttack(byte attack) {
		attack_ = attack;
	}
	
	public boolean getDivineShield() {
		return divineShield_;
	}
	
	public void setDivineShield(boolean divineShield) {
		divineShield_ = divineShield;
	}
	
	public boolean hasAttacked() {
		return hasAttacked_;
	}
	
	public void hasAttacked(boolean hasAttacked) {
		hasAttacked_ = hasAttacked;
	}

	public boolean hasWindFuryAttacked() {
		return hasWindFuryAttacked_;
	}
	
	public void hasWindFuryAttacked(boolean hasAttacked) {
		hasWindFuryAttacked_ = hasAttacked;
	}

	public boolean getCharge() {
		return charge_;
	}
	
	public void setCharge(boolean value) {
		charge_ = value;
	}
	
	public boolean getFrozen() {
		return frozen_;
	}
	
	public void setFrozen(boolean value) {
		frozen_ = value;
	}
	
	public boolean getSummoned() {
		return summoned_;
	}
	
	public void setSummoned(boolean value) {
		summoned_ = value;
	}
	
	public boolean getTransformed() {
		return transformed_;
	}
	
	public void setTransformed(boolean value) {
		transformed_ = value;
	}
	
	public byte getExtraAttackUntilTurnEnd() {
		return extraAttackUntilTurnEnd_;
	}
	
	public void setExtraAttackUntilTurnEnd(byte value) {
		extraAttackUntilTurnEnd_ = value;
	}
	
	public boolean getDestroyOnTurnStart() {
		return destroyOnTurnStart_;
	}
	
	public void setDestroyOnTurnStart(boolean value) {
		destroyOnTurnStart_ = value;
	}

	public boolean getDestroyOnTurnEnd() {
		return destroyOnTurnEnd_;
	}
	
	public void setDestroyOnTurnEnd(boolean value) {
		destroyOnTurnEnd_ = value;
	}

	/**
	 * Called at the start of the turn
	 * 
	 * This function is called at the start of the turn.  Any derived class must override it to implement whatever
	 * "start of the turn" effect the card has.
	 */
	@Override
	public BoardState startTurn(int thisMinionPlayerIndex, int thisMinionIndex, BoardState boardState, Deck deckPlayer0, Deck deckPlayer1) throws HSInvalidPlayerIndexException {
		if (destroyOnTurnStart_) {
			this.destroyed(thisMinionPlayerIndex, thisMinionIndex, new HearthTreeNode(boardState), deckPlayer0, deckPlayer1);
		}
		return boardState;
	}
	
	/**
	 * End the turn and resets the card state
	 * 
	 * This function is called at the end of the turn.  Any derived class must override it and remove any 
	 * temporary buffs that it has.
	 * 
	 * This is not the most efficient implementation... luckily, endTurn only happens once per turn
	 */
	@Override
	public BoardState endTurn(int thisMinionPlayerIndex, int thisMinionIndex, BoardState boardState, Deck deckPlayer0, Deck deckPlayer1) throws HSInvalidPlayerIndexException {
		extraAttackUntilTurnEnd_ = 0;
		if (destroyOnTurnEnd_) {
			this.destroyed(thisMinionPlayerIndex, thisMinionIndex, new HearthTreeNode(boardState), deckPlayer0, deckPlayer1);
		}
		return boardState;
	}
	
	/**
	 * Called when this minion takes damage
	 * 
	 * Always use this function to take damage... it properly notifies all others of its damage and possibly of its death
	 * 
	 * @param damage The amount of damage to take
	 * @param attackerPlayerIndex The player index of the attacker.  This is needed to do things like +spell damage.
	 * @param thisPlayerIndex The player index of this minion
	 * @param thisMinionIndex The minion index of this minion
	 * @param boardState 
	 * @param deck
	 * @throws HSInvalidPlayerIndexException
	 */
	public HearthTreeNode takeDamage(byte damage, int attackerPlayerIndex, int thisPlayerIndex, int thisMinionIndex, HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1) throws HSInvalidPlayerIndexException {
		return this.takeDamage(damage, attackerPlayerIndex, thisPlayerIndex, thisMinionIndex, boardState, deckPlayer0, deckPlayer1, false);
	}
	
	/**
	 * Called when this minion takes damage
	 * 
	 * Always use this function to take damage... it properly notifies all others of its damage and possibly of its death
	 * 
	 * @param damage The amount of damage to take
	 * @param attackerPlayerIndex The player index of the attacker.  This is needed to do things like +spell damage.
	 * @param thisPlayerIndex The player index of this minion
	 * @param thisMinionIndex The minion index of this minion
	 * @param boardState 
	 * @param deck
	 * @param isSpellDamage True if this is a spell damage
	 * @throws HSInvalidPlayerIndexException
	 */
	public HearthTreeNode takeDamage(byte damage, int attackerPlayerIndex, int thisPlayerIndex, int thisMinionIndex, HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1, boolean isSpellDamage) throws HSInvalidPlayerIndexException {
		if (!divineShield_) {
			byte totalDamage = isSpellDamage ? (byte)(damage + boardState.data_.getSpellDamage(attackerPlayerIndex)) : damage;
			health_ = (byte)(health_ - totalDamage);
			
			//Notify all that the minion is damaged
			HearthTreeNode toRet = boardState;
			toRet = toRet.data_.getHero_p0().minionDamagedEvent(0, 0, thisPlayerIndex, thisMinionIndex, toRet, deckPlayer0, deckPlayer1);
			for (int j = 0; j < toRet.data_.getNumMinions_p0(); ++j) {
				if (!toRet.data_.getMinion_p0(j).silenced_)
					toRet = toRet.data_.getMinion_p0(j).minionDamagedEvent(0, j + 1, thisPlayerIndex, thisMinionIndex, toRet, deckPlayer0, deckPlayer1);
			}
			toRet = toRet.data_.getHero_p1().minionDamagedEvent(1, 0, thisPlayerIndex, thisMinionIndex, toRet, deckPlayer0, deckPlayer1);
			for (int j = 0; j < toRet.data_.getNumMinions_p1(); ++j) {
				if (!toRet.data_.getMinion_p1(j).silenced_)
					toRet = toRet.data_.getMinion_p1(j).minionDamagedEvent(1, j + 1, thisPlayerIndex, thisMinionIndex, toRet, deckPlayer0, deckPlayer1);
			}
			
			//If fatal, notify all that it is dead
			if (health_ <= 0) {
				toRet = this.destroyed(thisPlayerIndex, thisMinionIndex, toRet, deckPlayer0, deckPlayer1);
			}
			return toRet;
		} else {
			if (damage > 0)
				divineShield_ = false;
			return boardState;
		}
	}
	
	/**
	 * Called when this minion dies (destroyed)
	 * 
	 * Always use this function to "kill" minions
	 * 
	 * @param thisPlayerIndex The player index of this minion
	 * @param thisMinionIndex The minion index of this minion
	 * @param boardState 
	 * @param deck
	 * @throws HSInvalidPlayerIndexException
	 */
	public HearthTreeNode destroyed(int thisPlayerIndex, int thisMinionIndex, HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1) throws HSInvalidPlayerIndexException {
		
		health_ = 0;
		
		HearthTreeNode toRet = boardState;
		//Notify all that it is dead
		toRet = toRet.data_.getHero_p0().minionDeadEvent(0, 0, thisPlayerIndex, thisMinionIndex, toRet, deckPlayer0, deckPlayer1);
		for (int j = 0; j < toRet.data_.getNumMinions_p0(); ++j) {
			if (!toRet.data_.getMinion_p0(j).silenced_)
				toRet = toRet.data_.getMinion_p0(j).minionDeadEvent(0, j + 1, thisPlayerIndex, thisMinionIndex, toRet, deckPlayer0, deckPlayer1);
		}
		toRet = toRet.data_.getHero_p1().minionDeadEvent(1, 0, thisPlayerIndex, thisMinionIndex, toRet, deckPlayer0, deckPlayer1);
		for (int j = 0; j < toRet.data_.getNumMinions_p1(); ++j) {
			if (!toRet.data_.getMinion_p1(j).silenced_)
				toRet = toRet.data_.getMinion_p1(j).minionDeadEvent(1, j + 1, thisPlayerIndex, thisMinionIndex, toRet, deckPlayer0, deckPlayer1);
		}
		
		return toRet;

	}
	
	/**
	 * Called when this minion is silenced
	 * 
	 * Always use this function to "silence" minions
	 * 
	 * @param thisPlayerIndex The player index of this minion
	 * @param thisMinionIndex The minion index of this minion
	 * @param boardState 
	 * @param deck
	 * @throws HSInvalidPlayerIndexException
	 */
	public HearthTreeNode silenced(int thisPlayerIndex, int thisMinionIndex, HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1) throws HSInvalidPlayerIndexException {
		divineShield_ = false;
		taunt_ = false;
		charge_ = false;
		frozen_ = false;
		windFury_ = false;
		silenced_ = true;
		return boardState;
	}
	
	/**
	 * Called when this minion is healed
	 * 
	 * Always use this function to heal minions
	 * 
	 * @param healAmount The amount of healing to take
	 * @param thisPlayerIndex The player index of this minion
	 * @param thisMinionIndex The minion index of this minion
	 * @param boardState 
	 * @param deck
	 * @throws HSInvalidPlayerIndexException
	 */
	public HearthTreeNode takeHeal(byte healAmount, int thisPlayerIndex, int thisMinionIndex, HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1) throws HSInvalidPlayerIndexException {
		
		if (health_ < maxHealth_) {
			if (health_ + healAmount > maxHealth_)
				health_ = maxHealth_;
			else
				health_ = (byte)(health_ + healAmount);
			
			//Notify all that it the minion is healed
			HearthTreeNode toRet = boardState;
			toRet = toRet.data_.getHero_p0().minionHealedEvent(0, 0, thisPlayerIndex, thisMinionIndex, toRet, deckPlayer0, deckPlayer1);
			for (int j = 0; j < toRet.data_.getNumMinions_p0(); ++j) {
				if (!toRet.data_.getMinion_p0(j).silenced_)
					toRet = toRet.data_.getMinion_p0(j).minionHealedEvent(0, j + 1, thisPlayerIndex, thisMinionIndex, toRet, deckPlayer0, deckPlayer1);
			}
			toRet = toRet.data_.getHero_p1().minionHealedEvent(1, 0, thisPlayerIndex, thisMinionIndex, toRet, deckPlayer0, deckPlayer1);
			for (int j = 0; j < toRet.data_.getNumMinions_p1(); ++j) {
				if (!toRet.data_.getMinion_p1(j).silenced_)
					toRet = toRet.data_.getMinion_p1(j).minionHealedEvent(1, j + 1, thisPlayerIndex, thisMinionIndex, toRet, deckPlayer0, deckPlayer1);
			}
			return toRet;
		}
		return boardState;
	}
	
	/**
	 * 
	 * Use the card on the given target
	 * 
	 * @param thisCardIndex The index (position) of the card in the hand
	 * @param playerIndex The index of the target player.  0 if targeting yourself or your own minions, 1 if targeting the enemy
	 * @param minionIndex The index of the target minion.
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * 
	 * @return The boardState is manipulated and returned
	 */
	@Override
	public HearthTreeNode useOn(
			int thisCardIndex,
			int playerIndex,
			int minionIndex,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1)
		throws HSInvalidPlayerIndexException
	{
		//A generic card does nothing except for consuming mana
		HearthTreeNode toRet = this.use_core(thisCardIndex, playerIndex, minionIndex, boardState, deckPlayer0, deckPlayer1);
		
		if (toRet != null) {
			//Notify all other cards/characters of the card's use
			if (!transformed_) {
				for (int j = 0; j < toRet.data_.getNumCards_hand(); ++j) {
					toRet = toRet.data_.getCard_hand_p0(j).otherCardUsedEvent(j, toRet, deckPlayer0, deckPlayer1);
				}
				toRet = toRet.data_.getHero_p0().otherCardUsedEvent(0, toRet, deckPlayer0, deckPlayer1);
				for (int j = 0; j < toRet.data_.getNumMinions_p0(); ++j) {
					if (!toRet.data_.getMinion_p0(j).silenced_)
						toRet = toRet.data_.getMinion_p0(j).otherCardUsedEvent(j, toRet, deckPlayer0, deckPlayer1);
				}
				toRet = toRet.data_.getHero_p1().otherCardUsedEvent(0, toRet, deckPlayer0, deckPlayer1);
				for (int j = 0; j < toRet.data_.getNumMinions_p1(); ++j) {
					if (!toRet.data_.getMinion_p1(j).silenced_)
						toRet = toRet.data_.getMinion_p1(j).otherCardUsedEvent(j, toRet, deckPlayer0, deckPlayer1);
				}
			}
			if (summoned_) {
				//Notify all that a minion is created
				toRet = toRet.data_.getHero_p0().minionSummonedEvent(0, 0, playerIndex, minionIndex, toRet, deckPlayer0, deckPlayer1);
				for (int j = 0; j < toRet.data_.getNumMinions_p0(); ++j) {
					if (!toRet.data_.getMinion_p0(j).silenced_)
						toRet = toRet.data_.getMinion_p0(j).minionSummonedEvent(0, j + 1, playerIndex, minionIndex, toRet, deckPlayer0, deckPlayer1);
				}
				toRet = toRet.data_.getHero_p1().minionSummonedEvent(1, 0, playerIndex, minionIndex, toRet, deckPlayer0, deckPlayer1);
				for (int j = 0; j < toRet.data_.getNumMinions_p1(); ++j) {
					if (!toRet.data_.getMinion_p1(j).silenced_)
						toRet = toRet.data_.getMinion_p1(j).minionSummonedEvent(1, j + 1, playerIndex, minionIndex, toRet, deckPlayer0, deckPlayer1);
				}
			} else {
				//Notify all that a minion is created
				toRet = toRet.data_.getHero_p0().minionPlacedEvent(0, 0, playerIndex, minionIndex, toRet, deckPlayer0, deckPlayer1);
				for (int j = 0; j < toRet.data_.getNumMinions_p0(); ++j) {
					if (!toRet.data_.getMinion_p0(j).silenced_)
						toRet = toRet.data_.getMinion_p0(j).minionPlacedEvent(0, j + 1, playerIndex, minionIndex, toRet, deckPlayer0, deckPlayer1);
				}
				toRet = toRet.data_.getHero_p1().minionPlacedEvent(1, 0, playerIndex, minionIndex, toRet, deckPlayer0, deckPlayer1);
				for (int j = 0; j < toRet.data_.getNumMinions_p1(); ++j) {
					if (!toRet.data_.getMinion_p1(j).silenced_)
						toRet = toRet.data_.getMinion_p1(j).minionPlacedEvent(1, j + 1, playerIndex, minionIndex, toRet, deckPlayer0, deckPlayer1);
				}
			}
		}
		
		return toRet;
	}
	
	/**
	 * 
	 * Use the card on the given target
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
			Deck deckPlayer0,
			Deck deckPlayer1)
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
			return boardState;
							
		} else {
			return null;
		}

	}
	
	/**
	 * 
	 * Attack with the minion
	 * 
	 * @param thisMinionIndex Attacking minion's index (note: attacking player index is assumed to be 0)
	 * @param playerIndex The index of the target player.  0 if targeting yourself or your own minions, 1 if targeting the enemy
	 * @param minionIndex The index of the target minion.
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * 
	 * @return The boardState is manipulated and returned
	 */
	public HearthTreeNode attack(
			int thisMinionIndex,
			int playerIndex,
			int minionIndex,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1)
		throws HSInvalidPlayerIndexException
	{
		
		if (frozen_) {
			this.hasAttacked_ = true;
			this.frozen_ = false;
			return boardState;
		}
		
		//Notify all that an attack is beginning
		HearthTreeNode toRet = boardState;
		if (toRet != null) {
			//Notify all that a minion is created
			toRet = toRet.data_.getHero_p0().minionAttackingEvent(0, thisMinionIndex, playerIndex, minionIndex, toRet, deckPlayer0, deckPlayer1);
			for (int j = 0; j < toRet.data_.getNumMinions_p0(); ++j) {
				if (!toRet.data_.getMinion_p0(j).silenced_)
					toRet = toRet.data_.getMinion_p0(j).minionAttackingEvent(0, thisMinionIndex, playerIndex, minionIndex, toRet, deckPlayer0, deckPlayer1);
			}
			toRet = toRet.data_.getHero_p1().minionAttackingEvent(0, thisMinionIndex, playerIndex, minionIndex, toRet, deckPlayer0, deckPlayer1);
			for (int j = 0; j < toRet.data_.getNumMinions_p1(); ++j) {
				if (!toRet.data_.getMinion_p1(j).silenced_)
					toRet = toRet.data_.getMinion_p1(j).minionAttackingEvent(0, thisMinionIndex, playerIndex, minionIndex, toRet, deckPlayer0, deckPlayer1);
			}
		}
		
		//Do the actual attack
		toRet = this.attack_core(thisMinionIndex, playerIndex, minionIndex, boardState, deckPlayer0, deckPlayer1);
		
		return toRet;
	}

	/**
	 * 
	 * Attack with the minion
	 * 
	 * @param thisMinionIndex Attacking minion's index (note: attacking player index is assumed to be 0)
	 * @param playerIndex The index of the target player.  0 if targeting yourself or your own minions, 1 if targeting the enemy
	 * @param minionIndex The index of the target minion.
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * 
	 * @return The boardState is manipulated and returned
	 */
	protected HearthTreeNode attack_core(
			int thisMinionIndex,
			int playerIndex,
			int minionIndex,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1)
		throws HSInvalidPlayerIndexException
	{
		
		if (hasAttacked_) {
			//minion has already attacked
			return null;
		}
		
		if (playerIndex == 0) {
			return null;
		}
		
		HearthTreeNode toRet = boardState;
		if (minionIndex == 0) {
			//attack the enemy hero
			toRet.data_.getHero_p1().takeDamage((byte)(this.attack_ + this.extraAttackUntilTurnEnd_), 0, playerIndex, minionIndex, toRet, deckPlayer0, deckPlayer1);
			toRet = this.takeDamage(toRet.data_.getHero_p1().attack_, playerIndex, 0, thisMinionIndex, toRet, deckPlayer0, deckPlayer1);
			if (windFury_ && !hasWindFuryAttacked_)
				hasWindFuryAttacked_ = true;
			else
				hasAttacked_ = true;
			return toRet;
		} else {
			Minion target = toRet.data_.getMinion_p1(minionIndex - 1);
			byte origAttack = target.attack_;
			toRet = target.takeDamage((byte)(this.attack_ + this.extraAttackUntilTurnEnd_), 0, playerIndex, minionIndex, toRet, deckPlayer0, deckPlayer1);
			toRet = this.takeDamage(origAttack, playerIndex, 0, thisMinionIndex, toRet, deckPlayer0, deckPlayer1);
			if (target.getHealth() <= 0) {
				toRet.data_.removeMinion_p1(minionIndex - 1);
			}
			if (health_ <= 0) {
				toRet.data_.removeMinion_p0(thisMinionIndex - 1);
			}
			if (windFury_ && !hasWindFuryAttacked_)
				hasWindFuryAttacked_ = true;
			else
				hasAttacked_ = true;
			return toRet;
		}

	}


	
	//======================================================================================
	// Hooks for various events
	//======================================================================================	

	/**
	 * 
	 * Called whenever another minion comes on board
	 * 
	 * @param playerIndex The index of the created minion's player.  0 if targeting yourself or your own minions, 1 if targeting the enemy
	 * @param minionIndex The index of the created minion.
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * 
	 * @return The boardState is manipulated and returned
	 */
	public HearthTreeNode minionPlacedEvent(
			int thisMinionPlayerIndex,
			int thisMinionIndex,
			int placedMinionPlayerIndex,
			int placedMinionIndex,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1)
		throws HSInvalidPlayerIndexException
	{
		return boardState;
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
	public HearthTreeNode minionSummonedEvent(
			int thisMinionPlayerIndex,
			int thisMinionIndex,
			int summonedMinionPlayerIndex,
			int summeonedMinionIndex,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1)
		throws HSInvalidPlayerIndexException
	{
		return boardState;
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
	public HearthTreeNode minionTransformedEvent(
			int thisMinionPlayerIndex,
			int thisMinionIndex,
			int transformedMinionPlayerIndex,
			int transformedMinionIndex,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1)
		throws HSInvalidPlayerIndexException
	{
		return boardState;
	}

	/**
	 * 
	 * Called whenever another minion is attacked
	 * 
	 * @param attackingPlayerIndex The index of the attacking player.  0 if targeting yourself or your own minions, 1 if targeting the enemy
	 * @param attackingMinionIndex The index of the attacking minion.
	 * @param targetPlayerIndex The target player's index
	 * @param targetMinionIndex The target minion's index
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * 
	 * @return The boardState is manipulated and returned
	 */
	public HearthTreeNode minionAttackedEvent(
			int attackingPlayerIndex,
			int attackingMinionIndex,
			int targetPlayerIndex,
			int targetMinionIndex,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1)
		throws HSInvalidPlayerIndexException
	{
		return boardState;
	}

	/**
	 * 
	 * Called whenever another minion is attacking another character
	 * 
	 * @param attackingPlayerIndex The index of the attacking player.  0 if targeting yourself or your own minions, 1 if targeting the enemy
	 * @param attackingMinionIndex The index of the attacking minion.
	 * @param targetPlayerIndex The target player's index
	 * @param targetMinionIndex The target minion's index
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * 
	 * @return The boardState is manipulated and returned
	 */
	public HearthTreeNode minionAttackingEvent(
			int attackingPlayerIndex,
			int attackingMinionIndex,
			int targetPlayerIndex,
			int targetMinionIndex,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1)
		throws HSInvalidPlayerIndexException
	{
		return boardState;
	}
	
	/**
	 * 
	 * Called whenever another minion is damaged
	 * 
	 * @param playerIndex The index of the damaged minion's player.  0 if targeting yourself or your own minions, 1 if targeting the enemy
	 * @param minionIndex The index of the damaged minion.
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * 
	 * @return The boardState is manipulated and returned
	 */
	public HearthTreeNode minionDamagedEvent(
			int thisMinionPlayerIndex,
			int thisMinionIndex,
			int damagedPlayerIndex,
			int damagedMinionIndex,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1)
		throws HSInvalidPlayerIndexException
	{
		return boardState;
	}
	
	/**
	 * 
	 * Called whenever another minion dies
	 * 
	 * @param playerIndex The index of the dead minion's player.  0 if targeting yourself or your own minions, 1 if targeting the enemy
	 * @param minionIndex The index of the dead minion.
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * 
	 * @return The boardState is manipulated and returned
	 */
	public HearthTreeNode minionDeadEvent(
			int thisMinionPlayerIndex,
			int thisMinionIndex,
			int deadMinionPlayerIndex,
			int deadMinionIndex,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1)
		throws HSInvalidPlayerIndexException
	{
		return boardState;
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
	public HearthTreeNode minionHealedEvent(
			int thisMinionPlayerIndex,
			int thisMinionIndex,
			int healedMinionPlayerIndex,
			int healedMinionIndex,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1)
		throws HSInvalidPlayerIndexException
	{
		return boardState;
	}

	
	@Override
	public JSONObject toJSON() {
		JSONObject json = super.toJSON();
		json.put("attack", attack_);
		json.put("baseAttack", baseAttack_);
		json.put("health", health_);
		json.put("baseHealth", baseHealth_);
		json.put("maxHealth", maxHealth_);
		json.put("taunt", taunt_);
		json.put("divineShield", divineShield_);
		json.put("windFury", windFury_);
		json.put("charge", charge_);
		json.put("frozen", frozen_);
		json.put("silenced", silenced_);
		json.put("hasAttacked", hasAttacked_);
		return json;
	}
	
	@Override
	public Object deepCopy() {
		return new Minion(
				this.name_,
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
				this.silenced_,
				this.summoned_,
				this.transformed_,
				this.destroyOnTurnStart_,
				this.destroyOnTurnEnd_,
				this.isInHand_,
				this.hasBeenUsed_);
	}
	
	@Override
	public boolean equals(Object other) {
		if (!super.equals(other)) {
			return false;
		}
		if (attack_ != ((Minion)other).attack_)
			return false;
		if (health_ != ((Minion)other).health_)
			return false;
		if (maxHealth_ != ((Minion)other).maxHealth_)
			return false;
		if (baseHealth_ != ((Minion)other).baseHealth_)
			return false;
		if (baseAttack_ != ((Minion)other).baseAttack_)
			return false;
		if (extraAttackUntilTurnEnd_ != ((Minion)other).extraAttackUntilTurnEnd_)
			return false;
		if (taunt_ != ((Minion)other).taunt_)
			return false;
		if (divineShield_ != ((Minion)other).divineShield_)
			return false;
		if (windFury_ != ((Minion)other).windFury_)
			return false;
		if (charge_ != ((Minion)other).charge_)
			return false;
		if (hasAttacked_ != ((Minion)other).hasAttacked_)
			return false;
		if (hasWindFuryAttacked_ != ((Minion)other).hasWindFuryAttacked_)
			return false;
		if (frozen_ != ((Minion)other).frozen_)
			return false;
		if (silenced_ != ((Minion)other).silenced_)
			return false;
		if (summoned_ != ((Minion)other).summoned_)
			return false;
		if (transformed_ != ((Minion)other).transformed_)
			return false;
		if (destroyOnTurnStart_ != ((Minion)other).destroyOnTurnStart_)
			return false;
		if (destroyOnTurnEnd_ != ((Minion)other).destroyOnTurnEnd_)
			return false;
		return true;
	}

}
