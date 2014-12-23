package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public class DreadCorsair extends Minion {
	
	private static final boolean HERO_TARGETABLE = true;
	private static final byte SPELL_DAMAGE = 0;
	
	public DreadCorsair() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;
        this.tribe = MinionTribe.PIRATE;
	}
	
	@Override
	public byte getManaCost(PlayerSide side, BoardModel board) {
		if (side.getPlayer(board).getHero().getWeapon() == null)
			return baseManaCost;
		byte manaCost = (byte)(baseManaCost - side.getPlayer(board).getHero().getWeapon().getWeaponDamage());
		if (manaCost < 0)
			manaCost = 0;
		return manaCost;
	}

}
