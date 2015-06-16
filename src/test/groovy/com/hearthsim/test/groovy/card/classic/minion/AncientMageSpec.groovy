package com.hearthsim.test.groovy.card.classic.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.classic.minion.rare.AncientMage
import com.hearthsim.card.goblinsvsgnomes.minion.common.SaltyDog
import com.hearthsim.card.goblinsvsgnomes.minion.rare.SootSpewer
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static org.junit.Assert.assertFalse

class AncientMageSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([AncientMage])
                field([[minion:SaltyDog],[minion:SootSpewer],[minion:SaltyDog]])
                mana(7)
            }
            waitingPlayer {
                field([[minion:SaltyDog],[minion:SootSpewer],[minion:SaltyDog]])
                mana(4)
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "playing Ancient Mage on the left edge"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(AncientMage, CharacterIndex.HERO)
                mana(3)
                updateMinion(CharacterIndex.MINION_2, [deltaSpellDamage: 1])
                numCardsUsed(1)
            }
        }

    }

    def "playing Ancient Mage the middle"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.MINION_1, root)

        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(AncientMage, CharacterIndex.MINION_1)
                mana(3)
                updateMinion(CharacterIndex.MINION_1, [deltaSpellDamage: 1])
                updateMinion(CharacterIndex.MINION_3, [deltaSpellDamage: 1])
                numCardsUsed(1)
            }
        }

    }
    def "playing Ancient Mage on the right edge"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.MINION_3, root)

        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(AncientMage, CharacterIndex.MINION_3)
                mana(3)
                updateMinion(CharacterIndex.MINION_3, [deltaSpellDamage: 1])
                numCardsUsed(1)
            }
        }

    }

    def "playing Ancient Mage on empty board"() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([AncientMage])
                mana(7)
            }
        }
        root = new HearthTreeNode(startingBoard)

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(AncientMage)
                mana(3)
                numCardsUsed(1)
            }
        }
    }
}
