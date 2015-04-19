package com.hearthsim.card.weapon;

import com.hearthsim.card.Card;
import com.hearthsim.card.ImplementedCardList;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectHeroWeapon;
import com.hearthsim.event.effect.EffectOnResolveTargetable;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterSummon;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public abstract class WeaponCard extends Card implements EffectOnResolveTargetable {

    protected boolean isImmune() {
        return immune;
    }

    protected void setImmune(boolean immune) {
        this.immune = immune;
    }

    private boolean immune = false; // Does not take damage from attacking

    private byte weaponCharge;
    private byte weaponDamage;

    public WeaponCard() {
        super();
    }

    @Override
    protected void initFromImplementedCard(ImplementedCardList.ImplementedCard implementedCard) {
        super.initFromImplementedCard(implementedCard);
        if (implementedCard != null) {
            this.weaponCharge = (byte) implementedCard.durability;
            this.weaponDamage = (byte) implementedCard.attack_;
        }
    }

    @Override
    public WeaponCard deepCopy() {
        WeaponCard weapon = (WeaponCard) super.deepCopy();
        weapon.weaponCharge = weaponCharge;
        weapon.weaponDamage = weaponDamage;
        return weapon;
    }

    @Override
    public boolean equals(Object other) {
        if (!super.equals(other)) {
            return false;
        }

        if (this.getClass() != other.getClass()) {
            return false;
        }

        if (this.weaponDamage != ((WeaponCard) other).weaponDamage) {
            return false;
        }

        if (this.weaponCharge != ((WeaponCard) other).weaponCharge) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + weaponCharge;
        result = 31 * result + weaponDamage;
        return result;
    }

    @Override
    public EffectCharacter getTargetableEffect() {
        return new EffectHeroWeapon(this);
    }

    @Override
    public FilterCharacter getTargetableFilter() {
        return FilterCharacterSummon.SELF;
    }

    public byte getWeaponCharge() {
        return weaponCharge;
    }

    public void setWeaponCharge(byte weaponCharge) {
        this.weaponCharge = weaponCharge;
    }

    public void addWeaponCharge(byte weaponCharge) {
        this.weaponCharge += weaponCharge;
    }

    public void addWeaponDamage(byte weaponDamage) {
        this.weaponDamage += weaponDamage;
    }

    public byte getWeaponDamage() {
        return weaponDamage;
    }

    public void setWeaponDamage(byte weaponDamage) {
        this.weaponDamage = weaponDamage;
    }

    public void useWeaponCharge() {
        this.useWeaponCharge(1);
    }

    public void useWeaponCharge(int durabilityLoss) {
        if (!this.immune) {
            this.setWeaponCharge((byte) (this.getWeaponCharge() - durabilityLoss));
        }
    }

    public void beforeAttack(PlayerSide targetMinionPlayerSide, Minion targetMinion, HearthTreeNode toRet) throws HSException {
    }

    public void afterAttack(PlayerSide targetMinionPlayerSide, Minion targetMinion, HearthTreeNode toRet) throws HSException {
        this.useWeaponCharge();
    }

    public void minionSummonedEvent(PlayerSide thisMinionPlayerSide, PlayerSide summonedMinionPlayerSide, Minion summonedMinion, HearthTreeNode boardState) {
    }
}
