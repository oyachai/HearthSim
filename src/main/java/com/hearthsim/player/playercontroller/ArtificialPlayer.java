package com.hearthsim.player.playercontroller;

import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.util.DeepCopyable;
import com.hearthsim.util.HearthActionBoardPair;
import com.hearthsim.util.factory.BoardStateFactoryBase;

import java.util.List;

public interface ArtificialPlayer extends DeepCopyable<ArtificialPlayer> {

    /**
     * Play a turn
     *
     * This function is called by GameMaster, and it should return a BoardState resulting from the AI playing its turn.
     *
     * @param turn Turn number, 1-based
     * @param board The board state at the beginning of the turn (after all card draws and minion deaths)
     *
     * @return A list of HearthActionBoardPair that the AI has performed, starting from the earliest play to the last.
     * @throws HSException
     */
    public List<HearthActionBoardPair> playTurn(int turn, BoardModel board) throws HSException;


    /**
     * Play a turn
     *
     * This function is called by GameMaster, and it should return a BoardState resulting from the AI playing its turn.
     *
     * @param turn Turn number, 1-based
     * @param board The board state at the beginning of the turn (after all card draws and minion deaths)
     * @param factory The factory to use for node generation
     *
     * @return A list of HearthActionBoardPair that the AI has performed, starting from the earliest play to the last.
     * @throws HSException
     */
    public List<HearthActionBoardPair> playTurn(int turn, BoardModel board, BoardStateFactoryBase factory) throws HSException;

    public int getMaxThinkTime();
}
