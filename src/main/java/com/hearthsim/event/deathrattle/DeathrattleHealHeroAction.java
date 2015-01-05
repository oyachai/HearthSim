package com.hearthsim.event.deathrattle;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class DeathrattleHealHeroAction extends DeathrattleAction {

    private final byte amount_;
    private final boolean targetEnemyHero_;

    public DeathrattleHealHeroAction(byte amount, boolean targetEnemyHero) {
        amount_ = amount;
        targetEnemyHero_ = targetEnemyHero;
    }

    @Override
    public HearthTreeNode performAction(
        Card origin,
        PlayerSide playerSide,
        HearthTreeNode boardState,
        Deck deckPlayer0,
        Deck deckPlayer1)
        throws HSException {
        HearthTreeNode toRet = super.performAction(origin, playerSide, boardState, deckPlayer0, deckPlayer1);
        if (toRet != null) {
            PlayerSide targetSide = targetEnemyHero_ ? playerSide.getOtherPlayer() : playerSide;
            toRet = toRet.data_.getHero(targetSide).takeHeal(amount_, targetSide, toRet, deckPlayer0, deckPlayer1);
        }
        return toRet;
    }

}
