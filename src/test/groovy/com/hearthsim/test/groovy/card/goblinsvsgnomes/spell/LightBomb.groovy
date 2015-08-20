package com.hearthsim.test.groovy.card.goblinsvsgnomes.spell

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.ChillwindYeti
import com.hearthsim.card.basic.minion.DalaranMage
import com.hearthsim.card.classic.minion.common.Shieldbearer
import com.hearthsim.card.goblinsvsgnomes.minion.common.GilblinStalker
import com.hearthsim.card.goblinsvsgnomes.spell.epic.Lightbomb
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static org.junit.Assert.assertEquals

class LightBomb extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def "deal damage to each minion equal to its attack"() {

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([Lightbomb])
                field([[minion: Shieldbearer], [minion: GilblinStalker], [minion: ChillwindYeti]])
                mana(6)
            }
            waitingPlayer {
                field([[minion: Shieldbearer], [minion: GilblinStalker], [minion: ChillwindYeti]])
            }
        }

        root = new HearthTreeNode(startingBoard)

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        assertEquals(root, ret);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(Lightbomb)
                mana(0)
                numCardsUsed(1)
                updateMinion(CharacterIndex.MINION_1, [deltaHealth:0])
                updateMinion(CharacterIndex.MINION_2, [deltaHealth:-2])
                updateMinion(CharacterIndex.MINION_3, [deltaHealth:-4])
            }
            waitingPlayer {
                updateMinion(CharacterIndex.MINION_1, [deltaHealth:0])
                updateMinion(CharacterIndex.MINION_2, [deltaHealth:-2])
                updateMinion(CharacterIndex.MINION_3, [deltaHealth:-4])
            }
        }
    }

    def "affected by spellpower"() {

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([Lightbomb])
                field([[minion: Shieldbearer], [minion: GilblinStalker], [minion: ChillwindYeti], [minion: DalaranMage]])
                mana(6)
            }
            waitingPlayer {
                field([[minion: Shieldbearer], [minion: GilblinStalker], [minion: ChillwindYeti]])
            }
        }

        root = new HearthTreeNode(startingBoard)

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        assertEquals(root, ret);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(Lightbomb)
                mana(0)
                numCardsUsed(1)
                updateMinion(CharacterIndex.MINION_1, [deltaHealth:-1])
                updateMinion(CharacterIndex.MINION_4, [deltaHealth:-2])
                removeMinion(CharacterIndex.MINION_3)
                removeMinion(CharacterIndex.MINION_2)
            }
            waitingPlayer {
                updateMinion(CharacterIndex.MINION_1, [deltaHealth:-1])
                removeMinion(CharacterIndex.MINION_3)
                removeMinion(CharacterIndex.MINION_2)

            }
        }
    }

}
