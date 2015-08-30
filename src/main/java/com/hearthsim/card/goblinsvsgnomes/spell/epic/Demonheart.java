package com.hearthsim.card.goblinsvsgnomes.spell.epic;

import com.hearthsim.card.CharacterIndex;
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
                public HearthTreeNode applyEffect(PlayerSide targetSide, CharacterIndex targetCharacterIndex, HearthTreeNode boardState) {
                    HearthTreeNode toRet = boardState;
                    Minion targetCharacter = boardState.data_.getCharacter(targetSide, targetCharacterIndex);
                    if (isCurrentPlayer(targetSide) && targetCharacter.getTribe() == MinionTribe.DEMON) {
                        targetCharacter.addAttack(5);
                        targetCharacter.addMaxHealth(5);
                        targetCharacter.addHealth(5);
                    } else {
                        toRet = super.applyEffect(targetSide, targetCharacterIndex, boardState);
                    }
                    return toRet;
                }
            };
        }
        return this.effect;
    }
}
