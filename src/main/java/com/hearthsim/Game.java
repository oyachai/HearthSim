package com.hearthsim;

import com.hearthsim.card.Card;
import com.hearthsim.card.CardEndTurnInterface;
import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.basic.spell.TheCoin;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.player.playercontroller.ArtificialPlayer;
import com.hearthsim.results.GameRecord;
import com.hearthsim.results.GameResult;
import com.hearthsim.results.GameSimpleRecord;
import com.hearthsim.util.HearthAction;
import com.hearthsim.util.HearthAction.Verb;
import com.hearthsim.util.HearthActionBoardPair;
import com.hearthsim.util.factory.BoardStateFactoryBase;
import com.hearthsim.util.tree.HearthTreeNode;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    private final static int maxTurns_ = 100;

    private BoardModel boardModel;

    private PlayerModel playerGoingFirst;

    private ArtificialPlayer aiForPlayerGoingFirst;
    private ArtificialPlayer aiForPlayerGoingSecond;

    public final ArrayList<HearthActionBoardPair> gameHistory = new ArrayList<>();

    public Game(PlayerModel playerModel0, PlayerModel playerModel1, ArtificialPlayer ai0, ArtificialPlayer ai1) {
            this(playerModel0, playerModel1, ai0, ai1, false);
        }

        public Game(PlayerModel playerModel0, PlayerModel playerModel1, ArtificialPlayer ai0, ArtificialPlayer ai1, int firstPlayerId) {
            playerGoingFirst = playerModel0;
            PlayerModel playerGoingSecond = playerModel1;

        aiForPlayerGoingFirst = ai0;
        aiForPlayerGoingSecond = ai1;

        if (firstPlayerId == 1) {
            playerGoingFirst = playerModel1;
            playerGoingSecond = playerModel0;
            aiForPlayerGoingFirst = ai1;
            aiForPlayerGoingSecond = ai0;
        }
        log.debug("alternate play order: {}", firstPlayerId);
        log.debug("first player id: {}", playerGoingFirst.getPlayerId());

            boardModel = new BoardModel(playerGoingFirst, playerGoingSecond);

        }

        public Game(PlayerModel playerModel0, PlayerModel playerModel1, ArtificialPlayer ai0, ArtificialPlayer ai1, boolean shufflePlayOrder) {
            this(playerModel0, playerModel1, ai0, ai1, shufflePlayOrder && Math.random() >= 0.5 ? 0 : 1);
    }

    public GameResult runGame() throws HSException {
        //the first player draws 3 cards
        boardModel.getCurrentPlayer().placeCardHand(0);
        boardModel.getCurrentPlayer().placeCardHand(1);
        boardModel.getCurrentPlayer().placeCardHand(2);
        boardModel.getCurrentPlayer().setDeckPos((byte)3);

        //the second player draws 4 cards
        boardModel.getWaitingPlayer().placeCardHand(0);
        boardModel.getWaitingPlayer().placeCardHand(1);
        boardModel.getWaitingPlayer().placeCardHand(2);
        boardModel.getWaitingPlayer().placeCardHand(3);
        boardModel.getWaitingPlayer().placeCardHand(new TheCoin());
        boardModel.getWaitingPlayer().setDeckPos((byte)4);

        GameRecord record = new GameSimpleRecord();

        record.put(0, PlayerSide.CURRENT_PLAYER, boardModel.deepCopy(), null);
        record.put(0, PlayerSide.CURRENT_PLAYER, boardModel.flipPlayers().deepCopy(), null);

        gameHistory.add(new HearthActionBoardPair(null, boardModel));

        GameResult gameResult;
        for (int turnCount = 0; turnCount < maxTurns_; ++turnCount) {
            log.debug("starting turn " + turnCount);
            long turnStart = System.currentTimeMillis();

            gameResult = playTurn(turnCount, record, aiForPlayerGoingFirst);
            if (gameResult != null)
                return gameResult;

            gameResult = playTurn(turnCount, record, aiForPlayerGoingSecond);
            if (gameResult != null)
                return gameResult;

            long turnEnd = System.currentTimeMillis();
            long turnDelta = turnEnd - turnStart;
            if (turnDelta > aiForPlayerGoingFirst.getMaxThinkTime() / 2) {
                log.warn("turn took {} ms, more than half of alloted think time ({})", turnDelta,
                        aiForPlayerGoingFirst.getMaxThinkTime());
            } else {
                log.debug("turn took {} ms", turnDelta);
            }

        }
        return new GameResult(playerGoingFirst.getPlayerId(), -1, 0, record);
    }

    private GameResult playTurn(int turnCount, GameRecord record, ArtificialPlayer ai) throws HSException {
        boardModel = Game.beginTurn(boardModel.deepCopy()); // Deep copy here to make sure history is preserved properly
        gameHistory.add(new HearthActionBoardPair(new HearthAction(Verb.START_TURN), boardModel.deepCopy()));

        GameResult gameResult;

        gameResult = checkGameOver(turnCount, record);
        if (gameResult != null)
            return gameResult;

        List<HearthActionBoardPair> allMoves = ai.playTurn(turnCount, boardModel);
        if (allMoves.size() > 0) {
            // If allMoves is empty, it means that there was absolutely nothing the AI could do
            boardModel = allMoves.get(allMoves.size() - 1).board;
            gameHistory.addAll(allMoves);
        }

        boardModel = Game.endTurn(boardModel);

        record.put(turnCount + 1, PlayerSide.CURRENT_PLAYER, boardModel.deepCopy(), allMoves);

        gameResult = checkGameOver(turnCount, record);
        if (gameResult != null)
            return gameResult;

        boardModel = boardModel.flipPlayers();
        gameHistory.add(new HearthActionBoardPair(new HearthAction(Verb.END_TURN), boardModel.deepCopy()));

        return null;
    }

    public GameResult checkGameOver(int turnCount, GameRecord record) {
        if (boardModel.isDead(PlayerSide.CURRENT_PLAYER)) {
            PlayerModel winner = boardModel.modelForSide(PlayerSide.WAITING_PLAYER);
            return new GameResult(playerGoingFirst.getPlayerId(), winner.getPlayerId(), turnCount + 1, record);
        } else if (boardModel.isDead(PlayerSide.WAITING_PLAYER)) {
            PlayerModel winner = boardModel.modelForSide(PlayerSide.CURRENT_PLAYER);
            return new GameResult(playerGoingFirst.getPlayerId(), winner.getPlayerId(), turnCount + 1, record);
        }
        return null;
    }

    public static BoardModel beginTurn(BoardModel board) throws HSException {

        HearthTreeNode toRet = new HearthTreeNode(board);

        toRet.data_.resetHand();
        toRet.data_.resetMinions();

        PlayerModel currentPlayer = toRet.data_.getCurrentPlayer();
        PlayerModel waitingPlayer = toRet.data_.getWaitingPlayer();

        for (Minion targetMinion : currentPlayer.getMinions()) {
            toRet = targetMinion.startTurn(PlayerSide.CURRENT_PLAYER, toRet);
        }
        for (Minion targetMinion : waitingPlayer.getMinions()) {
            toRet = targetMinion.startTurn(PlayerSide.WAITING_PLAYER, toRet);
        }

        toRet = BoardStateFactoryBase.handleDeadMinions(toRet);

        // TODO card draw will need to move into BoardModel at some point so we can react to them appropriately
        Card drawn = currentPlayer.drawNextCardFromDeck();
        toRet.data_.applyAurasToCardInHand(PlayerSide.CURRENT_PLAYER, drawn);

        if (currentPlayer.getMaxMana() < 10) {
            currentPlayer.addMaxMana((byte) 1);
        }
        toRet.data_.resetMana();

        return toRet.data_;
    }

    public static BoardModel endTurn(BoardModel board) throws HSException {
        HearthTreeNode toRet = new HearthTreeNode(board);

        PlayerModel currentPlayer = toRet.data_.getCurrentPlayer();
        PlayerModel waitingPlayer = toRet.data_.getWaitingPlayer();

        toRet = currentPlayer.getHero().endTurn(PlayerSide.CURRENT_PLAYER, toRet);
        toRet = waitingPlayer.getHero().endTurn(PlayerSide.WAITING_PLAYER, toRet);

        // TODO: The minions should trigger end-of-turn effects in the order that they were played
        for (int index = 0; index < currentPlayer.getNumMinions(); ++index) {
            CardEndTurnInterface targetMinion = currentPlayer.getCharacter(CharacterIndex.fromInteger(index + 1));
            try {
                toRet = targetMinion.endTurn(PlayerSide.CURRENT_PLAYER, toRet);
            } catch(HSException e) {
                e.printStackTrace();
            }
        }
        for (int index = 0; index < waitingPlayer.getNumMinions(); ++index) {
            CardEndTurnInterface targetMinion = waitingPlayer.getCharacter(CharacterIndex.fromInteger(index + 1));
            try {
                toRet = targetMinion.endTurn(PlayerSide.WAITING_PLAYER, toRet);
            } catch(HSException e) {
                e.printStackTrace();
            }
        }

        toRet = BoardStateFactoryBase.handleDeadMinions(toRet);

        return toRet.data_;
    }
}
