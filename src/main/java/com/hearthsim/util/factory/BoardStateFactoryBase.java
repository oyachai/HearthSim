package com.hearthsim.util.factory;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.player.playercontroller.ArtificialPlayer;
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
	 * 
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
	
	public HearthTreeNode createHeroAbilityBranches(HearthTreeNode boardStateNode, ArtificialPlayer ai) throws HSException {
        log.trace("creating hero ability branches");

		boolean heroAbilityUsable = false;
		if (!boardStateNode.data_.getCurrentPlayerHero().hasBeenUsed()) {
			//Case0: Decided to use the hero ability -- Use it on everything!
			for(int i = 0; i <= PlayerSide.CURRENT_PLAYER.getPlayer(boardStateNode).getNumMinions(); ++i) {
				Minion targetMinion = boardStateNode.data_.getCurrentPlayerCharacter(i);
				if (boardStateNode.data_.getCurrentPlayerHero().canBeUsedOn(PlayerSide.CURRENT_PLAYER, targetMinion, boardStateNode.data_)) {
					HearthTreeNode newState = new HearthTreeNode((BoardModel)boardStateNode.data_.deepCopy());
					Minion copiedTargetMinion = newState.data_.getCurrentPlayerCharacter(i);
					newState = newState.data_.getCurrentPlayerHero().useHeroAbility(PlayerSide.CURRENT_PLAYER, copiedTargetMinion, newState, deckPlayer0_, deckPlayer1_, false);
					if (newState != null) {
						newState = this.doMoves(newState, ai);
						if (newState != null) boardStateNode.addChild(newState);
						heroAbilityUsable = true;
					}
				}
			}
			for(int i = 0; i <= PlayerSide.WAITING_PLAYER.getPlayer(boardStateNode).getNumMinions(); ++i) {
				Minion targetMinion = boardStateNode.data_.getWaitingPlayerCharacter(i);
				if (boardStateNode.data_.getCurrentPlayerHero().canBeUsedOn(PlayerSide.WAITING_PLAYER, targetMinion, boardStateNode.data_)) {
					HearthTreeNode newState = new HearthTreeNode((BoardModel)boardStateNode.data_.deepCopy());
					Minion copiedTargetMinion = newState.data_.getWaitingPlayerCharacter(i);
					newState = newState.data_.getCurrentPlayerHero().useHeroAbility(PlayerSide.WAITING_PLAYER, copiedTargetMinion, newState, deckPlayer0_, deckPlayer1_, false);
					if (newState != null) {
						newState = this.doMoves(newState, ai);
						if (newState != null) boardStateNode.addChild(newState);
						heroAbilityUsable = true;
					}
				}
			}
			if (heroAbilityUsable) {
				//Case1: Decided not to use the hero ability
				HearthTreeNode newState = new HearthTreeNode((BoardModel)boardStateNode.data_.deepCopy());
				newState.data_.getCurrentPlayerHero().hasBeenUsed(true);
				newState = this.doMoves(newState, ai);
				if (newState != null) boardStateNode.addChild(newState);
			}
		}
		
		return boardStateNode;
	}
	
	public HearthTreeNode createMinionAttackBranches(HearthTreeNode boardStateNode, ArtificialPlayer ai) throws HSException {
        log.trace("creating minion attack branches");
		//Use the minions that we have out on the board
		//the case where I choose to not use any more minions
		boolean allAttacked = boardStateNode.data_.getCurrentPlayerHero().hasAttacked();
		for ( final Minion minion : PlayerSide.CURRENT_PLAYER.getPlayer(boardStateNode).getMinions()) {
			allAttacked = allAttacked && minion.hasAttacked();
		}
		
		if (!allAttacked && PlayerSide.CURRENT_PLAYER.getPlayer(boardStateNode).getNumMinions() > 0) {
			HearthTreeNode newState = new HearthTreeNode((BoardModel)boardStateNode.data_.deepCopy());
			for (Minion minion : PlayerSide.CURRENT_PLAYER.getPlayer(newState).getMinions()) {
				minion.hasAttacked(true);
			}
			newState.data_.getCurrentPlayerHero().hasAttacked(true);
			newState = this.doMoves(newState, ai);
			if (newState != null) boardStateNode.addChild(newState);
		}
		//attack with hero if possible
		if (!boardStateNode.data_.getCurrentPlayerHero().hasAttacked() && (boardStateNode.data_.getCurrentPlayerHero().getAttack() + boardStateNode.data_.getCurrentPlayerHero().getExtraAttackUntilTurnEnd()) > 0) {
			ArrayList<Integer> attackable = boardStateNode.data_.getAttackableMinions();
			for(final Integer integer : attackable) {
				int i = integer.intValue();
				HearthTreeNode newState = new HearthTreeNode((BoardModel)boardStateNode.data_.deepCopy());
				Minion targetMinion = newState.data_.getWaitingPlayerCharacter(i);
				newState = newState.data_.getCurrentPlayerHero().attack(PlayerSide.WAITING_PLAYER, targetMinion, newState, deckPlayer0_, deckPlayer1_);
				if (newState != null) {
					newState = this.doMoves(newState, ai);
					if (newState != null) boardStateNode.addChild(newState);
				}
			}				
		}
		//attack with minion
		for (int ic = 0; ic < PlayerSide.CURRENT_PLAYER.getPlayer(boardStateNode).getNumMinions(); ++ic) {
			final Minion minion = PlayerSide.CURRENT_PLAYER.getPlayer(boardStateNode).getMinions().get(ic);
			if (minion.hasAttacked()) {
				continue;
			}
			ArrayList<Integer> attackable = boardStateNode.data_.getAttackableMinions();
			for(final Integer integer : attackable) {
				int i = integer.intValue();
				HearthTreeNode newState = new HearthTreeNode((BoardModel)boardStateNode.data_.deepCopy());
				Minion targetMinion = newState.data_.getWaitingPlayerCharacter(i);
				Minion tempMinion = PlayerSide.CURRENT_PLAYER.getPlayer(newState).getMinions().get(ic);
				newState = tempMinion.attack(PlayerSide.WAITING_PLAYER, targetMinion, newState, deckPlayer0_, deckPlayer1_);
				if (newState != null) {
					newState = this.doMoves(newState, ai);
					if (newState != null) boardStateNode.addChild(newState);
				}
			}
		}
		return boardStateNode;
	}
	
	public HearthTreeNode createCardUseBranches(HearthTreeNode boardStateNode, ArtificialPlayer ai) throws HSException {
        log.trace("creating card use branches");

		//check to see if all the cards have been used already
		boolean allUsed = true;
		for (final Card card : boardStateNode.data_.getCurrentPlayerHand()) {
			allUsed = allUsed && card.hasBeenUsed();
		}
		
		//the case where I chose not to use any more cards
		if (!allUsed) {
			HearthTreeNode newState = new HearthTreeNode((BoardModel)boardStateNode.data_.deepCopy());
			for (Card card : newState.data_.getCurrentPlayerHand()) {
				card.hasBeenUsed(true);
			}
			newState = this.doMoves(newState, ai);
			if (newState != null) boardStateNode.addChild(newState);
		}
		
		
		int mana = boardStateNode.data_.getCurrentPlayer().getMana();
		for (int ic = 0; ic < boardStateNode.data_.getNumCards_hand(); ++ic) {
			if (boardStateNode.data_.getCurrentPlayerCardHand(ic).getMana() <= mana && !boardStateNode.data_.getCurrentPlayerCardHand(ic).hasBeenUsed()) {
				//we can use this card!  Let's try using it on everything
				for(int i = 0; i <= PlayerSide.CURRENT_PLAYER.getPlayer(boardStateNode).getNumMinions(); ++i) {
					Minion targetMinion = boardStateNode.data_.getCurrentPlayerCharacter(i);
					if (boardStateNode.data_.getCurrentPlayerCardHand(ic).canBeUsedOn(PlayerSide.CURRENT_PLAYER, targetMinion, boardStateNode.data_)) {
						HearthTreeNode newState = new HearthTreeNode((BoardModel)boardStateNode.data_.deepCopy());
						Minion copiedTargetMinion = newState.data_.getCurrentPlayerCharacter(i);
						Card card = newState.data_.getCurrentPlayerCardHand(ic);
						newState = card.getCardAction().useOn(PlayerSide.CURRENT_PLAYER, copiedTargetMinion, newState, deckPlayer0_, deckPlayer1_, false);
						if (newState != null) {
							newState = this.doMoves(newState, ai);
							if (newState != null) boardStateNode.addChild(newState);
						}
					}
				}
				for(int i = 0; i <= PlayerSide.WAITING_PLAYER.getPlayer(boardStateNode).getNumMinions(); ++i) {
					Minion targetMinion = boardStateNode.data_.getWaitingPlayerCharacter(i);
					if (boardStateNode.data_.getCurrentPlayerCardHand(ic).canBeUsedOn(PlayerSide.WAITING_PLAYER, targetMinion, boardStateNode.data_)) {
						HearthTreeNode newState = new HearthTreeNode((BoardModel)boardStateNode.data_.deepCopy());
						Minion copiedTargetMinion = newState.data_.getWaitingPlayerCharacter(i);
						Card card = newState.data_.getCurrentPlayerCardHand(ic);
						newState = card.getCardAction().useOn(PlayerSide.WAITING_PLAYER, copiedTargetMinion, newState, deckPlayer0_, deckPlayer1_, false);
						if (newState != null) {
							newState = this.doMoves(newState, ai);
							if (newState != null) boardStateNode.addChild(newState);
						}
					}
				}
			}
		}
		return boardStateNode;
	}

	/**
	 * Recursively generate all possible moves
	 * 
	 * This function recursively generates all possible moves that can be done starting from a given BoardState.
	 * While generating the moves, it applies the scoring function to each BoardState generated, and it will only keep the
	 * highest scoring branch.
	 * 
	 * The results are stored in a tree structure and returned as a tree of BoardState class.
	 * 
	 * @param boardStateNode The initial BoardState wrapped in a HearthTreeNode.
	 * @param scoreFunc The scoring function for AI.
	 * 
	 * @return boardStateNode manipulated such that all subsequent actions are children of the original boardStateNode input.
	 */
	public HearthTreeNode doMoves(HearthTreeNode boardStateNode, ArtificialPlayer ai) throws HSException {
        log.trace("recursively performing moves");

		if (lethal_) {
            log.debug("found lethal");
			//if it's lethal, we don't have to do anything ever.  Just play the lethal.
			return null;
		}

		if (timedOut_) {
            log.debug("think time is already over");
			//Time's up!  no more thinking... 
			return null;
		}
		
		if (System.currentTimeMillis() - startTime_ > maxTime_) {
            log.debug("setting think time over");
			timedOut_ = true;
			return null;
		}
				
		if (boardStateNode.numChildren() > 0) {
			//If this node already has children, just call doMoves on each of its children.
			//This situation can happen, for example, after a battle cry
			for (HearthTreeNode child : boardStateNode.getChildren()) {
				this.doMoves(child, ai);
			}
			return boardStateNode;
		}
		
		boolean lethalFound = false;
        if (!boardStateNode.data_.isAlive(PlayerSide.CURRENT_PLAYER) || !boardStateNode.data_.isAlive(PlayerSide.WAITING_PLAYER) && !boardStateNode.data_.isAlive(PlayerSide.WAITING_PLAYER)) {
            //one of the players is dead, no reason to keep playing
            lethal_ = true;
            lethalFound = true;
        }

        if (!lethalFound) {

			//-----------------------------------------------------------------------------------------
			// Use the Hero ability
			//-----------------------------------------------------------------------------------------
			boardStateNode = this.createHeroAbilityBranches(boardStateNode, ai);
			
			
			//-----------------------------------------------------------------------------------------
			// Use the cards in the hand
			//-----------------------------------------------------------------------------------------
			boardStateNode = this.createCardUseBranches(boardStateNode, ai);
			

			//-----------------------------------------------------------------------------------------
			// Attack with the minions on the board
			//-----------------------------------------------------------------------------------------
			boardStateNode = this.createMinionAttackBranches(boardStateNode, ai);


		}
		
			
		if (boardStateNode.isLeaf()) {
			//If at this point the node has no children, it is a leaf node.  Compute its board score and store it.
			boardStateNode.setScore(ai.boardScore(boardStateNode.data_));
			boardStateNode.setNumNodesTries(1);
		} else {
			//If it is not a leaf, set the score as the maximum score of its children.
			//We can also throw out any children that don't have the highest score (boy, this sounds so wrong...)
			double tmpScore = -1.e300;
			int tmpNumNodesTried = 0;
			Iterator<HearthTreeNode> iter = boardStateNode.getChildren().iterator();
			HearthTreeNode bestBranch = null;
			
			while (iter.hasNext()) {
				HearthTreeNode child = iter.next();
				tmpNumNodesTried += child.getNumNodesTried();
				double origScore = child.getScore();
				double theScore = origScore;
				if (child instanceof CardDrawNode)
					theScore += ((CardDrawNode)child).cardDrawScore(deckPlayer0_, ai);
				if (child instanceof RandomEffectNode)
					theScore = ((RandomEffectNode)child).weightedAverageScore(deckPlayer0_, ai);
				if (theScore > tmpScore) {
					tmpScore = theScore;
					bestBranch = child;
				}
			}

			if (bestBranch instanceof StopNode) {
				bestBranch.clearChildren(); //cannot continue past a StopNode
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
	 * 
	 * For each dead minion, the function calls its deathrattle in the correct order, and then removes the dead minions from the board.
	 * 
	 * @return true if there are dead minions left (minions might have died during deathrattle).  false otherwise.
	 * @throws HSException 
	 */
	public static HearthTreeNode handleDeadMinions(HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1) throws HSException {
		HearthTreeNode toRet = boardState;
		IdentityLinkedList<BoardModel.MinionPlayerPair> deadMinions = new IdentityLinkedList<BoardModel.MinionPlayerPair>();
		for (BoardModel.MinionPlayerPair minionIdPair : toRet.data_.getAllMinionsFIFOList()) {
			if (minionIdPair.getMinion().getTotalHealth() <= 0) {
				deadMinions.add(minionIdPair);
			}
		}
		for (BoardModel.MinionPlayerPair minionIdPair : deadMinions) {
            PlayerSide playerSide = boardState.data_.sideForModel(minionIdPair.getPlayerModel());
            toRet = minionIdPair.getMinion().destroyed(playerSide, toRet, deckPlayer0, deckPlayer1);
			toRet.data_.removeMinion(minionIdPair);
		}
		if (toRet.data_.hasDeadMinions())
			return BoardStateFactoryBase.handleDeadMinions(toRet, deckPlayer0, deckPlayer1);
		else
			return toRet;
	}
	
	
}


