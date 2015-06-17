package com.hearthsim.test.groovy.card.classic.minion

import com.hearthsim.Game
import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.GoldshireFootman
import com.hearthsim.card.classic.minion.legendary.RagnarosTheFirelord
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static org.junit.Assert.assertFalse


/**
 * Created by oyachai on 4/5/15.
 */
class RagnarosTheFirelordSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([RagnarosTheFirelord])
                mana(8)
            }
            waitingPlayer {
                mana(4)
                field([[minion: GoldshireFootman]])
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "playing Blood Imp"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(RagnarosTheFirelord)
                mana(0)
                numCardsUsed(1)
            }
        }

        def retAfterEndTurn = new HearthTreeNode(Game.endTurn(ret.data_))
        if (retAfterEndTurn.data_.getWaitingPlayer().getHero().getHealth() < 30) {
            assertBoardDelta(copiedBoard, retAfterEndTurn.data_) {
                currentPlayer {
                    playMinion(RagnarosTheFirelord)
                    mana(0)
                    numCardsUsed(1)
                }
                waitingPlayer {
                    heroHealth(22)
                }
            }
        } else {
            assertBoardDelta(copiedBoard, retAfterEndTurn.data_) {
                currentPlayer {
                    playMinion(RagnarosTheFirelord)
                    mana(0)
                    numCardsUsed(1)
                }
                waitingPlayer {
                    removeMinion(CharacterIndex.MINION_1)
                }
            }
        }

    }
}
