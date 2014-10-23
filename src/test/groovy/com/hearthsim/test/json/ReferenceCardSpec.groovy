package com.hearthsim.test.json

import com.hearthsim.card.ImplementedCardList
import com.hearthsim.json.registry.ReferenceCardRegistry
import groovy.util.logging.Slf4j
import spock.lang.Ignore
import spock.lang.Specification

@Slf4j
class ReferenceCardSpec extends Specification {

    @Ignore
    def 'all collectible cards implemented'() {
        // let's count by weapon etc.

        when:
        def list = new ImplementedCardList()
        def registry = ReferenceCardRegistry.instance

        then:

        def missingCards = []
        registry.collectibles.each { cardDefinition ->
            def card = list.list_.find {
                it.name_ == cardDefinition.name
            }
            if (!card)
                missingCards << cardDefinition
        }

        log.warn "---MISSING WEAPONS---"
        def weapons = missingCards.findAll{ it.type == 'Weapon' }
        weapons.each {
            log.warn it.toString()
        }

        log.warn "---MISSING MINIONS---"
        def minions = missingCards.findAll{ it.type == 'Minion' }
        minions.each {
            log.warn it.toString()
        }

        log.warn "---MISSING SPELLS (NON-SECRET)---"
        def spells = missingCards.findAll{ it.type == 'Spell' && !it.mechanics?.contains ('Secret') }
        spells.each {
            log.warn it.toString()
        }

        log.warn "---MISSING SPELLS (SECRET)---"
        def secrets = missingCards.findAll{ it.type == 'Spell' && it.mechanics?.contains ('Secret') }
        secrets.each {
            log.warn it.toString()
        }


        registry.collectibles.size() == list.cardList.size()
    }

}
