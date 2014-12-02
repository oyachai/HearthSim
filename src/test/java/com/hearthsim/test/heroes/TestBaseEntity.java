//package com.hearthsim.test.heroes;
//
//import com.hearthsim.util.tree.HearthTreeNode;
//import com.hearthsim.card.Card;
//import com.hearthsim.card.Deck;
//import com.hearthsim.card.minion.Hero;
//import com.hearthsim.card.minion.Minion;
//import com.hearthsim.card.minion.concrete.BoulderfistOgre;
//import com.hearthsim.card.minion.concrete.RaidLeader;
//import com.hearthsim.card.minion.heroes.Druid;
//import com.hearthsim.card.spellcard.concrete.TheCoin;
//import com.hearthsim.card.spellcard.concrete.WildGrowth;
//import com.hearthsim.exception.HSException;
//import com.hearthsim.model.BoardModel;
//import com.hearthsim.model.PlayerSide;
//import com.hearthsim.util.tree.HearthTreeNode;
//import org.junit.Before;
//import org.junit.Test;
//import static org.junit.Assert.*;
//
//public class TestBaseEntity
//{
//	private HearthTreeNode board;
//	private Deck deck;
//	
//	@Before
//	public void setup() {
//		board = new HearthTreeNode(new BoardModel(new Druid(), new Hero()));
//
//		BaseEntity minion0_0 = new BoulderfistOgre();
//		BaseEntity minion0_1 = new RaidLeader();
//		BaseEntity minion1_0 = new BoulderfistOgre();
//		BaseEntity minion1_1 = new RaidLeader();
//		
//		board.data_.placeCardHandCurrentPlayer(minion0_0);
//		board.data_.placeCardHandCurrentPlayer(minion0_1);
//				
//		board.data_.placeCardHandWaitingPlayer(minion1_0);
//		board.data_.placeCardHandWaitingPlayer(minion1_1);
//
//		Card cards[] = new Card[10];
//		for (int index = 0; index < 10; ++index) {
//			cards[index] = new TheCoin();
//		}
//	
//		deck = new Deck(cards);
//
//		Card fb = new WildGrowth();
//		board.data_.placeCardHandCurrentPlayer(fb);
//
//		board.data_.getCurrentPlayer().setMana((byte)8);
//		board.data_.getWaitingPlayer().setMana((byte)8);
//		
//		board.data_.getCurrentPlayer().setMaxMana((byte)8);
//		board.data_.getWaitingPlayer().setMaxMana((byte)8);
//		
//		HearthTreeNode tmpBoard = new HearthTreeNode(board.data_.flipPlayers());
//		try {
//			tmpBoard.data_.getCurrentPlayerCardHand(0).useOn(PlayerSide.CURRENT_PLAYER, tmpBoard.data_.getCurrentPlayerHero(), tmpBoard, deck, null);
//			tmpBoard.data_.getCurrentPlayerCardHand(0).useOn(PlayerSide.CURRENT_PLAYER, tmpBoard.data_.getCurrentPlayerHero(), tmpBoard, deck, null);
//		} catch (HSException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		board = new HearthTreeNode(tmpBoard.data_.flipPlayers());
//		try {
//			board.data_.getCurrentPlayerCardHand(0).useOn(PlayerSide.CURRENT_PLAYER, board.data_.getCurrentPlayerHero(), board, deck, null);
//			board.data_.getCurrentPlayerCardHand(0).useOn(PlayerSide.CURRENT_PLAYER, board.data_.getCurrentPlayerHero(), board, deck, null);
//		} catch (HSException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		board.data_.resetMana();
//		board.data_.resetMinions();
//		
//	}
//}
