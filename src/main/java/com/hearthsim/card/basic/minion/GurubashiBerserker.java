package com.hearthsim.card.basic.minion;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public class GurubashiBerserker extends Minion {

    public GurubashiBerserker() {
        super();
    }

    public byte takeDamage(byte damage, PlayerSide originSide, PlayerSide thisPlayerSide, BoardModel board, boolean isSpellDamage) {
        byte actualDamage = super.takeDamage(damage, originSide, thisPlayerSide, board, isSpellDamage);
        if (actualDamage > 0 && !this.isSilenced()) {
            this.attack_ = (byte)(this.attack_ + 3);
        }
        return actualDamage;
    }
}
