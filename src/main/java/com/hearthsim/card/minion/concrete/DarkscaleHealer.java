package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectCharacterHeal;
import com.hearthsim.util.tree.HearthTreeNode;

public class DarkscaleHealer extends Minion implements MinionUntargetableBattlecry {

    private static final CardEffectCharacter effect = new CardEffectCharacterHeal(2);

    private static final CharacterFilter filter = CharacterFilter.ALL_FRIENDLIES;

    public DarkscaleHealer() {
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
        return this.effectAllUsingFilter(DarkscaleHealer.effect, DarkscaleHealer.filter, boardState);
    }
}
