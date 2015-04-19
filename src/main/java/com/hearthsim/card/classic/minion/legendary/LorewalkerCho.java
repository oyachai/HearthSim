package com.hearthsim.card.classic.minion.legendary;

import com.hearthsim.card.Card;
import com.hearthsim.card.CardPlayBeginInterface;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectHero;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class LorewalkerCho extends Minion implements CardPlayBeginInterface {

    private final static EffectCharacter<Card> effect = new EffectHero<Card>() {
        @Override
        public HearthTreeNode applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, HearthTreeNode boardState) {
            PlayerModel targetPlayer = boardState.data_.modelForSide(targetSide);
            if (!targetPlayer.isHandFull()) {
                Card copy = origin.createResetCopy();
                targetPlayer.getHand().add(copy);
            }
            return boardState;
        }
    };

    public LorewalkerCho() {
        super();
    }

    @Override
    public HearthTreeNode onCardPlayBegin(PlayerSide thisCardPlayerSide, PlayerSide cardUserPlayerSide, Card usedCard, HearthTreeNode boardState, boolean singleRealizationOnly) {
        if (!this.isInHand() && usedCard instanceof SpellCard) {
            return LorewalkerCho.effect.applyEffect(cardUserPlayerSide, usedCard, cardUserPlayerSide.getOtherPlayer(), this, boardState);
        }

        return boardState;
    }
}
