package com.hearthsim.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.minion.concrete.Archmage;
import com.hearthsim.card.minion.concrete.BloodfenRaptor;
import com.hearthsim.card.minion.concrete.ChillwindYeti;
import com.hearthsim.card.minion.concrete.KoboldGeomancer;
import com.hearthsim.card.minion.concrete.RiverCrocolisk;
import com.hearthsim.card.minion.concrete.SilverHandRecruit;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestMinionPlacement {

	private HearthTreeNode board;
	private BloodfenRaptor raptor;
	private ChillwindYeti yeti;
	private RiverCrocolisk croc;
	

	@Before
	public void setUp() throws Exception {
		board = new HearthTreeNode(new BoardModel());
		
		raptor = new BloodfenRaptor();
		yeti = new ChillwindYeti();
		croc = new RiverCrocolisk();
		
		board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, raptor);
		board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, yeti);
		board.data_.placeMinion(PlayerSide.WAITING_PLAYER, croc);

		board.data_.getCurrentPlayer().setMana((byte)8);
		board.data_.getWaitingPlayer().setMana((byte)8);
		
		board.data_.getCurrentPlayer().setMaxMana((byte)8);
		board.data_.getWaitingPlayer().setMaxMana((byte)8);
	}

	@Test
	public void testCanBeUsedOnOpponentHero() throws HSException {
		Archmage archmage = new Archmage();
		board.data_.placeCardHandCurrentPlayer(archmage);

		HearthTreeNode ret = archmage.useOn(PlayerSide.WAITING_PLAYER, 0, this.board, null, null);
		assertNull(ret);
		
		assertEquals(board.data_.getNumCards_hand(), 1);
		assertEquals(board.data_.getCurrentPlayer().getMana(), 8);
		assertEquals(board.data_.getWaitingPlayer().getMana(), 8);		
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 1);
	}

	@Test
	public void testCanBeUsedOnOpponentMinion() throws HSException {
		Archmage archmage = new Archmage();
		board.data_.placeCardHandCurrentPlayer(archmage);

		HearthTreeNode ret = archmage.useOn(PlayerSide.WAITING_PLAYER, 1, this.board, null, null);
		assertNull(ret);
		
		assertEquals(board.data_.getNumCards_hand(), 1);
		assertEquals(board.data_.getCurrentPlayer().getMana(), 8);
		assertEquals(board.data_.getWaitingPlayer().getMana(), 8);		
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 1);
	}
	
	@Test
	public void testCanBeUsedOnOwnHero() throws HSException {
		Archmage archmage = new Archmage();
		board.data_.placeCardHandCurrentPlayer(archmage);

		HearthTreeNode ret = archmage.useOn(PlayerSide.CURRENT_PLAYER, 0, this.board, null, null);
		assertEquals(board, ret);
		
		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(board.data_.getCurrentPlayer().getMana(), 2);
		assertEquals(board.data_.getWaitingPlayer().getMana(), 8);		

		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 3);
		assertEquals(archmage, this.board.data_.getCurrentPlayerCharacter(1));
		assertEquals(raptor, this.board.data_.getCurrentPlayerCharacter(2));
		assertEquals(yeti, this.board.data_.getCurrentPlayerCharacter(3));

		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 1);
	}

	@Test
	public void testCanBeUsedOnOwnMinionMiddle() throws HSException {
		Archmage archmage = new Archmage();
		board.data_.placeCardHandCurrentPlayer(archmage);

		HearthTreeNode ret = archmage.useOn(PlayerSide.CURRENT_PLAYER, 1, this.board, null, null);
		assertEquals(board, ret);
		
		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(board.data_.getCurrentPlayer().getMana(), 2);
		assertEquals(board.data_.getWaitingPlayer().getMana(), 8);		

		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 3);
		assertEquals(raptor, this.board.data_.getCurrentPlayerCharacter(1));
		assertEquals(archmage, this.board.data_.getCurrentPlayerCharacter(2));
		assertEquals(yeti, this.board.data_.getCurrentPlayerCharacter(3));

		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 1);
	}

	@Test
	public void testCanBeUsedOnOwnMinionRight() throws HSException {
		Archmage archmage = new Archmage();
		board.data_.placeCardHandCurrentPlayer(archmage);

		HearthTreeNode ret = archmage.useOn(PlayerSide.CURRENT_PLAYER, 2, this.board, null, null);
		assertEquals(board, ret);
		
		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(board.data_.getCurrentPlayer().getMana(), 2);
		assertEquals(board.data_.getWaitingPlayer().getMana(), 8);		

		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 3);
		assertEquals(raptor, this.board.data_.getCurrentPlayerCharacter(1));
		assertEquals(yeti, this.board.data_.getCurrentPlayerCharacter(2));
		assertEquals(archmage, this.board.data_.getCurrentPlayerCharacter(3));

		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 1);
	}

	@Test
	public void testCanBeUsedOnFullBoard() throws HSException {
		board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new SilverHandRecruit());
		board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new SilverHandRecruit());
		board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new SilverHandRecruit());
		board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new SilverHandRecruit());
		board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new SilverHandRecruit());

		Archmage archmage = new Archmage();
		board.data_.placeCardHandCurrentPlayer(archmage);

		HearthTreeNode ret = archmage.useOn(PlayerSide.CURRENT_PLAYER, 1, this.board, null, null);
		assertNull(ret);
		
		assertEquals(board.data_.getNumCards_hand(), 1);
		assertEquals(board.data_.getCurrentPlayer().getMana(), 8);
		assertEquals(board.data_.getWaitingPlayer().getMana(), 8);		
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 7);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 1);
	}

	@Test
	public void testAddsSpellDamage() throws HSException {
		Archmage archmage = new Archmage();
		board.data_.placeCardHandCurrentPlayer(archmage);

		HearthTreeNode ret = archmage.useOn(PlayerSide.CURRENT_PLAYER, 1, this.board, null, null);
		assertEquals(board, ret);
		
		assertEquals(1, board.data_.getSpellDamage(PlayerSide.CURRENT_PLAYER));
	}

	@Test
	public void testSpellDamageStacks() throws HSException {
		KoboldGeomancer kobold = new KoboldGeomancer();
		board.data_.placeCardHandCurrentPlayer(kobold);

		HearthTreeNode ret = kobold.useOn(PlayerSide.CURRENT_PLAYER, 1, this.board, null, null);
		assertEquals(board, ret);

		kobold = new KoboldGeomancer();
		board.data_.placeCardHandCurrentPlayer(kobold);

		ret = kobold.useOn(PlayerSide.CURRENT_PLAYER, 1, this.board, null, null);
		assertEquals(board, ret);

		assertEquals(2, board.data_.getSpellDamage(PlayerSide.CURRENT_PLAYER));
	}
}
