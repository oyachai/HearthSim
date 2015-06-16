package com.hearthsim.test.groovy.card.goblinsvsgnomes.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.BloodfenRaptor
import com.hearthsim.card.basic.minion.BoulderfistOgre
import com.hearthsim.card.basic.minion.StonetuskBoar
import com.hearthsim.card.goblinsvsgnomes.minion.rare.KingOfBeasts
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER

class KingOfBeastsSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([KingOfBeasts])
                field([[minion: BoulderfistOgre], [minion:StonetuskBoar], [minion:BloodfenRaptor]])
                mana(10)
            }
        }
        root = new HearthTreeNode(startingBoard)
    }

    def "adds extra attack for each beast"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null;

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(KingOfBeasts, CharacterIndex.HERO)
                mana(5)
                numCardsUsed(1)
                updateMinion(CharacterIndex.MINION_1, [deltaAttack: +2])
            }
        }
    }
}
