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
	PlayerModel[] players_;
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
        if (shufflePlayOrder && Math.random() > 0.5) {
            s0_ = 1;
            s1_ = 0;
        }
        log.debug("shuffle play order: {}", shufflePlayOrder);
        log.debug("first player index: {}", s0_);

        players_ = new PlayerModel[2];
		players_[0] = playerModel0;
		players_[1] = playerModel1;
		boardModel_ = new BoardModel(players_[s0_].hero_, players_[s1_].hero_);

        this.ais = new ArtificialPlayer[2];

        this.ais[0] = ai0;
        this.ais[1] = ai1;
	}
	
	public GameResult runGame() throws HSException {
		curTurn_ = 0;
		curPlayer_ = 0;

		//the first player draws 3 cards
		boardModel_.placeCard_hand_p0(players_[s0_].drawFromDeck(0));
		boardModel_.placeCard_hand_p0(players_[s0_].drawFromDeck(1));
		boardModel_.placeCard_hand_p0(players_[s0_].drawFromDeck(2));
		boardModel_.setDeckPos_p0(3);

		//the second player draws 4 cards
		boardModel_.placeCard_hand_p1(players_[s1_].drawFromDeck(0));
		boardModel_.placeCard_hand_p1(players_[s1_].drawFromDeck(1));
		boardModel_.placeCard_hand_p1(players_[s1_].drawFromDeck(2));
		boardModel_.placeCard_hand_p1(players_[s1_].drawFromDeck(3));
		boardModel_.placeCard_hand_p1(new TheCoin());
		boardModel_.setDeckPos_p1(4);
		
		GameRecord record = new GameSimpleRecord();
		record.put(0, s0_, (BoardModel) boardModel_.deepCopy());
		record.put(0, s1_, (BoardModel) boardModel_.flipPlayers().deepCopy());
				
		for (int i = 0; i < maxTurns_; ++i) {
            log.info("starting turn "+ i);
            long turnStart = System.currentTimeMillis();

            beginTurn(i, boardModel_, players_[s0_], players_[s1_]);

			if (!boardModel_.isAlive_p0()) {
				return new GameResult(s0_, s1_, i + 1, record);
			} else if (!boardModel_.isAlive_p1()) {
				return new GameResult(s0_, s0_, i + 1, record);
			}

			boardModel_ = playTurn(i, boardModel_, players_[s0_], players_[s1_], ais[s0_]);
			endTurn(i, boardModel_, players_[s0_], players_[s1_]);

			record.put(i + 1, s0_, (BoardModel) boardModel_.deepCopy());
			if (!boardModel_.isAlive_p0()) {
				return new GameResult(s0_, s1_, i + 1, record);
			} else if (!boardModel_.isAlive_p1()) {
				return new GameResult(s0_, s0_, i + 1, record);
			}

			boardModel_ = boardModel_.flipPlayers();

			beginTurn(i, boardModel_, players_[s1_], players_[s0_]);

			if (!boardModel_.isAlive_p0()) {
				return new GameResult(s0_, s0_, i + 1, record);
			} else if (!boardModel_.isAlive_p1()) {
				return new GameResult(s0_, s1_, i + 1, record);
			}

			boardModel_ = playTurn(i, boardModel_, players_[s1_], players_[s0_], ais[s1_]);
			endTurn(i, boardModel_, players_[s1_], players_[s0_]);
			record.put(i + 1, s1_, (BoardModel) boardModel_.deepCopy());

			if (!boardModel_.isAlive_p0()) {
				return new GameResult(s0_, s0_, i + 1, record);
			} else if (!boardModel_.isAlive_p1()) {
				return new GameResult(s0_, s1_, i + 1, record);
			}

			boardModel_ = boardModel_.flipPlayers();

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

    public void beginTurn(int turn, BoardModel board, PlayerModel playerModel0, PlayerModel playerModel1) throws HSException
    {
        board.startTurn(playerModel0.getDeck(), playerModel1.getDeck());

        Card newCard = playerModel0.drawFromDeck(board.getDeckPos_p0());
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

    public BoardModel playTurn(int turn, BoardModel board, PlayerModel playerModel0, PlayerModel playerModel1, ArtificialPlayer ai) throws HSException {
        return ai.playTurn(turn, board, playerModel0, playerModel1);
    }

    public void endTurn(int turn, BoardModel board, PlayerModel playerModel0, PlayerModel playerModel1) throws HSException {
        board.endTurn(playerModel0.getDeck(), playerModel1.getDeck());
    }
}
