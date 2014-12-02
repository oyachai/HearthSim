package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;import com.hearthsim.entity.BaseEntity;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

import java.util.EnumSet;


public class ElvenArcher extends Minion {

	private static final boolean HERO_TARGETABLE = true;
	private static final boolean SUMMONED = false;
	private static final boolean TRANSFORMED = false;
	private static final byte SPELL_DAMAGE = 0;
	
	public ElvenArcher() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;
        summoned_ = SUMMONED;
        transformed_ = TRANSFORMED;
	}
	
	
	
	public EnumSet<BattlecryTargetType> getBattlecryTargets() {
		return EnumSet.of(BattlecryTargetType.FRIENDLY_HERO, BattlecryTargetType.ENEMY_HERO, BattlecryTargetType.FRIENDLY_MINIONS, BattlecryTargetType.ENEMY_MINIONS);
	}
	
	/**
	 * Battlecry: Deal 1 damage to a chosen target
	 */
	
	public HearthTreeNode useTargetableBattlecry_core(
			PlayerSide side,
			BaseEntity targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1
		) throws HSException
	{
		HearthTreeNode toRet = boardState;
		toRet = targetMinion.takeDamage((byte)1, PlayerSide.CURRENT_PLAYER, side, toRet, deckPlayer0, deckPlayer1, false, true);
		return toRet;
	}
	
	/**
	 * 
	 * Override for battlecry
	 * 
	 * Battlecry: Deal 1 damage to a chosen target
	 * 
	 *
     *
     * @param side
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     *
     * @return The boardState is manipulated and returned
	 */
//	
//	public HearthTreeNode use_core(
//			PlayerSide side,
//			BaseEntity targetMinion,
//			HearthTreeNode boardState,
//			Deck deckPlayer0,
//			Deck deckPlayer1,
//			boolean singleRealizationOnly)
//		throws HSException
//	{	
//		HearthTreeNode toRet = super.use_core(side, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);
//		if (toRet != null) {
//
//			{
//				HearthTreeNode newState = new HearthTreeNode((BoardModel)boardState.data_.deepCopy());
//				newState = newState.data_.getCurrentPlayerHero().takeDamage((byte)1, PlayerSide.CURRENT_PLAYER, PlayerSide.CURRENT_PLAYER, newState, deckPlayer0, deckPlayer1, false, false);
//				toRet.addChild(newState);
//			}
//
//			{
//				for (int index = 0; index < PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getNumMinions(); ++index) {
//					if (PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getMinions().get(index) == this)
//						continue;
//					HearthTreeNode newState = new HearthTreeNode((BoardModel)boardState.data_.deepCopy());
//					BaseEntity minion = PlayerSide.CURRENT_PLAYER.getPlayer(newState).getMinions().get(index);
//					newState = minion.takeDamage((byte)1, PlayerSide.CURRENT_PLAYER, PlayerSide.CURRENT_PLAYER, newState, deckPlayer0, deckPlayer1, false, true);
//					newState = BoardStateFactoryBase.handleDeadMinions(newState, deckPlayer0, deckPlayer1);
//					toRet.addChild(newState);
//				}
//			}
//
//			{
//				HearthTreeNode newState = new HearthTreeNode((BoardModel)boardState.data_.deepCopy());
//				newState = newState.data_.getWaitingPlayerHero().takeDamage((byte)1, PlayerSide.CURRENT_PLAYER, PlayerSide.WAITING_PLAYER, newState, deckPlayer0, deckPlayer1, false, false);
//				toRet.addChild(newState);
//			}
//
//			{
//				for (int index = 0; index < PlayerSide.WAITING_PLAYER.getPlayer(toRet).getNumMinions(); ++index) {
//					HearthTreeNode newState = new HearthTreeNode((BoardModel)boardState.data_.deepCopy());
//					BaseEntity minion = PlayerSide.WAITING_PLAYER.getPlayer(newState).getMinions().get(index);
//					newState = minion.takeDamage((byte)1, PlayerSide.CURRENT_PLAYER, PlayerSide.WAITING_PLAYER, newState, deckPlayer0, deckPlayer1, false, true);
//					newState = BoardStateFactoryBase.handleDeadMinions(newState, deckPlayer0, deckPlayer1);
//					toRet.addChild(newState);
//				}
//			}
//
//			return boardState;
//							
//		} else {
//			return null;				
//		}
//
//	}
}
