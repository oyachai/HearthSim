package com.hearthsim.test.groovy.card


import com.hearthsim.card.minion.concrete.BloodsailCorsair
import com.hearthsim.card.minion.concrete.HarrisonJones
import com.hearthsim.card.weapon.concrete.FieryWarAxe
import com.hearthsim.model.BoardModel
import com.hearthsim.Game
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.CardDrawNode
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.*

class HarrisonJonesSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([HarrisonJones])
                mana(10)
            }
            waitingPlayer {
                weapon(FieryWarAxe) {
                    weaponCharge(2)
                }
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "destroys weapon and draws cards"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, 0, root)

        expect:
        ret != null
        ret instanceof CardDrawNode
        ((CardDrawNode)ret).numCardsToDraw == 2

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(HarrisonJones)
                mana(5)
                numCardsUsed(1)
            }
            waitingPlayer {
                weapon(null){}
            }
        }
    }

    def "can play without opponent having a weapon"() {
        startingBoard.modelForSide(WAITING_PLAYER).getHero().destroyWeapon();

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, 0, root)

        expect:
        ret != null
        !(ret instanceof CardDrawNode)

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(HarrisonJones)
                mana(5)
                numCardsUsed(1)
            }
        }
    }
}
