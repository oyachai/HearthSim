package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Lightwell extends Minion {

    public Lightwell() {
        super();
    }

    @Override
    public HearthTreeNode startTurn(PlayerSide side, HearthTreeNode boardModel, Deck deckPlayer0, Deck deckPlayer1) throws HSException {
        HearthTreeNode toRet = boardModel;
        if (side == PlayerSide.CURRENT_PLAYER) {
            PlayerModel currentPlayer = toRet.data_.modelForSide(PlayerSide.CURRENT_PLAYER);

            //check to see if anyone is damaged
            boolean isDamaged = currentPlayer.getHero().getTotalMaxHealth() > currentPlayer.getHero().getTotalHealth();
            for (Minion minion : currentPlayer.getMinions()) {
                isDamaged = isDamaged || minion.getTotalMaxHealth() > minion.getTotalHealth();
            }
            if (!isDamaged)
                return super.startTurn(side, toRet, deckPlayer0, deckPlayer1);

            Minion targetMinion = currentPlayer.getCharacter((int) (Math.random() * (currentPlayer.getNumMinions() + 1)));
            while (targetMinion.getTotalMaxHealth() == targetMinion.getTotalHealth()) {
                targetMinion = currentPlayer.getCharacter((int)(Math.random()*(currentPlayer.getNumMinions() + 1)));
            }
            toRet = targetMinion.takeHeal((byte)3, PlayerSide.CURRENT_PLAYER, toRet, deckPlayer0, deckPlayer1);
        }
        return super.startTurn(side, toRet, deckPlayer0, deckPlayer1);
    }

}
