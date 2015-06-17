package com.hearthsim.test.groovy.card.classic.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.BoulderfistOgre
import com.hearthsim.card.basic.spell.TheCoin
import com.hearthsim.card.classic.minion.rare.Si7Agent
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static org.junit.Assert.assertFalse

/**
 * Created by oyachai on 2/1/15.
 */
class Si7AgentSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([TheCoin, Si7Agent])
                field([[minion: BoulderfistOgre]])
                mana(10)
            }
            waitingPlayer {
                field([[minion: BoulderfistOgre]])
                mana(10)
            }
        }
        root = new HearthTreeNode(startingBoard)
    }

    def "Playing SI:7 Agent without combo"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayerCardHand(1)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(Si7Agent, CharacterIndex.HERO)
                numCardsUsed(1)
                mana(7)
            }
        }
    }

    def "Playing SI:7 Agent with combo"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCoin = root.data_.getCurrentPlayerCardHand(0)
        theCoin.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)
        def si7 = root.data_.getCurrentPlayerCardHand(0)
        def ret = si7.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(TheCoin)
                playMinion(Si7Agent, CharacterIndex.HERO)
                numCardsUsed(2)
                mana(7)
            }
        }

        assert ret.numChildren() == 4;

        HearthTreeNode child0 = ret.getChildren().get(0);
        assertBoardDelta(ret.data_, child0.data_) {
            currentPlayer {
                heroHealth(28)
            }
        }

        HearthTreeNode child1 = ret.getChildren().get(2);
        assertBoardDelta(ret.data_, child1.data_) {
            waitingPlayer {
                heroHealth(28)
            }
        }

        HearthTreeNode child2 = ret.getChildren().get(1);
        assertBoardDelta(ret.data_, child2.data_) {
            currentPlayer {
                updateMinion(CharacterIndex.MINION_2, [deltaHealth: -2])
            }
        }

        HearthTreeNode child3 = ret.getChildren().get(3);
        assertBoardDelta(ret.data_, child3.data_) {
            waitingPlayer {
                updateMinion(CharacterIndex.MINION_1, [deltaHealth: -2])
            }
        }

    }

}
