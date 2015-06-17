package com.hearthsim.test.groovy.card.classic.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.GoldshireFootman
import com.hearthsim.card.basic.minion.WarGolem
import com.hearthsim.card.classic.minion.rare.VoidTerror
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static org.junit.Assert.assertFalse

class VoidTerrorSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([VoidTerror])
                field([[minion: GoldshireFootman]
					, [minion: WarGolem]])
                mana(7)
            }
            waitingPlayer {
                mana(4)
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "playing Void Terror to the left of all minions"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(VoidTerror, CharacterIndex.HERO)
                removeMinion(CharacterIndex.MINION_2)
                mana(4)
                updateMinion(CharacterIndex.MINION_1, [deltaHealth: 2, deltaMaxHealth: 2, deltaAttack: 1])
                numCardsUsed(1)
            }
        }
    }

    def "playing Void Terror to the right of all minions"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.MINION_2, root)

        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(VoidTerror, CharacterIndex.MINION_2)
                removeMinion(CharacterIndex.MINION_2)
                mana(4)
                updateMinion(CharacterIndex.MINION_2, [deltaHealth: 7, deltaMaxHealth: 7, deltaAttack: 7])
                numCardsUsed(1)
            }
        }
    }

    def "playing Void Terror in the middle"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.MINION_1, root)

        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(VoidTerror, CharacterIndex.MINION_1)
                removeMinion(CharacterIndex.MINION_1)
                removeMinion(CharacterIndex.MINION_2)
                mana(4)
                updateMinion(CharacterIndex.MINION_1, [deltaHealth: 9, deltaMaxHealth: 9, deltaAttack: 8])
                numCardsUsed(1)
            }
        }
    }
}
