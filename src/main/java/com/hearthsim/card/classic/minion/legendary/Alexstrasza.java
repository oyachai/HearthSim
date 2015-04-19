package com.hearthsim.card.classic.minion.legendary;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterBuff;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedBattlecry;

public class Alexstrasza extends Minion implements MinionBattlecryInterface {

    /**
     * Battlecry: Set a hero's remaining health to 15
     */
    private final static FilterCharacterTargetedBattlecry filter = new FilterCharacterTargetedBattlecry() {
        protected boolean includeEnemyHero() {
            return true;
        }
        protected boolean includeOwnHero() {
            return true;
        }
    };

    private final static EffectCharacter battlecryAction = new EffectCharacterBuff(0, 15);

    public Alexstrasza() {
        super();
    }

    @Override
    public FilterCharacter getBattlecryFilter() {
        return Alexstrasza.filter;
    }

    @Override
    public EffectCharacter getBattlecryEffect() {
        return Alexstrasza.battlecryAction;
    }
}
