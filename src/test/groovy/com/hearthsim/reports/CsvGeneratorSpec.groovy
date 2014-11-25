package com.hearthsim.reports

import spock.lang.Specification

class CsvGeneratorSpec extends Specification {

    def "reports known removals"() {

        when:
        def generator = new CSVGenerator()

        def removals = generator.removals()
        then:
        removals.any{it.name =='Fireball'}
        removals.any{it.name =='Fireball'}

    }

    def "reports known aoe"() {

    }

    def "reports known buffs"() {

    }

    def "reports known silence"() {

        when:
        def generator = new CSVGenerator()

        def removals = generator.silences()
        then:
        removals.any{it.name =='Keeper of the Grove'}

    }

    def "reports known heals"() {

    }

    def "cards are associated to heroes correctly"() {

        when:
        def generator = new CSVGenerator()
        def mageRemovals = generator.removalsByPlayerClass('Mage')
        def druidRemovals = generator.removalsByPlayerClass('Druid')

        then:
        mageRemovals.any{it.name =='Fireball'}
        !druidRemovals.any{it.name =='Fireball'}
        druidRemovals.any{it.name =='Keeper of the Grove'}

    }

    def 'produces expected grid'(){

        when:
        def generator = new CSVGenerator()
        def grid = generator.gridForPlayerClass('Druid')

        then:
        grid[0][0] == 'Druid'
        grid[2][0] == 'Spells'

    }


}
