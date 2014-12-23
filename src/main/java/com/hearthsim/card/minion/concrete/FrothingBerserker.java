package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionDamagedInterface;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class FrothingBerserker extends Minion implements MinionDamagedInterface {
	
	private static final boolean HERO_TARGETABLE = true;
	private static final byte SPELL_DAMAGE = 0;
	
	public FrothingBerserker() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;
	}
	
	/**
	 * Whenever a minion takes damage, gain 1 attack
     * */
	public HearthTreeNode minionDamagedEvent(
			PlayerSide thisMinionPlayerSide,
			PlayerSide damagedPlayerSide,
			Minion damagedMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1)
	{
		this.addAttack((byte)1);
		return boardState;
	}
}
