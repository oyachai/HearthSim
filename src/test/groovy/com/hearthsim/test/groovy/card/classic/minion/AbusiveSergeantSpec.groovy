package com.hearthsim.test.groovy.card.classic.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.BoulderfistOgre
import com.hearthsim.card.classic.minion.common.AbusiveSergeant
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static org.junit.Assert.assertEquals

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
        def ret = theCard.battlecryEffect.applyEffect(CURRENT_PLAYER, CharacterIndex.MINION_1, root)

        expect:
        assertEquals(root, ret);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                updateMinion(CharacterIndex.MINION_1, [deltaExtraAttack: +2])
            }
        }
    }

    def "buff is additive"() {
        def copiedBoard = startingBoard.deepCopy()
        def target = root.data_.modelForSide(CURRENT_PLAYER).getCharacter(CharacterIndex.MINION_1)
        target.extraAttackUntilTurnEnd = 2
        def theCard = new AbusiveSergeant()
        def ret = theCard.battlecryEffect.applyEffect(CURRENT_PLAYER, CharacterIndex.MINION_1, root)

        expect:
        assertEquals(root, ret);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                updateMinion(CharacterIndex.MINION_1, [deltaExtraAttack: +4])
            }
        }
    }
}
