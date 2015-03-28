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

public class Houndmaster extends Minion implements MinionTargetableBattlecry {

    /**
     * Battlecry: Give a friendly beast +2/+2 and Taunt
     */
    private final static MinionFilterTargetedBattlecry filter = new MinionFilterTargetedBattlecry() {
        protected boolean includeOwnMinions() { return true; }
        protected MinionTribe tribeFilter() { return MinionTribe.BEAST; }
    };

    private final static CardEffectCharacter battlecryAction = new CardEffectCharacterBuff(2, 2, true);

    public Houndmaster() {
        super();
    }

    @Override
    public boolean canTargetWithBattlecry(PlayerSide originSide, Card origin, PlayerSide targetSide, int targetCharacterIndex, BoardModel board) {
        return Houndmaster.filter.targetMatches(originSide, origin, targetSide, targetCharacterIndex, board);
    }

    @Override
    public HearthTreeNode useTargetableBattlecry_core(PlayerSide originSide, Minion origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) throws HSException {
        return Houndmaster.battlecryAction.applyEffect(originSide, origin, targetSide, targetCharacterIndex, boardState);
    }
}
