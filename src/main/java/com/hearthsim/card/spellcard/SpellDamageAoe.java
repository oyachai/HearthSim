package com.hearthsim.card.spellcard;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class SpellDamageAoe extends SpellDamage {
	
	protected boolean hitsOwnHero = false;
	protected boolean hitsOwnMinions = false;
	protected boolean hitsEnemyHero = false;
	protected boolean hitsEnemyMinions = true;
	
	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public SpellDamageAoe(byte mana, byte damage, boolean hasBeenUsed) {
		super(mana, damage, hasBeenUsed);
	}

	@Override
	public boolean canBeUsedOn(PlayerSide playerSide, Minion minion, BoardModel boardModel) {
		if(!super.canBeUsedOn(playerSide, minion, boardModel)) {
			return false;
		}

		if(isCurrentPlayer(playerSide)) {
			return false;
		}

		if(isNotHero(minion)) {
			return false;
		}

		return true;
	}

	/**
	 * Use the card on the given target
	 * This card damages all enemy minions but not enemy hero
	 *
	 * @param side
	 * @param boardState The BoardState before this card has performed its action. It will be manipulated and returned.
	 * @return The boardState is manipulated and returned
	 */
	@Override
	protected HearthTreeNode use_core(PlayerSide side, Minion targetMinion, HearthTreeNode boardState,
			Deck deckPlayer0, Deck deckPlayer1, boolean singleRealizationOnly) throws HSException {
		if(boardState != null && this.hitsOwnHero) {
			Minion self = boardState.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 0);
			boardState = this.attack(PlayerSide.CURRENT_PLAYER, self, boardState, deckPlayer0, deckPlayer1);
		}

		if(boardState != null && this.hitsOwnMinions) {
			boardState = this.attackAllMinionsOnSide(PlayerSide.CURRENT_PLAYER, boardState, deckPlayer0, deckPlayer1);
		}

		if(boardState != null && this.hitsEnemyHero) {
			boardState = this.attack(PlayerSide.WAITING_PLAYER, targetMinion, boardState, deckPlayer0, deckPlayer1);
		}

		if(boardState != null && this.hitsEnemyMinions) {
			boardState = this.attackAllMinionsOnSide(PlayerSide.WAITING_PLAYER, boardState, deckPlayer0, deckPlayer1);
		}

		if(boardState != null) {
			boardState.data_.getCurrentPlayer().subtractMana(this.getManaCost(side, boardState));
			boardState.data_.removeCard_hand(this);
		}
		return boardState;
	}
}
