package com.hearthsim.test.groovy.card.classic.weapon

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.BoulderfistOgre
import com.hearthsim.card.classic.weapon.epic.Gorehowl
import com.hearthsim.card.minion.Minion
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

        def theCard = copiedBoard.getCurrentPlayer().getHand().get(0);
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, copiedRoot);

        Minion hero = ret.data_.getCurrentPlayer().getHero();
        ret = hero.attack(PlayerSide.WAITING_PLAYER, CharacterIndex.MINION_1, ret);

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
                numCardsUsed(1)
            }
            waitingPlayer {
                removeMinion(CharacterIndex.MINION_1)
            }
        }
    }

    def 'breaks after hitting 0 damage'() {
        def copiedBoard = startingBoard.deepCopy()
        def copiedRoot = new HearthTreeNode(copiedBoard)

        def theCard = copiedBoard.getCurrentPlayer().getHand().get(0);
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, copiedRoot);

        Minion hero = ret.data_.getCurrentPlayer().getHero();
        hero.getWeapon().setWeaponDamage((byte)1);

        ret = hero.attack(PlayerSide.WAITING_PLAYER, CharacterIndex.MINION_1, ret);

        expect:
        ret != null
        assertBoardDelta(startingBoard, copiedBoard) {
            currentPlayer {
                heroHealth(24)
                heroHasAttacked(true)
                mana(3)
                removeCardFromHand(Gorehowl)
                numCardsUsed(1)
            }
            waitingPlayer {
                updateMinion(CharacterIndex.MINION_1, [deltaHealth: -1])
            }
        }
    }

    def 'removes charge after targeting hero'() {
        def copiedBoard = startingBoard.deepCopy()
        def copiedRoot = new HearthTreeNode(copiedBoard)

        def theCard = copiedBoard.getCurrentPlayer().getHand().get(0);
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, copiedRoot);

        Minion hero = ret.data_.getCurrentPlayer().getHero();
        ret = hero.attack(PlayerSide.WAITING_PLAYER, CharacterIndex.HERO, ret);

        expect:
        ret != null
        assertBoardDelta(startingBoard, copiedBoard) {
            currentPlayer {
                heroHasAttacked(true)
                mana(3)
                removeCardFromHand(Gorehowl)
                numCardsUsed(1)
            }
            waitingPlayer {
                heroHealth(23)
            }
        }
    }
}
