package com.hearthsim.test.groovy.card.classic.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.spell.TheCoin
import com.hearthsim.card.classic.minion.legendary.EdwinVanCleef
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static org.junit.Assert.assertFalse

/**
 * Created by oyachai on 3/21/15.
 */
class EdwinVanCleefSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([EdwinVanCleef, TheCoin, TheCoin])
                mana(3)
            }
            waitingPlayer {
                mana(1)
            }
        }
        root = new HearthTreeNode(startingBoard)
    }

    def "Playing Ewdin VanCleef without combo"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(EdwinVanCleef, CharacterIndex.HERO)
                numCardsUsed(1)
                mana(0)
            }
        }
    }

    def "Playing Ewdin VanCleef with combo 1"() {
        def copiedBoard = startingBoard.deepCopy()

        def theCoin = root.data_.getCurrentPlayer().getHand().get(1)
        theCoin.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(TheCoin)
                playMinion(EdwinVanCleef, CharacterIndex.HERO)
                numCardsUsed(2)
                mana(1)
                updateMinion(CharacterIndex.MINION_1, [deltaHealth: 2, deltaMaxHealth: 2, deltaAttack: 2])
            }
        }
    }

    def "Playing Ewdin VanCleef with combo 2"() {
        def copiedBoard = startingBoard.deepCopy()

        def theCoin = root.data_.getCurrentPlayer().getHand().get(1)
        theCoin.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        theCoin = root.data_.getCurrentPlayer().getHand().get(1)
        theCoin.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(TheCoin)
                removeCardFromHand(TheCoin)
                playMinion(EdwinVanCleef, CharacterIndex.HERO)
                numCardsUsed(3)
                mana(2)
                updateMinion(CharacterIndex.MINION_1, [deltaHealth: 4, deltaMaxHealth: 4, deltaAttack: 4])
            }
        }
    }

}
