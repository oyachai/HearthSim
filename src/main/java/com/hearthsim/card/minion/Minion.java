package com.hearthsim.card.minion;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.EnumSet;

import org.json.JSONObject;

import com.hearthsim.card.Card;
import com.hearthsim.card.CardEndTurnInterface;
import com.hearthsim.card.CardStartTurnInterface;
import com.hearthsim.card.Deck;
import com.hearthsim.card.ImplementedCardList;
import com.hearthsim.event.attack.AttackAction;
import com.hearthsim.event.deathrattle.DeathrattleAction;
import com.hearthsim.exception.HSException;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.HearthAction;
import com.hearthsim.util.HearthAction.Verb;
import com.hearthsim.util.factory.BoardStateFactoryBase;
import com.hearthsim.util.tree.HearthTreeNode;

public class Minion extends Card implements CardEndTurnInterface, CardStartTurnInterface {

	public enum BattlecryTargetType {
		FRIENDLY_HERO,
		ENEMY_HERO,
		FRIENDLY_MINIONS,
		ENEMY_MINIONS,
		FRIENDLY_BEASTS,
		ENEMY_BEASTS,
		FRIENDLY_MURLOCS,
		ENEMY_MURLOCS
	}

	public enum MinionTribe {
		NONE,
		BEAST,
		MECH,
		MURLOC,
		PIRATE,
		DRAGON,
		DEMON,
		TOTEM
	}

	protected boolean taunt_;
	protected boolean divineShield_;
	protected boolean windFury_;
	protected boolean charge_;

	protected boolean hasAttacked_;
	protected boolean hasWindFuryAttacked_;

	protected boolean frozen_;
	protected boolean silenced_;
	protected boolean stealthed_;
	protected boolean heroTargetable_;

	protected byte health_;
	protected byte maxHealth_;
	protected byte baseHealth_;
	protected byte auraHealth_;

	protected byte attack_;
	protected byte baseAttack_;
	protected byte extraAttackUntilTurnEnd_;
	protected byte auraAttack_;

	protected boolean destroyOnTurnStart_;
	protected boolean destroyOnTurnEnd_;

	protected byte spellDamage_;

	protected DeathrattleAction deathrattleAction_;
	protected AttackAction attackAction_;

	protected MinionTribe tribe = MinionTribe.NONE;

	// This is a flag to tell the BoardState that it can't cheat on the placement of this minion
	protected boolean placementImportant_ = false;

	public Minion() {
		super();
		ImplementedCardList cardList = ImplementedCardList.getInstance();
		ImplementedCardList.ImplementedCard implementedCard = cardList.getCardForClass(this.getClass());
		if(implementedCard != null) {
			// only 'Minion' class is not implemented
        	baseManaCost = (byte) implementedCard.mana_;
			name_ = implementedCard.name_;
			attack_ = (byte)implementedCard.attack_;
			baseAttack_ = attack_;
			health_ = (byte)implementedCard.health_;
			maxHealth_ = health_;
			baseHealth_ = health_;
			taunt_ = implementedCard.taunt_;
			divineShield_ = implementedCard.divineShield_;
			windFury_ = implementedCard.windfury_;
			charge_ = implementedCard.charge_;
			stealthed_ = implementedCard.stealth_;
			isInHand_ = true;
			// TODO: spellpower could be deduced from text quite easily
		}
	}

	/**
	 * Simplified constructor
	 *
	 * @param name
	 * @param mana
	 * @param attack
	 * @param health
	 * @param baseAttack
	 * @param baseHealth
	 * @param maxHealth
	 */
	public Minion(String name, byte mana, byte attack, byte health, byte baseAttack, byte baseHealth, byte maxHealth) {
		super(name, mana, false, true);
		attack_ = attack;
		health_ = health;
		taunt_ = false;
		divineShield_ = false;
		windFury_ = false;
		charge_ = false;
		hasAttacked_ = false;
		baseAttack_ = baseAttack;
		extraAttackUntilTurnEnd_ = 0;
		hasWindFuryAttacked_ = false;
		frozen_ = false;
		silenced_ = false;
		baseHealth_ = baseHealth;
		maxHealth_ = maxHealth;
		destroyOnTurnStart_ = false;
		destroyOnTurnEnd_ = false;
		deathrattleAction_ = null;
		attackAction_ = null;

		auraAttack_ = 0;
		auraHealth_ = 0;

		spellDamage_ = 0;

		stealthed_ = false;
		heroTargetable_ = true;
	}

	@Deprecated
	public Minion(String name, byte mana, byte attack, byte health, byte baseAttack, byte extraAttackUntilTurnEnd,
			byte auraAttack, byte baseHealth, byte maxHealth, byte auraHealth, byte spellDamage, boolean taunt,
			boolean divineShield, boolean windFury, boolean charge, boolean hasAttacked, boolean hasWindFuryAttacked,
			boolean frozen, boolean silenced, boolean stealthed, boolean heroTargetable, boolean summoned,
			boolean transformed, boolean destroyOnTurnStart, boolean destroyOnTurnEnd,
			DeathrattleAction deathrattleAction, AttackAction attackAction, boolean isInHand, boolean hasBeenUsed) {
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
		destroyOnTurnStart_ = destroyOnTurnStart;
		destroyOnTurnEnd_ = destroyOnTurnEnd;
		deathrattleAction_ = deathrattleAction;
		attackAction_ = attackAction;

		auraAttack_ = auraAttack;
		auraHealth_ = auraHealth;

		spellDamage_ = spellDamage;

		stealthed_ = stealthed;
		heroTargetable_ = heroTargetable;
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

	public void addHealth(byte value) {
		health_ += value;
	}

	public byte getMaxHealth() {
		return maxHealth_;
	}

	public void setMaxHealth(byte health) {
		maxHealth_ = health;
	}

	public void addMaxHealth(byte value) {
		maxHealth_ += value;
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

	public void addAttack(byte value) {
		attack_ += value;
	}

	public boolean getDivineShield() {
		return divineShield_;
	}

	public void setDivineShield(boolean divineShield) {
		divineShield_ = divineShield;
	}

	public boolean canAttack() {
		return !this.hasAttacked_ && (this.attack_ + this.extraAttackUntilTurnEnd_) > 0 && !this.frozen_;
	}
	
	@Deprecated
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

	public boolean getWindfury() {
		return windFury_;
	}

	public void setWindfury(boolean value) {
		windFury_ = value;
		if (hasAttacked_) {
			hasAttacked_ = false;
			hasWindFuryAttacked_ = true;
		}
	}

	public void addExtraAttackUntilTurnEnd(byte value) {
		extraAttackUntilTurnEnd_ += value;
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
		return deathrattleAction_ != null;
	}

	public void setDeathrattle(DeathrattleAction action) {
		deathrattleAction_ = action;
	}

	public byte getAuraAttack() {
		return auraAttack_;
	}

	public void setAuraAttack(byte value) {
		auraAttack_ = value;
	}

	public byte getAuraHealth() {
		return auraHealth_;
	}

	public void setAuraHealth(byte value) {
		auraHealth_ = value;
	}

	public byte getTotalAttack() {
		return (byte)(attack_ + auraAttack_ + extraAttackUntilTurnEnd_);
	}

	public byte getTotalHealth() {
		return (byte)(health_ + auraHealth_);
	}

	public byte getTotalMaxHealth() {
		return (byte)(maxHealth_ + auraHealth_);
	}

	public MinionTribe getTribe() {
		return tribe;
	}
	
	public void setTribe(MinionTribe value) {
		this.tribe = value;
	}

	public void addAuraHealth(byte value) {
		auraHealth_ += value;
	}

	public void removeAuraHealth(byte value) {
		health_ += value;
		if(health_ > maxHealth_)
			health_ = maxHealth_;
		auraHealth_ -= value;
	}

	public boolean getStealthed() {
		return stealthed_;
	}

	public void setStealthed(boolean value) {
		stealthed_ = value;
	}

	public boolean getPlacementImportant() {
		return placementImportant_;
	}

	public void setPlacementImportant(boolean value) {
		placementImportant_ = value;
	}

	public boolean isHeroTargetable() {
		return heroTargetable_;
	}

	public void setHeroTargetable(boolean value) {
		heroTargetable_ = value;
	}

	public byte getSpellDamage() {
		return spellDamage_;
	}

	public void setSpellDamage(byte value) {
		spellDamage_ = value;
	}

	public void addSpellDamage(byte value) {
		spellDamage_ += value;
	}

	public void subtractSpellDamage(byte value) {
		spellDamage_ -= value;
	}

	/**
	 * Called at the start of the turn
	 * 
	 * This function is called at the start of the turn. Any derived class must override it to implement whatever "start of the turn" effect the card has.
	 */
	@Override
	public HearthTreeNode startTurn(PlayerSide thisMinionPlayerIndex, HearthTreeNode boardModel, Deck deckPlayer0,
			Deck deckPlayer1) throws HSException {
		if(destroyOnTurnStart_) {
			// toRet = this.destroyed(thisMinionPlayerIndex, toRet, deckPlayer0, deckPlayer1);
			this.setHealth((byte)-99);
		}
		return boardModel;
	}

	/**
	 * End the turn and resets the card state
	 * 
	 * This function is called at the end of the turn. Any derived class must override it and remove any temporary buffs that it has.
	 * 
	 * This is not the most efficient implementation... luckily, endTurn only happens once per turn
	 */
	@Override
	public HearthTreeNode endTurn(PlayerSide thisMinionPlayerIndex, HearthTreeNode boardModel, Deck deckPlayer0,
			Deck deckPlayer1) throws HSException {
		extraAttackUntilTurnEnd_ = 0;
		if(destroyOnTurnEnd_) {
			// toRet = this.destroyed(thisMinionPlayerIndex, toRet, deckPlayer0, deckPlayer1);
			this.setHealth((byte)-99);
		}
		return boardModel;
	}

	/**
	 * Called when this minion takes damage
	 * 
	 * Always use this function to take damage... it properly notifies all others of its damage and possibly of its death
	 * 
	 * @param damage The amount of damage to take
	 * @param attackPlayerSide The player index of the attacker. This is needed to do things like +spell damage.
	 * @param thisPlayerSide
	 * @param boardState
	 * @param deckPlayer0 The deck of player0
	 * @param isSpellDamage True if this is a spell damage
	 * @param handleMinionDeath Set this to True if you want the death event to trigger when (if) the minion dies from this damage. Setting this flag to True will also trigger deathrattle immediately.
	 * @throws HSInvalidPlayerIndexException
	 */
	public HearthTreeNode takeDamage(byte damage, PlayerSide attackPlayerSide, PlayerSide thisPlayerSide,
			HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1, boolean isSpellDamage,
			boolean handleMinionDeath) throws HSException {
		if(!divineShield_) {
			byte totalDamage = isSpellDamage ? (byte)(damage + boardState.data_.getSpellDamage(attackPlayerSide))
					: damage;
			health_ = (byte)(health_ - totalDamage);

			return this.notifyMinionDamaged(boardState, thisPlayerSide, deckPlayer0, deckPlayer1);
		} else {
			if(damage > 0)
				divineShield_ = false;
			return boardState;
		}
	}

	/**
	 * Called when this minion dies (destroyed)
	 * 
	 * Always use this function to "kill" minions
	 * 
	 *
	 *
	 * @param thisPlayerSide
	 * @param boardState
	 * @param deckPlayer0
	 * @param deckPlayer1
	 *
	 * @throws HSInvalidPlayerIndexException
	 */
	public HearthTreeNode destroyed(PlayerSide thisPlayerSide, HearthTreeNode boardState, Deck deckPlayer0,
			Deck deckPlayer1) throws HSException {

		health_ = 0;
		HearthTreeNode toRet = boardState;

		// perform the deathrattle action if there is one
		if(deathrattleAction_ != null) {
			toRet = deathrattleAction_.performAction(this, thisPlayerSide, toRet, deckPlayer0, deckPlayer1);
		}

		// Notify all that it is dead
		toRet = this.notifyMinionDead(thisPlayerSide, this, toRet, deckPlayer0, deckPlayer1);
		return toRet;

	}

	// Use for bounce (e.g., Brewmaster) or recreate (e.g., Reincarnate)
	public Minion createResetCopy() {
		try {
			Constructor<? extends Minion> ctor = this.getClass().getConstructor();
			Minion object = ctor.newInstance();
			return object;
		} catch(NoSuchMethodException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void silenced(PlayerSide thisPlayerSide, HearthTreeNode boardState) throws HSInvalidPlayerIndexException {
		this.silenced(thisPlayerSide, boardState.data_);
	}

	/**
	 * Called when this minion is silenced
	 * 
	 * Always use this function to "silence" minions
	 *
	 * @param thisPlayerSide
	 * @param boardState
	 * @throws HSInvalidPlayerIndexException
	 */
	public void silenced(PlayerSide thisPlayerSide, BoardModel boardState) throws HSInvalidPlayerIndexException {
		spellDamage_ = 0;
		divineShield_ = false;
		taunt_ = false;
		charge_ = false;
		frozen_ = false;
		windFury_ = false;
		silenced_ = true;
		deathrattleAction_ = null;
		stealthed_ = false;
		heroTargetable_ = true;

		// Reset the attack and health to base
		this.attack_ = this.baseAttack_;
		if(this.maxHealth_ > this.baseHealth_) {
			this.maxHealth_ = this.baseHealth_;
			if(this.health_ > this.maxHealth_)
				this.health_ = this.maxHealth_;
		}
	}

	/**
	 * Called when this minion is healed
	 * 
	 * Always use this function to heal minions
	 * 
	 * @param healAmount The amount of healing to take
	 * @param thisPlayerSide
	 * @param boardState
	 * @param deckPlayer0 The deck of player0 @throws HSInvalidPlayerIndexException
	 * */
	public HearthTreeNode takeHeal(byte healAmount, PlayerSide thisPlayerSide, HearthTreeNode boardState,
			Deck deckPlayer0, Deck deckPlayer1) throws HSException {

		if(health_ < maxHealth_) {
			if(health_ + healAmount > maxHealth_)
				health_ = maxHealth_;
			else
				health_ = (byte)(health_ + healAmount);

			// Notify all that it the minion is healed
			return this.notifyMinionHealed(boardState, thisPlayerSide, deckPlayer0, deckPlayer1);
		}
		return boardState;
	}

	@Override
	public boolean canBeUsedOn(PlayerSide playerSide, Minion minion, BoardModel boardModel) {
		if(!super.canBeUsedOn(playerSide, minion, boardModel)) {
			return false;
		}
		return playerSide != PlayerSide.WAITING_PLAYER && !hasBeenUsed;
	}

	/**
	 * Use a targetable battlecry. This will add battlecry nodes to boardState as children.
	 * 
	 * @param side
	 * @param targetMinion
	 * @param boardState
	 * @param deckPlayer0
	 * @param deckPlayer1
	 * @return
	 * @throws HSException
	 */
	public HearthTreeNode useTargetableBattlecry(PlayerSide side, Minion targetMinion, HearthTreeNode boardState,
			Deck deckPlayer0, Deck deckPlayer1) throws HSException {
		if(this instanceof MinionTargetableBattlecry) {
			MinionTargetableBattlecry battlecryMinion = (MinionTargetableBattlecry)this;
		
			HearthTreeNode node = new HearthTreeNode(boardState.data_.deepCopy());

			// Need to get the new node's version of the target minion
			int targetMinionIndex = side.getPlayer(boardState).getMinions().indexOf(targetMinion);
			if(targetMinionIndex >= 0) {
				node = battlecryMinion.useTargetableBattlecry_core(side, side.getPlayer(node).getMinions().get(targetMinionIndex),
						node, deckPlayer0, deckPlayer1);
			} else if(targetMinion instanceof Hero) {
				node = battlecryMinion.useTargetableBattlecry_core(side, side.getPlayer(node).getHero(), node, deckPlayer0,
						deckPlayer1);
			} else {
				node = null;
			}

			if(node != null) {
				// Check for dead minions
				node = BoardStateFactoryBase.handleDeadMinions(node, deckPlayer0, deckPlayer1);
				// add the new node to the tree
				boardState.addChild(node);
			}
		}

		return boardState;
	}

	/**
	 * Use an untargetable battlecry.
	 * 
	 * @param minionPlacementTarget
	 * @param boardState
	 * @param deckPlayer0
	 * @param deckPlayer1
	 * @param singleRealizationOnly
	 * @return
	 * @throws HSException
	 */
	public HearthTreeNode useUntargetableBattlecry(Minion minionPlacementTarget, HearthTreeNode boardState,
			Deck deckPlayer0, Deck deckPlayer1, boolean singleRealizationOnly) throws HSException {
		HearthTreeNode toRet = boardState;
		if(this instanceof MinionUntargetableBattlecry) {
			MinionUntargetableBattlecry battlecryMinion = (MinionUntargetableBattlecry)this;
			toRet = battlecryMinion.useUntargetableBattlecry_core(minionPlacementTarget, boardState, deckPlayer0,
					deckPlayer1, singleRealizationOnly);
			if(toRet != null) {
				// Check for dead minions
				toRet = BoardStateFactoryBase.handleDeadMinions(toRet, deckPlayer0, deckPlayer1);
			}
		}
		return toRet;
	}

	/**
	 * 
	 * Places a minion on the board by using the card in hand
	 * 
	 *
	 *
	 * @param side
	 * @param targetMinion The target minion (can be a Hero). The new minion is always placed to the right of (higher index) the target minion. If the target minion is a hero, then it is placed at the left-most position.
	 * @param boardState The BoardState before this card has performed its action. It will be manipulated and returned.
	 * @param deckPlayer0 The deck of player0
	 * @return The boardState is manipulated and returned
	 * @throws HSException
	 */
	@Override
	protected HearthTreeNode use_core(PlayerSide side, Minion targetMinion, HearthTreeNode boardState,
			Deck deckPlayer0, Deck deckPlayer1, boolean singleRealizationOnly) throws HSException {
		if(hasBeenUsed || side == PlayerSide.WAITING_PLAYER || boardState.data_.modelForSide(side).getNumMinions() >= 7)
			return null;

		HearthTreeNode toRet = boardState;
		toRet.data_.getCurrentPlayer().subtractMana(this.getManaCost(PlayerSide.CURRENT_PLAYER, boardState.data_));
		toRet.data_.removeCard_hand(this);
		toRet = this
				.summonMinion(side, targetMinion, boardState, deckPlayer0, deckPlayer1, true, singleRealizationOnly);
		return toRet;
	}

	/**
	 * 
	 * Places a minion on the board via a summon effect
	 * 
	 * This function is meant to be used when summoning minions through means other than a direct card usage.
	 * 
	 *
	 * @param targetSide
	 * @param targetMinion The target minion (can be a Hero). If it is a Hero, then the minion is placed on the last (right most) spot on the board.
	 * @param boardState The BoardState before this card has performed its action. It will be manipulated and returned.
	 * @param deckPlayer0 The deck of player0
	 * @param deckPlayer1 The deck of player1
	 * @param wasTransformed If the minion was 'summoned' as a result of a transform effect (e.g. Hex, Polymorph), set this to true.
	 *
	 * @return The boardState is manipulated and returned
	 */
	public HearthTreeNode summonMinion(PlayerSide targetSide, Minion targetMinion, HearthTreeNode boardState,
			Deck deckPlayer0, Deck deckPlayer1, boolean wasPlayed, boolean singleRealizationOnly) throws HSException {
		if(boardState.data_.modelForSide(targetSide).getNumMinions() >= 7)
			return null;

		HearthTreeNode toRet = boardState;
		toRet = this
				.summonMinion_core(targetSide, targetMinion, toRet, deckPlayer0, deckPlayer1, singleRealizationOnly);

		
		if(this instanceof MinionUntargetableBattlecry) {
			toRet = this.useUntargetableBattlecry(targetMinion, toRet, deckPlayer0, deckPlayer1,
					singleRealizationOnly);
		}
		
		if(this instanceof MinionTargetableBattlecry) {
			EnumSet<BattlecryTargetType> targets = ((MinionTargetableBattlecry)this).getBattlecryTargets();
		
			// Battlecry if available
			for(BattlecryTargetType btt : targets) {
				switch (btt) {
					case ENEMY_HERO:
						toRet = this.useTargetableBattlecry(PlayerSide.WAITING_PLAYER,
								PlayerSide.WAITING_PLAYER.getPlayer(toRet).getHero(), toRet, deckPlayer0, deckPlayer1);
						break;
					case FRIENDLY_HERO:
						toRet = this.useTargetableBattlecry(PlayerSide.CURRENT_PLAYER,
								PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getHero(), toRet, deckPlayer0, deckPlayer1);
						break;
					case ENEMY_MINIONS:
						for(Minion minion : PlayerSide.WAITING_PLAYER.getPlayer(toRet).getMinions()) {
							if (!minion.getStealthed())
								toRet = this.useTargetableBattlecry(PlayerSide.WAITING_PLAYER, minion, toRet, deckPlayer0,
										deckPlayer1);
						}
						break;
					case FRIENDLY_MINIONS:
						for(Minion minion : PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getMinions()) {
							if(minion != this)
								toRet = this.useTargetableBattlecry(PlayerSide.CURRENT_PLAYER, minion, toRet, deckPlayer0,
										deckPlayer1);
						}
						break;
					case ENEMY_BEASTS:
						for(Minion minion : PlayerSide.WAITING_PLAYER.getPlayer(toRet).getMinions()) {
							if(minion.getTribe() == MinionTribe.BEAST)
								toRet = this.useTargetableBattlecry(PlayerSide.WAITING_PLAYER, minion, toRet, deckPlayer0,
										deckPlayer1);
						}
						break;
					case FRIENDLY_BEASTS:
						for(Minion minion : PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getMinions()) {
							if(minion != this && minion.getTribe() == MinionTribe.BEAST)
								toRet = this.useTargetableBattlecry(PlayerSide.CURRENT_PLAYER, minion, toRet, deckPlayer0,
										deckPlayer1);
						}
						break;
					case ENEMY_MURLOCS:
						for(Minion minion : PlayerSide.WAITING_PLAYER.getPlayer(toRet).getMinions()) {
							if(minion.getTribe() == MinionTribe.MURLOC)
								toRet = this.useTargetableBattlecry(PlayerSide.WAITING_PLAYER, minion, toRet, deckPlayer0,
										deckPlayer1);
						}
						break;
					case FRIENDLY_MURLOCS:
						for(Minion minion : PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getMinions()) {
							if(minion != this && minion.getTribe() == MinionTribe.MURLOC)
								toRet = this.useTargetableBattlecry(PlayerSide.CURRENT_PLAYER, minion, toRet, deckPlayer0,
										deckPlayer1);
						}
						break;
					default:
						break;
				}
			}
		}

		if(wasPlayed)
			toRet = this.notifyMinionPlayed(toRet, targetSide, deckPlayer0, deckPlayer1);

		toRet = this.notifyMinionSummon(toRet, targetSide, deckPlayer0, deckPlayer1);

		return toRet;
	}

	/**
	 * 
	 * Places a minion on the board via a summon effect
	 * 
	 * This function is meant to be used when summoning minions through means other than a direct card usage.
	 * 
	 *
	 * @param targetSide
	 * @param targetMinion The target minion (can be a Hero). The new minion is always placed to the right of (higher index) the target minion. If the target minion is a hero, then it is placed at the left-most position.
	 * @param boardState The BoardState before this card has performed its action. It will be manipulated and returned.
	 * @return The boardState is manipulated and returned
	 * @param deckPlayer0 The deck of player0
	 * @param deckPlayer1 The deck of player1
	 * @throws HSException
	 */
	protected HearthTreeNode summonMinion_core(PlayerSide targetSide, Minion targetMinion, HearthTreeNode boardState,
			Deck deckPlayer0, Deck deckPlayer1, boolean singleRealizationOnly) throws HSException {
		HearthTreeNode toRet = this.placeMinion(targetSide, targetMinion, boardState, deckPlayer0, deckPlayer1,
				singleRealizationOnly);
		if(!charge_) {
			hasAttacked_ = true;
		}
		hasBeenUsed = true;
		return toRet;
	}

	public HearthTreeNode placeMinion(PlayerSide targetSide, Minion targetMinion, HearthTreeNode boardState,
			Deck deckPlayer0, Deck deckPlayer1, boolean singleRealizationOnly) throws HSException {
		if(isHero(targetMinion))
			boardState.data_.placeMinion(targetSide, this, 0);
		else
			boardState.data_.placeMinion(targetSide, this,
					targetSide.getPlayer(boardState).getMinions().indexOf(targetMinion) + 1);
		return this.notifyMinionPlacement(boardState, targetSide, deckPlayer0, deckPlayer1);
	}

	/**
	 * 
	 * Attack with the minion
	 * 
	 *
	 *
	 * @param targetMinionPlayerSide
	 * @param targetMinion The target minion
	 * @param boardState The BoardState before this card has performed its action. It will be manipulated and returned.
	 * @param deckPlayer0 The deck of player0
	 * @return The boardState is manipulated and returned
	 */
	public HearthTreeNode attack(PlayerSide targetMinionPlayerSide, Minion targetMinion, HearthTreeNode boardState,
			Deck deckPlayer0, Deck deckPlayer1) throws HSException {

		// can't attack a stealthed target
		if(targetMinion.getStealthed())
			return null;

		if(!this.canAttack()) {
			return null;
		}
		
		if(targetMinionPlayerSide == PlayerSide.CURRENT_PLAYER) {
			return null;
		}
		
		// Notify all that an attack is beginning
		HearthTreeNode toRet = boardState;
		int attackerIndex = this instanceof Hero ? 0 : PlayerSide.CURRENT_PLAYER.getPlayer(boardState).getMinions()
				.indexOf(this) + 1;
		int targetIndex = targetMinion instanceof Hero ? 0 : targetMinionPlayerSide.getPlayer(boardState).getMinions()
				.indexOf(targetMinion) + 1;

		// Do the actual attack
		toRet = this.attack_core(targetMinionPlayerSide, targetMinion, boardState, deckPlayer0, deckPlayer1);

		// check for and remove dead minions
		if(toRet != null) {
			toRet.setAction(new HearthAction(Verb.ATTACK, PlayerSide.CURRENT_PLAYER, attackerIndex,
					targetMinionPlayerSide, targetIndex));
			toRet = BoardStateFactoryBase.handleDeadMinions(toRet, deckPlayer0, deckPlayer1);
		}

		// Attacking means you lose stealth
		if(toRet != null)
			this.stealthed_ = false;

		return toRet;
	}

	/**
	 * 
	 * Attack with the minion
	 * 
	 *
	 *
	 * @param targetMinionPlayerSide
	 * @param targetMinion The target minion
	 * @param boardState The BoardState before this card has performed its action. It will be manipulated and returned.
	 * @param deckPlayer0 The deck of player0
	 * @return The boardState is manipulated and returned
	 */
	protected HearthTreeNode attack_core(PlayerSide targetMinionPlayerSide, Minion targetMinion,
			HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1) throws HSException {

		HearthTreeNode toRet = boardState;
		byte origAttack = targetMinion.getTotalAttack();
		toRet = targetMinion.takeDamage(this.getTotalAttack(), PlayerSide.CURRENT_PLAYER, targetMinionPlayerSide,
				toRet, deckPlayer0, deckPlayer1, false, false);
		toRet = this.takeDamage(origAttack, targetMinionPlayerSide, PlayerSide.CURRENT_PLAYER, toRet, deckPlayer0,
				deckPlayer1, false, false);
		if(windFury_ && !hasWindFuryAttacked_)
			hasWindFuryAttacked_ = true;
		else
			hasAttacked_ = true;
		return toRet;

	}

	// ======================================================================================
	// Various notifications
	// ======================================================================================
	protected HearthTreeNode notifyMinionSummon(HearthTreeNode boardState, PlayerSide targetSide, Deck deckPlayer0,
			Deck deckPlayer1) throws HSException {
		HearthTreeNode toRet = boardState;
		ArrayList<MinionSummonedInterface> matches = new ArrayList<MinionSummonedInterface>();

		Card hero = toRet.data_.getCurrentPlayerHero();
		if(hero instanceof MinionSummonedInterface) {
			matches.add((MinionSummonedInterface)hero);
		}

		for(Minion minion : PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getMinions()) {
			if(!minion.isSilenced() && minion instanceof MinionSummonedInterface) {
				matches.add((MinionSummonedInterface)minion);
			}
		}

		for(MinionSummonedInterface match : matches) {
			toRet = match.minionSummonEvent(PlayerSide.CURRENT_PLAYER, targetSide, this,
					toRet, deckPlayer0, deckPlayer1);
		}
		matches.clear();

		hero = toRet.data_.getWaitingPlayerHero();
		if(hero instanceof MinionSummonedInterface) {
			matches.add((MinionSummonedInterface)hero);
		}

		for(Minion minion : PlayerSide.WAITING_PLAYER.getPlayer(toRet).getMinions()) {
			if(!minion.isSilenced() && minion instanceof MinionSummonedInterface) {
				matches.add((MinionSummonedInterface)minion);
			}
		}

		for(MinionSummonedInterface match : matches) {
			toRet = match.minionSummonEvent(PlayerSide.WAITING_PLAYER, targetSide, this, toRet, deckPlayer0,
					deckPlayer1);
		}
		return toRet;
	}

	protected HearthTreeNode notifyMinionPlacement(HearthTreeNode boardState, PlayerSide targetSide, Deck deckPlayer0,
			Deck deckPlayer1) throws HSException {
		HearthTreeNode toRet = boardState;

		ArrayList<MinionPlacedInterface> matches = new ArrayList<MinionPlacedInterface>();

		Card hero = toRet.data_.getCurrentPlayerHero();
		if(hero instanceof MinionPlacedInterface) {
			matches.add((MinionPlacedInterface)hero);
		}

		for(Minion minion : PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getMinions()) {
			if(!minion.isSilenced() && minion instanceof MinionPlacedInterface) {
				matches.add((MinionPlacedInterface)minion);
			}
		}

		for(MinionPlacedInterface match : matches) {
			toRet = match.minionPlacedEvent(PlayerSide.CURRENT_PLAYER, targetSide, this,
					toRet, deckPlayer0, deckPlayer1);
		}
		matches.clear();

		hero = toRet.data_.getWaitingPlayerHero();
		if(hero instanceof MinionPlacedInterface) {
			matches.add((MinionPlacedInterface)hero);
		}

		for(Minion minion : PlayerSide.WAITING_PLAYER.getPlayer(toRet).getMinions()) {
			if(!minion.isSilenced() && minion instanceof MinionPlacedInterface) {
				matches.add((MinionPlacedInterface)minion);
			}
		}

		for(MinionPlacedInterface match : matches) {
			toRet = match.minionPlacedEvent(PlayerSide.WAITING_PLAYER, targetSide, this, toRet, deckPlayer0,
					deckPlayer1);
		}

		return toRet;
	}

	protected HearthTreeNode notifyMinionPlayed(HearthTreeNode boardState, PlayerSide targetSide, Deck deckPlayer0,
			Deck deckPlayer1) throws HSException {
		HearthTreeNode toRet = boardState;
		ArrayList<MinionPlayedInterface> matches = new ArrayList<MinionPlayedInterface>();

		Card hero = toRet.data_.getCurrentPlayerHero();
		if(hero instanceof MinionPlayedInterface) {
			matches.add((MinionPlayedInterface)hero);
		}

		for(Minion minion : PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getMinions()) {
			if(!minion.isSilenced() && minion instanceof MinionPlayedInterface) {
				matches.add((MinionPlayedInterface)minion);
			}
		}

		for(MinionPlayedInterface match : matches) {
			toRet = match.minionPlayedEvent(PlayerSide.CURRENT_PLAYER, targetSide, this,
					toRet, deckPlayer0, deckPlayer1);
		}
		matches.clear();

		hero = toRet.data_.getWaitingPlayerHero();
		if(hero instanceof MinionPlayedInterface) {
			matches.add((MinionPlayedInterface)hero);
		}

		for(Minion minion : PlayerSide.WAITING_PLAYER.getPlayer(toRet).getMinions()) {
			if(!minion.isSilenced() && minion instanceof MinionPlayedInterface) {
				matches.add((MinionPlayedInterface)minion);
			}
		}

		for(MinionPlayedInterface match : matches) {
			toRet = match.minionPlayedEvent(PlayerSide.WAITING_PLAYER, targetSide, this, toRet, deckPlayer0,
					deckPlayer1);
		}
		
		return toRet;
	}

	protected HearthTreeNode notifyMinionDamaged(HearthTreeNode boardState, PlayerSide targetSide, Deck deckPlayer0,
			Deck deckPlayer1) throws HSException {
		HearthTreeNode toRet = boardState;
		ArrayList<MinionDamagedInterface> matches = new ArrayList<MinionDamagedInterface>();

		Card hero = toRet.data_.getCurrentPlayerHero();
		if(hero instanceof MinionDamagedInterface) {
			matches.add((MinionDamagedInterface)hero);
		}

		for(Minion minion : PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getMinions()) {
			if(!minion.isSilenced() && minion instanceof MinionDamagedInterface) {
				matches.add((MinionDamagedInterface)minion);
			}
		}

		for(MinionDamagedInterface match : matches) {
			toRet = match.minionDamagedEvent(PlayerSide.CURRENT_PLAYER, targetSide, this,
					toRet, deckPlayer0, deckPlayer1);
		}
		matches.clear();

		hero = toRet.data_.getWaitingPlayerHero();
		if(hero instanceof MinionDamagedInterface) {
			matches.add((MinionDamagedInterface)hero);
		}

		for(Minion minion : PlayerSide.WAITING_PLAYER.getPlayer(toRet).getMinions()) {
			if(!minion.isSilenced() && minion instanceof MinionDamagedInterface) {
				matches.add((MinionDamagedInterface)minion);
			}
		}

		for(MinionDamagedInterface match : matches) {
			toRet = match.minionDamagedEvent(PlayerSide.WAITING_PLAYER, targetSide, this, toRet, deckPlayer0,
					deckPlayer1);
		}

		return toRet;
	}

	protected HearthTreeNode notifyMinionDead(PlayerSide deadMinionPlayerSide,
			Minion deadMinion, HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1) throws HSException {
		HearthTreeNode toRet = boardState;
		ArrayList<MinionDeadInterface> matches = new ArrayList<MinionDeadInterface>();

		Card hero = toRet.data_.getCurrentPlayerHero();
		if(hero instanceof MinionDeadInterface) {
			matches.add((MinionDeadInterface)hero);
		}

		for(Minion minion : PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getMinions()) {
			if(!minion.isSilenced() && minion instanceof MinionDeadInterface) {
				matches.add((MinionDeadInterface)minion);
			}
		}

		for(MinionDeadInterface match : matches) {
			toRet = match.minionDeadEvent(PlayerSide.CURRENT_PLAYER, deadMinionPlayerSide, deadMinion,
					toRet, deckPlayer0, deckPlayer1);
		}
		matches.clear();

		hero = toRet.data_.getWaitingPlayerHero();
		if(hero instanceof MinionDeadInterface) {
			matches.add((MinionDeadInterface)hero);
		}

		for(Minion minion : PlayerSide.WAITING_PLAYER.getPlayer(toRet).getMinions()) {
			if(!minion.isSilenced() && minion instanceof MinionDeadInterface) {
				matches.add((MinionDeadInterface)minion);
			}
		}

		for(MinionDeadInterface match : matches) {
			toRet = match.minionDeadEvent(PlayerSide.WAITING_PLAYER, deadMinionPlayerSide, deadMinion, toRet, deckPlayer0,
					deckPlayer1);
		}

		return toRet;
	}

	protected HearthTreeNode notifyMinionHealed(HearthTreeNode boardState, PlayerSide targetSide, Deck deckPlayer0,
			Deck deckPlayer1) throws HSException {
		HearthTreeNode toRet = boardState;
		ArrayList<MinionHealedInterface> matches = new ArrayList<MinionHealedInterface>();

		Card hero = toRet.data_.getCurrentPlayerHero();
		if(hero instanceof MinionHealedInterface) {
			matches.add((MinionHealedInterface)hero);
		}

		for(Minion minion : PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getMinions()) {
			if(!minion.isSilenced() && minion instanceof MinionHealedInterface) {
				matches.add((MinionHealedInterface)minion);
			}
		}

		for(MinionHealedInterface match : matches) {
			toRet = match.minionHealedEvent(PlayerSide.CURRENT_PLAYER, targetSide, this,
					toRet, deckPlayer0, deckPlayer1);
		}
		matches.clear();

		hero = toRet.data_.getWaitingPlayerHero();
		if(hero instanceof MinionHealedInterface) {
			matches.add((MinionHealedInterface)hero);
		}

		for(Minion minion : PlayerSide.WAITING_PLAYER.getPlayer(toRet).getMinions()) {
			if(!minion.isSilenced() && minion instanceof MinionHealedInterface) {
				matches.add((MinionHealedInterface)minion);
			}
		}

		for(MinionHealedInterface match : matches) {
			toRet = match.minionHealedEvent(PlayerSide.WAITING_PLAYER, targetSide, this, toRet, deckPlayer0,
					deckPlayer1);
		}

		return toRet;
	}

	@Override
	public JSONObject toJSON() {
		JSONObject json = super.toJSON();
		json.put("attack", attack_);
		json.put("baseAttack", baseAttack_);
		if(health_ != maxHealth_) json.put("health", health_);
		json.put("baseHealth", baseHealth_);
		json.put("maxHealth", maxHealth_);
		if(taunt_) json.put("taunt", taunt_);
		if(divineShield_) json.put("divineShield", divineShield_);
		if(windFury_) json.put("windFury", windFury_);
		if(charge_) json.put("charge", charge_);
		if(frozen_) json.put("frozen", frozen_);
		if(silenced_) json.put("silenced", silenced_);
		if(hasAttacked_) json.put("hasAttacked", hasAttacked_);
		if(spellDamage_ != 0) json.put("spellDamage", spellDamage_);
		return json;
	}

	/**
	 * Deep copy of the object
	 * 
	 * Note: the event actions are not actually deep copied.
	 */
	@Override
	public Card deepCopy() {

		Minion minion = null;
		try {
			minion = getClass().newInstance();
		} catch(InstantiationException e) {
			log.error("instantiation error", e);
		} catch(IllegalAccessException e) {
			log.error("illegal access error", e);
		}
		if(minion == null) {
			throw new RuntimeException("unable to instantiate minion.");
		}

		minion.name_ = name_;
        minion.baseManaCost = baseManaCost;
		minion.attack_ = attack_;
		minion.health_ = health_;
		minion.baseAttack_ = baseAttack_;
		minion.extraAttackUntilTurnEnd_ = extraAttackUntilTurnEnd_;
		minion.auraAttack_ = auraAttack_;
		minion.baseHealth_ = baseHealth_;
		minion.maxHealth_ = maxHealth_;
		minion.auraHealth_ = auraHealth_;
		minion.spellDamage_ = spellDamage_;
		minion.taunt_ = taunt_;
		minion.divineShield_ = divineShield_;
		minion.windFury_ = windFury_;
		minion.charge_ = charge_;
		minion.hasAttacked_ = hasAttacked_;
		minion.hasWindFuryAttacked_ = hasWindFuryAttacked_;
		minion.frozen_ = frozen_;
		minion.silenced_ = silenced_;
		minion.stealthed_ = stealthed_;
		minion.heroTargetable_ = heroTargetable_;
		minion.destroyOnTurnStart_ = destroyOnTurnStart_;
		minion.destroyOnTurnEnd_ = destroyOnTurnEnd_;
		minion.deathrattleAction_ = deathrattleAction_;
		minion.attackAction_ = attackAction_;
		minion.isInHand_ = isInHand_;
		minion.hasBeenUsed = hasBeenUsed;
		// TODO: continue here.

		return minion;
	}

	@Override
	public boolean equals(Object other) {
		if(!super.equals(other)) {
			return false;
		}

		Minion otherMinion = (Minion)other;
		if(health_ != otherMinion.health_)
			return false;
		if(maxHealth_ != otherMinion.maxHealth_)
			return false;
		if(baseHealth_ != otherMinion.baseHealth_)
			return false;
		if(auraHealth_ != otherMinion.auraHealth_)
			return false;

		if(attack_ != otherMinion.attack_)
			return false;
		if(baseAttack_ != otherMinion.baseAttack_)
			return false;
		if(extraAttackUntilTurnEnd_ != otherMinion.extraAttackUntilTurnEnd_)
			return false;
		if(auraAttack_ != otherMinion.auraAttack_)
			return false;

		if(taunt_ != otherMinion.taunt_)
			return false;
		if(divineShield_ != otherMinion.divineShield_)
			return false;
		if(windFury_ != otherMinion.windFury_)
			return false;
		if(charge_ != otherMinion.charge_)
			return false;
		if(stealthed_ != otherMinion.stealthed_)
			return false;
		if(hasAttacked_ != otherMinion.hasAttacked_)
			return false;
		if(heroTargetable_ != otherMinion.heroTargetable_)
			return false;
		if(hasWindFuryAttacked_ != otherMinion.hasWindFuryAttacked_)
			return false;
		if(frozen_ != otherMinion.frozen_)
			return false;
		if(silenced_ != otherMinion.silenced_)
			return false;
		if(destroyOnTurnStart_ != otherMinion.destroyOnTurnStart_)
			return false;
		if(destroyOnTurnEnd_ != otherMinion.destroyOnTurnEnd_)
			return false;

		if(spellDamage_ != otherMinion.spellDamage_)
			return false;

		// This is checked for reference equality
		if(deathrattleAction_ != ((Minion)other).deathrattleAction_)
			return false;

		// This is checked for reference equality
		if(attackAction_ != ((Minion)other).attackAction_)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (taunt_ ? 1 : 0);
		result = 31 * result + (divineShield_ ? 1 : 0);
		result = 31 * result + (windFury_ ? 1 : 0);
		result = 31 * result + (charge_ ? 1 : 0);
		result = 31 * result + (hasAttacked_ ? 1 : 0);
		result = 31 * result + (hasWindFuryAttacked_ ? 1 : 0);
		result = 31 * result + (frozen_ ? 1 : 0);
		result = 31 * result + (silenced_ ? 1 : 0);
		result = 31 * result + (stealthed_ ? 1 : 0);
		result = 31 * result + (heroTargetable_ ? 1 : 0);
		result = 31 * result + health_;
		result = 31 * result + maxHealth_;
		result = 31 * result + baseHealth_;
		result = 31 * result + auraHealth_;
		result = 31 * result + attack_;
		result = 31 * result + baseAttack_;
		result = 31 * result + extraAttackUntilTurnEnd_;
		result = 31 * result + auraAttack_;
		result = 31 * result + (destroyOnTurnStart_ ? 1 : 0);
		result = 31 * result + (destroyOnTurnEnd_ ? 1 : 0);
		result = 31 * result + spellDamage_;
		result = 31 * result + (deathrattleAction_ != null ? deathrattleAction_.hashCode() : 0);
		result = 31 * result + (attackAction_ != null ? attackAction_.hashCode() : 0);
		result = 31 * result + (placementImportant_ ? 1 : 0);
		return result;
	}

	public boolean currentPlayerBoardFull(HearthTreeNode boardState) {
		return PlayerSide.CURRENT_PLAYER.getPlayer(boardState).getNumMinions() >= 7;
	}

}
