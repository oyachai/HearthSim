package com.hearthsim.test.groovy.card.weapon

import com.hearthsim.card.minion.concrete.BloodfenRaptor
import com.hearthsim.card.weapon.concrete.SwordOfJustice
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER

class SwordOfJusticeSpec extends CardSpec{

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([SwordOfJustice, BloodfenRaptor])
                mana(5)
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def 'removes durability and gives +1/+1 to minion on summon'() {
        def copiedBoard = startingBoard.deepCopy()
        def copiedRoot = new HearthTreeNode(copiedBoard)
        def swordOfJustice = copiedBoard.getCurrentPlayerCardHand(0);
        def ret = swordOfJustice.useOn(CURRENT_PLAYER, 0, copiedRoot, null, null);
        def raptor = copiedBoard.getCurrentPlayerCardHand(0);
        ret = raptor.useOn(CURRENT_PLAYER, 0, ret, null, null);

        expect:
        ret != null
        assertBoardDelta(startingBoard, copiedBoard) {
            currentPlayer {
                weapon(SwordOfJustice) {
                    weaponCharge(4)
                }
                playMinion(BloodfenRaptor)
                updateMinion(0, [deltaHealth: +1])
                updateMinion(0, [deltaAttack: +1])
                mana(0)
                removeCardFromHand(SwordOfJustice)
            }
        }


    }

}
