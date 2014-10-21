package com.hearthsim.test.json

import com.hearthsim.card.ImplementedCardList
import com.hearthsim.json.registry.CardRegistry
import groovy.util.logging.Slf4j
import spock.lang.Ignore
import spock.lang.Specification

@Slf4j
class ImplementedCardSpec extends Specification {

    @Ignore
    def 'all collectible cards implemented'() {
        when:
        def list = new ImplementedCardList()
        def registry = CardRegistry.instance

        then:

        def missingCards = []
        registry.collectibles.each { cardDefinition ->
            def card = list.list_.find {
                it.name_ == cardDefinition.name
            }
            if (!card)
                missingCards << cardDefinition
        }

        missingCards.each {
            log.warn it.toString()
        }

        registry.cardDefinitions.size() == list.cardList.size()
    }

}
