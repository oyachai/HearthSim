package com.hearthsim.test.groovy.card

import com.hearthsim.card.minion.concrete.BloodfenRaptor
import com.hearthsim.card.minion.concrete.HauntedCreeper
import com.hearthsim.card.minion.concrete.KingOfBeasts
import com.hearthsim.card.minion.concrete.LilExorcist
import com.hearthsim.card.minion.concrete.LootHoarder

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER

import com.hearthsim.model.BoardModel;
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode;

class LilExorcistSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([LilExorcist])
                mana(10)
            }
            waitingPlayer {
                field([[minion: HauntedCreeper], [minion:LootHoarder], [minion:BloodfenRaptor]])
            }
        }
        root = new HearthTreeNode(startingBoard)
    }

    def "adds extra 1/1 for each enemy deathrattle"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, 0, root)

        expect:
        ret != null;

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(LilExorcist, 0)
                mana(7)
                numCardsUsed(1)
                updateMinion(0, [deltaAttack: +2, deltaHealth: +2, deltaMaxHealth: +2])
            }
        }
    }
}
