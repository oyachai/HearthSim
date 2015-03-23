package com.hearthsim.event.deathrattle;

import com.hearthsim.event.EffectMinionAction;
import com.hearthsim.event.MinionFilterUntargetedDeathrattle;

public class DeathrattleMindControl extends DeathrattleEffectRandomMinion {

    public DeathrattleMindControl() {
        super(EffectMinionAction.MIND_CONTROL, new MinionFilterUntargetedDeathrattle(){
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
