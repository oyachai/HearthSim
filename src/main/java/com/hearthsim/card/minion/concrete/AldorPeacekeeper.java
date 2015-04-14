package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.filter.FilterCharacterInterface;
import com.hearthsim.event.filter.FilterCharacterTargetedBattlecry;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectCharacterBuff;

public class AldorPeacekeeper extends Minion implements MinionBattlecryInterface {

    /**
     * Battlecry: Change an enemy minion's attack to 1
     */
    private final static FilterCharacterTargetedBattlecry filter = new FilterCharacterTargetedBattlecry() {
        protected boolean includeEnemyMinions() {
            return true;
        }
    };

    private final static CardEffectCharacter<Minion> battlecryAction = new CardEffectCharacterBuff<>(1, 0);

    public AldorPeacekeeper() {
        super();
    }

    @Override
    public FilterCharacterInterface getBattlecryFilter() {
        return AldorPeacekeeper.filter;
    }

    @Override
    public CardEffectCharacter<Minion> getBattlecryEffect() {
        return AldorPeacekeeper.battlecryAction;
    }
}
