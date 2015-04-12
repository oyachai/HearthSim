package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.CharacterFilterInterface;
import com.hearthsim.event.CharacterFilterTargetedBattlecry;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectCharacterBuff;

public class AldorPeacekeeper extends Minion implements MinionBattlecryInterface {

    /**
     * Battlecry: Change an enemy minion's attack to 1
     */
    private final static CharacterFilterTargetedBattlecry filter = new CharacterFilterTargetedBattlecry() {
        protected boolean includeEnemyMinions() {
            return true;
        }
    };

    private final static CardEffectCharacter<Minion> battlecryAction = new CardEffectCharacterBuff<>(1, 0);

    public AldorPeacekeeper() {
        super();
    }

    @Override
    public CharacterFilterInterface getBattlecryFilter() {
        return AldorPeacekeeper.filter;
    }

    @Override
    public CardEffectCharacter<Minion> getBattlecryEffect() {
        return AldorPeacekeeper.battlecryAction;
    }
}
