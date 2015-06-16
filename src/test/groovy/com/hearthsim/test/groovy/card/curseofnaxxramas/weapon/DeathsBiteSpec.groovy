package com.hearthsim.test.groovy.card.curseofnaxxramas.weapon

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.BoulderfistOgre
import com.hearthsim.card.basic.weapon.FieryWarAxe
import com.hearthsim.card.classic.minion.common.ArathiWeaponsmith
import com.hearthsim.card.classic.weapon.common.BattleAxe
import com.hearthsim.card.curseofnaxxramas.weapon.common.DeathsBite
import com.hearthsim.card.minion.Minion
import com.hearthsim.model.BoardModel
import com.hearthsim.model.PlayerSide
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER

class DeathsBiteSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([DeathsBite, FieryWarAxe, ArathiWeaponsmith])
                mana(10)
            }

            waitingPlayer {
                field([[minion: BoulderfistOgre]])
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def 'deathrattle triggers after replacing weapon'() {
        def copiedBoard = startingBoard.deepCopy()
        def copiedRoot = new HearthTreeNode(copiedBoard)

        def theCard = copiedBoard.getCurrentPlayer().getHand().get(0);
        theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, copiedRoot);

        theCard = copiedBoard.getCurrentPlayer().getHand().get(0);
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, copiedRoot);

        expect:
        ret != null
        assertBoardDelta(startingBoard, copiedBoard) {
            currentPlayer {
                weapon(FieryWarAxe) {
                }
                mana(4)
                removeCardFromHand(DeathsBite)
                removeCardFromHand(FieryWarAxe)
                numCardsUsed(2)
            }
            waitingPlayer {
                updateMinion(CharacterIndex.MINION_1, [deltaHealth: -1])
            }
        }
    }

    def 'deathrattle triggers after replacing weapon via minion'() {
        def copiedBoard = startingBoard.deepCopy()
        def copiedRoot = new HearthTreeNode(copiedBoard)

        def theCard = copiedBoard.getCurrentPlayer().getHand().get(0);
        theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, copiedRoot);

        theCard = copiedBoard.getCurrentPlayer().getHand().get(1); // play ArathiWeaponsmith
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, copiedRoot);

        expect:
        ret != null
        assertBoardDelta(startingBoard, ret.data_) {
            currentPlayer {
                weapon(BattleAxe) {
                }
                mana(2)
                removeCardFromHand(DeathsBite)
                playMinion(ArathiWeaponsmith)
                updateMinion(CharacterIndex.MINION_1, [deltaHealth: -1])
                numCardsUsed(2)
            }
            waitingPlayer {
                updateMinion(CharacterIndex.MINION_1, [deltaHealth: -1])
            }
        }
    }

    def 'deathrattle triggers after using last charge'() {
        def copiedBoard = startingBoard.deepCopy()
        def copiedRoot = new HearthTreeNode(copiedBoard)

        def theCard = copiedBoard.getCurrentPlayer().getHand().get(0);
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, copiedRoot);

        Minion hero = ret.data_.getCurrentPlayer().getHero();
        hero.getWeapon().setWeaponCharge((byte)1);

        ret = hero.attack(PlayerSide.WAITING_PLAYER, CharacterIndex.HERO, ret);

        expect:
        ret != null
        assertBoardDelta(startingBoard, copiedBoard) {
            currentPlayer {
                mana(6)
                heroHasAttacked(true)
                removeCardFromHand(DeathsBite)
                numCardsUsed(1)
            }
            waitingPlayer {
                heroHealth(26)
                updateMinion(CharacterIndex.MINION_1, [deltaHealth: -1])
            }
        }
    }
}
