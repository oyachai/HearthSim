package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class BloodImp extends Minion {
	
	private static final boolean HERO_TARGETABLE = true;
	private static final byte SPELL_DAMAGE = 0;
	
	public BloodImp() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;

        this.tribe = MinionTribe.DEMON;
	}

	@Override
	public HearthTreeNode endTurn(PlayerSide thisMinionPlayerIndex, HearthTreeNode boardModel, Deck deckPlayer0, Deck deckPlayer1) throws HSException {
		HearthTreeNode toRet = boardModel;
		if (thisMinionPlayerIndex == PlayerSide.CURRENT_PLAYER) {
			PlayerModel player = PlayerSide.CURRENT_PLAYER.getPlayer(toRet);
			if (player.getNumMinions() > 1) {
				Minion buffTargetMinion = this;
				while (buffTargetMinion == this) {
					buffTargetMinion = player.getMinions().get((int)(Math.random() * player.getNumMinions()));
				}
				buffTargetMinion.addMaxHealth((byte)1);
				buffTargetMinion.addHealth((byte)1);
			}
		}
		return super.endTurn(thisMinionPlayerIndex, toRet, deckPlayer0, deckPlayer1);
	}
}
