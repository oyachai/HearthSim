package com.hearthsim.card.blackrockmountain.spell;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellDamageTargetableCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.CardDrawNode;
import com.hearthsim.util.tree.HearthTreeNode;

public class QuickShot extends SpellDamageTargetableCard {

    public QuickShot() {
        super();
    }

    @Override
    protected HearthTreeNode use_core(PlayerSide side, Minion targetMinion,HearthTreeNode boardState) throws HSException {
        PlayerModel currentPlayer = boardState.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
        HearthTreeNode toRet = super.use_core(side, targetMinion, boardState);
        if (toRet != null && currentPlayer.getHand().size() == 0) {
            if (toRet instanceof CardDrawNode) {
                ((CardDrawNode) toRet).addNumCardsToDraw(1);
            } else {
                toRet = new CardDrawNode(toRet, 1);
            }
        }
        return toRet;
    }
}
