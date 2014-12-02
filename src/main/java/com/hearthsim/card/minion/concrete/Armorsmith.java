package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.entity.BaseEntity;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Armorsmith extends Minion {

	private static final boolean HERO_TARGETABLE = true;
	private static final boolean SUMMONED = false;
	private static final boolean TRANSFORMED = false;
	private static final byte SPELL_DAMAGE = 0;
	
	public Armorsmith() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;
        summoned_ = SUMMONED;
        transformed_ = TRANSFORMED;
	}
	
	/**
	 * 
	 * Whenever a friendly minion takes damage, gain 1 Armor
	 * 
	 *
     * @param thisMinionPlayerSide
     * @param damagedPlayerSide
     * @param damagedMinion The damaged minion
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     * @param deckPlayer0 The deck of player0    @return The boardState is manipulated and returned
     * */
	public HearthTreeNode minionDamagedEvent(
			PlayerSide thisMinionPlayerSide,
			PlayerSide damagedPlayerSide,
			BaseEntity damagedMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1)
		throws HSInvalidPlayerIndexException
	{
		HearthTreeNode toRet = super.minionDamagedEvent(thisMinionPlayerSide, damagedPlayerSide, damagedMinion, boardState, deckPlayer0, deckPlayer1);
		if (thisMinionPlayerSide == damagedPlayerSide) {
			Hero hero = toRet.data_.getHero(thisMinionPlayerSide);
			hero.setArmor((byte)(hero.getArmor() + 1));
		}
		return toRet;
	}
}
