package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellDamageTargetableCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Eviscerate extends SpellDamageTargetableCard {

    public Eviscerate() {
        super();
        this.damage_ = 2; // TODO: read this in from json file
    }

    @Deprecated
    public Eviscerate(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    @Override
    protected HearthTreeNode use_core(
        PlayerSide side,
        Minion targetMinion,
        HearthTreeNode boardState,
        boolean singleRealizationOnly)
        throws HSException {
        byte origDamage = damage_;
        if (boardState.data_.getCurrentPlayer().isComboEnabled()) {
            damage_ = 4;
        }
        HearthTreeNode toRet = super.use_core(side, targetMinion, boardState, singleRealizationOnly);
        damage_ = origDamage;
        return toRet;
    }
}
