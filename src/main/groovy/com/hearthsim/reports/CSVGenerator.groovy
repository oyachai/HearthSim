package com.hearthsim.reports

import com.hearthsim.PlayerClass
import com.hearthsim.json.registry.ReferenceCardRegistry
import groovy.json.JsonSlurper

class CSVGenerator {

    ReferenceCardRegistry registry
    def cardAugmentation

    CSVGenerator() {
        registry = ReferenceCardRegistry.getInstance()
        def slurper = new JsonSlurper()
        cardAugmentation = slurper.parse(getClass().getResourceAsStream('/card_augmentation.json'))
    }

    def removals() {
        def removalNames = cardAugmentation.findAll { it.attributes.contains 'removal' }.collect { it.name }

        removalNames.collect {
            registry.cardByName it
        }
    }

    def buffs() {
        def removalNames = cardAugmentation.findAll { it.attributes.contains 'buff' }.collect { it.name }

        removalNames.collect {
            registry.cardByName it
        }
    }

    def removalsByPlayerClass(playerClass) {
        removals().findAll { it.playerClass == playerClass || it.playerClass == PlayerClass.NEUTRAL.referenceName }
    }

    def buffsByPlayerClass(playerClass) {
        buffs().findAll { it.playerClass == playerClass || it.playerClass == PlayerClass.NEUTRAL.referenceName }
    }



    def silences() {
        def removalNames = cardAugmentation.findAll { it.attributes.contains 'silence' }.collect { it.name }

        removalNames.collect {
            registry.cardByName it
        }
    }

    def gridForPlayerClass(playerClass) {

        //todo: dump out an empty 'card_augmentation' list
        // dump out the card augmentation list, merging all collectibles w/ existing augmented data

        //todo: move lookups to registry
        def collectibles = registry.collectibles

        def cardsForPlayerClass = collectibles.findAll { it.playerClass == playerClass }
        def minionsForNeutral = collectibles.findAll { it.playerClass == 'Neutral' }
        def spellsForPlayerClass = cardsForPlayerClass.findAll { it.type == 'Spell' }
        def minionsForPlayerClass = cardsForPlayerClass.findAll { it.type == 'Minion' }
        def weaponsForPlayerClass = cardsForPlayerClass.findAll { it.type == 'Weapon' }

        def removalsForPlayerClass = removalsByPlayerClass(playerClass)
        def buffsByPlayerClass = buffsByPlayerClass(playerClass)

        def grid = []
        grid << [playerClass]
        grid << buildManaRow()
        grid << []

        def cardCategories = [
                [name: 'Removals', cards: removalsForPlayerClass],
                [name: 'Buffs', cards: buffsByPlayerClass],
                [name: 'Spells', cards: spellsForPlayerClass],
                [name: 'Minions', cards: minionsForPlayerClass],
                [name: 'Weapons', cards: weaponsForPlayerClass],
                [name: 'Neutral Minions', cards: minionsForNeutral]
        ]

        cardCategories.each { cardCategory ->
            def cards = cardCategory.cards
            grid << ["${cardCategory.name} (${cards.size()})"]
            buildRowsForCards(cards).each {
                grid << it
            }
        }

        grid

    }

    def buildRowsForCards(cards) {
        def cardsByMana = []
        (0..10).each { manaCost ->
            def cardsForMana = cards.findAll { it.cost == manaCost }
            cardsByMana << cardsForMana
        }
        cardsByMana << cards.findAll { it.cost > 10 }

        // find the mana with the highest so we know how many rows we need.
        def requiredRows = 0
        cardsByMana.each { it ->
            if (it.size() > requiredRows) {
                requiredRows = it.size()
            }
        }

        def cardsRows = []
        requiredRows.times { rowCount ->
            def cardRow = []
            cardsRows << cardRow
            (0..11).each { manaCost ->
                def cardsForManaCost = cardsByMana[manaCost]
                if (cardsForManaCost.size() > rowCount) {
                    cardRow << cardsForManaCost[rowCount].name
                } else {
                    cardRow << ''
                }
            }
        }

        cardsRows
    }

    def gridToCSV(grid) {
        def builder = new StringBuilder()
        grid.each { row ->
            builder << row.join(',')
            builder << '\n'
        }
        builder.toString()
    }

    private ArrayList buildManaRow() {
        def manaRow = []
        (0..10).each {
            manaRow << it
        }
        manaRow << '11+'
        manaRow
    }

    public static void main(String[] args) {

        def playerClasses = PlayerClass.values().collect { it.referenceName }
        playerClasses
        def generator = new CSVGenerator()

        playerClasses.each { playerClass ->
            def grid = generator.gridForPlayerClass(playerClass)
            def csv = generator.gridToCSV(grid)

            def file = new File("${playerClass}.csv")
            file.text = csv
        }


    }


}


