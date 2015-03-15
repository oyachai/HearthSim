package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellDamageAoe;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Blizzard extends SpellDamageAoe {

    /*
     * Deal $2 damage to all enemy minions and <b>Freeze</b> them.
     */
    public Blizzard() {
        super();
    }

    @Override
    public HearthTreeNode attack(PlayerSide targetMinionPlayerSide, Minion targetMinion, HearthTreeNode boardState) throws HSException {
        targetMinion.setFrozen(true);
        return targetMinion.takeDamage(damage_, PlayerSide.CURRENT_PLAYER, targetMinionPlayerSide, boardState, true, false);
    }
}
