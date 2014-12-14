package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class QuestingAdventurer extends Minion {

	private static final boolean HERO_TARGETABLE = true;
	private static final boolean TRANSFORMED = false;
	private static final byte SPELL_DAMAGE = 0;
	
	public QuestingAdventurer() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;
        transformed_ = TRANSFORMED;
	}
	
	@Override
	public HearthTreeNode onCardPlayBegin(
			PlayerSide thisCardPlayerSide,
			PlayerSide cardUserPlayerSide,
			Card usedCard,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1,
			boolean singleRealizationOnly)
	throws HSException
	{
		HearthTreeNode toRet = super.onCardPlayBegin(thisCardPlayerSide, cardUserPlayerSide, usedCard, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);
		if (usedCard != this && thisCardPlayerSide == cardUserPlayerSide) {
			this.addAttack((byte)1);
			this.addHealth((byte)1);
			this.addMaxHealth((byte)1);
		}
		return toRet;
	}

}
