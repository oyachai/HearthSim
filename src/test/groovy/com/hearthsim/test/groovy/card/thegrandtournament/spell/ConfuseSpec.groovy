package com.hearthsim.test.groovy.card.thegrandtournament.spell

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.minion.heroes.Priest
import com.hearthsim.card.minion.heroes.TestHero
import com.hearthsim.card.thegrandtournament.minion.epic.Recruiter
import com.hearthsim.card.thegrandtournament.spell.epic.Confuse
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER

/**
 * Created by oyachai on 8/23/15.
 */
class ConfuseSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make(new Priest(), new TestHero(), {
            currentPlayer {
                heroHealth(10)
                hand([Confuse])
                field([[minion:Recruiter]])
                mana(10)
            }
            waitingPlayer {
                field([[minion:Recruiter]])
            }
        })

        root = new HearthTreeNode(startingBoard)
    }

    def "swaps attacks and healths of all minions"() {
        def copiedBoard = startingBoard.deepCopy()
        def confuse = root.data_.getCurrentPlayer().getHand().get(0)

        def ret = confuse.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root);

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                mana(8)
                removeCardFromHand(Confuse)
                numCardsUsed(1)
                updateMinion(CharacterIndex.MINION_1, [deltaHealth: 1, deltaMaxHealth: 1, deltaAttack: -1])
            }
            waitingPlayer {
                updateMinion(CharacterIndex.MINION_1, [deltaHealth: 1, deltaMaxHealth: 1, deltaAttack: -1])
            }
        }
    }
}
