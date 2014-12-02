package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;import com.hearthsim.entity.BaseEntity;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.json.JSONObject;

public class TheCoin extends SpellCard {

	public TheCoin(boolean hasBeenUsed) {
		super((byte)0, hasBeenUsed);
	}

	public TheCoin() {
		this(false);
	}

	
	public Object deepCopy() {
		return new TheCoin(this.hasBeenUsed());
	}

	
    public boolean canBeUsedOn(PlayerSide playerSide, BaseEntity minion, BoardModel boardModel) {
        return !(isWaitingPlayer(playerSide) || isNotHero(minion));
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
	
	protected HearthTreeNode use_core(
			PlayerSide side,
			BaseEntity targetMinion,
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
