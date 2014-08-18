package com.hearthsim.util.tree;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.player.playercontroller.ArtificialPlayer;

/**
 * A card draw triggers a stop
 * 
 * Note: a card draw triggers a stop only when it is a card that you draw on your turn.
 * Any effects that draws a card for the opponent should just draw the card right away.
 *
 */
public class CardDrawNode extends StopNode {
	
	int numCardsToDraw_;
	
	public CardDrawNode(HearthTreeNode origNode, int numCardsToDraw) {
		super(origNode);
		numCardsToDraw_ = numCardsToDraw;
	}

	/**
	 * Add a number of cards to the draw queue
	 * 
	 * @param valueToAdd
	 */
	public void addNumCardsToDraw(int valueToAdd) {
		numCardsToDraw_ += valueToAdd;
	}
	
	public int getNumCardsToDraw() {
		return numCardsToDraw_;
	}
	
	/**
	 * Queue up a specified number of cards for drawing from the deck
	 * 
	 * @param value Number of cards to queue up for drawing
	 */
	public void setNumCardsToDraw(int value) {
		numCardsToDraw_ = value;
	}
	
	/**
	 * Draw the cards that are queued 
	 * 
	 * This function actually draws the cards that are queued up by the various card draw mechanics.
	 * This function shouldn't be called by anyone except BoardStateFactory.
	 * 
	 */
	public void drawCard(Deck deck) {
		Card card = deck.drawCard(data_.getDeckPos_p0());
		if (card == null) {
			byte fatigueDamage = data_.getFatigueDamage_p0();
			data_.setFatigueDamage_p0((byte)(fatigueDamage + 1));
			data_.getHero_p0().setHealth((byte)(data_.getHero_p0().getHealth() - fatigueDamage));
		} else {
			data_.placeCard_hand_p0(card);
			data_.setDeckPos_p0(data_.getDeckPos_p0() + 1);
		}
	}
	

	public double cardDrawScore(Deck deck, ArtificialPlayer ai) {
		int numCardsInDeck = deck.getNumCards();
		int numCardsRemaining = numCardsInDeck - data_.getDeckPos_p0();
		int numCardsToActuallyDraw = numCardsToDraw_;
		int totalFatigueDamage = 0;
		if (numCardsRemaining < numCardsToDraw_) {
			//expected fatigue damage
			int fatigueDamage = data_.getFatigueDamage_p0();
			for (int i = 0; i < numCardsToDraw_ - numCardsRemaining; ++i) {
				totalFatigueDamage += fatigueDamage;
				fatigueDamage += 1;
			}
			numCardsToActuallyDraw = numCardsRemaining;
		}
		//find the average card score of the remaining cards
		double averageCardScore = 0.0;
		for (int indx = data_.getDeckPos_p0(); indx < numCardsInDeck; ++indx) {
			averageCardScore += ai.cardInHandScore(deck.drawCard(indx));
		}
		averageCardScore = numCardsRemaining <= 0 ? 0.0 : averageCardScore / numCardsRemaining;
		
		double toRet = averageCardScore * numCardsToActuallyDraw;
		int heroHealth = data_.getHero_p0().getHealth();
		int heroArmor = data_.getHero_p0().getArmor();
		int armorLeft = heroArmor > totalFatigueDamage ? heroArmor - totalFatigueDamage : 0;
		int healthLeft = armorLeft > 0 ? heroHealth : heroHealth - (totalFatigueDamage - heroArmor);
		toRet += ai.heroHealthScore_p0(healthLeft, armorLeft) - ai.heroHealthScore_p0(heroHealth, heroArmor);
		
		//If you are going to draw a card 2 different ways, it is usually better to draw the cards first
		double depthPenalty = 1.e-4 * depth_;
		toRet -= depthPenalty;
		
		return toRet;
	}

	@Override
	public HearthTreeNode finishAllEffects(Deck deck) {
		for (int i = 0; i < numCardsToDraw_; ++i)
			this.drawCard(deck);
		return new HearthTreeNode(this.data_, this.score_, this.depth_, this.children_, this.numNodesTried_);
	}
}
