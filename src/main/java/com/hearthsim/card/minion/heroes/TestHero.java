package com.hearthsim.card.minion.heroes;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestHero extends Hero {
    public TestHero() {
        this("NoHero", (byte) 30);
    }

    public TestHero(String name, byte health) {
        this.name_ = name;
        this.health_ = health;
        this.baseHealth_ = health_;
        this.maxHealth_ = health_;
        this.heroTargetable_ = true;
    }

	/**
	 * Use the hero ability on a given target
	 * 
	 * Test Hero: Does nothing
     *
     * @param targetPlayerSide
     * @param targetMinion The target minion
     * @param boardState
     * @param deckPlayer0
     * @param deckPlayer1
     *
     * @return
	 */
	@Override
	public HearthTreeNode useHeroAbility_core(
			PlayerSide targetPlayerSide,
			Minion targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1,
			boolean singleRealizationOnly)
		throws HSException
	{
		HearthTreeNode toRet = boardState;
		this.hasBeenUsed = true;
		toRet.data_.getCurrentPlayer().subtractMana(HERO_ABILITY_COST);
		return toRet;
	}

}
