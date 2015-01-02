package com.hearthsim.player.playercontroller;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public interface BoardScorer {
    /**
     * Board score function
     * The all important board score function. It is a function that measures how 'good' the given board is.
     * As a convention, this function should be an increasing function of the board's goodness.
     *
     * @param board The current board state
     * @return
     */
    public double boardScore(BoardModel board);

    /**
     * Returns the card score for a particular card assuming that it is in the hand
     *
     * @param card
     * @return
     */
    public double cardInHandScore(Card card, BoardModel board);


    /**
     * Returns the score of a minion assuming that it is on the board (in the field)
     * @param minion The minion to be scored
     * @param board The current board
     * @return
     */
    public double minionOnBoardScore(Minion minion, PlayerSide side, BoardModel board);

    public double heroHealthScore_p0(double heroHealth, double heroArmor);

    public double heroHealthScore_p1(double heroHealth, double heroArmor);
}
