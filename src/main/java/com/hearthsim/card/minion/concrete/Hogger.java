package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Hogger extends Minion {

	private static final boolean HERO_TARGETABLE = true;
	private static final byte SPELL_DAMAGE = 0;
	
	public Hogger() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;

	}

	@Override
	public HearthTreeNode endTurn(PlayerSide thisMinionPlayerIndex, HearthTreeNode boardModel, Deck deckPlayer0, Deck deckPlayer1) throws HSException {
		HearthTreeNode toRet = boardModel;
		if (thisMinionPlayerIndex == PlayerSide.CURRENT_PLAYER && PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getNumMinions() < 7) {
			Minion minion = new Gnoll();
			Minion placementTarget = PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getMinions().getLast();
			minion.summonMinion(PlayerSide.CURRENT_PLAYER, placementTarget, toRet, deckPlayer0, deckPlayer1, false, false);
		}
		return super.endTurn(thisMinionPlayerIndex, toRet, deckPlayer0, deckPlayer1);
	}

}
