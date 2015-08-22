package com.hearthsim.card.blackrockmountain.minion.legendary;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.conditional.Conditional;
import com.hearthsim.event.effect.conditional.EffectCharacterConditional;
import com.hearthsim.event.filter.FilterCharacterInterface;
import com.hearthsim.event.filter.FilterCharacterTargetedBattlecry;
import com.hearthsim.model.PlayerSide;

public class RendBlackhand extends Minion implements MinionBattlecryInterface {

    private final static FilterCharacterTargetedBattlecry filter = FilterCharacterTargetedBattlecry.ALL_LEGENDARY_MINIONS;

    private final static EffectCharacter<Minion> battlecryAction = new EffectCharacterConditional<Minion>(
        EffectCharacter.DESTROY,
        Conditional.HOLDING_DRAGON,
        PlayerSide.CURRENT_PLAYER
    );

    public RendBlackhand() {
        super();
    }

    @Override
    public FilterCharacterInterface getBattlecryFilter() {
        return RendBlackhand.filter;
    }

    @Override
    public EffectCharacter<Minion> getBattlecryEffect() {
        return RendBlackhand.battlecryAction;
    }
}
