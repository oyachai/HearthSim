package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectHero;
import com.hearthsim.event.effect.CardEffectHeroWeaponDestroy;

public class BloodsailCorsair extends Minion implements MinionBattlecryInterface {

    private static final CardEffectHero effect = new CardEffectHeroWeaponDestroy(1);

    /**
     * Battlecry: Remove 1 Durability from your opponent's weapon.
     */
    public BloodsailCorsair() {
        super();
    }

    @Override
    public FilterCharacter getBattlecryFilter() {
        return FilterCharacter.OPPONENT;
    }

    @Override
    public CardEffectCharacter getBattlecryEffect() {
        return BloodsailCorsair.effect;
    }
}
