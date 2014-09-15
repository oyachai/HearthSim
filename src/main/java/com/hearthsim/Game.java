package com.hearthsim;

import com.hearthsim.card.Card;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.player.playercontroller.ArtificialPlayer;
import com.hearthsim.results.GameRecord;
import com.hearthsim.results.GameResult;
import com.hearthsim.results.GameSimpleRecord;

public class Game {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

	final static int maxTurns_ = 100;

    BoardModel boardModel_;
    ArtificialPlayer [] ais;

	int curPlayer_;
	int curTurn_;
	
	int s0_; //player index of the player that goes first
	int s1_; //player index of the player that goes second
	
	public Game(PlayerModel playerModel0, PlayerModel playerModel1, ArtificialPlayer ai0, ArtificialPlayer ai1) {
		this(playerModel0, playerModel1, ai0, ai1, false);
	}
	
	public Game(PlayerModel playerModel0, PlayerModel playerModel1, ArtificialPlayer ai0, ArtificialPlayer ai1, boolean shufflePlayOrder) {
		s0_ = 0;
		s1_ = 1;

        PlayerModel firstPlayer = playerModel0;
        if (shufflePlayOrder && Math.random() > 0.5) {
            s0_ = 1;
            s1_ = 0;
            firstPlayer = playerModel1;
        }
        log.debug("shuffle play order: {}", shufflePlayOrder);
        log.debug("first player index: {}", s0_);

		boardModel_ = new BoardModel(playerModel0, playerModel1, firstPlayer);

        this.ais = new ArtificialPlayer[2];

        this.ais[0] = ai0;
        this.ais[1] = ai1;
	}
	
	public GameResult runGame() throws HSException {
		curTurn_ = 0;
		curPlayer_ = 0;

		//the first player draws 3 cards
		boardModel_.placeCard_hand_p0(0);
		boardModel_.placeCard_hand_p0(1);
		boardModel_.placeCard_hand_p0(2);
		boardModel_.setDeckPos_p0(3);

		//the second player draws 4 cards
		boardModel_.placeCard_hand_p1(0);
		boardModel_.placeCard_hand_p1(1);
		boardModel_.placeCard_hand_p1(2);
		boardModel_.placeCard_hand_p1(3);
		boardModel_.placeCard_hand_p1(new TheCoin());
		boardModel_.setDeckPos_p1(4);
		
		GameRecord record = new GameSimpleRecord();
		record.put(0, s0_, (BoardModel) boardModel_.deepCopy());
		record.put(0, s1_, (BoardModel) boardModel_.flipPlayers().deepCopy());

        GameResult gameResult;
        for (int turnCount = 0; turnCount < maxTurns_; ++turnCount) {
            log.info("starting turn " + turnCount);
            long turnStart = System.currentTimeMillis();

            gameResult = playTurn(turnCount, record, ais[s0_]);
            if (gameResult != null)
                return gameResult;

            gameResult = playTurn(turnCount, record, ais[s1_]);
            if (gameResult != null)
                return gameResult;

            long turnEnd = System.currentTimeMillis();
            long turnDelta = turnEnd - turnStart;
            if (turnDelta > ArtificialPlayer.MAX_THINK_TIME / 2) {
                log.warn("turn took {} ms, more than half of alloted think time ({})", turnDelta);
            } else {
                log.debug("turn took {} ms", turnDelta);
            }

		}
		return new GameResult(s0_, -1, 0, record);
	}

    private GameResult playTurn(int turnCount, GameRecord record, ArtificialPlayer ai) throws HSException {
        beginTurn(turnCount, boardModel_);

        GameResult gameResult;

        gameResult = checkGameOver(turnCount, record);
        if (gameResult != null) return gameResult;

        boardModel_ = playAITurn(turnCount, boardModel_, ai);
        endTurn(boardModel_);

        record.put(turnCount + 1, s0_, (BoardModel) boardModel_.deepCopy());

        gameResult = checkGameOver(turnCount, record);
        if (gameResult != null) return gameResult;

        boardModel_ = boardModel_.flipPlayers();

        return null;
    }

    public GameResult checkGameOver(int turnCount, GameRecord record){
        if (!boardModel_.isAlive_p0()) {
            return new GameResult(s0_, s1_, turnCount + 1, record);
        } else if (!boardModel_.isAlive_p1()) {
            return new GameResult(s0_, s0_, turnCount + 1, record);
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
            board.getHero_p0().setHealth((byte)(board.getHero_p0().getHealth() - fatigueDamage));
        } else {
            board.setDeckPos_p0(board.getDeckPos_p0() + 1);
            board.placeCard_hand_p0(newCard);
        }
        if (board.getMana_p0() < 10)
            board.addMaxMana_p0(1);
        board.resetMana();

    }

    public BoardModel playAITurn(int turn, BoardModel board, ArtificialPlayer ai) throws HSException {
        return ai.playTurn(turn, board, board.getCurrentPlayer(), board.getWaitingPlayer());
    }

    public void endTurn(BoardModel board) throws HSException {
        board.endTurn(board.getCurrentPlayer().getDeck(), board.getWaitingPlayer().getDeck());
    }
}
