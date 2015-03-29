package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectCharacterDraw;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class AcolyteOfPain extends Minion {

    private static final CardEffectCharacter effect = new CardEffectCharacterDraw(1);

    public AcolyteOfPain() {
        super();
    }

    /**
     * Called when this minion takes damage
     *
     * Draw a card whenever this minion takes damage
     * @param damage The amount of damage to take
     * @param attackPlayerSide The player index of the attacker.  This is needed to do things like +spell damage.
     * @param thisPlayerSide
     * @param boardState
     * @param isSpellDamage True if this is a spell damage   @throws HSException    */
    @Override
    public HearthTreeNode takeDamageAndNotify(byte damage, PlayerSide attackPlayerSide, PlayerSide thisPlayerSide, HearthTreeNode boardState, boolean isSpellDamage, boolean handleMinionDeath) {
        boolean hadShield = this.getDivineShield();
        HearthTreeNode toRet = super.takeDamageAndNotify(damage, attackPlayerSide, thisPlayerSide, boardState, isSpellDamage, handleMinionDeath);

        if (!hadShield && damage > 0) {
            toRet = AcolyteOfPain.effect.applyEffect(thisPlayerSide, this, thisPlayerSide, 0, toRet);
        }

        return toRet;
    }
}
