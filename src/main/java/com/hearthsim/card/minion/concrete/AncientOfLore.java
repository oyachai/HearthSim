package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.attack.AttackAction;
import com.hearthsim.event.deathrattle.DeathrattleAction;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.CardDrawNode;
import com.hearthsim.util.tree.HearthTreeNode;

public class AncientOfLore extends Minion {

	private static final byte HEAL_AMOUNT = 5;
	
	private static final String NAME = "Ancient of Lore";
	private static final byte MANA_COST = 7;
	private static final byte ATTACK = 5;
	private static final byte HEALTH = 5;
	
	private static final boolean TAUNT = false;
	private static final boolean DIVINE_SHIELD = false;
	private static final boolean WINDFURY = false;
	private static final boolean CHARGE = false;
	
	private static final boolean STEALTHED = false;
	private static final boolean HERO_TARGETABLE = true;
	private static final boolean SUMMONED = false;
	private static final boolean TRANSFORMED = false;
	private static final byte SPELL_DAMAGE = 0;
	
	public AncientOfLore() {
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
	
	public AncientOfLore(	
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
		return new AncientOfLore(
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
				this.hasBeenUsed);
	}

	/**
	 * 
	 * Choose one: Draw 2 cards; or Restore 5 health
	 * 
	 *
     *
     * @param side
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     *
     * @return The boardState is manipulated and returned
	 */
	@Override
	public HearthTreeNode use_core(
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
			int thisMinionIndex = PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getMinions().indexOf(this);
			{
				HearthTreeNode newState = toRet.addChild(new CardDrawNode(new HearthTreeNode((BoardModel)toRet.data_.deepCopy()), 2));
			}
			{
				{
					HearthTreeNode newState = new HearthTreeNode((BoardModel)toRet.data_.deepCopy());
					newState = newState.data_.getCurrentPlayerHero().takeHeal(HEAL_AMOUNT, PlayerSide.CURRENT_PLAYER, newState, deckPlayer0, deckPlayer1);
					toRet.addChild(newState);
				}
				
				{
					for (int index = 0; index < PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getNumMinions(); ++index) {
						if (index != thisMinionIndex) {
							HearthTreeNode newState = new HearthTreeNode((BoardModel)toRet.data_.deepCopy());
							newState = PlayerSide.CURRENT_PLAYER.getPlayer(newState).getMinions().get(index).takeHeal(HEAL_AMOUNT, PlayerSide.CURRENT_PLAYER, newState, deckPlayer0, deckPlayer1);
							toRet.addChild(newState);
						}
					}
				}

				{
					HearthTreeNode newState = new HearthTreeNode((BoardModel)toRet.data_.deepCopy());
					newState = newState.data_.getWaitingPlayerHero().takeHeal(HEAL_AMOUNT, PlayerSide.WAITING_PLAYER, newState, deckPlayer0, deckPlayer1);
					toRet.addChild(newState);
				}
				
				{
					for (int index = 0; index < PlayerSide.WAITING_PLAYER.getPlayer(toRet).getNumMinions(); ++index) {
						HearthTreeNode newState = new HearthTreeNode((BoardModel)toRet.data_.deepCopy());
						newState = PlayerSide.WAITING_PLAYER.getMinions().get(index).takeHeal(HEAL_AMOUNT, PlayerSide.WAITING_PLAYER, newState, deckPlayer0, deckPlayer1);
						toRet.addChild(newState);
					}
				}
			}
			return toRet;
		} else {
			return null;
		}
	}
}
