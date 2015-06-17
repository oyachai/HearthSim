package com.hearthsim.card.basic.spell;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.Minion.MinionTribe;
import com.hearthsim.card.spellcard.SpellDamageTargetableCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class KillCommand extends SpellDamageTargetableCard {

    public KillCommand() {
        super();
        this.damage_ = 3;
    }

    @Deprecated
    public KillCommand(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    /**
     *
     * Use the card on the given target
     *
     * Deals 3 damage.  If you have a beast, deals 5 damage.
     *
     *
     *
     * @param side
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     *
     * @return The boardState is manipulated and returned
     */
    @Override
    protected HearthTreeNode use_core(
            PlayerSide side,
            Minion targetMinion,
            HearthTreeNode boardState)
        throws HSException {
        boolean haveBeast = false;
        PlayerModel currentPlayer = boardState.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
        for (final Minion minion : currentPlayer.getMinions()) {
            haveBeast = haveBeast || minion.getTribe() == MinionTribe.BEAST;
        }
        if (haveBeast)
            this.damage_ = (byte)5;
        else
            this.damage_ = (byte)3;

        return super.use_core(side, targetMinion, boardState);
    }
}
