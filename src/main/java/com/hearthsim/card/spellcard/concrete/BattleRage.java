package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.MinionList;
import com.hearthsim.util.tree.CardDrawNode;
import com.hearthsim.util.tree.HearthTreeNode;

public class BattleRage extends SpellCard {


	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public BattleRage(boolean hasBeenUsed) {
		super((byte)2, hasBeenUsed);
		
		this.canTargetEnemyHero = false;
		this.canTargetEnemyMinions = false;
		this.canTargetOwnMinions = false;
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public BattleRage() {
		this(false);
	}

	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Draw a card for each damaged friendly character
	 * 
	 * @param thisCardIndex The index (position) of the card in the hand
	 * @param playerIndex The index of the target player.  0 if targeting yourself or your own minions, 1 if targeting the enemy
	 * @param minionIndex The index of the target minion.
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * 
	 * @return The boardState is manipulated and returned
	 */
	@Override
	protected HearthTreeNode use_core(
			PlayerSide targetPlayerSide,
			Minion targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1,
			boolean singleRealizationOnly)
		throws HSException
	{
		HearthTreeNode toRet = super.use_core(targetPlayerSide, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);
		if (toRet != null) {
            PlayerModel playerModel = targetPlayerSide.getPlayer(toRet);
            Hero hero = playerModel.getHero();
            MinionList minions = playerModel.getMinions();
            int numCardsToDraw = hero.getTotalHealth() < hero.getTotalMaxHealth() ? 1 : 0;
			for (Minion minion : minions) {
				numCardsToDraw += minion.getTotalHealth() < minion.getTotalMaxHealth() ? 1 : 0;
			}
			if (toRet instanceof CardDrawNode) {
				((CardDrawNode) toRet).addNumCardsToDraw(numCardsToDraw);
			} else {
				toRet = new CardDrawNode(toRet, numCardsToDraw); //draw two cards
			}
		}
		return toRet;
	}
}
