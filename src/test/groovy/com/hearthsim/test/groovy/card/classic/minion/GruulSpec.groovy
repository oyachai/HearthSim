package com.hearthsim.test.groovy.card.classic.minion

import com.hearthsim.Game
import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.classic.minion.legendary.Gruul
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static org.junit.Assert.assertFalse

class GruulSpec extends CardSpec {
    HearthTreeNode root
    BoardModel startingBoard

    def setup() {

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([Gruul])
                mana(8)
            }
            waitingPlayer {
                mana(4)
            }
        }

        root = new HearthTreeNode(startingBoard)
    }
    
    def "playing Gruul"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(Gruul)
                mana(0)
                numCardsUsed(1)
            }
        }

        def retAfterEndTurn = new HearthTreeNode(Game.endTurn(ret.data_))
        assertBoardDelta(copiedBoard, retAfterEndTurn.data_) {
            currentPlayer {
                playMinion(Gruul)
                mana(0)
                updateMinion(CharacterIndex.MINION_1, [deltaHealth: 1, deltaAttack: 1, deltaMaxHealth: 1])
                numCardsUsed(1)
            }
        }

    }
    
}
