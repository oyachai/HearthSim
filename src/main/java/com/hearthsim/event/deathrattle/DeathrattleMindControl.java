package com.hearthsim.event.deathrattle;

import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.filter.FilterCharacterUntargetedDeathrattle;

public class DeathrattleMindControl extends DeathrattleEffectRandomMinion {

    public DeathrattleMindControl() {
        super(EffectCharacter.MIND_CONTROL, new FilterCharacterUntargetedDeathrattle(){
            @Override
            protected boolean includeEnemyMinions() {
                return true;
            }
        });
    }

    @Override
    public boolean equals(Object other) {
        if (!super.equals(other)) {
            return false;
        }

        if (!(other instanceof DeathrattleMindControl))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return 1;
    }
}
