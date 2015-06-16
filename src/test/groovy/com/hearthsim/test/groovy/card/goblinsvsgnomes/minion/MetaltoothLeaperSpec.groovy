package com.hearthsim.test.groovy.card.goblinsvsgnomes.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.StonetuskBoar
import com.hearthsim.card.classic.minion.common.HarvestGolem
import com.hearthsim.card.goblinsvsgnomes.minion.common.AnnoyOTron
import com.hearthsim.card.goblinsvsgnomes.minion.rare.MetaltoothLeaper
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER

class MetaltoothLeaperSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([MetaltoothLeaper])
                field([[minion: HarvestGolem], [minion:StonetuskBoar], [minion:AnnoyOTron]])
                mana(10)
            }
        }
        root = new HearthTreeNode(startingBoard)
    }

    def "buffs friendly minions"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null;

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(MetaltoothLeaper, CharacterIndex.HERO)
                mana(7)
                numCardsUsed(1)
                updateMinion(CharacterIndex.MINION_2, [deltaAttack: +2])
                updateMinion(CharacterIndex.MINION_4, [deltaAttack: +2])
            }
        }
    }
}
