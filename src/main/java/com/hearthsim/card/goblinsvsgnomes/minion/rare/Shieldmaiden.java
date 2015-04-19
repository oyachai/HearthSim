package com.hearthsim.card.goblinsvsgnomes.minion.rare;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectHeroBuff;

public class Shieldmaiden extends Minion implements MinionBattlecryInterface {

    private static final EffectCharacter<Card> effect = new EffectHeroBuff<>(0, 5);

    public Shieldmaiden() {
        super();
    }

    /**
     * Battlecry: Deal 3 damage to your hero
     */
    @Override
    public EffectCharacter<Card> getBattlecryEffect() {
        return Shieldmaiden.effect;
    }
}
