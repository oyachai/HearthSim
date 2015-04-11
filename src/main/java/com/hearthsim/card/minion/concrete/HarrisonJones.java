package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.card.weapon.WeaponCard;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectCharacterDraw;
import com.hearthsim.event.effect.CardEffectHeroWeaponDestroy;
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
    public CardEffectCharacter<Minion> getBattlecryEffect() {
        return (PlayerSide originSide, Minion origin, PlayerSide targetSide, int minionPlacementIndex, HearthTreeNode boardState) -> {
            WeaponCard weapon = boardState.data_.modelForSide(PlayerSide.WAITING_PLAYER).getHero().getWeapon();
            if (weapon != null) {
                CardEffectCharacterDraw<Minion> draw = new CardEffectCharacterDraw<>(weapon.getWeaponCharge());
                boardState = CardEffectHeroWeaponDestroy.DESTROY.applyEffect(PlayerSide.CURRENT_PLAYER, this, PlayerSide.WAITING_PLAYER, boardState);
                boardState = draw.applyEffect(PlayerSide.CURRENT_PLAYER, this, PlayerSide.CURRENT_PLAYER, boardState);
            }

            return boardState;
        };
    }
}
