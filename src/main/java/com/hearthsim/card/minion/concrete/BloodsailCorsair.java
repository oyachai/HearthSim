package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.event.deathrattle.DeathrattleAction;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class BloodsailCorsair extends Minion implements MinionUntargetableBattlecry {

    private static final boolean HERO_TARGETABLE = true;
    private static final byte SPELL_DAMAGE = 1;

    public BloodsailCorsair() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;

        this.tribe = MinionTribe.PIRATE;
    }

    /**
     * Battlecry: Remove 1 Durability from your opponent's weapon.
     */
    @Override
    public HearthTreeNode useUntargetableBattlecry_core(
        Minion minionPlacementTarget,
        HearthTreeNode boardState,
        Deck deckPlayer0,
        Deck deckPlayer1,
        boolean singleRealizationOnly
    ) throws HSException {
        boolean hasWeapon = boardState.data_.getWaitingPlayerHero().getWeapon() != null;
        if (hasWeapon) {
            boardState.data_.getWaitingPlayerHero().getWeapon().useWeaponCharge();
            DeathrattleAction action = boardState.data_.getWaitingPlayerHero().checkForWeaponDeath();
            if (action != null) {
                boardState = action.performAction(null, PlayerSide.WAITING_PLAYER, boardState, deckPlayer0, deckPlayer1);
            }
        }
        return boardState;
    }
}
