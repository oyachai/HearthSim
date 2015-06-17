package com.hearthsim.test.groovy.card.classic.spell

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.weapon.FieryWarAxe
import com.hearthsim.card.classic.spell.rare.Upgrade
import com.hearthsim.card.classic.weapon.common.HeavyAxe
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER

class UpgradeSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([Upgrade, FieryWarAxe])
                mana(10)
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def 'playing without a weapon adds 1/3 axe'() {
        def copiedBoard = startingBoard.deepCopy()
        def copiedRoot = new HearthTreeNode(copiedBoard)

        def theCard = copiedBoard.getCurrentPlayer().getHand().get(0);
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, copiedRoot);

        expect:
        ret != null
        assertBoardDelta(startingBoard, copiedBoard) {
            currentPlayer {
                weapon(HeavyAxe) {
                }
                mana(9)
                removeCardFromHand(Upgrade)
                numCardsUsed(1)
            }
        }
    }

    def 'playing with a weapon buffs 1/1'() {
        def theCard = startingBoard.getCurrentPlayer().getHand().get(1);
        root = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root);

        def copiedBoard = startingBoard.deepCopy()
        def copiedRoot = new HearthTreeNode(copiedBoard)

        theCard = copiedBoard.getCurrentPlayer().getHand().get(0);
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, copiedRoot);

        expect:
        ret != null
        assertBoardDelta(startingBoard, copiedBoard) {
            currentPlayer {
                weaponCharge(3)
                weaponDamage(4)
                mana(7)
                removeCardFromHand(Upgrade)
                numCardsUsed(2)
            }
        }
    }
}
