package com.hearthsim.test.groovy.card

import com.hearthsim.model.BoardModel;
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode;
import com.hearthsim.card.minion.concrete.GoldshireFootman
import com.hearthsim.card.minion.concrete.WarGolem
import com.hearthsim.card.minion.concrete.VoidTerror
import com.hearthsim.card.minion.concrete.Hound

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.*

class VoidTerrorSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([VoidTerror])
                field([[minion: GoldshireFootman]
					, [minion: WarGolem]])
                mana(7)
            }
            waitingPlayer {
                mana(4)
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "playing Void Terror to the left of all minions"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, 0, root, null, null)

        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(VoidTerror, 0)
                removeMinion(1)
                mana(4)
                updateMinion(0, [deltaHealth: 2, deltaMaxHealth: 2, deltaAttack: 1])
                numCardsUsed(1)
            }
        }
    }

    def "playing Void Terror to the right of all minions"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, 2, root, null, null)

        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(VoidTerror, 2)
                removeMinion(1)
                mana(4)
                updateMinion(1, [deltaHealth: 7, deltaMaxHealth: 7, deltaAttack: 7])
                numCardsUsed(1)
            }
        }
    }

    def "playing Void Terror in the middle"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, 1, root, null, null)

        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(VoidTerror, 1)
                removeMinion(0)
                removeMinion(1)
                mana(4)
                updateMinion(0, [deltaHealth: 9, deltaMaxHealth: 9, deltaAttack: 8])
                numCardsUsed(1)
            }
        }
    }
}
