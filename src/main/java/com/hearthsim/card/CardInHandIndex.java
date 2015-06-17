package com.hearthsim.card;

import com.hearthsim.model.PlayerSide;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by oyachai on 6/15/15.
 */
public enum CardInHandIndex {

    CARD_0(0),
    CARD_1(1),
    CARD_2(2),
    CARD_3(3),
    CARD_4(4),
    CARD_5(5),
    CARD_6(6),
    CARD_7(7),
    CARD_8(8),
    CARD_9(9),
    UNKNOWN(99);

    private final int index;
    CardInHandIndex(int index) {
        this.index = index;
    }

    public static CardInHandIndex fromInteger(int flag) {
        CardInHandIndex type = intToTypeMap.get(flag);
        if (type == null)
            return CardInHandIndex.UNKNOWN;
        return type;
    }

    public int getInt() {
        return index;
    }

    private static final Map<Integer, CardInHandIndex> intToTypeMap = new HashMap<>();
    static {
        for (CardInHandIndex type : CardInHandIndex.values()) {
            intToTypeMap.put(type.index, type);
        }
    }

    /**
     * Created by oyachai on 6/15/15.
     */
    public static class CardInHandLocation extends Location<CardInHandIndex> {

        public CardInHandLocation(PlayerSide playerSide, CardInHandIndex index) {
            super(playerSide, index);
        }
    }

}
