package com.hearthsim.card.goblinsvsgnomes.spell.epic;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectOnResolveAoe;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Lightbomb extends SpellDamage implements EffectOnResolveAoe {

    public Lightbomb() {
        super();
    }

    @Override
    public EffectCharacter getAoeEffect() {
        return this.getSpellDamageEffect();
    }

    @Override
    public FilterCharacter getAoeFilter() {
        return FilterCharacter.ALL_MINIONS;
    }

    @Override
    protected HearthTreeNode use_core(PlayerSide side, Minion targetMinion, HearthTreeNode boardState) throws HSException {

        HearthTreeNode toRet = super.use_core(side, targetMinion, boardState);

        PlayerModel currentPlayer = toRet.data_.modelForSide(PlayerSide.CURRENT_PLAYER);

        for (Minion minion : currentPlayer.getMinions()) {
            toRet = minion.takeDamageAndNotify(minion.getAttack(), PlayerSide.CURRENT_PLAYER, side, toRet, false, false);
        }

        PlayerModel waitingPlayer = toRet.data_.modelForSide(PlayerSide.WAITING_PLAYER);
        for (Minion minion : waitingPlayer.getMinions()) {
            toRet = minion.takeDamageAndNotify(minion.getAttack(), PlayerSide.CURRENT_PLAYER, side, toRet, false, false);
        }

        return toRet;
    }
}
