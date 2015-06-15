package com.hearthsim.test.groovy.card.classic.minion

import com.hearthsim.card.classic.minion.epic.PatientAssassin
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.assertFalse

class PatientAssassinSpec extends CardSpec {
    
    HearthTreeNode root
    BoardModel startingBoard

    def setup() {

        def minionMana = 2;
        def attack = 5;
        def health = 3;

        def commonField = [
            [mana: minionMana, attack: attack, maxHealth: health],
        ]

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([PatientAssassin])
                field([[minion: PatientAssassin]])
                mana(7)
            }
            waitingPlayer {
                field(commonField)
                mana(4)
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "playing Patient Assassin and attacking the Hero with it"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, 1, root)

        def patientAssassin = ret.data_.modelForSide(CURRENT_PLAYER).getCharacter(1)
        def ret2 = patientAssassin.attack(WAITING_PLAYER, 0, ret)

        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret2.data_) {
            currentPlayer {
                playMinion(PatientAssassin)
                mana(5)
                updateMinion(0, [hasAttacked: true, stealthed: false])
                numCardsUsed(1)
            }
            waitingPlayer {
                heroHealth(29)
            }
        }
    }
    
    def "playing Patient Assassin and attacking a minion with it"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, 1, root)

        def patientAssassin = ret.data_.modelForSide(CURRENT_PLAYER).getCharacter(1)
        def ret2 = patientAssassin.attack(WAITING_PLAYER, 1, ret)

        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret2.data_) {
            currentPlayer {
                playMinion(PatientAssassin)
                mana(5)
                removeMinion(0)
                numCardsUsed(1)
            }
            waitingPlayer {
                removeMinion(0)
            }
        }
    }
}
