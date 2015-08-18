package com.hearthsim.test.groovy.card.thegrandtournament.spell

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.classic.minion.common.Squire
import com.hearthsim.card.minion.heroes.Priest
import com.hearthsim.card.minion.heroes.TestHero
import com.hearthsim.card.thegrandtournament.minion.epic.Recruiter
import com.hearthsim.card.thegrandtournament.spell.common.FlashHeal
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER

/**
 * Created by oyachai on 8/17/15.
 */
class FlashHealSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make(new Priest(), new TestHero(), {
            currentPlayer {
                heroHealth(10)
                hand([FlashHeal])
                field([[minion:Recruiter]])
                mana(10)
            }
            waitingPlayer {
                field([[minion:Recruiter]])
            }
        })

        root = new HearthTreeNode(startingBoard)
    }

    def "can heal hero"() {
        def copiedBoard = startingBoard.deepCopy()
        def flashHeal = root.data_.getCurrentPlayer().getHand().get(0)

        def ret = flashHeal.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root);

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                mana(9)
                removeCardFromHand(FlashHeal)
                numCardsUsed(1)
                heroHealth(15)
            }
        }
    }

    def "can heal minions"() {
        def flashHeal = root.data_.getCurrentPlayer().getHand().get(0)
        def minion1 = root.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_1)
        minion1.setHealth((byte)1)

        def copiedBoard = startingBoard.deepCopy()
        def ret = flashHeal.useOn(CURRENT_PLAYER, CharacterIndex.MINION_1, root);

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                mana(9)
                removeCardFromHand(FlashHeal)
                numCardsUsed(1)
                updateMinion(CharacterIndex.MINION_1, [deltaHealth: 3])
            }
        }
    }
}
