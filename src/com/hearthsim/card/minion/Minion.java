package com.hearthsim.card.minion;

import java.util.Iterator;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.event.attack.AttackAction;
import com.hearthsim.event.deathrattle.DeathrattleAction;
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

	protected DeathrattleAction deathrattleAction_;
	protected AttackAction attackAction_;
	
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
				null,
				null,
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
					DeathrattleAction deathrattleAction,
					AttackAction attackAction,
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
		deathrattleAction_ = deathrattleAction;
		attackAction_ = attackAction;
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
	
	public boolean isSilenced() {
		return silenced_;
	}
	
	public boolean hasDeathrattle() {
		return deathrattleAction_ == null;
	}

	/**
	 * Called at the start of the turn
	 * 
	 * This function is called at the start of the turn.  Any derived class must override it to implement whatever
	 * "start of the turn" effect the card has.
	 */
	@Override
	public BoardState startTurn(int thisMinionPlayerIndex, BoardState boardState, Deck deckPlayer0, Deck deckPlayer1) throws HSInvalidPlayerIndexException {
		if (destroyOnTurnStart_) {
			this.destroyed(thisMinionPlayerIndex, new HearthTreeNode(boardState), deckPlayer0, deckPlayer1);
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
	public BoardState endTurn(int thisMinionPlayerIndex, BoardState boardState, Deck deckPlayer0, Deck deckPlayer1) throws HSInvalidPlayerIndexException {
		extraAttackUntilTurnEnd_ = 0;
		if (destroyOnTurnEnd_) {
			this.destroyed(thisMinionPlayerIndex, new HearthTreeNode(boardState), deckPlayer0, deckPlayer1);
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
	 * @param boardState 
	 * @param deck
	 * @param deckPlayer0 The deck of player0
	 * @param deckPlayer0 The deck of player1
	 * 
	 * @throws HSInvalidPlayerIndexException
	 */
	public HearthTreeNode takeDamage(byte damage, int attackerPlayerIndex, int thisPlayerIndex, HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1) throws HSInvalidPlayerIndexException {
		return this.takeDamage(damage, attackerPlayerIndex, thisPlayerIndex, boardState, deckPlayer0, deckPlayer1, false);
	}
	
	/**
	 * Called when this minion takes damage
	 * 
	 * Always use this function to take damage... it properly notifies all others of its damage and possibly of its death
	 * 
	 * @param damage The amount of damage to take
	 * @param attackerPlayerIndex The player index of the attacker.  This is needed to do things like +spell damage.
	 * @param thisPlayerIndex The player index of this minion
	 * @param boardState 
	 * @param deckPlayer0 The deck of player0
	 * @param deckPlayer0 The deck of player1
	 * @param isSpellDamage True if this is a spell damage
	 * 
	 * @throws HSInvalidPlayerIndexException
	 */
	public HearthTreeNode takeDamage(byte damage, int attackerPlayerIndex, int thisPlayerIndex, HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1, boolean isSpellDamage) throws HSInvalidPlayerIndexException {
		if (!divineShield_) {
			byte totalDamage = isSpellDamage ? (byte)(damage + boardState.data_.getSpellDamage(attackerPlayerIndex)) : damage;
			health_ = (byte)(health_ - totalDamage);
			
			//Notify all that the minion is damaged
			HearthTreeNode toRet = boardState;
			toRet = toRet.data_.getHero_p0().minionDamagedEvent(0, thisPlayerIndex, this, toRet, deckPlayer0, deckPlayer1);
			for (int j = 0; j < toRet.data_.getNumMinions_p0(); ++j) {
				if (!toRet.data_.getMinion_p0(j).silenced_)
					toRet = toRet.data_.getMinion_p0(j).minionDamagedEvent(0, thisPlayerIndex, this, toRet, deckPlayer0, deckPlayer1);
			}
			toRet = toRet.data_.getHero_p1().minionDamagedEvent(1, thisPlayerIndex, this, toRet, deckPlayer0, deckPlayer1);
			for (int j = 0; j < toRet.data_.getNumMinions_p1(); ++j) {
				if (!toRet.data_.getMinion_p1(j).silenced_)
					toRet = toRet.data_.getMinion_p1(j).minionDamagedEvent(1, thisPlayerIndex, this, toRet, deckPlayer0, deckPlayer1);
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
	 * @param boardState 
	 * @param deckPlayer0
	 * @param deckPlayer1
	 * 
	 * @throws HSInvalidPlayerIndexException
	 */
	public HearthTreeNode destroyed(int thisPlayerIndex, HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1) throws HSInvalidPlayerIndexException {
		
		health_ = 0;
		HearthTreeNode toRet = boardState;
		
		//perform the deathrattle action if there is one
		if (deathrattleAction_ != null)
			toRet =  deathrattleAction_.performAction(this, thisPlayerIndex, toRet, deckPlayer0, deckPlayer1);
		
		//Notify all that it is dead
		toRet = toRet.data_.getHero_p0().minionDeadEvent(0, thisPlayerIndex, this, toRet, deckPlayer0, deckPlayer1);
		for (int j = 0; j < toRet.data_.getNumMinions_p0(); ++j) {
			if (!toRet.data_.getMinion_p0(j).silenced_)
				toRet = toRet.data_.getMinion_p0(j).minionDeadEvent(0, thisPlayerIndex, this, toRet, deckPlayer0, deckPlayer1);
		}
		toRet = toRet.data_.getHero_p1().minionDeadEvent(1, thisPlayerIndex, this, toRet, deckPlayer0, deckPlayer1);
		for (int j = 0; j < toRet.data_.getNumMinions_p1(); ++j) {
			if (!toRet.data_.getMinion_p1(j).silenced_)
				toRet = toRet.data_.getMinion_p1(j).minionDeadEvent(1, thisPlayerIndex, this, toRet, deckPlayer0, deckPlayer1);
		}
		
		return toRet;

	}
	
	/**
	 * Called when this minion is silenced
	 * 
	 * Always use this function to "silence" minions
	 * 
	 * @param thisPlayerIndex The player index of this minion
	 * @param boardState 
	 * @param deckPlayer0 The deck of player0
	 * @param deckPlayer0 The deck of player1
	 * 
	 * @throws HSInvalidPlayerIndexException
	 */
	public HearthTreeNode silenced(int thisPlayerIndex, HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1) throws HSInvalidPlayerIndexException {
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
	 * @param boardState 
	 * @param deckPlayer0 The deck of player0
	 * @param deckPlayer0 The deck of player1
	 * 
	 * @throws HSInvalidPlayerIndexException
	 */
	public HearthTreeNode takeHeal(byte healAmount, int thisPlayerIndex, HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1) throws HSInvalidPlayerIndexException {
		
		if (health_ < maxHealth_) {
			if (health_ + healAmount > maxHealth_)
				health_ = maxHealth_;
			else
				health_ = (byte)(health_ + healAmount);
			
			//Notify all that it the minion is healed
			HearthTreeNode toRet = boardState;
			toRet = toRet.data_.getHero_p0().minionHealedEvent(0, thisPlayerIndex, this, toRet, deckPlayer0, deckPlayer1);
			for (Iterator<Minion> iter = toRet.data_.getMinions_p0().iterator(); iter.hasNext();) {
				Minion minion = iter.next();
				if (!minion.silenced_)
					toRet = minion.minionHealedEvent(0, thisPlayerIndex, this, toRet, deckPlayer0, deckPlayer1);
			}
			toRet = toRet.data_.getHero_p1().minionHealedEvent(1, thisPlayerIndex, this, toRet, deckPlayer0, deckPlayer1);
			for (Iterator<Minion> iter = toRet.data_.getMinions_p1().iterator(); iter.hasNext();) {
				Minion minion = iter.next();
				if (!minion.silenced_)
					toRet = minion.minionHealedEvent(1, thisPlayerIndex, this, toRet, deckPlayer0, deckPlayer1);
			}
			return toRet;
		}
		return boardState;
	}
	
	/**
	 * 
	 * Places the minion on the board
	 * 
	 * @param targetPlayerIndex The index of the target player.  0 if targeting yourself or your own minions, 1 if targeting the enemy
	 * @param targetMinion The target minion (can be a Hero).  If it is a Hero, then the minion is placed on the last (right most) spot on the board.
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * @param deckPlayer0 The deck of player0
	 * @param deckPlayer0 The deck of player1
	 * 
	 * @return The boardState is manipulated and returned
	 */
	@Override
	public HearthTreeNode useOn(
			int targetPlayerIndex,
			Minion targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1)
		throws HSInvalidPlayerIndexException
	{
		//A generic card does nothing except for consuming mana
		HearthTreeNode toRet = this.use_core(targetPlayerIndex, targetMinion, boardState, deckPlayer0, deckPlayer1);
		
		if (toRet != null) {
			//Notify all other cards/characters of the card's use
			if (!transformed_) {
				for (Iterator<Card> iter = toRet.data_.getCards_hand_p0().iterator(); iter.hasNext();) {
					toRet = (iter.next()).otherCardUsedEvent(toRet, deckPlayer0, deckPlayer1);
				}
				toRet = toRet.data_.getHero_p0().otherCardUsedEvent(toRet, deckPlayer0, deckPlayer1);
				for (Iterator<Minion> iter = toRet.data_.getMinions_p0().iterator(); iter.hasNext();) {
					Minion minion = iter.next();
					if (!minion.silenced_)
						toRet = minion.otherCardUsedEvent(toRet, deckPlayer0, deckPlayer1);
				}
				toRet = toRet.data_.getHero_p1().otherCardUsedEvent(toRet, deckPlayer0, deckPlayer1);
				for (Iterator<Minion> iter = toRet.data_.getMinions_p1().iterator(); iter.hasNext();) {
					Minion minion = iter.next();
					if (!minion.silenced_)
						toRet = minion.otherCardUsedEvent(toRet, deckPlayer0, deckPlayer1);
				}
			}
			if (summoned_) {
				//Notify all that a minion is summoned
				toRet = toRet.data_.getHero_p0().minionSummonedEvent(0, targetPlayerIndex, this, toRet, deckPlayer0, deckPlayer1);
				for (Iterator<Minion> iter = toRet.data_.getMinions_p0().iterator(); iter.hasNext();) {
					Minion minion = iter.next();
					if (!minion.silenced_)
						toRet = minion.minionSummonedEvent(0, targetPlayerIndex, this, toRet, deckPlayer0, deckPlayer1);
				}
				toRet = toRet.data_.getHero_p1().minionSummonedEvent(1, targetPlayerIndex, this, toRet, deckPlayer0, deckPlayer1);
				for (Iterator<Minion> iter = toRet.data_.getMinions_p1().iterator(); iter.hasNext();) {
					Minion minion = iter.next();
					if (!minion.silenced_)
						toRet = minion.minionSummonedEvent(1, targetPlayerIndex, this, toRet, deckPlayer0, deckPlayer1);
				}
			} else {
				//Notify all that a minion is placed
				toRet = toRet.data_.getHero_p0().minionPlacedEvent(0, targetPlayerIndex, this, toRet, deckPlayer0, deckPlayer1);
				for (Iterator<Minion> iter = toRet.data_.getMinions_p0().iterator(); iter.hasNext();) {
					Minion minion = iter.next();
					if (!minion.silenced_)
						toRet = minion.minionPlacedEvent(0, targetPlayerIndex, this, toRet, deckPlayer0, deckPlayer1);
				}
				toRet = toRet.data_.getHero_p1().minionPlacedEvent(1, targetPlayerIndex, this, toRet, deckPlayer0, deckPlayer1);
				for (Iterator<Minion> iter = toRet.data_.getMinions_p1().iterator(); iter.hasNext();) {
					Minion minion = iter.next();
					if (!minion.silenced_)
						toRet = minion.minionPlacedEvent(1, targetPlayerIndex, this, toRet, deckPlayer0, deckPlayer1);
				}
			}
		}
		
		return toRet;
	}
	
	@Override
    public boolean canBeUsedOn(int playerIndex, Minion minion) {
		if (playerIndex == 1)
			return false;
		if (hasBeenUsed_) 
			return false;
		return true;
    }
    
	/**
	 * 
	 * Places a minion on the board.
	 * 
	 * @param targetPlayerIndex The index of the target player.  0 if targeting yourself or your own minions, 1 if targeting the enemy
	 * @param targetMinion The target minion (can be a Hero).  The new minion is always placed to the right of (higher index) the target minion.  If the target minion is a hero, then it is placed at the left-most position.
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * @param deckPlayer0 The deck of player0
	 * @param deckPlayer0 The deck of player1
	 * 
	 * @return The boardState is manipulated and returned
	 */
	@Override
	protected HearthTreeNode use_core(
			int targetPlayerIndex,
			Minion targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1)
		throws HSInvalidPlayerIndexException
	{
		if (hasBeenUsed_) {
			//Card is already used, nothing to do
			return null;
		}
		
		if (targetPlayerIndex == 1)
			return null;
		
		if (boardState.data_.getNumMinions_p0() < 7) {

			if (!charge_) {
				hasAttacked_ = true;
			}
			hasBeenUsed_ = true;
			if (targetMinion instanceof Hero)
				boardState.data_.placeMinion(0, this, 0);
			else
				boardState.data_.placeMinion(0, this, boardState.data_.getMinions_p0().indexOf(targetMinion) + 1);
			boardState.data_.setMana_p0(boardState.data_.getMana_p0() - this.mana_);
			boardState.data_.removeCard_hand(this);
			return boardState;
							
		} else {
			return null;
		}

	}
	
	/**
	 * 
	 * Attack with the minion
	 * 
	 * @param targetMinionPlayerIndex The index of the target player.  0 if targeting yourself or your own minions, 1 if targeting the enemy
	 * @param targetMinion The target minion
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * @param deckPlayer0 The deck of player0
	 * @param deckPlayer0 The deck of player1
	 * 
	 * @return The boardState is manipulated and returned
	 */
	public HearthTreeNode attack(
			int targetMinionPlayerIndex,
			Minion targetMinion,
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
			toRet = toRet.data_.getHero_p0().minionAttackingEvent(0, this, targetMinionPlayerIndex, targetMinion, toRet, deckPlayer0, deckPlayer1);
			for (Iterator<Minion> iter = toRet.data_.getMinions_p0().iterator(); iter.hasNext();) {
				Minion minion = iter.next();
				if (!minion.silenced_)
					toRet = minion.minionAttackingEvent(0, this, targetMinionPlayerIndex, targetMinion, toRet, deckPlayer0, deckPlayer1);
			}
			toRet = toRet.data_.getHero_p1().minionAttackingEvent(0, this, targetMinionPlayerIndex, targetMinion, toRet, deckPlayer0, deckPlayer1);
			for (Iterator<Minion> iter = toRet.data_.getMinions_p1().iterator(); iter.hasNext();) {
				Minion minion = iter.next();
				if (!minion.silenced_)
					toRet = minion.minionAttackingEvent(0, this, targetMinionPlayerIndex, targetMinion, toRet, deckPlayer0, deckPlayer1);
			}
		}
		
		//Do the actual attack
		toRet = this.attack_core(targetMinionPlayerIndex, targetMinion, boardState, deckPlayer0, deckPlayer1);
		
		//check for and remove dead minions
		Iterator<Minion> iter0 = toRet.data_.getMinions_p0().iterator();
		while (iter0.hasNext()) {
			Minion tMinion = iter0.next();
			if (tMinion.getHealth() <= 0) {
				toRet = tMinion.destroyed(0, toRet, deckPlayer0, deckPlayer1);
				iter0.remove();
				toRet.data_.getMinions_p0().remove(tMinion);
			}
		}
		Iterator<Minion> iter1 = toRet.data_.getMinions_p1().iterator();
		while (iter1.hasNext()) {
			Minion tMinion = iter1.next();
			if (tMinion.getHealth() <= 0) {
				toRet = tMinion.destroyed(1, toRet, deckPlayer0, deckPlayer1);
				iter1.remove();
				toRet.data_.getMinions_p1().remove(tMinion);
			}
		}
		
		return toRet;
	}

	/**
	 * 
	 * Attack with the minion
	 * 
	 * @param targetMinionPlayerIndex The index of the target player.  0 if targeting yourself or your own minions, 1 if targeting the enemy
	 * @param targetMinion The target minion
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * @param deckPlayer0 The deck of player0
	 * @param deckPlayer0 The deck of player1
	 * 
	 * @return The boardState is manipulated and returned
	 */
	protected HearthTreeNode attack_core(
			int targetMinionPlayerIndex,
			Minion targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1)
		throws HSInvalidPlayerIndexException
	{
		
		if (hasAttacked_) {
			//minion has already attacked
			return null;
		}
		
		if (targetMinionPlayerIndex == 0) {
			return null;
		}
		
		HearthTreeNode toRet = boardState;
		byte origAttack = targetMinion.attack_;
		toRet = targetMinion.takeDamage((byte)(this.attack_ + this.extraAttackUntilTurnEnd_), 0, targetMinionPlayerIndex, toRet, deckPlayer0, deckPlayer1);
		toRet = this.takeDamage(origAttack, targetMinionPlayerIndex, 0, toRet, deckPlayer0, deckPlayer1);
		if (!(targetMinion instanceof Hero)) {
			if (targetMinion.getHealth() <= 0) {
				toRet = targetMinion.destroyed(targetMinionPlayerIndex, boardState, deckPlayer0, deckPlayer1);
				toRet.data_.removeMinion_p1(targetMinion);
			}
		}
		if (!(this instanceof Hero)) {
			if (health_ <= 0) {
				toRet = this.destroyed(0, boardState, deckPlayer0, deckPlayer1);
				toRet.data_.removeMinion_p0(this);
			}
		}
		if (windFury_ && !hasWindFuryAttacked_)
			hasWindFuryAttacked_ = true;
		else
			hasAttacked_ = true;
		return toRet;

	}


	
	//======================================================================================
	// Hooks for various events
	//======================================================================================	

	/**
	 * 
	 * Called whenever another minion comes on board
	 * 
	 * @param thisMinionPlayerIndex The player index of this minion
	 * @param placedMinionPlayerIndex The index of the placed minion's player.
	 * @param placedMinion The placed minion
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * @param deckPlayer0 The deck of player0
	 * @param deckPlayer0 The deck of player1
	 * 
	 * @return The boardState is manipulated and returned
	 */
	public HearthTreeNode minionPlacedEvent(
			int thisMinionPlayerIndex,
			int placedMinionPlayerIndex,
			Minion placedMinion,
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
	 * @param thisMinionPlayerIndex The player index of this minion
	 * @param summonedMinionPlayerIndex The index of the summoned minion's player.
	 * @param summonedMinion The summoned minion
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * @param deckPlayer0 The deck of player0
	 * @param deckPlayer0 The deck of player1
	 * 
	 * @return The boardState is manipulated and returned
	 */
	public HearthTreeNode minionSummonedEvent(
			int thisMinionPlayerIndex,
			int summonedMinionPlayerIndex,
			Minion summonedMinion,
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
	 * @param thisMinionPlayerIndex The player index of this minion
	 * @param transformedMinionPlayerIndex The index of the transformed minion's player.
	 * @param transformedMinion The transformed minion (the minion that resulted from a transformation)
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * @param deckPlayer0 The deck of player0
	 * @param deckPlayer0 The deck of player1
	 * 
	 * @return The boardState is manipulated and returned
	 */
	public HearthTreeNode minionTransformedEvent(
			int thisMinionPlayerIndex,
			int transformedMinionPlayerIndex,
			Minion transformedMinion,
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
	 * @param attackingMinion The attacking minion
	 * @param attackedPlayerIndex The target player's index
	 * @param attackedMinion The target (attacked) minion
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * @param deckPlayer0 The deck of player0
	 * @param deckPlayer0 The deck of player1
	 * 
	 * @return The boardState is manipulated and returned
	 */
	public HearthTreeNode minionAttackedEvent(
			int attackingPlayerIndex,
			Minion attackingMinion,
			int attackedPlayerIndex,
			Minion attackedMinion,
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
	 * @param attackingMinion The attacking minion
	 * @param attackedPlayerIndex The target player's index
	 * @param attackedMinion The target (attacked) minion
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * @param deckPlayer0 The deck of player0
	 * @param deckPlayer0 The deck of player1
	 * 
	 * @return The boardState is manipulated and returned
	 */
	public HearthTreeNode minionAttackingEvent(
			int attackingPlayerIndex,
			Minion attackingMinion,
			int attackedPlayerIndex,
			Minion attackedMinion,
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
	 * @param thisMinionPlayerIndex The index of the damaged minion's player.  0 if targeting yourself or your own minions, 1 if targeting the enemy
	 * @param damagedPlayerIndex The player index of the damaged minion.
	 * @param damagedMinion The damaged minion
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * @param deckPlayer0 The deck of player0
	 * @param deckPlayer0 The deck of player1
	 * 
	 * @return The boardState is manipulated and returned
	 */
	public HearthTreeNode minionDamagedEvent(
			int thisMinionPlayerIndex,
			int damagedPlayerIndex,
			Minion damagedMinion,
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
	 * @param thisMinionPlayerIndex The index of the damaged minion's player.  0 if targeting yourself or your own minions, 1 if targeting the enemy
	 * @param deadMinionPlayerIndex The player index of the dead minion.
	 * @param deadMinion The dead minion
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * @param deckPlayer0 The deck of player0
	 * @param deckPlayer0 The deck of player1
	 * 
	 * @return The boardState is manipulated and returned
	 */
	public HearthTreeNode minionDeadEvent(
			int thisMinionPlayerIndex,
			int deadMinionPlayerIndex,
			Minion deadMinion,
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
	 * @param thisMinionPlayerIndex The index of the damaged minion's player.  0 if targeting yourself or your own minions, 1 if targeting the enemy
	 * @param healedMinionPlayerIndex The player index of the healed minion.
	 * @param healedMinion The healed minion
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * @param deckPlayer0 The deck of player0
	 * @param deckPlayer0 The deck of player1
	 * 
	 * @return The boardState is manipulated and returned
	 */
	public HearthTreeNode minionHealedEvent(
			int thisMinionPlayerIndex,
			int healedMinionPlayerIndex,
			Minion healedMinion,
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
	
	/**
	 * Deep copy of the object
	 * 
	 * Note: the event actions are not actually deep copied.
	 */
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
				this.deathrattleAction_,
				this.attackAction_,
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
		
		//This is checked for reference equality
		if (deathrattleAction_ != ((Minion)other).deathrattleAction_)
			return false;
		
		//This is checked for reference equality
		if (attackAction_ != ((Minion)other).attackAction_)
			return false;
		
		return true;
	}

}
