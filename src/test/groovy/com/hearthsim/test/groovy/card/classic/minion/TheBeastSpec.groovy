package com.hearthsim.test.groovy.card.classic.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.GoldshireFootman
import com.hearthsim.card.basic.minion.WarGolem
import com.hearthsim.card.basic.spell.ShadowWordDeath
import com.hearthsim.card.classic.minion.legendary.FinkleEinhorn
import com.hearthsim.card.classic.minion.legendary.TheBeast
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static org.junit.Assert.assertFalse

/**
 * Created by oyachai on 6/7/15.
 */
class TheBeastSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def "killing The Beast summons a Finkle Einhorn"() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([ShadowWordDeath])
                field([[minion:TheBeast]])
                mana(9)
            }
            waitingPlayer {
                field([[minion: WarGolem],
                       [minion: GoldshireFootman]])
                mana(4)
            }
        }

        root = new HearthTreeNode(startingBoard)

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.MINION_1, root)

        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(ShadowWordDeath)
                removeMinion(CharacterIndex.MINION_1)
                mana(6)
                numCardsUsed(1)
            }
            waitingPlayer {
                addMinionToField(FinkleEinhorn)
            }
        }

    }
}
