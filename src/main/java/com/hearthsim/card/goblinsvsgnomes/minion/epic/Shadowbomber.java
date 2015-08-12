package com.hearthsim.card.goblinsvsgnomes.minion.epic;

import com.hearthsim.card.Card;
import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterDamage;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Shadowbomber extends Minion implements MinionBattlecryInterface {

    private static final EffectCharacter<Card> effect = new EffectCharacterDamage<>(3);

    private static final FilterCharacter filter = FilterCharacter.ALL_HEROES;

    public Shadowbomber() {
        super();
    }

    /**
     * Battlecry: Heals friendly characters for 2
     */
    @Override
    public EffectCharacter<Minion> getBattlecryEffect() {
        return (PlayerSide targetSide, CharacterIndex minionPlacementIndex, HearthTreeNode boardState) ->
            this.effectAllUsingFilter(Shadowbomber.effect, Shadowbomber.filter, boardState);
    }
}
