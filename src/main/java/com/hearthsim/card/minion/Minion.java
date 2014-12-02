package com.hearthsim.card.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.ImplementedCardList;
import com.hearthsim.event.attack.AttackAction;
import com.hearthsim.event.deathrattle.DeathrattleAction;
import com.hearthsim.exception.HSException;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.factory.BoardStateFactoryBase;
import com.hearthsim.util.tree.HearthTreeNode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;

public class Minion extends Card {

	public enum BattlecryTargetType {
		NO_BATTLECRY,
		NO_TARGET,
		FRIENDLY_HERO, ENEMY_HERO,
    	FRIENDLY_MINIONS, ENEMY_MINIONS,
    	FRIENDLY_BEASTS, ENEMY_BEASTS,
    	FRIENDLY_MURLOCS, ENEMY_MURLOCS
    }
	
	MinionStateFactory mf = new MinionStateFactory();
	public ArrayList<MinionState> minion_stateList; 
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
	
	protected boolean summoned_;
	protected boolean transformed_;
	
	protected boolean destroyOnTurnStart_;
	protected boolean destroyOnTurnEnd_;

	protected byte spellDamage_;

	protected DeathrattleAction deathrattleAction_;
	protected AttackAction attackAction_;
	
	//This is a flag to tell the BoardState that it can't cheat on the placement of this minion
	protected boolean placementImportant_ = false;

    public Minion() {
        super();
        minion_stateList = new ArrayList<MinionState>();
        ImplementedCardList cardList = ImplementedCardList.getInstance();
        ImplementedCardList.ImplementedCard implementedCard = cardList.getCardForClass(this.getClass());
        if (implementedCard!=null){
            // only 'Minion' class is not implemented
            mana_ = (byte) implementedCard.mana_;
            name_ = implementedCard.name_;
            attack_ = (byte) implementedCard.attack_;
            baseAttack_ = attack_;
            health_ = (byte) implementedCard.health_;
            maxHealth_ = health_;
            baseHealth_ = health_;
            taunt_ = implementedCard.taunt_;
            divineShield_ = implementedCard.divineShield_;
            windFury_ = implementedCard.windfury_;
            charge_ = implementedCard.charge_;
            stealthed_ = implementedCard.stealth_;
            isInHand_ = true;
            //todo: spellpower could be deduced from text quite easily
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
    // pass the minionstate_list to slowly replace the implementation
	public Minion(String name, byte mana, byte attack, byte health, byte baseAttack, byte baseHealth, byte maxHealth) {
		this(
				name,
				mana,
				attack,
				health,
				baseAttack,
				(byte)0,
				(byte)0,
				baseHealth,
				maxHealth,
				(byte)0,
				(byte)0,
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
				false,
				false,
				false,
				false,
				null,
				null,
				true,
				false);
	}
	// pass the minionstate_list to slowly replace the implementation
	public Minion(	String name,
					byte mana,
					byte attack,
					byte health,
					byte baseAttack,
					byte extraAttackUntilTurnEnd,
					byte auraAttack,
					byte baseHealth,
					byte maxHealth,
					byte auraHealth,
					byte spellDamage,
					boolean taunt,
					boolean divineShield,
					boolean windFury,
					boolean charge,
					boolean hasAttacked,
					boolean hasWindFuryAttacked,
					boolean frozen,
					boolean silenced,
					boolean stealthed,
					boolean heroTargetable,
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

		auraAttack_ = auraAttack;
		auraHealth_ = auraHealth;

		spellDamage_ = spellDamage;

		stealthed_ = stealthed;
		heroTargetable_ = heroTargetable;
	}
	
	public Minion(	String name,
			byte mana,
			byte attack,
			byte health,
			byte baseAttack,
			byte extraAttackUntilTurnEnd,
			byte auraAttack,
			byte baseHealth,
			byte maxHealth,
			byte auraHealth,
			byte spellDamage,
			boolean taunt,
			boolean divineShield,
			boolean silenced,
			boolean hasAttacked,
			boolean hasWindFuryAttacked,
			ArrayList<MinionState> ms,
			boolean heroTargetable,
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
		//windFury_ = windFury;
		//charge_ = charge;
		hasAttacked_ = hasAttacked;
		baseAttack_ = baseAttack;
		extraAttackUntilTurnEnd_ = extraAttackUntilTurnEnd;
		hasWindFuryAttacked_ = hasWindFuryAttacked;
		//frozen_ = frozen;
		silenced_ = silenced;
		baseHealth_ = baseHealth;
		maxHealth_ = maxHealth;
		summoned_ = summoned;
		transformed_ = transformed;
		destroyOnTurnStart_ = destroyOnTurnStart;
		destroyOnTurnEnd_ = destroyOnTurnEnd;
		deathrattleAction_ = deathrattleAction;
		attackAction_ = attackAction;
		
		auraAttack_ = auraAttack;
		auraHealth_ = auraHealth;
		
		spellDamage_ = spellDamage;
		
		//stealthed_ = stealthed;
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
	
	public MinionState getState(MinionState state){
		int index = 0;
		int count = 0;
		boolean exists = false;
		if(minion_stateList != null){
			for(MinionState ms: minion_stateList){
				if(ms.getClass().equals(state.getClass())){
					exists = true;
					index = count;
				}
				count++;
			}
		
		if(exists == true)
			return minion_stateList.get(index);
		else
			return null;
		}
		else
			return null;
	}
	
	public void addState(MinionState state){
		boolean stateExists = false;
		if(minion_stateList != null){
			for(MinionState ms: minion_stateList){
				if(ms.getClass().equals(state.getClass())){
					stateExists = true;
				}
			}
			if (stateExists == false)
				minion_stateList.add(state);
		}
	}
	
	public void removeState(MinionState state){
		int count = 0;
		int removeIndex = 0;
		boolean exists = false;
		if(minion_stateList != null){
			for(MinionState ms: minion_stateList){
				if(ms.getClass().equals(state.getClass())){
					exists = true;
					removeIndex = count;
				}
				count++;
			}
			if (exists==true)
				minion_stateList.remove(removeIndex);
		}
	}
		
	public boolean getWindfury() {
		return windFury_;
	}
	
	public void setWindfury(boolean value) {
		windFury_ = value;
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

	
	public void addAuraHealth(byte value) {
		auraHealth_ += value;
	}
	
	public void removeAuraHealth(byte value) {
		health_ += value;
		if (health_ > maxHealth_)
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

    /**
	 * Called at the start of the turn
	 * 
	 * This function is called at the start of the turn.  Any derived class must override it to implement whatever
	 * "start of the turn" effect the card has.
	 */
	@Override
	public HearthTreeNode startTurn(PlayerSide thisMinionPlayerIndex, HearthTreeNode boardModel, Deck deckPlayer0, Deck deckPlayer1) throws HSException {
		HearthTreeNode toRet = boardModel;
		if (destroyOnTurnStart_) {
//			toRet = this.destroyed(thisMinionPlayerIndex, toRet, deckPlayer0, deckPlayer1);
			this.setHealth((byte)-99);
		}
		return toRet;
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
	public HearthTreeNode endTurn(PlayerSide thisMinionPlayerIndex, HearthTreeNode boardModel, Deck deckPlayer0, Deck deckPlayer1) throws HSException {
		extraAttackUntilTurnEnd_ = 0;
		HearthTreeNode toRet = boardModel;
		if (destroyOnTurnEnd_) {
//			toRet = this.destroyed(thisMinionPlayerIndex, toRet, deckPlayer0, deckPlayer1);
			this.setHealth((byte)-99);
		}
		return toRet;
	}
	
	/**
	 * Called when this minion takes damage
	 * 
	 * Always use this function to take damage... it properly notifies all others of its damage and possibly of its death
	 *  @param damage The amount of damage to take
	 * @param attackPlayerSide The player index of the attacker.  This is needed to do things like +spell damage.
     * @param thisPlayerSide
     * @param boardState
     * @param deckPlayer0 The deck of player0
     * @param isSpellDamage True if this is a spell damage
     * @param handleMinionDeath Set this to True if you want the death event to trigger when (if) the minion dies from this damage.  Setting this flag to True will also trigger deathrattle immediately.
     * @throws HSInvalidPlayerIndexException
	 */
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
	{
		
		if (!divineShield_) {
		//if (getState(mf.makeDivineShield()) == null){
			byte totalDamage = isSpellDamage ? (byte)(damage + boardState.data_.getSpellDamage(attackPlayerSide)) : damage;
			health_ = (byte)(health_ - totalDamage);
			
			//Notify all that the minion is damaged
			HearthTreeNode toRet = boardState;
			toRet = toRet.data_.getCurrentPlayerHero().minionDamagedEvent(PlayerSide.CURRENT_PLAYER, thisPlayerSide, this, toRet, deckPlayer0, deckPlayer1);
			for (int j = 0; j < PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getNumMinions(); ++j) {
				if (!PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getMinions().get(j).silenced_)
					toRet = PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getMinions().get(j).minionDamagedEvent(PlayerSide.CURRENT_PLAYER, thisPlayerSide, this, toRet, deckPlayer0, deckPlayer1);
			}
			toRet = toRet.data_.getWaitingPlayerHero().minionDamagedEvent(PlayerSide.WAITING_PLAYER, thisPlayerSide, this, toRet, deckPlayer0, deckPlayer1);
			for (int j = 0; j < PlayerSide.WAITING_PLAYER.getPlayer(toRet).getNumMinions(); ++j) {
				if (!PlayerSide.WAITING_PLAYER.getPlayer(toRet).getMinions().get(j).silenced_)
					toRet = PlayerSide.WAITING_PLAYER.getPlayer(toRet).getMinions().get(j).minionDamagedEvent(PlayerSide.WAITING_PLAYER, thisPlayerSide, this, toRet, deckPlayer0, deckPlayer1);
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
	 *
     *
     * @param thisPlayerSide
     * @param boardState
     * @param deckPlayer0
     * @param deckPlayer1
     *
     * @throws HSInvalidPlayerIndexException
	 */
	public HearthTreeNode destroyed(PlayerSide thisPlayerSide, HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1) throws HSException {

        health_ = 0;
        HearthTreeNode toRet = boardState;

        //perform the deathrattle action if there is one
        if (deathrattleAction_ != null) {
            toRet =  deathrattleAction_.performAction(this, thisPlayerSide, toRet, deckPlayer0, deckPlayer1);
        }

        //Notify all that it is dead
        toRet = toRet.data_.getCurrentPlayerHero().minionDeadEvent(PlayerSide.CURRENT_PLAYER, thisPlayerSide, this, toRet, deckPlayer0, deckPlayer1);
        for (int j = 0; j < PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getNumMinions(); ++j) {
            if (!PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getMinions().get(j).silenced_)
                toRet = PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getMinions().get(j).minionDeadEvent(PlayerSide.CURRENT_PLAYER, thisPlayerSide, this, toRet, deckPlayer0, deckPlayer1);
        }
        toRet = toRet.data_.getWaitingPlayerHero().minionDeadEvent(PlayerSide.WAITING_PLAYER, thisPlayerSide, this, toRet, deckPlayer0, deckPlayer1);
        for (int j = 0; j < PlayerSide.WAITING_PLAYER.getPlayer(toRet).getNumMinions(); ++j) {
            if (!PlayerSide.WAITING_PLAYER.getPlayer(toRet).getMinions().get(j).silenced_)
                toRet = PlayerSide.WAITING_PLAYER.getPlayer(toRet).getMinions().get(j).minionDeadEvent(PlayerSide.WAITING_PLAYER, thisPlayerSide, this, toRet, deckPlayer0, deckPlayer1);
        }

        return toRet;

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
		if (!silenced_) {
			boardState.setSpellDamage(PlayerSide.CURRENT_PLAYER, (byte)(boardState.getSpellDamage(PlayerSide.CURRENT_PLAYER) - spellDamage_));
		}

		divineShield_ = false;
		taunt_ = false;
		charge_ = false;
		frozen_ = false;
		windFury_ = false;
		silenced_ = true;
		deathrattleAction_ = null;
		stealthed_ = false;
		heroTargetable_ = true;
		
		//Reset the attack and health to base
		this.attack_ = this.baseAttack_;
		if (this.maxHealth_ > this.baseHealth_) {
			this.maxHealth_ = this.baseHealth_;
			if (this.health_ > this.maxHealth_)
				this.health_ = this.maxHealth_;
		}
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
	public HearthTreeNode takeHeal(byte healAmount, PlayerSide thisPlayerSide, HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1) throws HSInvalidPlayerIndexException {
		
		if (health_ < maxHealth_) {
			if (health_ + healAmount > maxHealth_)
				health_ = maxHealth_;
			else
				health_ = (byte)(health_ + healAmount);
			
			//Notify all that it the minion is healed
			HearthTreeNode toRet = boardState;
			toRet = toRet.data_.getCurrentPlayerHero().minionHealedEvent(PlayerSide.CURRENT_PLAYER, thisPlayerSide, this, toRet, deckPlayer0, deckPlayer1);
            for (Minion minion : PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getMinions()) {
                if (!minion.silenced_)
                    toRet = minion.minionHealedEvent(PlayerSide.CURRENT_PLAYER, thisPlayerSide, this, toRet, deckPlayer0, deckPlayer1);
            }
			toRet = toRet.data_.getWaitingPlayerHero().minionHealedEvent(PlayerSide.WAITING_PLAYER, thisPlayerSide, this, toRet, deckPlayer0, deckPlayer1);
            for (Minion minion : PlayerSide.WAITING_PLAYER.getPlayer(toRet).getMinions()) {
                if (!minion.silenced_)
                    toRet = minion.minionHealedEvent(PlayerSide.WAITING_PLAYER, thisPlayerSide, this, toRet, deckPlayer0, deckPlayer1);
            }
			return toRet;
		}
		return boardState;
	}
	
	
	@Override
    public boolean canBeUsedOn(PlayerSide playerSide, Minion minion, BoardModel boardModel) {
        return playerSide != PlayerSide.WAITING_PLAYER && !hasBeenUsed;
    }

	/**
	 * Use a targetable battlecry.
	 * 
	 * @param side
	 * @param targetMinion
	 * @param boardState
	 * @param deckPlayer0
	 * @param deckPlayer1
	 * @return
	 * @throws HSException
	 */
	public HearthTreeNode useTargetableBattlecry(
			PlayerSide side,
			Minion targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1
		) throws HSException
	{
		HearthTreeNode node = new HearthTreeNode((BoardModel)boardState.data_.deepCopy());
		int targetMinionIndex = side.getPlayer(boardState).getMinions().indexOf(targetMinion);
		if (targetMinionIndex >= 0) {
			node = this.useTargetableBattlecry_core(side, side.getPlayer(node).getMinions().get(targetMinionIndex), node, deckPlayer0, deckPlayer1);
		} else if (targetMinion instanceof Hero) {
			node = this.useTargetableBattlecry_core(side, side.getPlayer(node).getHero(), node, deckPlayer0, deckPlayer1);
		} else {
			node = null;
		}
		if (node != null) {
			//Check for dead minions
			node = BoardStateFactoryBase.handleDeadMinions(node, deckPlayer0, deckPlayer1);
			//add the new node to the tree
			boardState.addChild(node);
		}
		return boardState;
	}

	/**
	 * Derived classes should implement this function for targtable battlecries.
	 * 
	 * @param side
	 * @param targetMinion
	 * @param boardState
	 * @param deckPlayer0
	 * @param deckPlayer1
	 * @return
	 * @throws HSException
	 */
	public HearthTreeNode useTargetableBattlecry_core(
			PlayerSide side,
			Minion targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1
		) throws HSException
	{
		return null;
	}
	
	public EnumSet<BattlecryTargetType> getBattlecryTargets() {
		return EnumSet.of(BattlecryTargetType.NO_BATTLECRY);
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
	public HearthTreeNode useUntargetableBattlecry(
			Minion minionPlacementTarget,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1,
			boolean singleRealizationOnly
		) throws HSException
	{
		HearthTreeNode toRet = this.useUntargetableBattlecry_core(minionPlacementTarget, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);
		if (toRet != null) {
			//Check for dead minions
			toRet = BoardStateFactoryBase.handleDeadMinions(toRet, deckPlayer0, deckPlayer1);
		}
		return toRet;
	}
	
	public HearthTreeNode useUntargetableBattlecry_core(
			Minion minionPlacementTarget,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1,
			boolean singleRealizationOnly
		) throws HSException
	{
		return null;
	}	
	/**
	 * 
	 * Places a minion on the board by using the card in hand
	 * 
	 *
     *
     * @param side
     * @param targetMinion The target minion (can be a Hero).  The new minion is always placed to the right of (higher index) the target minion.  If the target minion is a hero, then it is placed at the left-most position.
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     * @param deckPlayer0 The deck of player0
     * @return The boardState is manipulated and returned
	 * @throws HSException 
	 */
	@Override
	protected HearthTreeNode use_core(
			PlayerSide side,
			Minion targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1,
			boolean singleRealizationOnly)
		throws HSException
	{
		if (hasBeenUsed) {
			//Card is already used, nothing to do
			return null;
		}
		
		if (side == PlayerSide.WAITING_PLAYER)
			return null;
		
		HearthTreeNode toRet = this.summonMinion(side, targetMinion, boardState, deckPlayer0, deckPlayer1, false);
		if (toRet != null) { //summon succeeded, now let's use up our mana
			toRet.data_.getCurrentPlayer().subtractMana(this.mana_);
			toRet.data_.removeCard_hand(this);

			//Battlecry if available
			for (BattlecryTargetType btt : this.getBattlecryTargets()) {
				switch  (btt) {
				case NO_TARGET:
					toRet = this.useUntargetableBattlecry(targetMinion, toRet, deckPlayer0, deckPlayer1, singleRealizationOnly);
					break;
				case ENEMY_HERO:
					toRet = this.useTargetableBattlecry(PlayerSide.WAITING_PLAYER, PlayerSide.WAITING_PLAYER.getPlayer(toRet).getHero(), toRet, deckPlayer0, deckPlayer1);
					break;
				case FRIENDLY_HERO:
					toRet = this.useTargetableBattlecry(PlayerSide.CURRENT_PLAYER, PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getHero(), toRet, deckPlayer0, deckPlayer1);
					break;
				case ENEMY_MINIONS:
					for (Minion minion : PlayerSide.WAITING_PLAYER.getPlayer(toRet).getMinions()) {
						toRet = this.useTargetableBattlecry(PlayerSide.WAITING_PLAYER, minion, toRet, deckPlayer0, deckPlayer1);
					}
					break;
				case FRIENDLY_MINIONS:
					for (Minion minion : PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getMinions()) {
						if (minion != this)
							toRet = this.useTargetableBattlecry(PlayerSide.CURRENT_PLAYER, minion, toRet, deckPlayer0, deckPlayer1);
					}
					break;
				case ENEMY_BEASTS:
					for (Minion minion : PlayerSide.WAITING_PLAYER.getPlayer(toRet).getMinions()) {
						if (minion instanceof Beast)
							toRet = this.useTargetableBattlecry(PlayerSide.WAITING_PLAYER, minion, toRet, deckPlayer0, deckPlayer1);
					}
					break;
				case FRIENDLY_BEASTS:
					for (Minion minion : PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getMinions()) {
						if (minion != this && minion instanceof Beast)
							toRet = this.useTargetableBattlecry(PlayerSide.CURRENT_PLAYER, minion, toRet, deckPlayer0, deckPlayer1);
					}
					break;
				case ENEMY_MURLOCS:
					for (Minion minion : PlayerSide.WAITING_PLAYER.getPlayer(toRet).getMinions()) {
						if (minion instanceof Murloc)
							toRet = this.useTargetableBattlecry(PlayerSide.WAITING_PLAYER, minion, toRet, deckPlayer0, deckPlayer1);
					}
					break;
				case FRIENDLY_MURLOCS:
					for (Minion minion : PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getMinions()) {
						if (minion != this && minion instanceof Murloc)
							toRet = this.useTargetableBattlecry(PlayerSide.CURRENT_PLAYER, minion, toRet, deckPlayer0, deckPlayer1);
					}
					break;
				default:
					break;
				}
			}
			
			
			//Notify all that a minion is placed
			toRet = toRet.data_.getCurrentPlayerHero().minionPlacedEvent(toRet);
			for (Iterator<Minion> iter = toRet.data_.getCurrentPlayer().getMinions().iterator(); iter.hasNext();) {
				Minion minion = iter.next();
				if (!minion.silenced_)
					toRet = minion.minionPlacedEvent(toRet);
			}
			toRet = toRet.data_.getWaitingPlayerHero().minionPlacedEvent(toRet);
			for (Iterator<Minion> iter = toRet.data_.getWaitingPlayer().getMinions().iterator(); iter.hasNext();) {
				Minion minion = iter.next();
				if (!minion.silenced_)
					toRet = minion.minionPlacedEvent(toRet);
			}
		
		}
		
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
     * @param targetMinion The target minion (can be a Hero).  If it is a Hero, then the minion is placed on the last (right most) spot on the board.
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     * @param deckPlayer0 The deck of player0
     * @param deckPlayer1 The deck of player1
     * @param wasTransformed If the minion was 'summoned' as a result of a transform effect (e.g. Hex, Polymorph), set this to true.
     *
     * @return The boardState is manipulated and returned
	 */
	public HearthTreeNode summonMinion(
            PlayerSide targetSide,
            Minion targetMinion,
            HearthTreeNode boardState,
            Deck deckPlayer0,
            Deck deckPlayer1,
            boolean wasTransformed)
		throws HSException
	{
		HearthTreeNode toRet = this.summonMinion_core(targetSide, targetMinion, boardState, deckPlayer0, deckPlayer1);
		
		if (toRet != null) {
			toRet.data_.addSpellDamage(targetSide, this.spellDamage_);
			if (!wasTransformed) {
				//Notify all that a minion is summoned

				toRet = toRet.data_.getCurrentPlayerHero().minionSummonedEvent(PlayerSide.CURRENT_PLAYER, targetSide, this, toRet, deckPlayer0, deckPlayer1);
                for (Minion minion : PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getMinions()) {
                    if (!minion.silenced_)
                        toRet = minion.minionSummonedEvent(PlayerSide.CURRENT_PLAYER, targetSide, this, toRet, deckPlayer0, deckPlayer1);
                }
				toRet = toRet.data_.getWaitingPlayerHero().minionSummonedEvent(PlayerSide.WAITING_PLAYER, targetSide, this, toRet, deckPlayer0, deckPlayer1);
                for (Minion minion : PlayerSide.WAITING_PLAYER.getPlayer(toRet).getMinions()) {
                    if (!minion.silenced_)
                        toRet = minion.minionSummonedEvent(PlayerSide.WAITING_PLAYER, targetSide, this, toRet, deckPlayer0, deckPlayer1);
                }
			} else {
				//Notify all that a minion is transformed
				toRet = toRet.data_.getCurrentPlayerHero().minionTransformedEvent(PlayerSide.CURRENT_PLAYER, targetSide, this, toRet, deckPlayer0, deckPlayer1);
                for (Minion minion : PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getMinions()) {
                    if (!minion.silenced_)
                        toRet = minion.minionTransformedEvent(PlayerSide.CURRENT_PLAYER, targetSide, this, toRet, deckPlayer0, deckPlayer1);
                }
				toRet = toRet.data_.getWaitingPlayerHero().minionTransformedEvent(PlayerSide.WAITING_PLAYER, targetSide, this, toRet, deckPlayer0, deckPlayer1);
                for (Minion minion : PlayerSide.WAITING_PLAYER.getPlayer(toRet).getMinions()) {
                    if (!minion.silenced_)
                        toRet = minion.minionTransformedEvent(PlayerSide.WAITING_PLAYER, targetSide, this, toRet, deckPlayer0, deckPlayer1);
                }
			}
		}
		
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
     * @param targetMinion The target minion (can be a Hero).  The new minion is always placed to the right of (higher index) the target minion.  If the target minion is a hero, then it is placed at the left-most position.
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     * @return The boardState is manipulated and returned
     * @param deckPlayer0 The deck of player0
     * @param deckPlayer1 The deck of player1
	 * @throws HSException 
	 */
	protected HearthTreeNode summonMinion_core(
            PlayerSide targetSide,
			Minion targetMinion,
			HearthTreeNode boardState,
            Deck deckPlayer0,
            Deck deckPlayer1
			)
		throws HSException
	{		
		if (boardState.data_.modelForSide(targetSide).getNumMinions() < 7) {
			if (!charge_) {
				hasAttacked_ = true;
			}
			hasBeenUsed = true;
			if (isHero(targetMinion))
				boardState.data_.placeMinion(targetSide, this, 0);
			else
				boardState.data_.placeMinion(targetSide, this, targetSide.getPlayer(boardState).getMinions().indexOf(targetMinion) + 1);
			return boardState;
							
		} else {
			return null;
		}

	}
	/**
	 * 
	 * Attack with the minion
	 * 
	 *
     *
     * @param targetMinionPlayerSide
     * @param targetMinion The target minion
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     * @param deckPlayer0 The deck of player0
     * @return The boardState is manipulated and returned
	 */
	public HearthTreeNode attack(
			PlayerSide targetMinionPlayerSide,
			Minion targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1)
		throws HSException
	{
		
		//can't attack a stealthed target
		if (targetMinion.getState(mf.makeStealthed()) != null)
		//if (targetMinion.getStealthed())
			return null;
		
		MinionState frozenState = new FrozenState();
		//used to be frozen_
		if (getState(frozenState) != null) {
			getState(frozenState).handleStateEffect(this);
			this.removeState(frozenState);
			//this.frozen_ = false;
			return boardState;
		}
		
		//Notify all that an attack is beginning
		HearthTreeNode toRet = boardState;
		if (toRet != null) {
			//Notify all that a minion is created
			toRet = toRet.data_.getCurrentPlayerHero().minionAttackingEvent(toRet);
            for (Minion minion : PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getMinions()) {
                if (!minion.silenced_)
                    toRet = minion.minionAttackingEvent(toRet);
            }
			toRet = toRet.data_.getWaitingPlayerHero().minionAttackingEvent(toRet);
            for (Minion minion : PlayerSide.WAITING_PLAYER.getPlayer(toRet).getMinions()) {
                if (!minion.silenced_)
                    toRet = minion.minionAttackingEvent(toRet);
            }
		}
		
		//Do the actual attack
		toRet = this.attack_core(targetMinionPlayerSide, targetMinion, boardState, deckPlayer0, deckPlayer1);
		
		//check for and remove dead minions
		if (toRet != null) {
			toRet = BoardStateFactoryBase.handleDeadMinions(toRet, deckPlayer0, deckPlayer1);
		}
		
		//Attacking means you lose stealth
		if (toRet != null)
			//this.stealthed_ = false;
			this.removeState(mf.makeStealthed());
		
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
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     * @param deckPlayer0 The deck of player0
     * @return The boardState is manipulated and returned
	 */
	protected HearthTreeNode attack_core(
			PlayerSide targetMinionPlayerSide,
			Minion targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1)
		throws HSException
	{
		
		if (hasAttacked_) {
			//minion has already attacked
            log.debug("trying to attack when card has already attacked.");
			return null;
		}
		
		if (targetMinionPlayerSide == PlayerSide.CURRENT_PLAYER) {
            log.debug("trying to attack ourself, derp..");
			return null;
		}
		
		if (this.getTotalAttack() <= 0) {
            log.debug("unable to attack with zero attack damage.");
            return null;
        }

		
		HearthTreeNode toRet = boardState;
		byte origAttack = targetMinion.getTotalAttack();
		toRet = targetMinion.takeDamage(this.getTotalAttack(), PlayerSide.CURRENT_PLAYER, targetMinionPlayerSide, toRet, deckPlayer0, deckPlayer1, false, false);
		toRet = this.takeDamage(origAttack, targetMinionPlayerSide, PlayerSide.CURRENT_PLAYER, toRet, deckPlayer0, deckPlayer1, false, false);
		if (getState(mf.makeWindfury())!= null && !hasWindFuryAttacked_)
			mf.makeWindfury().handleStateEffect(this);
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
	 *  @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     *
     * */
	public HearthTreeNode minionPlacedEvent(
            HearthTreeNode boardState)
		throws HSInvalidPlayerIndexException
	{
		return boardState;
	}


	/**
	 * 
	 * Called whenever another minion is summoned using a spell
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
	 *  @param thisMinionPlayerSide The player index of this minion
	 * @param transformedMinionPlayerSide
     * @param transformedMinion The transformed minion (the minion that resulted from a transformation)
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     * @param deckPlayer0 The deck of player0    @return The boardState is manipulated and returned
     * */
	public HearthTreeNode minionTransformedEvent(
			PlayerSide thisMinionPlayerSide,
			PlayerSide transformedMinionPlayerSide,
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
	 * Called whenever another minion is attacking another character
	 * 
	 *  @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     *
     * */
	public HearthTreeNode minionAttackingEvent(
            HearthTreeNode boardState)
		throws HSInvalidPlayerIndexException
	{
		return boardState;
	}
	
	/**
	 * 
	 * Called whenever another minion is damaged
	 * 
	 *
     * @param thisMinionPlayerSide
     * @param damagedPlayerSide
     * @param damagedMinion The damaged minion
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     * @param deckPlayer0 The deck of player0    @return The boardState is manipulated and returned
     * */
	public HearthTreeNode minionDamagedEvent(
			PlayerSide thisMinionPlayerSide,
			PlayerSide damagedPlayerSide,
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
	 *
     * @param thisMinionPlayerSide
     * @param deadMinionPlayerSide
     * @param deadMinion The dead minion
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     * @param deckPlayer0 The deck of player0    @return The boardState is manipulated and returned
     * */
	public HearthTreeNode minionDeadEvent(
			PlayerSide thisMinionPlayerSide,
			PlayerSide deadMinionPlayerSide,
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
	 *
     * @param thisMinionPlayerSide
     * @param healedMinionPlayerSide
     * @param healedMinion The healed minion
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     * @param deckPlayer0 The deck of player0    @return The boardState is manipulated and returned
     * */
	public HearthTreeNode minionHealedEvent(
			PlayerSide thisMinionPlayerSide,
			PlayerSide healedMinionPlayerSide,
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

        Minion minion = null;
        try {
            minion = getClass().newInstance();
        } catch (InstantiationException e) {
            log.error("instantiation error", e);
        } catch (IllegalAccessException e) {
            log.error("illegal access error", e);
        }
        if (minion == null) {
            throw new RuntimeException("unable to instantiate minion.");
        }


        minion.name_ = name_;
        minion.mana_ = mana_;
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
        minion.summoned_ = summoned_;
        minion.transformed_ = transformed_;
        minion.destroyOnTurnStart_ = destroyOnTurnStart_;
        minion.destroyOnTurnEnd_ = destroyOnTurnEnd_;
        minion.deathrattleAction_ = deathrattleAction_;
        minion.attackAction_ = attackAction_;
        minion.isInHand_ = isInHand_;
        minion.hasBeenUsed = hasBeenUsed;
        //todo: continue here.


        return minion;
    }

    @Override
	public boolean equals(Object other) {
		if (!super.equals(other)) {
			return false;
		}
		
		Minion otherMinion = (Minion)other;
		if (health_ != otherMinion.health_) return false;
		if (maxHealth_ != otherMinion.maxHealth_) return false;
		if (baseHealth_ != otherMinion.baseHealth_) return false;
		if (auraHealth_ != otherMinion.auraHealth_) return false;

		if (attack_ != otherMinion.attack_) return false;
		if (baseAttack_ != otherMinion.baseAttack_) return false;
		if (extraAttackUntilTurnEnd_ != otherMinion.extraAttackUntilTurnEnd_) return false;
		if (auraAttack_ != otherMinion.auraAttack_) return false;

		if (taunt_ != otherMinion.taunt_) return false;
		if (divineShield_ != otherMinion.divineShield_) return false;
		if (windFury_ != otherMinion.windFury_) return false;
		if (charge_ != otherMinion.charge_) return false;
		if (stealthed_ != otherMinion.stealthed_) return false;
		if (hasAttacked_ != otherMinion.hasAttacked_) return false;
		if (heroTargetable_ != otherMinion.heroTargetable_) return false;
		if (hasWindFuryAttacked_ != otherMinion.hasWindFuryAttacked_) return false;
		if (frozen_ != otherMinion.frozen_) return false;
		if (silenced_ != otherMinion.silenced_) return false;
		if (summoned_ != otherMinion.summoned_) return false;
		if (transformed_ != otherMinion.transformed_) return false;
		if (destroyOnTurnStart_ != otherMinion.destroyOnTurnStart_) return false;
		if (destroyOnTurnEnd_ != otherMinion.destroyOnTurnEnd_) return false;
		
		if (spellDamage_ != otherMinion.spellDamage_) return false;

		//This is checked for reference equality
		if (deathrattleAction_ != ((Minion)other).deathrattleAction_)
			return false;
		
		//This is checked for reference equality
		if (attackAction_ != ((Minion)other).attackAction_)
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
        result = 31 * result + (int) health_;
        result = 31 * result + (int) maxHealth_;
        result = 31 * result + (int) baseHealth_;
        result = 31 * result + (int) auraHealth_;
        result = 31 * result + (int) attack_;
        result = 31 * result + (int) baseAttack_;
        result = 31 * result + (int) extraAttackUntilTurnEnd_;
        result = 31 * result + (int) auraAttack_;
        result = 31 * result + (summoned_ ? 1 : 0);
        result = 31 * result + (transformed_ ? 1 : 0);
        result = 31 * result + (destroyOnTurnStart_ ? 1 : 0);
        result = 31 * result + (destroyOnTurnEnd_ ? 1 : 0);
        result = 31 * result + (int) spellDamage_;
        result = 31 * result + (deathrattleAction_ != null ? deathrattleAction_.hashCode() : 0);
        result = 31 * result + (attackAction_ != null ? attackAction_.hashCode() : 0);
        result = 31 * result + (placementImportant_ ? 1 : 0);
        return result;
    }

    public boolean currentPlayerBoardFull(HearthTreeNode boardState) {
        return PlayerSide.CURRENT_PLAYER.getPlayer(boardState).getNumMinions() >= 7;
    }
    
}
