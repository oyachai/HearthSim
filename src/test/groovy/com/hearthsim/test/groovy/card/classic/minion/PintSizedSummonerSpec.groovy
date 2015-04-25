package com.hearthsim.test.groovy.card.classic.minion

import com.hearthsim.Game
import com.hearthsim.card.basic.minion.BloodfenRaptor
import com.hearthsim.card.basic.minion.RiverCrocolisk
import com.hearthsim.card.basic.spell.Fireball
import com.hearthsim.card.basic.spell.TheCoin
import com.hearthsim.card.classic.minion.rare.PintSizedSummoner
import com.hearthsim.card.goblinsvsgnomes.minion.common.SpiderTank
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER

class PintSizedSummonerSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([PintSizedSummoner, SpiderTank, BloodfenRaptor])
                deck([RiverCrocolisk, TheCoin])
                mana(10)
            }
            waitingPlayer {
                hand([RiverCrocolisk, Fireball])
                mana(10)
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "playing Pint-Sized Summoner does not decrease minion mana cost"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, 0, root)

        theCard = ret.data_.getCurrentPlayer().getHand().get(0)
        ret = theCard.useOn(CURRENT_PLAYER, 0, ret)

        expect:
        ret != null;

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(PintSizedSummoner, 0)
                playMinion(SpiderTank, 0)
                manaUsed(5)
                numCardsUsed(2)
                updateCardInHand(0, [manaDelta: 0])
            }
        }
    }

    def "discounts first minion played"() {
        startingBoard.placeMinion(CURRENT_PLAYER, new PintSizedSummoner())

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(1)
        def ret = theCard.useOn(CURRENT_PLAYER, 0, root)

        expect:
        ret != null;

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(SpiderTank, 0)
                manaUsed(2)
                numCardsUsed(1)
                updateCardInHand(0, [manaDelta: 0])
                updateCardInHand(1, [manaDelta: 0])
            }
        }
    }

    def "playing a minion with enemy Pint-Sized Summoner has no discount"() {
        startingBoard.placeMinion(WAITING_PLAYER, new PintSizedSummoner())

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(1)
        def ret = theCard.useOn(CURRENT_PLAYER, 0, root)

        expect:
        ret != null;

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(SpiderTank, 0)
                manaUsed(3)
                numCardsUsed(1)
            }
        }
    }

    def "no discount for second minion"() {
        startingBoard.placeMinion(CURRENT_PLAYER, new PintSizedSummoner())

        def theCard = root.data_.getCurrentPlayer().getHand().get(1)
        def pre = theCard.useOn(CURRENT_PLAYER, 0, root)

        def copiedBoard = pre.data_.deepCopy()
        theCard = pre.data_.getCurrentPlayer().getHand().get(1)
        def ret = theCard.useOn(CURRENT_PLAYER, 0, pre)

        expect:
        pre != null;
        ret != null;

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(BloodfenRaptor, 0)
                manaUsed(2)
                numCardsUsed(2)
            }
        }
    }

    def "next turn, discount is re-activated"() {
        startingBoard.placeMinion(CURRENT_PLAYER, new PintSizedSummoner())

        def theCard = root.data_.getCurrentPlayer().getHand().get(1)
        def pre = theCard.useOn(CURRENT_PLAYER, 0, root)

        def copiedBoard = pre.data_.deepCopy()
        def ret = new HearthTreeNode(Game.beginTurn(pre.data_))

        expect:
        ret != null;

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                mana(10)
                addCardToHand(RiverCrocolisk)
                addDeckPos(1)
                numCardsUsed(0)
                updateMinion(0, [hasAttacked: false, hasBeenUsed: false])
                updateCardInHand(0, [manaDelta: -1])
                updateCardInHand(1, [manaDelta: -1])
                updateCardInHand(2, [manaDelta: -1])
            }
        }
    }
}
