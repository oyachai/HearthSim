package com.hearthsim.util.tree;

import com.hearthsim.card.Deck;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.player.playercontroller.BoardScorer;
import com.hearthsim.util.HearthAction;

/**
 * A card draw triggers a stop
 *
 * Note: a card draw triggers a stop only when it is a card that you draw on your turn.
 * Any effects that draws a card for the opponent should just draw the card right away.
 *
 */
public class CardDrawNode extends StopNode {

    private int numCardsToDraw_;

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
     * Draw the cards that are queued
     * <p>
     * This function actually draws the cards that are queued up by the various card draw mechanics.
     * This function shouldn't be called by anyone except BoardStateFactory.
     */
    private void drawQueuedCard() {
        for (int indx = 0; indx < numCardsToDraw_; ++indx) {
            data_.getCurrentPlayer().drawNextCardFromDeck();
        }
    }


    public double cardDrawScore(Deck deck, BoardScorer ai) {
        int numCardsInDeck = deck.getNumCards();
        int numCardsRemaining = numCardsInDeck - data_.getCurrentPlayer().getDeckPos();
        int numCardsToActuallyDraw = numCardsToDraw_;
        int totalFatigueDamage = 0;
        if (numCardsRemaining < numCardsToDraw_) {
            //expected fatigue damage
            int fatigueDamage = data_.getCurrentPlayer().getFatigueDamage();
            for (int i = 0; i < numCardsToDraw_ - numCardsRemaining; ++i) {
                totalFatigueDamage += fatigueDamage;
                fatigueDamage += 1;
            }
            numCardsToActuallyDraw = numCardsRemaining;
        }
        //find the average card score of the remaining cards
        double averageCardScore = 0.0;
        if (numCardsRemaining > 0) {
            for (int indx = data_.getCurrentPlayer().getDeckPos(); indx < numCardsInDeck; ++indx) {
                averageCardScore += ai.cardInHandScore(deck.drawCard(indx), this.data_);
            }
            averageCardScore = averageCardScore / numCardsRemaining;
        }

        double toRet = averageCardScore * numCardsToActuallyDraw;
        int heroHealth = data_.getCurrentPlayer().getHero().getHealth();
        int heroArmor = data_.getCurrentPlayer().getHero().getArmor();
        int armorLeft = heroArmor > totalFatigueDamage ? heroArmor - totalFatigueDamage : 0;
        int healthLeft = armorLeft > 0 ? heroHealth : heroHealth - (totalFatigueDamage - heroArmor);
        toRet += ai.heroHealthScore_p0(healthLeft, armorLeft) - ai.heroHealthScore_p0(heroHealth, heroArmor);

        //If you are going to draw a card 2 different ways, it is usually better to draw the cards when you have more mana
        double manaBenefit = 1.e-2 * data_.getCurrentPlayer().getMana();
        toRet += manaBenefit;

        return toRet;
    }

    @Override
    public HearthTreeNode finishAllEffects() {
        this.drawQueuedCard();
        HearthAction drawAction = new HearthAction(HearthAction.Verb.DRAW_CARDS, PlayerSide.CURRENT_PLAYER, this.numCardsToDraw_);
        return new HearthTreeNode(this.data_, drawAction, this.score_, this.depth_, this.children_);
    }
}
