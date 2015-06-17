package com.hearthsim.test.groovy.card.goblinsvsgnomes.weapon

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.BloodfenRaptor
import com.hearthsim.card.basic.weapon.FieryWarAxe
import com.hearthsim.card.classic.minion.common.HarvestGolem
import com.hearthsim.card.goblinsvsgnomes.weapon.rare.Powermace
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode
import com.hearthsim.util.tree.RandomEffectNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

class PowermaceSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([Powermace, FieryWarAxe])
                mana(10)
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def 'deathrattle fizzles with no minions on board'() {
        def copiedBoard = startingBoard.deepCopy()
        def copiedRoot = new HearthTreeNode(copiedBoard)

        def theCard = copiedBoard.getCurrentPlayer().getHand().get(0);
        theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, copiedRoot);

        theCard = copiedBoard.getCurrentPlayer().getHand().get(0);
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, copiedRoot);

        expect:
        ret != null
        ret.numChildren() == 0

        assertBoardDelta(startingBoard, copiedBoard) {
            currentPlayer {
                weapon(FieryWarAxe) {
                }
                mana(5)
                removeCardFromHand(Powermace)
                removeCardFromHand(FieryWarAxe)
                numCardsUsed(2)
            }
        }
    }

    def 'deathrattle buffs friendly mech'() {
        startingBoard.placeMinion(CURRENT_PLAYER, new HarvestGolem(), CharacterIndex.HERO)

        def copiedBoard = startingBoard.deepCopy()
        def copiedRoot = new HearthTreeNode(copiedBoard)

        def theCard = copiedBoard.getCurrentPlayer().getHand().get(0);
        theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, copiedRoot);

        theCard = copiedBoard.getCurrentPlayer().getHand().get(0);
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, copiedRoot);

        expect:
        ret != null
        ret.numChildren() == 0

        assertBoardDelta(startingBoard, copiedBoard) {
            currentPlayer {
                weapon(FieryWarAxe) {
                }
                mana(5)
                removeCardFromHand(Powermace)
                removeCardFromHand(FieryWarAxe)
                numCardsUsed(2)
                updateMinion(CharacterIndex.MINION_1, [deltaMaxHealth: +2, deltaHealth: +2, deltaAttack: +2])
            }
        }
    }

    def 'deathrattle ignores friendly non-mech'() {
        startingBoard.placeMinion(CURRENT_PLAYER, new BloodfenRaptor(), CharacterIndex.HERO)

        def copiedBoard = startingBoard.deepCopy()
        def copiedRoot = new HearthTreeNode(copiedBoard)

        def theCard = copiedBoard.getCurrentPlayer().getHand().get(0);
        theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, copiedRoot);

        theCard = copiedBoard.getCurrentPlayer().getHand().get(0);
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, copiedRoot);

        expect:
        ret != null
        ret.numChildren() == 0

        assertBoardDelta(startingBoard, copiedBoard) {
            currentPlayer {
                weapon(FieryWarAxe) {
                }
                mana(5)
                removeCardFromHand(Powermace)
                removeCardFromHand(FieryWarAxe)
                numCardsUsed(2)
            }
        }
    }

    def 'deathrattle ignores enemy mech'() {
        startingBoard.placeMinion(WAITING_PLAYER, new HarvestGolem(), CharacterIndex.HERO)

        def copiedBoard = startingBoard.deepCopy()
        def copiedRoot = new HearthTreeNode(copiedBoard)

        def theCard = copiedBoard.getCurrentPlayer().getHand().get(0);
        theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, copiedRoot);

        theCard = copiedBoard.getCurrentPlayer().getHand().get(0);
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, copiedRoot);

        expect:
        ret != null
        ret.numChildren() == 0

        assertBoardDelta(startingBoard, copiedBoard) {
            currentPlayer {
                weapon(FieryWarAxe) {
                }
                mana(5)
                removeCardFromHand(Powermace)
                removeCardFromHand(FieryWarAxe)
                numCardsUsed(2)
            }
        }
    }

    def "dying with 2 friendly minions"() {
        startingBoard.placeMinion(CURRENT_PLAYER, new HarvestGolem(), CharacterIndex.HERO)
        startingBoard.placeMinion(CURRENT_PLAYER, new HarvestGolem(), CharacterIndex.MINION_1)

        def copiedBoard = startingBoard.deepCopy()
        def copiedRoot = new HearthTreeNode(copiedBoard)

        def theCard = copiedBoard.getCurrentPlayer().getHand().get(0);
        theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, copiedRoot);

        theCard = copiedBoard.getCurrentPlayer().getHand().get(0);
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, copiedRoot);

        expect:
        ret != null

        assertBoardDelta(startingBoard, copiedBoard) {
            currentPlayer {
                weapon(FieryWarAxe) {
                }
                mana(5)
                removeCardFromHand(Powermace)
                removeCardFromHand(FieryWarAxe)
                numCardsUsed(2)
            }
        }

        assertTrue(ret instanceof RandomEffectNode)
        assertEquals(ret.numChildren(), 2)

        HearthTreeNode child0 = ret.getChildren().get(0);
        assertBoardDelta(ret.data_, child0.data_) {
            currentPlayer {
                updateMinion(CharacterIndex.MINION_1, [deltaMaxHealth: +2, deltaHealth: +2, deltaAttack: +2])
            }
        }

        HearthTreeNode child1 = ret.getChildren().get(1);
        assertBoardDelta(ret.data_, child1.data_) {
            currentPlayer {
                updateMinion(CharacterIndex.MINION_2, [deltaMaxHealth: +2, deltaHealth: +2, deltaAttack: +2])
            }
        }
    }
}
