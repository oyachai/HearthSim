package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.json.JSONObject;

public class TheCoin extends SpellCard {

	public TheCoin(boolean hasBeenUsed) {
		super("The Coin", (byte)0, hasBeenUsed);
	}

	public TheCoin() {
		this(false);
	}

	@Override
	public Object deepCopy() {
		return new TheCoin(this.hasBeenUsed());
	}

	@Override
    public boolean canBeUsedOn(PlayerModel playerModel, Minion minion, BoardModel boardModel) {
		if (playerModel == boardModel.getWaitingPlayer() || !(minion instanceof Hero))
			return false;
		return true;
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
		if (!this.canBeUsedOn(side, targetMinion, boardState.data_))
			return null;
		HearthTreeNode toRet = super.use_core(side, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);
		if (toRet != null) {
			int newMana = toRet.data_.getMana_p0();
			newMana = newMana >= 10 ? newMana : newMana + 1;
			toRet.data_.setMana_p0(newMana);
		}
		return boardState;
	}

	public JSONObject toJSON() {
		JSONObject json = super.toJSON();
		json.put("type", "SpellTheCoin");
		return json;
	}
}
