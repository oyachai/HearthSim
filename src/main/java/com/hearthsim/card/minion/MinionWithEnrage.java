package com.hearthsim.card.minion;

import com.hearthsim.exception.HSException;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public abstract class MinionWithEnrage extends Minion {

    protected boolean enraged_;

    protected MinionWithEnrage() {
        super();
    }

    @Override
    public Minion deepCopy() {
        MinionWithEnrage minionWithEnrage = (MinionWithEnrage) super.deepCopy();
        minionWithEnrage.enraged_ = enraged_;
        return  minionWithEnrage;
    }

    public boolean isEnraged() {
        return this.enraged_;
    }

    /**
     * Turn on enrage
     *
     */
    public abstract void enrage();

    /**
     * Turn off enrage
     */
    public abstract void pacify();


    @Override
    public byte takeDamage(byte damage, PlayerSide originSide, PlayerSide thisPlayerSide, BoardModel board, boolean isSpellDamage) {
        byte actualDamage = super.takeDamage(damage, originSide, thisPlayerSide, board, isSpellDamage);
        if(actualDamage > 0) {
            this.enrageCheck();
        }
        return actualDamage;
    }

    /**
     * Called when this minion is healed
     *
     * Always use this function to heal minions
     *  @param healAmount The amount of healing to take
     * @param thisPlayerSide
     * @param boardState
     * */
    @Override
    public HearthTreeNode takeHealAndNotify(byte healAmount, PlayerSide thisPlayerSide, HearthTreeNode boardState) throws HSException {
        HearthTreeNode toRet = super.takeHealAndNotify(healAmount, thisPlayerSide, boardState);
        this.enrageCheck();
        return toRet;
    }

    /**
     * Called when this minion is silenced
     *
     * Always use this function to "silence" minions
     *
     *
     *
     * @param thisPlayerSide
     * @param boardState
     * @throws HSInvalidPlayerIndexException
     */
    @Override
    public void silenced(PlayerSide thisPlayerSide, BoardModel boardState) throws HSInvalidPlayerIndexException {
        super.silenced(thisPlayerSide, boardState);
        if (enraged_)
            this.pacify();
    }

    private void enrageCheck() {
        if (!silenced_) {
            if (health_ < maxHealth_ && !enraged_) {
                enraged_ = true;
                this.enrage();
            } else if (health_ == maxHealth_ && enraged_) {
                enraged_ = false;
                this.pacify();
            }
        }
    }
}
