package com.hearthsim.util;

import java.util.ArrayList;
import java.util.Iterator;

import sun.awt.util.IdentityLinkedList;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.json.JSONArray;
import com.json.JSONObject;


/**
 * A class that represents the current state of the board (game)
 *
 */
public class BoardState implements DeepCopyable {
		
	MinionList p0_minions_;
	MinionList p1_minions_;
	
	IdentityLinkedList<Card> p0_hand_;
	IdentityLinkedList<Card> p1_hand_;
	
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
	
	byte p0_spellDamage_;
	byte p1_spellDamage_;
	
	public BoardState() {
		this(new Hero("hero0", (byte)30), new Hero("hero1", (byte)30));
	}
	
	public BoardState(Hero p0_hero, Hero p1_hero) {
		p0_minions_ = new MinionList();
		p1_minions_ = new MinionList();
		p0_hand_ = new IdentityLinkedList<Card>();
		p1_hand_ = new IdentityLinkedList<Card>();
		p0_mana_ = 0;
		p1_mana_ = 0;
		p0_maxMana_ = 0;
		p1_maxMana_ = 0;
		p0_deckPos_ = 0;
		p1_deckPos_ = 0;
		p0_fatigueDamage_ = 1;
		p1_fatigueDamage_ = 1;
		
		p0_hero_ = p0_hero;
		p1_hero_ = p1_hero;		
	}
	
	public MinionList getMinions(int playerIndex) throws HSInvalidPlayerIndexException {
		if (playerIndex == 0)
			return p0_minions_;
		else if (playerIndex == 1)
			return p1_minions_;
		else
			throw new HSInvalidPlayerIndexException();
	}

	public MinionList getMinions_p0() {
		return p0_minions_;
	}
	
	public MinionList getMinions_p1() {
		return p1_minions_;
	}
	
	public IdentityLinkedList<Card> getCards_hand_p0() {
		return p0_hand_;
	}

	public IdentityLinkedList<Card> getCards_hand_p1() {
		return p1_hand_;
	}
	
	public Minion getMinion(int playerIndex, int index) throws HSInvalidPlayerIndexException {
		if (playerIndex == 0)
			return p0_minions_.get(index);
		else if (playerIndex == 1)
			return p1_minions_.get(index);
		else
			throw new HSInvalidPlayerIndexException();
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

	public void setMinions_p0(MinionList minions) {
		p0_minions_ = minions;
	}
	
	public void setMinions_p1(MinionList minions) {
		p1_minions_ = minions;
	}
	
	public void setCards_hand_p0(IdentityLinkedList<Card> cards) {
		p0_hand_ = cards;
	}
	
	public void setCards_hand_p1(IdentityLinkedList<Card> cards) {
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
	
	
	public Minion getCharacter(int playerIndex, int index) throws HSInvalidPlayerIndexException {
		if (playerIndex == 0)
			return index == 0 ? p0_hero_ : p0_minions_.get(index - 1);
		else if (playerIndex == 1)
			return index == 0 ? p1_hero_ : p1_minions_.get(index - 1);
		else
			throw new HSInvalidPlayerIndexException();
	}
	
	public Minion getCharacter_p0(int index) {
		return index == 0 ? p0_hero_ : p0_minions_.get(index - 1);
	}

	public Minion getCharacter_p1(int index) {
		return index == 0 ? p1_hero_ : p1_minions_.get(index - 1);
	}

	//-----------------------------------------------------------------------------------
	// Various ways to put a minion onto board
	//-----------------------------------------------------------------------------------
	/**
	 * Place a minion onto the board.  Does not trigger any events.
	 * 
	 * This is a function to place a minion on the board.  Use this function only if you
	 * want no events to be trigger upon placing the minion.
	 *
	 * @param minion The minion to be placed on the board.
	 * @param position The position to place the minion.  The new minion goes to the "left" (lower index) of the postinion index.
	 * @throws HSInvalidPlayerIndexException 
	 */
	public void placeMinion(int playerIndex, Minion minion, int position) throws HSInvalidPlayerIndexException {
		if (playerIndex == 0)
			p0_minions_.add(position, minion);
		else if (playerIndex == 1) 
			p1_minions_.add(position, minion);
		else
			throw new HSInvalidPlayerIndexException();
		minion.isInHand(false);
	}

	
	/**
	 * Place a minion onto the board.  Does not trigger any events.
	 * 
	 * This is a function to place a minion on the board.  Use this function only if you
	 * want no events to be trigger upon placing the minion.
	 *
	 * @param minion The minion to be placed on the board.  The minion is placed on the right-most space.
	 * @throws HSInvalidPlayerIndexException 
	 */
	public void placeMinion(int playerIndex, Minion minion) throws HSInvalidPlayerIndexException {
		minion.isInHand(false);
		if (playerIndex == 0)
			p0_minions_.add(minion);
		else if (playerIndex == 1)
			p1_minions_.add(minion);
		else
			throw new HSInvalidPlayerIndexException();
	}

	//-----------------------------------------------------------------------------------
	//-----------------------------------------------------------------------------------

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
	
	public void removeCard_hand(Card card) {
		p0_hand_.remove(card);
	}

	public int getNumMinions(int playerIndex) throws HSInvalidPlayerIndexException {
		if (playerIndex == 0)
			return p0_minions_.size();
		else if (playerIndex == 1)
			return p1_minions_.size();
		else
			throw new HSInvalidPlayerIndexException();
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
	
	public int getNumCards_hand_p0() {
		return p0_hand_.size();
	}

	public int getNumCards_hand_p1() {
		return p1_hand_.size();
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

	public int getMana(int playerIndex) throws HSInvalidPlayerIndexException {
		if (playerIndex == 0)
			return p0_mana_;
		else if (playerIndex == 1) 
			return p1_mana_;
		else
			throw new HSInvalidPlayerIndexException();
	}
	
	public void setMana(int playerIndex, int mana) throws HSInvalidPlayerIndexException {
		if (playerIndex == 0)
			p0_mana_ = mana;
		else if (playerIndex == 1) 
			p1_mana_ = mana;
		else
			throw new HSInvalidPlayerIndexException();
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
	
	public void addMana(int playerIndex, int mana) throws HSInvalidPlayerIndexException {
		if (playerIndex == 0)
			p0_mana_ += mana;
		else if (playerIndex == 1) 
			p1_mana_ += mana;
		else
			throw new HSInvalidPlayerIndexException();
	}
	
	public void addMana_p0(int mana) {
		p0_mana_ += mana;
	}

	public void addMana_p1(int mana) {
		p1_mana_ += mana;
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
	 * Draw a card from a deck and place it in the hand
	 * 
	 * This function is intentionally only implemented for Player1.  
	 * For Player0, it is almost always correct to user CardDrawNode instead.
	 * 
	 * @param deck Deck from which to draw.
	 * @param numCards Number of cards to draw.
	 * @throws HSInvalidPlayerIndexException
	 */
	public void drawCardFromDeck_p1(Deck deck, int numCards) throws HSInvalidPlayerIndexException {
		//This minion is an enemy minion.  Let's draw a card for the enemy.  No need to use a StopNode for enemy card draws.
		for (int indx = 0; indx < numCards; ++indx) {
			Card card = deck.drawCard(this.getDeckPos(1));
			if (card == null) {
				byte fatigueDamage = this.getFatigueDamage(1);
				this.setFatigueDamage(1, (byte)(fatigueDamage + 1));
				this.getHero(1).setHealth((byte)(this.getHero(1).getHealth() - fatigueDamage));
			} else {
				if (this.getNumCards_hand_p1() < 10) {
					this.placeCard_hand(1, card);
				}
				this.setDeckPos(1, this.getDeckPos(1) + 1);
			}
		}
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
	
	/**
	 * Get the spell damage
	 * 
	 * Returns the additional spell damage provided by buffs
	 * @return
	 */
	public byte getSpellDamage(int playerIndex) throws HSInvalidPlayerIndexException {
		if (playerIndex == 0)
			return p0_spellDamage_;
		else if (playerIndex == 1) 
			return p1_spellDamage_;
		else
			throw new HSInvalidPlayerIndexException();
	}
	
	/**
	 * Set the spell damage
	 * 
	 * Returns the additional spell damage provided by buffs
	 * @return
	 */
	public void setSpellDamage(int playerIndex, byte damage) throws HSInvalidPlayerIndexException {
		if (playerIndex == 0)
			p0_spellDamage_ = damage;
		else if (playerIndex == 1)
			p1_spellDamage_ = damage;
		else
			throw new HSInvalidPlayerIndexException();
	}

	public Minion removeMinion(int playerIndex, int index) throws HSInvalidPlayerIndexException {
		Minion minion = null;
		try {
			if (playerIndex == 0)
				minion = p0_minions_.remove(index);
			else if (playerIndex == 1)
				minion = p1_minions_.remove(index);
			else
				throw new HSInvalidPlayerIndexException();
		} catch (IndexOutOfBoundsException e) {
			
		}
		return minion;
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
	
	public boolean removeMinion(int playerIndex, Minion minion) throws HSInvalidPlayerIndexException {
		if (playerIndex == 0)
			return p0_minions_.remove(minion);
		else if (playerIndex == 1)
			return p1_minions_.remove(minion);
		else
			throw new HSInvalidPlayerIndexException();
	}

	public boolean removeMinion_p0(Minion minion) {
		return p0_minions_.remove(minion);
	}
	
	public boolean removeMinion_p1(Minion minion) {
		return p1_minions_.remove(minion);
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
			for (Iterator<Minion> iter = p1_minions_.positionIterator(); iter.hasNext(); iter.next()) {
				toRet.add((Integer)counter);
				counter++;
			}
			return toRet;
		} else {
			int counter = 1;
			for (Iterator<Minion> iter = p1_minions_.positionIterator(); iter.hasNext();) {
				if ((iter.next()).getTaunt())
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
	
	public void endTurn(Deck deckPlayer0, Deck deckPlayer1) throws HSInvalidPlayerIndexException {
		p0_hero_.endTurn(0, this, deckPlayer0, deckPlayer1);
		p1_hero_.endTurn(1, this, deckPlayer0, deckPlayer1);
		for (int index = 0; index < p0_minions_.size(); ++index) {
			Minion targetMinion = p0_minions_.get(index);
			try {
				targetMinion.endTurn(0, this, deckPlayer0, deckPlayer1);
			} catch (HSException e) {
				e.printStackTrace();
			}
		}
		for (int index = 0; index < p1_minions_.size(); ++index) {
			Minion targetMinion = p1_minions_.get(index);
			try {
				targetMinion.endTurn(1, this, deckPlayer0, deckPlayer1);
			} catch (HSException e) {
				e.printStackTrace();
			}
		}
		
		Iterator<Minion> iter = p0_minions_.iterator();
		while (iter.hasNext()) {
			Minion targetMinion = iter.next();
			if (targetMinion.getHealth() <= 0)
				iter.remove();
		}

		iter = p1_minions_.iterator();
		while (iter.hasNext()) {
			Minion targetMinion = iter.next();
			if (targetMinion.getHealth() <= 0)
				iter.remove();
		}
	}
	
	public void startTurn(Deck deckPlayer0, Deck deckPlayer1) throws HSException {
		this.resetHand();
		this.resetMinions();

		for (Minion targetMinion : p0_minions_) {
			try {
				targetMinion.startTurn(0, this, deckPlayer0, deckPlayer1);
			} catch (HSInvalidPlayerIndexException e) {
				e.printStackTrace();
			}
		}
		for (Minion targetMinion : p1_minions_) {
			try {
				targetMinion.startTurn(1, this, deckPlayer0, deckPlayer1);
			} catch (HSInvalidPlayerIndexException e) {
				e.printStackTrace();
			}
		}
		Iterator<Minion> iter = p0_minions_.iterator();
		while (iter.hasNext()) {
			Minion targetMinion = iter.next();
			if (targetMinion.getHealth() <= 0) {
				iter.remove();
				p0_minions_.remove(targetMinion);
			}
		}

		iter = p1_minions_.iterator();
		while (iter.hasNext()) {
			Minion targetMinion = iter.next();
			if (targetMinion.getHealth() <= 0) {
				iter.remove();
				p1_minions_.remove(targetMinion);
			}
		}
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

	   if (p0_spellDamage_ != ((BoardState)other).p0_spellDamage_)
		   return false;

	   if (p1_spellDamage_ != ((BoardState)other).p1_spellDamage_)
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
		p0_hero_.hasAttacked(false);
		p0_hero_.hasBeenUsed(false);
		p1_hero_.hasAttacked(false);
		p1_hero_.hasBeenUsed(false);
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
		MinionList p0_minions = newState.getMinions_p0();
		MinionList p1_minions = newState.getMinions_p1();
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
		newState.p0_spellDamage_ = p1_spellDamage_;
		newState.p1_spellDamage_ = p0_spellDamage_;
		return newState;
	}
	
	public Object deepCopy() {
		BoardState newBoard = new BoardState();
		for (Iterator<Minion> iter = p0_minions_.positionIterator(); iter.hasNext();) {
			Minion tc = (Minion)(iter.next()).deepCopy();
			try {
				newBoard.placeMinion(0, tc);
			} catch (HSInvalidPlayerIndexException e) {
				
			}
		}
		for (Iterator<Minion> iter = p1_minions_.positionIterator(); iter.hasNext();) {
			Minion tc = (Minion)(iter.next()).deepCopy();
			try {
				newBoard.placeMinion(1, tc);
			} catch (HSInvalidPlayerIndexException e) {
				
			}
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
		newBoard.p0_spellDamage_ = this.p0_spellDamage_;
		newBoard.p1_spellDamage_ = this.p1_spellDamage_;
		return newBoard;
	}
	
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		json.put("p0_mana", p0_mana_);
		json.put("p1_mana", p1_mana_);
		json.put("p0_maxMana", p0_maxMana_);
		json.put("p1_maxMana", p0_maxMana_);
		json.put("p0_deckPos", p0_deckPos_);
		json.put("p1_deckPos", p1_deckPos_);

		JSONArray p0_minions = new JSONArray();
		for (Minion minion : p0_minions_)
			p0_minions.put(minion.toJSON());
		json.put("p0_minions", p0_minions);

		JSONArray p1_minions = new JSONArray();
		for (Minion minion : p1_minions_)
			p1_minions.put(minion.toJSON());
		json.put("p1_minions", p1_minions);

		JSONArray p0_hand = new JSONArray();
		for (Card card : p0_hand_)
			p0_hand.put(card.toJSON());
		json.put("p0_hand", p0_hand);
		
		JSONArray p1_hand = new JSONArray();
		for (Card card : p1_hand_)
			p1_hand.put(card.toJSON());
		json.put("p1_hand", p1_hand);

		json.put("p0_hero", p0_hero_.toJSON());
		json.put("p1_hero", p1_hero_.toJSON());

		json.put("p0_fatigue", p0_fatigueDamage_);
		json.put("p1_fatigue", p1_fatigueDamage_);
		json.put("p0_spellDamage", p0_spellDamage_);
		json.put("p1_spellDamage", p1_spellDamage_);
		return json;
	}
	
	public String toString() {
		JSONObject json = this.toJSON();
		return json.toString();
	}
	
}