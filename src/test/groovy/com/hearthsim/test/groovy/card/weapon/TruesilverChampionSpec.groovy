package com.hearthsim.test.groovy.card.weapon

import com.hearthsim.card.minion.Minion
import com.hearthsim.card.minion.concrete.BoulderfistOgre
import com.hearthsim.card.weapon.concrete.TruesilverChampion
import com.hearthsim.model.BoardModel
import com.hearthsim.model.PlayerSide
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode
import spock.lang.Ignore

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

            waitingPlayer {
                field([[minion: BoulderfistOgre]])
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
                heroHasAttacked(true)
                weapon(TruesilverChampion) {
                    weaponDamage(4)
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

    @Ignore("Existing bug")
    def 'cannot overheal before attack'() {
        startingBoard.getCurrentPlayerCharacter(0).setHealth((byte)30);
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
                weapon(TruesilverChampion) {
                    weaponDamage(4)
                    weaponCharge(1)
                }
                mana(0)
                removeCardFromHand(TruesilverChampion)
            }
            waitingPlayer {
                updateMinion(0, [deltaHealth: -4])
            }
        }
    }
}
