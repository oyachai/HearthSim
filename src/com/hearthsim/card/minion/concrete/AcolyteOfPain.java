package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.tree.CardDrawNode;
import com.hearthsim.util.tree.HearthTreeNode;

public class AcolyteOfPain extends Minion {

	private static final String NAME = "Acolyte of Pain";
	private static final byte MANA_COST = 3;
	private static final byte ATTACK = 1;
	private static final byte HEALTH = 3;
	
	private static final boolean TAUNT = false;
	private static final boolean DIVINE_SHIELD = false;
	private static final boolean WINDFURY = false;
	private static final boolean CHARGE = false;
	
	private static final boolean SUMMONED = false;
	private static final boolean TRANSFORMED = false;
	
	public AcolyteOfPain() {
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
				SUMMONED,
				TRANSFORMED,
				false,
				false,
				true,
				false
			);
	}
	
	public AcolyteOfPain(	
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
			boolean summoned,
			boolean transformed,
			boolean destroyOnTurnStart,
			boolean destroyOnTurnEnd,
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
			summoned,
			transformed,
			destroyOnTurnStart,
			destroyOnTurnEnd,
			isInHand,
			hasBeenUsed);
	}
	
	@Override
	public Object deepCopy() {
		return new AcolyteOfPain(
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
				this.summoned_,
				this.transformed_,
				this.destroyOnTurnStart_,
				this.destroyOnTurnEnd_,
				this.isInHand_,
				this.hasBeenUsed_);
	}
	

	/**
	 * Called when this minion takes damage
	 * 
	 * Draw a card whenever this minion takes damage
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
	@Override
	public HearthTreeNode takeDamage(byte damage, int attackerPlayerIndex, int thisPlayerIndex, int thisMinionIndex, HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1, boolean isSpellDamage) throws HSInvalidPlayerIndexException {
		if (!divineShield_) {
			HearthTreeNode toRet = super.takeDamage(damage, attackerPlayerIndex, thisPlayerIndex, thisMinionIndex, boardState, deckPlayer0, deckPlayer1, isSpellDamage);
			if (damage > 0 && thisPlayerIndex == 0) {
				if (toRet instanceof CardDrawNode) {
					((CardDrawNode) toRet).addNumCardsToDraw(1);
				} else {
					toRet = new CardDrawNode(toRet, 1, this, 0, thisMinionIndex, thisPlayerIndex, thisMinionIndex); //draw one card
				}
			} else if (damage > 0) {
				//This minion is an enemy minion.  Let's draw a card for the enemy.  No need to use a StopNode for enemy card draws.
				Card card = deckPlayer1.drawCard(toRet.data_.getDeckPos(1));
				if (card == null) {
					byte fatigueDamage = toRet.data_.getFatigueDamage(1);
					toRet.data_.setFatigueDamage(1, (byte)(fatigueDamage + 1));
					toRet.data_.getHero(1).setHealth((byte)(toRet.data_.getHero(1).getHealth() - fatigueDamage));
				} else {
					toRet.data_.placeCard_hand(1, card);
					toRet.data_.setDeckPos(1, toRet.data_.getDeckPos(1) + 1);
				}
			}
			return toRet;
		} else {
			divineShield_ = false;
			return boardState;
		}
	}
}
