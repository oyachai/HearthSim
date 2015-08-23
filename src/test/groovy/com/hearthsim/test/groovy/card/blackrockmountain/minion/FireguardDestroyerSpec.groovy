package com.hearthsim.test.groovy.card.blackrockmountain.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.WarGolem
import com.hearthsim.card.blackrockmountain.minion.common.AxeFlinger
import com.hearthsim.card.blackrockmountain.minion.common.FireguardDestroyer
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode
import com.hearthsim.util.tree.RandomEffectNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER

/**
 * Created by oyachai on 8/23/15.
 */
class FireguardDestroyerSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([FireguardDestroyer])
                field([[minion:AxeFlinger]])
                mana(10)
            }
            waitingPlayer {
                field([[minion:AxeFlinger], [minion: WarGolem]])
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "randomly buffs attack"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(FireguardDestroyer, CharacterIndex.HERO)
                mana(6)
                numCardsUsed(1)
            }
        }

        ret instanceof RandomEffectNode

        HearthTreeNode ch0 = ret.getChildren().get(0);
        assertBoardDelta(ret.data_, ch0.data_) {
            currentPlayer {
                overload(1)
                updateMinion(CharacterIndex.MINION_1, [deltaAttack: 1])
            }
        }

        HearthTreeNode ch1 = ret.getChildren().get(1);
        assertBoardDelta(ret.data_, ch1.data_) {
            currentPlayer {
                overload(1)
                updateMinion(CharacterIndex.MINION_1, [deltaAttack: 2])
            }
        }

        HearthTreeNode ch2 = ret.getChildren().get(2);
        assertBoardDelta(ret.data_, ch2.data_) {
            currentPlayer {
                overload(1)
                updateMinion(CharacterIndex.MINION_1, [deltaAttack: 3])
            }
        }

        HearthTreeNode ch3 = ret.getChildren().get(3);
        assertBoardDelta(ret.data_, ch3.data_) {
            currentPlayer {
                overload(1)
                updateMinion(CharacterIndex.MINION_1, [deltaAttack: 4])
            }
        }

    }

}
