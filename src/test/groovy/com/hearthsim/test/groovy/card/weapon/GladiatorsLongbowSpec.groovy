package com.hearthsim.test.groovy.card.weapon

import com.hearthsim.card.minion.Minion
import com.hearthsim.card.spellcard.concrete.ArcaneShot
import com.hearthsim.card.minion.concrete.BoulderfistOgre
import com.hearthsim.card.weapon.concrete.GladiatorsLongbow
import com.hearthsim.model.BoardModel
import com.hearthsim.model.PlayerSide
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER

class GladiatorsLongbowSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([GladiatorsLongbow])
                mana(10)
            }

            waitingPlayer {
                field([[minion: BoulderfistOgre]])
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def 'immune on attack'() {
        def copiedBoard = startingBoard.deepCopy()
        def copiedRoot = new HearthTreeNode(copiedBoard)

        def theCard = copiedBoard.getCurrentPlayer().getHand().get(0);
        def ret = theCard.useOn(CURRENT_PLAYER, 0, copiedRoot);

        Minion hero = ret.data_.getCurrentPlayer().getHero();
        ret = hero.attack(PlayerSide.WAITING_PLAYER, 1, ret, null, null, false);

        expect:
        ret != null
        assertBoardDelta(startingBoard, copiedBoard) {
            currentPlayer {
                heroHasAttacked(true)
                weapon(GladiatorsLongbow) {
                    weaponDamage(5)
                    weaponCharge(1)
                }
                mana(3)
                removeCardFromHand(GladiatorsLongbow)
                numCardsUsed(1)
            }
            waitingPlayer {
                updateMinion(0, [deltaHealth: -5])
            }
        }
    }

    def 'no longer immune after attack'() {
        def copiedBoard = startingBoard.deepCopy()
        def copiedRoot = new HearthTreeNode(copiedBoard)

        def theCard = copiedBoard.getCurrentPlayer().getHand().get(0);
        def ret = theCard.useOn(CURRENT_PLAYER, 0, copiedRoot);

        Minion hero = ret.data_.getCurrentPlayer().getHero();
        hero.attack(PlayerSide.WAITING_PLAYER, 1, ret, null, null, false);

        def arcaneShot = new ArcaneShot();
        ret = arcaneShot.useOn(CURRENT_PLAYER, 0, copiedRoot);

        expect:
        ret != null
        assertBoardDelta(startingBoard, copiedBoard) {
            currentPlayer {
                heroHealth(28)
                heroHasAttacked(true)
                weapon(GladiatorsLongbow) {
                    weaponDamage(5)
                    weaponCharge(1)
                }
                mana(2)
                removeCardFromHand(GladiatorsLongbow)
                numCardsUsed(2)
            }
            waitingPlayer {
                updateMinion(0, [deltaHealth: -5])
            }
        }
    }
}
