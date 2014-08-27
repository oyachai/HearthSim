package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Beast;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.attack.AttackAction;
import com.hearthsim.event.deathrattle.DeathrattleAction;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.tree.CardDrawNode;
import com.hearthsim.util.tree.HearthTreeNode;


public class StarvingBuzzard extends Beast {

	private static final String NAME = "Starving Buzzard";
	private static final byte MANA_COST = 2;
	private static final byte ATTACK = 2;
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
	
	public StarvingBuzzard() {
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
	
	public StarvingBuzzard(	
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
		return new StarvingBuzzard(
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
	 * Called whenever a minion is summoned on the board
	 * 
	 * The buzzard draws a card whenever a Beast is placed on the battlefield
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
		if (summonedMinionPlayerIndex == 1 || thisMinionPlayerIndex == 1)
			return boardState;
		
		HearthTreeNode toRet = super.minionSummonedEvent(thisMinionPlayerIndex, summonedMinionPlayerIndex, summonedMinion, boardState, deckPlayer0, deckPlayer1);
		if (toRet.data_.getMinion_p0(summonedMinionPlayerIndex - 1) instanceof Beast) {
			if (toRet instanceof CardDrawNode) {
				((CardDrawNode) toRet).addNumCardsToDraw(1);
			} else {
				toRet = new CardDrawNode(toRet, 1); //draw one card
			}
		}

		return toRet;
	}
}
