package com.hearthsim.event.deathrattle;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.exception.HSException;
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
                                        Deck deckPlayer0,
                                        Deck deckPlayer1,
                                        boolean singleRealizationOnly) throws HSException {
        HearthTreeNode toRet = super.performAction(origin, playerSide, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);
        if (toRet != null) {
            PlayerModel otherPlayer = toRet.data_.modelForSide(playerSide.getOtherPlayer());
            toRet = otherPlayer.getHero().takeDamage(damage_, playerSide, playerSide, toRet, deckPlayer0, deckPlayer1, false, false);
        }
        return toRet;
    }
}
