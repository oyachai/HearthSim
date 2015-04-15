package com.hearthsim.card.minion;

import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

@Deprecated
public interface MinionUntargetableBattlecry extends MinionBattlecryInterface {
    public HearthTreeNode useUntargetableBattlecry_core(int minionPlacementIndex, HearthTreeNode boardState, boolean singleRealizationOnly);

    @Override
    public default EffectCharacter<Minion> getBattlecryEffect() {
        return (PlayerSide originSide, Minion origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) -> this.useUntargetableBattlecry_core(targetCharacterIndex, boardState, false);
    }
}
