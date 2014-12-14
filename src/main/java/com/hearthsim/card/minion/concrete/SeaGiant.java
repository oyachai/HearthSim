package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public class SeaGiant extends Minion {
	
	private static final boolean HERO_TARGETABLE = true;
	private static final byte SPELL_DAMAGE = 0;
	
	public SeaGiant() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;

	}
	
	@Override
	public byte getManaCost(PlayerSide side, BoardModel board) {
		byte manaCost = (byte)(baseManaCost - PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().size() - PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().size());
		if (manaCost < 0)
			manaCost = 0;
		return manaCost;
	}

}
