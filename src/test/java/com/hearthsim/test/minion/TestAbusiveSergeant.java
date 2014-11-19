package com.hearthsim.test.minion;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.minion.concrete.AbusiveSergeant;
import com.hearthsim.card.minion.concrete.BoulderfistOgre;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestAbusiveSergeant {
	private HearthTreeNode board;

	@Before
	public void setup() throws HSException {
		board = new HearthTreeNode(new BoardModel());
	}

	@Test
	public void testAddsExtraAttack() throws HSException {
		BoulderfistOgre ogre = new BoulderfistOgre();
		AbusiveSergeant abusive = new AbusiveSergeant();
		abusive.useTargetableBattlecry_core(PlayerSide.WAITING_PLAYER, ogre, board, null, null);
		assertEquals(2, ogre.getExtraAttackUntilTurnEnd());
	}

	@Test
	public void testBuffIsAdditive() throws HSException {
		BoulderfistOgre ogre = new BoulderfistOgre();
		ogre.setExtraAttackUntilTurnEnd((byte)2);
		AbusiveSergeant abusive = new AbusiveSergeant();
		abusive.useTargetableBattlecry_core(PlayerSide.WAITING_PLAYER, ogre, board, null, null);
		assertEquals(4, ogre.getExtraAttackUntilTurnEnd());
	}
}
