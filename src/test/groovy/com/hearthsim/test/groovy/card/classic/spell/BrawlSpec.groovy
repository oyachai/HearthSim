package com.hearthsim.test.groovy.card.classic.spell

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.Voidwalker
import com.hearthsim.card.basic.minion.WarGolem
import com.hearthsim.card.classic.minion.common.FaerieDragon
import com.hearthsim.card.classic.minion.common.StranglethornTiger
import com.hearthsim.card.classic.spell.epic.Brawl
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode
import com.hearthsim.util.tree.RandomEffectNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER

class BrawlSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([Brawl])
                field([[minion: Voidwalker], [minion: WarGolem]])
                mana(10)
            }
            waitingPlayer {
                field([[minion: FaerieDragon],[minion: StranglethornTiger]])
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "returned node is null for no targets"() {
        startingBoard.removeMinion(CURRENT_PLAYER, CharacterIndex.MINION_1);
        startingBoard.removeMinion(CURRENT_PLAYER, CharacterIndex.MINION_1);
        startingBoard.removeMinion(WAITING_PLAYER, CharacterIndex.MINION_1);
        startingBoard.removeMinion(WAITING_PLAYER, CharacterIndex.MINION_1);

        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret == null
    }

    def "returned node is null for only one target"() {
        startingBoard.removeMinion(CURRENT_PLAYER, CharacterIndex.MINION_1);
        startingBoard.removeMinion(WAITING_PLAYER, CharacterIndex.MINION_1);
        startingBoard.removeMinion(WAITING_PLAYER, CharacterIndex.MINION_1);

        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret == null
    }

    def "returned node is RNG for two or more targets"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null
        ret instanceof RandomEffectNode
        ret.numChildren() == 4

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                numCardsUsed(1)
            }
        }
    }

    def "hits all minions but one"() {
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null
        ret instanceof RandomEffectNode
        ret.numChildren() == 4

        def copiedBoard = ret.data_.deepCopy()

        HearthTreeNode child0 = ret.getChildren().get(0);
        assertBoardDelta(copiedBoard, child0.data_) {
            currentPlayer {
                removeCardFromHand(Brawl)
                mana(5)
                removeMinion(CharacterIndex.MINION_2)
            }
            waitingPlayer {
                removeMinion(CharacterIndex.MINION_2)
                removeMinion(CharacterIndex.MINION_1)
            }
        }

        HearthTreeNode child1 = ret.getChildren().get(1);
        assertBoardDelta(copiedBoard, child1.data_) {
            currentPlayer {
                removeCardFromHand(Brawl)
                mana(5)
                removeMinion(CharacterIndex.MINION_1)
            }
            waitingPlayer {
                removeMinion(CharacterIndex.MINION_2)
                removeMinion(CharacterIndex.MINION_1)
            }
        }

        HearthTreeNode child2 = ret.getChildren().get(2);
        assertBoardDelta(copiedBoard, child2.data_) {
            currentPlayer {
                removeCardFromHand(Brawl)
                mana(5)
                removeMinion(CharacterIndex.MINION_2)
                removeMinion(CharacterIndex.MINION_1)
            }
            waitingPlayer {
                removeMinion(CharacterIndex.MINION_2)
            }
        }

        HearthTreeNode child3 = ret.getChildren().get(3);
        assertBoardDelta(copiedBoard, child3.data_) {
            currentPlayer {
                removeCardFromHand(Brawl)
                mana(5)
                removeMinion(CharacterIndex.MINION_2)
                removeMinion(CharacterIndex.MINION_1)
            }
            waitingPlayer {
                removeMinion(CharacterIndex.MINION_1)
            }
        }
    }
}
