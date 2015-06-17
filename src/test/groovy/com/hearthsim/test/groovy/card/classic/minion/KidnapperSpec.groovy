package com.hearthsim.test.groovy.card.classic.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.BoulderfistOgre
import com.hearthsim.card.basic.spell.TheCoin
import com.hearthsim.card.classic.minion.epic.Kidnapper
import com.hearthsim.card.classic.minion.rare.Abomination
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertFalse

/**
 * Created by oyachai on 3/21/15.
 */
class KidnapperSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([TheCoin, Kidnapper])
                field([[minion: BoulderfistOgre]])
                mana(10)
            }
            waitingPlayer {
                field([[minion: Abomination]])
                mana(10)
            }
        }
        root = new HearthTreeNode(startingBoard)
    }

    def "Playing Kidnapper without combo"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(1)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(Kidnapper, CharacterIndex.HERO)
                numCardsUsed(1)
                mana(4)
            }
        }
    }

    def "Playing Kidnapper with combo"() {
        def copiedBoard = startingBoard.deepCopy()

        def theCoin = root.data_.getCurrentPlayer().getHand().get(0)
        def ret0 = theCoin.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        def theCard = ret0.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(TheCoin)
                playMinion(Kidnapper, CharacterIndex.HERO)
                numCardsUsed(2)
                mana(4)
            }
        }

        assertEquals(2, ret.numChildren())

        HearthTreeNode child0 = ret.getChildren().get(0);
        assertBoardDelta(ret.data_, child0.data_) {
            currentPlayer {
                addCardToHand(BoulderfistOgre)
                removeMinion(CharacterIndex.MINION_2)
            }
        }

        HearthTreeNode child1 = ret.getChildren().get(1);
        assertBoardDelta(ret.data_, child1.data_) {
            waitingPlayer {
                addCardToHand(Abomination)
                removeMinion(CharacterIndex.MINION_1)
            }
        }

    }

    def "Playing Kidnapper with combo on full hand"() {
        startingBoard.placeCardHand(WAITING_PLAYER, new BoulderfistOgre())
        startingBoard.placeCardHand(WAITING_PLAYER, new BoulderfistOgre())
        startingBoard.placeCardHand(WAITING_PLAYER, new BoulderfistOgre())
        startingBoard.placeCardHand(WAITING_PLAYER, new BoulderfistOgre())
        startingBoard.placeCardHand(WAITING_PLAYER, new BoulderfistOgre())
        startingBoard.placeCardHand(WAITING_PLAYER, new BoulderfistOgre())
        startingBoard.placeCardHand(WAITING_PLAYER, new BoulderfistOgre())
        startingBoard.placeCardHand(WAITING_PLAYER, new BoulderfistOgre())
        startingBoard.placeCardHand(WAITING_PLAYER, new BoulderfistOgre())
        startingBoard.placeCardHand(WAITING_PLAYER, new BoulderfistOgre())

        def copiedBoard = startingBoard.deepCopy()


        def theCoin = root.data_.getCurrentPlayer().getHand().get(0)
        def ret0 = theCoin.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        def theCard = ret0.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(TheCoin)
                playMinion(Kidnapper, CharacterIndex.HERO)
                numCardsUsed(2)
                mana(4)
            }
        }

        assertEquals(2, ret.numChildren())

        HearthTreeNode child0 = ret.getChildren().get(0);
        assertBoardDelta(ret.data_, child0.data_) {
            currentPlayer {
                addCardToHand(BoulderfistOgre)
                removeMinion(CharacterIndex.MINION_2)
            }
        }

        HearthTreeNode child1 = ret.getChildren().get(1);
        assertBoardDelta(ret.data_, child1.data_) {
            currentPlayer {
                heroHealth(28)
                updateMinion(CharacterIndex.MINION_1, [deltaHealth: -2])
                updateMinion(CharacterIndex.MINION_2, [deltaHealth: -2])
            }
            waitingPlayer {
                heroHealth(28)
                removeMinion(CharacterIndex.MINION_1)
            }
        }
    }

}
