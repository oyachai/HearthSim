package com.hearthsim.card.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.event.attack.AttackAction;
import com.hearthsim.event.deathrattle.DeathrattleAction;
import com.hearthsim.exception.HSException;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.util.factory.BoardStateFactoryBase;
import com.hearthsim.util.tree.HearthTreeNode;
import org.json.JSONObject;

import java.util.Iterator;

public class Minion extends Card {
	
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
				false,
				false,
				false,
				false,
				false,
				null,
				null,
				true,
				false);
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
	
	/**
	 * Called at the start of the turn
	 * 
	 * This function is called at the start of the turn.  Any derived class must override it to implement whatever
	 * "start of the turn" effect the card has.
	 */
	@Override
	public BoardModel startTurn(PlayerModel thisMinionPlayerIndex, BoardModel boardModel, Deck deckPlayer0, Deck deckPlayer1) throws HSException {
		if (destroyOnTurnStart_) {
			this.destroyed(thisMinionPlayerIndex, new HearthTreeNode(boardModel), deckPlayer0, deckPlayer1);
		}
		return boardModel;
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
	public BoardModel endTurn(PlayerModel thisMinionPlayerIndex, BoardModel boardModel, Deck deckPlayer0, Deck deckPlayer1) throws HSException {
		extraAttackUntilTurnEnd_ = 0;
		if (destroyOnTurnEnd_) {
			this.destroyed(thisMinionPlayerIndex, new HearthTreeNode(boardModel), deckPlayer0, deckPlayer1);
		}
		return boardModel;
	}
	
	/**
	 * Called when this minion takes damage
	 * 
	 * Always use this function to take damage... it properly notifies all others of its damage and possibly of its death
	 * 
	 * @param damage The amount of damage to take
	 * @param attackPlayerModel The player index of the attacker.  This is needed to do things like +spell damage.
	 * @param thisPlayerModel
     *@param boardState
     * @param deckPlayer0 The deck of player0
     * @param isSpellDamage True if this is a spell damage
     * @param handleMinionDeath Set this to True if you want the death event to trigger when (if) the minion dies from this damage.  Setting this flag to True will also trigger deathrattle immediately.
*     @throws HSInvalidPlayerIndexException
	 */
	public HearthTreeNode takeDamage(
			byte damage,
			PlayerModel attackPlayerModel,
			PlayerModel thisPlayerModel,
			HearthTreeNode boardState,
			Deck deckPlayer0, 
			Deck deckPlayer1,
			boolean isSpellDamage,
			boolean handleMinionDeath)
		throws HSException
	{
		if (!divineShield_) {
			byte totalDamage = isSpellDamage ? (byte)(damage + boardState.data_.getSpellDamage(attackPlayerModel)) : damage;
			health_ = (byte)(health_ - totalDamage);
			
			//Notify all that the minion is damaged
			HearthTreeNode toRet = boardState;
			toRet = toRet.data_.getCurrentPlayerHero().minionDamagedEvent(toRet.data_.getCurrentPlayer(), thisPlayerModel, this, toRet, deckPlayer0, deckPlayer1);
			for (int j = 0; j < toRet.data_.getCurrentPlayer().getNumMinions(); ++j) {
				if (!toRet.data_.getCurrentPlayer().getMinions().get(j).silenced_)
					toRet = toRet.data_.getCurrentPlayer().getMinions().get(j).minionDamagedEvent(toRet.data_.getCurrentPlayer(), thisPlayerModel, this, toRet, deckPlayer0, deckPlayer1);
			}
			toRet = toRet.data_.getWaitingPlayerHero().minionDamagedEvent(toRet.data_.getWaitingPlayer(), thisPlayerModel, this, toRet, deckPlayer0, deckPlayer1);
			for (int j = 0; j < toRet.data_.getWaitingPlayer().getNumMinions(); ++j) {
				if (!toRet.data_.getWaitingPlayer().getMinions().get(j).silenced_)
					toRet = toRet.data_.getWaitingPlayer().getMinions().get(j).minionDamagedEvent(toRet.data_.getWaitingPlayer(), thisPlayerModel, this, toRet, deckPlayer0, deckPlayer1);
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
     * @param thisPlayerModel
     * @param boardState
     * @param deckPlayer0
     * @param deckPlayer1
     *
     * @throws HSInvalidPlayerIndexException
	 */
	public HearthTreeNode destroyed(PlayerModel thisPlayerModel, HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1) throws HSException {

        health_ = 0;
        HearthTreeNode toRet = boardState;

        if (!silenced_) {
            BoardModel data_ = boardState.data_;
            PlayerModel currentPlayer = data_.getCurrentPlayer();
            toRet.data_.setSpellDamage(currentPlayer, (byte)(data_.getSpellDamage(currentPlayer) - spellDamage_));
        }

        //perform the deathrattle action if there is one
        if (deathrattleAction_ != null) {
            toRet =  deathrattleAction_.performAction(this, thisPlayerModel, toRet, deckPlayer0, deckPlayer1);
        }

        //Notify all that it is dead
        toRet = toRet.data_.getCurrentPlayerHero().minionDeadEvent(toRet.data_.getCurrentPlayer(), thisPlayerModel, this, toRet, deckPlayer0, deckPlayer1);
        for (int j = 0; j < toRet.data_.getCurrentPlayer().getNumMinions(); ++j) {
            if (!toRet.data_.getCurrentPlayer().getMinions().get(j).silenced_)
                toRet = toRet.data_.getCurrentPlayer().getMinions().get(j).minionDeadEvent(toRet.data_.getCurrentPlayer(), thisPlayerModel, this, toRet, deckPlayer0, deckPlayer1);
        }
        toRet = toRet.data_.getWaitingPlayerHero().minionDeadEvent(toRet.data_.getWaitingPlayer(), thisPlayerModel, this, toRet, deckPlayer0, deckPlayer1);
        for (int j = 0; j < toRet.data_.getWaitingPlayer().getNumMinions(); ++j) {
            if (!toRet.data_.getWaitingPlayer().getMinions().get(j).silenced_)
                toRet = toRet.data_.getWaitingPlayer().getMinions().get(j).minionDeadEvent(toRet.data_.getWaitingPlayer(), thisPlayerModel, this, toRet, deckPlayer0, deckPlayer1);
        }

        return toRet;

	}
	
	/**
	 * Called when this minion is silenced
	 * 
	 * Always use this function to "silence" minions
	 * 
	 *
     * @param thisPlayerModel
     * @param boardState
     * @param deckPlayer0 The deck of player0
     * @throws HSInvalidPlayerIndexException
	 */
	public HearthTreeNode silenced(PlayerModel thisPlayerModel, HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1) throws HSInvalidPlayerIndexException {
		HearthTreeNode toRet = boardState;
		if (!silenced_) {
            BoardModel data_ = boardState.data_;
            toRet.data_.setSpellDamage(data_.getCurrentPlayer(), (byte)(data_.getSpellDamage(data_.getCurrentPlayer()) - spellDamage_));
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

		return toRet;
	}
	
	/**
	 * Called when this minion is healed
	 * 
	 * Always use this function to heal minions
	 * 
	 * @param healAmount The amount of healing to take
	 * @param thisPlayerModel
     *@param boardState
     * @param deckPlayer0 The deck of player0   @throws HSInvalidPlayerIndexException
	 */
	public HearthTreeNode takeHeal(byte healAmount, PlayerModel thisPlayerModel, HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1) throws HSInvalidPlayerIndexException {
		
		if (health_ < maxHealth_) {
			if (health_ + healAmount > maxHealth_)
				health_ = maxHealth_;
			else
				health_ = (byte)(health_ + healAmount);
			
			//Notify all that it the minion is healed
			HearthTreeNode toRet = boardState;
			toRet = toRet.data_.getCurrentPlayerHero().minionHealedEvent(boardState.data_.getCurrentPlayer(), thisPlayerModel, this, toRet, deckPlayer0, deckPlayer1);
			for (Iterator<Minion> iter = toRet.data_.getCurrentPlayer().getMinions().iterator(); iter.hasNext();) {
				Minion minion = iter.next();
				if (!minion.silenced_)
					toRet = minion.minionHealedEvent(boardState.data_.getCurrentPlayer(), thisPlayerModel, this, toRet, deckPlayer0, deckPlayer1);
			}
			toRet = toRet.data_.getWaitingPlayerHero().minionHealedEvent(boardState.data_.getWaitingPlayer(), thisPlayerModel, this, toRet, deckPlayer0, deckPlayer1);
			for (Iterator<Minion> iter = toRet.data_.getWaitingPlayer().getMinions().iterator(); iter.hasNext();) {
				Minion minion = iter.next();
				if (!minion.silenced_)
					toRet = minion.minionHealedEvent(boardState.data_.getWaitingPlayer(), thisPlayerModel, this, toRet, deckPlayer0, deckPlayer1);
			}
			return toRet;
		}
		return boardState;
	}
	
	/**
	 * 
	 * Places the minion on the board by using the card in hand
	 * 
	 *
     *
     * @param targetPlayer
     * @param targetMinion The target minion (can be a Hero).  If it is a Hero, then the minion is placed on the last (right most) spot on the board.
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     * @param deckPlayer0 The deck of player0
     * @return The boardState is manipulated and returned
	 */
	@Override
	public HearthTreeNode useOn(
			PlayerModel targetPlayer,
			Minion targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1,
			boolean singleRealizationOnly)
		throws HSException
	{
		HearthTreeNode toRet = this.use_core(targetPlayer, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);
		
		if (toRet != null) {

            BoardModel data_ = toRet.data_;
            data_.setSpellDamage(data_.getCurrentPlayer(), (byte) (data_.getSpellDamage(data_.getCurrentPlayer()) + spellDamage_));
			
			isInHand_ = false;
			
			//Notify all other cards/characters of the card's use
            for (Card card : data_.getCurrentPlayerHand()) {
                toRet = card.otherCardUsedEvent(toRet.data_.getCurrentPlayer(), toRet.data_.getCurrentPlayer(), this, toRet, deckPlayer0, deckPlayer1);
            }
			toRet = data_.getCurrentPlayerHero().otherCardUsedEvent(toRet.data_.getCurrentPlayer(), toRet.data_.getCurrentPlayer(), this, toRet, deckPlayer0, deckPlayer1);
            for (Minion minion : data_.getCurrentPlayer().getMinions()) {
                if (!minion.silenced_)
                    toRet = minion.otherCardUsedEvent(toRet.data_.getCurrentPlayer(), toRet.data_.getCurrentPlayer(), this, toRet, deckPlayer0, deckPlayer1);
            }

            for (Card card : data_.getWaitingPlayerHand()) {
                toRet = card.otherCardUsedEvent(toRet.data_.getWaitingPlayer(), toRet.data_.getCurrentPlayer(), this, toRet, deckPlayer0, deckPlayer1);
            }
			toRet = data_.getWaitingPlayerHero().otherCardUsedEvent(toRet.data_.getWaitingPlayer(), toRet.data_.getCurrentPlayer(), this, toRet, deckPlayer0, deckPlayer1);
            for (Minion minion : data_.getWaitingPlayer().getMinions()) {
                if (!minion.silenced_)
                    toRet = minion.otherCardUsedEvent(toRet.data_.getWaitingPlayer(), toRet.data_.getCurrentPlayer(), this, toRet, deckPlayer0, deckPlayer1);
            }
			
			//Notify all that a minion is placed
			toRet = data_.getCurrentPlayerHero().minionPlacedEvent(0, targetPlayer, this, toRet, deckPlayer0, deckPlayer1);
			for (Iterator<Minion> iter = data_.getCurrentPlayer().getMinions().iterator(); iter.hasNext();) {
				Minion minion = iter.next();
				if (!minion.silenced_)
					toRet = minion.minionPlacedEvent(0, targetPlayer, this, toRet, deckPlayer0, deckPlayer1);
			}
			toRet = data_.getWaitingPlayerHero().minionPlacedEvent(1, targetPlayer, this, toRet, deckPlayer0, deckPlayer1);
			for (Iterator<Minion> iter = data_.getWaitingPlayer().getMinions().iterator(); iter.hasNext();) {
				Minion minion = iter.next();
				if (!minion.silenced_)
					toRet = minion.minionPlacedEvent(1, targetPlayer, this, toRet, deckPlayer0, deckPlayer1);
			}
		}
		
		return toRet;
	}
	
	@Override
    public boolean canBeUsedOn(PlayerModel playerModel, Minion minion, BoardModel boardModel) {
		if (playerModel == boardModel.getWaitingPlayer())
			return false;
		if (hasBeenUsed_) 
			return false;
		return true;
    }
    
	/**
	 * 
	 * Places a minion on the board by using the card in hand
	 * 
	 *
     * @param playerModel
     * @param targetMinion The target minion (can be a Hero).  The new minion is always placed to the right of (higher index) the target minion.  If the target minion is a hero, then it is placed at the left-most position.
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     * @param deckPlayer0 The deck of player0
     * @return The boardState is manipulated and returned
	 * @throws HSException 
	 */
	@Override
	protected HearthTreeNode use_core(
			PlayerModel playerModel,
			Minion targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1,
			boolean singleRealizationOnly)
		throws HSException
	{
		if (hasBeenUsed_) {
			//Card is already used, nothing to do
			return null;
		}
		
		if (boardState.data_.getWaitingPlayer() == playerModel)
			return null;
		
		HearthTreeNode toRet = this.summonMinion(playerModel, targetMinion, boardState, deckPlayer0, deckPlayer1, false);
		if (toRet != null) { //summon succeeded, now let's use up our mana
			toRet.data_.setMana_p0(toRet.data_.getMana_p0() - this.mana_);
			toRet.data_.removeCard_hand(this);
		}
		return toRet;
	}
	
	
	
	/**
	 * 
	 * Places a minion on the board via a summon effect
	 * 
	 * This function is meant to be used when summoning minions through means other than a direct card usage.
	 * 
	 * @param targetMinion The target minion (can be a Hero).  If it is a Hero, then the minion is placed on the last (right most) spot on the board.
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * @param deckPlayer0 The deck of player0
	 * @param wasTransformed If the minion was 'summoned' as a result of a transform effect (e.g. Hex, Polymorph), set this to true.
	 *
	 * @return The boardState is manipulated and returned
	 */
	public HearthTreeNode summonMinion(
            PlayerModel targetPlayer,
            Minion targetMinion,
            HearthTreeNode boardState,
            Deck deckPlayer0,
            Deck deckPlayer1,
            boolean wasTransformed)
		throws HSException
	{
		HearthTreeNode toRet = this.summonMinion_core(targetPlayer, targetMinion, boardState);
		
		if (toRet != null) {
			if (!wasTransformed) {
				//Notify all that a minion is summoned
				toRet = toRet.data_.getCurrentPlayerHero().minionSummonedEvent(boardState.data_.getCurrentPlayer(), targetPlayer, this, toRet, deckPlayer0, deckPlayer1);
                for (Minion minion : toRet.data_.getCurrentPlayer().getMinions()) {
                    if (!minion.silenced_)
                        toRet = minion.minionSummonedEvent(boardState.data_.getCurrentPlayer(), targetPlayer, this, toRet, deckPlayer0, deckPlayer1);
                }
				toRet = toRet.data_.getWaitingPlayerHero().minionSummonedEvent(boardState.data_.getWaitingPlayer(), targetPlayer, this, toRet, deckPlayer0, deckPlayer1);
                for (Minion minion : toRet.data_.getWaitingPlayer().getMinions()) {
                    if (!minion.silenced_)
                        toRet = minion.minionSummonedEvent(boardState.data_.getWaitingPlayer(), targetPlayer, this, toRet, deckPlayer0, deckPlayer1);
                }
			} else {
				//Notify all that a minion is transformed
				toRet = toRet.data_.getCurrentPlayerHero().minionTransformedEvent(boardState.data_.getCurrentPlayer(), targetPlayer, this, toRet, deckPlayer0, deckPlayer1);
                for (Minion minion : toRet.data_.getCurrentPlayer().getMinions()) {
                    if (!minion.silenced_)
                        toRet = minion.minionTransformedEvent(boardState.data_.getCurrentPlayer(), targetPlayer, this, toRet, deckPlayer0, deckPlayer1);
                }
				toRet = toRet.data_.getWaitingPlayerHero().minionTransformedEvent(boardState.data_.getWaitingPlayer(), targetPlayer, this, toRet, deckPlayer0, deckPlayer1);
                for (Minion minion : toRet.data_.getWaitingPlayer().getMinions()) {
                    if (!minion.silenced_)
                        toRet = minion.minionTransformedEvent(boardState.data_.getWaitingPlayer(), targetPlayer, this, toRet, deckPlayer0, deckPlayer1);
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
	 * @param targetPlayerIndex The index of the target player.  0 if targeting yourself or your own minions, 1 if targeting the enemy
	 * @param targetMinion The target minion (can be a Hero).  The new minion is always placed to the right of (higher index) the target minion.  If the target minion is a hero, then it is placed at the left-most position.
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * @param deckPlayer0 The deck of player0
	 * @param deckPlayer0 The deck of player1
	 * 
	 * @return The boardState is manipulated and returned
	 * @throws HSException 
	 */
	protected HearthTreeNode summonMinion_core(
            PlayerModel targetPlayer,
			Minion targetMinion,
			HearthTreeNode boardState)
		throws HSException
	{		
		if (targetPlayer.getNumMinions() < 7) {

			if (!charge_) {
				hasAttacked_ = true;
			}
			hasBeenUsed_ = true;
			if (targetMinion instanceof Hero)
				boardState.data_.placeMinion(targetPlayer, this, 0);
			else
				boardState.data_.placeMinion(targetPlayer, this, targetPlayer.getMinions().indexOf(targetMinion) + 1);
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
     * @param targetMinionPlayerModel
     * @param targetMinion The target minion
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     * @param deckPlayer0 The deck of player0
     * @return The boardState is manipulated and returned
	 */
	public HearthTreeNode attack(
			PlayerModel targetMinionPlayerModel,
			Minion targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1)
		throws HSException
	{
		
		//can't attack a stealthed target
		if (targetMinion.getStealthed())
			return null;
		
		if (frozen_) {
			this.hasAttacked_ = true;
			this.frozen_ = false;
			return boardState;
		}
		
		//Notify all that an attack is beginning
		HearthTreeNode toRet = boardState;
		if (toRet != null) {
			//Notify all that a minion is created
			toRet = toRet.data_.getCurrentPlayerHero().minionAttackingEvent(toRet.data_.getCurrentPlayer(), this, targetMinionPlayerModel, targetMinion, toRet, deckPlayer0, deckPlayer1);
            for (Minion minion : toRet.data_.getCurrentPlayer().getMinions()) {
                if (!minion.silenced_)
                    toRet = minion.minionAttackingEvent(toRet.data_.getCurrentPlayer(), this, targetMinionPlayerModel, targetMinion, toRet, deckPlayer0, deckPlayer1);
            }
			toRet = toRet.data_.getWaitingPlayerHero().minionAttackingEvent(toRet.data_.getCurrentPlayer(), this, targetMinionPlayerModel, targetMinion, toRet, deckPlayer0, deckPlayer1);
            for (Minion minion : toRet.data_.getWaitingPlayer().getMinions()) {
                if (!minion.silenced_)
                    toRet = minion.minionAttackingEvent(toRet.data_.getCurrentPlayer(), this, targetMinionPlayerModel, targetMinion, toRet, deckPlayer0, deckPlayer1);
            }
		}
		
		//Do the actual attack
		toRet = this.attack_core(targetMinionPlayerModel, targetMinion, boardState, deckPlayer0, deckPlayer1);
		
		//check for and remove dead minions
		if (toRet != null) {
			toRet = BoardStateFactoryBase.handleDeadMinions(toRet, deckPlayer0, deckPlayer1);
		}
		
		//Attacking means you lose stealth
		if (toRet != null)
			this.stealthed_ = false;
		
		return toRet;
	}

	/**
	 * 
	 * Attack with the minion
	 * 
	 *
     * @param targetMinionPlayerModel
     * @param targetMinion The target minion
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     * @param deckPlayer0 The deck of player0
     * @return The boardState is manipulated and returned
	 */
	protected HearthTreeNode attack_core(
			PlayerModel targetMinionPlayerModel,
			Minion targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1)
		throws HSException
	{
		
		if (hasAttacked_) {
			//minion has already attacked
			return null;
		}
		
		if (targetMinionPlayerModel == boardState.data_.getCurrentPlayer()) {
			return null;
		}
		
		if (this.getTotalAttack() <= 0)
			return null;

		
		HearthTreeNode toRet = boardState;
		byte origAttack = targetMinion.getTotalAttack();
		toRet = targetMinion.takeDamage(this.getTotalAttack(), toRet.data_.getCurrentPlayer(), targetMinionPlayerModel, toRet, deckPlayer0, deckPlayer1, false, false);
		toRet = this.takeDamage(origAttack, targetMinionPlayerModel, toRet.data_.getCurrentPlayer(), toRet, deckPlayer0, deckPlayer1, false, false);
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
	 * @param playerModel
     *@param placedMinion The placed minion
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     * @param deckPlayer0 The deck of player0    @return The boardState is manipulated and returned
	 */
	public HearthTreeNode minionPlacedEvent(
			int thisMinionPlayerIndex,
			PlayerModel playerModel,
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
	 *
     * @param thisMinionPlayerModel
     * @param summonedMinionPlayerModel
     *@param summonedMinion The summoned minion
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     * @param deckPlayer0 The deck of player0    @return The boardState is manipulated and returned
	 */
	public HearthTreeNode minionSummonedEvent(
			PlayerModel thisMinionPlayerModel,
			PlayerModel summonedMinionPlayerModel,
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
	 * @param thisMinionPlayerModel The player index of this minion
	 * @param transformedMinionPlayerModel
     *@param transformedMinion The transformed minion (the minion that resulted from a transformation)
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     * @param deckPlayer0 The deck of player0    @return The boardState is manipulated and returned
	 */
	public HearthTreeNode minionTransformedEvent(
			PlayerModel thisMinionPlayerModel,
			PlayerModel transformedMinionPlayerModel,
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
	 *
     * @param attackingPlayerModel
     * @param attackingMinion The attacking minion
     * @param attackedPlayerModel
     *@param attackedMinion The target (attacked) minion
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     * @param deckPlayer0 The deck of player0    @return The boardState is manipulated and returned
	 */
	public HearthTreeNode minionAttackingEvent(
			PlayerModel attackingPlayerModel,
			Minion attackingMinion,
			PlayerModel attackedPlayerModel,
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
	 *
     * @param thisMinionPlayerModel
     * @param damagedPlayerModel
     *@param damagedMinion The damaged minion
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     * @param deckPlayer0 The deck of player0    @return The boardState is manipulated and returned
	 */
	public HearthTreeNode minionDamagedEvent(
			PlayerModel thisMinionPlayerModel,
			PlayerModel damagedPlayerModel,
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
     * @param thisMinionPlayerModel
     * @param deadMinionPlayerModel
     *@param deadMinion The dead minion
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     * @param deckPlayer0 The deck of player0    @return The boardState is manipulated and returned
	 */
	public HearthTreeNode minionDeadEvent(
			PlayerModel thisMinionPlayerModel,
			PlayerModel deadMinionPlayerModel,
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
     * @param thisMinionPlayerModel
     * @param healedMinionPlayerModel
     *@param healedMinion The healed minion
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     * @param deckPlayer0 The deck of player0    @return The boardState is manipulated and returned
	 */
	public HearthTreeNode minionHealedEvent(
			PlayerModel thisMinionPlayerModel,
			PlayerModel healedMinionPlayerModel,
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
				this.auraAttack_,
				this.baseHealth_,
				this.maxHealth_,
				this.auraHealth_,
				this.spellDamage_,
				this.taunt_,
				this.divineShield_,
				this.windFury_,
				this.charge_,
				this.hasAttacked_,
				this.hasWindFuryAttacked_,
				this.frozen_,
				this.silenced_,
				this.stealthed_,
				this.heroTargetable_,
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
}
