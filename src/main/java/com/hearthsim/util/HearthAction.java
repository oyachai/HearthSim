package com.hearthsim.util;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
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
	
	public final PlayerModel actionPerformerPlayerModel;
	public final int cardOrCharacterIndex_;
	
	public final PlayerModel targetPlayerModel;
	public final int targetCharacterIndex_;
	
	public HearthAction(Verb verb, PlayerModel actionPerformerPlayerModel, int cardOrCharacterIndex, PlayerModel targetPlayerModel, int targetCharacterIndex) {
		verb_ = verb;
		this.actionPerformerPlayerModel = actionPerformerPlayerModel;
		cardOrCharacterIndex_ = cardOrCharacterIndex;

		this.targetPlayerModel = targetPlayerModel;
		targetCharacterIndex_ = targetCharacterIndex;
	}

	public HearthTreeNode perform(HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1) throws HSException {
		HearthTreeNode toRet = boardState;
		switch(verb_) {
			case USE_CARD: {
				Card card = boardState.data_.getCard_hand(actionPerformerPlayerModel, cardOrCharacterIndex_);
				Minion target = boardState.data_.getCharacter(targetPlayerModel, targetCharacterIndex_);
				toRet = card.useOn(targetPlayerModel, target, toRet, deckPlayer0, deckPlayer1, true);
			}
			break;
			case HERO_ABILITY: {
				Hero hero = boardState.data_.getHero(actionPerformerPlayerModel);
				Minion target = boardState.data_.getCharacter(targetPlayerModel, targetCharacterIndex_);
				toRet = hero.useHeroAbility(targetPlayerModel, target, toRet, deckPlayer0, deckPlayer1, true);
			}
			break;
			case ATTACK: {
				Minion attacker = boardState.data_.getCharacter(actionPerformerPlayerModel, cardOrCharacterIndex_);
				Minion target = boardState.data_.getCharacter(targetPlayerModel, targetCharacterIndex_);
				toRet = attacker.attack(targetPlayerModel, target, toRet, deckPlayer0, deckPlayer1);
			}
			break;
		}
		return toRet;
	}
}
