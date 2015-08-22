package com.hearthsim.test.groovy.card.blackrockmountain.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.Voidwalker
import com.hearthsim.card.basic.minion.WarGolem
import com.hearthsim.card.blackrockmountain.minion.rare.DarkIronSkulker
import com.hearthsim.card.goblinsvsgnomes.minion.common.GilblinStalker
import com.hearthsim.card.goblinsvsgnomes.spell.epic.TreeOfLife
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER

class DarkIronSkulkerSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([DarkIronSkulker])
                field([[minion: GilblinStalker],[minion: WarGolem]])
                mana(10)
            }
            waitingPlayer {
                mana(10)
                field([[minion: GilblinStalker],[minion: WarGolem]])
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "deal 2 damage to all undamaged enemy minions"() {


        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.MINION_2, root)

        expect:
        ret != null;

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(DarkIronSkulker)
                mana(5)
                numCardsUsed(1)
            }
            waitingPlayer {
                heroHealth(30)
                updateMinion(CharacterIndex.MINION_1, [deltaHealth:-2])
                updateMinion(CharacterIndex.MINION_2, [deltaHealth:-2])
            }
        }

    }

    def "deal no damage to damaged enemy minions"() {

        startingBoard.getWaitingPlayer().getCharacter(CharacterIndex.MINION_1).setHealth((byte)1);
        startingBoard.getWaitingPlayer().getCharacter(CharacterIndex.MINION_2).setHealth((byte)1);

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.MINION_2, root)

        expect:
        ret != null;

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(DarkIronSkulker)
                mana(5)
                numCardsUsed(1)
            }
            waitingPlayer {
                heroHealth(30)
                updateMinion(CharacterIndex.MINION_1, [deltaHealth:0])
                updateMinion(CharacterIndex.MINION_2, [deltaHealth:0])
            }
        }

    }

}
