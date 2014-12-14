package com.hearthsim.test.heroes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.BoulderfistOgre;
import com.hearthsim.card.minion.concrete.KoboldGeomancer;
import com.hearthsim.card.minion.concrete.RaidLeader;
import com.hearthsim.card.minion.heroes.TestHero;
import com.hearthsim.card.spellcard.concrete.HolySmite;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.card.spellcard.concrete.WildGrowth;
import com.hearthsim.card.weapon.concrete.FieryWarAxe;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestHeroBase {

	private HearthTreeNode board;
	private Deck deck;

	@Before
	public void setup() throws HSException {
		TestHero self = new TestHero();
		self.enableHeroAbility = true;
		board = new HearthTreeNode(new BoardModel(self, new TestHero()));

		Minion minion0_0 = new BoulderfistOgre();
		Minion minion0_1 = new RaidLeader();
		Minion minion1_0 = new BoulderfistOgre();
		Minion minion1_1 = new RaidLeader();

		board.data_.placeCardHandCurrentPlayer(minion0_0);
		board.data_.placeCardHandCurrentPlayer(minion0_1);

		board.data_.placeCardHandWaitingPlayer(minion1_0);
		board.data_.placeCardHandWaitingPlayer(minion1_1);

		Card cards[] = new Card[10];
		for (int index = 0; index < 10; ++index) {
			cards[index] = new TheCoin();
		}

		deck = new Deck(cards);

		Card fb = new WildGrowth();
		board.data_.placeCardHandCurrentPlayer(fb);

		board.data_.getCurrentPlayer().setMana((byte)9);
		board.data_.getWaitingPlayer().setMana((byte)9);
		
		board.data_.getCurrentPlayer().setMaxMana((byte)8);
		board.data_.getWaitingPlayer().setMaxMana((byte)8);
		
		HearthTreeNode tmpBoard = new HearthTreeNode(board.data_.flipPlayers());
		tmpBoard.data_.getCurrentPlayerCardHand(0).useOn(PlayerSide.CURRENT_PLAYER,
				tmpBoard.data_.getCurrentPlayerHero(), tmpBoard, deck, null);
		tmpBoard.data_.getCurrentPlayerCardHand(0).useOn(PlayerSide.CURRENT_PLAYER,
				tmpBoard.data_.getCurrentPlayerHero(), tmpBoard, deck, null);

		board = new HearthTreeNode(tmpBoard.data_.flipPlayers());
		board.data_.getCurrentPlayerCardHand(0).useOn(PlayerSide.CURRENT_PLAYER,
				board.data_.getCurrentPlayerHero(), board, deck, null);
		board.data_.getCurrentPlayerCardHand(0).useOn(PlayerSide.CURRENT_PLAYER,
				board.data_.getCurrentPlayerHero(), board, deck, null);

		board.data_.resetMana();
		board.data_.resetMinions();
	}

	@Test
	public void testHeroCannotAttack() throws HSException {
		Minion target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 0);
		Hero hero = board.data_.getCurrentPlayerHero();

		assertFalse(hero.canAttack());

		HearthTreeNode ret = hero.attack(PlayerSide.WAITING_PLAYER, target, board, deck, null);
		assertNull(ret);
	}

	@Test
	public void testMinionAttackingHero() throws HSException {
		// null case
		Minion target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 0);
		Minion minion = PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0);
		HearthTreeNode ret = minion.attack(PlayerSide.WAITING_PLAYER, target, board, deck, null);
		assertEquals(board, ret);

		assertEquals(board.data_.getNumCards_hand(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 2);

		assertEquals(board.data_.getCurrentPlayer().getMana(), 8);
		assertEquals(board.data_.getWaitingPlayer().getMana(), 8);
		assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 28);

		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), 2);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), 7);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), 7);

		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), 2);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getTotalAttack(), 7);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getTotalAttack(), 7);
	}

	@Test
	public void testHeropowerIsMarkedUsed() throws HSException {
		Hero hero = board.data_.getCurrentPlayerHero();
		Minion target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 0); // Hero
		HearthTreeNode ret = hero.useHeroAbility(PlayerSide.CURRENT_PLAYER, target, board, deck, null);
		assertEquals(board, ret);
		assertTrue(hero.hasBeenUsed());
	}

	@Test
	public void testHeropowerSubtractsMana() throws HSException {
		Hero hero = board.data_.getCurrentPlayerHero();
		Minion target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 0); // Hero
		HearthTreeNode ret = hero.useHeroAbility(PlayerSide.CURRENT_PLAYER, target, board, deck, null);
		assertEquals(board, ret);
		assertEquals(board.data_.getCurrentPlayer().getMana(), 6);
	}

	@Test
	public void testHeropowerCannotBeUsedTwice() throws HSException {
		Hero hero = board.data_.getCurrentPlayerHero();
		Minion target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 0); // Hero
		HearthTreeNode ret = hero.useHeroAbility(PlayerSide.CURRENT_PLAYER, target, board, deck, null);
		assertEquals(board, ret);

		target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 0); // Hero
		assertFalse(hero.canBeUsedOn(PlayerSide.CURRENT_PLAYER, target, board.data_));

		ret = hero.useHeroAbility(PlayerSide.CURRENT_PLAYER, target, board, deck, null);
		assertNull(ret);

		assertTrue(hero.hasBeenUsed());
		assertEquals(board.data_.getCurrentPlayer().getMana(), 6);
	}

	@Test
	public void testHeropowerResets() throws HSException {
		Hero hero = board.data_.getCurrentPlayerHero();
		Minion target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 0); // Hero
		HearthTreeNode ret = hero.useHeroAbility(PlayerSide.CURRENT_PLAYER, target, board, deck, null);
		assertEquals(board, ret);

		board.data_.resetMinions();
		assertFalse(hero.hasBeenUsed());
	}

	@Test
	public void testNotEnoughMana() throws HSException {
		Hero hero = board.data_.getCurrentPlayerHero();
		board.data_.getCurrentPlayer().setMana((byte) 1);

		Minion target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 0); // Hero
		//assertFalse(hero.canBeUsedOn(PlayerSide.CURRENT_PLAYER, target, board.data_)); // TODO doesn't work yet
		
		HearthTreeNode ret = hero.useHeroAbility(PlayerSide.CURRENT_PLAYER, target, board, deck, null);
		assertNull(ret);

		assertFalse(hero.hasBeenUsed());
		assertEquals(board.data_.getCurrentPlayer().getMana(), 1);
	}

	@Test
	public void testDeepCopyWeapon() throws HSException {
		Hero hero = board.data_.getCurrentPlayerHero();
		board.data_.getCurrentPlayer().setMana((byte) 2);

		FieryWarAxe axe = new FieryWarAxe();
		hero.setWeapon(axe);

		Hero copy = hero.deepCopy();

		assertEquals(hero, copy);
		assertEquals(axe, copy.getWeapon());
	}

	@Test
	public void testNotEqualWeapon() throws HSException {
		Hero hero = board.data_.getCurrentPlayerHero();
		board.data_.getCurrentPlayer().setMana((byte) 2);

		Hero copy = hero.deepCopy();

		FieryWarAxe axe = new FieryWarAxe();
		hero.setWeapon(axe);

		FieryWarAxe otherAxe = new FieryWarAxe();
		otherAxe.setWeaponCharge_((byte)1);
		copy.setWeapon(otherAxe);

		assertNotEquals(hero, copy);
	}

	@Test
	public void testMinionAttackingHeroRemovesArmorFirst() throws HSException {
		Hero opponent = board.data_.getWaitingPlayerHero();
		opponent.setArmor((byte)3);

		Minion minion = PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0);
		HearthTreeNode ret = minion.attack(PlayerSide.WAITING_PLAYER, opponent, board, deck, null);
		assertEquals(board, ret);

		assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getWaitingPlayerHero().getArmor(), 1);
	}

	@Test
	@Ignore("Existing bug")
	public void testSpellpowerEffectsArmor() throws HSException {
		Hero opponent = board.data_.getWaitingPlayerHero();
		opponent.setArmor((byte)10);

		board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new KoboldGeomancer());

		HolySmite smite = new HolySmite();
		board.data_.placeCardHand(PlayerSide.CURRENT_PLAYER, smite);

		HearthTreeNode ret = smite.useOn(PlayerSide.WAITING_PLAYER, opponent, board, deck, null);
		assertEquals(board, ret);

		assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getWaitingPlayerHero().getArmor(), 7);
	}

	@Test
	public void testFatigueDamage() throws HSException {
		board.data_.modelForSide(PlayerSide.CURRENT_PLAYER).drawNextCardFromDeck(); // 1 damage
		board.data_.modelForSide(PlayerSide.CURRENT_PLAYER).drawNextCardFromDeck(); // 2 damage = 3 total

		assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 27);
	}

	@Test
	public void testFatigueDamageEffectsArmorFirst() throws HSException {
		board.data_.getCurrentPlayerHero().setArmor((byte)10);

		board.data_.modelForSide(PlayerSide.CURRENT_PLAYER).drawNextCardFromDeck(); // 1 damage
		board.data_.modelForSide(PlayerSide.CURRENT_PLAYER).drawNextCardFromDeck(); // 2 damage = 3 total

		assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getCurrentPlayerHero().getArmor(), 7);
	}
}
