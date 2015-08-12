package com.hearthsim.card.classic.minion.rare;

import com.hearthsim.card.Card;
import com.hearthsim.card.CardPlayBeginInterface;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectHeroDraw;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class GadgetzanAuctioneer extends Minion implements CardPlayBeginInterface {

    private final static EffectCharacter<Card> effect = new EffectHeroDraw<>(1);

    public GadgetzanAuctioneer() {
        super();
    }

    @Override
    public HearthTreeNode onCardPlayBegin(PlayerSide thisCardPlayerSide, PlayerSide cardUserPlayerSide, Card usedCard, HearthTreeNode boardState) {
        if (!this.setInHand() && thisCardPlayerSide == cardUserPlayerSide && usedCard instanceof SpellCard) {
            return GadgetzanAuctioneer.effect.applyEffect(thisCardPlayerSide, this, boardState);
        }

        return boardState;
    }
}
