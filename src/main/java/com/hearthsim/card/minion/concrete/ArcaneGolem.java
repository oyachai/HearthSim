package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.event.effect.CardEffectHero;
import com.hearthsim.event.effect.CardEffectHeroMana;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class ArcaneGolem extends Minion implements MinionUntargetableBattlecry {

    private final static CardEffectHero effect = new CardEffectHeroMana(1, 1);

    public ArcaneGolem() {
        super();
    }

    /**
     * Battlecry: Destroy your opponent's weapon
     */
    @Override
    public HearthTreeNode useUntargetableBattlecry_core(int minionPlacementIndex, HearthTreeNode boardState, boolean singleRealizationOnly) {
        return ArcaneGolem.effect.applyEffect(PlayerSide.CURRENT_PLAYER, this, PlayerSide.WAITING_PLAYER, boardState);
    }
}
