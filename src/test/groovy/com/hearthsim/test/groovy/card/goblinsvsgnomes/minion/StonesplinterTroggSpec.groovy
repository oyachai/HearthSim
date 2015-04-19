package com.hearthsim.test.groovy.card.goblinsvsgnomes.minion

import com.hearthsim.card.basic.spell.TheCoin
import com.hearthsim.card.goblinsvsgnomes.minion.common.StonesplinterTrogg
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER

class StonesplinterTroggSpec extends CardSpec {
    def "playing a spell card with a Stonesplinter Trogg on the field"() {
        def startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([StonesplinterTrogg, TheCoin])
                field([[minion: StonesplinterTrogg]])
                mana(9)
            }
            waitingPlayer {
                field([[minion: StonesplinterTrogg]])
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
                updateMinion(0, [deltaAttack: 1])
            }
        }
    }
}
