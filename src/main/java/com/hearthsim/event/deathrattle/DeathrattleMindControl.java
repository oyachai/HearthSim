package com.hearthsim.event.deathrattle;

import com.hearthsim.event.FilterUntargetedDeathrattle;
import com.hearthsim.event.EffectMinionMindControl;

public class DeathrattleMindControl extends DeathrattleEffectRandomMinion {

    public DeathrattleMindControl() {
        super(new EffectMinionMindControl(), new FilterUntargetedDeathrattle(){
            @Override
            protected boolean canEffectEnemyMinions() { return true; }
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
