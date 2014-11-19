package com.hearthsim.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.BloodfenRaptor;
import com.hearthsim.card.minion.concrete.ChillwindYeti;
import com.hearthsim.card.minion.concrete.DarkIronDwarf;
import com.hearthsim.card.minion.concrete.FaerieDragon;
import com.hearthsim.card.minion.concrete.RiverCrocolisk;
import com.hearthsim.card.minion.concrete.StranglethornTiger;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestMinionBattlecry {

	private HearthTreeNode board;

	@Before
	public void setUp() throws Exception {
		board = new HearthTreeNode(new BoardModel());

		board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new BloodfenRaptor());
		board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new ChillwindYeti());
		board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new RiverCrocolisk());

		board.data_.getCurrentPlayer().setMana((byte)8);
		board.data_.getCurrentPlayer().setMaxMana((byte)8);
	}

	@Test
	public void testCanBattlecryOwnStealthed() throws HSException {
		DarkIronDwarf darkIronDwarf = new DarkIronDwarf();
		board.data_.placeCardHand(PlayerSide.CURRENT_PLAYER, darkIronDwarf);
		board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new StranglethornTiger());

		Minion target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 0);
		HearthTreeNode ret = darkIronDwarf.useOn(PlayerSide.CURRENT_PLAYER, target, board, null, null);
		assertEquals(board, ret);
		assertEquals(board.numChildren(), 4); // 3 on friendly (including Tiger) and 1 on enemy
	}

	@Test
	@Ignore("Existing bug")
	public void testCannotBattlecryEnemyStealthed() throws HSException {
		DarkIronDwarf darkIronDwarf = new DarkIronDwarf();
		board.data_.placeCardHand(PlayerSide.CURRENT_PLAYER, darkIronDwarf);
		board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new FaerieDragon());

		Minion target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 0);
		HearthTreeNode ret = darkIronDwarf.useOn(PlayerSide.CURRENT_PLAYER, target, board, null, null);
		assertEquals(board, ret);
		assertEquals(board.numChildren(), 3); // 2 on friendly and 1 on enemy (excluding Tiger)
	}

	@Test
	public void testCanBattlecryOwnFaerie() throws HSException {
		DarkIronDwarf darkIronDwarf = new DarkIronDwarf();
		board.data_.placeCardHand(PlayerSide.CURRENT_PLAYER, darkIronDwarf);
		board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new FaerieDragon());

		Minion target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 0);
		HearthTreeNode ret = darkIronDwarf.useOn(PlayerSide.CURRENT_PLAYER, target, board, null, null);
		assertEquals(board, ret);
		assertEquals(board.numChildren(), 4); // 3 on friendly (including Faerie) and 1 on enemy
	}

	@Test
	public void testCanBattlecryEnemyFaerie() throws HSException {
		DarkIronDwarf darkIronDwarf = new DarkIronDwarf();
		board.data_.placeCardHand(PlayerSide.CURRENT_PLAYER, darkIronDwarf);
		board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new StranglethornTiger());

		Minion target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 0);
		HearthTreeNode ret = darkIronDwarf.useOn(PlayerSide.CURRENT_PLAYER, target, board, null, null);
		assertEquals(board, ret);
		assertEquals(board.numChildren(), 4); // 2 on friendly and 2 on enemy (including Faerie)
	}
}
