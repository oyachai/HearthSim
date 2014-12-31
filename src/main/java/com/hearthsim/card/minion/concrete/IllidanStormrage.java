package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.CardPlayBeginInterface;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.FlameOfAzzinoth;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class IllidanStormrage extends Minion implements CardPlayBeginInterface {

	private static final boolean HERO_TARGETABLE = true;
	private static final byte SPELL_DAMAGE = 0;
	
	public IllidanStormrage() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;

        this.tribe = MinionTribe.DEMON;
	}

	@Override
	public HearthTreeNode onCardPlayBegin(PlayerSide thisCardPlayerSide,
			PlayerSide cardUserPlayerSide, Card usedCard,
			HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1,
			boolean singleRealizationOnly) throws HSException {
		HearthTreeNode toRet = boardState;
		if (thisCardPlayerSide == cardUserPlayerSide && usedCard != this) {
			if (thisCardPlayerSide.getPlayer(toRet).getMinions().size() < 7) {
				toRet = new FlameOfAzzinoth().summonMinion(thisCardPlayerSide, this, toRet, deckPlayer0, deckPlayer1, false, singleRealizationOnly);
			}
		}
		return toRet;
	}
}
