package com.hearthsim.util.factory;

import com.hearthsim.card.Deck;
import com.hearthsim.exception.HSException;
import com.hearthsim.player.playercontroller.BoardScorer;
import com.hearthsim.util.tree.HearthTreeNode;
import com.hearthsim.util.tree.RandomEffectNode;
import com.hearthsim.util.tree.StopNode;

import java.util.ArrayList;
import java.util.TreeSet;

public class BreadthBoardStateFactory extends BoardStateFactoryBase {

    public BreadthBoardStateFactory(Deck deckPlayer0, Deck deckPlayer1) {
        super(deckPlayer0, deckPlayer1);
    }

    // Currently only used to test lethal combinations. AI should use depthFirstSearch instead.
    public void addChildLayers(HearthTreeNode boardStateNode, int maxDepth) throws HSException {

        // Saving seen states lets us prune duplicate states. This saves us a ton of time when the trees get deep.
        TreeSet<Integer> states = new TreeSet<>();

        // We need to process things by batch so we can track how deep we've gone.
        ArrayList<HearthTreeNode> currentDepth = new ArrayList<>();
        ArrayList<HearthTreeNode> nextDepth = new ArrayList<>();
        currentDepth.add(boardStateNode);

        // Debugging variables for tracking loop complications
        boolean dupeSkipOn = true;
        int depthReached = 0;
        int processedCount = 0;
        int childCount = 0;
        int stateCompareCount = 0;
        int dupeSkip = 0;

        HearthTreeNode current;
        while(maxDepth > 0) {
            while(!currentDepth.isEmpty()) {
                processedCount++;
                current = currentDepth.remove(0);

                // The game ended; nothing left to do.
                if (current.data_.isLethalState()) {
                    continue;
                }

                // Try to use existing children if possible. This can happen after an auto-populating node (e.g., Battlecries)
                ArrayList<HearthTreeNode> children;
                if (current.isLeaf()) {
                    children = this.createChildren(current);
                } else {
                    children = new ArrayList<>(current.getChildren());
                }
                current.clearChildren(); // TODO suboptimal but we need to remove existing children so we can check for duplicates

                for (HearthTreeNode child : children) {
                    childCount++;
                    if (!(child instanceof StopNode)) {
                        if (dupeSkipOn) {
                            if (states.size() > 0) {
                                stateCompareCount += Math.log(states.size()); // .contains uses quick search
                                // Using .hashCode lets us use TreeSet and .contains to look for dupes
                                if (states.contains(child.data_.hashCode())) {
                                    dupeSkip++;
                                    continue;
                                }
                            }
                            states.add(child.data_.hashCode());
                        }
                        nextDepth.add(child); // Add to next depth so the inner loop will eventually process it
                    } else {
                        this.addChildLayers(child, maxDepth - 1);
                    }
                    current.addChild(child);
                }
            }

            if (nextDepth.isEmpty()) {
                break;
            }

            depthReached++;
            maxDepth--;
            ArrayList<HearthTreeNode> old = currentDepth;
            currentDepth = nextDepth;
            nextDepth = old; // We processed all of the previous depth so we don't need to .clear().
        }

        log.debug("createChildLayers summary depthReached=" + depthReached + " processedCount=" + processedCount
                + " childCount=" + childCount + " compareCount=" + stateCompareCount + " dupeSkip=" + dupeSkip);
    }

    private void processScoresForTree(HearthTreeNode root, BoardScorer ai, boolean scoringPruning) {
        double score = ai.boardScore(root.data_);
        root.setScore(score);

        if (root.isLeaf()) {
            root.setBestChildScore(score);
        } else {
            HearthTreeNode best = null;
            for (HearthTreeNode child : root.getChildren()) {
                this.processScoresForTree(child, ai, scoringPruning);

                if (best == null || best.getBestChildScore() < child.getBestChildScore()) {
                    best = child;
                }
            }

            // We need to process this after the children have populated their scores
            if (root instanceof RandomEffectNode) {
                double boardScore = ((RandomEffectNode)root).weightedAverageBestChildScore();
                root.setScore(boardScore);
                root.setBestChildScore(boardScore);
            } else {
                if (best != null) {
                    root.setBestChildScore(best.getBestChildScore());
                    if (scoringPruning) {
                        root.clearChildren();
                        root.addChild(best);
                    }
                }
            }
        }
    }

    @Override
    public HearthTreeNode doMoves(HearthTreeNode root, BoardScorer ai) throws HSException {
        this.breadthFirstSearch(root, ai, 100, false);
        return root;
    }

    protected void breadthFirstSearch(HearthTreeNode root, BoardScorer ai, int maxDepth, boolean prune) throws HSException {
        this.addChildLayers(root, maxDepth);
        this.processScoresForTree(root, ai, prune);
    }
}
