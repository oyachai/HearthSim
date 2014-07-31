package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.util.BoardState;
import com.hearthsim.util.tree.HearthTreeNode;
import com.hearthsim.event.attack.AttackAction;
import com.hearthsim.event.deathrattle.DeathrattleAction;


public class IronforgeRifleman extends Minion {
	
	private static final byte BATTLECRY_DAMAGE = 1;

	private static final String NAME = "Ironforge Rifleman";
	private static final byte MANA_COST = 3;
	private static final byte ATTACK = 2;
	private static final byte HEALTH = 2;
	
	private static final boolean TAUNT = false;
	private static final boolean DIVINE_SHIELD = false;
	private static final boolean WINDFURY = false;
	private static final boolean CHARGE = false;
	
	private static final boolean SUMMONED = false;
	private static final boolean TRANSFORMED = false;
	
	public IronforgeRifleman() {
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
				false,
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
	
	public IronforgeRifleman(	
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
			silenced,
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
		return new IronforgeRifleman(
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
	
	/**
	 * 
	 * Override for battlecry
	 * 
	 * Battlecry: Deal 1 damage to a chosen target
	 * 
	 * @param thisCardIndex The index (position) of the card in the hand
	 * @param playerIndex The index of the target player.  0 if targeting yourself or your own minions, 1 if targeting the enemy
	 * @param minionIndex The index of the target minion.
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * 
	 * @return The boardState is manipulated and returned
	 */
	@Override
	public HearthTreeNode use_core(
			int targetPlayerIndex,
			Minion targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1)
		throws HSException
	{
		HearthTreeNode toRet = super.use_core(targetPlayerIndex, targetMinion, boardState, deckPlayer0, deckPlayer1);
		if (toRet != null) {

			{
				HearthTreeNode newState = new HearthTreeNode((BoardState)boardState.data_.deepCopy());
				newState = newState.data_.getHero_p0().takeDamage(BATTLECRY_DAMAGE, 0, 0, newState, deckPlayer0, deckPlayer1);
				toRet.addChild(newState);
			}

			{
				for (int index = 0; index < boardState.data_.getNumMinions_p0(); ++index) {
					if (boardState.data_.getMinion_p0(index) == this)
						continue;
					HearthTreeNode newState = new HearthTreeNode((BoardState)boardState.data_.deepCopy());
					Minion minion = newState.data_.getMinion_p0(index);
					newState = minion.takeDamage(BATTLECRY_DAMAGE, 0, 0, newState, deckPlayer0, deckPlayer1);
					if (minion.getHealth() <= 0) {
						newState.data_.removeMinion_p0(minion);
					}
					toRet.addChild(newState);
				}
			}

			{
				HearthTreeNode newState = new HearthTreeNode((BoardState)boardState.data_.deepCopy());
				newState = newState.data_.getHero_p1().takeDamage(BATTLECRY_DAMAGE, 0, 1, newState, deckPlayer0, deckPlayer1);
				toRet.addChild(newState);
			}

			{
				for (int index = 0; index < boardState.data_.getNumMinions_p1(); ++index) {		
					HearthTreeNode newState = new HearthTreeNode((BoardState)boardState.data_.deepCopy());
					Minion minion = newState.data_.getMinion_p1(index);
					newState = minion.takeDamage(BATTLECRY_DAMAGE, 0, 1, newState, deckPlayer0, deckPlayer1);
					if (minion.getHealth() <= 0) {
						newState.data_.removeMinion_p1(minion);
					}
					toRet.addChild(newState);
				}
			}

			return boardState;
							
		} else {
			return null;				
		}

	}
}
