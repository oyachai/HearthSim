package com.hearthsim.test.groovy.card.classic.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.classic.minion.common.DruidOfTheClaw
import com.hearthsim.card.classic.spell.common.Silence
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static org.junit.Assert.assertNotNull

class DruidOfTheClawSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([DruidOfTheClaw])
                mana(8)
            }
        }
        root = new HearthTreeNode(startingBoard)
    }

    def "original board model has 4/4"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        assertNotNull(ret);

        assert ret.numChildren() == 2

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(DruidOfTheClaw)
                mana(3)
                numCardsUsed(1)
                updateMinion(CharacterIndex.MINION_1, [health: 4, maxHealth: 4, taunt: false, charge: false])
            }
        }
    }

    def "4/6 Taunt option"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        assertNotNull(ret);

        assert ret.numChildren() == 2

        assertBoardDelta(copiedBoard, ret.getChildren().get(0).data_) {
            currentPlayer {
                playMinion(DruidOfTheClaw)
                mana(3)
                numCardsUsed(1)
                updateMinion(CharacterIndex.MINION_1, [health: 6, maxHealth: 6, taunt: true, charge: false])
            }
        }
    }

    def "4/4 Charge option"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        assertNotNull(ret);

        assert ret.numChildren() == 2

        assertBoardDelta(copiedBoard, ret.getChildren().get(1).data_) {
            currentPlayer {
                playMinion(DruidOfTheClaw)
                mana(3)
                numCardsUsed(1)
                updateMinion(CharacterIndex.MINION_1, [health: 4, maxHealth: 4, taunt: false, charge: true])
            }
        }
    }

    def "4/6 Taunt silenced"() {
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        def silenced = ret.getChildren().get(0)
        def copiedBoard = silenced.data_.deepCopy()
        theCard = new Silence()
        silenced = theCard.useOn(CURRENT_PLAYER, CharacterIndex.MINION_1, silenced)

        expect:
        assertNotNull(ret);

        assert ret.numChildren() == 2

        assertNotNull(silenced);

        assertBoardDelta(copiedBoard, silenced.data_) {
            currentPlayer {
                numCardsUsed(2)
                updateMinion(CharacterIndex.MINION_1, [silenced: true, health: 6, maxHealth: 6, taunt: false, charge: false])
            }
        }
    }

    def "4/4 Charge silenced"() {
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        def silenced = ret.getChildren().get(1)
        def copiedBoard = silenced.data_.deepCopy()
        theCard = new Silence()
        silenced = theCard.useOn(CURRENT_PLAYER, CharacterIndex.MINION_1, silenced)

        expect:
        assertNotNull(ret);

        assert ret.numChildren() == 2

        assertNotNull(silenced);

        assertBoardDelta(copiedBoard, silenced.data_) {
            currentPlayer {
                numCardsUsed(2)
                updateMinion(CharacterIndex.MINION_1, [silenced: true, health: 4, maxHealth: 4, taunt: false, charge: false])
            }
        }
    }
}
