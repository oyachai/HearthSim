package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;import com.hearthsim.entity.BaseEntity;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Undertaker extends Minion {

	private static final boolean HERO_TARGETABLE = true;
	private static final boolean SUMMONED = false;
	private static final boolean TRANSFORMED = false;
	private static final byte SPELL_DAMAGE = 0;
	
	public Undertaker() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;
        summoned_ = SUMMONED;
        transformed_ = TRANSFORMED;
	}
	
	/**
	 * Whenever a minion with Deathrattle is summoned, gain +1/+1
     * */
	public HearthTreeNode minionSummonedEvent(
			PlayerSide thisMinionPlayerSide,
			PlayerSide summonedMinionPlayerSide,
			BaseEntity summonedMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1)
		throws HSInvalidPlayerIndexException
	{
		HearthTreeNode toRet = super.minionSummonedEvent(thisMinionPlayerSide, summonedMinionPlayerSide, summonedMinion, boardState, deckPlayer0, deckPlayer1);
		if (toRet != null && summonedMinion.hasDeathrattle() && thisMinionPlayerSide == summonedMinionPlayerSide) {
			this.addHealth((byte)1);
			this.addAttack((byte)1);
		}
		return toRet;
	}

}
