package com.hearthsim.card.weapon.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.weapon.WeaponCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class TruesilverChampion extends WeaponCard {

    @Override
    public void onAttack(PlayerSide targetMinionPlayerSide, Minion targetMinion, HearthTreeNode toRet, Deck deckPlayer0, Deck deckPlayer1) throws HSException {
        super.onAttack(targetMinionPlayerSide, targetMinion, toRet, deckPlayer0, deckPlayer1);

        BoardModel boardModel = toRet.data_;
        PlayerModel currentPlayer = boardModel.getCurrentPlayer();
        currentPlayer.getHero().takeHeal((byte) 2, targetMinionPlayerSide, toRet, deckPlayer0, deckPlayer1);
    }
}
