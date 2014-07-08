package com.hearthsim.card.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.BoardState;
import com.hearthsim.util.DeepCopyable;
import com.hearthsim.util.HearthTreeNode;
import com.json.JSONObject;

public class Minion extends Card {
	
	protected boolean taunt_;
	protected boolean divineShield_;
	protected boolean windFury_;
	protected boolean charge_;
	
	protected boolean hasAttacked_;
	protected boolean hasWindFuryAttacked_;
	
	protected boolean frozen_;

	
	protected byte health_;
	protected byte maxHealth_;
	protected byte baseHealth_;
	
	protected byte attack_;
	protected byte baseAttack_;

	public Minion(String name, byte mana, byte attack, byte health, byte baseAttack, byte baseHealth, byte maxHealth) {
		this(name, mana, attack, health, baseAttack, baseHealth, maxHealth, false, false, false, false, false, false, false, true, false);
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
		hasWindFuryAttacked_ = hasWindFuryAttacked;
		frozen_ = frozen;
		baseHealth_ = baseHealth;
		maxHealth_ = maxHealth;
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
	
	public void takeDamage(byte damage, int thisPlayerIndex, int thisMinionIndex, HearthTreeNode<BoardState> boardState, Deck deck) throws HSInvalidPlayerIndexException {
		if (!divineShield_) {
			health_ = (byte)(health_ - damage);
			
			//Notify all that the minion is damaged
			HearthTreeNode<BoardState> toRet = boardState;
			toRet = toRet.data_.getHero_p0().minionDamagedEvent(thisPlayerIndex, thisMinionIndex, toRet, deck);
			for (int j = 0; j < toRet.data_.getNumMinions_p0(); ++j) {
				toRet = toRet.data_.getMinion_p0(j).minionDamagedEvent(thisPlayerIndex, thisMinionIndex, toRet, deck);
			}
			toRet = toRet.data_.getHero_p1().minionDamagedEvent(thisPlayerIndex, thisMinionIndex, toRet, deck);
			for (int j = 0; j < toRet.data_.getNumMinions_p1(); ++j) {
				toRet = toRet.data_.getMinion_p1(j).minionDamagedEvent(thisPlayerIndex, thisMinionIndex, toRet, deck);
			}
			
			//If fatal, notify all that it is dead
			if (health_ <= 0) {
				toRet = toRet.data_.getHero_p0().minionDeadEvent(thisPlayerIndex, thisMinionIndex, toRet, deck);
				for (int j = 0; j < toRet.data_.getNumMinions_p0(); ++j) {
					toRet = toRet.data_.getMinion_p0(j).minionDeadEvent(thisPlayerIndex, thisMinionIndex, toRet, deck);
				}
				toRet = toRet.data_.getHero_p1().minionDeadEvent(thisPlayerIndex, thisMinionIndex, toRet, deck);
				for (int j = 0; j < toRet.data_.getNumMinions_p1(); ++j) {
					toRet = toRet.data_.getMinion_p1(j).minionDeadEvent(thisPlayerIndex, thisMinionIndex, toRet, deck);
				}
			}
		} else {
			divineShield_ = false;
		}
	}
	
	public void destroyed(int thisPlayerIndex, int thisMinionIndex, HearthTreeNode<BoardState> boardState, Deck deck) throws HSInvalidPlayerIndexException {
		
		health_ = 0;
		
		HearthTreeNode<BoardState> toRet = boardState;
		//Notify all that it is dead
		toRet = toRet.data_.getHero_p0().minionDeadEvent(thisPlayerIndex, thisMinionIndex, toRet, deck);
		for (int j = 0; j < toRet.data_.getNumMinions_p0(); ++j) {
			toRet = toRet.data_.getMinion_p0(j).minionDeadEvent(thisPlayerIndex, thisMinionIndex, toRet, deck);
		}
		toRet = toRet.data_.getHero_p1().minionDeadEvent(thisPlayerIndex, thisMinionIndex, toRet, deck);
		for (int j = 0; j < toRet.data_.getNumMinions_p1(); ++j) {
			toRet = toRet.data_.getMinion_p1(j).minionDeadEvent(thisPlayerIndex, thisMinionIndex, toRet, deck);
		}

	}
	
	public void takeHeal(byte healAmount, int thisPlayerIndex, int thisMinionIndex, HearthTreeNode<BoardState> boardState, Deck deck) throws HSInvalidPlayerIndexException {
		
		if (health_ < maxHealth_) {
			if (health_ + healAmount > maxHealth_)
				health_ = maxHealth_;
			else
				health_ = (byte)(health_ + healAmount);
			
			//Notify all that it the minion is healed
			HearthTreeNode<BoardState> toRet = boardState;
			toRet = toRet.data_.getHero_p0().minionHealedEvent(0, 0, thisPlayerIndex, thisMinionIndex, toRet, deck);
			for (int j = 0; j < toRet.data_.getNumMinions_p0(); ++j) {
				toRet = toRet.data_.getMinion_p0(j).minionHealedEvent(0, j + 1, thisPlayerIndex, thisMinionIndex, toRet, deck);
			}
			toRet = toRet.data_.getHero_p1().minionHealedEvent(1, 0, thisPlayerIndex, thisMinionIndex, toRet, deck);
			for (int j = 0; j < toRet.data_.getNumMinions_p1(); ++j) {
				toRet = toRet.data_.getMinion_p1(j).minionHealedEvent(1, j + 1, thisPlayerIndex, thisMinionIndex, toRet, deck);
			}
			
		}
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
	public final HearthTreeNode<BoardState> useOn(
			int thisCardIndex,
			int playerIndex,
			int minionIndex,
			HearthTreeNode<BoardState> boardState,
			Deck deck)
		throws HSInvalidPlayerIndexException
	{
		//A generic card does nothing except for consuming mana
		HearthTreeNode<BoardState> toRet = super.useOn(thisCardIndex, playerIndex, minionIndex, boardState, deck);
		
		if (playerIndex == 0 && toRet != null) {
			//Notify all that a minion is created
			toRet = toRet.data_.getHero_p0().otherCardUsedEvent(0, toRet, deck);
			for (int j = 0; j < toRet.data_.getNumMinions_p0(); ++j) {
				toRet = toRet.data_.getMinion_p0(j).minionCreatedEvent(playerIndex, minionIndex, toRet, deck);
			}
			toRet = toRet.data_.getHero_p1().otherCardUsedEvent(0, toRet, deck);
			for (int j = 0; j < toRet.data_.getNumMinions_p1(); ++j) {
				toRet = toRet.data_.getMinion_p1(j).minionCreatedEvent(playerIndex, minionIndex, toRet, deck);
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
	protected HearthTreeNode<BoardState> use_core(
			int thisCardIndex,
			int playerIndex,
			int minionIndex,
			HearthTreeNode<BoardState> boardState,
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
			boardState.data_.placeMinion_p0(this, minionIndex - 1);
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
	public HearthTreeNode<BoardState> attack(
			int thisMinionIndex,
			int playerIndex,
			int minionIndex,
			HearthTreeNode<BoardState> boardState,
			Deck deck)
		throws HSInvalidPlayerIndexException
	{
		
		if (frozen_) {
			this.hasAttacked_ = true;
			this.frozen_ = false;
			return boardState;
		}
		
		//Notify all that an attack is beginning
		HearthTreeNode<BoardState> toRet = boardState;
		if (toRet != null) {
			//Notify all that a minion is created
			toRet = toRet.data_.getHero_p0().minionAttackingEvent(0, thisMinionIndex, playerIndex, minionIndex, toRet, deck);
			for (int j = 0; j < toRet.data_.getNumMinions_p0(); ++j) {
				toRet = toRet.data_.getMinion_p0(j).minionAttackingEvent(0, thisMinionIndex, playerIndex, minionIndex, toRet, deck);
			}
			toRet = toRet.data_.getHero_p1().minionAttackingEvent(0, thisMinionIndex, playerIndex, minionIndex, toRet, deck);
			for (int j = 0; j < toRet.data_.getNumMinions_p1(); ++j) {
				toRet = toRet.data_.getMinion_p1(j).minionAttackingEvent(0, thisMinionIndex, playerIndex, minionIndex, toRet, deck);
			}
		}
		
		//Do the actual attack
		toRet = this.attack_core(thisMinionIndex, playerIndex, minionIndex, boardState, deck);
		
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
	protected HearthTreeNode<BoardState> attack_core(
			int thisMinionIndex,
			int playerIndex,
			int minionIndex,
			HearthTreeNode<BoardState> boardState,
			Deck deck)
		throws HSInvalidPlayerIndexException
	{
		
		if (hasAttacked_) {
			//minion has already attacked
			return null;
		}
		
		if (minionIndex == 0) {
			//attack the enemy hero
			boardState.data_.getHero_p1().takeDamage(this.attack_, playerIndex, minionIndex, boardState, deck);
			this.takeDamage(boardState.data_.getHero_p1().attack_, 0, thisMinionIndex, boardState, deck);
			if (windFury_ && !hasWindFuryAttacked_)
				hasWindFuryAttacked_ = true;
			else
				hasAttacked_ = true;
			return boardState;
		} else {
			Minion target = boardState.data_.getMinion_p1(minionIndex - 1);
			target.takeDamage(this.attack_, playerIndex, minionIndex, boardState, deck);
			this.takeDamage(target.attack_, 0, thisMinionIndex, boardState, deck);
			if (target.getHealth() <= 0) {
				boardState.data_.removeMinion_p1(target);
			}
			if (health_ <= 0) {
				boardState.data_.removeMinion_p0(thisMinionIndex - 1);
			}
			if (windFury_ && !hasWindFuryAttacked_)
				hasWindFuryAttacked_ = true;
			else
				hasAttacked_ = true;
			return boardState;
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
	protected HearthTreeNode<BoardState> minionCreatedEvent(int createdMinionPlayerIndex, int createdMinionIndex, HearthTreeNode<BoardState> boardState, Deck deck) {
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
	protected HearthTreeNode<BoardState> minionAttackedEvent(int attackingPlayerIndex, int attackingMinionIndex, int targetPlayerIndex, int targetMinionIndex, HearthTreeNode<BoardState> boardState, Deck deck) {
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
	protected HearthTreeNode<BoardState> minionAttackingEvent(int attackingPlayerIndex, int attackingMinionIndex, int targetPlayerIndex, int targetMinionIndex, HearthTreeNode<BoardState> boardState, Deck deck) {
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
	protected HearthTreeNode<BoardState> minionDamagedEvent(int damagedPlayerIndex, int damagedMinionIndex, HearthTreeNode<BoardState> boardState, Deck deck) {
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
	protected HearthTreeNode<BoardState> minionDeadEvent(int deadMinionPlayerIndex, int deadMinionIndex, HearthTreeNode<BoardState> boardState, Deck deck) {
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
	protected HearthTreeNode<BoardState> minionHealedEvent(
			int thisMinionPlayerIndex,
			int thisMinionIndex,
			int healedMinionPlayerIndex,
			int healedMinionIndex,
			HearthTreeNode<BoardState> boardState,
			Deck deck)
		throws HSInvalidPlayerIndexException
	{
		return boardState;
	}

	
	@Override
	public JSONObject toJSON() {
		JSONObject json = super.toJSON();
		json.put("type", "Minion");
		json.put("attack", attack_);
		json.put("baseAttack", baseAttack_);
		json.put("health", health_);
		json.put("baseHealth", baseHealth_);
		json.put("maxHealth", maxHealth_);
		json.put("taunt", taunt_);
		json.put("divineShield", divineShield_);
		json.put("windFury", windFury_);
		json.put("charge", charge_);
		json.put("hasAttacked", hasAttacked_);
		return json;
	}
	
	@Override
	public DeepCopyable deepCopy() {
		return new Minion(
				this.name_,
				this.mana_,
				this.attack_,
				this.health_,
				this.baseAttack_,
				this.baseHealth_,
				this.maxHealth_,
				this.taunt_,
				this.divineShield_,
				this.windFury_,
				this.charge_,
				this.hasAttacked_,
				this.hasWindFuryAttacked_,
				this.frozen_,
				this.isInHand_,
				this.hasBeenUsed());
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
		return true;
	}

}
