package com.hearthsim.card.minion.concrete;

import java.util.EnumSet;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class AncientMage extends Minion {
	
	private static final boolean HERO_TARGETABLE = true;
	private static final boolean SUMMONED = false;
	private static final boolean TRANSFORMED = false;
	private static final byte SPELL_DAMAGE = 0;
	
	public AncientMage() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;
        summoned_ = SUMMONED;
        transformed_ = TRANSFORMED;
	}
	
	@Override
	public EnumSet<BattlecryTargetType> getBattlecryTargets() {
		return EnumSet.of(BattlecryTargetType.NO_TARGET);
	}
	
	/**
	 * Battlecry: Destroy your opponent's weapon
	 */
	@Override
	public HearthTreeNode useUntargetableBattlecry_core(
			Minion minionPlacementTarget,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1,
			boolean singleRealizationOnly
		) throws HSException
	{
		HearthTreeNode toRet = boardState;
		int thisMinionIndex = PlayerSide.CURRENT_PLAYER.getPlayer(boardState).getMinions().indexOf(this);
		int numMinions = PlayerSide.CURRENT_PLAYER.getPlayer(boardState).getNumMinions();
		if (numMinions > 1) {
			int minionToTheLeft = thisMinionIndex > 0 ? thisMinionIndex - 1 : -1;
			int minionToTheRight = thisMinionIndex < numMinions - 1 ? thisMinionIndex + 1 : -1;
			if (minionToTheLeft >= 0) {
				Minion minionToBuff = toRet.data_.getMinion(PlayerSide.CURRENT_PLAYER, minionToTheLeft);
				minionToBuff.addSpellDamage((byte)1);
			}
			if (minionToTheRight >= 0) {
				Minion minionToBuff = toRet.data_.getMinion(PlayerSide.CURRENT_PLAYER, minionToTheRight);
				minionToBuff.addSpellDamage((byte)1);
			}
		}
		return toRet;
	}

	

}