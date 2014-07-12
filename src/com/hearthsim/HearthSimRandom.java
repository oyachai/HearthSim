package com.hearthsim;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.exception.HSException;
import com.hearthsim.player.playercontroller.ArtificialPlayer;

/**
 * A Game setup to play a random (fictitious) minion vs random (fictitious) minion games.
 * 
 * This class can be used to set up a game where each player plays a deck consisting of
 * 30 minions with random stats.  The attacks and healths are set to random numbers between
 * min value and max value, and the mana cost of the cards are assumed to be (attack + health) / 2.
 *
 */
public class HearthSimRandom extends HearthSim {

	@Override
	public GameResult runSingleGame() throws HSException {
		
		//------------------------------------------------------------------------
		//Create the random decks
		//------------------------------------------------------------------------
		Card[] cards0_ = new Card[numCardsInDeck_];
		Card[] cards1_ = new Card[numCardsInDeck_];

		for (int i = 0; i < numCardsInDeck_; ++i) {
			byte attack0 = (byte)((int)(Math.random() * maxMinionAttack_) + 1);
			byte health0 = (byte)((int)(Math.random() * maxMinionHealth_) + 1);
			byte mana0 = (byte)((int)(0.5 * (attack0 + health0)));
			cards0_[i] = new Minion("" + i, mana0, attack0, health0, attack0, health0, health0, false, false, false, false, false, false, false, false, false, true, false);

			byte attack1 = (byte)((int)(Math.random() * maxMinionAttack_) + 1);
			byte health1 = (byte)((int)(Math.random() * maxMinionHealth_) + 1);
			byte mana1 = (byte)((int)(0.5 * (attack1 + health1)));
			cards1_[i] = new Minion("" + i, mana1, attack1, health1, attack1, health1, health1, false, false, false, false, false, false, false, false, false, true, false);
		}
		
		int nt = 0;
		while (nt < numTaunts_) {
			int irand = (int)(Math.random() * numCardsInDeck_);
			if (!((Minion)cards0_[irand]).getTaunt()) {
				((Minion)cards1_[irand]).setTaunt(true);
				++nt;
			}
		}
		
		int ns = 0;
		while (ns < numHolySmite_) {
			int irand = (int)(Math.random() * numCardsInDeck_);
			if (!(cards1_[irand] instanceof SpellDamage)) {
				if (!((Minion)cards1_[irand]).getTaunt()) {
					cards1_[irand] = new SpellDamage("" + irand, (byte)1, (byte)2, false);
					++ns;
				}
			}
		}
		Deck deck0 = new Deck(cards0_);
		Deck deck1 = new Deck(cards1_);


		//------------------------------------------------------------------------		
		//Set up generic hero classes
		//------------------------------------------------------------------------
		Hero hero0 = new Hero();
		Hero hero1 = new Hero();
		
		//------------------------------------------------------------------------
		//Set up the AIs
		//------------------------------------------------------------------------
		ArtificialPlayer ai0 = new ArtificialPlayer(
				p0_w_a_,
				p0_w_h_,
				p0_wt_a_,
				p0_wt_h_,
				p0_wTaunt_,
				p0_my_wHeroHealth_,
				p0_enemy_wHeroHealth_,
				p0_wMana_,
				p0_my_wNumMinions_,
				p0_enemy_wNumMinions_,
				p0_wSd_add_,
				p0_wSd_mult_
				);
		
		ArtificialPlayer ai1 = new ArtificialPlayer(
				p1_w_a_,
				p1_w_h_,
				p1_wt_a_,
				p1_wt_h_,
				p1_wTaunt_,
				p1_my_wHeroHealth_,
				p1_enemy_wHeroHealth_,
				p1_wMana_,
				p1_my_wNumMinions_,
				p1_enemy_wNumMinions_,
				p1_wSd_add_,
				p1_wSd_mult_
				);
		
		return super.runSingleGame(ai0, hero0, deck0, ai1, hero1, deck1);
	}
}
