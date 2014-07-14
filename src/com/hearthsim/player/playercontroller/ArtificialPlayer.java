package com.hearthsim.player.playercontroller;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.exception.HSException;
import com.hearthsim.exception.HSInvalidParamFileException;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.exception.HSParamNotFoundException;
import com.hearthsim.io.ParamFile;
import com.hearthsim.player.Player;
import com.hearthsim.util.BoardState;
import com.hearthsim.util.BoardStateFactory;
import com.hearthsim.util.HearthAction;
import com.hearthsim.util.HearthTreeNode;

public class ArtificialPlayer {
	
	int nLookahead_;
	
	double my_wAttack_; //weight for the attack score
	double my_wHealth_;
	double enemy_wAttack_; //weight for the attack score
	double enemy_wHealth_;
	
	double my_wHeroHealth_;
	double enemy_wHeroHealth_;
	
	double wTaunt_;
	double wMana_;
	
	double my_wNumMinions_;
	double enemy_wNumMinions_;
	
	double wSd_add_;
	double wSd_mult_;
	
	double my_wDivineShield_;
	double enemy_wDivineShield_;
	
	public ArtificialPlayer() {
		this(1.0, 1.0, 1.0, 1.0);
	}
	public ArtificialPlayer(double my_wAttack, double my_wHealth, double enemy_wAttack, double enemy_wHealth) {
		this(my_wAttack, my_wHealth, enemy_wAttack, enemy_wHealth, 0.0, 0.1, 0.1, 0.1, 0.5, 0.5, 0.0, 0.5, 0.0, 0.0);
	}
	
	public ArtificialPlayer(
			double my_wAttack,
			double my_wHealth,
			double enemy_wAttack,
			double enemy_wHealth,
			double wTaunt, 
			double my_wHeroHealth,
			double enemy_wHeroHealth,
			double wMana,
			double my_wNumMinions,
			double enemy_wNumMinions,
			double wSd_add,
			double wSd_mult,
			double my_wDivineShield,
			double enemy_wDivineShield) {
		nLookahead_ = 1;
		
		wMana_ = wMana;
		my_wAttack_ = my_wAttack;
		my_wHealth_ = my_wHealth;
		enemy_wAttack_ = enemy_wAttack;
		enemy_wHealth_ = enemy_wHealth;
		my_wHeroHealth_ = my_wHeroHealth;
		enemy_wHeroHealth_ = enemy_wHeroHealth;
		wTaunt_ = wTaunt;
		
		my_wNumMinions_ = my_wNumMinions;
		enemy_wNumMinions_ = enemy_wNumMinions;
		
		wSd_add_ = wSd_add;
		wSd_mult_ = wSd_mult;
		
		my_wDivineShield_ = my_wDivineShield;
		enemy_wDivineShield_ = enemy_wDivineShield;
	}
	
	public ArtificialPlayer(Path aiParamFile) throws IOException, HSInvalidParamFileException {
		ParamFile pFile = new ParamFile(aiParamFile);
		try {
			my_wAttack_ = pFile.getDouble("w_a");
			my_wHealth_ = pFile.getDouble("w_h");
			enemy_wAttack_ = pFile.getDouble("wt_a");
			enemy_wHealth_ = pFile.getDouble("wt_h");
			wTaunt_ = pFile.getDouble("w_taunt");
			my_wHeroHealth_ = pFile.getDouble("w_health");
			enemy_wHeroHealth_ = pFile.getDouble("wt_health");

			my_wNumMinions_ = pFile.getDouble("w_num_minions");
			enemy_wNumMinions_ = pFile.getDouble("wt_num_minions");;

			//The following two have default values for now... 
			//These are rather arcane parameters, so please understand 
			//them before attempting to change them. 
			wSd_add_ = pFile.getDouble("w_sd_mult", 1.0);
			wSd_mult_ = pFile.getDouble("w_sd_add", 0.9);
			
			//Divine Shield defualts to 0 for now
			my_wDivineShield_ = pFile.getDouble("w_divine_shield", 0.0);
			my_wDivineShield_ = pFile.getDouble("wt_divine_shield", 0.0);

		} catch (HSParamNotFoundException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}
	
	public double boardScore(BoardState board) {
				
		LinkedList<Minion> myBoardCards;
		LinkedList<Minion> opBoardCards;
		LinkedList<Card> myHandCards;
		myBoardCards = board.getMinions_p0();
		opBoardCards = board.getMinions_p1();
		myHandCards = board.getCards_hand_p0();
		
		//my score
		double myScore = 0.0;
		for (final Minion card: myBoardCards) {
			myScore += card.getAttack() * my_wAttack_ + card.getHealth() * my_wHealth_ + (card.getTaunt() ? 1.0 : 0.0) * wTaunt_ + (card.getAttack() + card.getHealth()) * my_wDivineShield_;
		}
		
		//opponent score
		double opScore = 0.0;
		for (final Minion card: opBoardCards) {
			opScore += card.getAttack() * enemy_wAttack_ + card.getHealth() * enemy_wHealth_ + (card.getTaunt() ? 1.0 : 0.0) * wTaunt_ + (card.getAttack() + card.getHealth()) * enemy_wDivineShield_;
		}
		
		//my cards.  The more cards that I have, the better
		double handScore = 0.0;
		for (final Card card: myHandCards) {
			if (card instanceof SpellDamage)
				handScore += ((SpellDamage)card).getAttack() * wSd_mult_ + wSd_add_;
			else
				handScore += card.getMana() * wMana_;
		}
		
		//the more we beat on the opponent hero, the better
		double heroScore = my_wHeroHealth_ * board.getHero_p0().getHealth() - enemy_wHeroHealth_ * board.getHero_p1().getHealth();
		
		if (board.getHero_p1().getHealth() <= 0) {
			//dead enemy hero is a very good thing
			heroScore += 100000.0;
		}
		
		if (board.getHero_p0().getHealth() <= 0) {
			//dead own hero is a very bad thing
			heroScore -= 100000000.0;
		}
		
		//the more minions you have, the better.  The less minions the enemy has, the better
		double minionScore = 0.0;
		minionScore += my_wNumMinions_ * (board.getNumMinions_p0());
		minionScore -= enemy_wNumMinions_ * (board.getNumMinions_p1());
		
		double score = myScore - opScore + handScore + heroScore + minionScore;
		
		return score;
	}
	
	public BoardState playTurn(int turn, BoardState board, Player player) throws HSException {
		//The goal of this ai is to maximize his board score
		double cur_score = boardScore(board);
		
		HearthTreeNode<BoardState> allMoves = playPossibilities(turn, board, player.getDeck());

		System.out.print("turn = " + turn + ", player = " + player.getName() + ", numHand = " + board.getNumCards_hand() + ", numMinion = " + board.getNumMinions_p0() + ", numEnemyMinion = " + board.getNumMinions_p1());
		System.out.flush();
		
		BoardState bestPlay = null;
		double bestScore = -1000000.0;
		for (final HearthTreeNode<BoardState> node : allMoves.getAllLeaves()) {
			double score = this.boardScore(node.data_);
			if (score > bestScore) {
				bestPlay = node.data_;
				bestScore = score;
			}
		}

		System.out.println(", number of nodes = " + allMoves.getAllLeaves().size() + ", playerHealth = " + bestPlay.getHero_p0().getHealth() + ", rMinion = " + bestPlay.getNumMinions_p0() + ", eMinion = " + bestPlay.getNumMinions_p1());

		return bestPlay;
	}

	public HearthTreeNode<BoardState> playPossibilities(int turn, BoardState board, Deck deck) throws HSInvalidPlayerIndexException {

		HearthTreeNode<BoardState> toRet = new HearthTreeNode<BoardState>(board);
		BoardStateFactory factory = new BoardStateFactory(deck);
		toRet = factory.doMoves(toRet);
		return toRet;
	}
	
	public void writeOut(String filename, Object node) {
		Writer writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "utf-8"));
			writer.write(node.toString());
		} catch (IOException e) { 
			System.err.println("Exception: " + e.getMessage());
		} finally {
			try {writer.close();} catch (Exception e) {}
		}
	}

}
