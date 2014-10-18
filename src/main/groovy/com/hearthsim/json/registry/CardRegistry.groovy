package com.hearthsim.json.registry

import groovy.json.JsonSlurper

class CardRegistry {

    private static CardRegistry instance

    private final static List<String> disabledCards = [
            'Adrenaline Rush'
    ]

    List<CardDefinition> cardDefinitions = []

    public synchronized static CardRegistry getInstance() {
        if (!instance) {
            instance = new CardRegistry()
        }
        return instance
    }

    private CardRegistry() {

        def setNames = [
                'Basic',
                'Expert',
                'Missions',
                'Promotion',
                'System',
                'Credits',
                'Reward',
                'Curse of Naxxramas',
                'Debug'
        ]

        def slurper = new JsonSlurper()

        def allSets = slurper.parse(getClass().getResourceAsStream('/AllSets.json'))

        setNames.each { setName ->
            extractCard(allSets, setName)
        }
    }

    private void extractCard(allSets, setName) {
        def inSet = allSets.find { it.key == setName }
        def cards = inSet.value


        cards.each { card ->

            if (!disabledCards.contains(card.name) && !(card.type == 'Enchantment')) {

                def playerClass = card.playerClass != null ? card.playerClass.toString() : 'Neutral'

                cardDefinitions << new CardDefinition(
                        id: card.id,
                        type: card.type,
                        name: card.name,
                        set: setName,
                        playerClass: playerClass,
                        cost: card.cost,
                        attack: card.attack ?: -1,
                        health: card.health ?: -1,
                        text: card.text,
                        rarity: card.rarity,
                        mechanics: card.mechanics,
                        collectible: card.collectible
                )


            }

        }
    }

    public List<CardDefinition> minionsByManaCostAndClass(int cost, String playerClass) {
        cardDefinitions.findAll {
            it.collectible && it.cost == cost && it.playerClass == playerClass && it.type == 'Minion'
        }
    }

    public List<CardDefinition> spellsByManaCostAndClass(int cost, String playerClass) {
        cardDefinitions.findAll {
            it.collectible && it.cost == cost && it.playerClass == playerClass && !it.mechanics?.contains('Secret') && it.type == 'Spell'
        }
    }

    public CardDefinition cardByName(String name) {
        cardDefinitions.find { it.name == name }
    }

}
