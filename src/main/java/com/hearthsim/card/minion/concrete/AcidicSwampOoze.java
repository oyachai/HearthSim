package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.event.deathrattle.DeathrattleAction;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectHero;
import com.hearthsim.event.effect.CardEffectHeroWeaponDestroy;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class AcidicSwampOoze extends Minion implements MinionUntargetableBattlecry {

    private static final CardEffectHero effect = CardEffectHeroWeaponDestroy.DESTROY;

    public AcidicSwampOoze() {
        super();
    }

    /**
     * Battlecry: Destroy your opponent's weapon
     */
    @Override
    public HearthTreeNode useUntargetableBattlecry_core(int minionPlacementIndex, HearthTreeNode boardState, boolean singleRealizationOnly) {
        return effect.applyEffect(PlayerSide.CURRENT_PLAYER, this, PlayerSide.WAITING_PLAYER, boardState);
    }
}
