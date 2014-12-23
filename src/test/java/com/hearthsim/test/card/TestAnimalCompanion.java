package com.hearthsim.test.card;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.BloodfenRaptor;
import com.hearthsim.card.minion.concrete.Huffer;
import com.hearthsim.card.minion.concrete.Leokk;
import com.hearthsim.card.minion.concrete.Misha;
import com.hearthsim.card.spellcard.concrete.AnimalCompanion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import com.hearthsim.util.tree.RandomEffectNode;

public class TestAnimalCompanion {


	private HearthTreeNode board;

	private static final byte mana = 2;
	private static final byte attack0 = 5;
	private static final byte health0 = 3;
	private static final byte health1 = 7;

	@Before
	public void setup() throws HSException {
		board = new HearthTreeNode(new BoardModel());

		Minion minion0_0 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
		Minion minion0_1 = new Minion("" + 0, mana, attack0, (byte)(health1 - 1), attack0, health1, health1);
		Minion minion1_0 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
		Minion minion1_1 = new Minion("" + 0, mana, attack0, (byte)(health1 - 1), attack0, health1, health1);
		
		board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, minion0_0);
		board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, minion0_1);
		
		board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion1_0);
		board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion1_1);
		
		AnimalCompanion fb = new AnimalCompanion();
		board.data_.placeCardHandCurrentPlayer(fb);

		board.data_.getCurrentPlayer().setMana((byte)4);
	}

	@Test
	public void testLeokkBuffs() throws HSException {
		
		Card leokk = new Leokk();
		board.data_.placeCardHandCurrentPlayer(leokk);
		
		Card theCard = board.data_.getCurrentPlayerCardHand(1);
		HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 2, board, null, null);
		
		//Use Leokk.  The other minions should now be buffed with +1 attack
		assertEquals(board, ret);
		assertEquals(board.data_.getNumCards_hand(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 3);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 2);
		assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 30);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), health0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), health1 - 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getHealth(), 4);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), health0);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), health1 - 1);

		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), attack0 + 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getTotalAttack(), attack0 + 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getTotalAttack(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), attack0);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getTotalAttack(), attack0);

		
		//Now, attack and kill Leokk.  All minions should go back to their original attack
		Minion minion = PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2);
		minion.hasAttacked(false);
		Minion target2 = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 1);
		ret = minion.attack(PlayerSide.WAITING_PLAYER, target2, board, null, null);
		
		assertEquals(board, ret);
		assertEquals(board.data_.getNumCards_hand(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 2);
		assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 30);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), health0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), health1 - 1);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), health0 - 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), health1 - 1);

		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), attack0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getTotalAttack(), attack0);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), attack0);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getTotalAttack(), attack0);

	}
	
	@Test
	public void testSummonsHufferLeokkOrMisha() throws HSException {
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board, null, null);
		assertNotNull(ret); // ret != null because of how AnimalCompanion creates its RNG node
		assertTrue(ret instanceof RandomEffectNode);

		// Check that the original node is not touched
		assertEquals(1, board.data_.getNumCards_hand());
		assertEquals(4, board.data_.getCurrentPlayer().getMana());
				
		assertEquals(2, PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions());
		assertEquals(2, PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions());
		
		//child node 0 = Huffer
		HearthTreeNode c0 = ret.getChildren().get(0);
		assertEquals(3, PlayerSide.CURRENT_PLAYER.getPlayer(c0).getNumMinions());
		assertTrue(PlayerSide.CURRENT_PLAYER.getPlayer(c0).getMinions().get(2) instanceof Huffer);
		
		//child node 1 = Leokk
		HearthTreeNode c1 = ret.getChildren().get(1);
		assertEquals(3, PlayerSide.CURRENT_PLAYER.getPlayer(c1).getNumMinions());
		assertTrue(PlayerSide.CURRENT_PLAYER.getPlayer(c1).getMinions().get(2) instanceof Leokk);

		//child node 2 = Misha
		HearthTreeNode c2 = ret.getChildren().get(2);
		assertEquals(3, PlayerSide.CURRENT_PLAYER.getPlayer(c2).getNumMinions());
		assertTrue(PlayerSide.CURRENT_PLAYER.getPlayer(c2).getMinions().get(2) instanceof Misha);
	}

	@Test
	public void testCannotPlayWithFullBoard() throws HSException {
		board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new BloodfenRaptor());
		board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new BloodfenRaptor());
		board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new BloodfenRaptor());
		board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new BloodfenRaptor());
		board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new BloodfenRaptor());

		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		Minion target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 0);
		assertFalse(theCard.canBeUsedOn(PlayerSide.CURRENT_PLAYER, target, board.data_));
		
		HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board, null, null);		
		assertNull(ret);
	}
}
