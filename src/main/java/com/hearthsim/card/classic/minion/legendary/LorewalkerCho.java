package com.hearthsim.card.classic.minion.legendary;

import com.hearthsim.card.Card;
import com.hearthsim.card.CardPlayBeginInterface;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class LorewalkerCho extends Minion implements CardPlayBeginInterface {

    public LorewalkerCho() {
        super();
    }

    @Override
    public HearthTreeNode onCardPlayBegin(PlayerSide thisCardPlayerSide, PlayerSide cardUserPlayerSide, Card usedCard, HearthTreeNode boardState) {
        if (!this.setInHand() && usedCard instanceof SpellCard) {
            PlayerModel targetPlayer = boardState.data_.modelForSide(cardUserPlayerSide.getOtherPlayer());
            if (!targetPlayer.isHandFull()) {
                Card copy = usedCard.createResetCopy();
                targetPlayer.getHand().add(copy);
            }
            return boardState;
        }

        return boardState;
    }
}
