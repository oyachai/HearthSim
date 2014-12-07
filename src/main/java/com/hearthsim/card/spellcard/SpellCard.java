package com.hearthsim.card.spellcard;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

import org.json.JSONObject;

public class SpellCard extends Card {

	protected boolean canTargetOwnHero = true;
	protected boolean canTargetOwnMinions = true;
	protected boolean canTargetEnemyHero = true;
	protected boolean canTargetEnemyMinions = true;
	
	public SpellCard(byte mana, boolean hasBeenUsed) {
		super(mana, hasBeenUsed, true);
	}

	public SpellCard(byte mana) {
		this(mana, false);
	}

	@Override
	public boolean canBeUsedOn(PlayerSide playerSide, Minion minion, BoardModel boardModel) {
		if(hasBeenUsed || minion.getStealthed() || !minion.isHeroTargetable()) {
			return false;
		}

		if (!canTargetOwnHero && isCurrentPlayer(playerSide) && isHero(minion)) {
			return false;
		}

		if (!canTargetOwnMinions && isCurrentPlayer(playerSide) && !isHero(minion)) {
			return false;
		}

		if (!canTargetEnemyHero && isWaitingPlayer(playerSide) && isHero(minion)) {
			return false;
		}

		if (!canTargetEnemyMinions && isWaitingPlayer(playerSide) && !isHero(minion)) {
			return false;
		}

		return true;
	}

	@Override
	public JSONObject toJSON() {
		JSONObject json = super.toJSON();
		json.put("type", "SpellCard");
		return json;
	}
}
