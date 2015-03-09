package com.hearthsim.test.card;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.BoulderfistOgre;
import com.hearthsim.card.minion.concrete.RaidLeader;
import com.hearthsim.card.spellcard.concrete.SavageRoar;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestSavageRoar {

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
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new BoulderfistOgre());

        Card fb = new SavageRoar();
        currentPlayer.placeCardHand(fb);

        currentPlayer.setMana((byte) 10);
        waitingPlayer.setMana((byte) 10);

        currentPlayer.setMaxMana((byte) 10);
        waitingPlayer.setMaxMana((byte) 10);

        board.data_.resetMana();
        board.data_.resetMinions();
    }

    @Test
    public void test2() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board, null, null);

        assertFalse(ret == null);

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 2);
        assertEquals(waitingPlayer.getNumMinions(), 2);
        assertEquals(currentPlayer.getMana(), 7);
        assertEquals(waitingPlayer.getMana(), 10);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);
        assertEquals(currentPlayer.getMinions().get(0).getHealth(), 2);
        assertEquals(currentPlayer.getMinions().get(1).getHealth(), 7);
        assertEquals(waitingPlayer.getMinions().get(0).getHealth(), 2);
        assertEquals(waitingPlayer.getMinions().get(1).getHealth(), 7);

        assertEquals(currentPlayer.getMinions().get(0).getTotalAttack(), 4);
        assertEquals(currentPlayer.getMinions().get(1).getTotalAttack(), 9);
        assertEquals(waitingPlayer.getMinions().get(0).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalAttack(), 7);

        assertEquals(currentPlayer.getHero().getExtraAttackUntilTurnEnd(), 2);
        assertEquals(waitingPlayer.getHero().getExtraAttackUntilTurnEnd(), 0);
        assertEquals(currentPlayer.getMinions().get(0).getExtraAttackUntilTurnEnd(), 2);
        assertEquals(currentPlayer.getMinions().get(1).getExtraAttackUntilTurnEnd(), 2);
        assertEquals(waitingPlayer.getMinions().get(0).getExtraAttackUntilTurnEnd(), 0);
        assertEquals(waitingPlayer.getMinions().get(1).getExtraAttackUntilTurnEnd(), 0);

        Minion target = waitingPlayer.getCharacter(2);
        ret = currentPlayer.getMinions().get(0).attack(PlayerSide.WAITING_PLAYER, target, board, null, null, false);

        assertFalse(ret == null);
        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 1);
        assertEquals(waitingPlayer.getNumMinions(), 2);
        assertEquals(currentPlayer.getMana(), 7);
        assertEquals(waitingPlayer.getMana(), 10);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);
        assertEquals(currentPlayer.getMinions().get(0).getHealth(), 7);
        assertEquals(waitingPlayer.getMinions().get(0).getHealth(), 2);
        assertEquals(waitingPlayer.getMinions().get(1).getHealth(), 3);

        assertEquals(currentPlayer.getMinions().get(0).getTotalAttack(), 8);
        assertEquals(waitingPlayer.getMinions().get(0).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalAttack(), 7);

        assertEquals(currentPlayer.getHero().getExtraAttackUntilTurnEnd(), 2);
        assertEquals(waitingPlayer.getHero().getExtraAttackUntilTurnEnd(), 0);
        assertEquals(currentPlayer.getMinions().get(0).getExtraAttackUntilTurnEnd(), 2);
        assertEquals(waitingPlayer.getMinions().get(0).getExtraAttackUntilTurnEnd(), 0);
        assertEquals(waitingPlayer.getMinions().get(1).getExtraAttackUntilTurnEnd(), 0);

        target = waitingPlayer.getCharacter(2);
        ret = currentPlayer.getHero().attack(PlayerSide.WAITING_PLAYER, target, board, null, null, false);

        assertFalse(ret == null);
        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 1);
        assertEquals(waitingPlayer.getNumMinions(), 2);
        assertEquals(currentPlayer.getMana(), 7);
        assertEquals(waitingPlayer.getMana(), 10);
        assertEquals(currentPlayer.getHero().getHealth(), 23);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);
        assertEquals(currentPlayer.getMinions().get(0).getHealth(), 7);
        assertEquals(waitingPlayer.getMinions().get(0).getHealth(), 2);
        assertEquals(waitingPlayer.getMinions().get(1).getHealth(), 1);

        assertEquals(currentPlayer.getMinions().get(0).getTotalAttack(), 8);
        assertEquals(waitingPlayer.getMinions().get(0).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalAttack(), 7);

        assertEquals(currentPlayer.getHero().getExtraAttackUntilTurnEnd(), 2);
        assertEquals(waitingPlayer.getHero().getExtraAttackUntilTurnEnd(), 0);
        assertEquals(currentPlayer.getMinions().get(0).getExtraAttackUntilTurnEnd(), 2);
        assertEquals(waitingPlayer.getMinions().get(0).getExtraAttackUntilTurnEnd(), 0);
        assertEquals(waitingPlayer.getMinions().get(1).getExtraAttackUntilTurnEnd(), 0);
    }
}
