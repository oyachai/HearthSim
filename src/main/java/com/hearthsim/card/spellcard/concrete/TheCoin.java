package com.hearthsim.card.spellcard.concrete;

import org.json.JSONObject;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class TheCoin extends SpellCard {

	public TheCoin(boolean hasBeenUsed) {
		super((byte)0, hasBeenUsed);

		this.canTargetEnemyHero = false;
		this.canTargetEnemyMinions = false;
		this.canTargetOwnMinions = false;
	}

	public TheCoin() {
		this(false);
	}

	@Override
	public SpellCard deepCopy() {
		return new TheCoin(this.hasBeenUsed());
	}

	/**
	 * 
	 * Use the card on the given target
	 * 
	 *
     *
     * @param side
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
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
			int newMana = toRet.data_.getCurrentPlayer().getMana();
			newMana = newMana >= 10 ? newMana : newMana + 1;
			toRet.data_.getCurrentPlayer().setMana(newMana);
		}
		return boardState;
	}

	public JSONObject toJSON() {
		JSONObject json = super.toJSON();
		json.put("type", "SpellTheCoin");
		return json;
	}
}
