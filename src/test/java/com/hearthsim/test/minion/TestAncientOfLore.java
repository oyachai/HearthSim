package com.hearthsim.test.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.AncientOfLore;
import com.hearthsim.card.minion.concrete.BloodfenRaptor;
import com.hearthsim.card.minion.concrete.RaidLeader;
import com.hearthsim.card.minion.concrete.StormwindChampion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.CardDrawNode;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestAncientOfLore {

    private HearthTreeNode board;
    private PlayerModel currentPlayer;
    private PlayerModel waitingPlayer;

    @Before
    public void setup() throws HSException {
        board = new HearthTreeNode(new BoardModel());
        currentPlayer = board.data_.getCurrentPlayer();
        waitingPlayer = board.data_.getWaitingPlayer();

        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new RaidLeader());
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new StormwindChampion());

        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new RaidLeader());
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new BloodfenRaptor());

        currentPlayer.setMana((byte) 10);
        waitingPlayer.setMana((byte) 10);

        Minion fb = new AncientOfLore();
        currentPlayer.placeCardHand(fb);
    }

    @Test
    public void test1() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board);
        assertFalse(ret == null);

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 3);
        assertEquals(waitingPlayer.getNumMinions(), 2);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);
        assertEquals(currentPlayer.getMinions().get(0).getTotalHealth(), 6);
        assertEquals(currentPlayer.getMinions().get(1).getTotalHealth(), 3);
        assertEquals(currentPlayer.getMinions().get(2).getTotalHealth(), 6);
        assertEquals(waitingPlayer.getMinions().get(0).getTotalHealth(), 2);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalHealth(), 2);

        assertEquals(currentPlayer.getMinions().get(0).getTotalAttack(), 7);
        assertEquals(currentPlayer.getMinions().get(1).getTotalAttack(), 3);
        assertEquals(currentPlayer.getMinions().get(2).getTotalAttack(), 7);
        assertEquals(waitingPlayer.getMinions().get(0).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalAttack(), 4);

        assertEquals(currentPlayer.getMinions().get(0).getAuraAttack(), 2);
        assertEquals(currentPlayer.getMinions().get(1).getAuraAttack(), 1);
        assertEquals(currentPlayer.getMinions().get(2).getAuraAttack(), 1);
        assertEquals(waitingPlayer.getMinions().get(0).getAuraAttack(), 0);
        assertEquals(waitingPlayer.getMinions().get(1).getAuraAttack(), 1);

        //first child should be a CardDrawNode
        assertTrue( ret.getChildren().get(0) instanceof CardDrawNode );
        assertEquals( ((CardDrawNode)ret.getChildren().get(0)).getNumCardsToDraw(), 2);

        //there should be 7 children
        assertEquals( ret.numChildren(), 7);
    }
}
