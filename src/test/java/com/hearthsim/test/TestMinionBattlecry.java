package com.hearthsim.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.minion.concrete.Alexstrasza;
import com.hearthsim.card.minion.concrete.ArgentProtector;
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
		board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new RiverCrocolisk());
		board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new RiverCrocolisk());

		board.data_.getCurrentPlayer().setMana((byte)10);
		board.data_.getCurrentPlayer().setMaxMana((byte)10);
	}

	@Test
	public void testMinionIsPlayed() throws HSException {
		DarkIronDwarf darkIronDwarf = new DarkIronDwarf();
		board.data_.placeCardHand(PlayerSide.CURRENT_PLAYER, darkIronDwarf);

		HearthTreeNode ret = darkIronDwarf.useOn(PlayerSide.CURRENT_PLAYER, 0, board, null, null);
		assertEquals(board, ret);

		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 3);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 3);
		assertEquals(board.data_.getCurrentPlayer().getMana(), 6);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), 4);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), 4);
	}

	@Test
	// TODO explicitly verify the battlecry targeted correctly instead of relying on numChildren counts
	public void testTargetingFriendlyAndEnemyMinions() throws HSException {
		DarkIronDwarf darkIronDwarf = new DarkIronDwarf();
		board.data_.placeCardHand(PlayerSide.CURRENT_PLAYER, darkIronDwarf);

		HearthTreeNode ret = darkIronDwarf.useOn(PlayerSide.CURRENT_PLAYER, 0, board, null, null);
		assertEquals(board, ret);

		// At this point, the BoardState should have 5 children: 2 buffs on friendly side
		// and 3 buffs on enemy side
		assertEquals(board.numChildren(), 5);
	}

	@Test
	// TODO explicitly verify the battlecry targeted correctly instead of relying on numChildren counts
	public void testTargetingFriendlyMinions() throws HSException {
		ArgentProtector protector = new ArgentProtector();
		board.data_.placeCardHand(PlayerSide.CURRENT_PLAYER, protector);

		HearthTreeNode ret = protector.useOn(PlayerSide.CURRENT_PLAYER, 0, board, null, null);
		assertEquals(board, ret);

		// At this point, the BoardState should have 2 children: 2 buffs on friendly side
		assertEquals(board.numChildren(), 2);
	}

	@Test
	public void testTargetingFriendlyAndEnemyHeroes() throws HSException {
		Alexstrasza alexstrasza = new Alexstrasza();
		board.data_.placeCardHand(PlayerSide.CURRENT_PLAYER, alexstrasza);

		HearthTreeNode ret = alexstrasza.useOn(PlayerSide.CURRENT_PLAYER, 0, board, null, null);
		assertEquals(board, ret);

		// At this point, the BoardState should have 2 children: own hero and enemy hero
		assertEquals(board.numChildren(), 2);
	}

	@Test
	public void testCanBattlecryOwnStealthed() throws HSException {
		DarkIronDwarf darkIronDwarf = new DarkIronDwarf();
		board.data_.placeCardHand(PlayerSide.CURRENT_PLAYER, darkIronDwarf);
		board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new StranglethornTiger());

		HearthTreeNode ret = darkIronDwarf.useOn(PlayerSide.CURRENT_PLAYER, 0, board, null, null);
		assertEquals(board, ret);
		assertEquals(board.numChildren(), 6); // 3 on friendly (including Tiger) and 3 on enemy
	}

	@Test
	public void testCannotBattlecryEnemyStealthed() throws HSException {
		DarkIronDwarf darkIronDwarf = new DarkIronDwarf();
		board.data_.placeCardHand(PlayerSide.CURRENT_PLAYER, darkIronDwarf);
		board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new StranglethornTiger());

		HearthTreeNode ret = darkIronDwarf.useOn(PlayerSide.CURRENT_PLAYER, 0, board, null, null);
		assertEquals(board, ret);
		assertEquals(board.numChildren(), 5); // 2 on friendly and 3 on enemy (excluding Tiger)
	}

	@Test
	public void testCanBattlecryOwnFaerie() throws HSException {
		DarkIronDwarf darkIronDwarf = new DarkIronDwarf();
		board.data_.placeCardHand(PlayerSide.CURRENT_PLAYER, darkIronDwarf);
		board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new FaerieDragon());

		HearthTreeNode ret = darkIronDwarf.useOn(PlayerSide.CURRENT_PLAYER, 0, board, null, null);
		assertEquals(board, ret);
		assertEquals(board.numChildren(), 6); // 3 on friendly (including Faerie) and 3 on enemy
	}

	@Test
	public void testCanBattlecryEnemyFaerie() throws HSException {
		DarkIronDwarf darkIronDwarf = new DarkIronDwarf();
		board.data_.placeCardHand(PlayerSide.CURRENT_PLAYER, darkIronDwarf);
		board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new FaerieDragon());

		HearthTreeNode ret = darkIronDwarf.useOn(PlayerSide.CURRENT_PLAYER, 0, board, null, null);
		assertEquals(board, ret);
		assertEquals(board.numChildren(), 6); // 2 on friendly and 4 on enemy (including Faerie)
	}
}
