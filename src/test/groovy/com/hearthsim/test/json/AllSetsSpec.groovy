package com.hearthsim.test.json

import com.hearthsim.json.registry.ReferenceCardRegistry
import spock.lang.Ignore
import spock.lang.Specification

class AllSetsSpec extends Specification {

    @Ignore
    def "correct number of neutrals in registry with correct cost"() {
        when:
        def instance = ReferenceCardRegistry.getInstance()

        then:
        instance.minionsByManaCostAndClass(cost, hero).size() == count

        where:
        cost | count | hero
        0    | 2     | 'Neutral'
        1    | 25    | 'Neutral'
        2    | 46    | 'Neutral'
        3    | 49    | 'Neutral'
        4    | 32    | 'Neutral'
        5    | 35    | 'Neutral'
        6    | 25    | 'Neutral'
        7    | 8     | 'Neutral'
        8    | 7     | 'Neutral'
        9    | 8     | 'Neutral'
        10   | 2     | 'Neutral'
        12   | 2     | 'Neutral'
        20   | 1     | 'Neutral'

        0    | 0     | 'Druid'
        1    | 0     | 'Druid'
        2    | 1     | 'Druid'
        3    | 2     | 'Druid'
        4    | 1     | 'Druid'
        5    | 2     | 'Druid'
        6    | 1     | 'Druid'
        7    | 3     | 'Druid'
        8    | 1     | 'Druid'
        9    | 2     | 'Druid'
        10   | 0     | 'Druid'

        0    | 0     | 'Hunter'
        1    | 2     | 'Hunter'
        2    | 2     | 'Hunter'
        3    | 1     | 'Hunter'
        4    | 2     | 'Hunter'
        5    | 3     | 'Hunter'
        6    | 1     | 'Hunter'
        7    | 1     | 'Hunter'
        8    | 0     | 'Hunter'
        9    | 1     | 'Hunter'
        10   | 0     | 'Hunter'

        0    | 0     | 'Mage'
        1    | 1     | 'Mage'
        2    | 2     | 'Mage'
        3    | 3     | 'Mage'
        4    | 4     | 'Mage'
        5    | 0     | 'Mage'
        6    | 0     | 'Mage'
        7    | 2     | 'Mage'
        8    | 0     | 'Mage'
        9    | 0     | 'Mage'
        10   | 0     | 'Mage'

        0    | 0     | 'Paladin'
        1    | 0     | 'Paladin'
        2    | 2     | 'Paladin'
        3    | 2     | 'Paladin'
        4    | 0     | 'Paladin'
        5    | 4     | 'Paladin'
        6    | 0     | 'Paladin'
        7    | 1     | 'Paladin'
        8    | 1     | 'Paladin'
        9    | 0     | 'Paladin'
        10   | 0     | 'Paladin'

        0    | 0     | 'Priest'
        1    | 3     | 'Priest'
        2    | 3     | 'Priest'
        3    | 1     | 'Priest'
        4    | 2     | 'Priest'
        5    | 2     | 'Priest'
        6    | 2     | 'Priest'
        7    | 1     | 'Priest'
        8    | 0     | 'Priest'
        9    | 0     | 'Priest'
        10   | 0     | 'Priest'

        0    | 0     | 'Rogue'
        1    | 0     | 'Rogue'
        2    | 4     | 'Rogue'
        3    | 3     | 'Rogue'
        4    | 2     | 'Rogue'
        5    | 2     | 'Rogue'
        6    | 2     | 'Rogue'
        7    | 0     | 'Rogue'
        8    | 0     | 'Rogue'
        9    | 0     | 'Rogue'
        10   | 0     | 'Rogue'

        0    | 0     | 'Shaman'
        1    | 1     | 'Shaman'
        2    | 3     | 'Shaman'
        3    | 2     | 'Shaman'
        4    | 4     | 'Shaman'
        5    | 1     | 'Shaman'
        6    | 1     | 'Shaman'
        7    | 1    | 'Shaman'
        8    | 1     | 'Shaman'
        9    | 0     | 'Shaman'
        10   | 0     | 'Shaman'

        0    | 0     | 'Warlock'
        1    | 3     | 'Warlock'
        2    | 2     | 'Warlock'
        3    | 3     | 'Warlock'
        4    | 4     | 'Warlock'
        5    | 2     | 'Warlock'
        6    | 2     | 'Warlock'
        7    | 0     | 'Warlock'
        8    | 0     | 'Warlock'
        9    | 2     | 'Warlock'
        10   | 0     | 'Warlock'

        0    | 0     | 'Warrior'
        1    | 1     | 'Warrior'
        2    | 2     | 'Warrior'
        3    | 2     | 'Warrior'
        4    | 4     | 'Warrior'
        5    | 1     | 'Warrior'
        6    | 2     | 'Warrior'
        7    | 0     | 'Warrior'
        8    | 1     | 'Warrior'
        9    | 0     | 'Warrior'
        10   | 0     | 'Warrior'
    }

    @Ignore
    def "correct number of spells by hero"() {
        when:
        def instance = ReferenceCardRegistry.getInstance()

        then:
        instance.spellsByManaCostAndClass(cost, hero).size() == count

        where:
        cost | count | hero
        0    | 0     | 'Neutral'
        1    | 0     | 'Neutral'
        2    | 0     | 'Neutral'
        3    | 0     | 'Neutral'
        4    | 0     | 'Neutral'
        5    | 0     | 'Neutral'
        6    | 0     | 'Neutral'
        7    | 0     | 'Neutral'
        8    | 0     | 'Neutral'
        9    | 0     | 'Neutral'
        10   | 0     | 'Neutral'
        12   | 0     | 'Neutral'
        20   | 0     | 'Neutral'

        0    | 2     | 'Druid'
        1    | 3     | 'Druid'
        2    | 4     | 'Druid'
        3    | 3     | 'Druid'
        4    | 4     | 'Druid'
        5    | 2     | 'Druid'
        6    | 4     | 'Druid'
        7    | 0     | 'Druid'
        8    | 0     | 'Druid'
        9    | 1     | 'Druid'
        10   | 0     | 'Druid'

        0    | 1     | 'Hunter'
        1    | 3     | 'Hunter'
        2    | 4     | 'Hunter'
        3    | 4     | 'Hunter'
        4    | 1     | 'Hunter'
        5    | 2     | 'Hunter'
        6    | 0     | 'Hunter'
        7    | 0     | 'Hunter'
        8    | 0     | 'Hunter'
        9    | 0     | 'Hunter'
        10   | 0     | 'Hunter'

        0    | 0     | 'Mage'
        1    | 3     | 'Mage'
        2    | 4     | 'Mage'
        3    | 2     | 'Mage'
        4    | 4     | 'Mage'
        5    | 1     | 'Mage'
        6    | 1     | 'Mage'
        7    | 1     | 'Mage'
        8    | 0     | 'Mage'
        9    | 0     | 'Mage'
        10   | 1     | 'Mage'

        0    | 0     | 'Paladin'
        1    | 4     | 'Paladin'
        2    | 3     | 'Paladin'
        3    | 2     | 'Paladin'
        4    | 3     | 'Paladin'
        5    | 3     | 'Paladin'
        6    | 1     | 'Paladin'
        7    | 0     | 'Paladin'
        8    | 1     | 'Paladin'
        9    | 0     | 'Paladin'
        10   | 0     | 'Paladin'

        0    | 2     | 'Priest'
        1    | 5     | 'Priest'
        2    | 4     | 'Priest'
        3    | 4     | 'Priest'
        4    | 3     | 'Priest'
        5    | 1     | 'Priest'
        6    | 2     | 'Priest'
        7    | 0     | 'Priest'
        8    | 0     | 'Priest'
        9    | 0     | 'Priest'
        10   | 1     | 'Priest'

        0    | 3     | 'Rogue'
        1    | 4     | 'Rogue'
        2    | 6     | 'Rogue'
        3    | 2     | 'Rogue'
        4    | 2     | 'Rogue'
        5    | 1     | 'Rogue'
        6    | 1     | 'Rogue'
        7    | 1     | 'Rogue'
        8    | 0     | 'Rogue'
        9    | 0     | 'Rogue'
        10   | 0     | 'Rogue'

        0    | 2     | 'Shaman'
        1    | 5     | 'Shaman'
        2    | 5     | 'Shaman'
        3    | 5     | 'Shaman'
        4    | 1     | 'Shaman'
        5    | 1     | 'Shaman'
        6    | 0     | 'Shaman'
        7    | 0     | 'Shaman'
        8    | 0     | 'Shaman'
        9    | 0     | 'Shaman'
        10   | 0     | 'Shaman'

        0    | 1     | 'Warlock'
        1    | 4     | 'Warlock'
        2    | 2     | 'Warlock'
        3    | 4     | 'Warlock'
        4    | 3     | 'Warlock'
        5    | 2     | 'Warlock'
        6    | 1     | 'Warlock'
        7    | 0     | 'Warlock'
        8    | 1     | 'Warlock'
        9    | 0     | 'Warlock'
        10   | 0     | 'Warlock'

        0    | 1     | 'Warrior'
        1    | 4     | 'Warrior'
        2    | 7     | 'Warrior'
        3    | 3     | 'Warrior'
        4    | 1     | 'Warrior'
        5    | 1     | 'Warrior'
        6    | 0     | 'Warrior'
        7    | 1     | 'Warrior'
        8    | 0     | 'Warrior'
        9    | 0     | 'Warrior'
        10   | 0     | 'Warrior'
    }

    def 'able to find card by name '(){
        when:
        def instance = ReferenceCardRegistry.getInstance()

        then:
        def definition = instance.cardByName('Abomination')
        definition.id=='EX1_097'
    }

}
