package com.hearthsim.test.helpers

import com.hearthsim.card.basic.minion.ElvenArcher
import com.hearthsim.model.BoardModel
import com.hearthsim.model.PlayerSide
import com.hearthsim.test.HearthBaseSpec

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static com.hearthsim.test.IsByteEqual.isByteEqual
import static org.hamcrest.CoreMatchers.*
import static spock.util.matcher.HamcrestSupport.expect

class BoardModelBuidlerSpec extends HearthBaseSpec {

    def "no-param building "() {
        when:
        BoardModel boardModel = new BoardModelBuilder().make {}

        then:
        expect boardModel, notNullValue()
        expect boardModel.currentPlayer, notNullValue()
        expect boardModel.waitingPlayer, notNullValue()
        [CURRENT_PLAYER, WAITING_PLAYER].each { PlayerSide side ->
            def playerModel = boardModel.modelForSide(side)
            //TODO: see if there is an 'isTrue' matcher. better yet one for collections
            //TODO: see if we want to use hamcrest at all...
            assert playerModel.hero != null
            assert playerModel.hero.armor == 0
            assert playerModel.hero.weaponCharge == 0
            assert playerModel.hero.health == 30

            assert playerModel.deck.getNumCards() == 0
            assert playerModel.hand.isEmpty()
            assert playerModel.minions.isEmpty()
            assert playerModel.overload == 0
            assert playerModel.spellDamage == 0
            assert playerModel.mana == 0
            assert playerModel.mana == 0
            assert playerModel.fatigueDamage == 1
        }
    }

    def 'build field from map'() {
        when:
        BoardModel boardModel = new BoardModelBuilder().make {
            currentPlayer {
                field([
                        [mana: 2, attack: 5, maxHealth: 3],
                ])
            }
        }

        then:
        def currentPlayer = boardModel.getCurrentPlayer()
        expect currentPlayer.minions.size(), is(1)

        def firstMinion = currentPlayer.minions[0]
        expect firstMinion.baseManaCost, isByteEqual(2)
        expect firstMinion.attack, isByteEqual(5)
        expect firstMinion.health, isByteEqual(3)
        expect firstMinion.maxHealth, isByteEqual(3)
        expect firstMinion.baseHealth, isByteEqual(3)
    }

    def 'build hand from class'() {
        when:
        BoardModel boardModel = new BoardModelBuilder().make {
            currentPlayer {
                hand([ElvenArcher])
            }
        }

        then:
        def currentPlayer = boardModel.getCurrentPlayer()
        expect currentPlayer.hand.size(), equalTo(1)
        expect currentPlayer.hand[0], isA(ElvenArcher)
    }

    def 'build hero attributes'() {
        when:
        BoardModel boardModel = new BoardModelBuilder().make {
            currentPlayer {
                mana(1)
                fatigueDamage(1)
                overload(1)
            }
        }

        then:
        def currentPlayer = boardModel.modelForSide(CURRENT_PLAYER)
        expect currentPlayer.mana, equalTo((byte)1)
        expect currentPlayer.maxMana, equalTo((byte)1)
        expect currentPlayer.fatigueDamage, equalTo((byte)1)

        def playerModel = currentPlayer
        expect playerModel.overload, isByteEqual(1)
    }

    def 'build current and waiting player'() {
        when:
        BoardModel boardModel = new BoardModelBuilder().make {
            currentPlayer {
                fatigueDamage(1)
            }
            waitingPlayer {
                fatigueDamage(2)
            }
        }

        then:
        expect boardModel.modelForSide(CURRENT_PLAYER).fatigueDamage, equalTo((byte)1)
        expect boardModel.modelForSide(WAITING_PLAYER).fatigueDamage, equalTo((byte)2)
    }


}
