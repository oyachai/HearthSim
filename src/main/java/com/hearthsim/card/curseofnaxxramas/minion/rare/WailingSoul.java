package com.hearthsim.card.curseofnaxxramas.minion.rare;

import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.util.tree.HearthTreeNode;

public class WailingSoul extends Minion implements MinionUntargetableBattlecry {

    private static final EffectCharacter effect = EffectCharacter.SILENCE;

    private static final FilterCharacter filter = new FilterCharacter() {

        @Override
        protected boolean includeOwnMinions() {
            return true;
        }

        @Override
        protected boolean excludeSource() {
            return true;
        }
    };

    public WailingSoul() {
        super();
    }

    @Override
    public HearthTreeNode useUntargetableBattlecry_core(
        CharacterIndex minionPlacementIndex,
        HearthTreeNode boardState
    ) {
        return this.effectAllUsingFilter(WailingSoul.effect, WailingSoul.filter, boardState);
    }
}
