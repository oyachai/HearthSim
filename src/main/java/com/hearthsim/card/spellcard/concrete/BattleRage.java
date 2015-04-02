package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.CharacterFilterTargetedSpell;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.IdentityLinkedList;
import com.hearthsim.util.tree.CardDrawNode;
import com.hearthsim.util.tree.HearthTreeNode;

public class BattleRage extends SpellCard {


    /**
     * Constructor
     *
     * @param hasBeenUsed Whether the card has already been used or not
     */
    @Deprecated
    public BattleRage(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public BattleRage() {
        super();
    }

    @Override
    public CharacterFilter getTargetableFilter() {
        return CharacterFilterTargetedSpell.SELF;
    }

    /**
     *
     * Use the card on the given target
     *
     * Draw a card for each damaged friendly character
     *
     * @param thisCardIndex The index (position) of the card in the hand
     * @param playerIndex The index of the target player.  0 if targeting yourself or your own minions, 1 if targeting the enemy
     * @param minionIndex The index of the target minion.
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     *
     * @return The boardState is manipulated and returned
     */
    @Override
    public CardEffectCharacter getTargetableEffect() {
        if (this.effect == null) {
            this.effect = new CardEffectCharacter() {
                @Override
                public HearthTreeNode applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) {
                    PlayerModel playerModel = boardState.data_.modelForSide(targetSide);
                    Hero hero = playerModel.getHero();
                    IdentityLinkedList<Minion> minions = playerModel.getMinions();
                    int numCardsToDraw = hero.getTotalHealth() < hero.getTotalMaxHealth() ? 1 : 0;
                    for (Minion minion : minions) {
                        numCardsToDraw += minion.getTotalHealth() < minion.getTotalMaxHealth() ? 1 : 0;
                    }
                    if (boardState instanceof CardDrawNode) {
                        ((CardDrawNode) boardState).addNumCardsToDraw(numCardsToDraw);
                    } else {
                        boardState = new CardDrawNode(boardState, numCardsToDraw); //draw two cards
                    }
                    return boardState;
                }
            };
        }
        return this.effect;
    }
}
