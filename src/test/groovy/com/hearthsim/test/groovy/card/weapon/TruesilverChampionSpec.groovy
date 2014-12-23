package com.hearthsim.test.groovy.card.weapon

import com.hearthsim.card.minion.Minion
import com.hearthsim.card.weapon.concrete.TruesilverChampion
import com.hearthsim.model.BoardModel
import com.hearthsim.model.PlayerSide
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER

class TruesilverChampionSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([TruesilverChampion])
                heroHealth(28)
                mana(4)
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def 'heals on attack'() {
        def copiedBoard = startingBoard.deepCopy()
        def copiedRoot = new HearthTreeNode(copiedBoard)
        def theCard = copiedBoard.getCurrentPlayerCardHand(0);
        def ret = theCard.useOn(CURRENT_PLAYER, 0, copiedRoot, null, null);
        Minion hero = ret.data_.getCurrentPlayerHero();
        def target = copiedBoard.getCharacter(PlayerSide.WAITING_PLAYER, 0);
        ret = hero.attack(PlayerSide.WAITING_PLAYER, target, ret, null, null);

        expect:
        ret != null
        assertBoardDelta(startingBoard, copiedBoard) {
            currentPlayer {
                heroHealth(30)
                heroAttack(4)
                heroHasAttacked(true)
                weapon(TruesilverChampion) {
                    weaponCharge(1)
                }
                mana(0)
                removeCardFromHand(TruesilverChampion)

            }
            waitingPlayer {
                heroHealth(26)
            }
        }


    }


}
