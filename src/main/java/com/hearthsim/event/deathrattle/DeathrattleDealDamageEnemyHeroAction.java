package com.hearthsim.event.deathrattle;

import com.hearthsim.card.Card;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class DeathrattleDealDamageEnemyHeroAction extends DeathrattleAction {
    private final byte damage_;

    public DeathrattleDealDamageEnemyHeroAction(byte damage) {
        damage_ = damage;
    }

    @Override
    public HearthTreeNode performAction(Card origin,
                                        PlayerSide playerSide,
                                        HearthTreeNode boardState,
                                        boolean singleRealizationOnly) {
        HearthTreeNode toRet = super.performAction(origin, playerSide, boardState, singleRealizationOnly);
        if (toRet != null) {
            PlayerModel otherPlayer = toRet.data_.modelForSide(playerSide.getOtherPlayer());
            toRet = otherPlayer.getHero().takeDamageAndNotify(damage_, playerSide, playerSide, toRet, false, false);
        }
        return toRet;
    }
}
