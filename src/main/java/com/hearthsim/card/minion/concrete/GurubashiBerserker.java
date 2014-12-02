package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;import com.hearthsim.entity.BaseEntity;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;


public class GurubashiBerserker extends Minion {

	private static final String NAME = "Gurubashi Berserker";
	private static final byte MANA_COST = 5;
	private static final byte ATTACK = 2;
	private static final byte HEALTH = 7;
	
	private static final boolean TAUNT = false;
	private static final boolean DIVINE_SHIELD = false;
	private static final boolean WINDFURY = false;
	private static final boolean CHARGE = false;
	
	private static final boolean STEALTHED = false;
	private static final boolean HERO_TARGETABLE = true;
	private static final boolean SUMMONED = false;
	private static final boolean TRANSFORMED = false;
	private static final byte SPELL_DAMAGE = 0;
	
	public GurubashiBerserker() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;
        summoned_ = SUMMONED;
        transformed_ = TRANSFORMED;
	}
	
	/**
	 * Called when this minion takes damage
	 * 
	 * Override for special ability: gain +3 attack whenever this minion takes damage
	 *  @param damage The amount of damage to take
	 * @param attackPlayerSide The player index of the attacker.  This is needed to do things like +spell damage.
     * @param thisPlayerSide
     * @param boardState
     * @param isSpellDamage True if this is a spell damage   @throws HSInvalidPlayerIndexException
     * */
	
	public HearthTreeNode takeDamage(
			byte damage,
			PlayerSide attackPlayerSide,
			PlayerSide thisPlayerSide,
			HearthTreeNode boardState,
			Deck deckPlayer0, 
			Deck deckPlayer1,
			boolean isSpellDamage,
			boolean handleMinionDeath)
		throws HSException
	{
		if (!divineShield_) {
			HearthTreeNode toRet = super.takeDamage(damage, attackPlayerSide, thisPlayerSide, boardState, deckPlayer0, deckPlayer1, isSpellDamage, handleMinionDeath);
			if (!silenced_)
				this.attack_ = (byte)(this.attack_ + 3);
			return toRet;
		} else {
			divineShield_ = false;
			return boardState;
		}
	}
	
	/**
	 * Called when this minion is silenced
	 * 
	 * Always use this function to "silence" minions
	 * 
	 *
     *
     * @param thisPlayerSide
     * @param boardState
     * @throws HSInvalidPlayerIndexException
	 */
	
	public void silenced(PlayerSide thisPlayerSide, BoardModel boardState) throws HSInvalidPlayerIndexException {
		super.silenced(thisPlayerSide, boardState);
		this.attack_ = this.baseAttack_;
	}
}
