package com.hearthsim.test.groovy.card.goblinsvsgnomes.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.spell.TheCoin
import com.hearthsim.card.goblinsvsgnomes.minion.common.SpiderTank
import com.hearthsim.card.goblinsvsgnomes.minion.rare.CobaltGuardian
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER

class CobaltGuardianSpec extends CardSpec {
    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([TheCoin, SpiderTank])
                field([[minion: CobaltGuardian]])
                mana(9)
            }
            waitingPlayer {
                field([[minion: CobaltGuardian]])
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "gain divine shield on friendly mech play"() {
        def copiedBoard = startingBoard.deepCopy()
        def card = root.data_.getCurrentPlayer().getHand().get(1)
        def ret = card.useOn(CURRENT_PLAYER, CharacterIndex.MINION_1, root)

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(SpiderTank)
                mana(6)
                numCardsUsed(1)
                updateMinion(CharacterIndex.MINION_1, [divineShield: true])
            }
        }
    }

    def "does not give divine shield after playing self"() {
        startingBoard.removeMinion(CURRENT_PLAYER, CharacterIndex.MINION_1)
        startingBoard.modelForSide(CURRENT_PLAYER).getHand().add(new CobaltGuardian())
        def copiedBoard = startingBoard.deepCopy()
        def card = root.data_.getCurrentPlayer().getHand().get(2)
        def ret = card.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(CobaltGuardian)
                mana(4)
                numCardsUsed(1)
            }
        }
    }

    def "does not give divine shield on spell"() {
        def copiedBoard = startingBoard.deepCopy()
        def card = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = card.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(TheCoin)
                mana(10)
                numCardsUsed(1)
            }
        }
    }

    def "does not gain divine shield while in hand"() {
        startingBoard.removeMinion(CURRENT_PLAYER, CharacterIndex.MINION_1)
        startingBoard.modelForSide(CURRENT_PLAYER).getHand().add(new CobaltGuardian())
        def copiedBoard = startingBoard.deepCopy()
        def card = root.data_.getCurrentPlayer().getHand().get(1)
        def ret = card.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(SpiderTank)
                mana(6)
                numCardsUsed(1)
            }
        }
    }
}
