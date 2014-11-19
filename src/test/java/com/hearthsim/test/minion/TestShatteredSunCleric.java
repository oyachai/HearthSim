package com.hearthsim.test.minion;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.minion.concrete.BoulderfistOgre;
import com.hearthsim.card.minion.concrete.ShatteredSunCleric;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestShatteredSunCleric {
	private HearthTreeNode board;

	@Before
	public void setup() throws HSException {
		board = new HearthTreeNode(new BoardModel());
	}

	@Test
	public void testAddsExtraStats() throws HSException {
		BoulderfistOgre ogre = new BoulderfistOgre();
		ShatteredSunCleric shattered = new ShatteredSunCleric();
		shattered.useTargetableBattlecry_core(PlayerSide.WAITING_PLAYER, ogre, board, null, null);
		assertEquals(7, ogre.getAttack());
		assertEquals(8, ogre.getHealth());
	}

	@Test
	public void testBuffIsAdditive() throws HSException {
		BoulderfistOgre ogre = new BoulderfistOgre();
		ShatteredSunCleric shattered = new ShatteredSunCleric();
		shattered.useTargetableBattlecry_core(PlayerSide.WAITING_PLAYER, ogre, board, null, null);
		shattered.useTargetableBattlecry_core(PlayerSide.WAITING_PLAYER, ogre, board, null, null);
		assertEquals(8, ogre.getAttack());
		assertEquals(9, ogre.getHealth());
	}
}
