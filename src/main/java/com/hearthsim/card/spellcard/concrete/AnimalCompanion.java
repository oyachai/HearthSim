package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;import com.hearthsim.entity.BaseEntity;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.Huffer;
import com.hearthsim.card.minion.concrete.Leokk;
import com.hearthsim.card.minion.concrete.Misha;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.entity.BaseEntity;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.HearthAction;
import com.hearthsim.util.tree.HearthTreeNode;
import com.hearthsim.util.tree.RandomEffectNode;

public class AnimalCompanion extends SpellCard {

	
	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public AnimalCompanion(boolean hasBeenUsed) {
		super((byte)3, hasBeenUsed);
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public AnimalCompanion() {
		this(false);
	}

	
	public Object deepCopy() {
		return new AnimalCompanion(this.hasBeenUsed);
	}
	

	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Summons either Huffer, Leokk, or Misha
	 * 
	 *
     *
     * @param side
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     *
     * @return The boardState is manipulated and returned
	 */
	
	protected HearthTreeNode use_core(
			PlayerSide side,
			BaseEntity targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1,
			boolean singleRealizationOnly)
		throws HSException
	{
		if (isNotHero(targetMinion) || isWaitingPlayer(side)) {
			return null;
		}
		
		int numMinions = PlayerSide.CURRENT_PLAYER.getPlayer(boardState).getNumMinions();
		if (numMinions >= 7)
			return null;
		
		HearthTreeNode toRet = null;
		
		if (singleRealizationOnly) {
			toRet = super.use_core(side, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);
			if (toRet != null) {
				double rnd = Math.random();
				BaseEntity minion = null;
				if (rnd < 0.333333333333333333333333333) {
					minion = new Huffer();
				} else if (rnd > 0.66666666666666666666666666666) {
					minion = new Leokk();
				} else {
					minion = new Misha();
				}
            	BaseEntity placementTarget = toRet.data_.getCharacter(side, toRet.data_.getMinions(side).size()); //this minion can't be a hero
            	toRet = (minion).summonMinion(side, placementTarget, toRet, deckPlayer0, deckPlayer1, false);					
			}
		} else {
			toRet = new RandomEffectNode(boardState, new HearthAction(HearthAction.Verb.USE_CARD, PlayerSide.CURRENT_PLAYER, 0, side, 0));
			if (toRet != null) {
				int thisCardIndex = side.getPlayer(boardState).getHand().indexOf(this);
				for (BaseEntity minion : new Minion[]{ new Huffer(), new Leokk(), new Misha()}) {
					HearthTreeNode newState = toRet.addChild(new HearthTreeNode((BoardModel) toRet.data_.deepCopy()));
					newState = super.use_core(side, side.getPlayer(newState).getHero(), newState, deckPlayer0, deckPlayer1, singleRealizationOnly);
					BaseEntity placementTarget = newState.data_.getCharacter(side, newState.data_.getMinions(side).size()); //this minion can't be a hero
	            	newState = (minion).summonMinion(side, placementTarget, newState, deckPlayer0, deckPlayer1, false);					
					newState.data_.getCurrentPlayer().subtractMana(this.mana_);
					side.getPlayer(newState).getHand().remove(thisCardIndex);
				}
			}
		}
		return toRet;
	}

}
