package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.card.minion.MinionTargetableBattlecry;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.CharacterFilterTargetedBattlecry;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class FrostElemental extends Minion implements MinionBattlecryInterface {

    /**
     * Battlecry: Freeze a character
     */
    private final static CharacterFilterTargetedBattlecry filter = new CharacterFilterTargetedBattlecry() {
        protected boolean includeEnemyHero() { return true; }
        protected boolean includeEnemyMinions() { return true; }
//        protected boolean includeOwnHero() { return true; }
//        protected boolean includeOwnMinions() { return true; }
    };

    private final static CardEffectCharacter battlecryAction = CardEffectCharacter.FREEZE;

    public FrostElemental() {
        super();
    }

    /**
     * Let's assume that it is never a good idea to freeze your own character
     */
    @Override
    public CharacterFilter getBattlecryFilter() {
        return FrostElemental.filter;
    }

    @Override
    public CardEffectCharacter getBattlecryEffect() {
        return FrostElemental.battlecryAction;
    }
}
