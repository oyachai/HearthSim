package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionDeadInterface;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class FlesheatingGhoul extends Minion implements MinionDeadInterface {

	private static final boolean HERO_TARGETABLE = true;
	private static final byte SPELL_DAMAGE = 0;
	
	public FlesheatingGhoul() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;

	}
	
	
	/**
	 * 
	 * Whenever a minion dies, gain +1 Attack
	 * 
	 *
     * @param thisMinionPlayerSide
     * @param deadMinionPlayerSide
     * @param deadMinion The dead minion
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     * @param deckPlayer0 The deck of player0    @return The boardState is manipulated and returned
     * */
	@Override
	public HearthTreeNode minionDeadEvent(
			PlayerSide thisMinionPlayerSide,
			PlayerSide deadMinionPlayerSide,
			Minion deadMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1)
	{
		this.attack_ += 1;
		return boardState;
	}

}
