package com.hearthsim.test.groovy.card


import com.hearthsim.card.minion.concrete.BloodsailRaider
import com.hearthsim.card.weapon.concrete.FieryWarAxe
import com.hearthsim.model.BoardModel
import com.hearthsim.Game
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.*

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
        def target = root.data_.modelForSide(CURRENT_PLAYER).getCharacter(1)
        def theCard = root.data_.getCurrentPlayer().getHand().get(1)
        def ret = theCard.useOn(CURRENT_PLAYER, target, root, null, null)

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
        def ret = theCard.useOn(CURRENT_PLAYER, 0, root, null, null)
        
        theCard = root.data_.getCurrentPlayer().getHand().get(0)
        ret = theCard.useOn(CURRENT_PLAYER, 1, ret, null, null)

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
                updateMinion(1, [ deltaAttack: 3])
                numCardsUsed(2)
            }
        }
    }
}

