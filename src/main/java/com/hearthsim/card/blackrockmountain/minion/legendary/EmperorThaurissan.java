package com.hearthsim.card.blackrockmountain.minion.legendary;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.effect.EffectHandManaCost;
import com.hearthsim.event.effect.SimpleEffectHand;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class EmperorThaurissan extends Minion {

    private static final SimpleEffectHand effect = new EffectHandManaCost(-1);

    public EmperorThaurissan() {
        super();
    }

    @Override
    public HearthTreeNode endTurn(PlayerSide thisMinionPlayerIndex, HearthTreeNode boardModel) throws HSException {
        HearthTreeNode tmpState = super.endTurn(thisMinionPlayerIndex, boardModel);
        if (isWaitingPlayer(thisMinionPlayerIndex))
            return tmpState;

        PlayerModel currentPlayer = boardModel.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
        for (Card card : currentPlayer.getHand()) {
            effect.applyEffect(PlayerSide.CURRENT_PLAYER, this, PlayerSide.CURRENT_PLAYER, card, boardModel.data_);
        }

        return boardModel;
    }

}
