package com.hearthsim.card.classic.minion.rare;

import com.hearthsim.card.Card;
import com.hearthsim.card.CardPlayBeginInterface;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterBuffTemp;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class ManaAddict extends Minion implements CardPlayBeginInterface {

    private final static EffectCharacter<Card> effect = new EffectCharacterBuffTemp<>(2);

    public ManaAddict() {
        super();
    }

    public HearthTreeNode onCardPlayBegin(PlayerSide thisCardPlayerSide, PlayerSide cardUserPlayerSide, Card usedCard,
            HearthTreeNode boardState) {
        if (this.setInHand()) {
            return boardState;
        }
        if (cardUserPlayerSide == thisCardPlayerSide && usedCard instanceof SpellCard) {
            ManaAddict.effect.applyEffect(thisCardPlayerSide, this, boardState);
        }
        return boardState;
    }
}
