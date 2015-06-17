package com.hearthsim.test.groovy.card.curseofnaxxramas.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.BloodfenRaptor
import com.hearthsim.card.basic.spell.Fireball
import com.hearthsim.card.classic.minion.common.FlameImp
import com.hearthsim.card.curseofnaxxramas.minion.common.NerubarWeblord
import com.hearthsim.card.goblinsvsgnomes.minion.rare.BombLobber
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER

class NerubarWeblordSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([NerubarWeblord, FlameImp, BloodfenRaptor])
                mana(10)
            }
            waitingPlayer {
                hand([BombLobber, Fireball])
                mana(10)
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "playing Weblord increases battlecry mana cost"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null;

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(NerubarWeblord, CharacterIndex.HERO)
                mana(8)
                numCardsUsed(1)
            }
        }
    }

    // This test also makes sure the mana effect actually sticks since our test helpers use placeMinion directly
    def "playing a battlecry minion with enemy Weblord on board costs 2 more"() {
        startingBoard.placeMinion(WAITING_PLAYER, new NerubarWeblord())

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(1)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null;

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                heroHealth(27)
                playMinion(FlameImp, CharacterIndex.HERO)
                manaUsed(3)
                numCardsUsed(1)
            }
        }
    }
}
