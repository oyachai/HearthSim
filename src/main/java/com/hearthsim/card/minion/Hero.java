package com.hearthsim.card.minion;

import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.weapon.WeaponCard;
import com.hearthsim.event.deathrattle.DeathrattleAction;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.HearthAction;
import com.hearthsim.util.HearthAction.Verb;
import com.hearthsim.util.factory.BoardStateFactoryBase;
import com.hearthsim.util.tree.HearthTreeNode;
import org.json.JSONObject;

public abstract class Hero extends Minion implements MinionSummonedInterface {

    protected static final byte HERO_ABILITY_COST = 2; // Assumed to be 2 for all heroes

    private WeaponCard weapon;
    private byte armor_;

    public Hero() {
        super();
    }

    @Deprecated
    public byte getWeaponCharge() {
        if (weapon == null) {
            return 0;
        } else {
            return weapon.getWeaponCharge();
        }
    }

    public void addArmor(byte armor) {
        armor_ += armor;
    }

    public byte getArmor() {
        return armor_;
    }

    public void setArmor(byte armor) {
        armor_ = armor;
    }

    /**
     *
     * @return the class name of the hero, e.g. Hunter
     */
    public String getHeroClass() {
        return this.getClass().getSimpleName();
    }

    @Override
    public boolean isHero() {
        return true;
    }

    @Override
    public Hero deepCopy() {
        Hero copy = (Hero) super.deepCopy();
        if (weapon != null) {
            copy.weapon = weapon.deepCopy();
        }
        copy.armor_ = armor_;

        return copy;
    }

    /**
     * Attack with the hero
     * <p>
     * A hero can only attack if it has a temporary buff, such as weapons
     *
     * @param targetMinionPlayerSide
     * @param targetMinion           The target minion
     * @param boardState             The BoardState before this card has performed its action. It will be manipulated and returned.
     * @return The boardState is manipulated and returned
     */
    @Override
    public HearthTreeNode attack(PlayerSide targetMinionPlayerSide, Minion targetMinion, HearthTreeNode boardState) throws HSException {

        if (!this.canAttack(boardState.data_, PlayerSide.CURRENT_PLAYER)) {
            return null;
        }

        WeaponCard attackingWeapon = this.getWeapon();

        if (attackingWeapon != null) {
            attackingWeapon.beforeAttack(targetMinionPlayerSide, targetMinion, boardState);
        }
        HearthTreeNode toRet = super.attack(targetMinionPlayerSide, targetMinion, boardState);

        // TODO: weapon deathrattles should happen at the same time as the minion deathrattles
        if (toRet != null && attackingWeapon != null) {
            attackingWeapon.afterAttack(targetMinionPlayerSide, targetMinion, boardState);
            DeathrattleAction weaponDeathrattle = this.checkForWeaponDeath();
            if (weaponDeathrattle != null) {
                toRet = weaponDeathrattle.performAction(CharacterIndex.HERO, PlayerSide.CURRENT_PLAYER, toRet);
                toRet = BoardStateFactoryBase.handleDeadMinions(toRet);
            }
        }

        return toRet;
    }

    @Override
    public boolean canBeUsedOn(PlayerSide playerSide, Minion minion, BoardModel boardModel) {
        if (this.hasBeenUsed()) {
            return false;
        }
        if (boardModel.getCurrentPlayer().getMana() < HERO_ABILITY_COST) {
            return false;
        }
        if (!minion.isHeroTargetable()) {
            return false;
        }
        return true;
    }

    public final HearthTreeNode useHeroAbility(PlayerSide targetPlayerSide, CharacterIndex targetIndex,
                                               HearthTreeNode boardState) throws HSException {
        Minion targetMinion = boardState.data_.modelForSide(targetPlayerSide).getCharacter(targetIndex);
        HearthTreeNode toRet = this.useHeroAbility(targetPlayerSide, targetMinion, boardState);
        if (toRet != null)
            toRet = toRet.notifyHeroAbilityUsed(targetPlayerSide, targetMinion);
        return toRet;
    }

    /**
     * Use the hero ability on a given target
     *
     * @param targetPlayerSide
     * @param targetMinion     The target minion
     * @param boardState
     * @return
     */
    public final HearthTreeNode useHeroAbility(PlayerSide targetPlayerSide, Minion targetMinion, HearthTreeNode boardState) {

        if (!this.canBeUsedOn(targetPlayerSide, targetMinion, boardState.data_)) {
            return null;
        }

        PlayerModel targetPlayer = boardState.data_.modelForSide(targetPlayerSide);

        HearthTreeNode toRet = this.useHeroAbility_core(targetPlayerSide, targetMinion, boardState);
        if (toRet != null) {
            CharacterIndex targetIndex = targetPlayer.getIndexForCharacter(targetMinion);
            toRet.setAction(new HearthAction(Verb.HERO_ABILITY, PlayerSide.CURRENT_PLAYER, 0, targetPlayerSide,
                targetIndex));
            toRet = BoardStateFactoryBase.handleDeadMinions(toRet);
        }
        return toRet;
    }

    protected abstract HearthTreeNode useHeroAbility_core(PlayerSide targetPlayerSide, Minion targetMinion, HearthTreeNode boardState);

    /**
     * Called when this minion takes damage
     * <p>
     * Overridden from Minion. Need to handle armor.
     *
     * @param damage           The amount of damage to take
     * @param thisPlayerSide
     * @param isSpellDamage
     */
    @Override
    public byte takeDamage(byte damage, PlayerSide originSide, PlayerSide thisPlayerSide, BoardModel board, boolean isSpellDamage) {
        byte totalDamage = damage;
        if (isSpellDamage) {
            totalDamage += board.modelForSide(originSide).getSpellDamage();
        }

        byte damageRemaining = (byte) (totalDamage - armor_);
        if (damageRemaining > 0) {
            armor_ = 0;
            super.takeDamage(damageRemaining, originSide, thisPlayerSide, board, false);
        } else {
            armor_ = (byte) (armor_ - totalDamage);
        }
        return totalDamage;
    }

    /**
     * Simpler version of take damage
     * <p>
     * For now, the Hero taking damage has no consequence to the board state.  So, this version can be used as a way to simplify the code.
     *
     * @param damage The amount of damage taken by the hero
     */
    public void takeDamage(byte damage) {
        byte damageRemaining = (byte) (damage - armor_);
        if (damageRemaining > 0) {
            armor_ = 0;
            health_ -= (byte) (damage - armor_);
        } else {
            armor_ = (byte) (armor_ - damage);
        }
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = super.toJSON();
        if (this.armor_ > 0) json.put("armor", this.armor_);
        json.put("weapon", this.weapon);
        return json;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;

        Hero hero = (Hero) o;

        if (armor_ != hero.armor_)
            return false;
        if (weapon != null ? !weapon.equals(hero.weapon) : hero.weapon != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (weapon != null ? weapon.hashCode() : 0);
        result = 31 * result + armor_;
        return result;
    }

    @Override
    public byte getTotalAttack(BoardModel boardModel, PlayerSide thisMinionPlayerSide) {
        byte attack = super.getTotalAttack(boardModel, thisMinionPlayerSide);
        if (this.getWeapon() != null) {
            attack += this.getWeapon().getWeaponDamage();
        }
        return attack;
    }

    public DeathrattleAction setWeapon(WeaponCard weapon) {
        if (weapon == null) {
            throw new RuntimeException("use 'destroy weapon' method if trying to remove weapon.");
        }

        DeathrattleAction action = this.weapon != null && this.weapon.hasDeathrattle() ? this.weapon.getDeathrattle() : null;
        this.weapon = weapon;
        return action;
    }

    public WeaponCard getWeapon() {
        return weapon;
    }

    public DeathrattleAction destroyWeapon() {
        if (weapon != null) {
            DeathrattleAction action = weapon.hasDeathrattle() ? weapon.getDeathrattle() : null;
            weapon = null;
            return action;
        }
        return null;
    }

    public DeathrattleAction checkForWeaponDeath() {
        if (weapon != null && weapon.getWeaponCharge() == 0) {
            return this.destroyWeapon();
        }

        return null;
    }

    @Override
    public HearthTreeNode minionSummonEvent(PlayerSide thisMinionPlayerSide, PlayerSide summonedMinionPlayerSide, Minion summonedMinion, HearthTreeNode boardState) {
        if (weapon != null) {
            weapon.minionSummonedEvent(thisMinionPlayerSide, summonedMinionPlayerSide, summonedMinion, boardState);
        }
        return boardState;
    }
}
