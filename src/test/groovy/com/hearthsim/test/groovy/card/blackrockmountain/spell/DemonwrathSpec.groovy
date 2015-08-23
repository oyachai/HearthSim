package com.hearthsim.test.groovy.card.blackrockmountain.spell

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.WarGolem
import com.hearthsim.card.blackrockmountain.minion.common.AxeFlinger
import com.hearthsim.card.blackrockmountain.minion.common.ImpGangBoss
import com.hearthsim.card.blackrockmountain.spell.rare.Demonwrath
import com.hearthsim.card.classic.minion.rare.AzureDrake
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER

/**
 * Created by oyachai on 8/23/15.
 */
class DemonwrathSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([Demonwrath])
                field([[minion:AxeFlinger], [minion: ImpGangBoss]])
                mana(10)
            }
            waitingPlayer {
                field([[minion:AxeFlinger], [minion: WarGolem], [minion: ImpGangBoss]])
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "deals 2 damage to non-demons"() {
        def copiedBoard = startingBoard.deepCopy()
        def demonwrath = root.data_.getCurrentPlayer().getHand().get(0)

        def ret = demonwrath.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root);

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(Demonwrath)
                mana(7)
                numCardsUsed(1)
                heroHealth(28)
                updateMinion(CharacterIndex.MINION_1, [deltaHealth: -2])
            }
            waitingPlayer {
                heroHealth(28)
                updateMinion(CharacterIndex.MINION_1, [deltaHealth: -2])
                updateMinion(CharacterIndex.MINION_2, [deltaHealth: -2])
            }
        }
    }

    def "is affected by spell damage"() {
        startingBoard.placeMinion(CURRENT_PLAYER, new AzureDrake())
        def copiedBoard = startingBoard.deepCopy()
        def demonwrath = root.data_.getCurrentPlayer().getHand().get(0)

        def ret = demonwrath.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root);

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(Demonwrath)
                mana(7)
                numCardsUsed(1)
                heroHealth(28)
                updateMinion(CharacterIndex.MINION_1, [deltaHealth: -3])
                updateMinion(CharacterIndex.MINION_3, [deltaHealth: -3])
            }
            waitingPlayer {
                heroHealth(28)
                updateMinion(CharacterIndex.MINION_1, [deltaHealth: -3])
                updateMinion(CharacterIndex.MINION_2, [deltaHealth: -3])
            }
        }
    }
}
