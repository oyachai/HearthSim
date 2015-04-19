package com.hearthsim.card.basic.spell;

import com.hearthsim.card.Card;
import com.hearthsim.card.spellcard.SpellDamageTargetableCard;
import com.hearthsim.event.effect.EffectHand;
import com.hearthsim.event.effect.EffectOnResolveRandomHand;
import com.hearthsim.event.filter.FilterHand;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Soulfire extends SpellDamageTargetableCard implements EffectOnResolveRandomHand {

    private static final EffectHand effect = new EffectHand() {
        @Override
        public HearthTreeNode applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, Card target, HearthTreeNode boardState) {
            boardState.data_.modelForSide(originSide).getHand().remove(target);
            return boardState;
        }
    };

    private static final FilterHand filter = FilterHand.OWN;

    public Soulfire() {
        super();
    }

    @Deprecated
    public Soulfire(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    @Override
    public EffectHand getRandomTargetEffect() {
        return Soulfire.effect;
    }

    @Override
    public FilterHand getRandomTargetFilter() {
        return Soulfire.filter;
    }
}
