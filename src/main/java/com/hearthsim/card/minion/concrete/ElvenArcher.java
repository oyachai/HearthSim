package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.util.boardstate.BoardState;
import com.hearthsim.util.boardstate.BoardStateFactoryBase;
import com.hearthsim.util.tree.HearthTreeNode;
import com.hearthsim.event.attack.AttackAction;
import com.hearthsim.event.deathrattle.DeathrattleAction;


public class ElvenArcher extends Minion {


	private static final String NAME = "Elven Archer";
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
	
	public ElvenArcher() {
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
	
	public ElvenArcher(	
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
		return new ElvenArcher(
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
			Deck deckPlayer1,
			boolean singleRealizationOnly)
		throws HSException
	{	
		HearthTreeNode toRet = super.use_core(targetPlayerIndex, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);
		if (toRet != null) {

			{
				HearthTreeNode newState = new HearthTreeNode((BoardState)boardState.data_.deepCopy());
				newState = newState.data_.getHero_p0().takeDamage((byte)1, 0, 0, newState, deckPlayer0, deckPlayer1, false, false);
				toRet.addChild(newState);
			}

			{
				for (int index = 0; index < boardState.data_.getNumMinions_p0(); ++index) {
					if (boardState.data_.getMinion_p0(index) == this)
						continue;
					HearthTreeNode newState = new HearthTreeNode((BoardState)boardState.data_.deepCopy());
					Minion minion = newState.data_.getMinion_p0(index);
					newState = minion.takeDamage((byte)1, 0, 0, newState, deckPlayer0, deckPlayer1, false, true);
					newState = BoardStateFactoryBase.handleDeadMinions(newState, deckPlayer0, deckPlayer1);
					toRet.addChild(newState);
				}
			}

			{
				HearthTreeNode newState = new HearthTreeNode((BoardState)boardState.data_.deepCopy());
				newState = newState.data_.getHero_p1().takeDamage((byte)1, 0, 1, newState, deckPlayer0, deckPlayer1, false, false);
				toRet.addChild(newState);
			}

			{
				for (int index = 0; index < boardState.data_.getNumMinions_p1(); ++index) {		
					HearthTreeNode newState = new HearthTreeNode((BoardState)boardState.data_.deepCopy());
					Minion minion = newState.data_.getMinion_p1(index);
					newState = minion.takeDamage((byte)1, 0, 1, newState, deckPlayer0, deckPlayer1, false, true);
					newState = BoardStateFactoryBase.handleDeadMinions(newState, deckPlayer0, deckPlayer1);
					toRet.addChild(newState);
				}
			}

			return boardState;
							
		} else {
			return null;				
		}

	}
}
