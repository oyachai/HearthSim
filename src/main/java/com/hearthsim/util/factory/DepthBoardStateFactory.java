package com.hearthsim.util.factory;

import com.hearthsim.card.Deck;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.player.playercontroller.BoardScorer;
import com.hearthsim.util.tree.CardDrawNode;
import com.hearthsim.util.tree.HearthTreeNode;
import com.hearthsim.util.tree.RandomEffectNode;
import com.hearthsim.util.tree.StopNode;

import java.util.ArrayList;
import java.util.HashSet;

public class DepthBoardStateFactory extends BoardStateFactoryBase {

    private boolean lethal_;
    private boolean timedOut_;
    private final long maxTime_;

    private long startTime_;

    private final boolean useDuplicateNodePruning;
    private int numNodes;
    private int numDuplicates;
    private HashSet<BoardModel> boardsAlreadySeen;

    /**
     * Constructor
     * maxThinkTime defaults to 10000 milliseconds (10 seconds)
     */
    public DepthBoardStateFactory(Deck deckPlayer0, Deck deckPlayer1, boolean useDuplicateNodePruning) {
        this(deckPlayer0, deckPlayer1, 10000, useDuplicateNodePruning);
    }

    /**
     * Constructor
     *
     * @param deckPlayer0
     * @param deckPlayer1
     * @param maxThinkTime The maximum amount of time in milliseconds the factory is allowed to spend on generating the simulation tree.
     */
    public DepthBoardStateFactory(Deck deckPlayer0, Deck deckPlayer1, long maxThinkTime, boolean useDuplicateNodePruning) {
        super(deckPlayer0, deckPlayer1);

        lethal_ = false;
        startTime_ = System.currentTimeMillis();
        maxTime_ = maxThinkTime;
        timedOut_ = false;
        this.useDuplicateNodePruning = useDuplicateNodePruning;
        if (useDuplicateNodePruning)
            boardsAlreadySeen = new HashSet<>(500000);
    }

    public DepthBoardStateFactory(Deck deckPlayer0, Deck deckPlayer1, long maxThinkTime, boolean useDuplicateNodePruning, SparseChildNodeCreator sparseChildNodeCreator) {
        super(deckPlayer0, deckPlayer1, sparseChildNodeCreator);

        lethal_ = false;
        startTime_ = System.currentTimeMillis();
        maxTime_ = maxThinkTime;
        timedOut_ = false;
        this.useDuplicateNodePruning = useDuplicateNodePruning;
        if (useDuplicateNodePruning)
            boardsAlreadySeen = new HashSet<>(500000);
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
    @Override
    public HearthTreeNode doMoves(HearthTreeNode boardStateNode, BoardScorer ai) throws HSException {
        log.trace("recursively performing moves");

        if (lethal_) {
            log.debug("found lethal");
            // if it's lethal, we don't have to do anything ever. Just play the lethal.
            return null;
        }

        if (System.currentTimeMillis() - startTime_ > maxTime_) {
            log.debug("setting think time over");
            timedOut_ = true;
        }

        if (timedOut_) {
            log.debug("think time is already over");
            // Time's up! no more thinking...
            return null;
        }

        boolean lethalFound = false;
        if (boardStateNode.data_.isLethalState()) { // one of the players is dead, no reason to keep playing
            lethal_ = true;
            lethalFound = true;
        }

        boardStateNode.setScore(ai.boardScore(boardStateNode.data_));

        // We can end up with children at this state, for example, after a battle cry. If we don't have children yet, create them.
        ++numNodes;
        if (!lethalFound && boardStateNode.numChildren() <= 0) {
            if (useDuplicateNodePruning) {
                if (boardsAlreadySeen.contains(boardStateNode.data_)) {
                    boardStateNode.setBestChildScore(boardStateNode.getScore());
                    ++numDuplicates;
                    return null;
                } else {
                    boardsAlreadySeen.add(boardStateNode.data_);
                }
            }
            ArrayList<HearthTreeNode> nodes = this.createChildren(boardStateNode);
            boardStateNode.addChildren(nodes);
        }

        if (boardStateNode.isLeaf()) {
            // If at this point the node has no children, it is a leaf node. Set its best child score to its own score.
            boardStateNode.setBestChildScore(boardStateNode.getScore());
        } else {
            // If it is not a leaf, set the score as the maximum score of its children.
            // We can also throw out any children that don't have the highest score (boy, this sounds so wrong...)
            double tmpScore;
            double bestScore = 0;
            HearthTreeNode bestBranch = null;

            for (HearthTreeNode child : boardStateNode.getChildren()) {
//                try {
                    this.doMoves(child, ai); // Don't need to check lethal because lethal states shouldn't get children. Even if they do, doMoves resolves the issue.
//                } catch (StackOverflowError e) {
//                    e.printStackTrace();
//                }
                tmpScore = child.getBestChildScore();

                // We need to add the card score after child scoring because CardDrawNode children
                // do not inherit the value of drawn cards
                // TODO Children of CardDrawNodes should be able to track this on their own. Doing
                // it this way "breaks" the best score chain and makes it harder to isolate and test
                // scoring. It effectively "bubbles down" the tree but it isn't sent through when
                // creating children.
                if (child instanceof CardDrawNode) {
                    tmpScore += ((CardDrawNode)child).cardDrawScore(deckPlayer0_, ai);
                }

                if (bestBranch == null || tmpScore > bestScore) {
                    bestBranch = child;
                    bestScore = tmpScore;
                }
            }

            // TODO this should be automatically handled else where...
            // Cannot continue past a StopNode except RNG nodes. The children of RNG nodes will be used
            // during resolution so if we clear them out it can get awkward
            if (bestBranch instanceof StopNode && !(bestBranch instanceof RandomEffectNode)) {
                bestBranch.clearChildren();
            }

            if (boardStateNode instanceof RandomEffectNode) {
                // Set best child score according to the random effect score. We need to set this after all descendants have been calculated
                double boardScore = ((RandomEffectNode)boardStateNode).weightedAverageBestChildScore();
                boardStateNode.setBestChildScore(boardScore);
                // TODO do we want to override boardStateNode.score here?
            } else {
                boardStateNode.clearChildren();
                boardStateNode.addChild(bestBranch);
                if (bestBranch != null) {
                    boardStateNode.setBestChildScore(bestBranch.getBestChildScore());
                }
            }
        }

        return boardStateNode;
    }
}
