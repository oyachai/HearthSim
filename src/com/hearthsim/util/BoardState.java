package com.hearthsim.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSInvalidPlayerIndexException;



public class BoardState implements DeepCopyable {
	
	LinkedList<Minion> p0_minions_;
	LinkedList<Minion> p1_minions_;
	
	LinkedList<Card> p0_hand_;
	LinkedList<Card> p1_hand_;
	
	Hero p0_hero_;
	Hero p1_hero_;
	
	int p0_mana_;
	int p1_mana_;
	int p0_maxMana_;
	int p1_maxMana_;
	
	int p0_deckPos_;
	int p1_deckPos_;
	byte p0_fatigueDamage_;
	byte p1_fatigueDamage_;
	
	public BoardState() {
		p0_minions_ = new LinkedList<Minion>();
		p1_minions_ = new LinkedList<Minion>();
		p0_hand_ = new LinkedList<Card>();
		p1_hand_ = new LinkedList<Card>();
		p0_mana_ = 0;
		p1_mana_ = 0;
		p0_maxMana_ = 0;
		p1_maxMana_ = 0;
		p0_deckPos_ = 0;
		p1_deckPos_ = 0;
		p0_fatigueDamage_ = 1;
		p1_fatigueDamage_ = 1;
		
		p0_hero_ = new Hero("hero0", (byte)30, (byte)30);
		p1_hero_ = new Hero("hero1", (byte)30, (byte)30);
	}
	
	public LinkedList<Minion> getMinions_p0() {
		return p0_minions_;
	}
	
	public LinkedList<Minion> getMinions_p1() {
		return p1_minions_;
	}
	
	public LinkedList<Card> getCards_hand_p0() {
		return p0_hand_;
	}

	public LinkedList<Card> getCards_hand_p1() {
		return p1_hand_;
	}
	
	public Minion getMinion_p0(int index) {
		return p0_minions_.get(index);
	}
	
	public Minion getMinion_p1(int index) {
		return p1_minions_.get(index);
	}
	
	public Card getCard_hand_p0(int index) {
		return p0_hand_.get(index);
	}
	
	public Card getCard_hand_p1(int index) {
		return p1_hand_.get(index);
	}

	public void setMinions_p0(LinkedList<Minion> minions) {
		p0_minions_ = minions;
	}
	
	public void setMinions_p1(LinkedList<Minion> minions) {
		p1_minions_ = minions;
	}
	
	public void setCards_hand_p0(LinkedList<Card> cards) {
		p0_hand_ = cards;
	}
	
	public void setCards_hand_p1(LinkedList<Card> cards) {
		p1_hand_ = cards;
	}

	public void setMinion_p0(Minion card, int index) {
		p0_minions_.set(index, card);
	}
	
	public void setMinion_p1(Minion card, int index) {
		p1_minions_.set(index, card);
	}
	
	public void setCard_hand(Card card, int index) {
		p0_hand_.set(index, card);
	}
	
	public Iterator<Minion> getIterator_p0() {
		return p0_minions_.iterator();
	}
	
	public Iterator<Minion> getIterator_p1() {
		return p1_minions_.iterator();
	}
	
	public Iterator<Card> getIterator_hand() {
		return p0_hand_.iterator();
	}
	
	public void placeMinion_p0(Minion card, int position) {
		card.isInHand(false);
		p0_minions_.add(position, card);
	}
	
	public void placeMinion_p1(Minion card, int position) {
		card.isInHand(false);
		p1_minions_.add(position, card);
	}
	
	public void placeMinion_p0(Minion card) {
		card.isInHand(false);
		p0_minions_.add(card);
	}
	
	public void placeMinion_p1(Minion card) {
		card.isInHand(false);
		p1_minions_.add(card);
	}
	
	public void placeCard_hand(int playerIndex, Card card) throws HSInvalidPlayerIndexException {
		card.isInHand(true);
		if (playerIndex == 0)
			p0_hand_.add(card);
		else if (playerIndex == 1)
			p1_hand_.add(card);
		else
			throw new HSInvalidPlayerIndexException();
	}
	
	public void placeCard_hand_p0(Card card) {
		card.isInHand(true);
		p0_hand_.add(card);
	}

	public void placeCard_hand_p1(Card card) {
		card.isInHand(true);
		p1_hand_.add(card);
	}
	
	public void removeCard_hand(int index) {
		p0_hand_.remove(index);
	}
	
	public int getNumMinions_p0() {
		return p0_minions_.size();
	}
	
	public int getNumMinions_p1() {
		return p1_minions_.size();
	}
	
	public int getNumCards_hand() {
		return p0_hand_.size();
	}
	
	public Hero getHero(int playerIndex) throws HSInvalidPlayerIndexException {
		if (playerIndex == 0)
			return p0_hero_;
		else if (playerIndex == 1)
			return p1_hero_;
		else
			throw new HSInvalidPlayerIndexException();
	}
	
	public Hero getHero_p0() {
		return p0_hero_;
	}
		
	public Hero getHero_p1() {
		return p1_hero_;
	}
	
	public void setHero_p0(Hero hero) {
		p0_hero_ = hero;
	}
	
	public void setHero_p1(Hero hero) {
		p1_hero_ = hero;
	}

	public int getMana_p0() {
		return p0_mana_;
	}
	
	public void setMana_p0(int mana) {
		p0_mana_ = mana;
	}
	
	public int getMana_p1() {
		return p1_mana_;
	}
	
	public void setMana_p1(int mana) {
		p1_mana_ = mana;
	}

	public int getMaxMana_p0() {
		return p0_maxMana_;
	}
	
	public void setMaxMana_p0(int mana) {
		p0_maxMana_ = mana;
	}
	
	public int getMaxMana_p1() {
		return p1_maxMana_;
	}
	
	public void setMaxMana_p1(int mana) {
		p1_maxMana_ = mana;
	}
	
	public void addMaxMana_p0(int mana) {
		p0_maxMana_ += mana;
	}
	
	public void addMaxMana_p1(int mana) {
		p1_maxMana_ += mana;
	}
	
	/**
	 * Index of the next card in deck
	 * 
	 * Returns the index of the next card to draw from the deck.
	 * @return
	 */
	public int getDeckPos(int playerIndex) throws HSInvalidPlayerIndexException {
		if (playerIndex == 0)
			return p0_deckPos_;
		else if (playerIndex == 1)
			return p1_deckPos_;
		else
			throw new HSInvalidPlayerIndexException();
	}
	
	public void setDeckPos(int playerIndex, int position) throws HSInvalidPlayerIndexException {
		if (playerIndex == 0)
			p0_deckPos_ = position;
		else if (playerIndex == 1)
			p1_deckPos_ = position;
		else
			throw new HSInvalidPlayerIndexException();
	}
	
	/**
	 * Index of the next card in deck
	 * 
	 * Returns the index of the next card to draw from the deck.
	 * @return
	 */
	public int getDeckPos_p0() {
		return p0_deckPos_;
	}
	
	public void setDeckPos_p0(int position) {
		p0_deckPos_ = position;
	}
	
	/**
	 * Index of the next card in deck
	 * 
	 * Returns the index of the next card to draw from the deck.
	 * @return
	 */
	public int getDeckPos_p1() {
		return p1_deckPos_;
	}
	
	public void setDeckPos_p1(int position) {
		p1_deckPos_ = position;
	}

	/**
	 * Get the fatigue damage
	 * 
	 * Returns the fatigue damage taken when a card draw fails next.
	 * @return
	 */
	public byte getFatigueDamage(int playerIndex) throws HSInvalidPlayerIndexException {
		if (playerIndex == 0)
			return p0_fatigueDamage_;
		else if (playerIndex == 1) 
			return p1_fatigueDamage_;
		else
			throw new HSInvalidPlayerIndexException();
	}
	
	public void setFatigueDamage(int playerIndex, byte damage) throws HSInvalidPlayerIndexException {
		if (playerIndex == 0)
			p0_fatigueDamage_ = damage;
		else if (playerIndex == 1)
			p1_fatigueDamage_ = damage;
		else
			throw new HSInvalidPlayerIndexException();
	}
	
	/**
	 * Get the fatigue damage for player 0
	 * 
	 * Returns the fatigue damage taken when a card draw fails next.
	 * @return
	 */
	public byte getFatigueDamage_p0() {
		return p0_fatigueDamage_;
	}
	
	public void setFatigueDamage_p0(byte damage) {
		p0_fatigueDamage_ = damage;
	}
	
	/**
	 * Get the fatigue damage for player 1
	 * 
	 * Returns the fatigue damage taken when a card draw fails next.
	 * @return
	 */
	public byte getFatigueDamage_p1() {
		return p1_fatigueDamage_;
	}
	
	public void setFatigueDamage_p1(byte damage) {
		p1_fatigueDamage_ = damage;
	}
	
	
	//various ways to remove cards / minions
	public Minion removeMinion_p0(Minion minion) {
		boolean success = p0_minions_.remove(minion);
		if (success) {
			return minion;
		} else {
			return null;
		}
	}
	
	public Minion removeMinion_p1(Minion minion) {
		boolean success = p1_minions_.remove(minion);
		if (success) {
			return minion;
		} else {
			return null;
		}
	}
	
	public Minion removeMinion_p0(int index) {
		Minion minion = null;
		try {
			minion = p0_minions_.remove(index);
		} catch (IndexOutOfBoundsException e) {
			
		}
		return minion;
	}
	
	public Minion removeMinion_p1(int index) {
		Minion minion = null;
		try {
			minion = p1_minions_.remove(index);
		} catch (IndexOutOfBoundsException e) {
			
		}
		return minion;
	}
	
	public boolean isAlive_p0() {
		return p0_hero_.getHealth() > 0;
	}
	
	public boolean isAlive_p1() {
		return p1_hero_.getHealth() > 0;
	}
	
	public ArrayList<Integer> getAttackableMinions_p1() {
		ArrayList<Integer> toRet = new ArrayList<Integer>();
		boolean hasTaunt = false;
		for (final Minion minion : p1_minions_) {
			hasTaunt = hasTaunt || minion.getTaunt();
		}
		if (!hasTaunt) {
			toRet.add((Integer)0);
			int counter = 1;
			for (final Minion minion : p1_minions_) {
				toRet.add((Integer)counter);
				counter++;
			}
			return toRet;
		} else {
			int counter = 1;
			for (final Minion minion : p1_minions_) {
				if (minion.getTaunt())
					toRet.add((Integer)counter);
				counter++;
			}
			return toRet;
		}
	}
	
	public void resetMana() {
		p0_mana_ = p0_maxMana_;
		p1_mana_ = p1_maxMana_;
	}
	
	public boolean equals(Object other)
	{
	   if (other == null)
	   {
	      return false;
	   }

	   if (this.getClass() != other.getClass())
	   {
	      return false;
	   }
	   
	   if (p0_mana_ != ((BoardState)other).p0_mana_)
		   return false;
	   if (p1_mana_ != ((BoardState)other).p1_mana_)
		   return false;
	   if (p0_maxMana_ != ((BoardState)other).p0_maxMana_)
		   return false;
	   if (p1_maxMana_ != ((BoardState)other).p1_maxMana_)
		   return false;
	   
	   if (!p0_hero_.equals(((BoardState)other).p0_hero_)) {
		   return false;
	   }

	   if (!p1_hero_.equals(((BoardState)other).p1_hero_)) {
		   return false;
	   }
	   
	   if (p0_deckPos_ != ((BoardState)other).p0_deckPos_)
		   return false;

	   if (p1_deckPos_ != ((BoardState)other).p1_deckPos_)
		   return false;

	   if (p0_fatigueDamage_ != ((BoardState)other).p0_fatigueDamage_)
		   return false;
	   
	   if (p1_fatigueDamage_ != ((BoardState)other).p1_fatigueDamage_)
		   return false;
	   
	   if (p0_minions_.size() != ((BoardState)other).p0_minions_.size()) 
		   return false;
	   if (p1_minions_.size() != ((BoardState)other).p1_minions_.size()) 
		   return false;
	   if (p0_hand_.size() != ((BoardState)other).p0_hand_.size()) 
		   return false;

	   for (int i = 0; i < p0_minions_.size(); ++i) {
		   if (!p0_minions_.get(i).equals(((BoardState)other).p0_minions_.get(i))) {
			   return false;
		   }
	   }

	   for (int i = 0; i < p1_minions_.size(); ++i) {
		   if (!p1_minions_.get(i).equals(((BoardState)other).p1_minions_.get(i))) {
			   return false;
		   }
	   }

	   for (int i = 0; i < p0_hand_.size(); ++i) {
		   if (!p0_hand_.get(i).equals(((BoardState)other).p0_hand_.get(i))) {
			   return false;
		   }
	   }
	   
	   for (int i = 0; i < p1_hand_.size(); ++i) {
		   if (!p1_hand_.get(i).equals(((BoardState)other).p1_hand_.get(i))) {
			   return false;
		   }
	   }

	   // More logic here to be discuss below...
	   return true;
	}
	
	@Override
	public int hashCode() {
		int hs = p0_hand_.size();
		if (hs < 0) hs = 0;
		int res = hs + p0_minions_.size() * 10 + p1_minions_.size() * 100;
		res += (p0_mana_ <= 0 ? 0 : (p0_mana_ - 1) * 1000); 
		res += ((p0_hero_.getHealth() + p1_hero_.getHealth()) % 100) * 10000;
		int th = 0;
		if (hs > 0) {
			Card cc = p0_hand_.get(0);
			try {
				Minion mm = (Minion)cc;
				th += ((cc.hasBeenUsed() ? 1 : 0) + mm.getAttack() + mm.getHealth() + cc.getMana());
			} catch (ClassCastException e) {
				th += ((cc.hasBeenUsed() ? 1 : 0) + cc.getMana());
			}
		}
		if (hs > 1) {
			Card cc = p0_hand_.get(1);
			try {
				Minion mm = (Minion)cc;
				th += ((cc.hasBeenUsed() ? 1 : 0) + mm.getAttack() + mm.getHealth() + cc.getMana());
			} catch (ClassCastException e) {
				th += ((cc.hasBeenUsed() ? 1 : 0) + cc.getMana());				
			}
		}
		res += (th % 10) * 1000000;
		int mh0 = 0;
		if (p0_minions_.size() > 0) {
			mh0 += (p0_minions_.get(0).getHealth());
		}
		if (p0_minions_.size() > 1) {
			mh0 += (p0_minions_.get(1).getHealth());
		}
		res += (mh0 % 100) * 10000000;
		int mh1 = 0;
		if (p1_minions_.size() > 0) {
			mh1 += p1_minions_.get(0).getHealth();
		}
		if (p1_minions_.size() > 1) {
			mh1 += p1_minions_.get(1).getHealth();
		}
		res += (mh1 % 20) * 100000000;
		return res;
	}
	
	/**
	 * Reset all minions to clear their has_attacked state.
	 * 
	 * Call this function at the beginning of each turn
	 * 
	 */
	public void resetMinions() {
		for (Minion minion : p0_minions_) {
			minion.hasAttacked(false);
			minion.hasBeenUsed(false);
		}
		for (Minion minion : p1_minions_) {
			minion.hasAttacked(false);
			minion.hasBeenUsed(false);
		}
	}
	
	/**
	 * Reset the has_been_used state of the cards in hand
	 */
	public void resetHand() {
		for(Card card : p0_hand_) {
			card.hasBeenUsed(false);
		}
	}
	
	public BoardState flipPlayers() { 
		BoardState newState = (BoardState)this.deepCopy();
		LinkedList<Minion> p0_minions = newState.getMinions_p0();
		LinkedList<Minion> p1_minions = newState.getMinions_p1();
		int p0_mana = newState.getMana_p0();
		int p1_mana = newState.getMana_p1();
		int p0_maxMana = newState.getMaxMana_p0();
		int p1_maxMana = newState.getMaxMana_p1();

		newState.p0_hero_ = p1_hero_;
		newState.p1_hero_ = p0_hero_;
		newState.setMinions_p0(p1_minions);
		newState.setMinions_p1(p0_minions);
		newState.setMana_p0(p1_mana);
		newState.setMana_p1(p0_mana);
		newState.setMaxMana_p0(p1_maxMana);
		newState.setMaxMana_p1(p0_maxMana);
		newState.p0_hand_ = p1_hand_;
		newState.p1_hand_ = p0_hand_;
		newState.p0_deckPos_ = p1_deckPos_;
		newState.p1_deckPos_ = p0_deckPos_;
		newState.p0_fatigueDamage_ = p1_fatigueDamage_;
		newState.p1_fatigueDamage_ = p0_fatigueDamage_;
		return newState;
	}
	
	public Object deepCopy() {
		BoardState newBoard = new BoardState();
		for (final Minion card: p0_minions_) {
			Minion tc = (Minion)card.deepCopy();
			newBoard.placeMinion_p0(tc);
		}
		for (final Minion card: p1_minions_) {
			Minion tc = (Minion)card.deepCopy();
			newBoard.placeMinion_p1(tc);
		}
		for (final Card card: p0_hand_) {
			Card tc = (Card)card.deepCopy();
			newBoard.placeCard_hand_p0(tc);
		}
		for (final Card card: p1_hand_) {
			Card tc = (Card)card.deepCopy();
			newBoard.placeCard_hand_p1(tc);
		}
		
		newBoard.setHero_p0((Hero)this.p0_hero_.deepCopy());
		newBoard.setHero_p1((Hero)this.p1_hero_.deepCopy());
		
		newBoard.p0_deckPos_ = this.p0_deckPos_;
		newBoard.p1_deckPos_ = this.p1_deckPos_;
		newBoard.p0_fatigueDamage_ = this.p0_fatigueDamage_;
		newBoard.p1_fatigueDamage_ = this.p1_fatigueDamage_;
		newBoard.p0_mana_ = this.p0_mana_;
		newBoard.p1_mana_ = this.p1_mana_;
		newBoard.p0_maxMana_ = this.p0_maxMana_;
		newBoard.p1_maxMana_ = this.p1_maxMana_;
		
		return newBoard;
	}
	
	public String toString() {
		String toRet = "{";
		toRet = toRet + "\"p0_minions\": " + p0_minions_ + ", ";
		toRet = toRet + "\"p1_minions\": " + p1_minions_ + ", ";
		toRet = toRet + "\"p0_hand\": " + p0_hand_ + ", ";
		toRet = toRet + "\"p0_hero\": " + p0_hero_ + ", ";
		toRet = toRet + "\"p1_hero\": " + p1_hero_ + ", ";
		toRet = toRet + "\"p0_mana\": " + p0_mana_ + ", ";
		toRet = toRet + "\"p1_mana\": " + p1_mana_ + ", ";
		toRet = toRet + "\"p0_maxMana\": " + p0_maxMana_ + ", ";
		toRet = toRet + "\"p1_maxMana\": " + p1_maxMana_ + "";
		toRet = toRet + "}";
		return toRet;
	}
	
}