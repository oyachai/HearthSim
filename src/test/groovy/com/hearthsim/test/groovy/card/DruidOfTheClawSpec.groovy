package com.hearthsim.test.groovy.card

import com.hearthsim.card.Card
import com.hearthsim.card.Deck
import com.hearthsim.card.minion.concrete.BaineBloodhoof
import com.hearthsim.card.minion.concrete.CairneBloodhoof
import com.hearthsim.card.minion.concrete.DruidOfTheClaw
import com.hearthsim.card.minion.concrete.SylvanasWindrunner
import com.hearthsim.card.minion.concrete.WarGolem
import com.hearthsim.card.minion.concrete.GoldshireFootman
import com.hearthsim.card.spellcard.concrete.ShadowWordDeath
import com.hearthsim.model.BoardModel
import com.hearthsim.Game
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode
import com.hearthsim.util.tree.RandomEffectNode
import spock.lang.Ignore

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.*

class DruidOfTheClawSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([DruidOfTheClaw])
                mana(7)
            }
        }
        root = new HearthTreeNode(startingBoard)
    }

    def "original board model has 4/4"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, 0, root)

        expect:
        assertNotNull(ret);

        assert ret.numChildren() == 2

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(DruidOfTheClaw)
                mana(2)
                numCardsUsed(1)
                updateMinion(0, [health: 4, maxHealth: 4, taunt: false, charge: false])
            }
        }
    }

    def "4/6 Taunt option"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, 0, root)

        expect:
        assertNotNull(ret);

        assert ret.numChildren() == 2

        assertBoardDelta(copiedBoard, ret.getChildren().get(0).data_) {
            currentPlayer {
                playMinion(DruidOfTheClaw)
                mana(2)
                numCardsUsed(1)
                updateMinion(0, [health: 6, maxHealth: 6, taunt: true, charge: false])
            }
        }
    }

    def "4/4 Charge option"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, 0, root)

        expect:
        assertNotNull(ret);

        assert ret.numChildren() == 2

        assertBoardDelta(copiedBoard, ret.getChildren().get(1).data_) {
            currentPlayer {
                playMinion(DruidOfTheClaw)
                mana(2)
                numCardsUsed(1)
                updateMinion(0, [health: 4, maxHealth: 4, taunt: false, charge: true])
            }
        }
    }
}
