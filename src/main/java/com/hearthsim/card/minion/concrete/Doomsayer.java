package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Doomsayer extends Minion {

    private static final CardEffectCharacter<Card> effect = CardEffectCharacter.DESTROY;

    private static final FilterCharacter filter = FilterCharacter.ALL_MINIONS;

    public Doomsayer() {
        super();
    }

    @Override
    public HearthTreeNode startTurn(PlayerSide thisMinionPlayerIndex, HearthTreeNode boardModel) throws HSException {
        HearthTreeNode toRet = boardModel;
        if (thisMinionPlayerIndex == PlayerSide.CURRENT_PLAYER) {
            toRet = this.effectAllUsingFilter(Doomsayer.effect, Doomsayer.filter, toRet);
        }
        return super.startTurn(thisMinionPlayerIndex, toRet);
    }
}
