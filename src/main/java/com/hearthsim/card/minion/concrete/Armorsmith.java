package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionDamagedInterface;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Armorsmith extends Minion implements MinionDamagedInterface {

	private static final boolean HERO_TARGETABLE = true;
	private static final byte SPELL_DAMAGE = 0;
	
	public Armorsmith() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;

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
	@Override
	public HearthTreeNode minionDamagedEvent(
			PlayerSide thisMinionPlayerSide,
			PlayerSide damagedPlayerSide,
			Minion damagedMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1)
	{
		HearthTreeNode toRet = boardState;
		if (thisMinionPlayerSide == damagedPlayerSide) {
			Hero hero = toRet.data_.getHero(thisMinionPlayerSide);
			hero.setArmor((byte)(hero.getArmor() + 1));
		}
		return toRet;
	}
}
