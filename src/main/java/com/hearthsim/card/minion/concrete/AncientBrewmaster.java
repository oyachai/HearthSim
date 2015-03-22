package com.hearthsim.card.minion.concrete;

import java.util.EnumSet;

import com.hearthsim.card.minion.BattlecryTargetType;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionTargetableBattlecry;
import com.hearthsim.event.EffectMinionBounce;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class AncientBrewmaster extends Minion implements MinionTargetableBattlecry {

    protected final EffectMinionBounce effect = new EffectMinionBounce();

    public AncientBrewmaster() {
        super();
    }

    @Override
    public EnumSet<BattlecryTargetType> getBattlecryTargets() {
        return EnumSet.of(BattlecryTargetType.FRIENDLY_MINIONS);
    }

    /**
     * Battlecry: Change an enemy minion's attack to 1
     */
    @Override
    public HearthTreeNode useTargetableBattlecry_core(PlayerSide side, Minion targetMinion, HearthTreeNode boardState) throws HSException {
        if (boardState != null) {
            effect.applyEffect(PlayerSide.CURRENT_PLAYER, this, side, targetMinion, boardState.data_);
        }
        return boardState;
    }
}
