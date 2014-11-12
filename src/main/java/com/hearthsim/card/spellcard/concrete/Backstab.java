package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public class Backstab extends SpellDamage {
	
	public Backstab() {
		this(false);
	}

	public Backstab(boolean hasBeenUsed) {
		super((byte)0, (byte)2, hasBeenUsed);
	}
	
	@Override
	public SpellDamage deepCopy() {
		return new Backstab(this.hasBeenUsed);
	}
	
	@Override
	public boolean canBeUsedOn(PlayerSide playerSide, Minion minion, BoardModel boardModel) {
		if(!super.canBeUsedOn(playerSide, minion, boardModel)) {
			return false;
		}
		
		if (isHero(minion)) {
			return false;
		}

		if (minion.getHealth() != minion.getMaxHealth()) {
			return false;
		}
		
		return true;
	}	
}
