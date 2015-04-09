package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.event.effect.CardEffectHero;
import com.hearthsim.event.effect.CardEffectHeroWeaponDestroy;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class BloodsailCorsair extends Minion implements MinionUntargetableBattlecry {

    private static final CardEffectHero effect = new CardEffectHeroWeaponDestroy(1);

    /**
     * Battlecry: Remove 1 Durability from your opponent's weapon.
     */
    public BloodsailCorsair() {
        super();
    }

    @Override
    public HearthTreeNode useUntargetableBattlecry_core(int minionPlacementIndex, HearthTreeNode boardState, boolean singleRealizationOnly) {
        return effect.applyEffect(PlayerSide.CURRENT_PLAYER, this, PlayerSide.WAITING_PLAYER, boardState);
    }
}
