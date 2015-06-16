package com.hearthsim.test;

import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.basic.minion.BloodfenRaptor;
import com.hearthsim.card.basic.minion.ChillwindYeti;
import com.hearthsim.card.basic.minion.RiverCrocolisk;
import com.hearthsim.card.classic.minion.common.ArgentProtector;
import com.hearthsim.card.classic.minion.common.DarkIronDwarf;
import com.hearthsim.card.classic.minion.common.FaerieDragon;
import com.hearthsim.card.classic.minion.common.StranglethornTiger;
import com.hearthsim.card.classic.minion.legendary.Alexstrasza;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestMinionBattlecry {

    private HearthTreeNode board;
    private PlayerModel currentPlayer;
    private PlayerModel waitingPlayer;

    @Before
    public void setUp() throws Exception {
        board = new HearthTreeNode(new BoardModel());
        currentPlayer = board.data_.getCurrentPlayer();
        waitingPlayer = board.data_.getWaitingPlayer();

        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new BloodfenRaptor());
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new ChillwindYeti());

        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new RiverCrocolisk());
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new RiverCrocolisk());
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new RiverCrocolisk());

        currentPlayer.setMana((byte) 10);
        currentPlayer.setMaxMana((byte) 10);
    }

    @Test
    public void testMinionIsPlayed() throws HSException {
        DarkIronDwarf darkIronDwarf = new DarkIronDwarf();
        board.data_.placeCardHand(PlayerSide.CURRENT_PLAYER, darkIronDwarf);

        HearthTreeNode ret = darkIronDwarf.useOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);
        assertEquals(board, ret);

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 3);
        assertEquals(waitingPlayer.getNumMinions(), 3);
        assertEquals(currentPlayer.getMana(), 6);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), 4);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 4);
    }

    @Test
    // TODO explicitly verify the battlecry targeted correctly instead of relying on numChildren counts
    public void testTargetingFriendlyAndEnemyMinions() throws HSException {
        DarkIronDwarf darkIronDwarf = new DarkIronDwarf();
        board.data_.placeCardHand(PlayerSide.CURRENT_PLAYER, darkIronDwarf);

        HearthTreeNode ret = darkIronDwarf.useOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);
        assertEquals(board, ret);

        // At this point, the BoardState should have 5 children: 2 buffs on friendly side
        // and 3 buffs on enemy side
        assertEquals(board.numChildren(), 5);
    }

    @Test
    // TODO explicitly verify the battlecry targeted correctly instead of relying on numChildren counts
    public void testTargetingFriendlyMinions() throws HSException {
        ArgentProtector protector = new ArgentProtector();
        board.data_.placeCardHand(PlayerSide.CURRENT_PLAYER, protector);

        HearthTreeNode ret = protector.useOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);
        assertEquals(board, ret);

        // At this point, the BoardState should have 2 children: 2 buffs on friendly side
        assertEquals(board.numChildren(), 2);
    }

    @Test
    public void testTargetingFriendlyAndEnemyHeroes() throws HSException {
        Alexstrasza alexstrasza = new Alexstrasza();
        board.data_.placeCardHand(PlayerSide.CURRENT_PLAYER, alexstrasza);

        HearthTreeNode ret = alexstrasza.useOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);
        assertEquals(board, ret);

        // At this point, the BoardState should have 2 children: own hero and enemy hero
        assertEquals(board.numChildren(), 2);
    }

    @Test
    public void testCanBattlecryOwnStealthed() throws HSException {
        DarkIronDwarf darkIronDwarf = new DarkIronDwarf();
        board.data_.placeCardHand(PlayerSide.CURRENT_PLAYER, darkIronDwarf);
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new StranglethornTiger());

        HearthTreeNode ret = darkIronDwarf.useOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);
        assertEquals(board, ret);
        assertEquals(board.numChildren(), 6); // 3 on friendly (including Tiger) and 3 on enemy
    }

    @Test
    public void testCannotBattlecryEnemyStealthed() throws HSException {
        DarkIronDwarf darkIronDwarf = new DarkIronDwarf();
        board.data_.placeCardHand(PlayerSide.CURRENT_PLAYER, darkIronDwarf);
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new StranglethornTiger());

        HearthTreeNode ret = darkIronDwarf.useOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);
        assertEquals(board, ret);
        assertEquals(board.numChildren(), 5); // 2 on friendly and 3 on enemy (excluding Tiger)
    }

    @Test
    public void testCanBattlecryOwnFaerie() throws HSException {
        DarkIronDwarf darkIronDwarf = new DarkIronDwarf();
        board.data_.placeCardHand(PlayerSide.CURRENT_PLAYER, darkIronDwarf);
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new FaerieDragon());

        HearthTreeNode ret = darkIronDwarf.useOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);
        assertEquals(board, ret);
        assertEquals(board.numChildren(), 6); // 3 on friendly (including Faerie) and 3 on enemy
    }

    @Test
    public void testCanBattlecryEnemyFaerie() throws HSException {
        DarkIronDwarf darkIronDwarf = new DarkIronDwarf();
        board.data_.placeCardHand(PlayerSide.CURRENT_PLAYER, darkIronDwarf);
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new FaerieDragon());

        HearthTreeNode ret = darkIronDwarf.useOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);
        assertEquals(board, ret);
        assertEquals(board.numChildren(), 6); // 2 on friendly and 4 on enemy (including Faerie)
    }
}
