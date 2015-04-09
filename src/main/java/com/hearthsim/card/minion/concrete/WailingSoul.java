package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.util.tree.HearthTreeNode;

public class WailingSoul extends Minion implements MinionUntargetableBattlecry {

    private static final CardEffectCharacter effect = CardEffectCharacter.SILENCE;

    private static final CharacterFilter filter = new CharacterFilter() {

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

    /**
     * Battlecry: Heals friendly characters for 2
     */
    @Override
    public HearthTreeNode useUntargetableBattlecry_core(
        int minionPlacementIndex,
        HearthTreeNode boardState,
        boolean singleRealizationOnly
    ) {
        return this.effectAllUsingFilter(WailingSoul.effect, WailingSoul.filter, boardState);
    }
}
