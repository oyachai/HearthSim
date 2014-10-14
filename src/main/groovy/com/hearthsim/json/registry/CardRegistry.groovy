package com.hearthsim.json.registry

import groovy.json.JsonSlurper

class CardRegistry {

    private static CardRegistry instance

    private final static List<String> disabledCards = [
            'Adrenaline Rush'
    ]

    private List<MinionDefinition> minionDefinitions = []
    private List<CardDefinition> spellDefinitions = []

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

            if (card.collectible && !disabledCards.contains(card.name)) {

                def playerClass = card.playerClass != null ? card.playerClass.toString() : 'Neutral'

                if (card.type == 'Minion') {
                    minionDefinitions << new MinionDefinition(
                            name: card.name,
                            set: setName,
                            playerClass: playerClass,
                            cost: card.cost,
                            attack: card.attack,
                            health: card.health,
                            text: card.text,
                            mechanics: card.mechanics
                    )
                } else if (card.type == 'Spell') {
                    spellDefinitions << new CardDefinition(
                            name: card.name,
                            type: card.type,
                            set: setName,
                            playerClass: playerClass,
                            cost: card.cost,
                            text: card.text,
                            mechanics: card.mechanics
                    )
                }


            }

        }
    }

    public List<MinionDefinition> minionsByManaCostAndClass(int cost, String playerClass) {
        minionDefinitions.findAll {
            it.cost == cost && it.playerClass == playerClass
        }
    }

    public List<MinionDefinition> spellsByManaCostAndClass(int cost, String playerClass) {
        spellDefinitions.findAll {
            it.cost == cost && it.playerClass == playerClass && !it.mechanics?.contains('Secret')
        }
    }
}
