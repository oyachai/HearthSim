package com.hearthsim.test.groovy.card.classic.spell

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.BloodfenRaptor
import com.hearthsim.card.classic.minion.rare.SpiritWolf
import com.hearthsim.card.classic.spell.rare.FeralSpirit
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER

class FeralSpiritSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([FeralSpirit])
                mana(10)
            }
        }


        root = new HearthTreeNode(startingBoard)
    }

    def "summons two wolves"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null;

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(FeralSpirit)
                addMinionToField(SpiritWolf)
                addMinionToField(SpiritWolf)
                overload(2)
                mana(7)
                numCardsUsed(1)
            }
        }
    }

    def "playing with room for one"() {
        startingBoard.placeMinion(CURRENT_PLAYER, new BloodfenRaptor());
        startingBoard.placeMinion(CURRENT_PLAYER, new BloodfenRaptor());
        startingBoard.placeMinion(CURRENT_PLAYER, new BloodfenRaptor());
        startingBoard.placeMinion(CURRENT_PLAYER, new BloodfenRaptor());
        startingBoard.placeMinion(CURRENT_PLAYER, new BloodfenRaptor());
        startingBoard.placeMinion(CURRENT_PLAYER, new BloodfenRaptor());

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null;

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(FeralSpirit)
                addMinionToField(SpiritWolf)
                overload(2)
                mana(7)
                numCardsUsed(1)
            }
        }
    }
}
