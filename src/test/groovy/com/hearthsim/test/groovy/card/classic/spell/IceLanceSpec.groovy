package com.hearthsim.test.groovy.card.classic.spell

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.KoboldGeomancer
import com.hearthsim.card.basic.minion.WarGolem
import com.hearthsim.card.classic.spell.common.IceLance
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.assertEquals

class IceLanceSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([IceLance])
                mana(1)
            }
            waitingPlayer {
                field([[minion: WarGolem]])
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "freezes unfrozen minion"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayerCardHand(0)
        def ret = theCard.useOn(WAITING_PLAYER, CharacterIndex.MINION_1, root)

        expect:
        assertEquals(root, ret);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(IceLance)
                mana(0)
                numCardsUsed(1)
            }
            waitingPlayer {
                updateMinion(CharacterIndex.MINION_1, [frozen : true])
            }
        }
    }

    def "damages frozen minion"() {
        startingBoard.modelForSide(WAITING_PLAYER).getCharacter(CharacterIndex.MINION_1).setFrozen(true)

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayerCardHand(0)
        def ret = theCard.useOn(WAITING_PLAYER, CharacterIndex.MINION_1, root)

        expect:
        assertEquals(root, ret);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(IceLance)
                mana(0)
                numCardsUsed(1)
            }
            waitingPlayer {
                updateMinion(CharacterIndex.MINION_1, [deltaHealth: -4])
            }
        }
    }

    def "affected by spellpower"() {
        startingBoard.modelForSide(WAITING_PLAYER).getCharacter(CharacterIndex.MINION_1).setFrozen(true)
        startingBoard.placeMinion(CURRENT_PLAYER, new KoboldGeomancer())

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayerCardHand(0)
        def ret = theCard.useOn(WAITING_PLAYER, CharacterIndex.MINION_1, root)

        expect:
        assertEquals(root, ret);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(IceLance)
                mana(0)
                numCardsUsed(1)
            }
            waitingPlayer {
                updateMinion(CharacterIndex.MINION_1, [deltaHealth: -5])
            }
        }
    }
}
