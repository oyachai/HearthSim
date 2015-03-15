package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.Minion.MinionTribe;
import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Demonheart extends SpellDamage {

    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public Demonheart() {
        super();

        this.canTargetEnemyHero = false;
        this.canTargetOwnHero = false;
    }

    /**
     * Attack using this spell
     *
     * @param targetMinionPlayerSide
     * @param targetMinion The target minion
     * @param boardState The BoardState before this card has performed its action. It will be manipulated and returned.
     * @return The boardState is manipulated and returned
     */
    @Override
    public HearthTreeNode attack(PlayerSide targetMinionPlayerSide, Minion targetMinion, HearthTreeNode boardState) throws HSException {
        HearthTreeNode toRet = boardState;
        if (isCurrentPlayer(targetMinionPlayerSide) && targetMinion.getTribe() == MinionTribe.DEMON) {
            targetMinion.setAttack((byte) (targetMinion.getAttack() + 5));
            targetMinion.setMaxHealth((byte) (targetMinion.getMaxHealth() + 5));
            targetMinion.setHealth((byte)(targetMinion.getHealth() + 5));
        } else {
            toRet = super.attack(targetMinionPlayerSide, targetMinion, boardState);
        }
        return toRet;
    }
}
