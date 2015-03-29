package com.hearthsim.event.deathrattle;

import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.CharacterFilterUntargetedDeathrattle;

public class DeathrattleMindControl extends DeathrattleEffectRandomMinion {

    public DeathrattleMindControl() {
        super(CardEffectCharacter.MIND_CONTROL, new CharacterFilterUntargetedDeathrattle(){
            @Override
            protected boolean includeEnemyMinions() { return true; }
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
