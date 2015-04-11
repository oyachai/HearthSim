package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.card.minion.MinionTargetableBattlecry;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.CharacterFilterTargetedBattlecry;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectCharacterDamage;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class IronforgeRifleman extends Minion implements MinionBattlecryInterface {

    /**
     * Battlecry: Deal 1 damage to a chosen target
     */
    private final static CharacterFilterTargetedBattlecry filter = new CharacterFilterTargetedBattlecry() {
        protected boolean includeEnemyHero() { return true; }
        protected boolean includeEnemyMinions() { return true; }
        protected boolean includeOwnHero() { return true; }
        protected boolean includeOwnMinions() { return true; }
    };

    private final static CardEffectCharacter battlecryAction = new CardEffectCharacterDamage(1);

    public IronforgeRifleman() {
        super();
    }

    @Override
    public CharacterFilter getBattlecryFilter() {
        return IronforgeRifleman.filter;
    }

    @Override
    public CardEffectCharacter getBattlecryEffect() {
        return IronforgeRifleman.battlecryAction;
    }
}
