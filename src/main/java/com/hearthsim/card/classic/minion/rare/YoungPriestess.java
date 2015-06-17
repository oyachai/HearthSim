package com.hearthsim.card.classic.minion.rare;

import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class YoungPriestess extends Minion {

    public YoungPriestess() {
        super();
    }

    /**
     * At the end of your turn, give another random friendly minion +1 Health
     */
    @Override
    public HearthTreeNode endTurn(PlayerSide thisMinionPlayerIndex, HearthTreeNode boardModel) throws HSException {
        HearthTreeNode toRet = super.endTurn(thisMinionPlayerIndex, boardModel);
        PlayerModel currentPlayer = toRet.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
        if (thisMinionPlayerIndex == PlayerSide.CURRENT_PLAYER) {
            int numFriendlyMinions = currentPlayer.getNumMinions();
            if (numFriendlyMinions > 1) {
                int minionToBuffIndex = (int)(Math.random() * numFriendlyMinions);
                Minion minionToBuff = currentPlayer.getCharacter(CharacterIndex.fromInteger(minionToBuffIndex + 1));
                while (minionToBuff == this) {
                    minionToBuffIndex = (int)(Math.random() * numFriendlyMinions);
                    minionToBuff = currentPlayer.getCharacter(CharacterIndex.fromInteger(minionToBuffIndex + 1));
                }
                minionToBuff.addHealth((byte) 1);
                minionToBuff.addMaxHealth((byte)1);
            }
        }
        return toRet;
    }
}
