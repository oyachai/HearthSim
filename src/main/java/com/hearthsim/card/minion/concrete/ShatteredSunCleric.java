package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionTargetableBattlecry;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectCharacterBuff;
import com.hearthsim.event.MinionFilterTargetedBattlecry;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class ShatteredSunCleric extends Minion implements MinionTargetableBattlecry {
    private final static MinionFilterTargetedBattlecry filter = new MinionFilterTargetedBattlecry() {
        protected boolean includeOwnMinions() { return true; }
    };

    private final static CardEffectCharacter battlecryAction = new CardEffectCharacterBuff(1, 1);

    public ShatteredSunCleric() {
        super();
    }

    @Override
    public boolean canTargetWithBattlecry(PlayerSide originSide, Card origin, PlayerSide targetSide, int targetCharacterIndex, BoardModel board) {
        return ShatteredSunCleric.filter.targetMatches(originSide, origin, targetSide, targetCharacterIndex, board);
    }

    @Override
    public HearthTreeNode useTargetableBattlecry_core(PlayerSide originSide, Minion origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) throws HSException {
        return ShatteredSunCleric.battlecryAction.applyEffect(originSide, origin, targetSide, targetCharacterIndex, boardState);
    }
}
