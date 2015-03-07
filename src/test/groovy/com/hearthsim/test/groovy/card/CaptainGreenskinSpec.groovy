package com.hearthsim.test.groovy.card


import com.hearthsim.card.minion.concrete.CaptainGreenskin
import com.hearthsim.card.weapon.concrete.FieryWarAxe
import com.hearthsim.model.BoardModel
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static org.junit.Assert.*

class CaptainGreenskinSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([CaptainGreenskin])
                mana(7)
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "playing Captain Greenskin without weapon"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayerCardHand(0)
        def ret = theCard.useOn(CURRENT_PLAYER, 0, root, null, null)

        expect:
        assertNotNull(ret);
        assertNull(ret.data_.getCurrentPlayerHero().getWeapon())

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(CaptainGreenskin)
                mana(2)
                numCardsUsed(1)
            }
        }
    }

    def "playing Captain Greenskin with weapon"() {
        startingBoard.getCurrentPlayerHero().setWeapon(new FieryWarAxe());
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayerCardHand(0)
        def ret = theCard.useOn(CURRENT_PLAYER, 0, root, null, null)

        expect:
        assertNotNull(ret);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(CaptainGreenskin)
                mana(2)
                weaponDamage(4)
                weaponCharge(3)
                numCardsUsed(1)
            }
        }
    }
}
