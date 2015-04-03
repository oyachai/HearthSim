package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.Hound;
import com.hearthsim.card.spellcard.SpellTargetableCard;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.CharacterFilterTargetedSpell;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class UnleashTheHounds extends SpellTargetableCard {

    @Deprecated
    public UnleashTheHounds(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    public UnleashTheHounds() {
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
     * @param side
     * @param targetMinion
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     * @param singleRealizationOnly
     *
     * @return The boardState is manipulated and returned
     */
    @Override
    public CardEffectCharacter getTargetableEffect() {
        if (this.effect == null) {
            this.effect = new CardEffectCharacter() {
                @Override
                public HearthTreeNode applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) {
                    PlayerModel currentPlayer = boardState.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
                    PlayerModel waitingPlayer = boardState.data_.modelForSide(PlayerSide.WAITING_PLAYER);
                    int numHoundsToSummon = waitingPlayer.getNumMinions();
                    if (numHoundsToSummon + currentPlayer.getNumMinions() > 7)
                        numHoundsToSummon = 7 - currentPlayer.getNumMinions();
                    for (int indx = 0; indx < numHoundsToSummon; ++indx) {
                        Minion placementTarget = currentPlayer.getNumMinions() > 0 ? currentPlayer.getMinions().getLast() : currentPlayer.getHero();
                        boardState = new Hound().summonMinion(PlayerSide.CURRENT_PLAYER, placementTarget, boardState, false, false);
                    }
                    return boardState;
                }
            };
        }
        return this.effect;
    }
}
