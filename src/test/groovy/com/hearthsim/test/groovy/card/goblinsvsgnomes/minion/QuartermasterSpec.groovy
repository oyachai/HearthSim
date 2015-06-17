package com.hearthsim.test.groovy.card.goblinsvsgnomes.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.GoldshireFootman
import com.hearthsim.card.basic.minion.SilverHandRecruit
import com.hearthsim.card.goblinsvsgnomes.minion.epic.Quartermaster
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER

/**
 * Created by oyachai on 6/7/15.
 */
class QuartermasterSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([Quartermaster])
                field([[minion:SilverHandRecruit], [minion:SilverHandRecruit], [minion:GoldshireFootman]])
                mana(10)
            }
            waitingPlayer {
                field([[minion:SilverHandRecruit]])
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "playing Quartermaster buffs your own Silverhand Recruits"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(Quartermaster, CharacterIndex.HERO)
                mana(5)
                numCardsUsed(1)
                updateMinion(CharacterIndex.MINION_2, [deltaHealth: 2, deltaMaxHealth: 2, deltaAttack: 2])
                updateMinion(CharacterIndex.MINION_3, [deltaHealth: 2, deltaMaxHealth: 2, deltaAttack: 2])

            }
        }
    }
}
