package com.hearthsim.test.groovy.card.classic.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.classic.minion.rare.AncientWatcher
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertTrue

/**
 * Created by oyachai on 4/5/15.
 */
class AncientWatcherSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([AncientWatcher])
                field([[minion: AncientWatcher],])
                mana(7)
            }
            waitingPlayer {
                mana(4)
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "playing Ancient Watcher"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(AncientWatcher, CharacterIndex.HERO)
                mana(5)
                numCardsUsed(1)
            }
        }

    }

    def "Ancient Watcher can't attack"() {
        def copiedBoard = startingBoard.deepCopy()
        def ancientWatcher = root.data_.getCharacter(CURRENT_PLAYER, CharacterIndex.MINION_1);
        def ret = ancientWatcher.attack(WAITING_PLAYER, root.data_.getCharacter(WAITING_PLAYER, CharacterIndex.HERO), root);

        expect:
        assertTrue(ret == null);
    }

    def "Silenced Ancient Watcher can attack"() {
        def copiedBoard = startingBoard.deepCopy()
        def ancientWatcher = root.data_.getCharacter(CURRENT_PLAYER, CharacterIndex.MINION_1);
        ancientWatcher.silenced(CURRENT_PLAYER, root.data_);
        def ret = ancientWatcher.attack(WAITING_PLAYER, root.data_.getCharacter(WAITING_PLAYER, CharacterIndex.HERO), root);

        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                updateMinion(CharacterIndex.MINION_1, [hasAttacked: true, silenced: true, cantAttack: false])
            }
            waitingPlayer {
                heroHealth(26)
            }
        }
    }

}
