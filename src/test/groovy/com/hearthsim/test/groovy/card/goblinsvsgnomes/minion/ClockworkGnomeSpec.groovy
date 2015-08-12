package com.hearthsim.test.groovy.card.goblinsvsgnomes.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.Voidwalker
import com.hearthsim.card.basic.spell.ShadowWordPain
import com.hearthsim.card.goblinsvsgnomes.minion.common.ClockworkGnome
import com.hearthsim.card.goblinsvsgnomes.spell.spareparts.ArmorPlating
import com.hearthsim.card.goblinsvsgnomes.spell.spareparts.EmergencyCoolant
import com.hearthsim.card.goblinsvsgnomes.spell.spareparts.FinickyCloakfield
import com.hearthsim.card.goblinsvsgnomes.spell.spareparts.ReversingSwitch
import com.hearthsim.card.goblinsvsgnomes.spell.spareparts.RustyHorn
import com.hearthsim.card.goblinsvsgnomes.spell.spareparts.TimeRewinder
import com.hearthsim.card.goblinsvsgnomes.spell.spareparts.WhirlingBlades
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode
import com.hearthsim.util.tree.RandomEffectNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER

/**
 * Created by oyachai on 8/11/15.
 */
class ClockworkGnomeSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([ClockworkGnome, ShadowWordPain])
                field([[minion: Voidwalker]])
                mana(10)
            }
            waitingPlayer {
                field([[minion: Voidwalker]])
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "playing Clockwork Gnome"() {
        def copiedBoard = startingBoard.deepCopy()
        def card = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = card.useOn(CURRENT_PLAYER, CharacterIndex.MINION_1, root)

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(ClockworkGnome)
                mana(9)
                numCardsUsed(1)
            }
        }
    }

    def "killing Clockwork Gnome"() {
        def copiedBoard = startingBoard.deepCopy()
        def card = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = card.useOn(CURRENT_PLAYER, CharacterIndex.MINION_1, root)
        def retBoard = ret.data_.deepCopy();

        def swp = ret.data_.getCurrentPlayer().getHand().get(0)
        def ret2 = swp.useOn(CURRENT_PLAYER, CharacterIndex.MINION_2, ret)

        expect:
        ret != null
        ret2 != null
        ret2 instanceof RandomEffectNode

        assertBoardDelta(copiedBoard, retBoard) {
            currentPlayer {
                playMinion(ClockworkGnome)
                mana(9)
                numCardsUsed(1)
            }
        }

        assertBoardDelta(retBoard, ret2.data_) {
            currentPlayer {
                removeCardFromHand(ShadowWordPain)
                removeMinion(CharacterIndex.MINION_2)
                mana(7)
                numCardsUsed(2)
            }
        }

        def parts = [ArmorPlating, EmergencyCoolant, FinickyCloakfield, ReversingSwitch, RustyHorn, TimeRewinder, WhirlingBlades]
        for (int i = 0; i < 7; ++i) {
            HearthTreeNode ch = ret2.getChildren().get(i)
            assertBoardDelta(ret2.data_, ch.data_) {
                currentPlayer {
                    addCardToHand(parts[i])
                }
            }
        }
    }
}
