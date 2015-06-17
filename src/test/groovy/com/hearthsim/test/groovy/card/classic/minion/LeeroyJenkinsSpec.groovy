package com.hearthsim.test.groovy.card.classic.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.classic.minion.common.Whelp
import com.hearthsim.card.classic.minion.legendary.LeeroyJenkins
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static org.junit.Assert.assertFalse

class LeeroyJenkinsSpec extends CardSpec {
    
    HearthTreeNode root
    BoardModel startingBoard

    def setup() {

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([LeeroyJenkins])
                mana(7)
            }
            waitingPlayer {
                mana(4)
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "playing Leeroy Jenkins"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:

        assertFalse(ret == null)
        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinionWithCharge(LeeroyJenkins)
                mana(2)
                numCardsUsed(1)
            }
            waitingPlayer {
                playMinion(Whelp)
                playMinion(Whelp)
            }
        }
    }
}
