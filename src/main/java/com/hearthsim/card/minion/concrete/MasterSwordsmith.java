package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class MasterSwordsmith extends Minion {

	private static final boolean HERO_TARGETABLE = true;
	private static final byte SPELL_DAMAGE = 5;
	
	public MasterSwordsmith() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;

	}
	
	@Override
	public HearthTreeNode endTurn(PlayerSide thisMinionPlayerIndex, HearthTreeNode boardModel, Deck deckPlayer0, Deck deckPlayer1) throws HSException {
		HearthTreeNode toRet = boardModel;
		int numMinions = PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getNumMinions();
		if (thisMinionPlayerIndex == PlayerSide.CURRENT_PLAYER && numMinions > 1) {	
			Minion buffTargetMinion = PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getMinions().get((int)(Math.random() * numMinions));
			while (buffTargetMinion == this) {
				buffTargetMinion = PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getMinions().get((int)(Math.random() * numMinions));
			}
			buffTargetMinion.addAttack((byte)1);
		}
		return super.endTurn(thisMinionPlayerIndex, toRet, deckPlayer0, deckPlayer1);
	}

}
