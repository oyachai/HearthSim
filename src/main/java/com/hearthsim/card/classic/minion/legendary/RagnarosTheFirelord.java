package com.hearthsim.card.classic.minion.legendary;

import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

/**
 * Created by oyachai on 4/5/15.
 */
public class RagnarosTheFirelord extends Minion {

    public RagnarosTheFirelord() {
        super();
    }

    @Override
    public HearthTreeNode endTurn(PlayerSide thisMinionPlayerIndex, HearthTreeNode boardModel) throws HSException {
        if (thisMinionPlayerIndex == PlayerSide.CURRENT_PLAYER) {
            PlayerModel waitingPlayer = boardModel.data_.modelForSide(PlayerSide.WAITING_PLAYER);
            CharacterIndex targetIndex = CharacterIndex.fromInteger((int)(Math.random() * waitingPlayer.getNumCharacters()));
            Minion targetMinion = waitingPlayer.getCharacter(targetIndex);
            boardModel = targetMinion.takeDamageAndNotify((byte)8, PlayerSide.CURRENT_PLAYER, PlayerSide.WAITING_PLAYER, boardModel, false, false);
        }
        return super.endTurn(thisMinionPlayerIndex, boardModel);
    }

}
