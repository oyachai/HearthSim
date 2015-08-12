package com.hearthsim.card.minion;

import com.hearthsim.card.CharacterIndex;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

@Deprecated
public interface MinionUntargetableBattlecry extends MinionBattlecryInterface {
    public HearthTreeNode useUntargetableBattlecry_core(CharacterIndex minionPlacementIndex, HearthTreeNode boardState);

    @Override
    public default EffectCharacter<Minion> getBattlecryEffect() {
        return (PlayerSide targetSide, CharacterIndex targetCharacterIndex, HearthTreeNode boardState) -> this.useUntargetableBattlecry_core(targetCharacterIndex, boardState);
    }
}
