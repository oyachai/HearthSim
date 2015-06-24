package com.hearthsim.test.groovy.card.classic.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.SilverHandRecruit
import com.hearthsim.card.basic.minion.Voidwalker
import com.hearthsim.card.basic.minion.WarGolem
import com.hearthsim.card.classic.minion.common.FaerieDragon
import com.hearthsim.card.classic.minion.common.StranglethornTiger
import com.hearthsim.card.classic.minion.rare.MindControlTech
import com.hearthsim.card.curseofnaxxramas.minion.rare.SludgeBelcher
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode
import com.hearthsim.util.tree.RandomEffectNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER

class MindControlTechSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([MindControlTech])
                field([[minion: Voidwalker], [minion: WarGolem]])
                mana(10)
            }
            waitingPlayer {
                field([[minion: FaerieDragon],[minion: StranglethornTiger],[minion: SludgeBelcher],[minion: SilverHandRecruit]])
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "returned node is normal for no targets"() {
        startingBoard.removeMinion(WAITING_PLAYER, CharacterIndex.MINION_1);
        startingBoard.removeMinion(WAITING_PLAYER, CharacterIndex.MINION_1);
        startingBoard.removeMinion(WAITING_PLAYER, CharacterIndex.MINION_1);
        startingBoard.removeMinion(WAITING_PLAYER, CharacterIndex.MINION_1);

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null
        ret instanceof HearthTreeNode
        !(ret instanceof RandomEffectNode)

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(MindControlTech, CharacterIndex.HERO)
                mana(7)
                numCardsUsed(1)
            }
        }
    }

    def "returned node is normal for only three targets"() {
        startingBoard.removeMinion(WAITING_PLAYER, CharacterIndex.MINION_1);

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null
        ret instanceof HearthTreeNode
        !(ret instanceof RandomEffectNode)

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(MindControlTech, CharacterIndex.HERO)
                mana(7)
                numCardsUsed(1)
            }
        }
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
                playMinion(MindControlTech, CharacterIndex.HERO)
                mana(7)
                numCardsUsed(1)
            }
        }
    }

    def "hits enemy minions"() {
        def copiedBoard = root.data_.deepCopy()

        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null
        ret instanceof RandomEffectNode
        ret.numChildren() == 4

        HearthTreeNode child0 = ret.getChildren().get(0);
        assertBoardDelta(copiedBoard, child0.data_) {
            currentPlayer {
                playMinion(MindControlTech, CharacterIndex.HERO)
                addMinionToField(FaerieDragon)
                mana(7)
                numCardsUsed(1)
            }
            waitingPlayer {
                removeMinion(CharacterIndex.MINION_1)
            }
        }

        HearthTreeNode child1 = ret.getChildren().get(1);
        assertBoardDelta(copiedBoard, child1.data_) {
            currentPlayer {
                playMinion(MindControlTech, CharacterIndex.HERO)
                addMinionToField(StranglethornTiger)
                mana(7)
                numCardsUsed(1)
            }
            waitingPlayer {
                removeMinion(CharacterIndex.MINION_2)
            }
        }

        HearthTreeNode child2 = ret.getChildren().get(2);
        assertBoardDelta(copiedBoard, child2.data_) {
            currentPlayer {
                playMinion(MindControlTech, CharacterIndex.HERO)
                addMinionToField(SludgeBelcher)
                mana(7)
                numCardsUsed(1)
            }
            waitingPlayer {
                removeMinion(CharacterIndex.MINION_3)
            }
        }

        HearthTreeNode child3 = ret.getChildren().get(3);
        assertBoardDelta(copiedBoard, child3.data_) {
            currentPlayer {
                playMinion(MindControlTech, CharacterIndex.HERO)
                addMinionToField(SilverHandRecruit)
                mana(7)
                numCardsUsed(1)
            }
            waitingPlayer {
                removeMinion(CharacterIndex.MINION_4)
            }
        }
    }
}
