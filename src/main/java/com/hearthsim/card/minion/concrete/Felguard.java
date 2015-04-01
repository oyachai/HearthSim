package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.event.effect.CardEffectHero;
import com.hearthsim.event.effect.CardEffectHeroMana;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Felguard extends Minion implements MinionUntargetableBattlecry {

    private final static CardEffectHero effect = new CardEffectHeroMana(0, -1);

    public Felguard() {
        super();
    }

    /**
     * Taunt.  Battlecry: Destroy one of your Mana Crystals
     */
    @Override
    public HearthTreeNode useUntargetableBattlecry_core(int minionPlacementIndex, HearthTreeNode boardState, boolean singleRealizationOnly) {
        return Felguard.effect.applyEffect(PlayerSide.CURRENT_PLAYER, this, PlayerSide.CURRENT_PLAYER, boardState);
    }
}
