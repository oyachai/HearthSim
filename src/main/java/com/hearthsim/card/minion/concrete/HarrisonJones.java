package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.card.weapon.WeaponCard;
import com.hearthsim.event.effect.CardEffectCharacterDraw;
import com.hearthsim.event.effect.CardEffectHeroWeaponDestroy;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class HarrisonJones extends Minion implements MinionUntargetableBattlecry {

    public HarrisonJones() {
        super();
    }

    /**
     * Battlecry: Destroy your opponent's weapon
     */
    @Override
    public HearthTreeNode useUntargetableBattlecry_core(
        int minionPlacementIndex,
        HearthTreeNode boardState,
        boolean singleRealizationOnly
    ) {
        WeaponCard weapon = boardState.data_.modelForSide(PlayerSide.WAITING_PLAYER).getHero().getWeapon();
        if (weapon != null) {
            CardEffectCharacterDraw<Minion> draw = new CardEffectCharacterDraw<>(weapon.getWeaponCharge());
            boardState = CardEffectHeroWeaponDestroy.DESTROY.applyEffect(PlayerSide.CURRENT_PLAYER, this, PlayerSide.WAITING_PLAYER, boardState);
            boardState = draw.applyEffect(PlayerSide.CURRENT_PLAYER, this, PlayerSide.CURRENT_PLAYER, boardState);
        }

        return boardState;
    }
}
