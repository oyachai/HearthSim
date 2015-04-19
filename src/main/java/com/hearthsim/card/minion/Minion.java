package com.hearthsim.card.minion;

import com.hearthsim.card.*;
import com.hearthsim.event.attack.AttackAction;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterSummon;
import com.hearthsim.event.effect.EffectOnResolveTargetable;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterSummon;
import com.hearthsim.exception.HSException;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.HearthAction;
import com.hearthsim.util.HearthAction.Verb;
import com.hearthsim.util.factory.BoardStateFactoryBase;
import com.hearthsim.util.tree.HearthTreeNode;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class Minion extends Card implements EffectOnResolveTargetable<Card>, CardEndTurnInterface, CardStartTurnInterface {
    private static final Logger log = LoggerFactory.getLogger(Card.class);

    public static enum MinionTribe {
        NONE,
        BEAST,
        MECH,
        MURLOC,
        PIRATE,
        DEMON,
        DRAGON,
        TOTEM
    }

    public static MinionTribe StringToMinionTribe(String race) {
        race = race == null ? "" : race.toLowerCase();
        switch (race) {
            case "beast":
                return MinionTribe.BEAST;
            case "mech":
                return MinionTribe.MECH;
            case "murloc":
                return MinionTribe.MURLOC;
            case "pirate":
                return MinionTribe.PIRATE;
            case "demon":
                return MinionTribe.DEMON;
            case "dragon":
                return MinionTribe.DRAGON;
            case "totem":
                return MinionTribe.TOTEM;
            default:
                return MinionTribe.NONE;
        }
    }

    private boolean taunt_;
    private boolean divineShield_;
    protected boolean windFury_;
    private boolean charge_;
    private boolean immune_ = false; // Ignores damage

    protected boolean hasAttacked_;
    protected boolean hasWindFuryAttacked_;

    private boolean frozen_;
    protected boolean silenced_;
    private boolean stealthed_;
    protected boolean heroTargetable_ = true;

    protected byte health_;
    protected byte maxHealth_;
    private byte auraHealth_;

    protected byte attack_;
    private byte extraAttackUntilTurnEnd_;
    private byte auraAttack_;

    private boolean destroyOnTurnStart_;
    private boolean destroyOnTurnEnd_;

    protected byte spellDamage_;
    protected boolean cantAttack;

    public Minion() {
        super();
    }

    @Override
    protected void initFromImplementedCard(ImplementedCardList.ImplementedCard implementedCard) {
        super.initFromImplementedCard(implementedCard);
        if (implementedCard != null) {
            // only 'Minion' class is not implemented
            this.attack_ = implementedCard.attack_ > 0 ? (byte) implementedCard.attack_ : 0;
            this.health_ = (byte) implementedCard.health_;
            this.maxHealth_ = health_;
            this.taunt_ = implementedCard.taunt_;
            this.divineShield_ = implementedCard.divineShield_;
            this.windFury_ = implementedCard.windfury_;
            this.charge_ = implementedCard.charge_;
            this.stealthed_ = implementedCard.stealth_;
            this.spellDamage_ = (byte) implementedCard.spellDamage;
            this.cantAttack = implementedCard.cantAttack;
        }
    }

    public boolean getTaunt() {
        return taunt_;
    }

    public void setTaunt(boolean taunt) {
        taunt_ = taunt;
    }

    public byte getHealth() {
        return health_;
    }

    public void setHealth(byte health) {
        health_ = health;
    }

    public final void addHealth(byte value) {
        this.setHealth((byte) (this.getHealth() + value));
    }

    public byte getMaxHealth() {
        return maxHealth_;
    }

    public void setMaxHealth(byte health) {
        maxHealth_ = health;
    }

    public final void addMaxHealth(byte value) {
        this.setMaxHealth((byte) (this.getMaxHealth() + value));
    }

    public byte getBaseHealth() {
        if (this.implementedCard == null) {
            return this.getMaxHealth();
        }
        return (byte) this.implementedCard.health_;
    }

    public byte getAttack() {
        return attack_;
    }

    public void setAttack(byte attack) {
        attack_ = attack;
    }

    public void addAttack(byte value) {
        attack_ += value;
    }

    public byte getBaseAttack() {
        if (this.implementedCard == null) {
            return this.attack_;
        }
        return (byte) this.implementedCard.attack_;
    }

    public boolean getDivineShield() {
        return divineShield_;
    }

    public void setDivineShield(boolean divineShield) {
        divineShield_ = divineShield;
    }

    public boolean canAttack() {
        return !this.hasAttacked_ && (this.getTotalAttack()) > 0 && !this.frozen_ && !cantAttack;
    }

    public void hasAttacked(boolean hasAttacked) {
        hasAttacked_ = hasAttacked;
    }

    public boolean hasWindFuryAttacked() {
        return hasWindFuryAttacked_;
    }

    public void hasWindFuryAttacked(boolean hasAttacked) {
        hasWindFuryAttacked_ = hasAttacked;
    }

    public boolean getCharge() {
        return charge_;
    }

    public void setCharge(boolean value) {
        charge_ = value;
    }

    public boolean getFrozen() {
        return frozen_;
    }

    public void setFrozen(boolean value) {
        frozen_ = value;
    }

    public boolean getWindfury() {
        return windFury_;
    }

    public void setWindfury(boolean value) {
        windFury_ = value;
        if (hasAttacked_) {
            hasAttacked_ = false;
            hasWindFuryAttacked_ = true;
        }
    }

    public void addExtraAttackUntilTurnEnd(byte value) {
        extraAttackUntilTurnEnd_ += value;
    }

    public byte getExtraAttackUntilTurnEnd() {
        return extraAttackUntilTurnEnd_;
    }

    public void setExtraAttackUntilTurnEnd(byte value) {
        extraAttackUntilTurnEnd_ = value;
    }

    public boolean getDestroyOnTurnStart() {
        return destroyOnTurnStart_;
    }

    public void setDestroyOnTurnStart(boolean value) {
        destroyOnTurnStart_ = value;
    }

    public boolean getDestroyOnTurnEnd() {
        return destroyOnTurnEnd_;
    }

    public void setDestroyOnTurnEnd(boolean value) {
        destroyOnTurnEnd_ = value;
    }

    public boolean isSilenced() {
        return silenced_;
    }

    protected void setSilenced(boolean silenced) {
        silenced_ = silenced;
    }

    public byte getAuraAttack() {
        return auraAttack_;
    }

    public void setAuraAttack(byte value) {
        auraAttack_ = value;
    }

    public byte getAuraHealth() {
        return auraHealth_;
    }

    public void setAuraHealth(byte value) {
        auraHealth_ = value;
    }

    public byte getTotalAttack() {
        return (byte) (attack_ + auraAttack_ + extraAttackUntilTurnEnd_);
    }

    public byte getTotalHealth() {
        return (byte) (health_ + auraHealth_);
    }

    public byte getTotalMaxHealth() {
        return (byte) (maxHealth_ + auraHealth_);
    }

    public MinionTribe getTribe() {
        if (this.implementedCard == null) {
            return MinionTribe.NONE;
        }

        return Minion.StringToMinionTribe(this.implementedCard.race);
    }

    public void addAuraHealth(byte value) {
        auraHealth_ += value;
    }

    public void removeAuraHealth(byte value) {
        health_ += value;
        if (health_ > maxHealth_)
            health_ = maxHealth_;
        auraHealth_ -= value;
    }

    public boolean getStealthed() {
        return stealthed_;
    }

    public void setStealthed(boolean value) {
        stealthed_ = value;
    }

    public boolean getImmune() {
        return immune_;
    }

    public void setImmune(boolean immune) {
        immune_ = immune;
    }

    // This is a flag to tell the BoardState that it can't cheat on the placement of this minion
    public boolean getPlacementImportant() {
        return false;
    }

    public boolean isHeroTargetable() {
        return heroTargetable_;
    }

    public void setHeroTargetable(boolean value) {
        heroTargetable_ = value;
    }

    public byte getSpellDamage() {
        return spellDamage_;
    }

    public void setSpellDamage(byte value) {
        spellDamage_ = value;
    }

    public void addSpellDamage(byte value) {
        spellDamage_ += value;
    }

    public void subtractSpellDamage(byte value) {
        spellDamage_ -= value;
    }

    public boolean isAlive() {
        return getTotalHealth() > 0;
    }

    public boolean isHero() {
        return false;
    }

    /**
     * Called at the start of the turn
     * <p>
     * This function is called at the start of the turn. Any derived class must override it to implement whatever "start of the turn" effect the card has.
     */
    @Override
    public HearthTreeNode startTurn(PlayerSide thisMinionPlayerIndex, HearthTreeNode boardModel) throws HSException {
        if (destroyOnTurnStart_) {
            // toRet = this.destroyAndNotify(thisMinionPlayerIndex, toRet, deckPlayer0, deckPlayer1);
            this.setHealth((byte) -99);
        }
        return boardModel;
    }

    /**
     * End the turn and resets the card state
     * <p>
     * This function is called at the end of the turn. Any derived class must override it and remove any temporary buffs that it has.
     * <p>
     * This is not the most efficient implementation... luckily, endTurn only happens once per turn
     */
    @Override
    public HearthTreeNode endTurn(PlayerSide thisMinionPlayerIndex, HearthTreeNode boardModel) throws HSException {
        extraAttackUntilTurnEnd_ = 0;
        if (destroyOnTurnEnd_) {
            // toRet = this.destroyAndNotify(thisMinionPlayerIndex, toRet, deckPlayer0, deckPlayer1);
            this.setHealth((byte) -99);
        }
        return boardModel;
    }

    /**
     * Called when this minion takes damage
     * <p>
     * Always use this function to take damage... it properly notifies all others of its damage and possibly of its death
     *
     * @param damage            The amount of damage to take
     * @param attackPlayerSide  The player index of the attacker. This is needed to do things like +spell damage.
     * @param thisPlayerSide
     * @param boardState
     * @param isSpellDamage     True if this is a spell damage
     * @param handleMinionDeath Set this to True if you want the death event to trigger when (if) the minion dies from this damage. Setting this flag to True will also trigger deathrattle immediately.
     * @throws HSInvalidPlayerIndexException
     */
    public HearthTreeNode takeDamageAndNotify(byte damage, PlayerSide attackPlayerSide, PlayerSide thisPlayerSide, HearthTreeNode boardState, boolean isSpellDamage, boolean handleMinionDeath) {
        byte damageDealt = this.takeDamage(damage, attackPlayerSide, thisPlayerSide, boardState.data_, isSpellDamage);
        if (damageDealt > 0) {
            return boardState.notifyMinionDamaged(thisPlayerSide, this);
        }
        return boardState;
    }

    public byte takeDamage(byte damage, PlayerSide originSide, PlayerSide thisPlayerSide, BoardModel board, boolean isSpellDamage) {
        if (this.divineShield_) {
            if (damage > 0)
                this.divineShield_ = false;
            return 0;
        }

        if (this.immune_) {
            return 0;
        }

        byte totalDamage = damage;
        if (isSpellDamage) {
            totalDamage += board.modelForSide(originSide).getSpellDamage();
        }
        this.health_ = (byte) (this.health_ - totalDamage);
        return totalDamage;
    }

    /**
     * Called when this minion dies (destroyAndNotify)
     * <p>
     * Always use this function to "kill" minions
     *
     * @param thisPlayerSide
     * @param boardState
     * @throws HSInvalidPlayerIndexException
     */
    public HearthTreeNode destroyAndNotify(PlayerSide thisPlayerSide, HearthTreeNode boardState, boolean singleRealizationOnly) {

        health_ = 0;
        HearthTreeNode toRet = boardState;

        // perform the deathrattle action if there is one
        if (deathrattleAction_ != null) {
            toRet = deathrattleAction_.performAction(this, thisPlayerSide, toRet, singleRealizationOnly);
        }

        toRet = toRet.notifyMinionDead(thisPlayerSide, this);
        return toRet;
    }

    /**
     * Called when this minion is silenced
     * <p>
     * Always use this function to "silence" minions
     *
     * @param thisPlayerSide
     * @param boardState
     * @throws HSInvalidPlayerIndexException
     */
    public void silenced(PlayerSide thisPlayerSide, BoardModel boardState) {
        spellDamage_ = 0;
        divineShield_ = false;
        taunt_ = false;
        charge_ = false;
        frozen_ = false;
        windFury_ = false;
        deathrattleAction_ = null;
        stealthed_ = false;
        heroTargetable_ = true;
        cantAttack = false;

        int damageTaken = this.maxHealth_ - this.health_;

        // Reset the attack and health to base
        this.attack_ = this.getBaseAttack();
        if (this.maxHealth_ > this.getBaseHealth()) {
            this.maxHealth_ = this.getBaseHealth();

            if (this.health_ > this.maxHealth_) {
                this.health_ = this.maxHealth_;
            }
        } else if (this.maxHealth_ < this.getBaseHealth()) {
            this.maxHealth_ = this.getBaseHealth();
            this.health_ = (byte) (this.maxHealth_ - damageTaken);
        }


        //Ask the board to clear this minion's aura
        boardState.removeAuraOfMinion(thisPlayerSide, this);

        //Set the silenced flag at the end
        silenced_ = true;
    }

    /**
     * Called when this minion is healed
     * <p>
     * Always use this function to heal minions
     *  @param healAmount     The amount of healing to take
     * @param thisPlayerSide
     * @param boardState
     */
    public HearthTreeNode takeHealAndNotify(byte healAmount, PlayerSide thisPlayerSide, HearthTreeNode boardState) {
        byte actual = this.takeHeal(healAmount, thisPlayerSide, boardState.data_);
        if (actual > 0) {
            return boardState.notifyMinionHealed(thisPlayerSide, this);
        }
        return boardState;
    }

    public byte takeHeal(byte healAmount, PlayerSide thisPlayerSide, BoardModel board) {
        int missing = this.maxHealth_ - this.health_;
        int actual = healAmount > missing ? missing : healAmount;
        this.health_ += actual;
        return (byte) actual;
    }

    @Override
    public boolean canBeUsedOn(PlayerSide playerSide, Minion minion, BoardModel boardModel) {
        if (!super.canBeUsedOn(playerSide, minion, boardModel)) {
            return false;
        }
        return playerSide != PlayerSide.WAITING_PLAYER && !hasBeenUsed;
    }

    /**
     * Use a targetable battlecry. This will add battlecry nodes to boardState as children.
     *
     * @param side
     * @param targetCharacterIndex
     * @param boardState
     * @return
     * @throws HSException
     */
    public HearthTreeNode useTargetableBattlecry(PlayerSide side, int targetCharacterIndex, HearthTreeNode boardState, boolean singleRealizationOnly) {
        if (this instanceof MinionBattlecryInterface) {
            boardState.data_.modelForSide(side);

            EffectCharacter<Minion> battlecryEffect = ((MinionBattlecryInterface)this).getBattlecryEffect();
            boardState = battlecryEffect.applyEffect(PlayerSide.CURRENT_PLAYER, this, side, targetCharacterIndex, boardState);

            if (boardState != null) {
                int originCharacterIndex = boardState.data_.modelForSide(PlayerSide.CURRENT_PLAYER).getIndexForCharacter(this);
                boardState = BoardStateFactoryBase.handleDeadMinions(boardState, singleRealizationOnly);
                boardState.setAction(new HearthAction(Verb.TARGETABLE_BATTLECRY, PlayerSide.CURRENT_PLAYER, originCharacterIndex, side, targetCharacterIndex));
            }
        }

        return boardState;
    }

    /**
     * Use an untargetable battlecry.
     *
     * @param minionPlacementIndex
     * @param boardState
     * @param singleRealizationOnly
     * @return
     * @throws HSException
     */
    @Deprecated
    public HearthTreeNode useUntargetableBattlecry(int minionPlacementIndex, HearthTreeNode boardState, boolean singleRealizationOnly) {
        HearthTreeNode toRet = boardState;
        if (this instanceof MinionUntargetableBattlecry) {
            EffectCharacter<Minion> battlecryEffect = ((MinionUntargetableBattlecry)this).getBattlecryEffect();
            toRet = battlecryEffect.applyEffect(PlayerSide.CURRENT_PLAYER, this, PlayerSide.CURRENT_PLAYER, minionPlacementIndex, toRet);
            if (toRet != null) {
                // Check for dead minions
                toRet = BoardStateFactoryBase.handleDeadMinions(toRet, singleRealizationOnly);
            }
        }
        return toRet;
    }

    /**
     * Places a minion on the board by using the card in hand
     *
     * @param side
     * @param targetMinion The target minion (can be a Hero). The new minion is always placed to the right of (higher index) the target minion. If the target minion is a hero, then it is placed at the left-most position.
     * @param boardState   The BoardState before this card has performed its action. It will be manipulated and returned.
     * @return The boardState is manipulated and returned
     * @throws HSException
     */
    @Override
    protected HearthTreeNode use_core(PlayerSide side, Minion targetMinion, HearthTreeNode boardState, boolean singleRealizationOnly) throws HSException {
        if (hasBeenUsed || side == PlayerSide.WAITING_PLAYER || boardState.data_.modelForSide(side).isBoardFull()) {
            return null;
        }

        HearthTreeNode toRet = boardState;
        toRet = super.use_core(side, targetMinion, toRet, singleRealizationOnly);
        return toRet;
    }

    @Override
    public EffectCharacter getTargetableEffect() {
        return new EffectCharacterSummon(this);
    }

    @Override
    public FilterCharacter getTargetableFilter() {
        return FilterCharacterSummon.ALL_FRIENDLIES;
    }

    /**
     * Places a minion on the board via a summon effect
     * <p>
     * This function is meant to be used when summoning minions through means other than a direct card usage.
     *
     * @param targetSide
     * @param boardState     The BoardState before this card has performed its action. It will be manipulated and returned.
     * @return The boardState is manipulated and returned
     */
    public HearthTreeNode summonMinion(PlayerSide targetSide, int targetMinionIndex, HearthTreeNode boardState, boolean wasPlayed, boolean singleRealizationOnly) {
        if (boardState.data_.modelForSide(targetSide).isBoardFull())
            return null;

        HearthTreeNode toRet = boardState;
        toRet = this.summonMinion_core(targetSide, targetMinionIndex, toRet);

        if (this instanceof MinionBattlecryInterface) {
            MinionBattlecryInterface battlecryOrigin = ((MinionBattlecryInterface) this);

            HearthTreeNode child;
            Minion origin;
            int originCharacterIndex = toRet.data_.modelForSide(PlayerSide.CURRENT_PLAYER).getIndexForCharacter(this);

            ArrayList<HearthTreeNode> children = new ArrayList<>();
            for (BoardModel.CharacterLocation characterLocation : toRet.data_) {
                if (battlecryOrigin.getBattlecryFilter().targetMatches(PlayerSide.CURRENT_PLAYER, this, characterLocation.getPlayerSide(), characterLocation.getIndex(), toRet.data_)) {
                    child = new HearthTreeNode(toRet.data_.deepCopy());
                    origin = child.data_.getCharacter(PlayerSide.CURRENT_PLAYER, originCharacterIndex);
                    child = origin.useTargetableBattlecry(characterLocation.getPlayerSide(), characterLocation.getIndex(), child, singleRealizationOnly);
                    if (child != null) {
                        children.add(child);
                    }
                }
            }
            toRet = this.createNodeWithChildren(toRet, children);
        }

        if (wasPlayed) {
            toRet = toRet.notifyMinionPlayed(targetSide, this);
        }

        toRet = toRet.notifyMinionSummon(targetSide, this);

        return toRet;
    }

    public HearthTreeNode summonMinion(PlayerSide targetSide, Minion targetMinion, HearthTreeNode boardState, boolean wasPlayed, boolean singleRealizationOnly) {
        return this.summonMinion(targetSide, boardState.data_.modelForSide(targetSide).getIndexForCharacter(targetMinion), boardState, wasPlayed, singleRealizationOnly);
    }

    public HearthTreeNode summonMinionAtEnd(PlayerSide targetSide, HearthTreeNode boardState, boolean wasPlayed, boolean singleRealizationOnly) {
        PlayerModel player = boardState.data_.modelForSide(targetSide);
        return this.summonMinion(targetSide, player.getNumMinions(), boardState, wasPlayed, singleRealizationOnly);
    }

    /**
     *
     * Places a minion on the board via a summon effect
     *
     * This function is meant to be used when summoning minions through means other than a direct card usage.
     *
     * @param targetSide
     * @param targetIndex The target character (can be a Hero). The new minion is always placed to the right of (higher index) the target minion. If the target minion is a hero, then it is placed at the left-most position.
     * @param boardState The BoardState before this card has performed its action. It will be manipulated and returned.
     * @return The boardState is manipulated and returned
     * @throws HSException
     */
    protected HearthTreeNode summonMinion_core(PlayerSide targetSide, int targetIndex, HearthTreeNode boardState) {
        boardState.data_.placeMinion(targetSide, this, targetIndex);
        if (!charge_) {
            hasAttacked_ = true;
        }
        hasBeenUsed = true;
        return boardState;
    }

    /**
     *
     * Attack with the minion
     *
     *
     *
     * @param targetMinionPlayerSide
     * @param targetMinion The target minion
     * @param boardState The BoardState before this card has performed its action. It will be manipulated and returned.
     * @return The boardState is manipulated and returned
     */
    public HearthTreeNode attack(PlayerSide targetMinionPlayerSide, Minion targetMinion, HearthTreeNode boardState, boolean singleRealizationOnly) throws HSException {

        // can't attack a stealthed target
        if (targetMinion.getStealthed())
            return null;

        if (!this.canAttack()) {
            return null;
        }

        if (targetMinionPlayerSide == PlayerSide.CURRENT_PLAYER) {
            return null;
        }

        PlayerModel currentPlayer = boardState.data_.getCurrentPlayer();
        PlayerModel targetPlayer = boardState.data_.modelForSide(targetMinionPlayerSide);

        // Notify all that an attack is beginning
        HearthTreeNode toRet;
        int attackerIndex = currentPlayer.getIndexForCharacter(this);
        int targetIndex = targetPlayer.getIndexForCharacter(targetMinion);

        // Do the actual attack
        toRet = this.attack_core(targetMinionPlayerSide, targetMinion, boardState, singleRealizationOnly);

        // check for and remove dead minions
        if (toRet != null) {
            toRet.setAction(new HearthAction(Verb.ATTACK, PlayerSide.CURRENT_PLAYER, attackerIndex,
                    targetMinionPlayerSide, targetIndex));
            toRet = BoardStateFactoryBase.handleDeadMinions(toRet, singleRealizationOnly);
        }

        // Attacking means you lose stealth
        if (toRet != null)
            this.stealthed_ = false;

        return toRet;
    }

    public HearthTreeNode attack(PlayerSide targetMinionPlayerSide, int targetCharacterIndex, HearthTreeNode boardState, boolean singleRealizationOnly) throws HSException {
        Minion targetCharacter = boardState.data_.modelForSide(targetMinionPlayerSide).getCharacter(targetCharacterIndex);
        return this.attack(targetMinionPlayerSide, targetCharacter, boardState, singleRealizationOnly);
    }

    /**
     *
     * Attack with the minion
     *
     *
     *
     * @param targetMinionPlayerSide
     * @param targetMinion The target minion
     * @param boardState The BoardState before this card has performed its action. It will be manipulated and returned.
     * @return The boardState is manipulated and returned
     */
    protected HearthTreeNode attack_core(PlayerSide targetMinionPlayerSide, Minion targetMinion, HearthTreeNode boardState, boolean singleRealizationOnly) throws HSException {

        HearthTreeNode toRet = boardState;
        byte origAttack = targetMinion.getTotalAttack();
        toRet = targetMinion.takeDamageAndNotify(this.getTotalAttack(), PlayerSide.CURRENT_PLAYER, targetMinionPlayerSide, toRet, false, false);
        toRet = this.takeDamageAndNotify(origAttack, targetMinionPlayerSide, PlayerSide.CURRENT_PLAYER, toRet, false, false);
        if (windFury_ && !hasWindFuryAttacked_)
            hasWindFuryAttacked_ = true;
        else
            hasAttacked_ = true;
        return toRet;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = super.toJSON();
        json.put("attack", attack_);
        json.put("baseAttack", this.getBaseAttack());
        if (health_ != maxHealth_) json.put("health", health_);
        json.put("baseHealth", this.getBaseHealth());
        json.put("maxHealth", maxHealth_);
        if (taunt_) json.put("taunt", taunt_);
        if (divineShield_) json.put("divineShield", divineShield_);
        if (windFury_) json.put("windFury", windFury_);
        if (charge_) json.put("charge", charge_);
        if (frozen_) json.put("frozen", frozen_);
        if (silenced_) json.put("silenced", silenced_);
        if (hasAttacked_) json.put("hasAttacked", hasAttacked_);
        if (spellDamage_ != 0) json.put("spellDamage", spellDamage_);
        return json;
    }

    /**
     * Deep copy of the object
     *
     * Note: the event actions are not actually deep copied.
     */
    @Override
    public Card deepCopy() {

        Minion minion = null;
        try {
            minion = getClass().newInstance();
        } catch(InstantiationException e) {
            Minion.log.error("instantiation error", e);
        } catch(IllegalAccessException e) {
            Minion.log.error("illegal access error", e);
        }
        if (minion == null) {
            throw new RuntimeException("unable to instantiate minion.");
        }

        minion.attack_ = attack_;
        minion.health_ = health_;
        minion.extraAttackUntilTurnEnd_ = extraAttackUntilTurnEnd_;
        minion.auraAttack_ = auraAttack_;
        minion.maxHealth_ = maxHealth_;
        minion.auraHealth_ = auraHealth_;
        minion.spellDamage_ = spellDamage_;
        minion.taunt_ = taunt_;
        minion.divineShield_ = divineShield_;
        minion.windFury_ = windFury_;
        minion.charge_ = charge_;
        minion.hasAttacked_ = hasAttacked_;
        minion.hasWindFuryAttacked_ = hasWindFuryAttacked_;
        minion.frozen_ = frozen_;
        minion.silenced_ = silenced_;
        minion.stealthed_ = stealthed_;
        minion.heroTargetable_ = heroTargetable_;
        minion.destroyOnTurnStart_ = destroyOnTurnStart_;
        minion.destroyOnTurnEnd_ = destroyOnTurnEnd_;
        minion.deathrattleAction_ = deathrattleAction_;
        minion.isInHand_ = isInHand_;
        minion.hasBeenUsed = hasBeenUsed;
        // TODO: continue here.

        return minion;
    }

    @Override
    public boolean equals(Object other) {
        if (!super.equals(other)) {
            return false;
        }

        Minion otherMinion = (Minion)other;
        if (health_ != otherMinion.health_)
            return false;
        if (maxHealth_ != otherMinion.maxHealth_)
            return false;
        if (auraHealth_ != otherMinion.auraHealth_)
            return false;

        if (attack_ != otherMinion.attack_)
            return false;
        if (extraAttackUntilTurnEnd_ != otherMinion.extraAttackUntilTurnEnd_)
            return false;
        if (auraAttack_ != otherMinion.auraAttack_)
            return false;

        if (taunt_ != otherMinion.taunt_)
            return false;
        if (divineShield_ != otherMinion.divineShield_)
            return false;
        if (windFury_ != otherMinion.windFury_)
            return false;
        if (charge_ != otherMinion.charge_)
            return false;
        if (stealthed_ != otherMinion.stealthed_)
            return false;
        if (hasAttacked_ != otherMinion.hasAttacked_)
            return false;
        if (heroTargetable_ != otherMinion.heroTargetable_)
            return false;
        if (hasWindFuryAttacked_ != otherMinion.hasWindFuryAttacked_)
            return false;
        if (frozen_ != otherMinion.frozen_)
            return false;
        if (silenced_ != otherMinion.silenced_)
            return false;
        if (destroyOnTurnStart_ != otherMinion.destroyOnTurnStart_)
            return false;
        if (destroyOnTurnEnd_ != otherMinion.destroyOnTurnEnd_)
            return false;

        if (spellDamage_ != otherMinion.spellDamage_)
            return false;

        // This is checked for reference equality
        if (deathrattleAction_ == null && ((Minion)other).deathrattleAction_ != null)
            return false;

        if (deathrattleAction_ != null && !deathrattleAction_.equals(((Minion)other).deathrattleAction_))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (taunt_ ? 1 : 0);
        result = 31 * result + (divineShield_ ? 1 : 0);
        result = 31 * result + (windFury_ ? 1 : 0);
        result = 31 * result + (charge_ ? 1 : 0);
        result = 31 * result + (hasAttacked_ ? 1 : 0);
        result = 31 * result + (hasWindFuryAttacked_ ? 1 : 0);
        result = 31 * result + (frozen_ ? 1 : 0);
        result = 31 * result + (silenced_ ? 1 : 0);
        result = 31 * result + (stealthed_ ? 1 : 0);
        result = 31 * result + (heroTargetable_ ? 1 : 0);
        result = 31 * result + health_;
        result = 31 * result + maxHealth_;
        result = 31 * result + this.getBaseHealth();
        result = 31 * result + auraHealth_;
        result = 31 * result + attack_;
        result = 31 * result + this.getBaseAttack();
        result = 31 * result + extraAttackUntilTurnEnd_;
        result = 31 * result + auraAttack_;
        result = 31 * result + (destroyOnTurnStart_ ? 1 : 0);
        result = 31 * result + (destroyOnTurnEnd_ ? 1 : 0);
        result = 31 * result + spellDamage_;
        result = 31 * result + (deathrattleAction_ != null ? deathrattleAction_.hashCode() : 0);
        result = 31 * result + (this.getPlacementImportant() ? 1 : 0);
        return result;
    }

    @Deprecated
    public Minion(String name, byte mana, byte attack, byte health, byte baseAttack, byte extraAttackUntilTurnEnd,
                  byte auraAttack, byte baseHealth, byte maxHealth, byte auraHealth, byte spellDamage, boolean taunt,
                  boolean divineShield, boolean windFury, boolean charge, boolean hasAttacked, boolean hasWindFuryAttacked,
                  boolean frozen, boolean silenced, boolean stealthed, boolean heroTargetable, boolean summoned,
                  boolean transformed, boolean destroyOnTurnStart, boolean destroyOnTurnEnd, AttackAction attackAction,
                  boolean isInHand, boolean hasBeenUsed) {
        super(name, mana, hasBeenUsed, isInHand, (byte) 0);
        attack_ = attack;
        health_ = health;
        taunt_ = taunt;
        divineShield_ = divineShield;
        windFury_ = windFury;
        charge_ = charge;
        hasAttacked_ = hasAttacked;
        extraAttackUntilTurnEnd_ = extraAttackUntilTurnEnd;
        hasWindFuryAttacked_ = hasWindFuryAttacked;
        frozen_ = frozen;
        silenced_ = silenced;
        maxHealth_ = maxHealth;
        destroyOnTurnStart_ = destroyOnTurnStart;
        destroyOnTurnEnd_ = destroyOnTurnEnd;

        auraAttack_ = auraAttack;
        auraHealth_ = auraHealth;

        spellDamage_ = spellDamage;

        stealthed_ = stealthed;
        heroTargetable_ = heroTargetable;
    }

    @Deprecated
    public boolean hasAttacked() {
        return hasAttacked_;
    }

    @Deprecated
    public void silenced(PlayerSide thisPlayerSide, HearthTreeNode boardState) throws HSInvalidPlayerIndexException {
        this.silenced(thisPlayerSide, boardState.data_);
    }

    @Deprecated
    public HearthTreeNode takeDamage(byte damage, PlayerSide attackPlayerSide, PlayerSide thisPlayerSide,
                                     HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1, boolean isSpellDamage,
                                     boolean handleMinionDeath) throws HSException {
        return this.takeDamageAndNotify(damage, attackPlayerSide, thisPlayerSide, boardState, isSpellDamage, handleMinionDeath);
    }

    @Deprecated
    public HearthTreeNode destroyed(PlayerSide thisPlayerSide, HearthTreeNode boardState, Deck deckPlayer0,
                                    Deck deckPlayer1, boolean singleRealizationOnly) throws HSException {
        return this.destroyAndNotify(thisPlayerSide, boardState, singleRealizationOnly);
    }

    @Deprecated
    public HearthTreeNode takeHeal(byte healAmount, PlayerSide thisPlayerSide, HearthTreeNode boardState,
                                   Deck deckPlayer0, Deck deckPlayer1) throws HSException {
        return this.takeHealAndNotify(healAmount, thisPlayerSide, boardState);
    }

    @Deprecated
    public HearthTreeNode useUntargetableBattlecry(Minion minionPlacementTarget, HearthTreeNode boardState, boolean singleRealizationOnly) throws HSException {
        return this.useUntargetableBattlecry(boardState.data_.getCurrentPlayer().getIndexForCharacter(minionPlacementTarget), boardState, singleRealizationOnly);
    }

    @Deprecated
    public HearthTreeNode useUntargetableBattlecry(Minion minionPlacementTarget, HearthTreeNode boardState,
                                                   Deck deckPlayer0, Deck deckPlayer1, boolean singleRealizationOnly) throws HSException {
        return this.useUntargetableBattlecry(minionPlacementTarget, boardState, singleRealizationOnly);
    }

    @Deprecated
    public HearthTreeNode summonMinion(PlayerSide targetSide, Minion targetMinion, HearthTreeNode boardState,
                                       Deck deckPlayer0, Deck deckPlayer1, boolean wasPlayed, boolean singleRealizationOnly) throws HSException {
        return this.summonMinion(targetSide, targetMinion, boardState, wasPlayed, singleRealizationOnly);
    }

    @Deprecated
    public HearthTreeNode summonMinion(PlayerSide targetSide, int targetIndex, HearthTreeNode boardState,
                                       Deck deckPlayer0, Deck deckPlayer1, boolean wasPlayed, boolean singleRealizationOnly) throws HSException {
        PlayerModel player = boardState.data_.modelForSide(targetSide);
        Minion targetLocation = player.getCharacter(targetIndex);
        return this.summonMinion(targetSide, targetLocation, boardState, wasPlayed, singleRealizationOnly);
    }

    @Deprecated
    public HearthTreeNode summonMinionAtEnd(PlayerSide targetSide, HearthTreeNode boardState,
                                            Deck deckPlayer0, Deck deckPlayer1, boolean wasPlayed, boolean singleRealizationOnly) throws HSException {
        PlayerModel player = boardState.data_.modelForSide(targetSide);
        return this.summonMinion(targetSide, player.getNumMinions(), boardState, wasPlayed, singleRealizationOnly);
    }

    @Deprecated
    protected HearthTreeNode summonMinion_core(PlayerSide targetSide, Minion targetMinion, HearthTreeNode boardState, boolean singleRealizationOnly) throws HSException {
        int targetIndex = boardState.data_.modelForSide(targetSide).getIndexForCharacter(targetMinion);
        return this.summonMinion_core(targetSide, targetIndex, boardState);
    }

    @Deprecated
    protected HearthTreeNode summonMinion_core(PlayerSide targetSide, Minion targetMinion, HearthTreeNode boardState,
                                               Deck deckPlayer0, Deck deckPlayer1, boolean singleRealizationOnly) throws HSException {
        return this.summonMinion_core(targetSide, targetMinion, boardState, singleRealizationOnly);
    }

    /**
     * Place a minion on the board
     *
     * Use this function if you need to place a minion on the board without triggering any of the "On Summon" effects
     *
     * @param targetSide
     * @param targetMinion
     * @param boardState
     * @param singleRealizationOnly
     * @return
     * @throws HSException
     */
    @Deprecated
    public HearthTreeNode placeMinion(PlayerSide targetSide, Minion targetMinion, HearthTreeNode boardState, boolean singleRealizationOnly) throws HSException {
        if (isHero(targetMinion)) {
            boardState.data_.placeMinion(targetSide, this, 0);
        } else {
            PlayerModel targetPlayer = boardState.data_.modelForSide(targetSide);
            boardState.data_.placeMinion(targetSide, this, targetPlayer.getIndexForCharacter(targetMinion));
        }
        return this.notifyMinionPlacement(boardState, targetSide);
    }

    @Deprecated
    public HearthTreeNode placeMinion(PlayerSide targetSide, Minion targetMinion, HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1, boolean singleRealizationOnly) throws HSException {
        return this.placeMinion(targetSide, targetMinion, boardState, singleRealizationOnly);
    }

    @Deprecated
    public HearthTreeNode attack(PlayerSide targetMinionPlayerSide, Minion targetMinion, HearthTreeNode boardState,
                                 Deck deckPlayer0, Deck deckPlayer1, boolean singleRealizationOnly) throws HSException {
        return this.attack(targetMinionPlayerSide, targetMinion, boardState, singleRealizationOnly);
    }

    @Deprecated
    public HearthTreeNode attack(PlayerSide targetMinionPlayerSide, int targetCharacterIndex, HearthTreeNode boardState,
                                 Deck deckPlayer0, Deck deckPlayer1, boolean singleRealizationOnly) throws HSException {
        return this.attack(targetMinionPlayerSide, targetCharacterIndex, boardState, singleRealizationOnly);
    }

    @Deprecated
    protected HearthTreeNode attack_core(PlayerSide targetMinionPlayerSide, Minion targetMinion,
                                         HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1, boolean singleRealizationOnly) throws HSException {
        return this.attack_core(targetMinionPlayerSide, targetMinion, boardState, singleRealizationOnly);
    }

        // ======================================================================================
    // Various notifications
    // ======================================================================================
    @Deprecated
    protected HearthTreeNode notifyMinionSummon(HearthTreeNode boardState, PlayerSide targetSide, Deck deckPlayer0,
                                                    Deck deckPlayer1) throws HSException {
        return this.notifyMinionSummon(boardState, targetSide);
    }

    @Deprecated
    protected HearthTreeNode notifyMinionSummon(HearthTreeNode boardState, PlayerSide targetSide) throws HSException {
        return boardState.notifyMinionSummon(targetSide, this);
    }

    @Deprecated
    protected HearthTreeNode notifyMinionPlacement(HearthTreeNode boardState, PlayerSide targetSide, Deck deckPlayer0,
                                                   Deck deckPlayer1) throws HSException {
        return this.notifyMinionPlacement(boardState, targetSide);
    }

    @Deprecated
    protected HearthTreeNode notifyMinionPlacement(HearthTreeNode boardState, PlayerSide targetSide) throws HSException {
        return boardState.notifyMinionPlacement(targetSide, this);
    }

    @Deprecated
    protected HearthTreeNode notifyMinionPlayed(HearthTreeNode boardState, PlayerSide targetSide, Deck deckPlayer0,
                                                Deck deckPlayer1) throws HSException {
        return this.notifyMinionPlayed(boardState, targetSide);
    }

    @Deprecated
    protected HearthTreeNode notifyMinionPlayed(HearthTreeNode boardState, PlayerSide targetSide) throws HSException {
        return boardState.notifyMinionPlayed(targetSide, this);
    }

    @Deprecated
    protected HearthTreeNode notifyMinionDamaged(HearthTreeNode boardState, PlayerSide targetSide, Deck deckPlayer0,
                                                 Deck deckPlayer1) throws HSException {
        return this.notifyMinionDamaged(boardState, targetSide);
    }

    @Deprecated
    protected HearthTreeNode notifyMinionDamaged(HearthTreeNode boardState, PlayerSide targetSide) throws HSException {
        return boardState.notifyMinionDamaged(targetSide, this);
    }

    @Deprecated
    protected HearthTreeNode notifyMinionDead(PlayerSide deadMinionPlayerSide,
                                              Minion deadMinion, HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1) throws HSException {
        return this.notifyMinionDead(deadMinionPlayerSide, deadMinion, boardState);
    }

    @Deprecated
    protected HearthTreeNode notifyMinionDead(PlayerSide deadMinionPlayerSide, Minion deadMinion, HearthTreeNode boardState) throws HSException {
        return boardState.notifyMinionDead(deadMinionPlayerSide, deadMinion);
    }

    @Deprecated
    protected HearthTreeNode notifyMinionHealed(HearthTreeNode boardState, PlayerSide targetSide, Deck deckPlayer0,
                                                Deck deckPlayer1) throws HSException {
        return this.notifyMinionHealed(boardState, targetSide);
    }

    @Deprecated
    protected HearthTreeNode notifyMinionHealed(HearthTreeNode boardState, PlayerSide targetSide) throws HSException {
        return boardState.notifyMinionHealed(targetSide, this);
    }

    @Deprecated
    public boolean currentPlayerBoardFull(HearthTreeNode boardState) {
        return boardState.data_.getCurrentPlayer().isBoardFull();
    }

}
