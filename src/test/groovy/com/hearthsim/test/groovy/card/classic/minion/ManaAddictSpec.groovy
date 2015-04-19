package com.hearthsim.test.groovy.card.classic.minion

import com.hearthsim.card.basic.spell.TheCoin
import com.hearthsim.card.classic.minion.rare.ManaAddict
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static org.junit.Assert.assertFalse

class ManaAddictSpec extends CardSpec {

    def "playing a spell card with a Mana Addict on the field"() {
        
        def startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([ManaAddict, TheCoin])
                mana(9)
            }
            waitingPlayer {
                field([[minion: ManaAddict]]) //This Questing Adventurer should not be buffed
                mana(9)
            }
        }

        def root = new HearthTreeNode(startingBoard)

        def copiedBoard = startingBoard.deepCopy()
        def target = root.data_.modelForSide(CURRENT_PLAYER).getCharacter(0)
        def manaWyrm = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = manaWyrm.useOn(CURRENT_PLAYER, target, root, null, null)

        def board2 = new HearthTreeNode(root.data_.deepCopy())
        def theCoin = board2.data_.getCurrentPlayer().getHand().get(0)
        def coinTarget = board2.data_.modelForSide(CURRENT_PLAYER).getCharacter(0)
        def ret2 = theCoin.useOn(CURRENT_PLAYER, coinTarget, board2, null, null)
        
        expect:
        assertFalse(ret == null);
        assertFalse(ret2 == null);
        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(ManaAddict)
                mana(7)
                numCardsUsed(1)
            }
        }
        assertBoardDelta(copiedBoard, ret2.data_) {
            currentPlayer {
                playMinion(ManaAddict)
                removeCardFromHand(TheCoin)
                mana(8)
                updateMinion(0, [deltaExtraAttack: 2])
                numCardsUsed(2)
            }
        }

    }
}
