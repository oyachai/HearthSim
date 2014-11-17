package com.hearthsim.util.factory;

import java.util.ArrayList;

import com.hearthsim.card.Deck;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.player.playercontroller.BoardScorer;
import com.hearthsim.util.IdentityLinkedList;
import com.hearthsim.util.tree.HearthTreeNode;

public abstract class BoardStateFactoryBase {

	protected final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

	protected final Deck deckPlayer0_;
	protected final Deck deckPlayer1_;

	protected final ChildNodeCreator childNodeCreator;

	/**
	 * Constructor
	 * 
	 * @param deckPlayer0
	 * @param deckPlayer1
	 */
	public BoardStateFactoryBase(Deck deckPlayer0, Deck deckPlayer1) {
		this(deckPlayer0, deckPlayer1, new ChildNodeCreatorBase(deckPlayer0, deckPlayer1));
	}

	/**
	 * Constructor
	 * 
	 * @param deckPlayer0
	 * @param deckPlayer1
	 */
	public BoardStateFactoryBase(Deck deckPlayer0, Deck deckPlayer1, ChildNodeCreator creator) {
		deckPlayer0_ = deckPlayer1;
		deckPlayer1_ = deckPlayer1;

		childNodeCreator = creator;
	}

	public ArrayList<HearthTreeNode> createChildren(HearthTreeNode boardStateNode) throws HSException {
		ArrayList<HearthTreeNode> nodes = new ArrayList<HearthTreeNode>();
		nodes.addAll(this.childNodeCreator.createHeroAbilityChildren(boardStateNode));
		nodes.addAll(this.childNodeCreator.createPlayCardChildren(boardStateNode));
		nodes.addAll(this.childNodeCreator.createAttackChildren(boardStateNode));
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
	public abstract HearthTreeNode doMoves(HearthTreeNode boardStateNode, BoardScorer ai) throws HSException;

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
