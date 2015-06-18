package com.hearthsim.test.groovy.card.classic.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.weapon.FieryWarAxe
import com.hearthsim.card.classic.minion.legendary.CaptainGreenskin
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertNull

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
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        assertNotNull(ret);
        assertNull(ret.data_.modelForSide(CURRENT_PLAYER).getHero().getWeapon())

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(CaptainGreenskin)
                mana(2)
                numCardsUsed(1)
            }
        }
    }

    def "playing Captain Greenskin with weapon"() {
        startingBoard.modelForSide(CURRENT_PLAYER).getHero().setWeapon(new FieryWarAxe());
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayerCardHand(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

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
