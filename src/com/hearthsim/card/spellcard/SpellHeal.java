package com.hearthsim.card.spellcard;

import com.hearthsim.card.minion.Minion;

public class SpellHeal extends SpellCard {


	byte healAmount_;
	
	public SpellHeal(String name, byte mana, byte healAmount, boolean hasBeenUsed) {
		super(name, mana, hasBeenUsed);
		healAmount_ = healAmount;
	}

	public SpellHeal() {
		super("Heal Spell", (byte)0, false);
		healAmount_ = 0;
	}

	public byte getHealAmount() {
		return healAmount_;
	}
	
	@Override
	public boolean equals(Object other)
	{

		if (!super.equals(other)) {
			return false;
		}
		
		if (other == null) {
			return false;
		}

		if (this.getClass() != other.getClass()) {
			return false;
		}
		
		if (this.healAmount_ != ((SpellHeal)other).healAmount_) {
			return false;
		}

	   return true;
	}
	
	@Override
	public Object deepCopy() {
		return new SpellHeal(this.getName(), this.getMana(), healAmount_, this.hasBeenUsed());
	}

}
