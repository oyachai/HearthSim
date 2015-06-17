package com.hearthsim.test.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.basic.minion.BloodfenRaptor;
import com.hearthsim.card.basic.minion.RaidLeader;
import com.hearthsim.card.basic.spell.HolySmite;
import com.hearthsim.card.classic.spell.common.Silence;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class TestRaidLeader {

    private HearthTreeNode board;
    private PlayerModel currentPlayer;
    private PlayerModel waitingPlayer;

    @Before
    public void setup() throws HSException {
        board = new HearthTreeNode(new BoardModel());
        currentPlayer = board.data_.getCurrentPlayer();
        waitingPlayer = board.data_.getWaitingPlayer();

        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new RaidLeader());
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new BloodfenRaptor());

        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new RaidLeader());
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new BloodfenRaptor());

        currentPlayer.setMana((byte) 20);
        waitingPlayer.setMana((byte) 20);

        Minion fb = new RaidLeader();
        currentPlayer.placeCardHand(fb);
    }

    @Test
    public void test1() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);
        assertFalse(ret == null);

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 3);
        assertEquals(waitingPlayer.getNumMinions(), 2);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), 2);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getHealth(), 2);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_3).getHealth(), 2);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), 2);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getHealth(), 2);

        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 3);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 3);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_3).getTotalAttack(), 5);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 4);

        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getAuraAttack(), 1);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getAuraAttack(), 1);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_3).getAuraAttack(), 2);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getAuraAttack(), 0);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getAuraAttack(), 1);
    }

    @Test
    public void test2() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);
        assertFalse(ret == null);

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 3);
        assertEquals(waitingPlayer.getNumMinions(), 2);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);

        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), 2);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getHealth(), 2);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_3).getHealth(), 2);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), 2);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getHealth(), 2);

        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 3);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 3);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_3).getTotalAttack(), 5);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 4);

        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getAuraAttack(), 1);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getAuraAttack(), 1);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_3).getAuraAttack(), 2);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getAuraAttack(), 0);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getAuraAttack(), 1);

        currentPlayer.placeCardHand(new HolySmite());
        theCard = currentPlayer.getHand().get(0);
        ret = theCard.useOn(PlayerSide.WAITING_PLAYER, CharacterIndex.MINION_1, board);

        assertFalse(ret == null);
        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 3);
        assertEquals(waitingPlayer.getNumMinions(), 1);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), 2);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getHealth(), 2);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_3).getHealth(), 2);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), 2);

        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 3);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 3);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_3).getTotalAttack(), 5);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 3);

        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getAuraAttack(), 1);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getAuraAttack(), 1);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_3).getAuraAttack(), 2);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getAuraAttack(), 0);
    }

    @Test
    public void test3() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);
        assertFalse(ret == null);

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 3);
        assertEquals(waitingPlayer.getNumMinions(), 2);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), 2);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getHealth(), 2);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_3).getHealth(), 2);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), 2);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getHealth(), 2);

        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 3);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 3);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_3).getTotalAttack(), 5);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 4);

        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getAuraAttack(), 1);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getAuraAttack(), 1);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_3).getAuraAttack(), 2);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getAuraAttack(), 0);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getAuraAttack(), 1);

        currentPlayer.placeCardHand(new Silence());
        theCard = currentPlayer.getHand().get(0);
        ret = theCard.useOn(PlayerSide.WAITING_PLAYER, CharacterIndex.MINION_1, board);

        assertFalse(ret == null);
        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 3);
        assertEquals(waitingPlayer.getNumMinions(), 2);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), 2);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getHealth(), 2);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_3).getHealth(), 2);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), 2);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getHealth(), 2);

        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 3);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 3);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_3).getTotalAttack(), 5);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 3);

        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getAuraAttack(), 1);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getAuraAttack(), 1);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_3).getAuraAttack(), 2);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getAuraAttack(), 0);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getAuraAttack(), 0);

        currentPlayer.placeCardHand(new Silence());
        theCard = currentPlayer.getHand().get(0);
        ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.MINION_1, board);

        assertFalse(ret == null);
        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 3);
        assertEquals(waitingPlayer.getNumMinions(), 2);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), 2);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getHealth(), 2);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_3).getHealth(), 2);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), 2);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getHealth(), 2);

        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 3);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 2);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_3).getTotalAttack(), 4);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 3);

        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getAuraAttack(), 1);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getAuraAttack(), 0);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_3).getAuraAttack(), 1);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getAuraAttack(), 0);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getAuraAttack(), 0);

        currentPlayer.placeCardHand(new BloodfenRaptor());
        theCard = currentPlayer.getHand().get(0);
        ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.MINION_3, board);

        assertFalse(ret == null);
        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 4);
        assertEquals(waitingPlayer.getNumMinions(), 2);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), 2);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getHealth(), 2);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_3).getHealth(), 2);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_4).getHealth(), 2);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), 2);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getHealth(), 2);

        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 3);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 2);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_3).getTotalAttack(), 4);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_4).getTotalAttack(), 4);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 3);

        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getAuraAttack(), 1);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getAuraAttack(), 0);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_3).getAuraAttack(), 1);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_4).getAuraAttack(), 1);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getAuraAttack(), 0);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getAuraAttack(), 0);
    }
}
