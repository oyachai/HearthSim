package com.hearthsim.test.groovy.card.classic.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.classic.minion.common.Whelp
import com.hearthsim.card.classic.minion.legendary.Onyxia
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static org.junit.Assert.assertFalse

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
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(Onyxia)
                mana(1)
                addMinionToField(Whelp, CharacterIndex.HERO)
                addMinionToField(Whelp, CharacterIndex.HERO)
                addMinionToField(Whelp, CharacterIndex.HERO)
                addMinionToField(Whelp, CharacterIndex.MINION_4)
                addMinionToField(Whelp, CharacterIndex.MINION_4)
                addMinionToField(Whelp, CharacterIndex.MINION_4)
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
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.MINION_1, root)

        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(Onyxia)
                mana(1)
                addMinionToField(Whelp, CharacterIndex.MINION_1)
                addMinionToField(Whelp, CharacterIndex.MINION_1)
                addMinionToField(Whelp, CharacterIndex.MINION_1)
                addMinionToField(Whelp, CharacterIndex.MINION_5)
                addMinionToField(Whelp, CharacterIndex.MINION_5)
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
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.MINION_6, root)

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
