package com.hearthsim.test.groovy.card.goblinsvsgnomes.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.BloodfenRaptor
import com.hearthsim.card.basic.minion.WarGolem
import com.hearthsim.card.basic.spell.HealingTouch
import com.hearthsim.card.classic.minion.common.FaerieDragon
import com.hearthsim.card.classic.minion.common.StranglethornTiger
import com.hearthsim.card.curseofnaxxramas.minion.common.ZombieChow
import com.hearthsim.card.goblinsvsgnomes.minion.rare.Shadowboxer
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode
import com.hearthsim.util.tree.RandomEffectNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER

class ShadowboxerSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([HealingTouch])
                heroHealth(20)
                field([[minion: Shadowboxer], [minion: WarGolem]])
                mana(10)
            }
            waitingPlayer {
                hand([BloodfenRaptor])
                field([[minion: FaerieDragon],[minion: StranglethornTiger]])
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "returned node is normal for only one target"() {
        startingBoard.removeMinion(WAITING_PLAYER, CharacterIndex.MINION_1);
        startingBoard.removeMinion(WAITING_PLAYER, CharacterIndex.MINION_1);

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)
        ret = ret.getChildren().get(0);

        expect:
        ret != null
        ret instanceof HearthTreeNode
        !(ret instanceof RandomEffectNode)

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(HealingTouch)
                heroHealth(28)
                mana(7)
                numCardsUsed(1)
            }
            waitingPlayer {
                heroHealth(29)
            }
        }
    }

    def "does trigger on enemy play"() {
        startingBoard.removeMinion(CURRENT_PLAYER, CharacterIndex.MINION_1);
        startingBoard.removeMinion(CURRENT_PLAYER, CharacterIndex.MINION_1);
        startingBoard.placeMinion(WAITING_PLAYER, new Shadowboxer())

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)
        ret = ret.getChildren().get(0);

        expect:
        ret != null
        ret instanceof HearthTreeNode
        !(ret instanceof RandomEffectNode)

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(HealingTouch)
                heroHealth(27)
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
        ret.numChildren() == 3

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(HealingTouch)
                heroHealth(28)
                mana(7)
                numCardsUsed(1)
            }
        }
    }

    def "hits all enemies"() {
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null
        ret instanceof RandomEffectNode
        ret.numChildren() == 3

        def copiedBoard = ret.data_.deepCopy()

        HearthTreeNode child0 = ret.getChildren().get(0);
        assertBoardDelta(copiedBoard, child0.data_) {
            waitingPlayer {
                heroHealth(29)
            }
        }

        HearthTreeNode child1 = ret.getChildren().get(1);
        assertBoardDelta(copiedBoard, child1.data_) {
            waitingPlayer {
                updateMinion(CharacterIndex.MINION_1, [deltaHealth: -1])
            }
        }

        HearthTreeNode child2 = ret.getChildren().get(2);
        assertBoardDelta(copiedBoard, child2.data_) {
            waitingPlayer {
                updateMinion(CharacterIndex.MINION_2, [deltaHealth: -1])
            }
        }
    }

    def "does not trigger deathrattles that happened already"() {

        root.data_.placeMinion(WAITING_PLAYER, new ZombieChow())
        def zombieChow = root.data_.getCharacter(WAITING_PLAYER, CharacterIndex.MINION_3)
        zombieChow.setHealth((byte)1)

        def copiedBoard = startingBoard.deepCopy()

        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null
        ret instanceof RandomEffectNode
        ret.numChildren() == 4

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(HealingTouch)
                heroHealth(28)
                mana(7)
                numCardsUsed(1)
            }
        }

        HearthTreeNode child0 = ret.getChildren().get(0);
        assertBoardDelta(ret.data_, child0.data_) {
            waitingPlayer {
                heroHealth(29)
            }
        }

        HearthTreeNode child1 = ret.getChildren().get(1);
        assertBoardDelta(ret.data_, child1.data_) {
            waitingPlayer {
                updateMinion(CharacterIndex.MINION_1, [deltaHealth: -1])
            }
        }

        HearthTreeNode child2 = ret.getChildren().get(2);
        assertBoardDelta(ret.data_, child2.data_) {
            waitingPlayer {
                updateMinion(CharacterIndex.MINION_2, [deltaHealth: -1])
            }
        }

        HearthTreeNode child3 = ret.getChildren().get(3);
        assertBoardDelta(ret.data_, child3.data_) {
            currentPlayer {
                heroHealth(30)
            }
            waitingPlayer {
                removeMinion(CharacterIndex.MINION_3)
            }
        }

        // Grandchildren

        HearthTreeNode gchild0 = child3.getChildren().get(0);
        assertBoardDelta(child3.data_, gchild0.data_) {
            waitingPlayer {
                heroHealth(29)
            }
        }

        HearthTreeNode gchild1 = child3.getChildren().get(1);
        assertBoardDelta(child3.data_, gchild1.data_) {
            waitingPlayer {
                updateMinion(CharacterIndex.MINION_1, [deltaHealth: -1])
            }
        }

        HearthTreeNode gchild2 = child3.getChildren().get(2);
        assertBoardDelta(child3.data_, gchild2.data_) {
            waitingPlayer {
                updateMinion(CharacterIndex.MINION_2, [deltaHealth: -1])
            }
        }

    }
}
