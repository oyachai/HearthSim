package com.hearthsim.test.groovy.card

import com.hearthsim.card.minion.concrete.IllidanStormrage
import com.hearthsim.card.minion.concrete.FlameOfAzzinoth
import com.hearthsim.card.spellcard.concrete.TheCoin
import com.hearthsim.model.BoardModel
import com.hearthsim.Game
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.*

class IllidanStormrageSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {

        def minionMana = 2;
        def attack = 5;
        def health0 = 3;

        def commonField = [
                [mana: minionMana, attack: attack, maxHealth: health0], //todo: attack may be irrelevant here
        ]

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([IllidanStormrage, TheCoin])
                field(commonField)
                mana(7)
            }
            waitingPlayer {
                field(commonField)
                mana(4)
            }
        }

        root = new HearthTreeNode(startingBoard)
    }
    
    def "playing Illidan Stormrage"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, 1, root, null, null)
        ret.data_.getCurrentPlayer().getHand().get(0).useOn(CURRENT_PLAYER, 0, ret, null, null)
        
        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(IllidanStormrage)
                addMinionToField(FlameOfAzzinoth)
                removeCardFromHand(TheCoin)
                mana(2)
                numCardsUsed(2)
            }
        }
    }

    def "does not create minions while sitting in hand"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(1)
        def ret = theCard.useOn(CURRENT_PLAYER, 0, root)

        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(TheCoin)
                mana(8)
                numCardsUsed(1)
            }
        }
    }
}
