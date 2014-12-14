package com.hearthsim.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.BloodfenRaptor;
import com.hearthsim.card.minion.concrete.BluegillWarrior;
import com.hearthsim.card.minion.concrete.ChillwindYeti;
import com.hearthsim.card.minion.concrete.FaerieDragon;
import com.hearthsim.card.minion.concrete.MurlocRaider;
import com.hearthsim.card.minion.concrete.RiverCrocolisk;
import com.hearthsim.card.minion.concrete.StranglethornTiger;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestMinionAttacking {

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
	public void testCharge() throws HSException {
		BluegillWarrior murloc = new BluegillWarrior();
		board.data_.placeCardHandCurrentPlayer(murloc);

		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board, null, null);
		assertEquals(board, ret);

		Minion theAttacker = PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0);
		assertTrue(theAttacker.canAttack());

		Minion target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 0);
		ret = theAttacker.attack(PlayerSide.WAITING_PLAYER, target, board, null, null);
		assertEquals(board, ret);

		assertEquals(30, board.data_.getCurrentPlayerHero().getHealth());
		assertEquals(28, board.data_.getWaitingPlayerHero().getHealth());
	}

	@Test
	public void testSummoningSickness() throws HSException {
		MurlocRaider murloc = new MurlocRaider();
		board.data_.placeCardHandCurrentPlayer(murloc);

		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board, null, null);
		assertEquals(board, ret);

		Minion theAttacker = PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0);
		assertFalse(theAttacker.canAttack());

		Minion target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 0);
		ret = theAttacker.attack(PlayerSide.WAITING_PLAYER, target, board, null, null);
		assertNull(ret);

		assertEquals(30, board.data_.getWaitingPlayerHero().getHealth());
	}

	@Test
	public void testAttackEnemyHero() throws HSException {
		Minion target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 0);
		HearthTreeNode ret = raptor.attack(PlayerSide.WAITING_PLAYER, target, board, null, null);
		assertEquals(board, ret);

		assertFalse(raptor.canAttack());
		assertEquals(30, board.data_.getCurrentPlayerHero().getHealth());
		assertEquals(27, board.data_.getWaitingPlayerHero().getHealth());
	}

	@Test
	public void testAttackEnemyMinion() throws HSException {
		HearthTreeNode ret = raptor.attack(PlayerSide.WAITING_PLAYER, croc, board, null, null);
		assertEquals(board, ret);

		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 1);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 0);
	}

	@Test
	public void testAttackFaerieMinion() throws HSException {
		FaerieDragon faerie = new FaerieDragon();
		board.data_.placeMinion(PlayerSide.WAITING_PLAYER, faerie);

		HearthTreeNode ret = raptor.attack(PlayerSide.WAITING_PLAYER, faerie, board, null, null);
		assertEquals(board, ret);

		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 1);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 1);
	}

	@Test
	public void testAttackStealthedMinion() throws HSException {
		StranglethornTiger tiger = new StranglethornTiger();
		board.data_.placeMinion(PlayerSide.WAITING_PLAYER, tiger);

		HearthTreeNode ret = raptor.attack(PlayerSide.WAITING_PLAYER, tiger, board, null, null);
		assertNull(ret);

		assertEquals(2, PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions());
		assertEquals(2, PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions());
	}

	@Test
	public void testAttackBreaksStealth() throws HSException {
		raptor.setStealthed(true);
		Minion target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 0);
		HearthTreeNode ret = raptor.attack(PlayerSide.WAITING_PLAYER, target, board, null, null);
		assertEquals(board, ret);

		assertFalse(raptor.getStealthed());
		assertEquals(30, board.data_.getCurrentPlayerHero().getHealth());
		assertEquals(27, board.data_.getWaitingPlayerHero().getHealth());
	}

	@Test
	public void testAttackOwnHero() throws HSException {
		Minion target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 0);
		HearthTreeNode ret = raptor.attack(PlayerSide.CURRENT_PLAYER, target, board, null, null);
		assertNull(ret);

		assertEquals(30, board.data_.getCurrentPlayerHero().getHealth());
		assertEquals(30, board.data_.getWaitingPlayerHero().getHealth());
	}

	@Test
	public void testAttackOwnMinion() throws HSException {
		HearthTreeNode ret = raptor.attack(PlayerSide.CURRENT_PLAYER, yeti, board, null, null);
		assertNull(ret);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 2);
	}

	@Test
	public void testWindfury() throws HSException {
		raptor.setWindfury(true);

		Minion target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 0);
		HearthTreeNode ret = raptor.attack(PlayerSide.WAITING_PLAYER, target, board, null, null);
		assertEquals(board, ret);

		assertTrue(raptor.hasWindFuryAttacked());
		assertTrue(raptor.canAttack());
		assertEquals(30, board.data_.getCurrentPlayerHero().getHealth());
		assertEquals(27, board.data_.getWaitingPlayerHero().getHealth());

		ret = raptor.attack(PlayerSide.WAITING_PLAYER, target, board, null, null);
		assertEquals(board, ret);

		assertTrue(raptor.hasWindFuryAttacked());
		assertFalse(raptor.canAttack());
		assertEquals(30, board.data_.getCurrentPlayerHero().getHealth());
		assertEquals(24, board.data_.getWaitingPlayerHero().getHealth());
	}

	@Test
	public void testWindfuryPostAttack() throws HSException {
		Minion target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 0);
		HearthTreeNode ret = raptor.attack(PlayerSide.WAITING_PLAYER, target, board, null, null);
		assertEquals(board, ret);

		assertFalse(raptor.hasWindFuryAttacked());
		assertFalse(raptor.canAttack());
		assertEquals(30, board.data_.getCurrentPlayerHero().getHealth());
		assertEquals(27, board.data_.getWaitingPlayerHero().getHealth());

		raptor.setWindfury(true);
		assertTrue(raptor.hasWindFuryAttacked());
		assertTrue(raptor.canAttack());

		ret = raptor.attack(PlayerSide.WAITING_PLAYER, target, board, null, null);
		assertEquals(board, ret);

		assertTrue(raptor.hasWindFuryAttacked());
		assertFalse(raptor.canAttack());
		assertEquals(30, board.data_.getCurrentPlayerHero().getHealth());
		assertEquals(24, board.data_.getWaitingPlayerHero().getHealth());
	}

	@Test
	public void testExtraAttackDamage() throws HSException {
		raptor.setExtraAttackUntilTurnEnd((byte)2);

		Minion target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 0);
		HearthTreeNode ret = raptor.attack(PlayerSide.WAITING_PLAYER, target, board, null, null);
		assertEquals(board, ret);

		assertFalse(raptor.canAttack());
		assertEquals(30, board.data_.getCurrentPlayerHero().getHealth());
		assertEquals(25, board.data_.getWaitingPlayerHero().getHealth());

		assertEquals(2, raptor.getExtraAttackUntilTurnEnd());
	}
}
