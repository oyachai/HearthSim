package com.hearthsim.test.groovy.card

import com.hearthsim.card.minion.concrete.*
import com.hearthsim.card.spellcard.concrete.Fireball
import com.hearthsim.card.spellcard.concrete.TheCoin
import com.hearthsim.card.spellcard.concrete.TwistingNether
import com.hearthsim.card.spellcard.concrete.Whirlwind
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.CardDrawNode
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER

class ScarletPurifierSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([ScarletPurifier])
                field([[minion:BloodfenRaptor], [minion:HauntedCreeper]])
                mana(10)
            }
            waitingPlayer {
                field([[minion:ZombieChow]])
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "deals damage to deathrattle minions"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, 0, root)

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(ScarletPurifier, 0)
                mana(7)
                numCardsUsed(1)
                removeMinion(2)
                addMinionToField(SpectralSpider)
                addMinionToField(SpectralSpider)
            }
            waitingPlayer {
                updateMinion(0, [deltaHealth: -2])
            }
        }
    }
}
