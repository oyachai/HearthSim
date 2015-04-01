package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.effect.CardEffectAoeInterface;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectCharacterBuffDelta;
import com.hearthsim.util.tree.HearthTreeNode;

public class ColdlightSeer extends Minion implements MinionUntargetableBattlecry, CardEffectAoeInterface {

    private static final CardEffectCharacter effect = new CardEffectCharacterBuffDelta(0, 2);

    private static final CharacterFilter filter = new CharacterFilter() {
        @Override
        protected boolean includeOwnMinions() {
            return true;
        }

        @Override
        protected MinionTribe tribeFilter() {
            return MinionTribe.MURLOC;
        }

        @Override
        protected boolean excludeSource() {
            return true;
        }
    };

    public ColdlightSeer() {
        super();
    }

    @Override
    public HearthTreeNode useUntargetableBattlecry_core(int minionPlacementIndex, HearthTreeNode boardState, boolean singleRealizationOnly) {
        return this.effectAllUsingFilter(this.getAoeEffect(), this.getAoeFilter(), boardState);
    }

    @Override
    public CardEffectCharacter getAoeEffect() {
        return ColdlightSeer.effect;
    }

    @Override
    public CharacterFilter getAoeFilter() {
        return ColdlightSeer.filter;
    }
}
