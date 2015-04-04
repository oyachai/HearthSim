package com.hearthsim.card.weapon;

import com.hearthsim.card.Card;
import com.hearthsim.card.ImplementedCardList;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.CharacterFilterSummon;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectHeroWeapon;
import com.hearthsim.event.effect.CardEffectOnResolveTargetableInterface;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public abstract class WeaponCard extends Card implements CardEffectOnResolveTargetableInterface {

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
        ImplementedCardList cardList = ImplementedCardList.getInstance();
        ImplementedCardList.ImplementedCard implementedCard = cardList.getCardForClass(this.getClass());
        weaponCharge = (byte) implementedCard.durability;
        weaponDamage = (byte) implementedCard.attack_;
        name_ = implementedCard.name_;
        baseManaCost = (byte) implementedCard.mana_;
        isInHand_ = true;
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
    public CardEffectCharacter getTargetableEffect() {
        return new CardEffectHeroWeapon(this);
    }

    @Override
    public CharacterFilter getTargetableFilter() {
        return CharacterFilterSummon.SELF;
    }

    public byte getWeaponCharge() {
        return weaponCharge;
    }

    public void setWeaponCharge(byte weaponCharge) {
        this.weaponCharge = weaponCharge;
    }

    public byte getWeaponDamage() {
        return weaponDamage;
    }

    public void setWeaponDamage(byte weaponDamage) {
        this.weaponDamage = weaponDamage;
    }

    public void useWeaponCharge() {
        if (!this.immune) {
            this.setWeaponCharge((byte) (this.getWeaponCharge() - 1));
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
