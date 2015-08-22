package com.hearthsim.test.groovy.card.blackrockmountain.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.WarGolem
import com.hearthsim.card.blackrockmountain.minion.common.AxeFlinger
import com.hearthsim.card.blackrockmountain.minion.legendary.RendBlackhand
import com.hearthsim.card.classic.minion.legendary.LorewalkerCho
import com.hearthsim.card.classic.minion.rare.TwilightDrake
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static org.junit.Assert.assertEquals

/**
 * Created by oyachai on 8/22/15.
 */
class RendBlackhandSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([RendBlackhand])
                field([[minion:AxeFlinger], [minion: LorewalkerCho]])
                mana(10)
            }
            waitingPlayer {
                field([[minion:AxeFlinger], [minion: WarGolem], [minion: LorewalkerCho]])
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "does not destroy when not holding a dragon"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = new RendBlackhand()
        def ret = theCard.battlecryEffect.applyEffect(CURRENT_PLAYER, CharacterIndex.MINION_2, root)

        expect:

        assertBoardDelta(copiedBoard, ret.data_) {
        }
    }

    def "destroy legendary when holding a dragon"() {
        startingBoard.placeCardHand(CURRENT_PLAYER, new TwilightDrake());
        def copiedBoard = startingBoard.deepCopy()
        def rendBlackhand = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = rendBlackhand.useOn(CURRENT_PLAYER, CharacterIndex.MINION_2, root)

        expect:

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(RendBlackhand)
                mana(3)
                numCardsUsed(1)
            }
        }

        assertEquals(ret.numChildren(), 2);

        def ch0 = ret.getChildren().get(0)
        assertBoardDelta(ret.data_, ch0.data_) {
            currentPlayer {
                removeMinion(CharacterIndex.MINION_2)
            }
        }

        def ch1 = ret.getChildren().get(1)
        assertBoardDelta(ret.data_, ch1.data_) {
            waitingPlayer {
                removeMinion(CharacterIndex.MINION_3)
            }
        }

    }

}
