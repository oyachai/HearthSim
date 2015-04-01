package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectCharacterDraw;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class AzureDrake extends Minion implements MinionUntargetableBattlecry {

    private static final CardEffectCharacter effect = new CardEffectCharacterDraw(1);

    public AzureDrake() {
        super();
    }

    /**
     * Battlecry: Draw one card
     */
    @Override
    public HearthTreeNode useUntargetableBattlecry_core(int minionPlacementIndex, HearthTreeNode boardState, boolean singleRealizationOnly) {
        return AzureDrake.effect.applyEffect(PlayerSide.CURRENT_PLAYER, this, PlayerSide.CURRENT_PLAYER, this, boardState);
    }
}
