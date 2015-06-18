package com.hearthsim.test.groovy.card.classic.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.weapon.FieryWarAxe
import com.hearthsim.card.classic.minion.common.BloodsailRaider
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static org.junit.Assert.assertFalse

class BloodsailRaiderSpec extends CardSpec {

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
                hand([FieryWarAxe, BloodsailRaider])
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
    
    def "playing Bloodsail Raider without weapon"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(1)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.MINION_1, root)

        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(BloodsailRaider)
                mana(5)
                numCardsUsed(1)
            }
        }
    }
    
    def "playing Bloodsail Raider with weapon"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)
        
        theCard = root.data_.getCurrentPlayer().getHand().get(0)
        ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.MINION_1, ret)

        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(BloodsailRaider)
                weapon(FieryWarAxe) {
                    weaponCharge(2)
                }
                removeCardFromHand(FieryWarAxe)
                mana(3)
                updateMinion(CharacterIndex.MINION_2, [ deltaAttack: 3])
                numCardsUsed(2)
            }
        }
    }
}

