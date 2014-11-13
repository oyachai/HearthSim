package com.hearthsim.util.factory;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.player.playercontroller.BruteForceSearchAI;
import com.hearthsim.util.IdentityLinkedList;
import com.hearthsim.util.tree.CardDrawNode;
import com.hearthsim.util.tree.HearthTreeNode;
import com.hearthsim.util.tree.RandomEffectNode;
import com.hearthsim.util.tree.StopNode;

import java.util.ArrayList;
import java.util.Iterator;

public class BoardStateFactoryBase {

	protected final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

	protected final Deck deckPlayer0_;
	protected final Deck deckPlayer1_;

	protected boolean lethal_;
	protected boolean timedOut_;
	public final long maxTime_;

	protected long startTime_;
	protected long curTime_;

	protected double curScore_;

	/**
	 * Constructor
	 * maxThinkTime defaults to 10000 milliseconds (10 seconds)
	 */
	public BoardStateFactoryBase(Deck deckPlayer0, Deck deckPlayer1) {
		this(deckPlayer0, deckPlayer1, 10000);
	}

	/**
	 * Constructor
	 * 
	 * @param deckPlayer0
	 * @param deckPlayer1
	 * @param maxThinkTime The maximum amount of time in milliseconds the factory is allowed to spend on generating the simulation tree.
	 */
	public BoardStateFactoryBase(Deck deckPlayer0, Deck deckPlayer1, long maxThinkTime) {
		deckPlayer0_ = deckPlayer1;
		deckPlayer1_ = deckPlayer1;
		lethal_ = false;
		startTime_ = System.currentTimeMillis();
		curScore_ = -1.e200;
		maxTime_ = maxThinkTime;
		timedOut_ = false;
	}

	public boolean didTimeOut() {
		return timedOut_;
	}

	public void resetTimeOut() {
		startTime_ = System.currentTimeMillis();
		timedOut_ = false;
	}

	private ArrayList<HearthTreeNode> getNextLayerOfHeroAbilityBranches(HearthTreeNode boardStateNode)
			throws HSException {
		ArrayList<HearthTreeNode> nodes = new ArrayList<HearthTreeNode>();

		Hero player = boardStateNode.data_.getCurrentPlayerHero();
		if(player.hasBeenUsed()) {
			return nodes;
		}

		HearthTreeNode newState = null;
		Minion copiedTargetMinion = null;

		// Case0: Decided to use the hero ability -- Use it on everything!
		for(int i = 0; i <= PlayerSide.CURRENT_PLAYER.getPlayer(boardStateNode).getNumMinions(); ++i) {
			Minion target = boardStateNode.data_.getCurrentPlayerCharacter(i);

			if(player.canBeUsedOn(PlayerSide.CURRENT_PLAYER, target, boardStateNode.data_)) {

				newState = new HearthTreeNode((BoardModel)boardStateNode.data_.deepCopy());
				copiedTargetMinion = newState.data_.getCurrentPlayerCharacter(i);

				newState = newState.data_.getCurrentPlayerHero().useHeroAbility(PlayerSide.CURRENT_PLAYER,
						copiedTargetMinion, newState, deckPlayer0_, deckPlayer1_, false);

				if(newState != null) {
					nodes.add(newState);
				}
			}
		}

		for(int i = 0; i <= PlayerSide.WAITING_PLAYER.getPlayer(boardStateNode).getNumMinions(); ++i) {
			Minion target = boardStateNode.data_.getWaitingPlayerCharacter(i);
			if(player.canBeUsedOn(PlayerSide.WAITING_PLAYER, target, boardStateNode.data_)) {

				newState = new HearthTreeNode((BoardModel)boardStateNode.data_.deepCopy());
				copiedTargetMinion = newState.data_.getWaitingPlayerCharacter(i);

				newState = newState.data_.getCurrentPlayerHero().useHeroAbility(PlayerSide.WAITING_PLAYER,
						copiedTargetMinion, newState, deckPlayer0_, deckPlayer1_, false);

				if(newState != null) {
					nodes.add(newState);
				}
			}
		}

		// Don't need to check hasBeenUsed here because we checked it above
		if(!nodes.isEmpty()) {
			// Case1: Decided not to use the hero ability
			newState = new HearthTreeNode((BoardModel)boardStateNode.data_.deepCopy());
			newState.data_.getCurrentPlayerHero().hasBeenUsed(true);
			nodes.add(newState);
		}

		return nodes;
	}

	private ArrayList<HearthTreeNode> getNextLayerOfAttackBranches(HearthTreeNode boardStateNode) throws HSException {
		ArrayList<HearthTreeNode> nodes = new ArrayList<HearthTreeNode>();

		ArrayList<Integer> attackable = boardStateNode.data_.getAttackableMinions();

		HearthTreeNode newState = null;
		Minion targetMinion = null;
		Minion tempMinion = null;

		// attack with characters
		for(int attackerIndex = 0; attackerIndex < PlayerSide.CURRENT_PLAYER.getPlayer(boardStateNode)
				.getNumCharacters(); ++attackerIndex) {
			if(!boardStateNode.data_.getCurrentPlayerCharacter(attackerIndex).canAttack()) {
				continue;
			}

			for(final Integer integer : attackable) {
				int targetIndex = integer.intValue();
				newState = new HearthTreeNode((BoardModel)boardStateNode.data_.deepCopy());

				targetMinion = newState.data_.getWaitingPlayerCharacter(targetIndex);
				tempMinion = newState.data_.getCurrentPlayerCharacter(attackerIndex);

				newState = tempMinion.attack(PlayerSide.WAITING_PLAYER, targetMinion, newState, deckPlayer0_,
						deckPlayer1_);

				if(newState != null) {
					nodes.add(newState);
				}
			}
		}

		// If no nodes were created then nothing could attack. If something could attack, we want to explicitly do nothing in its own node.
		if(!nodes.isEmpty()) {
			newState = new HearthTreeNode((BoardModel)boardStateNode.data_.deepCopy());
			for(Minion minion : PlayerSide.CURRENT_PLAYER.getPlayer(newState).getMinions()) {
				minion.hasAttacked(true);
			}
			newState.data_.getCurrentPlayerHero().hasAttacked(true);
			nodes.add(newState);
		}

		return nodes;
	}

	protected ArrayList<HearthTreeNode> getNextLayerOfCardBranches(HearthTreeNode boardStateNode) throws HSException {
		ArrayList<HearthTreeNode> nodes = new ArrayList<HearthTreeNode>();

		Minion targetMinion = null;
		Minion copiedTargetMinion = null;
		Card card = null;
		Card copiedCard = null;
		HearthTreeNode newState = null;

		int mana = boardStateNode.data_.getCurrentPlayer().getMana();
		for(int cardIndex = 0; cardIndex < boardStateNode.data_.getNumCards_hand(); ++cardIndex) {
			card = boardStateNode.data_.getCurrentPlayerCardHand(cardIndex);
			if(card.getManaCost(PlayerSide.CURRENT_PLAYER, boardStateNode) <= mana && !card.hasBeenUsed()) {

				// we can use this card! Let's try using it on everything
				for(int targetIndex = 0; targetIndex <= PlayerSide.CURRENT_PLAYER.getPlayer(boardStateNode)
						.getNumMinions(); ++targetIndex) {
					targetMinion = boardStateNode.data_.getCurrentPlayerCharacter(targetIndex);

					if(card.canBeUsedOn(PlayerSide.CURRENT_PLAYER, targetMinion, boardStateNode.data_)) {
						newState = new HearthTreeNode((BoardModel)boardStateNode.data_.deepCopy());
						copiedTargetMinion = newState.data_.getCurrentPlayerCharacter(targetIndex);
						copiedCard = newState.data_.getCurrentPlayerCardHand(cardIndex);
						newState = copiedCard.useOn(PlayerSide.CURRENT_PLAYER, copiedTargetMinion, newState,
								deckPlayer0_, deckPlayer1_, false);
						if(newState != null) {
							nodes.add(newState);
						}
					}
				}

				for(int targetIndex = 0; targetIndex <= PlayerSide.WAITING_PLAYER.getPlayer(boardStateNode)
						.getNumMinions(); ++targetIndex) {
					targetMinion = boardStateNode.data_.getWaitingPlayerCharacter(targetIndex);

					if(card.canBeUsedOn(PlayerSide.WAITING_PLAYER, targetMinion, boardStateNode.data_)) {
						newState = new HearthTreeNode((BoardModel)boardStateNode.data_.deepCopy());
						copiedTargetMinion = newState.data_.getWaitingPlayerCharacter(targetIndex);
						copiedCard = newState.data_.getCurrentPlayerCardHand(cardIndex);
						newState = copiedCard.useOn(PlayerSide.WAITING_PLAYER, copiedTargetMinion, newState,
								deckPlayer0_, deckPlayer1_, false);
						if(newState != null) {
							nodes.add(newState);
						}
					}
				}
			}
		}

		// If no nodes were created then nothing could be played. If something could be played, we want to explicitly do nothing in its own node.
		if(!nodes.isEmpty()) {
			newState = new HearthTreeNode((BoardModel)boardStateNode.data_.deepCopy());
			for(Card c : newState.data_.getCurrentPlayerHand()) {
				c.hasBeenUsed(true);
			}
			nodes.add(newState);
		}

		return nodes;
	}

	/**
	 * Recursively generate all possible moves
	 * This function recursively generates all possible moves that can be done starting from a given BoardState.
	 * While generating the moves, it applies the scoring function to each BoardState generated, and it will only keep the
	 * highest scoring branch.
	 * The results are stored in a tree structure and returned as a tree of BoardState class.
	 * 
	 * @param boardStateNode The initial BoardState wrapped in a HearthTreeNode.
	 * @param scoreFunc The scoring function for AI.
	 * @return boardStateNode manipulated such that all subsequent actions are children of the original boardStateNode input.
	 */
	public HearthTreeNode doMoves(HearthTreeNode boardStateNode, BruteForceSearchAI ai) throws HSException {
		log.trace("recursively performing moves");

		if(lethal_) {
			log.debug("found lethal");
			// if it's lethal, we don't have to do anything ever. Just play the lethal.
			return null;
		}

		if(timedOut_) {
			log.debug("think time is already over");
			// Time's up! no more thinking...
			return null;
		}

		if(System.currentTimeMillis() - startTime_ > maxTime_) {
			log.debug("setting think time over");
			timedOut_ = true;
			return null;
		}

		if(boardStateNode.numChildren() > 0) {
			// If this node already has children, just call doMoves on each of its children.
			// This situation can happen, for example, after a battle cry
			for(HearthTreeNode child : boardStateNode.getChildren()) {
				this.doMoves(child, ai);
			}
			return boardStateNode;
		}

		boolean lethalFound = false;
		if(boardStateNode.data_.isLethalState()) { // one of the players is dead, no reason to keep playing
			lethal_ = true;
			lethalFound = true;
		}

		if(!lethalFound) {

			ArrayList<HearthTreeNode> nodes = new ArrayList<HearthTreeNode>();
			nodes.addAll(this.getNextLayerOfHeroAbilityBranches(boardStateNode));
			nodes.addAll(this.getNextLayerOfCardBranches(boardStateNode));
			nodes.addAll(this.getNextLayerOfAttackBranches(boardStateNode));
			
			HearthTreeNode newState = null;
			for(HearthTreeNode node : nodes) {
				newState = this.doMoves(node, ai);
				if(newState != null)
					boardStateNode.addChild(newState);
			}
		}

		if(boardStateNode.isLeaf()) {
			// If at this point the node has no children, it is a leaf node. Compute its board score and store it.
			boardStateNode.setScore(ai.boardScore(boardStateNode.data_));
			boardStateNode.setNumNodesTries(1);
		} else {
			// If it is not a leaf, set the score as the maximum score of its children.
			// We can also throw out any children that don't have the highest score (boy, this sounds so wrong...)
			double tmpScore = -1.e300;
			int tmpNumNodesTried = 0;
			Iterator<HearthTreeNode> iter = boardStateNode.getChildren().iterator();
			HearthTreeNode bestBranch = null;

			while(iter.hasNext()) {
				HearthTreeNode child = iter.next();
				tmpNumNodesTried += child.getNumNodesTried();
				double origScore = child.getScore();
				double theScore = origScore;
				if(child instanceof CardDrawNode)
					theScore += ((CardDrawNode)child).cardDrawScore(deckPlayer0_, ai);
				if(child instanceof RandomEffectNode)
					theScore = ((RandomEffectNode)child).weightedAverageScore(deckPlayer0_, ai);
				if(theScore > tmpScore) {
					tmpScore = theScore;
					bestBranch = child;
				}
			}

			if(bestBranch instanceof StopNode) {
				bestBranch.clearChildren(); // cannot continue past a StopNode
			}
			boardStateNode.clearChildren();
			boardStateNode.addChild(bestBranch);
			boardStateNode.setScore(tmpScore);
			boardStateNode.setNumNodesTries(tmpNumNodesTried);
		}

		return boardStateNode;
	}

	/**
	 * Handles dead minions
	 * For each dead minion, the function calls its deathrattle in the correct order, and then removes the dead minions from the board.
	 * 
	 * @return true if there are dead minions left (minions might have died during deathrattle). false otherwise.
	 * @throws HSException
	 */
	public static HearthTreeNode handleDeadMinions(HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1)
			throws HSException {
		HearthTreeNode toRet = boardState;
		IdentityLinkedList<BoardModel.MinionPlayerPair> deadMinions = new IdentityLinkedList<BoardModel.MinionPlayerPair>();
		for(BoardModel.MinionPlayerPair minionIdPair : toRet.data_.getAllMinionsFIFOList()) {
			if(minionIdPair.getMinion().getTotalHealth() <= 0) {
				deadMinions.add(minionIdPair);
			}
		}
		for(BoardModel.MinionPlayerPair minionIdPair : deadMinions) {
			PlayerSide playerSide = boardState.data_.sideForModel(minionIdPair.getPlayerModel());
			toRet = minionIdPair.getMinion().destroyed(playerSide, toRet, deckPlayer0, deckPlayer1);
			toRet.data_.removeMinion(minionIdPair);
		}
		if(toRet.data_.hasDeadMinions())
			return BoardStateFactoryBase.handleDeadMinions(toRet, deckPlayer0, deckPlayer1);
		else
			return toRet;
	}

}
