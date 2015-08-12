package com.hearthsim.card.classic.minion.legendary;

import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.classic.spell.common.Bananas;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class KingMukla extends Minion implements MinionBattlecryInterface {

    public KingMukla() {
        super();
    }

    /**
     * Battlecry: Give your opponent 2 bananas
     */
    @Override
    public EffectCharacter<Minion> getBattlecryEffect() {
        return (PlayerSide targetSide, CharacterIndex minionPlacementIndex, HearthTreeNode boardState) -> {
            PlayerModel waitingPlayer = boardState.data_.modelForSide(PlayerSide.WAITING_PLAYER);
            for (int index = 0; index < 2; ++index) {
                int numCards = waitingPlayer.getHand().size();
                if (numCards < 10) {
                    waitingPlayer.placeCardHand(new Bananas());
                }
            }
            return boardState;
        };
    }
}
