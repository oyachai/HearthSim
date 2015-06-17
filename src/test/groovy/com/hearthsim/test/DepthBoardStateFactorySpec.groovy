package com.hearthsim.test

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.GoldshireFootman
import com.hearthsim.card.basic.minion.MurlocRaider
import com.hearthsim.card.classic.minion.common.Wisp
import com.hearthsim.model.BoardModel
import com.hearthsim.player.playercontroller.BruteForceSearchAI
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

class DepthBoardStateFactorySpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([GoldshireFootman, MurlocRaider, Wisp])
                mana(10)
            }
        }
        root = new HearthTreeNode(startingBoard)
    }
    
    def "Plays All Minions"() {
        def copiedBoard = startingBoard.deepCopy()
        BruteForceSearchAI ai0 = BruteForceSearchAI.buildStandardAI1();
        def res = ai0.playTurn(9, startingBoard)
        def newBoardModel = res.get(res.size() - 1).board;
        
        expect:

        assertBoardDelta(copiedBoard, newBoardModel) {
            currentPlayer {
                playMinion(Wisp)
                playMinion(GoldshireFootman, CharacterIndex.HERO)
                playMinion(MurlocRaider, CharacterIndex.HERO)
                mana(8)
                numCardsUsed(3)
            }
        }
    }

}
