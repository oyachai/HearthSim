package com.hearthsim.card.minion;

import java.util.EnumSet;

import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public interface MinionWithAura {

	public EnumSet<AuraTargetType> getAuraTargets();

	public void applyAura(
            PlayerSide targetSide, Minion targetMinion, BoardModel boardModel);
	
	public void removeAura(
            PlayerSide targetSide, Minion targetMinion, BoardModel boardModel);
	
}
