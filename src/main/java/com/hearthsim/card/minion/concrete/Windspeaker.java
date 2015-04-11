package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.CharacterFilterTargetedBattlecry;
import com.hearthsim.event.effect.CardEffectCharacter;

public class Windspeaker extends Minion implements MinionBattlecryInterface {

    /**
     * Battlecry: Give a friendly minion +3 Health
     */
    private final static CharacterFilterTargetedBattlecry filter = new CharacterFilterTargetedBattlecry() {
        protected boolean includeOwnMinions() { return true; }
    };

    private final static CardEffectCharacter battlecryAction = (originSide, origin, targetSide, targetCharacterIndex, boardState) -> {
        Minion targetMinion = boardState.data_.modelForSide(targetSide).getCharacter(targetCharacterIndex);
        targetMinion.setWindfury(true);
        return boardState;
    };

    public Windspeaker() {
        super();
    }

    @Override
    public CharacterFilter getBattlecryFilter() {
        return Windspeaker.filter;
    }

    @Override
    public CardEffectCharacter getBattlecryEffect() {
        return Windspeaker.battlecryAction;
    }
}
