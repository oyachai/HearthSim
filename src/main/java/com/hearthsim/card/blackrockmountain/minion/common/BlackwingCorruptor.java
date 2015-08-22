package com.hearthsim.card.blackrockmountain.minion.common;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterDamage;
import com.hearthsim.event.effect.conditional.Conditional;
import com.hearthsim.event.effect.conditional.EffectCharacterConditional;
import com.hearthsim.event.filter.FilterCharacterInterface;
import com.hearthsim.event.filter.FilterCharacterTargetedBattlecry;
import com.hearthsim.model.PlayerSide;

public class BlackwingCorruptor extends Minion implements MinionBattlecryInterface {

    private final static FilterCharacterTargetedBattlecry filter = FilterCharacterTargetedBattlecry.ALL;

    private final static EffectCharacter<Minion> battlecryAction = new EffectCharacterConditional<>(
        new EffectCharacterDamage<>(3),
        Conditional.HOLDING_DRAGON,
        PlayerSide.CURRENT_PLAYER
    );

    public BlackwingCorruptor() {
        super();
    }

    @Override
    public FilterCharacterInterface getBattlecryFilter() {
        return BlackwingCorruptor.filter;
    }

    @Override
    public EffectCharacter<Minion> getBattlecryEffect() {
        return BlackwingCorruptor.battlecryAction;
    }
}
