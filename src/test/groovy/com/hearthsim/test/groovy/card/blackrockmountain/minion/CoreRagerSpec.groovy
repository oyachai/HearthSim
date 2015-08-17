package com.hearthsim.test.groovy.card.blackrockmountain.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.blackrockmountain.minion.rare.CoreRager
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER

class CoreRagerSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def "gain +3/+3 when hand is empty"() {

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([CoreRager])
                mana(10)
            }
            waitingPlayer {
                mana(10)
            }
        }

        root = new HearthTreeNode(startingBoard)
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null;

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(CoreRager)
                mana(6)
                numCardsUsed(1)
                updateMinion(CharacterIndex.MINION_1, [deltaHealth: 3, deltaAttack: 3, deltaMaxHealth: 3])
            }
        }

    }

    def "no gain when hand is not empty"() {

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([CoreRager, CoreRager])
                mana(10)
            }
            waitingPlayer {
                mana(10)
            }
        }

        root = new HearthTreeNode(startingBoard)
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null;

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(CoreRager)
                mana(6)
                numCardsUsed(1)
                updateMinion(CharacterIndex.MINION_1, [deltaHealth: 0, deltaAttack: 0, deltaMaxHealth: 0])
            }
        }

    }

}
