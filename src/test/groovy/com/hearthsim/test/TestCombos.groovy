package com.hearthsim.test

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.GoldshireFootman
import com.hearthsim.card.basic.spell.DrainLife
import com.hearthsim.card.classic.minion.common.BloodsailRaider
import com.hearthsim.card.classic.minion.legendary.Malygos
import com.hearthsim.card.classic.minion.legendary.TheBlackKnight
import com.hearthsim.card.classic.minion.rare.SunfuryProtector
import com.hearthsim.card.goblinsvsgnomes.minion.common.ExplosiveSheep
import com.hearthsim.card.goblinsvsgnomes.minion.common.MicroMachine
import com.hearthsim.card.goblinsvsgnomes.minion.rare.BombLobber
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode
import com.hearthsim.util.tree.RandomEffectNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER

/**
 * Created by oyachai on 6/21/15.
 */
class TestCombos extends CardSpec {


    HearthTreeNode root
    BoardModel startingBoard

    def "BombLobber into Explosive Sheep"() {

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                heroHealth(30)
                hand([BombLobber])
                field([[minion: MicroMachine], [minion: SunfuryProtector]])
                mana(5)
            }
            waitingPlayer {
                heroHealth(28)
                hand([Malygos, TheBlackKnight, BloodsailRaider, DrainLife])
                field([[minion: GoldshireFootman], [minion: ExplosiveSheep]])
                mana(6)
            }
        }

        root = new HearthTreeNode(startingBoard)

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.MINION_2, root)

        expect:
        ret != null;
        ret instanceof RandomEffectNode;

        HearthTreeNode child0 = ret.getChildren().get(0);
        assertBoardDelta(copiedBoard, child0.data_) {
            currentPlayer {
                playMinion(BombLobber, CharacterIndex.MINION_2)
                mana(0)
                numCardsUsed(1)
            }
            waitingPlayer {
                removeMinion(CharacterIndex.MINION_1)
            }
        }

        HearthTreeNode child1 = ret.getChildren().get(1);
        assertBoardDelta(copiedBoard, child1.data_) {
            currentPlayer {
                playMinion(BombLobber, CharacterIndex.MINION_2)
                removeMinion(CharacterIndex.MINION_1)
                updateMinion(CharacterIndex.MINION_1, [deltaHealth: -2])
                updateMinion(CharacterIndex.MINION_2, [deltaHealth: -2])
                mana(0)
                numCardsUsed(1)
            }
            waitingPlayer {
                removeMinion(CharacterIndex.MINION_1)
                removeMinion(CharacterIndex.MINION_1)
            }
        }

    }
}
