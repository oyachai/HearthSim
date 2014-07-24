package com.hearthsim.player.playercontroller;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.util.LinkedList;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.exception.HSException;
import com.hearthsim.exception.HSInvalidParamFileException;
import com.hearthsim.exception.HSParamNotFoundException;
import com.hearthsim.io.ParamFile;
import com.hearthsim.player.Player;
import com.hearthsim.util.BoardState;
import com.hearthsim.util.BoardStateFactory;
import com.hearthsim.util.tree.HearthTreeNode;
import com.hearthsim.util.tree.StopNode;

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
	
	double my_wWeapon_;
	double enemy_wWeapon_;
	
	double my_wCharge_;
	double enemy_wCharge_;
	
	public ArtificialPlayer() {
		this(1.0, 1.0, 1.0, 1.0);
	}
	public ArtificialPlayer(double my_wAttack, double my_wHealth, double enemy_wAttack, double enemy_wHealth) {
		this(my_wAttack, my_wHealth, enemy_wAttack, enemy_wHealth, 0.0, 0.1, 0.1, 0.1, 0.5, 0.5, 0.0, 0.5, 0.0, 0.0);
	}
	
	/**
	 * Constructor - Deprecated, do not use!
	 * 
	 * @param my_wAttack
	 * @param my_wHealth
	 * @param enemy_wAttack
	 * @param enemy_wHealth
	 * @param wTaunt
	 * @param my_wHeroHealth
	 * @param enemy_wHeroHealth
	 * @param wMana
	 * @param my_wNumMinions
	 * @param enemy_wNumMinions
	 * @param wSd_add
	 * @param wSd_mult
	 * @param my_wDivineShield
	 * @param enemy_wDivineShield
	 */
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
		
		
		System.err.println("BIG FAT WARNING: This constructor is deprecated!");
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
		
		my_wWeapon_ = 0.5;
		enemy_wWeapon_ = 0.5;
	}
	
	/**
	 * Constructor
	 * 
	 * This is the preferred (non-deprecated) way to instantiate this class
	 * 
	 * @param aiParamFile The path to the input parameter file
	 * @throws IOException
	 * @throws HSInvalidParamFileException
	 */
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
			enemy_wDivineShield_ = pFile.getDouble("wt_divine_shield", 0.0);
			
			//weapon score for the hero
			my_wWeapon_ = pFile.getDouble("w_weapon", 0.5);
			enemy_wWeapon_ = pFile.getDouble("wt_weapon", 0.5);
			
			//charge model score
			my_wCharge_ = pFile.getDouble("w_charge", 0.0);
			enemy_wCharge_ = pFile.getDouble("wt_charge", 0.0);

		} catch (HSParamNotFoundException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}
	
	
	/**
	 * Returns the card score for a particular card assuming that it is in the hand
	 * 
	 * @param card
	 * @return
	 */
	public double cardInHandScore(Card card) {
		double theScore = 0.0;
		if (card instanceof SpellDamage)
			theScore += ((SpellDamage)card).getAttack() * wSd_mult_ + wSd_add_;
		else if (card instanceof Minion) {
			//Charge modeling.  Charge's value primarily comes from the fact that it can be used immediately upon placing it.
			//After the card is placed, it's really just like any other minion, except maybe for small value in bouncing it.
			//So, the additional score for charge minions should really only apply when it is still in the hand.
			Minion minion = (Minion)card;
			theScore += card.getMana() * wMana_ + (minion.getCharge() ? my_wCharge_ : 0.0);
		} else
			theScore += card.getMana() * wMana_;
		return theScore;
	}
	
	public double heroHealthScore_p0(double heroHealth, double heroArmor) {
		double toRet = my_wHeroHealth_ * (heroHealth + heroArmor);
		if (heroHealth <= 0) {
			//dead enemy hero is a very good thing
			toRet -= 100000000.0;
		}
		return toRet;
	}
	
	public double heroHealthScore_p1(double heroHealth, double heroArmor) {
		double toRet = -enemy_wHeroHealth_ * (heroHealth + heroArmor);
		if (heroHealth <= 0) {
			//dead enemy hero is a very good thing
			toRet += 100000.0;
		}
		return toRet;
	}
	
	/**
	 * Board score function
	 * 
	 * The all important board score function.  It is a function that measures how 'good' the given board is. 
	 * As a convention, this function should be an increasing function of the board's goodness.
	 * 
	 * @param board The current board state
	 * @return
	 */
	public double boardScore(BoardState board) {
				
		LinkedList<Minion> myBoardCards;
		LinkedList<Minion> opBoardCards;
		LinkedList<Card> myHandCards;
		myBoardCards = board.getMinions_p0();
		opBoardCards = board.getMinions_p1();
		myHandCards = board.getCards_hand_p0();
		
		//my board score
		double myScore = 0.0;
		for (final Minion minion: myBoardCards) {
			myScore += minion.getAttack() * my_wAttack_;
			myScore += minion.getHealth() * my_wHealth_;
			myScore += (minion.getTaunt() ? 1.0 : 0.0) * wTaunt_;
			if (minion.getDivineShield()) myScore += ((minion.getAttack() + minion.getHealth()) * my_wDivineShield_);
		}
				
		//opponent board score
		double opScore = 0.0;
		for (final Minion minion: opBoardCards) {
			opScore += minion.getAttack() * enemy_wAttack_;
			opScore += minion.getHealth() * enemy_wHealth_;
			opScore += (minion.getTaunt() ? 1.0 : 0.0) * wTaunt_;
			if (minion.getDivineShield()) opScore += (minion.getAttack() + minion.getHealth()) * enemy_wDivineShield_;
		}
		
		//weapons
		double weaponScore = 0.0;
		weaponScore += board.getHero_p0().getAttack() * board.getHero_p0().getWeaponCharge() * my_wWeapon_;
		weaponScore -= board.getHero_p1().getAttack() * board.getHero_p1().getWeaponCharge() * enemy_wWeapon_;
		
		//my cards.  The more cards that I have, the better
		double handScore = 0.0;
		for (final Card card: myHandCards) {
			handScore += this.cardInHandScore(card);
		}
		
		//the more we beat on the opponent hero, the better
		double heroScore = 0;
		heroScore += heroHealthScore_p0(board.getHero_p0().getHealth(), board.getHero_p0().getArmor());
		heroScore += heroHealthScore_p1(board.getHero_p1().getHealth(), board.getHero_p1().getArmor());
		
		//the more minions you have, the better.  The less minions the enemy has, the better
		double minionScore = 0.0;
		minionScore += my_wNumMinions_ * (board.getNumMinions_p0());
		minionScore -= enemy_wNumMinions_ * (board.getNumMinions_p1());
		
		double score = myScore - opScore + handScore + heroScore + minionScore + weaponScore;
		
		return score;
	}
	
	/**
	 * Play a turn
	 * 
	 * This function is called by GameMaster, and it should return a BoardState resulting from the AI playing its turn.
	 * 
	 * @param turn Turn number, 1-based
	 * @param board The board state at the beginning of the turn (after all card draws and minion deaths)
	 * @param player The player playing the turn
	 * @return
	 * @throws HSException
	 */
	public BoardState playTurn(int turn, BoardState board, Player player0, Player player1) throws HSException {
		//The goal of this ai is to maximize his board score
		HearthTreeNode toRet = new HearthTreeNode(board);
		BoardStateFactory factory = new BoardStateFactory(player0.getDeck(), player1.getDeck());
		HearthTreeNode allMoves = factory.doMoves(toRet, this);

//		System.out.print("turn = " + turn + ", p = " + player0.getName() + ", nHand = " + board.getNumCards_hand() + ", nMinion = " + board.getNumMinions_p0() + ", nEnemyMinion = " + board.getNumMinions_p1());
//		System.out.flush();
		
		HearthTreeNode bestPlay = allMoves.findMaxOfFunc(this);
		while( bestPlay instanceof StopNode ) {
			HearthTreeNode allEffectsDone = ((StopNode)bestPlay).finishAllEffects(player0.getDeck());
			factory.resetTimeOut();
			HearthTreeNode allMovesAtferStopNode = factory.doMoves(allEffectsDone, this);
			bestPlay = allMovesAtferStopNode.findMaxOfFunc(this);
		}

//		System.out.print(", number of nodes = " + allMoves.numLeaves() + ", playerHealth = " + bestPlay.data_.getHero_p0().getHealth() + ", rMinion = " + bestPlay.data_.getNumMinions_p0() + ", eMinion = " + bestPlay.data_.getNumMinions_p1());
//		if (factory.didTimeOut())
//			System.out.print(", tO");
//		System.out.println();
		
		return bestPlay.data_;
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
