package com.hearthsim.util;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.util.tree.HearthTreeNode;


/**
 * A class representing an action that a player can take
 * 
 *
 */
public class HearthAction {
	
	public enum Verb {
		USE_CARD, HERO_ABILITY, ATTACK
	}
		
	public final Verb verb_;
	
	public final int actionPerformerPlayerIndex_;
	public final int cardOrCharacterIndex_;
	
	public final int targetPlayerIndex_;
	public final int targetCharacterIndex_;
	
	public HearthAction(Verb verb, int actionPerformerPlayerIndex, int cardOrCharacterIndex, int targetPlayerIndex, int targetCharacterIndex) {
		verb_ = verb;
		actionPerformerPlayerIndex_ = actionPerformerPlayerIndex;
		cardOrCharacterIndex_ = cardOrCharacterIndex;

		targetPlayerIndex_ = targetPlayerIndex;
		targetCharacterIndex_ = targetCharacterIndex;
	}

	public HearthTreeNode perform(HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1) throws HSException {
		HearthTreeNode toRet = boardState;
		switch(verb_) {
			case USE_CARD: {
				Card card = boardState.data_.getCard_hand(actionPerformerPlayerIndex_, cardOrCharacterIndex_);
				Minion target = boardState.data_.getCharacter(targetPlayerIndex_, targetCharacterIndex_);
				toRet = card.useOn(targetPlayerIndex_, target, toRet, deckPlayer0, deckPlayer1, true);
			}
			break;
			case HERO_ABILITY: {
				Hero hero = boardState.data_.getHero(actionPerformerPlayerIndex_);
				Minion target = boardState.data_.getCharacter(targetPlayerIndex_, targetCharacterIndex_);
				toRet = hero.useHeroAbility(targetPlayerIndex_, target, toRet, deckPlayer0, deckPlayer1, true);
			}
			break;
			case ATTACK: {
				Minion attacker = boardState.data_.getCharacter(actionPerformerPlayerIndex_, cardOrCharacterIndex_);
				Minion target = boardState.data_.getCharacter(targetPlayerIndex_, targetCharacterIndex_);
				toRet = attacker.attack(targetPlayerIndex_, target, toRet, deckPlayer0, deckPlayer1);
			}
			break;
		}
		return toRet;
	}
}
