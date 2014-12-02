package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;import com.hearthsim.entity.BaseEntity;
import com.hearthsim.card.minion.Beast;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.entity.BaseEntity;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.HearthAction;
import com.hearthsim.util.factory.BoardStateFactoryBase;
import com.hearthsim.util.tree.HearthTreeNode;
import com.hearthsim.util.tree.RandomEffectNode;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class StampedingKodo extends Beast {

	private static final boolean HERO_TARGETABLE = true;
	private static final boolean SUMMONED = false;
	private static final boolean TRANSFORMED = false;
	private static final byte SPELL_DAMAGE = 0;
	
	public StampedingKodo() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;
        summoned_ = SUMMONED;
        transformed_ = TRANSFORMED;
	}

	
	public EnumSet<BattlecryTargetType> getBattlecryTargets() {
		return EnumSet.of(BattlecryTargetType.NO_TARGET);
	}
	
	/**
	 * Battlecry: Destroy
	 */
	
	public HearthTreeNode useUntargetableBattlecry_core(
			BaseEntity minionPlacementTarget,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1,
			boolean singleRealizationOnly
		) throws HSException
	{
		HearthTreeNode toRet = boardState;
		if (singleRealizationOnly) {
			if (toRet != null) {
				List<BaseEntity> possibleTargets = new ArrayList<BaseEntity>();
				for (BaseEntity minion : PlayerSide.WAITING_PLAYER.getPlayer(toRet).getMinions()) {
					if (minion.getTotalAttack() <= 2)
						possibleTargets.add((Minion) minion);
				}
				if (possibleTargets.size() > 0) {
					BaseEntity targetMinion = (Minion) possibleTargets.get((int)(Math.random() * possibleTargets.size()));
					targetMinion.setHealth((byte)-99); //destroyed!
				}
			}
		} else {
			int placementTargetIndex = minionPlacementTarget instanceof Hero ? 0 : PlayerSide.CURRENT_PLAYER.getPlayer(boardState).getMinions().indexOf(minionPlacementTarget) + 1;
			int thisMinionIndex = PlayerSide.CURRENT_PLAYER.getPlayer(boardState).getMinions().indexOf(this) + 1;
			toRet = new RandomEffectNode(boardState, new HearthAction(HearthAction.Verb.USE_CARD, PlayerSide.CURRENT_PLAYER, thisMinionIndex, PlayerSide.CURRENT_PLAYER, placementTargetIndex));
			if (toRet != null) {
				List<Minion> possibleTargets = new ArrayList<Minion>();
				for (BaseEntity minion : PlayerSide.WAITING_PLAYER.getPlayer(toRet).getMinions()) {
					if (minion.getTotalAttack() <= 2)
						possibleTargets.add((Minion) minion);
				}
				if (possibleTargets.size() > 0) {
					PlayerModel targetPlayer = PlayerSide.WAITING_PLAYER.getPlayer(toRet);
					for (BaseEntity possibleTarget : possibleTargets) {
						HearthTreeNode newState = new HearthTreeNode((BoardModel) toRet.data_.deepCopy());
						BaseEntity targetMinion = (Minion) PlayerSide.WAITING_PLAYER.getPlayer(newState).getMinions().get(targetPlayer.getMinions().indexOf(possibleTarget));
						targetMinion.setHealth((byte)-99); //destroyed!
						newState = BoardStateFactoryBase.handleDeadMinions(newState, deckPlayer0, deckPlayer1);
						toRet.addChild(newState);
					}
				}				
			}
		}
		return toRet;
	}
}
