package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectCharacterBuffDelta;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class ColdlightSeer extends Minion implements MinionBattlecryInterface {

    private static final CardEffectCharacter<Card> effect = new CardEffectCharacterBuffDelta<>(0, 2);

    private static final CharacterFilter filter = new CharacterFilter() {
        @Override
        protected boolean includeOwnMinions() {
            return true;
        }

        @Override
        protected MinionTribe tribeFilter() {
            return MinionTribe.MURLOC;
        }

        @Override
        protected boolean excludeSource() {
            return true;
        }
    };

    public ColdlightSeer() {
        super();
    }

    @Override
    public CardEffectCharacter<Minion> getBattlecryEffect() {
        return (PlayerSide originSide, Minion origin, PlayerSide targetSide, int minionPlacementIndex, HearthTreeNode boardState) -> {
            return this.effectAllUsingFilter(ColdlightSeer.effect, ColdlightSeer.filter, boardState);
        };
    }
}
