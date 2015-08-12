package com.hearthsim.card.classic.minion.legendary;

import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.card.weapon.WeaponCard;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectHeroDraw;
import com.hearthsim.event.effect.EffectHeroWeaponDestroy;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class HarrisonJones extends Minion implements MinionBattlecryInterface {

    public HarrisonJones() {
        super();
    }

    /**
     * Battlecry: Destroy your opponent's weapon
     */
    @Override
    public EffectCharacter<Minion> getBattlecryEffect() {
        return (PlayerSide targetSide, CharacterIndex minionPlacementIndex, HearthTreeNode boardState) -> {
            WeaponCard weapon = boardState.data_.modelForSide(PlayerSide.WAITING_PLAYER).getHero().getWeapon();
            if (weapon != null) {
                EffectHeroDraw<Minion> draw = new EffectHeroDraw<>(weapon.getWeaponCharge());
                boardState = EffectHeroWeaponDestroy.DESTROY.applyEffect(PlayerSide.WAITING_PLAYER, boardState);
                boardState = draw.applyEffect(PlayerSide.CURRENT_PLAYER, boardState);
            }

            return boardState;
        };
    }
}
