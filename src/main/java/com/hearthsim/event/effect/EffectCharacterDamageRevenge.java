package com.hearthsim.event.effect;

import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

/**
 * Created by oyachai on 8/20/15.
 */
public class EffectCharacterDamageRevenge implements EffectCharacter<Minion> {

    private final byte damage0;
    private final byte damage1;
    private final boolean effectedBySpellpower;

    public EffectCharacterDamageRevenge() {
        this(1, 3, true);
    }

    protected EffectCharacterDamageRevenge(int damage0, int damage1, boolean effectedBySpellpower) {
        this.damage0 = (byte) damage0;
        this.damage1 = (byte) damage1;
        this.effectedBySpellpower = effectedBySpellpower;
    }

    @Override
    public HearthTreeNode applyEffect(PlayerSide targetSide, CharacterIndex targetCharacterIndex, HearthTreeNode boardState) {
        Minion targetMinion = boardState.data_.modelForSide(targetSide).getCharacter(targetCharacterIndex);
        if (boardState.data_.getCurrentPlayer().getHero().getTotalHealth() <= 12) {
            return targetMinion.takeDamageAndNotify(this.damage1, PlayerSide.CURRENT_PLAYER, targetSide, boardState, this.effectedBySpellpower, true);
        } else {
            return targetMinion.takeDamageAndNotify(this.damage0, PlayerSide.CURRENT_PLAYER, targetSide, boardState, this.effectedBySpellpower, true);
        }
    }
}
