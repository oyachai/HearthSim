package com.hearthsim.test.groovy.card

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static org.junit.Assert.*

import com.hearthsim.card.minion.concrete.AbusiveSergeant
import com.hearthsim.card.minion.concrete.BoulderfistOgre
import com.hearthsim.model.BoardModel;
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode;

class AbusiveSergeantSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                field([[minion: BoulderfistOgre]])
                mana(10)
            }
        }
        root = new HearthTreeNode(startingBoard)
    }

    def "adds extra attack"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = new AbusiveSergeant()
        def ret = theCard.useTargetableBattlecry_core(CURRENT_PLAYER, theCard, CURRENT_PLAYER, 1, root)

        expect:
        assertEquals(root, ret);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                updateMinion(0, [deltaExtraAttack: +2])
            }
        }
    }

    def "buff is additive"() {
        def copiedBoard = startingBoard.deepCopy()
        def target = root.data_.modelForSide(CURRENT_PLAYER).getCharacter(1)
        target.extraAttackUntilTurnEnd = 2
        def theCard = new AbusiveSergeant()
        def ret = theCard.useTargetableBattlecry_core(CURRENT_PLAYER, theCard, CURRENT_PLAYER, 1, root)

        expect:
        assertEquals(root, ret);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                updateMinion(0, [deltaExtraAttack: +4])
            }
        }
    }
}
