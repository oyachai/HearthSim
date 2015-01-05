package com.hearthsim.test.groovy.card.weapon

import com.hearthsim.card.minion.Minion
import com.hearthsim.card.spellcard.concrete.ArcaneShot
import com.hearthsim.card.minion.concrete.BoulderfistOgre
import com.hearthsim.card.weapon.concrete.Gorehowl
import com.hearthsim.model.BoardModel
import com.hearthsim.model.PlayerSide
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER

class GorehowlSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([Gorehowl])
                mana(10)
            }

            waitingPlayer {
                field([[minion: BoulderfistOgre]])
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def 'removes attack after targeting minion'() {
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
                heroHealth(24)
                heroHasAttacked(true)
                weapon(Gorehowl) {
                    weaponDamage(6)
                }
                mana(3)
                removeCardFromHand(Gorehowl)
            }
            waitingPlayer {
                removeMinion(0)
            }
        }
    }

    def 'breaks after hitting 0 damage'() {
        def copiedBoard = startingBoard.deepCopy()
        def copiedRoot = new HearthTreeNode(copiedBoard)

        def theCard = copiedBoard.getCurrentPlayerCardHand(0);
        def ret = theCard.useOn(CURRENT_PLAYER, 0, copiedRoot, null, null);

        Minion hero = ret.data_.getCurrentPlayerHero();
        hero.getWeapon().setWeaponDamage((byte)1);

        def target = copiedBoard.getCharacter(PlayerSide.WAITING_PLAYER, 1);
        ret = hero.attack(PlayerSide.WAITING_PLAYER, target, ret, null, null);

        expect:
        ret != null
        assertBoardDelta(startingBoard, copiedBoard) {
            currentPlayer {
                heroHealth(24)
                heroHasAttacked(true)
                mana(3)
                removeCardFromHand(Gorehowl)
            }
            waitingPlayer {
                updateMinion(0, [deltaHealth: -1])
            }
        }
    }

    def 'removes charge after targeting hero'() {
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
                heroHasAttacked(true)
                mana(3)
                removeCardFromHand(Gorehowl)
            }
            waitingPlayer {
                heroHealth(23)
            }
        }
    }
}
