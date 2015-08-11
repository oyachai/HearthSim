package com.hearthsim.test.groovy.card.goblinsvsgnomes.spell

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.Voidwalker
import com.hearthsim.card.basic.minion.WarGolem
import com.hearthsim.card.goblinsvsgnomes.spell.epic.TreeOfLife
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode
import com.hearthsim.util.tree.RandomEffectNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER

/**
 * Created by oyachai on 8/10/15.
 */
class TreeOfLifeSpec extends CardSpec {
    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([TreeOfLife])
                field([[minion: Voidwalker], [minion: WarGolem]])
                mana(10)
            }
            waitingPlayer {
                field([[minion: Voidwalker],[minion: WarGolem]])
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "nothing happens when no character is damaged"() {

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null
        ret instanceof HearthTreeNode
        !(ret instanceof RandomEffectNode)

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(TreeOfLife)
                mana(1)
                numCardsUsed(1)
            }
        }
    }

    def "heals everyone to full"() {

        startingBoard.getCurrentPlayer().getCharacter(CharacterIndex.HERO).setHealth((byte)10);
        startingBoard.getCurrentPlayer().getCharacter(CharacterIndex.MINION_1).setHealth((byte)1);
        startingBoard.getCurrentPlayer().getCharacter(CharacterIndex.MINION_2).setHealth((byte)1);

        startingBoard.getWaitingPlayer().getCharacter(CharacterIndex.HERO).setHealth((byte)10);
        startingBoard.getWaitingPlayer().getCharacter(CharacterIndex.MINION_1).setHealth((byte)1);
        startingBoard.getWaitingPlayer().getCharacter(CharacterIndex.MINION_2).setHealth((byte)1);

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null
        ret instanceof HearthTreeNode
        !(ret instanceof RandomEffectNode)

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(TreeOfLife)
                mana(1)
                numCardsUsed(1)
                heroHealth(30)
                updateMinion(CharacterIndex.MINION_1, [deltaHealth: 2])
                updateMinion(CharacterIndex.MINION_2, [deltaHealth: 6])
            }
            waitingPlayer {
                heroHealth(30)
                updateMinion(CharacterIndex.MINION_1, [deltaHealth: 2])
                updateMinion(CharacterIndex.MINION_2, [deltaHealth: 6])
            }
        }
    }

}
