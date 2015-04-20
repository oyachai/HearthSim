package com.hearthsim.card.minion;

import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public abstract class MinionWithEnrage extends Minion {

    private boolean enraged_;

    protected MinionWithEnrage() {
        super();
    }

    @Override
    public void setHealth(byte health) {
        super.setHealth(health);
        this.enrageCheck();
    }

    @Override
    public void setMaxHealth(byte health) {
        super.setMaxHealth(health);
        this.enrageCheck();
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
    protected abstract void enrage();

    /**
     * Turn off enrage
     */
    protected abstract void pacify();


    @Override
    public byte takeDamage(byte damage, PlayerSide originSide, PlayerSide thisPlayerSide, BoardModel board, boolean isSpellDamage) {
        byte actualDamage = super.takeDamage(damage, originSide, thisPlayerSide, board, isSpellDamage);
        if (actualDamage > 0) {
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
    public byte takeHeal(byte healAmount, PlayerSide thisPlayerSide, BoardModel board) {
        byte actual = super.takeHeal(healAmount, thisPlayerSide, board);
        if (actual > 0) {
            this.enrageCheck();
        }
        return actual;
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
     */
    @Override
    public void silenced(PlayerSide thisPlayerSide, BoardModel boardState) {
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
