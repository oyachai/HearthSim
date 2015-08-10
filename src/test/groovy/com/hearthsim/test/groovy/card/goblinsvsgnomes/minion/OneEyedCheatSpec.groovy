package com.hearthsim.test.groovy.card.goblinsvsgnomes.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.classic.minion.common.BloodsailRaider
import com.hearthsim.card.goblinsvsgnomes.minion.rare.OneEyedCheat
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER

class OneEyedCheatSpec extends CardSpec {
    def "playing a pirate card with a One Eyed Cheat on the field"() {
        def startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([BloodsailRaider, OneEyedCheat])
                field([[minion: OneEyedCheat]])
                mana(10)
            }
            waitingPlayer {
                field([[minion: OneEyedCheat]])
            }
        }

        def root = new HearthTreeNode(startingBoard)

        def copiedBoard = startingBoard.deepCopy()
        def card = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = card.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(BloodsailRaider, CharacterIndex.HERO)
                mana(8)
                numCardsUsed(1)
                updateMinion(CharacterIndex.MINION_2, [stealthedUntilRevealed: true])
            }
        }
    }
}
