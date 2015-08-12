package com.hearthsim.test.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.basic.minion.BoulderfistOgre;
import com.hearthsim.card.basic.minion.RaidLeader;
import com.hearthsim.card.classic.minion.common.ArgentSquire;
import com.hearthsim.card.classic.minion.legendary.Alexstrasza;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestAlexstrasza {

    private HearthTreeNode board;
    private PlayerModel currentPlayer;
    private PlayerModel waitingPlayer;

    @Before
    public void setup() throws HSException {
        board = new HearthTreeNode(new BoardModel());
        currentPlayer = board.data_.getCurrentPlayer();
        waitingPlayer = board.data_.getWaitingPlayer();

        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new RaidLeader());
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new BoulderfistOgre());

        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new ArgentSquire());
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new RaidLeader());
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new BoulderfistOgre());

        Card fb = new Alexstrasza();
        currentPlayer.placeCardHand(fb);

        currentPlayer.setMana((byte) 10);
        waitingPlayer.setMana((byte) 10);
    }

    @Test
    public void testSetsOwnHealth() throws HSException {
        Minion hero = currentPlayer.getCharacter(CharacterIndex.HERO);
        Alexstrasza alexstrasza = new Alexstrasza();
        alexstrasza.getBattlecryEffect().applyEffect(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);
        assertEquals(15, hero.getHealth());
    }

    @Test
    public void testSetsEnemyHealth() throws HSException {
        Minion hero = waitingPlayer.getCharacter(CharacterIndex.HERO);
        Alexstrasza alexstrasza = new Alexstrasza();
        alexstrasza.getBattlecryEffect().applyEffect(PlayerSide.WAITING_PLAYER, CharacterIndex.HERO, board);
        assertEquals(15, hero.getHealth());
    }

    @Test
    public void testSetsDamagedHealth() throws HSException {
        Minion hero = currentPlayer.getCharacter(CharacterIndex.HERO);
        hero.setHealth((byte)20);
        Alexstrasza alexstrasza = new Alexstrasza();
        alexstrasza.getBattlecryEffect().applyEffect(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);
        assertEquals(15, hero.getHealth());
    }

    @Test
    public void testDoesNotEffectArmor() throws HSException {
        Hero hero = currentPlayer.getHero();
        hero.setArmor((byte)20);
        Alexstrasza alexstrasza = new Alexstrasza();
        alexstrasza.getBattlecryEffect().applyEffect(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);
        assertEquals(15, hero.getHealth());
        assertEquals(20, hero.getArmor());
    }

    @Test
    // TODO check to see if this counts as an actual heal effect (e.g., Soulpriest or Lightwarden)
    public void testHealsLowHealthTarget() throws HSException {
        Minion hero = currentPlayer.getCharacter(CharacterIndex.HERO);
        hero.setHealth((byte)2);
        Alexstrasza alexstrasza = new Alexstrasza();
        alexstrasza.getBattlecryEffect().applyEffect(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);
        assertEquals(15, hero.getHealth());
    }
}
