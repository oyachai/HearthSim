package com.hearthsim.test.groovy.card

import com.hearthsim.card.minion.concrete.BloodfenRaptor
import com.hearthsim.card.minion.concrete.KingOfBeasts
import com.hearthsim.card.minion.concrete.StonetuskBoar

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static org.junit.Assert.*

import com.hearthsim.card.minion.concrete.AbusiveSergeant
import com.hearthsim.card.minion.concrete.BoulderfistOgre
import com.hearthsim.model.BoardModel;
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode;

class KingOfBeastsSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([KingOfBeasts])
                field([[minion: BoulderfistOgre], [minion:StonetuskBoar], [minion:BloodfenRaptor]])
                mana(10)
            }
        }
        root = new HearthTreeNode(startingBoard)
    }

    def "adds extra attack for each beast"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, 0, root)

        expect:
        ret != null;

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(KingOfBeasts, 0)
                mana(5)
                numCardsUsed(1)
                updateMinion(0, [deltaAttack: +2])
            }
        }
    }
}
