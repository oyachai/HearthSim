package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class GurubashiBerserker extends Minion {

    public GurubashiBerserker() {
        super();
    }

    /**
     * Called when this minion takes damage
     *
     * Override for special ability: gain +3 attack whenever this minion takes damage
     *  @param damage The amount of damage to take
     * @param attackPlayerSide The player index of the attacker.  This is needed to do things like +spell damage.
     * @param thisPlayerSide
     * @param boardState
     * @param isSpellDamage True if this is a spell damage   @throws HSInvalidPlayerIndexException
     * */
    @Override
    public HearthTreeNode takeDamage(
            byte damage,
            PlayerSide attackPlayerSide,
            PlayerSide thisPlayerSide,
            HearthTreeNode boardState,
            boolean isSpellDamage,
            boolean handleMinionDeath)
        throws HSException {
        if (!divineShield_) {
            HearthTreeNode toRet = super.takeDamage(damage, attackPlayerSide, thisPlayerSide, boardState, isSpellDamage, handleMinionDeath);
            if (!silenced_)
                this.attack_ = (byte)(this.attack_ + 3);
            return toRet;
        } else {
            divineShield_ = false;
            return boardState;
        }
    }
}
