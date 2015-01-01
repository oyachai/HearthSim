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

        def theCard = copiedBoard.getCurrentPlayerCardHand(0);
        def ret = theCard.useOn(CURRENT_PLAYER, 0, copiedRoot, null, null);

        Minion hero = ret.data_.getCurrentPlayerHero();
        def target = copiedBoard.getCharacter(PlayerSide.WAITING_PLAYER, 1);
        ret = hero.attack(PlayerSide.WAITING_PLAYER, target, ret, null, null);

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
            }
            waitingPlayer {
                updateMinion(0, [deltaHealth: -5])
            }
        }
    }

    def 'no longer immune after attack'() {
        def copiedBoard = startingBoard.deepCopy()
        def copiedRoot = new HearthTreeNode(copiedBoard)

        def theCard = copiedBoard.getCurrentPlayerCardHand(0);
        def ret = theCard.useOn(CURRENT_PLAYER, 0, copiedRoot, null, null);

        Minion hero = ret.data_.getCurrentPlayerHero();
        def target = copiedBoard.getCharacter(PlayerSide.WAITING_PLAYER, 1);
        ret = hero.attack(PlayerSide.WAITING_PLAYER, target, ret, null, null);

        def arcaneShot = new ArcaneShot();
        ret = arcaneShot.useOn(CURRENT_PLAYER, 0, copiedRoot, null, null);

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
            }
            waitingPlayer {
                updateMinion(0, [deltaHealth: -5])
            }
        }
    }
}
