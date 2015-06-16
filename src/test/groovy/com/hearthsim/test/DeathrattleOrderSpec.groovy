package com.hearthsim.test

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.spell.Hellfire
import com.hearthsim.card.classic.minion.legendary.BaineBloodhoof
import com.hearthsim.card.classic.minion.legendary.CairneBloodhoof
import com.hearthsim.card.classic.minion.legendary.SylvanasWindrunner
import com.hearthsim.card.classic.minion.rare.Abomination
import com.hearthsim.card.curseofnaxxramas.minion.common.HauntedCreeper
import com.hearthsim.card.curseofnaxxramas.minion.common.SpectralSpider
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.assertFalse

class DeathrattleOrderSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def "minions can spawn before aoe"() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([Hellfire])
                mana(20)
            }
        }

        root = new HearthTreeNode(startingBoard)

        def creeper = new HauntedCreeper()
        def ret = creeper.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        def abom = new Abomination()
        ret = abom.useOn(CURRENT_PLAYER, CharacterIndex.HERO, ret)
        abom.setHealth((byte) 1)

        def copiedBoard = startingBoard.deepCopy()

        def hellfire = root.data_.getCurrentPlayer().getHand().get(0)
        ret = hellfire.useOn(CURRENT_PLAYER, CharacterIndex.HERO, ret)

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(Hellfire)
                removeMinion(CharacterIndex.MINION_2)
                removeMinion(CharacterIndex.MINION_1)
                mana(9)
                numCardsUsed(3)
                heroHealth(25)
            }
            waitingPlayer {
                heroHealth(25)
            }
        }
    }

    def "minions can spawn after aoe"() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([Hellfire])
                mana(20)
            }
        }

        root = new HearthTreeNode(startingBoard)

        def abom = new Abomination()
        def ret = abom.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)
        abom.setHealth((byte) 1)

        def creeper = new HauntedCreeper()
        ret = creeper.useOn(CURRENT_PLAYER, CharacterIndex.HERO, ret)

        def copiedBoard = startingBoard.deepCopy()

        def hellfire = root.data_.getCurrentPlayer().getHand().get(0)
        ret = hellfire.useOn(CURRENT_PLAYER, CharacterIndex.HERO, ret)

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(Hellfire)
                removeMinion(CharacterIndex.MINION_2)
                removeMinion(CharacterIndex.MINION_1)
                mana(9)
                numCardsUsed(3)
                heroHealth(25)
                addMinionToField(SpectralSpider)
                addMinionToField(SpectralSpider)
            }
            waitingPlayer {
                heroHealth(25)
            }
        }
    }

    def "Sylvanas played before Cairne will not steal Baine"() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                mana(10)
            }
            waitingPlayer {
                mana(10)
            }
        }

        root = new HearthTreeNode(startingBoard)
        SylvanasWindrunner syl = new SylvanasWindrunner();
        syl.setHealth((byte) 1)
        root.data_.placeMinion(CURRENT_PLAYER, syl);
        root.data_.placeMinion(WAITING_PLAYER, new CairneBloodhoof());
        def copiedBoard = startingBoard.deepCopy()

        def attacker = root.data_.modelForSide(CURRENT_PLAYER).getCharacter(CharacterIndex.MINION_1)
        def ret = attacker.attack(WAITING_PLAYER, CharacterIndex.MINION_1, root)

        expect:
        assertFalse(ret == null);
        assert ret.numChildren() == 0

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeMinion(CharacterIndex.MINION_1)
            }
            waitingPlayer {
                removeMinion(CharacterIndex.MINION_1)
                addMinionToField(BaineBloodhoof)
            }
        }
    }

    def "Sylvanas played after Cairne will steal Baine"() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                mana(10)
            }
            waitingPlayer {
                mana(10)
            }
        }

        root = new HearthTreeNode(startingBoard)
        SylvanasWindrunner syl = new SylvanasWindrunner();
        syl.setHealth((byte) 1)
        root.data_.placeMinion(WAITING_PLAYER, new CairneBloodhoof());
        root.data_.placeMinion(CURRENT_PLAYER, syl);
        def copiedBoard = startingBoard.deepCopy()

        def attacker = root.data_.modelForSide(CURRENT_PLAYER).getCharacter(CharacterIndex.MINION_1)
        def ret = attacker.attack(WAITING_PLAYER, CharacterIndex.MINION_1, root)

        expect:
        assertFalse(ret == null);
        assert ret.numChildren() == 0

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeMinion(CharacterIndex.MINION_1)
                addMinionToField(BaineBloodhoof)
            }
            waitingPlayer {
                removeMinion(CharacterIndex.MINION_1)
            }
        }
    }
}
