package com.hearthsim.card.minion;

import com.hearthsim.entity.BaseEntity;
import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.ImplementedCardList;
import com.hearthsim.entity.BaseEntity;
import com.hearthsim.event.attack.AttackAction;
import com.hearthsim.event.deathrattle.DeathrattleAction;
import com.hearthsim.exception.HSException;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.factory.BoardStateFactoryBase;
import com.hearthsim.util.tree.HearthTreeNode;
import org.json.JSONObject;

import java.util.EnumSet;
import java.util.Iterator;

public class Minion extends BaseEntity {

	public enum BattlecryTargetType {
		NO_BATTLECRY,
		NO_TARGET,
		FRIENDLY_HERO, ENEMY_HERO,
    	FRIENDLY_MINIONS, ENEMY_MINIONS,
    	FRIENDLY_BEASTS, ENEMY_BEASTS,
    	FRIENDLY_MURLOCS, ENEMY_MURLOCS
    }
	
	//This is a flag to tell the BoardState that it can't cheat on the placement of this minion
	protected boolean placementImportant_ = false;

    public Minion() {
        super();
        ImplementedCardList cardList = ImplementedCardList.getInstance();
        ImplementedCardList.ImplementedCard implementedCard = cardList.getCardForClass(this.getClass());
        if (implementedCard!=null){
            // only 'Minion' class is not implemented
            mana_ = (byte) implementedCard.mana_;
            name_ = implementedCard.name_;
            //attack_ = (byte) implementedCard.attack_;
            baseAttack_ = attack_;
            //health_ = (byte) implementedCard.health_;
            maxHealth_ = health_;
            baseHealth_ = health_;
            taunt_ = implementedCard.taunt_;
            divineShield_ = implementedCard.divineShield_;
            windFury_ = implementedCard.windfury_;
            charge_ = implementedCard.charge_;
            stealthed_ = implementedCard.stealth_;
            isInHand_ = true;
            //todo: spellpower could be deduced from text quite easily
        }

    }

    /**
	 * Simplified constructor
	 * 
	 * @param name
	 * @param mana
	 * @param attack
	 * @param health
	 * @param baseAttack
	 * @param baseHealth
	 * @param maxHealth
	 */
	public Minion(String name, byte mana, byte attack, byte health, byte baseAttack, byte baseHealth, byte maxHealth) {
		this(
				name,
				mana,
				attack,
				health,
				baseAttack,
				(byte)0,
				(byte)0,
				baseHealth,
				maxHealth,
				(byte)0,
				(byte)0,
				false,
				false,
				false,
				false,
				false,
				false,
				false,
				false,
				false,
				true,
				false,
				false,
				false,
				false,
				null,
				null,
				true,
				false);
		
	}

	public Minion(	String name,
					byte mana,
					byte attack,
					byte health,
					byte baseAttack,
					byte extraAttackUntilTurnEnd,
					byte auraAttack,
					byte baseHealth,
					byte maxHealth,
					byte auraHealth,
					byte spellDamage,
					boolean taunt,
					boolean divineShield,
					boolean windFury,
					boolean charge,
					boolean hasAttacked,
					boolean hasWindFuryAttacked,
					boolean frozen,
					boolean silenced,
					boolean stealthed,
					boolean heroTargetable,
					boolean summoned,
					boolean transformed,
					boolean destroyOnTurnStart,
					boolean destroyOnTurnEnd,
					DeathrattleAction deathrattleAction,
					AttackAction attackAction,
					boolean isInHand,
					boolean hasBeenUsed) {
		super(name, mana, hasBeenUsed, isInHand);
		attack_ = attack;
		health_ = health;
		taunt_ = taunt;
		divineShield_ = divineShield;
		windFury_ = windFury;
		charge_ = charge;
		hasAttacked_ = hasAttacked;
		baseAttack_ = baseAttack;
		extraAttackUntilTurnEnd_ = extraAttackUntilTurnEnd;
		hasWindFuryAttacked_ = hasWindFuryAttacked;
		frozen_ = frozen;
		silenced_ = silenced;
		baseHealth_ = baseHealth;
		maxHealth_ = maxHealth;
		summoned_ = summoned;
		transformed_ = transformed;
		destroyOnTurnStart_ = destroyOnTurnStart;
		destroyOnTurnEnd_ = destroyOnTurnEnd;
		deathrattleAction_ = deathrattleAction;
		attackAction_ = attackAction;

		auraAttack_ = auraAttack;
		auraHealth_ = auraHealth;

		spellDamage_ = spellDamage;

		stealthed_ = stealthed;
		heroTargetable_ = heroTargetable;
	}
}
