package com.hearthsim.test.helpers

import com.hearthsim.card.Card
import com.hearthsim.card.minion.Minion
import com.hearthsim.model.BoardModel
import com.hearthsim.model.PlayerSide

class BoardModelBuilder {

    private BoardModel boardModel
    private PlayerSide playerSide;

    BoardModel make(Closure definition) {
        if (!boardModel)
            boardModel = new BoardModel()

        runClosure definition

        boardModel
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
                minion.health = health
            } else {
                minion = new Minion("" + 0, (byte) mana, (byte) attack, (byte) health, (byte) attack, (byte) health, (byte) maxHealth)
            }
            boardModel.placeMinion(playerSide, minion);
        }
    }

    private updateMinion(int position, Map options){
        def minion = boardModel.getMinion(playerSide, position)
        if (options.deltaHealth) {
            minion.health += options.deltaHealth
        }
        if (options.deltaAttack) {
            minion.attack += options.deltaAttack
        }
    }

    private fatigueDamage(Number fatigueDamage) {
        boardModel.setFatigueDamage(playerSide, (byte) fatigueDamage)
    }

    private overload(Number overload) {
        boardModel.modelForSide(playerSide).overload = (byte) overload
    }

    private spellDamage(Number spellDamage) {
        boardModel.modelForSide(playerSide).spellDamage = (byte) spellDamage
    }

    private heroHealth(Number health){
        def side = boardModel.modelForSide(playerSide)
        side.hero.health = health
    }

    private mana(Number mana) {
        def model = boardModel.modelForSide(playerSide)
        model.setMana((int) mana)
        //todo: only do this if maxMana hasn't been set explicitly already
        if (model.getMaxMana() == 0)
            model.setMaxMana((int) mana)
    }

    private playMinion(Class<Minion> minionClass) {
        removeCardFromHand(minionClass)
        addMinionToField(minionClass)
    }

    private removeMinion(int index){
        boardModel.removeMinion(playerSide, index)
    }

    private removeCardFromHand(Class card) {
        def hand = boardModel.modelForSide(playerSide).hand
        def cardInHand = hand.find { it.class == card }
        boardModel.removeCardFromHand(cardInHand, playerSide)
    }

    private addMinionToField(Class<Minion> minionClass) {
        Minion minion = minionClass.newInstance()
        minion.hasAttacked(true)
        minion.hasBeenUsed(true)
        def numMinions = boardModel.modelForSide(playerSide).numMinions
        // place the minion at the end by default
        // todo: eventually this will need to be configurable
        boardModel.placeMinion(playerSide, minion, numMinions)
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


