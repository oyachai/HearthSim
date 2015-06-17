package com.hearthsim.test.groovy.card.classic.spell

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.BloodfenRaptor
import com.hearthsim.card.classic.minion.common.TreantWithCharge
import com.hearthsim.card.classic.spell.epic.ForceOfNature
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER

class ForceOfNatureSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([ForceOfNature])
                mana(10)
            }
        }


        root = new HearthTreeNode(startingBoard)
    }

    def "treant has charge"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null;
        ret.data_.getCharacter(CURRENT_PLAYER, CharacterIndex.MINION_1).charge
    }

    def "summons three treants"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null;

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(ForceOfNature)
                addMinionToField(TreantWithCharge, false, true)
                addMinionToField(TreantWithCharge, false, true)
                addMinionToField(TreantWithCharge, false, true)
                mana(4)
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
                removeCardFromHand(ForceOfNature)
                addMinionToField(TreantWithCharge, false, true)
                mana(4)
                numCardsUsed(1)
            }
        }
    }
}
