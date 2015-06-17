package com.hearthsim.test.groovy.card.classic.spell

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.WarGolem
import com.hearthsim.card.basic.spell.Polymorph
import com.hearthsim.card.basic.spell.Soulfire
import com.hearthsim.model.BoardModel
import com.hearthsim.player.playercontroller.BruteForceSearchAI
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode
import com.hearthsim.util.tree.RandomEffectNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static org.junit.Assert.*

class SoulfireSpec extends CardSpec {
    
    HearthTreeNode root
    BoardModel startingBoard

    def "playing Soulfire"() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([Soulfire, Polymorph, WarGolem])
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
                mana(7)
                numCardsUsed(1)
            }
        }

        assertTrue(ret instanceof RandomEffectNode)
        assertEquals(ret.numChildren(), 2)
        
        HearthTreeNode child0 = ret.getChildren().get(0);
        assertBoardDelta(copiedBoard, child0.data_) {
            currentPlayer {
                heroHealth(26)
                removeCardFromHand(Soulfire)
                removeCardFromHand(Polymorph)
                numCardsUsed(1)
                mana(6)
            }
        }

        HearthTreeNode child1 = ret.getChildren().get(1);
        assertBoardDelta(copiedBoard, child1.data_) {
            currentPlayer {
                heroHealth(26)
                removeCardFromHand(Soulfire)
                removeCardFromHand(WarGolem)
                numCardsUsed(1)
                mana(6)
            }
        }
    }
    def "playing a complete turn with Soulfire"() {
        
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([Soulfire])
                mana(7)
            }
            waitingPlayer {
                mana(7)
            }
        }
        def copiedBoard = startingBoard.deepCopy()
        
        BruteForceSearchAI ai0 = BruteForceSearchAI.buildStandardAI1()
        ai0.scorer.enemyHeroHealthWeight = 20.0
        ai0.scorer.myHeroHealthWeight = 20.0
        def allMoves = ai0.playTurn(7, startingBoard)

        expect:
        assertTrue(allMoves.size() > 0)
        if (allMoves.size() > 0) {
            //If allMoves is empty, it means that there was absolutely nothing the AI could do
            def resBoard = allMoves.get(allMoves.size() - 1).board;
            assertBoardDelta(copiedBoard, resBoard) {
                currentPlayer {
                    removeCardFromHand(Soulfire)
                    numCardsUsed(1)
                    mana(6)
                }
                waitingPlayer {
                    heroHealth(26)
                }
            }
        }
    }

    def "playing a complete turn with 2 Soulfire"() {        
        
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([Soulfire, Soulfire])
                mana(7)
            }
            waitingPlayer {
                mana(7)
            }
        }
        def copiedBoard = startingBoard.deepCopy()
        
        BruteForceSearchAI ai0 = BruteForceSearchAI.buildStandardAI1()
        ai0.scorer.enemyHeroHealthWeight = 20.0
        ai0.scorer.myHeroHealthWeight = 20.0
        def allMoves = ai0.playTurn(7, startingBoard)

        expect:
        assertTrue(allMoves.size() > 0)
        if (allMoves.size() > 0) {
            def resBoard = allMoves.get(allMoves.size() - 1).board;
            assertBoardDelta(copiedBoard, resBoard) {
                currentPlayer {
                    removeCardFromHand(Soulfire)
                    removeCardFromHand(Soulfire)
                    numCardsUsed(1)
                    mana(6)
                }
                waitingPlayer {
                    heroHealth(26)
                }
            }
        }
    }

    def "playing a complete turn with 3 Soulfire"() {
        
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([Soulfire, Soulfire, Soulfire])
                mana(7)
            }
            waitingPlayer {
                mana(7)
            }
        }
        def copiedBoard = startingBoard.deepCopy()
        
        BruteForceSearchAI ai0 = BruteForceSearchAI.buildStandardAI1()
        ai0.scorer.enemyHeroHealthWeight = 20.0
        ai0.scorer.myHeroHealthWeight = 20.0
        def allMoves = ai0.playTurn(7, startingBoard)

        expect:
        assertTrue(allMoves.size() > 0)
        if (allMoves.size() > 0) {
            def resBoard = allMoves.get(allMoves.size() - 1).board;
            assertBoardDelta(copiedBoard, resBoard) {
                currentPlayer {
                    removeCardFromHand(Soulfire)
                    removeCardFromHand(Soulfire)
                    removeCardFromHand(Soulfire)
                    numCardsUsed(2)
                    mana(5)
                }
                waitingPlayer {
                    heroHealth(22)
                }
            }
        }
    }

    
}
