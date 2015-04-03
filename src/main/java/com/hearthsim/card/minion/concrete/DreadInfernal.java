package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectCharacterDamage;
import com.hearthsim.util.tree.HearthTreeNode;

public class DreadInfernal extends Minion implements MinionUntargetableBattlecry {

    private static final CardEffectCharacter effect = new CardEffectCharacterDamage(1);

    private static final CharacterFilter filter = new CharacterFilter() {
        @Override
        protected boolean includeEnemyHero() {
            return true;
        }

        @Override
        protected boolean includeEnemyMinions() {
            return true;
        }

        @Override
        protected boolean includeOwnHero() {
            return true;
        }

        @Override
        protected boolean includeOwnMinions() {
            return true;
        }

        @Override
        protected boolean excludeSource() {
            return true;
        }
    };

    public DreadInfernal() {
        super();
    }

    /**
     * Battlecry: Deals 1 damage to all characters
     */
    @Override
    public HearthTreeNode useUntargetableBattlecry_core(int minionPlacementIndex, HearthTreeNode boardState, boolean singleRealizationOnly) {
        return this.effectAllUsingFilter(DreadInfernal.effect, DreadInfernal.filter, boardState);
    }
}
