package com.hearthsim.test.groovy.card
import com.hearthsim.card.minion.concrete.FrothingBerserker
import com.hearthsim.model.BoardModel
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.*

class FrothingBerserkerSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {

        def minionMana = 2;
        def attack = 5;
        def health0 = 3;
        def health1 = 7;

        def commonField = [
                [mana: minionMana, attack: attack, maxHealth: health0], //todo: attack may be irrelevant here
                [mana: minionMana, attack: attack, health: health1 - 1, maxHealth: health1]
        ]

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([FrothingBerserker])
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


    def "playing Frothing Berserker and attacking"() {
        def minionPlayedBoard = startingBoard.deepCopy()
        def copiedRoot = new HearthTreeNode(minionPlayedBoard)
        def theCard = minionPlayedBoard.getCurrentPlayer().getHand().get(0);
        theCard.useOn(CURRENT_PLAYER, 2, copiedRoot);

        def attacker = minionPlayedBoard.modelForSide(CURRENT_PLAYER).getCharacter(1)
        def ret =  attacker.attack(WAITING_PLAYER, 1, copiedRoot, null, null, false)
        
        expect:
        assertFalse(ret == null);

        assertBoardDelta(startingBoard, minionPlayedBoard) {
            currentPlayer {
                playMinion(FrothingBerserker)
                removeMinion(0)
                updateMinion(1, [deltaAttack: 2])
                mana(4)
                numCardsUsed(1)
            }
            waitingPlayer {
                removeMinion(0)
            }
        }
    }
}
