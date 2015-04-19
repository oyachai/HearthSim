package com.hearthsim.test.groovy.card.classic.spell

import com.hearthsim.card.basic.minion.BoulderfistOgre
import com.hearthsim.card.basic.minion.KoboldGeomancer
import com.hearthsim.card.basic.spell.Claw
import com.hearthsim.card.classic.spell.rare.Savagery
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER

class SavagerySpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([Claw, Savagery])
                mana(10)
            }
            waitingPlayer {
                field([[minion:BoulderfistOgre]])
            }
        }

        root = new HearthTreeNode(startingBoard)
        def theCard = startingBoard.getCurrentPlayer().getHand().get(0);
        root = theCard.useOn(CURRENT_PLAYER, 0, root);
        startingBoard = root.data_;
    }

    def 'deals damage'(){
        def copiedBoard = startingBoard.deepCopy()
        def copiedRoot = new HearthTreeNode(copiedBoard)
        def theCard = copiedBoard.getCurrentPlayer().getHand().get(0);
        def ret = theCard.useOn(WAITING_PLAYER, 1, copiedRoot);

        expect:
        ret != null

        assertBoardDelta(startingBoard, copiedBoard) {
            currentPlayer {
                mana(8)
                removeCardFromHand(Savagery)
                numCardsUsed(2)
            }
            waitingPlayer {
                updateMinion(0, [deltaHealth: -2])
            }
        }
    }

    def 'affected by spellpower'(){
        startingBoard.placeMinion(CURRENT_PLAYER, new KoboldGeomancer())

        def copiedBoard = startingBoard.deepCopy()
        def copiedRoot = new HearthTreeNode(copiedBoard)
        def theCard = copiedBoard.getCurrentPlayer().getHand().get(0);
        def ret = theCard.useOn(WAITING_PLAYER, 1, copiedRoot);

        expect:
        ret != null

        assertBoardDelta(startingBoard, copiedBoard) {
            currentPlayer {
                mana(8)
                removeCardFromHand(Savagery)
                numCardsUsed(2)
            }
            waitingPlayer {
                updateMinion(0, [deltaHealth: -3])
            }
        }
    }
}
