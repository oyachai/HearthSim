package com.hearthsim.card.basic.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectHero;
import com.hearthsim.event.effect.EffectHeroWeaponDestroy;
import com.hearthsim.event.filter.FilterCharacter;

public class AcidicSwampOoze extends Minion implements MinionBattlecryInterface {

    private static final EffectHero<Card> effect = EffectHeroWeaponDestroy.DESTROY;

    /**
     * Battlecry: Destroy your opponent's weapon
     */
    public AcidicSwampOoze() {
        super();
    }

    @Override
    public FilterCharacter getBattlecryFilter() {
        return FilterCharacter.OPPONENT;
    }

    @Override
    public EffectCharacter<Card> getBattlecryEffect() {
        return AcidicSwampOoze.effect;
    }
}
