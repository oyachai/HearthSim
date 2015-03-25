package com.hearthsim.card.spellcard;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.MinionFilter;
import com.hearthsim.event.MinionFilterTargetedSpell;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class SpellDamageAoe extends SpellDamage {

    protected MinionFilter hitsFilter;

    public SpellDamageAoe() {
        super();
        this.hitsFilter = MinionFilter.ENEMY_MINIONS;
    }

    /**
     * Constructor
     *
     * @param hasBeenUsed Whether the card has already been used or not
     */
    @Deprecated
    public SpellDamageAoe(byte mana, byte damage, boolean hasBeenUsed) {
        super(mana, damage, hasBeenUsed);
    }

    @Override
    public boolean canBeUsedOn(PlayerSide playerSide, Minion minion, BoardModel boardModel) {
        if (!super.canBeUsedOn(playerSide, minion, boardModel)) {
            return false;
        }

        if (isCurrentPlayer(playerSide)) {
            return false;
        }

        if (isNotHero(minion)) {
            return false;
        }

        return true;
    }

    /**
     * Use the card on the given target
     *
     * @param side
     * @param boardState The BoardState before this card has performed its action. It will be manipulated and returned.
     * @return The boardState is manipulated and returned
     */
    @Override
    protected HearthTreeNode use_core(PlayerSide side, Minion targetMinion, HearthTreeNode boardState, boolean singleRealizationOnly) throws HSException {
        boardState = this.attackAllUsingFilter(this.hitsFilter, boardState);

        if (boardState != null) {
            PlayerModel currentPlayer = boardState.data_.getCurrentPlayer();
            currentPlayer.subtractMana(this.getManaCost(side, boardState.data_));
            currentPlayer.getHand().remove(this);
        }

        return boardState;
    }
}
