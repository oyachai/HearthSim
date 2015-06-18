package com.hearthsim.test.groovy.card.classic.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.weapon.FieryWarAxe
import com.hearthsim.card.classic.minion.common.DreadCorsair
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static org.junit.Assert.assertFalse

class DreadCorsairSpec extends CardSpec {
    HearthTreeNode root
    BoardModel startingBoard

    def setup() {

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([DreadCorsair, FieryWarAxe])
                mana(8)
            }
            waitingPlayer {
                mana(4)
            }
        }

        root = new HearthTreeNode(startingBoard)
    }
    
    def "playing Dread Corsair with no weapon should use up 4 mana"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(DreadCorsair)
                mana(4)
                numCardsUsed(1)
            }
        }
    }
    
    def "playing Dread Corsair with Fiery War Axe should use up 1 mana"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(1)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)
        
        def dreadCorsair = root.data_.getCurrentPlayer().getHand().get(0)
        dreadCorsair.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)
        
        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(DreadCorsair)
                weapon(FieryWarAxe) {
                    weaponCharge(2)
                }
                removeCardFromHand(FieryWarAxe)
                mana(5)
                numCardsUsed(2)
            }
        }
    }
}
