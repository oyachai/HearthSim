package com.hearthsim.card.goblinsvsgnomes.weapon.epic;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.weapon.WeaponCard;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectOnResolveRandomCharacter;
import com.hearthsim.event.filter.FilterCharacter;

public class Coghammer extends WeaponCard implements EffectOnResolveRandomCharacter {
    private static final EffectCharacter effect = (targetSide, targetCharacterIndex, boardState) -> {
        Minion targetCharacter = boardState.data_.modelForSide(targetSide).getCharacter(targetCharacterIndex);
        targetCharacter.setDivineShield(true);
        targetCharacter.setTaunt(true);
        return boardState;
    };

    @Override
    public EffectCharacter getRandomTargetEffect() {
        return Coghammer.effect;
    }

    @Override
    public FilterCharacter getRandomTargetFilter() {
        return FilterCharacter.FRIENDLY_MINIONS;
    }
}
