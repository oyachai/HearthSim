package com.hearthsim.test.groovy.card.spell

import com.hearthsim.card.minion.concrete.BloodfenRaptor
import com.hearthsim.card.minion.concrete.BoulderfistOgre
import com.hearthsim.card.minion.concrete.KoboldGeomancer
import com.hearthsim.card.spellcard.concrete.Claw
import com.hearthsim.card.spellcard.concrete.LightningBolt
import com.hearthsim.card.spellcard.concrete.Savagery
import com.hearthsim.card.spellcard.concrete.ShieldSlam
import com.hearthsim.card.spellcard.concrete.SiphonSoul
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.*

class SiphonSoulSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([SiphonSoul])
                heroHealth(10)
                mana(10)
            }
            waitingPlayer {
                field([[minion:BoulderfistOgre]])
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def 'destroys minion and heals self'(){
        def copiedBoard = startingBoard.deepCopy()
        def copiedRoot = new HearthTreeNode(copiedBoard)
        def theCard = copiedBoard.getCurrentPlayer().getHand().get(0);
        def ret = theCard.useOn(WAITING_PLAYER, 1, copiedRoot);

        expect:
        ret != null

        assertBoardDelta(startingBoard, copiedBoard) {
            currentPlayer {
                mana(4)
                removeCardFromHand(SiphonSoul)
                numCardsUsed(1)
                heroHealth(13)
            }
            waitingPlayer {
                removeMinion(0)
            }
        }
    }
}
