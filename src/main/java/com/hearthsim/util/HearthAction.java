package com.hearthsim.util;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
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
	
	public final PlayerSide actionPerformerPlayerSide;
	public final int cardOrCharacterIndex_;
	
	public final PlayerSide targetPlayerSide;
	public final int targetCharacterIndex_;
	
	public final PlayerSide battlecryTargetPlayerSide;
	public final int battlecryTargetCharacterIndex;
	
	public HearthAction(Verb verb, PlayerSide actionPerformerPlayerSide, int cardOrCharacterIndex, PlayerSide targetPlayerSide, int targetCharacterIndex) {
		this(verb, actionPerformerPlayerSide, cardOrCharacterIndex, targetPlayerSide, targetCharacterIndex, null, -1);
	}
	
	public HearthAction(Verb verb, PlayerSide actionPerformerPlayerSide, int cardOrCharacterIndex, PlayerSide targetPlayerSide, int targetCharacterIndex, PlayerSide battlecryTargetPlayerSide, int battlecryTargetCharacterIndex) {
		verb_ = verb;
		this.actionPerformerPlayerSide = actionPerformerPlayerSide;
		cardOrCharacterIndex_ = cardOrCharacterIndex;

		this.targetPlayerSide = targetPlayerSide;
		targetCharacterIndex_ = targetCharacterIndex;
		
		this.battlecryTargetPlayerSide = battlecryTargetPlayerSide;
		this.battlecryTargetCharacterIndex = battlecryTargetCharacterIndex;
	}

	public HearthTreeNode perform(HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1) throws HSException {
		HearthTreeNode toRet = boardState;
		switch(verb_) {
			case USE_CARD: {
				Card card = boardState.data_.getCard_hand(actionPerformerPlayerSide, cardOrCharacterIndex_);
				Minion target = boardState.data_.getCharacter(targetPlayerSide, targetCharacterIndex_);
				toRet = card.getCardAction().useOn(targetPlayerSide, target, toRet, deckPlayer0, deckPlayer1, true);
			}
			break;
			case HERO_ABILITY: {
				Hero hero = boardState.data_.getHero(actionPerformerPlayerSide);
				Minion target = boardState.data_.getCharacter(targetPlayerSide, targetCharacterIndex_);
				toRet = hero.useHeroAbility(targetPlayerSide, target, toRet, deckPlayer0, deckPlayer1, true);
			}
			break;
			case ATTACK: {
				Minion attacker = boardState.data_.getCharacter(actionPerformerPlayerSide, cardOrCharacterIndex_);
				Minion target = boardState.data_.getCharacter(targetPlayerSide, targetCharacterIndex_);
				toRet = attacker.attack(targetPlayerSide, target, toRet, deckPlayer0, deckPlayer1);
			}
			break;
		}
		return toRet;
	}
}
