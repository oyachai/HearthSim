package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectCharacterHeal;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class DarkscaleHealer extends Minion implements MinionBattlecryInterface {

    private static final CardEffectCharacter<Card> effect = new CardEffectCharacterHeal<>(2);

    private static final CharacterFilter filter = CharacterFilter.ALL_FRIENDLIES;

    public DarkscaleHealer() {
        super();
    }

    /**
     * Battlecry: Heals friendly characters for 2
     */
    @Override
    public CardEffectCharacter<Minion> getBattlecryEffect() {
        return (PlayerSide originSide, Minion origin, PlayerSide targetSide, int minionPlacementIndex, HearthTreeNode boardState) ->
            this.effectAllUsingFilter(DarkscaleHealer.effect, DarkscaleHealer.filter, boardState);
    }
}
