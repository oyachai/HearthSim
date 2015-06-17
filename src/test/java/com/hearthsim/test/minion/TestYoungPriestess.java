package com.hearthsim.test.minion;

import com.hearthsim.Game;
import com.hearthsim.card.Card;
import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.basic.minion.BoulderfistOgre;
import com.hearthsim.card.basic.minion.RaidLeader;
import com.hearthsim.card.classic.minion.rare.YoungPriestess;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.player.playercontroller.BruteForceSearchAI;
import com.hearthsim.util.HearthActionBoardPair;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class TestYoungPriestess {

    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

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

        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new RaidLeader());

        Card fb = new YoungPriestess();
        currentPlayer.placeCardHand(fb);

        currentPlayer.setMana((byte) 8);
        waitingPlayer.setMana((byte) 8);
    }

    @Test
    public void test0() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.WAITING_PLAYER, CharacterIndex.HERO, board);

        assertNull(ret);

        assertEquals(currentPlayer.getHand().size(), 1);
        assertEquals(currentPlayer.getNumMinions(), 2);
        assertEquals(waitingPlayer.getNumMinions(), 1);
        assertEquals(currentPlayer.getMana(), 8);
        assertEquals(waitingPlayer.getMana(), 8);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getTotalHealth(), 2);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getTotalHealth(), 7);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getTotalHealth(), 2);

        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 2);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 7);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 2);
    }

    @Test
    public void test1() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.MINION_2, board);

        assertFalse(ret == null);

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 3);
        assertEquals(waitingPlayer.getNumMinions(), 1);
        assertEquals(currentPlayer.getMana(), 7);
        assertEquals(waitingPlayer.getMana(), 8);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getTotalHealth(), 2);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getTotalHealth(), 7);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_3).getTotalHealth(), 1);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getTotalHealth(), 2);

        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 2);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 7);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_3).getTotalAttack(), 3);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 2);
    }

    @Test
    public void test2() throws HSException {

        //In this test, player1 goes first.  It uses the Stranglethorn Tiger to attack the hero, which removes stealth from
        // the tiger.  Then, player0 plays a turn in which it is able to kill the tiger and hit the player1's hero for 6.

        BruteForceSearchAI ai0 = BruteForceSearchAI.buildStandardAI1();
        List<HearthActionBoardPair> ab = ai0.playTurn(0, board.data_);
        BoardModel resBoard0 = ab.get(ab.size() - 1).board;
        PlayerModel currentPlayer = resBoard0.modelForSide(PlayerSide.CURRENT_PLAYER);
        PlayerModel waitingPlayer = resBoard0.modelForSide(PlayerSide.WAITING_PLAYER);

        assertEquals(resBoard0.getCurrentPlayer().getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 3);
        assertEquals(waitingPlayer.getNumMinions(), 0);
        assertEquals(resBoard0.getCurrentPlayer().getMana(), 7);
        assertEquals(resBoard0.getWaitingPlayer().getMana(), 8);
        assertEquals(resBoard0.getCurrentPlayer().getHero().getHealth(), 30);
        assertEquals(resBoard0.getWaitingPlayer().getHero().getHealth(), 28);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getTotalHealth(), 1);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getTotalHealth(), 2);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_3).getTotalHealth(), 5);

        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 3);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 2);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_3).getTotalAttack(), 7);

        int origHealthMinion1 = 2;
        int origHealthMinion2 = 5;

        BoardModel resBoard1 = Game.endTurn(resBoard0);
        currentPlayer = resBoard1.modelForSide(PlayerSide.CURRENT_PLAYER);

        log.info("Raid Leader Health = " + currentPlayer.getCharacter(CharacterIndex.MINION_2).getHealth());
        log.info("Boulderfist Ogre Health = " + currentPlayer.getCharacter(CharacterIndex.MINION_3).getHealth());

        assertTrue((currentPlayer.getCharacter(CharacterIndex.MINION_2).getHealth() != origHealthMinion1)
                ^ (currentPlayer.getCharacter(CharacterIndex.MINION_3).getHealth() != origHealthMinion2));
    }
}
