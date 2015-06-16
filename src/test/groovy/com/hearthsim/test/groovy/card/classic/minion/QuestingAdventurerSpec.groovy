package com.hearthsim.test.groovy.card.classic.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.spell.TheCoin
import com.hearthsim.card.classic.minion.rare.QuestingAdventurer
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static org.junit.Assert.assertFalse


class QuestingAdventurerSpec extends CardSpec {

    def "playing QuestingAdventurer"() {
        
        def startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([QuestingAdventurer])
                mana(9)
            }
            waitingPlayer {
                mana(9)
            }
        }

        def root = new HearthTreeNode(startingBoard)

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        assertFalse(ret == null);
        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(QuestingAdventurer)
                mana(6)
                numCardsUsed(1)
            }
        }

    }
    
    def "playing a card with a Questing Adventurer on the field"() {
        
        def startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([TheCoin])
                field([[minion: QuestingAdventurer]])
                mana(9)
            }
            waitingPlayer {
                field([[minion: QuestingAdventurer]]) //This Questing Adventurer should not be buffed
                mana(9)
            }
        }

        def root = new HearthTreeNode(startingBoard)

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        assertFalse(ret == null);
        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(TheCoin)
                mana(10)
                updateMinion(CharacterIndex.MINION_1, [deltaHealth: 1, deltaAttack: 1, deltaMaxHealth: 1])
                numCardsUsed(1)
            }
        }

    }

    def "QuestingAdventurer in hand does not get buffed"() {

        def startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([QuestingAdventurer])
                mana(9)
            }
            waitingPlayer {
                mana(9)
            }
        }

        def root = new HearthTreeNode(startingBoard)

        def copiedBoard = startingBoard.deepCopy()
        def theCard = new TheCoin();
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        assertFalse(ret == null);
        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                mana(10)
                numCardsUsed(1)
            }
        }

    }
}
