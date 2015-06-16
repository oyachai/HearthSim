package com.hearthsim.card.classic.minion.epic;

import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.CardDrawNode;
import com.hearthsim.util.tree.HearthTreeNode;

public class AncientOfLore extends Minion {

    private static final byte HEAL_AMOUNT = 5;

    public AncientOfLore() {
        super();
    }

    /**
     *
     * Choose one: Draw 2 cards; or Restore 5 health
     *
     *
     *
     * @param side
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     *
     * @return The boardState is manipulated and returned
     */
    @Override
    public HearthTreeNode use_core(
            PlayerSide side,
            Minion targetMinion,
            HearthTreeNode boardState)
        throws HSException {
        HearthTreeNode toRet = super.use_core(side, targetMinion, boardState);

        if (toRet != null) {
            PlayerModel currentPlayer = boardState.data_.modelForSide(PlayerSide.CURRENT_PLAYER);

            CharacterIndex thisMinionIndex = currentPlayer.getIndexForCharacter(this);
            toRet.addChild(new CardDrawNode(new HearthTreeNode(toRet.data_.deepCopy()), 2));

            HearthTreeNode newState;
            for (CharacterIndex.CharacterLocation location : toRet.data_) {
                if (location.getPlayerSide() == side && location.getIndex() == thisMinionIndex) {
                    continue;
                }

                newState = new HearthTreeNode(toRet.data_.deepCopy());
                newState = newState.data_.getCharacter(location).takeHealAndNotify(HEAL_AMOUNT, location.getPlayerSide(), newState);
                toRet.addChild(newState);
            }
        }
        return toRet;
    }
}
