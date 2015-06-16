package com.hearthsim.test.groovy.card.classic.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.classic.minion.common.FlameImp
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertTrue

class FlameImpSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {

        def minionMana = 2;
        def attack = 5;
        def health0 = 3;
        def health1 = 7;

        def commonField = [
            [mana: minionMana, attack: attack, maxHealth: health0],
            //TODO: attack may be irrelevant here
            [mana: minionMana, attack: attack, health: health1 - 1, maxHealth: health1]
        ]

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([FlameImp])
                field(commonField)
                mana(7)
            }
            waitingPlayer {
                field(commonField)
                mana(4)
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "playing FlameImp damages the hero"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.MINION_2, root)

        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(FlameImp)
                mana(6)
                heroHealth(27)
                numCardsUsed(1)
            }
        }

    }

    def "playing FlameImp can kill own hero"() {
        def copiedBoard = startingBoard.deepCopy()
        root.data_.modelForSide(CURRENT_PLAYER).getCharacter(CharacterIndex.HERO).setHealth((byte)2)

        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.MINION_2, root)

        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(FlameImp)
                mana(6)
                heroHealth(-1)
                numCardsUsed(1)
            }
        }
        
        assertTrue(ret.data_.isLethalState())
    }
}
