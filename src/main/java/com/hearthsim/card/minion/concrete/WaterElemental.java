package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.FrozenState;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionState;
import com.hearthsim.card.minion.MinionStateFactory;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;


public class WaterElemental extends Minion {

	private static final boolean HERO_TARGETABLE = true;
	private static final boolean SUMMONED = false;
	private static final boolean TRANSFORMED = false;
	private static final byte SPELL_DAMAGE = 0;
	
	public WaterElemental() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;
        summoned_ = SUMMONED;
        transformed_ = TRANSFORMED;
	}
	
	/**
	 * 
	 * Attack with the minion
	 * 
	 * Any minion attacked by this minion is frozen.
	 * 
	 *
     *
     * @param targetMinionPlayerSide
     * @param targetMinion The target minion
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     * @param deckPlayer0 The deck of player0
     * @return The boardState is manipulated and returned
	 */
	protected HearthTreeNode attack_core(
			PlayerSide targetMinionPlayerSide,
			Minion targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1)
		throws HSException
	{
		HearthTreeNode toRet = super.attack_core(targetMinionPlayerSide, targetMinion, boardState, deckPlayer0, deckPlayer1);
		if (!silenced_ && toRet != null) {
		//	targetMinion.setFrozen(true);
			MinionStateFactory mf = new MinionStateFactory();
			targetMinion.addState(mf.makeFrozen());
		}
		return toRet;
	}
}
