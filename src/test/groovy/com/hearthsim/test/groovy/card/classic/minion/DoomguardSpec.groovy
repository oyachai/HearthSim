package com.hearthsim.test.groovy.card.classic.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.spell.HolySmite
import com.hearthsim.card.basic.spell.TheCoin
import com.hearthsim.card.classic.minion.rare.Doomguard
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertFalse

class DoomguardSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def "playing Doomguard with no other cards in hand"() {

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([Doomguard])
                mana(7)
            }
            waitingPlayer {
                mana(4)
            }
        }

        root = new HearthTreeNode(startingBoard)

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinionWithCharge(Doomguard)
                mana(2)
                numCardsUsed(1)
            }
        }
    }

    
    def "playing Doomguard with no one other card in hand"() {
        
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([Doomguard, TheCoin])
                mana(7)
            }
            waitingPlayer {
                mana(4)
            }
        }

        root = new HearthTreeNode(startingBoard)

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinionWithCharge(Doomguard)
                mana(2)
                numCardsUsed(1)
            }
        }
        
        assertEquals(ret.numChildren(), 1);
        
        HearthTreeNode child0 = ret.getChildren().get(0);
        assertBoardDelta(copiedBoard, child0.data_) {
            currentPlayer {
                playMinionWithCharge(Doomguard)
                mana(2)
                removeCardFromHand(TheCoin)
                numCardsUsed(1)
            }
        }

    }
        
    
    
    
    def "playing Doomguard with no two other cards in hand"() {
        
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([Doomguard, TheCoin, HolySmite])
                mana(7)
            }
            waitingPlayer {
                mana(4)
            }
        }

        root = new HearthTreeNode(startingBoard)

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinionWithCharge(Doomguard)
                mana(2)
                numCardsUsed(1)
            }
        }
        
        assertEquals(ret.numChildren(), 1);
        
        HearthTreeNode child0 = ret.getChildren().get(0);
        assertBoardDelta(copiedBoard, child0.data_) {
            currentPlayer {
                playMinionWithCharge(Doomguard)
                mana(2)
                removeCardFromHand(TheCoin)
                removeCardFromHand(HolySmite)
                numCardsUsed(1)
            }
        }

    }
}
