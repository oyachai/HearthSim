package com.hearthsim;

import java.util.ArrayList;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.exception.HSException;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.player.playercontroller.ArtificialPlayer;
import com.hearthsim.results.GameRecord;
import com.hearthsim.results.GameResult;
import com.hearthsim.results.GameSimpleRecord;
import com.hearthsim.util.tree.HearthTreeNode;

public class Game {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

	final static int maxTurns_ = 100;

    BoardModel boardModel_;
    
    PlayerModel playerGoingFirst;
    PlayerModel playerGoingSecond;
    
    ArtificialPlayer aiForPlayerGoingFirst;
    ArtificialPlayer aiForPlayerGoingSecond;

	int curTurn_;
	
	public Game(PlayerModel playerModel0, PlayerModel playerModel1, ArtificialPlayer ai0, ArtificialPlayer ai1) {
		this(playerModel0, playerModel1, ai0, ai1, false);
	}
	
	public Game(PlayerModel playerModel0, PlayerModel playerModel1, ArtificialPlayer ai0, ArtificialPlayer ai1, boolean shufflePlayOrder) {
        
		playerGoingFirst = playerModel0;
        playerGoingSecond = playerModel1;
        
        aiForPlayerGoingFirst = ai0;
        aiForPlayerGoingSecond = ai1;

        if (shufflePlayOrder && Math.random() > 0.5) {
            playerGoingFirst = playerModel1;
            playerGoingSecond = playerModel0;
            aiForPlayerGoingFirst = ai1;
            aiForPlayerGoingSecond = ai0;
        }
        log.debug("shuffle play order: {}", shufflePlayOrder);
        log.debug("first player id: {}", playerGoingFirst.getPlayerId());

		boardModel_ = new BoardModel(playerGoingFirst, playerGoingSecond);
	}
	
	public GameResult runGame() throws HSException {
		curTurn_ = 0;

		//the first player draws 3 cards
		boardModel_.placeCardHandCurrentPlayer(0);
		boardModel_.placeCardHandCurrentPlayer(1);
		boardModel_.placeCardHandCurrentPlayer(2);
		boardModel_.setDeckPos_p0(3);

		//the second player draws 4 cards
		boardModel_.placeCardHandWaitingPlayer(0);
		boardModel_.placeCardHandWaitingPlayer(1);
		boardModel_.placeCardHandWaitingPlayer(2);
		boardModel_.placeCardHandWaitingPlayer(3);
		boardModel_.placeCardHandWaitingPlayer(new TheCoin());
		boardModel_.setDeckPos_p1(4);
		
		GameRecord record = new GameSimpleRecord();

		record.put(0, PlayerSide.CURRENT_PLAYER, (BoardModel) boardModel_.deepCopy());
		record.put(0, PlayerSide.CURRENT_PLAYER, (BoardModel) boardModel_.flipPlayers().deepCopy());

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
            if (turnDelta > ArtificialPlayer.MAX_THINK_TIME / 2) {
                log.warn("turn took {} ms, more than half of alloted think time ({})", turnDelta, ArtificialPlayer.MAX_THINK_TIME);
            } else {
                log.debug("turn took {} ms", turnDelta);
            }

		}
		return new GameResult(playerGoingFirst.getPlayerId(), -1, 0, record);
	}

    private GameResult playTurn(int turnCount, GameRecord record, ArtificialPlayer ai) throws HSException {
        beginTurn(turnCount, boardModel_);

        GameResult gameResult;

        gameResult = checkGameOver(turnCount, record);
        if (gameResult != null) return gameResult;

        boardModel_ = playAITurn(turnCount, boardModel_, ai);
        boardModel_ = endTurn(boardModel_);

        record.put(turnCount + 1, PlayerSide.CURRENT_PLAYER, (BoardModel) boardModel_.deepCopy());

        gameResult = checkGameOver(turnCount, record);
        if (gameResult != null) return gameResult;

        boardModel_ = boardModel_.flipPlayers();

        return null;
    }

    public GameResult checkGameOver(int turnCount, GameRecord record){
        if (!boardModel_.isAlive(PlayerSide.CURRENT_PLAYER)) {
        	PlayerModel winner = boardModel_.modelForSide(PlayerSide.WAITING_PLAYER);
            return new GameResult(playerGoingFirst.getPlayerId(), winner.getPlayerId(), turnCount + 1, record);
        } else if (!boardModel_.isAlive(PlayerSide.WAITING_PLAYER)) {
        	PlayerModel winner = boardModel_.modelForSide(PlayerSide.CURRENT_PLAYER);
            return new GameResult(playerGoingFirst.getPlayerId(), winner.getPlayerId(), turnCount + 1, record);
        } 
        return null;
    }

    public void beginTurn(int turn, BoardModel board) throws HSException {

        board.startTurn();

        Card newCard = board.getCurrentPlayer().drawFromDeck(board.getDeckPos_p0());
        if (newCard == null) {
            //fatigue
            byte fatigueDamage = board.getFatigueDamage_p0();
            board.setFatigueDamage_p0((byte)(fatigueDamage + 1));
            board.getCurrentPlayerHero().setHealth((byte)(board.getCurrentPlayerHero().getHealth() - fatigueDamage));
        } else {
            board.setDeckPos_p0(board.getDeckPos_p0() + 1);
            board.placeCardHandCurrentPlayer(newCard);
        }
        if (board.getCurrentPlayer().getMaxMana() < 10)
            board.getCurrentPlayer().addMaxMana(1);
        board.resetMana();

    }

    public BoardModel playAITurn(int turn, BoardModel board, ArtificialPlayer ai) throws HSException {
        return ai.playTurn(turn, board, board.getCurrentPlayer(), board.getWaitingPlayer());
    }

    public BoardModel endTurn(BoardModel board) throws HSException {
    	Deck deckPlayer0 = board.getCurrentPlayer().getDeck();
    	Deck deckPlayer1 = board.getWaitingPlayer().getDeck();
    	
    	HearthTreeNode toRet = new HearthTreeNode(board);
    	
    	toRet = toRet.data_.getCurrentPlayer().getHero().endTurn(PlayerSide.CURRENT_PLAYER, toRet, deckPlayer0, deckPlayer1);
    	toRet = toRet.data_.getWaitingPlayer().getHero().endTurn(PlayerSide.WAITING_PLAYER, toRet, deckPlayer0, deckPlayer1);
        for (int index = 0; index < toRet.data_.getCurrentPlayer().getMinions().size(); ++index) {
            Minion targetMinion = toRet.data_.getCurrentPlayer().getMinions().get(index);
            try {
                toRet = targetMinion.endTurn(PlayerSide.CURRENT_PLAYER, toRet, deckPlayer0, deckPlayer1);
            } catch (HSException e) {
                e.printStackTrace();
            }
        }
        for (int index = 0; index < toRet.data_.getWaitingPlayer().getMinions().size(); ++index) {
            Minion targetMinion = toRet.data_.getWaitingPlayer().getMinions().get(index);
            try {
                toRet = targetMinion.endTurn(PlayerSide.WAITING_PLAYER, toRet, deckPlayer0, deckPlayer1);
            } catch (HSException e) {
                e.printStackTrace();
            }
        }

        ArrayList<Minion> toRemove = new ArrayList<Minion>();
        for (Minion targetMinion : toRet.data_.getCurrentPlayer().getMinions()) {
            if (targetMinion.getTotalHealth() <= 0)
                toRemove.add(targetMinion);
        }
        for (Minion minion : toRemove)
        	toRet.data_.getCurrentPlayer().getMinions().remove(minion);

        toRemove.clear();
        for (Minion targetMinion : toRet.data_.getWaitingPlayer().getMinions()) {
            if (targetMinion.getTotalHealth() <= 0)
                toRemove.add(targetMinion);
        }
        for (Minion minion : toRemove)
        	toRet.data_.getWaitingPlayer().getMinions().remove(minion);
        
        return toRet.data_;
    }
}
