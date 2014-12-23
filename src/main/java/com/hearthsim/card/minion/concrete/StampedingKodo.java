package com.hearthsim.card.minion.concrete;

import java.util.ArrayList;
import java.util.List;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.HearthAction;
import com.hearthsim.util.factory.BoardStateFactoryBase;
import com.hearthsim.util.tree.HearthTreeNode;
import com.hearthsim.util.tree.RandomEffectNode;

public class StampedingKodo extends Minion implements MinionUntargetableBattlecry {

	private static final boolean HERO_TARGETABLE = true;
	private static final byte SPELL_DAMAGE = 0;
	
	public StampedingKodo() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;

        this.tribe = MinionTribe.BEAST;
	}

	/**
	 * Battlecry: Destroy
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
		if (singleRealizationOnly) {
			if (toRet != null) {
				List<Minion> possibleTargets = new ArrayList<Minion>();
				for (Minion minion : PlayerSide.WAITING_PLAYER.getPlayer(toRet).getMinions()) {
					if (minion.getTotalAttack() <= 2)
						possibleTargets.add(minion);
				}
				if (possibleTargets.size() > 0) {
					Minion targetMinion = possibleTargets.get((int)(Math.random() * possibleTargets.size()));
					targetMinion.setHealth((byte)-99); //destroyed!
				}
			}
		} else {
			int placementTargetIndex = minionPlacementTarget instanceof Hero ? 0 : PlayerSide.CURRENT_PLAYER.getPlayer(boardState).getMinions().indexOf(minionPlacementTarget) + 1;
			int thisMinionIndex = PlayerSide.CURRENT_PLAYER.getPlayer(boardState).getMinions().indexOf(this) + 1;
			List<Minion> possibleTargets = new ArrayList<Minion>();
			for (Minion minion : PlayerSide.WAITING_PLAYER.getPlayer(toRet).getMinions()) {
				if (minion.getTotalAttack() <= 2)
					possibleTargets.add(minion);
			}
			if (possibleTargets.size() > 0) {
				toRet = new RandomEffectNode(boardState, new HearthAction(HearthAction.Verb.UNTARGETABLE_BATTLECRY, PlayerSide.CURRENT_PLAYER, thisMinionIndex, PlayerSide.CURRENT_PLAYER, placementTargetIndex));
				PlayerModel targetPlayer = PlayerSide.WAITING_PLAYER.getPlayer(toRet);
				for (Minion possibleTarget : possibleTargets) {
					HearthTreeNode newState = new HearthTreeNode(toRet.data_.deepCopy());
					Minion targetMinion = PlayerSide.WAITING_PLAYER.getPlayer(newState).getMinions().get(targetPlayer.getMinions().indexOf(possibleTarget));
					targetMinion.setHealth((byte)-99); //destroyed!
					newState = BoardStateFactoryBase.handleDeadMinions(newState, deckPlayer0, deckPlayer1);
					toRet.addChild(newState);
				}
			}				
		}
		return toRet;
	}
}
