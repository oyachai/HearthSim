package com.hearthsim.test.groovy.card

import com.hearthsim.Game;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.player.playercontroller.BruteForceSearchAI;
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode;
import com.hearthsim.util.tree.RandomEffectNode
import com.hearthsim.card.Deck;
import com.hearthsim.card.spellcard.concrete.Soulfire
import com.hearthsim.card.spellcard.concrete.Polymorph
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.concrete.WarGolem
import com.hearthsim.card.minion.heroes.Mage;
import com.hearthsim.card.minion.heroes.Paladin;

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
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
		def theCard = root.data_.getCurrentPlayerCardHand(0)
		def ret = theCard.useOn(CURRENT_PLAYER, 0, root, null, null)

		expect:
		assertFalse(ret == null);

		assertBoardDelta(copiedBoard, ret.data_) {
			currentPlayer {
				mana(7)
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
			}
		}

		HearthTreeNode child1 = ret.getChildren().get(1);
		assertBoardDelta(copiedBoard, child1.data_) {
			currentPlayer {
				heroHealth(26)
				removeCardFromHand(Soulfire)
				removeCardFromHand(WarGolem)
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
				}
				waitingPlayer {
					heroHealth(22)
				}
			}
		}
	}

	
}
