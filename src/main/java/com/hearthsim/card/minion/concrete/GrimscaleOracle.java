package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.Murloc;
import com.hearthsim.event.attack.AttackAction;
import com.hearthsim.event.deathrattle.DeathrattleAction;
import com.hearthsim.exception.HSException;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;


public class GrimscaleOracle extends Murloc {

	private static final String NAME = "Grimscale Oracle";
	private static final byte MANA_COST = 1;
	private static final byte ATTACK = 1;
	private static final byte HEALTH = 1;
	
	private static final boolean TAUNT = false;
	private static final boolean DIVINE_SHIELD = false;
	private static final boolean WINDFURY = false;
	private static final boolean CHARGE = false;
	
	private static final boolean STEALTHED = false;
	private static final boolean HERO_TARGETABLE = true;
	private static final boolean SUMMONED = false;
	private static final boolean TRANSFORMED = false;
	private static final byte SPELL_DAMAGE = 0;
	
	public GrimscaleOracle() {
		this(
				MANA_COST,
				ATTACK,
				HEALTH,
				ATTACK,
				(byte)0,
				(byte)0,
				HEALTH,
				HEALTH,
				(byte)0,
				SPELL_DAMAGE,
				TAUNT,
				DIVINE_SHIELD,
				WINDFURY,
				CHARGE,
				false,
				false,
				false,
				false,
				STEALTHED,
				HERO_TARGETABLE,
				SUMMONED,
				TRANSFORMED,
				false,
				false,
				null,
				null,
				true,
				false
			);
	}
	
	public GrimscaleOracle(	
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
			boolean hero_targetable,
			boolean summoned,
			boolean transformed,
			boolean destroyOnTurnStart,
			boolean destroyOnTurnEnd,
			DeathrattleAction deathrattleAction,
			AttackAction attackAction,
			boolean isInHand,
			boolean hasBeenUsed) {
		
		super(
			NAME,
			mana,
			attack,
			health,
			baseAttack,
			extraAttackUntilTurnEnd,
			auraAttack,
			baseHealth,
			maxHealth,
			auraHealth,
			spellDamage,
			taunt,
			divineShield,
			windFury,
			charge,
			hasAttacked,
			hasWindFuryAttacked,
			frozen,
			silenced,
			stealthed,
			hero_targetable,
			summoned,
			transformed,
			destroyOnTurnStart,
			destroyOnTurnEnd,
			deathrattleAction,
			attackAction,
			isInHand,
			hasBeenUsed);
	}
	
	@Override
	public Object deepCopy() {
		return new GrimscaleOracle(
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
	
	
	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Override for the temporary buff to attack
	 * 
	 *
     *
     * @param side
     * @param targetMinion The index of the target minion.
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     * @param deckPlayer0
     * @param deckPlayer1
     *
     * @return The boardState is manipulated and returned
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
		HearthTreeNode toRet = super.use_core(side, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);
		if (toRet != null) {
			
			for (Minion minion : PlayerSide.CURRENT_PLAYER.getMinions()) {
				if (minion instanceof Murloc && minion != this) {
					minion.setAuraAttack((byte)(minion.getAuraAttack() + 1));
				}
			}
			
			for (Minion minion : PlayerSide.WAITING_PLAYER.getMinions()) {
				if (minion instanceof Murloc && minion != this) {
					minion.setAuraAttack((byte)(minion.getAuraAttack() + 1));
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
	 *
     *
     * @param thisPlayerSide
     * @param boardState
     * @throws HSInvalidPlayerIndexException
	 */
	@Override
	public HearthTreeNode silenced(PlayerSide thisPlayerSide, HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1) throws HSInvalidPlayerIndexException {
		HearthTreeNode toRet = boardState;
		if (!silenced_) {
			for (Minion minion : toRet.data_.getCurrentPlayer().getMinions()) {
				if (minion instanceof Murloc && minion != this) {
					minion.setAuraAttack((byte)(minion.getAuraAttack() - 1));
				}
			}
			for (Minion minion : toRet.data_.getWaitingPlayer().getMinions()) {
				if (minion instanceof Murloc && minion != this) {
					minion.setAuraAttack((byte)(minion.getAuraAttack() - 1));
				}
			}
		}
		toRet = this.silenced(thisPlayerSide, toRet, deckPlayer0, deckPlayer1);
		return toRet;
	}
	
	/**
	 * Called when this minion dies (destroyed)
	 * 
	 * Override for the aura effect
	 * 
	 *
     * @param thisPlayerModel
     * @param boardState
     * @throws HSInvalidPlayerIndexException
	 */
	@Override
	public HearthTreeNode destroyed(PlayerModel thisPlayerModel, HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1) throws HSException {
		
		HearthTreeNode toRet = boardState;
		if (!silenced_) {
			for (Minion minion : toRet.data_.getCurrentPlayer().getMinions()) {
				if (minion instanceof Murloc && minion != this) {
					minion.setAuraAttack((byte)(minion.getAuraAttack() - 1));
				}
			}
			for (Minion minion : toRet.data_.getWaitingPlayer().getMinions()) {
				if (minion instanceof Murloc && minion != this) {
					minion.setAuraAttack((byte)(minion.getAuraAttack() - 1));
				}
			}
		}
		toRet = super.destroyed(thisPlayerModel, toRet, deckPlayer0, deckPlayer1);
		return toRet;
	}
	
	private HearthTreeNode doBuffs(
            Minion targetMinion,
            HearthTreeNode boardState)
		throws HSInvalidPlayerIndexException
	{
        if (!silenced_ && targetMinion instanceof Murloc && targetMinion != this) {
            targetMinion.setAuraAttack((byte) (targetMinion.getAuraAttack() + 1));
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
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     * @return The boardState is manipulated and returned
	 */
	@Override
	public HearthTreeNode minionSummonedEvent(
			PlayerSide thisMinionPlayerSide,
			PlayerSide summonedMinionPlayerSide,
			Minion summonedMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1)
		throws HSInvalidPlayerIndexException
	{
		return this.doBuffs(summonedMinion, boardState);
	}
	
	/**
	 * 
	 * Called whenever another minion is summoned using a spell
	 * 
	 *  @param thisMinionPlayerSide
     * @param transformedMinionPlayerSide
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     * @return The boardState is manipulated and returned
	 */
	@Override
	public HearthTreeNode minionTransformedEvent(
			PlayerSide thisMinionPlayerSide,
			PlayerSide transformedMinionPlayerSide,
			Minion transformedMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1)
		throws HSInvalidPlayerIndexException
	{
		return this.doBuffs(transformedMinion, boardState);
	}
}
