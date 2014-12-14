package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class YoungPriestess extends Minion {

	private static final boolean HERO_TARGETABLE = true;
	private static final byte SPELL_DAMAGE = 0;
	
	public YoungPriestess() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;

	}

	
	/**
	 * At the end of your turn, give another random friendly minion +1 Health
	 */
	@Override
	public HearthTreeNode endTurn(PlayerSide thisMinionPlayerIndex, HearthTreeNode boardModel, Deck deckPlayer0, Deck deckPlayer1) throws HSException {
		HearthTreeNode toRet = super.endTurn(thisMinionPlayerIndex, boardModel, deckPlayer0, deckPlayer1);
		if (toRet != null && thisMinionPlayerIndex == PlayerSide.CURRENT_PLAYER) {
			int numFriendlyMinions = PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getNumMinions();
			if (numFriendlyMinions > 1) {
				int minionToBuffIndex = (int)(Math.random() * numFriendlyMinions);
				while (PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getMinions().get(minionToBuffIndex) == this) {
					minionToBuffIndex = (int)(Math.random() * numFriendlyMinions);
				}
				PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getMinions().get(minionToBuffIndex).addHealth((byte)1);
				PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getMinions().get(minionToBuffIndex).addMaxHealth((byte)1);
			}
		}
		return toRet;
	}
}
