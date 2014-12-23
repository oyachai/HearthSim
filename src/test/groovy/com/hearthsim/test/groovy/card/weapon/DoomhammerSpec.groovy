package com.hearthsim.test.groovy.card.weapon

import com.hearthsim.card.weapon.concrete.Doomhammer
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER

class DoomhammerSpec extends CardSpec{

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([Doomhammer])
                mana(5)
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def 'gives windfury and overload'(){
        def copiedBoard = startingBoard.deepCopy()
        def copiedRoot = new HearthTreeNode(copiedBoard)
        def theCard = copiedBoard.getCurrentPlayerCardHand(0);
        def ret = theCard.useOn(CURRENT_PLAYER, 0, copiedRoot, null, null);

        expect:
        ret != null
        assertBoardDelta(startingBoard, copiedBoard) {
            currentPlayer {
                mana(0)
                overload(2)
                heroAttack(2)
                windFury(true)
                weapon(Doomhammer) {
                    weaponCharge(8)
                }
                removeCardFromHand(Doomhammer)
            }
        }

    }

}
