package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedBattlecry;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectCharacterBuffDelta;

public class Houndmaster extends Minion implements MinionBattlecryInterface {

    /**
     * Battlecry: Give a friendly beast +2/+2 and Taunt
     */
    private final static FilterCharacterTargetedBattlecry filter = new FilterCharacterTargetedBattlecry() {
        protected boolean includeOwnMinions() {
            return true;
        }
        protected MinionTribe tribeFilter() {
            return MinionTribe.BEAST;
        }
    };

    private final static CardEffectCharacter battlecryAction = new CardEffectCharacterBuffDelta(2, 2, true);

    public Houndmaster() {
        super();
    }

    @Override
    public FilterCharacter getBattlecryFilter() {
        return Houndmaster.filter;
    }

    @Override
    public CardEffectCharacter getBattlecryEffect() {
        return Houndmaster.battlecryAction;
    }
}
