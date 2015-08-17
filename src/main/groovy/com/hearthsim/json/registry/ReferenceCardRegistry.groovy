package com.hearthsim.json.registry

import groovy.json.JsonSlurper

class ReferenceCardRegistry {

    private static ReferenceCardRegistry instance

    private final static List<String> disabledCards = [
            'Adrenaline Rush'
    ]

    List<ReferenceCard> cardDefinitions = []

    public synchronized static ReferenceCardRegistry getInstance() {
        if (!instance) {
            instance = new ReferenceCardRegistry()
        }
        return instance
    }

    private ReferenceCardRegistry() {

        def setNames = [
                'Basic',
                'Classic',
                'Missions',
                'Promotion',
                'System',
                'Credits',
                'Reward',
                'Curse of Naxxramas',
                'Goblins vs Gnomes',
                'Blackrock Mountain',
                'The Grand Tournament',
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

                cardDefinitions << new ReferenceCard(
                        id: card.id,
                        type: card.type,
                        name: card.name,
                        set: setName,
                        playerClass: playerClass,
                        cost: card.cost,
                        attack: card.attack,
                        health: card.health,
                        durability: card.durability,
                        text: card.text,
                        rarity: card.rarity,
                        mechanics: card.mechanics,
                        collectible: card.collectible, //TODO: let's add some tests for these conditions
                        race: card.race
                )
            }

        }
    }

    public List<ReferenceCard> minionsByManaCostAndClass(int cost, String playerClass) {
        cardDefinitions.findAll {
            it.collectible && it.cost == cost && it.playerClass == playerClass && it.type == 'Minion'
        }
    }

    public List<ReferenceCard> spellsByManaCostAndClass(int cost, String playerClass) {
        cardDefinitions.findAll {
            it.collectible && it.cost == cost && it.playerClass == playerClass && !it.mechanics?.contains('Secret') && it.type == 'Spell'
        }
    }

    public ReferenceCard cardByName(String name) {
        cardDefinitions.find { it.name == name && (it.type != 'Hero' || it.collectible) }
    }

    public ReferenceCard cardByNameAndType(String name, String type) {
        cardDefinitions.find { it.name == name && it.type == type && (it.type != 'Hero' || it.collectible) }
    }

    public ReferenceCard cardById(String id) {
        cardDefinitions.find { it.id == id }
    }

    public List<ReferenceCard> getCollectibles(){
        cardDefinitions.findAll { it.collectible }
    }

}
