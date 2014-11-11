package com.hearthsim.card.spellcard;


public class SpellHeal extends SpellCard {


	byte healAmount_;
	
	public SpellHeal(String name, byte baseManaCost, byte healAmount, boolean hasBeenUsed) {
		super(baseManaCost, hasBeenUsed);
		healAmount_ = healAmount;
	}

	public SpellHeal() {
		super((byte)0, false);
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
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) healAmount_;
        return result;
    }

    @Override
	public SpellCard deepCopy() {
		return new SpellHeal(this.getName(), this.baseManaCost, healAmount_, this.hasBeenUsed());
	}

}
