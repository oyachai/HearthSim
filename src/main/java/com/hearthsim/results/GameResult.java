package com.hearthsim.results;

public class GameResult {

    public final int firstPlayerIndex_;
    public final int winnerPlayerIndex_;
    public final int gameDuration_;
    public final GameRecord record_;

    /**
     *
     * @param firstPlayerIndex Player index of the player that goes first
     * @param winnerPlayerIndex Player index of the winner
     * @param gameDuration
     * @param record
     */
    public GameResult(int firstPlayerIndex, int winnerPlayerIndex, int gameDuration, GameRecord record) {
        firstPlayerIndex_ = firstPlayerIndex;
        winnerPlayerIndex_ = winnerPlayerIndex;
        gameDuration_ = gameDuration;
        record_ = record;
    }
}
