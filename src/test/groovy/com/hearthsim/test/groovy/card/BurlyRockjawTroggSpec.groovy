package com.hearthsim.test.groovy.card

import com.hearthsim.card.Card
import com.hearthsim.card.Deck
import com.hearthsim.card.minion.concrete.BurlyRockjawTrogg
import com.hearthsim.card.minion.concrete.ManaWyrm
import com.hearthsim.card.spellcard.concrete.TheCoin
import com.hearthsim.model.BoardModel
import com.hearthsim.Game
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.*

class BurlyRockjawTroggSpec extends CardSpec {
    def "playing a spell card with a Mana Wyrm on the field"() {
        def startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([BurlyRockjawTrogg, TheCoin])
                field([[minion: BurlyRockjawTrogg]])
                mana(9)
            }
            waitingPlayer {
                field([[minion: BurlyRockjawTrogg]])
            }
        }

        def root = new HearthTreeNode(startingBoard)

        def copiedBoard = startingBoard.deepCopy()
        def target = root.data_.modelForSide(CURRENT_PLAYER).getCharacter(0)
        def theCoin = root.data_.getCurrentPlayer().getHand().get(1)
        def ret = theCoin.useOn(CURRENT_PLAYER, target, root)

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(TheCoin)
                mana(10)
                numCardsUsed(1)
            }
            waitingPlayer {
                updateMinion(0, [deltaAttack: 2])
            }
        }
    }
}
