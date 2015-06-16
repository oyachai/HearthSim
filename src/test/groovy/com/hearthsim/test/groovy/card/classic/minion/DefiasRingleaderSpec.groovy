package com.hearthsim.test.groovy.card.classic.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.spell.TheCoin
import com.hearthsim.card.classic.minion.common.DefiasBandit
import com.hearthsim.card.classic.minion.common.DefiasRingleader
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static org.junit.Assert.assertFalse

/**
 * Created by oyachai on 3/21/15.
 */
class DefiasRingleaderSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([TheCoin, DefiasRingleader])
                mana(2)
            }
            waitingPlayer {
                mana(1)
            }
        }
        root = new HearthTreeNode(startingBoard)
    }

    def "Playing Defias Ringleader without combo"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(1)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(DefiasRingleader, CharacterIndex.HERO)
                numCardsUsed(1)
                mana(0)
            }
        }
    }

    def "Playing Defias Ringleader with combo"() {
        def copiedBoard = startingBoard.deepCopy()

        def theCoin = root.data_.getCurrentPlayer().getHand().get(0)
        theCoin.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(TheCoin)
                playMinion(DefiasRingleader, CharacterIndex.HERO)
                playMinion(DefiasBandit, CharacterIndex.MINION_1)
                numCardsUsed(2)
                mana(1)
            }
        }
    }

}
