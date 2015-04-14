package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.spellcard.SpellDamageTargetableCard;
import com.hearthsim.card.spellcard.SpellRandomInterface;
import com.hearthsim.event.HandFilter;
import com.hearthsim.event.effect.CardEffectHand;
import com.hearthsim.event.effect.CardEffectOnResolveRandomCharacterInterface;
import com.hearthsim.event.effect.CardEffectOnResolveRandomHandInterface;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

import java.util.ArrayList;
import java.util.Collection;

public class Soulfire extends SpellDamageTargetableCard implements CardEffectOnResolveRandomHandInterface {

    private static final CardEffectHand effect = new CardEffectHand() {
        @Override
        public HearthTreeNode applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, Card target, HearthTreeNode boardState) {
            boardState.data_.modelForSide(originSide).getHand().remove(target);
            return boardState;
        }
    };

    private static final HandFilter filter = new HandFilter();

    public Soulfire() {
        super();
    }

    @Deprecated
    public Soulfire(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    @Override
    public CardEffectHand getRandomTargetEffect() {
        return Soulfire.effect;
    }

    @Override
    public HandFilter getRandomTargetFilter() {
        return Soulfire.filter;
    }
}
