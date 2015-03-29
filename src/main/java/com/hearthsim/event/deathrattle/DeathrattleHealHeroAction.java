package com.hearthsim.event.deathrattle;

import com.hearthsim.card.Card;
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
    public HearthTreeNode performAction(Card origin,
                                        PlayerSide playerSide,
                                        HearthTreeNode boardState,
                                        boolean singleRealizationOnly) {
        HearthTreeNode toRet = super.performAction(origin, playerSide, boardState, singleRealizationOnly);
        if (toRet != null) {
            PlayerSide targetSide = targetEnemyHero_ ? playerSide.getOtherPlayer() : playerSide;
            toRet = toRet.data_.modelForSide(targetSide).getHero().takeHealAndNotify(amount_, targetSide, toRet);
        }
        return toRet;
    }
}
