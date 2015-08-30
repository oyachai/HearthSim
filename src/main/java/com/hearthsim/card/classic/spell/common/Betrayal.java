package com.hearthsim.card.classic.spell.common;

import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellTargetableCard;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedSpell;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Betrayal extends SpellTargetableCard {

    public Betrayal() {
        super();
    }

    @Override
    public FilterCharacter getTargetableFilter() {
        return FilterCharacterTargetedSpell.ENEMY_MINIONS;
    }

    @Override
    public EffectCharacter getTargetableEffect() {
        if (this.effect == null) {
            this.effect = (targetSide, targetCharacterIndex, boardState) -> {
                if (boardState.data_.modelForSide(targetSide).getNumMinions() < 2)
                    return null;
                HearthTreeNode toRet = boardState;
                Minion targetCharacter = toRet.data_.getCharacter(targetSide, targetCharacterIndex);
                byte damageToDeal = targetCharacter.getTotalAttack(toRet, targetSide);

                CharacterIndex minionIndexToLeft = targetCharacterIndex.indexToLeft();
                if (minionIndexToLeft != CharacterIndex.HERO && minionIndexToLeft != CharacterIndex.UNKNOWN) {
                    toRet = boardState.data_.getCharacter(targetSide, minionIndexToLeft)
                        .takeDamageAndNotify(damageToDeal, PlayerSide.CURRENT_PLAYER,
                            targetSide, toRet, true, false);
                }

                CharacterIndex minionIndexToRight = targetCharacterIndex.indexToRight();
                if (minionIndexToRight != CharacterIndex.HERO && minionIndexToRight != CharacterIndex.UNKNOWN) {
                    Minion minionToRight = boardState.data_.getCharacter(targetSide, minionIndexToRight);
                    if (minionToRight != null) {
                        toRet = minionToRight.takeDamageAndNotify(damageToDeal, PlayerSide.CURRENT_PLAYER,
                            targetSide, toRet, true, false);
                    }
                }

                return toRet;
            };
        }
        return this.effect;
    }

}
