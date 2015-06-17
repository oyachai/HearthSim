package com.hearthsim.test.groovy.card.classic.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.weapon.FieryWarAxe
import com.hearthsim.card.classic.minion.legendary.HarrisonJones
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.CardDrawNode
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER

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
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

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
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

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
