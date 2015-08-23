package com.hearthsim.test.helpers

import com.hearthsim.card.Card
import com.hearthsim.card.CardInHandIndex
import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.minion.Hero
import com.hearthsim.card.minion.Minion
import com.hearthsim.card.minion.MinionMock
import com.hearthsim.card.minion.heroes.TestHero
import com.hearthsim.card.weapon.WeaponCard
import com.hearthsim.model.BoardModel
import com.hearthsim.model.PlayerSide

class BoardModelBuilder {

    private BoardModel boardModel
    private PlayerSide playerSide;

    BoardModel make(Hero hero1, Hero hero2, Closure definition) {
        if (!boardModel)
            boardModel = new BoardModel(hero1, hero2)

        runClosure definition

        boardModel
    }

    BoardModel make(Closure definition) {
        return make(new TestHero("hero0", (byte)30), new TestHero("hero1", (byte)30), definition)
    }

    private hand(List cardsInHand) {
        // currently assuming it's a list of classes
        cardsInHand.each { boardModel.placeCardHand(playerSide, it.newInstance()) }
    }

    private field(List field) {
        field.each {
            def mana = it.mana
            def attack = it.attack
            def maxHealth = it.maxHealth
            def health
            if (it.health) {
                health = it.health
            } else {
                health = maxHealth
            }

            def minion
            if (it.minion) {
                minion = it.minion.newInstance()
                if (health)
                    minion.health = health
                if (it.maxHealth)
                    minion.maxHealth = maxHealth
            } else {
                minion = new MinionMock("" + 0, (byte) mana, (byte) attack, (byte) health, (byte) attack, (byte) health, (byte) maxHealth)
            }
            boardModel.placeMinion(playerSide, minion);
        }
    }
    
    private deck(List cardsInDeck) {
        cardsInDeck.each {
            boardModel.placeCardDeck(playerSide, it.newInstance())
        }
    }

    private updateMinion(int position, Map options) {
        Minion minion = boardModel.modelForSide(playerSide).getCharacter(CharacterIndex.fromInteger(position))
        this.updateMinion(minion, options);
    }

    private updateMinion(CharacterIndex position, Map options) {
        Minion minion = boardModel.modelForSide(playerSide).getCharacter(position)
        this.updateMinion(minion, options);
    }

    private updateMinion(Minion minion, Map options) {
        minion.attack = options.containsKey('attack') ? options.attack : minion.attack;
        minion.attack += options.deltaAttack ? options.deltaAttack : 0;
        minion.extraAttackUntilTurnEnd += options.deltaExtraAttack ? options.deltaExtraAttack : 0;

        minion.health = options.containsKey('health') ? options.health : minion.health;
        minion.maxHealth = options.containsKey('maxHealth') ? options.maxHealth : minion.maxHealth;

        minion.health += options.deltaHealth ? options.deltaHealth : 0;
        minion.maxHealth += options.deltaMaxHealth ? options.deltaMaxHealth : 0;

        minion.auraAttack += options.deltaAuraAttack ? options.deltaAuraAttack : 0;
        minion.auraHealth += options.deltaAuraHealth ? options.deltaAuraHealth : 0;
        
        minion.spellDamage += options.deltaSpellDamage ? options.deltaSpellDamage : 0;
        
        minion.hasAttacked_ = options.containsKey('hasAttacked') ? options.hasAttacked : minion.hasAttacked_
        minion.hasBeenUsed = options.containsKey('hasBeenUsed') ? options.hasBeenUsed : minion.hasBeenUsed
        minion.stealthedUntilNextTurn = options.containsKey('stealthedUntilNextTurn') ? options.stealthedUntilNextTurn : minion.stealthedUntilNextTurn
        minion.stealthedUntilRevealed = options.containsKey('stealthedUntilRevealed') ? options.stealthedUntilRevealed : minion.stealthedUntilRevealed

        minion.charge = options.containsKey('charge') ? options.charge : minion.charge
        minion.frozen = options.containsKey('frozen') ? options.frozen : minion.frozen
        minion.silenced = options.containsKey('silenced') ? options.silenced : minion.silenced
        minion.taunt = options.containsKey('taunt') ? options.taunt : minion.taunt
        minion.cantAttack = options.containsKey('cantAttack') ? options.cantAttack : minion.cantAttack
        minion.immune = options.containsKey('immune') ? options.immune : minion.immune
        minion.divineShield = options.containsKey('divineShield') ? options.divineShield : minion.divineShield
        minion.destroyOnTurnStart = options.containsKey("destroyOnTurnStart") ? options.destroyOnTurnStart : minion.destroyOnTurnStart
    }

    private updateCardInHand(int position, Map options){
        def card = boardModel.getCard_hand(playerSide, CardInHandIndex.fromInteger(position))

        if (card instanceof Minion) {
            this.updateMinion((Minion)card, options);
        }

        card.hasBeenUsed = options.containsKey('hasBeenUsed') ? options.hasBeenUsed : card.hasBeenUsed
        card.manaDelta = options.containsKey('manaDelta') ? options.manaDelta : card.manaDelta
    }

    private fatigueDamage(Number fatigueDamage) {
        playerSide.getPlayer(boardModel).setFatigueDamage((byte) fatigueDamage)
    }

    private overload(Number overload) {
        boardModel.modelForSide(playerSide).overload = (byte) overload
    }

    private windFury(Boolean hasWindFury) {
        boardModel.modelForSide(playerSide).hero.windfury = hasWindFury
    }

    private heroHealth(Number health){
        def side = boardModel.modelForSide(playerSide)
        side.hero.health = health
    }

    private heroHasBeenUsed(Boolean hasBeenUsed){
        def side = boardModel.modelForSide(playerSide)
        side.hero.hasBeenUsed = hasBeenUsed
    }

    private heroArmor(Number armor){
        def side = boardModel.modelForSide(playerSide)
        side.hero.armor = armor
    }

    private heroAttack(Number attack){
        def side = boardModel.modelForSide(playerSide)
        side.hero.attack = attack
    }

    private heroFrozen(Boolean isFrozen){
        def side = boardModel.modelForSide(playerSide)
        side.hero.frozen = isFrozen
    }

    private mana(Number mana) {
        def model = boardModel.modelForSide(playerSide)
        model.setMana((byte) mana)
        //TODO: only do this if maxMana hasn't been set explicitly already
        if (model.getMaxMana() == 0)
            model.setMaxMana((byte) mana)
    }

    private manaUsed(Number mana) {
        def model = boardModel.modelForSide(playerSide)
        model.setMana((byte) (model.getMana() - mana))
    }
    
    private maxMana(Number mana) {
        def model = boardModel.modelForSide(playerSide)
        model.setMaxMana((byte) mana)
    }
        
    private playMinion(Class<Minion> minionClass) {
        removeCardFromHand(minionClass)
        addMinionToField(minionClass)
    }

    private playMinion(Class<Minion> minionClass, CharacterIndex placementIndex) {
        removeCardFromHand(minionClass)
        addMinionToField(minionClass, placementIndex)
    }
    
    private playMinionWithCharge(Class<Minion> minionClass) {
        removeCardFromHand(minionClass)
        addMinionToField(minionClass, false, true)
    }

    private removeMinion(CharacterIndex index){
        boardModel.removeMinion(playerSide, index)
    }
    
    private addCardToHand(Class cardClass) {
        Card card = cardClass.newInstance()
        boardModel.placeCardHand(playerSide, card)
    }
    
    private removeCardFromHand(Class card) {
        def hand = boardModel.modelForSide(playerSide).hand
        def cardInHand = hand.find { it.class == card }
        boardModel.removeCardFromHand(cardInHand, playerSide)
    }

    private addMinionToField(Class<Minion> minionClass) {
        addMinionToField(minionClass, true, true)
    }

    private addMinionToField(Class<Minion> minionClass, CharacterIndex placementIndex) {
        addMinionToField(minionClass, true, true, placementIndex)
    }
    
    private addMinionToField(Class<Minion> minionClass, boolean hasAttacked, boolean hasBeenUsed) {
        def numMinions = boardModel.modelForSide(playerSide).numMinions
        addMinionToField(minionClass, hasAttacked, hasBeenUsed, CharacterIndex.fromInteger(numMinions))
    }
        
    private addMinionToField(Class<Minion> minionClass, boolean hasAttacked, boolean hasBeenUsed, CharacterIndex placementIndex) {
        Minion minion = minionClass.newInstance()
        minion.hasAttacked(hasAttacked)
        minion.hasBeenUsed(hasBeenUsed)
        boardModel.placeMinion(playerSide, minion, placementIndex)
    }

    private heroHasAttacked(Boolean hasAttacked){
        def side = boardModel.modelForSide(playerSide)
        side.hero.hasAttacked(hasAttacked)
    }

    private weapon(Class<WeaponCard> weaponCard, Closure weaponClosure){
        def side = boardModel.modelForSide(playerSide)
        if (weaponCard == null) {
            side.hero.destroyWeapon();
        } else {
            side.hero.weapon = weaponCard.newInstance()
            side.hero.weapon.hasBeenUsed(true)
        }

        runClosure weaponClosure
    }

    private weaponCharge(Number charge){
        def side = boardModel.modelForSide(playerSide)
        side.hero.weapon.weaponCharge = charge
    }

    private weaponDamage(Number damage){
        def side = boardModel.modelForSide(playerSide)
        side.hero.weapon.weaponDamage = damage
    }

    private addDeckPos(Number num) {
        if (playerSide == PlayerSide.CURRENT_PLAYER) 
            boardModel.currentPlayer.deckPos += (int) num
        else 
            boardModel.waitingPlayer.deckPos += (int) num
    }

    private numCardsUsed(Number num) {
        if (playerSide == PlayerSide.CURRENT_PLAYER)
            boardModel.currentPlayer.numCardsUsed = (byte) num;
        else
            boardModel.waitingPlayer.numCardsUsed = (byte) num;
    }

    private currentPlayer(Closure player) {
        playerSide = PlayerSide.CURRENT_PLAYER

        runClosure player
    }

    private waitingPlayer(Closure player) {
        playerSide = PlayerSide.WAITING_PLAYER

        runClosure player
    }


    private runClosure(Closure runClosure) {
        // Create clone of closure for threading access.
        Closure runClone = runClosure.clone()

        // Set delegate of closure to this builder.
        runClone.delegate = this

        // And only use this builder as the closure delegate.
        runClone.resolveStrategy = Closure.DELEGATE_ONLY

        // Run closure code.
        runClone()
    }

}


