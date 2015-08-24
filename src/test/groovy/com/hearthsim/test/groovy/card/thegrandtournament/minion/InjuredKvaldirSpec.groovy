package com.hearthsim.test.groovy.card.thegrandtournament.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.minion.heroes.Priest
import com.hearthsim.card.minion.heroes.TestHero
import com.hearthsim.card.thegrandtournament.minion.rare.InjuredKvaldir
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER

class InjuredKvaldirSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make(new Priest(), new TestHero(), {
            currentPlayer {
                heroHealth(10)
                hand([InjuredKvaldir])
                mana(10)
            }
        })

        root = new HearthTreeNode(startingBoard)
    }

    def "playing Injured Kvaldir"() {
        def copiedBoard = startingBoard.deepCopy()
        def minion = startingBoard.modelForSide(CURRENT_PLAYER).getHand().get(0)
        def ret = minion.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(InjuredKvaldir)
                mana(9)
                numCardsUsed(1)
                updateMinion(CharacterIndex.MINION_1, [deltaHealth: -3])
            }
        }
    }
}
