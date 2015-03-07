package com.hearthsim.test.groovy.card.spell

import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec;
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode;
import com.hearthsim.card.minion.concrete.GoldshireFootman
import com.hearthsim.card.minion.concrete.WarGolem
import com.hearthsim.card.spellcard.concrete.TwistingNether

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static org.junit.Assert.*

class TwistingNetherSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([TwistingNether])
                field([[minion: GoldshireFootman], [minion: WarGolem]])
                mana(10)
            }
            waitingPlayer {
                field([[minion: GoldshireFootman], [minion: WarGolem]])
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "playing Twisting Nether kills all minions"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, 0, root, null, null)

        expect:
        assertNotNull(ret);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(TwistingNether)
                removeMinion(1)
                removeMinion(0)
                mana(2)
                numCardsUsed(1)
            }

            waitingPlayer {
                removeMinion(1)
                removeMinion(0)
            }
        }
    }
}
