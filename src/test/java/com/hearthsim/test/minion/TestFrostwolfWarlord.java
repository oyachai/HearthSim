package com.hearthsim.test.minion;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.concrete.FrostwolfWarlord;
import com.hearthsim.card.minion.concrete.SilverHandRecruit;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestFrostwolfWarlord {

	private HearthTreeNode board;
	private FrostwolfWarlord warlord;

	@Before
	public void setup() throws HSException {
		board = new HearthTreeNode(new BoardModel());

		warlord = new FrostwolfWarlord();
		board.data_.placeCardHandCurrentPlayer(warlord);

		board.data_.getCurrentPlayer().setMana((byte)7);
		board.data_.getCurrentPlayer().setMaxMana((byte)7);
	}

	@Test
	public void testBattlecryWithMultipleMinions() throws HSException {
		board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new SilverHandRecruit());
		board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new SilverHandRecruit());

		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 1, board, null, null);
		assertEquals(board, ret);
		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 3);
		assertEquals(board.data_.getCurrentPlayer().getMana(), 2);

		assertEquals(warlord.getHealth(), 4 + 2);
		assertEquals(warlord.getAttack(), 4 + 2);
	}

	@Test
	public void testBattlecryWithNoMinions() throws HSException {
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board, null, null);
		assertEquals(board, ret);
		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 1);
		assertEquals(board.data_.getCurrentPlayer().getMana(), 2);

		assertEquals(warlord.getHealth(), 4);
		assertEquals(warlord.getAttack(), 4);
	}

	@Test
	public void testSilence() throws HSException {
		board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new SilverHandRecruit());
		board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new SilverHandRecruit());

		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board, null, null);
		assertEquals(board, ret);

		warlord.silenced(PlayerSide.CURRENT_PLAYER, board);

		assertEquals(warlord.getHealth(), 4);
		assertEquals(warlord.getAttack(), 4);
	}
}
