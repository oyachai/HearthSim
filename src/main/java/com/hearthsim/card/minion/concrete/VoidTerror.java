package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.MinionList;
import com.hearthsim.util.tree.HearthTreeNode;

public class VoidTerror extends Minion implements MinionUntargetableBattlecry {
    private static final boolean HERO_TARGETABLE = true;
    private static final byte SPELL_DAMAGE = 0;

    public VoidTerror() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;
    }

    /**
     * Battlecry: Destroy the minions on either side of this minion and gain their Attack and Health.
     */
    @Override
    public HearthTreeNode useUntargetableBattlecry_core(
            Minion minionPlacementTarget,
            HearthTreeNode boardState,
            Deck deckPlayer0,
            Deck deckPlayer1,
            boolean singleRealizationOnly
        ) throws HSException {
        HearthTreeNode toRet = boardState;
        MinionList minions = boardState.data_.getMinions(PlayerSide.CURRENT_PLAYER);
        int thisMinionIndex = minions.indexOf(this);
        if (thisMinionIndex > 0) {
            Minion minionToDestroy = minions.get(thisMinionIndex - 1);
            this.addAttack(minionToDestroy.getTotalAttack());
            this.addHealth(minionToDestroy.getTotalHealth());
            this.addMaxHealth(minionToDestroy.getTotalHealth());
            minionToDestroy.setHealth((byte)-99);
        }
        if (thisMinionIndex < minions.size() - 1) {
            Minion minionToDestroy = minions.get(thisMinionIndex + 1);
            this.addAttack(minionToDestroy.getTotalAttack());
            this.addHealth(minionToDestroy.getTotalHealth());
            this.addMaxHealth(minionToDestroy.getTotalHealth());
            minionToDestroy.setHealth((byte)-99);
        }
        return toRet;
    }

}
