package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.effect.CardEffectAoeInterface;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Doomsayer extends Minion implements CardEffectAoeInterface {

    private static final CardEffectCharacter effect = CardEffectCharacter.DESTROY;

    private static final CharacterFilter filter = CharacterFilter.ALL_MINIONS;

    public Doomsayer() {
        super();
    }

    @Override
    public HearthTreeNode startTurn(PlayerSide thisMinionPlayerIndex, HearthTreeNode boardModel) throws HSException {
        HearthTreeNode toRet = boardModel;
        if (thisMinionPlayerIndex == PlayerSide.CURRENT_PLAYER) {
            toRet = this.effectAllUsingFilter(this.getAoeEffect(), this.getAoeFilter(), toRet);
        }
        return super.startTurn(thisMinionPlayerIndex, toRet);
    }

    @Override
    public CardEffectCharacter getAoeEffect() {
        return Doomsayer.effect;
    }

    @Override
    public CharacterFilter getAoeFilter() {
        return Doomsayer.filter;
    }
}
