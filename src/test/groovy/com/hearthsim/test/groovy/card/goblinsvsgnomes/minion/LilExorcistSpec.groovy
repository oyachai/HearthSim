package com.hearthsim.test.groovy.card.goblinsvsgnomes.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.BloodfenRaptor
import com.hearthsim.card.classic.minion.common.LootHoarder
import com.hearthsim.card.curseofnaxxramas.minion.common.HauntedCreeper
import com.hearthsim.card.goblinsvsgnomes.minion.rare.LilExorcist
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER

class LilExorcistSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([LilExorcist])
                mana(10)
            }
            waitingPlayer {
                field([[minion: HauntedCreeper], [minion:LootHoarder], [minion:BloodfenRaptor]])
            }
        }
        root = new HearthTreeNode(startingBoard)
    }

    def "adds extra 1/1 for each enemy deathrattle"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null;

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(LilExorcist, CharacterIndex.HERO)
                mana(7)
                numCardsUsed(1)
                updateMinion(CharacterIndex.MINION_1, [deltaAttack: +2, deltaHealth: +2, deltaMaxHealth: +2])
            }
        }
    }

    def "does not count friendly deathrattle"() {
        startingBoard.placeMinion(CURRENT_PLAYER, new HauntedCreeper())

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null;

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(LilExorcist, CharacterIndex.HERO)
                mana(7)
                numCardsUsed(1)
                updateMinion(CharacterIndex.MINION_1, [deltaAttack: +2, deltaHealth: +2, deltaMaxHealth: +2])
            }
        }
    }
}
