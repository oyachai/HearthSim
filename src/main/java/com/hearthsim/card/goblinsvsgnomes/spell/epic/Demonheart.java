package com.hearthsim.card.goblinsvsgnomes.spell.epic;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.Minion.MinionTribe;
import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.card.spellcard.SpellDamageTargetableCard;
import com.hearthsim.event.effect.EffectCharacterDamageSpell;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedSpell;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Demonheart extends SpellDamageTargetableCard {

    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public Demonheart() {
        super();
    }

    @Override
    public FilterCharacter getTargetableFilter() {
        return FilterCharacterTargetedSpell.ALL_MINIONS;
    }

    @Override
    public EffectCharacterDamageSpell<SpellDamage> getTargetableEffect() {
        if (this.effect == null) {
            this.effect = new EffectCharacterDamageSpell<SpellDamage>(damage_) {
                @Override
                public HearthTreeNode applyEffect(PlayerSide originSide, SpellDamage origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) {
                    HearthTreeNode toRet = boardState;
                    Minion targetCharacter = boardState.data_.getCharacter(targetSide, targetCharacterIndex);
                    if (isCurrentPlayer(targetSide) && targetCharacter.getTribe() == MinionTribe.DEMON) {
                        targetCharacter.setAttack((byte) (targetCharacter.getAttack() + 5));
                        targetCharacter.setMaxHealth((byte) (targetCharacter.getMaxHealth() + 5));
                        targetCharacter.setHealth((byte)(targetCharacter.getHealth() + 5));
                    } else {
                        toRet = super.applyEffect(originSide, origin, targetSide, targetCharacterIndex, boardState);
                    }
                    return toRet;
                }
            };
        }
        return this.effect;
    }
}
