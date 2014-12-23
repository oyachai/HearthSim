package com.hearthsim.card.minion.concrete;

import java.util.EnumSet;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionTargetableBattlecry;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Alexstrasza extends Minion implements MinionTargetableBattlecry {

	private static final boolean HERO_TARGETABLE = true;
	private static final byte SPELL_DAMAGE = 0;
	
	public Alexstrasza() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;

        this.tribe = MinionTribe.DRAGON;
	}
	@Override
	public EnumSet<BattlecryTargetType> getBattlecryTargets() {
		return EnumSet.of(BattlecryTargetType.FRIENDLY_HERO, BattlecryTargetType.ENEMY_HERO);
	}
	
	/**
	 * Battlecry: Set a hero's remaining health to 15
	 */
	@Override
	public HearthTreeNode useTargetableBattlecry_core(
			PlayerSide side,
			Minion targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1
		) throws HSException
	{
		side.getPlayer(boardState).getHero().setHealth((byte)15);
		return boardState;
	}

}
