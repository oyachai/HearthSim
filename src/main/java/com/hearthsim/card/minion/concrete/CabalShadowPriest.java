package com.hearthsim.card.minion.concrete;

import java.util.EnumSet;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.BattlecryTargetType;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionTargetableBattlecry;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class CabalShadowPriest extends Minion implements MinionTargetableBattlecry {

    private static final boolean HERO_TARGETABLE = true;
    private static final byte SPELL_DAMAGE = 0;

    public CabalShadowPriest() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;
}

    @Override
    public EnumSet<BattlecryTargetType> getBattlecryTargets() {
        return EnumSet.of(BattlecryTargetType.ENEMY_MINIONS);
    }

    @Override
    public HearthTreeNode useTargetableBattlecry_core(
            PlayerSide side,
            Minion targetMinion,
            HearthTreeNode boardState,
            Deck deckPlayer0,
            Deck deckPlayer1
        ) throws HSException {
        HearthTreeNode toRet = boardState;
        if (targetMinion.getTotalAttack() <= 2 && toRet.data_.getMinions(PlayerSide.CURRENT_PLAYER).size() < 6) {
            toRet.data_.removeMinion(targetMinion);
            toRet.data_.placeMinion(PlayerSide.CURRENT_PLAYER, targetMinion);
            if (targetMinion.getCharge()) {
                if (!targetMinion.canAttack()) {
                    targetMinion.hasAttacked(false);
                }
            } else {
                targetMinion.hasAttacked(true);
            }
            return boardState;
        } else {
            return null;
        }
    }
}
