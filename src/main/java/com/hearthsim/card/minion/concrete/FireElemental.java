package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedBattlecry;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectCharacterDamage;

public class FireElemental extends Minion implements MinionBattlecryInterface {

    /**
     * Battlecry: Deal 3 damage to a chosen target
     */
    private final static FilterCharacterTargetedBattlecry filter = new FilterCharacterTargetedBattlecry() {
        protected boolean includeEnemyHero() {
            return true;
        }
        protected boolean includeEnemyMinions() {
            return true;
        }
        protected boolean includeOwnHero() {
            return true;
        }
        protected boolean includeOwnMinions() {
            return true;
        }
    };

    private final static CardEffectCharacter battlecryAction = new CardEffectCharacterDamage(3);

    public FireElemental() {
        super();
    }

    @Override
    public FilterCharacter getBattlecryFilter() {
        return FireElemental.filter;
    }

    @Override
    public CardEffectCharacter getBattlecryEffect() {
        return FireElemental.battlecryAction;
    }
}
