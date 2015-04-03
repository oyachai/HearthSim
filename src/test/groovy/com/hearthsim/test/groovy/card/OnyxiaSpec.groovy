package com.hearthsim.test.groovy.card

import com.hearthsim.card.minion.concrete.Onyxia
import com.hearthsim.card.minion.concrete.Whelp
import com.hearthsim.model.BoardModel
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.*

class OnyxiaSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def "playing Onyxia on an empty board summons 6 Whelps"() {
        
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([Onyxia])
                mana(10)
            }
            waitingPlayer {
                mana(4)
            }
        }

        root = new HearthTreeNode(startingBoard)
        
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, 0, root, null, null)

        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(Onyxia)
                mana(1)
                addMinionToField(Whelp, 0)
                addMinionToField(Whelp, 0)
                addMinionToField(Whelp, 0)
                addMinionToField(Whelp, 4)
                addMinionToField(Whelp, 4)
                addMinionToField(Whelp, 4)
                numCardsUsed(1)
            }
        }
    }
    
    def "playing Onyxia with 1 other minion on board summons 5 Whelps"() {
        
        def minionMana = 2;
        def attack = 5;
        def health0 = 3;

        def commonField = [
                [mana: minionMana, attack: attack, maxHealth: health0],
        ]

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([Onyxia])
                field(commonField)
                mana(10)
            }
            waitingPlayer {
                mana(4)
            }
        }

        root = new HearthTreeNode(startingBoard)
        
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, 1, root, null, null)

        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(Onyxia)
                mana(1)
                addMinionToField(Whelp, 1)
                addMinionToField(Whelp, 1)
                addMinionToField(Whelp, 1)
                addMinionToField(Whelp, 5)
                addMinionToField(Whelp, 5)
                numCardsUsed(1)
            }
        }
    }
    
    def "playing Onyxia on a full board summons no Whelps"() {
        
        def minionMana = 2;
        def attack = 5;
        def health0 = 3;

        def commonField = [
                [mana: minionMana, attack: attack, maxHealth: health0],
                [mana: minionMana, attack: attack, maxHealth: health0],
                [mana: minionMana, attack: attack, maxHealth: health0],
                [mana: minionMana, attack: attack, maxHealth: health0],
                [mana: minionMana, attack: attack, maxHealth: health0],
                [mana: minionMana, attack: attack, maxHealth: health0],
        ]

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([Onyxia])
                field(commonField)
                mana(10)
            }
            waitingPlayer {
                mana(4)
            }
        }

        root = new HearthTreeNode(startingBoard)
        
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, 6, root, null, null)

        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(Onyxia)
                mana(1)
                numCardsUsed(1)
            }
        }
    }
}
