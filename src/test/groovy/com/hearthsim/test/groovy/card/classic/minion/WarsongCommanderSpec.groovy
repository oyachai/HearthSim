package com.hearthsim.test.groovy.card.classic.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.BloodfenRaptor
import com.hearthsim.card.basic.minion.WarGolem
import com.hearthsim.card.basic.minion.WarsongCommander
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER

class WarsongCommanderSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([BloodfenRaptor, WarGolem])
                field([[minion: WarsongCommander]])
                mana(10)
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "gives minions charge"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(BloodfenRaptor, CharacterIndex.HERO)
                mana(8)
                numCardsUsed(1)
                updateMinion(CharacterIndex.MINION_1, [charge: true])
            }
        }
    }

    def "does not trigger on self"() {
        startingBoard.removeMinion(CURRENT_PLAYER, CharacterIndex.MINION_1);
        startingBoard.modelForSide(CURRENT_PLAYER).getHand().add(new WarsongCommander())

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(2)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(WarsongCommander, CharacterIndex.HERO)
                mana(7)
                numCardsUsed(1)
            }
        }
    }

    def "does not trigger on enemy play"() {
        startingBoard.removeMinion(CURRENT_PLAYER, CharacterIndex.MINION_1);
        startingBoard.placeMinion(WAITING_PLAYER, new WarsongCommander())

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(BloodfenRaptor, CharacterIndex.HERO)
                mana(8)
                numCardsUsed(1)
            }
        }
    }

    def "does not trigger on big minions"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(1)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(WarGolem, CharacterIndex.HERO)
                mana(3)
                numCardsUsed(1)
            }
        }
    }
}
