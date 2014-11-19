package com.hearthsim.test.minion;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.hearthsim.card.minion.concrete.AldorPeacekeeper;
import com.hearthsim.card.minion.concrete.BoulderfistOgre;
import com.hearthsim.card.minion.concrete.StoneclawTotem;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestAldorPeacekeeper {
	private HearthTreeNode board;

	@Before
	public void setup() throws HSException {
		board = new HearthTreeNode(new BoardModel());
	}

	@Test
	public void testSetsAttack() throws HSException {
		BoulderfistOgre ogre = new BoulderfistOgre();
		AldorPeacekeeper peacekeeper = new AldorPeacekeeper();
		peacekeeper.useTargetableBattlecry_core(PlayerSide.WAITING_PLAYER, ogre, board, null, null);
		assertEquals(1, ogre.getAttack());
	}

	@Test
	public void testSetsAttackFromZero() throws HSException {
		StoneclawTotem totem = new StoneclawTotem();
		AldorPeacekeeper peacekeeper = new AldorPeacekeeper();
		peacekeeper.useTargetableBattlecry_core(PlayerSide.WAITING_PLAYER, totem, board, null, null);
		assertEquals(1, totem.getAttack());
	}

	@Test
	@Ignore("Existing bug")
	// Confirmed this is the case with Abusive Sergeant + Humility.
	public void testOverridesExtraAttack() throws HSException {
		BoulderfistOgre ogre = new BoulderfistOgre();
		ogre.setExtraAttackUntilTurnEnd((byte)2);
		AldorPeacekeeper peacekeeper = new AldorPeacekeeper();
		peacekeeper.useTargetableBattlecry_core(PlayerSide.WAITING_PLAYER, ogre, board, null, null);
		assertEquals(1, ogre.getAttack());
		assertEquals(0, ogre.getExtraAttackUntilTurnEnd());
	}

	@Test
	public void testDoesNotOverrideAura() throws HSException {
		BoulderfistOgre ogre = new BoulderfistOgre();
		ogre.setAuraAttack((byte)1);
		AldorPeacekeeper peacekeeper = new AldorPeacekeeper();
		peacekeeper.useTargetableBattlecry_core(PlayerSide.WAITING_PLAYER, ogre, board, null, null);
		assertEquals(1, ogre.getAttack());
		assertEquals(1, ogre.getAuraAttack());
	}
}
