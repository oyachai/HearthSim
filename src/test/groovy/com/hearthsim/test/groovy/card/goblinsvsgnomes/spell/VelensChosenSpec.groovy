package com.hearthsim.test.groovy.card.goblinsvsgnomes.spell

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.Voidwalker
import com.hearthsim.card.basic.minion.WarGolem
import com.hearthsim.card.goblinsvsgnomes.spell.common.Flamecannon
import com.hearthsim.card.goblinsvsgnomes.spell.common.VelensChosen
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode
import com.hearthsim.util.tree.RandomEffectNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER

/**
 * Created by oyachai on 7/31/15.
 */
class VelensChosenSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([VelensChosen])
                field([[minion: Voidwalker], [minion: WarGolem]])
                mana(10)
            }
            waitingPlayer {
                field([[minion: Voidwalker],[minion: WarGolem]])
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "cannot target hero"() {

        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret == null
    }

    def "can target own minion"() {

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.MINION_1, root)

        expect:
        ret != null
        ret instanceof HearthTreeNode
        !(ret instanceof RandomEffectNode)

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(VelensChosen)
                mana(7)
                numCardsUsed(1)
                updateMinion(CharacterIndex.MINION_1, [deltaAttack: 2, deltaHealth: 4, deltaMaxHealth: 4, deltaSpellDamage: 1])
            }
        }
    }

    def "can target enemy minion"() {

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(WAITING_PLAYER, CharacterIndex.MINION_1, root)

        expect:
        ret != null
        ret instanceof HearthTreeNode
        !(ret instanceof RandomEffectNode)

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(VelensChosen)
                mana(7)
                numCardsUsed(1)
            }
            waitingPlayer {
                updateMinion(CharacterIndex.MINION_1, [deltaAttack: 2, deltaHealth: 4, deltaMaxHealth: 4, deltaSpellDamage: 1])
            }
        }
    }
}
