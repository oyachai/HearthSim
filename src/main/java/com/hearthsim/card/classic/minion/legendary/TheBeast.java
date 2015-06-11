package com.hearthsim.card.classic.minion.legendary;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.deathrattle.DeathrattleSummonMinionAction;
import com.hearthsim.model.PlayerSide;

/**
 * Created by oyachai on 6/7/15.
 */
public class TheBeast extends Minion {

    public TheBeast() {
        super();

        deathrattleAction_ = new DeathrattleSummonMinionAction(FinkleEinhorn.class, 1, PlayerSide.WAITING_PLAYER);
    }

}
