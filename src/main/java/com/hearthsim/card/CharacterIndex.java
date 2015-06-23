package com.hearthsim.card;

import com.hearthsim.model.PlayerSide;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by oyachai on 6/15/15.
 */
public enum CharacterIndex {

    HERO(0),
    MINION_1(1),
    MINION_2(2),
    MINION_3(3),
    MINION_4(4),
    MINION_5(5),
    MINION_6(6),
    MINION_7(7),
    MINION_8(8),
    MINION_9(9),
    UNKNOWN(99);

    private final int index;
    CharacterIndex(int index) {
        this.index = index;
    }

    public static CharacterIndex fromInteger(int flag) {
        CharacterIndex type = intToTypeMap.get(flag);
        if (type == null)
            return CharacterIndex.UNKNOWN;
        return type;
    }

    public int getInt() {
        return index;
    }

    private static final Map<Integer, CharacterIndex> intToTypeMap = new HashMap<>();
    static {
        for (CharacterIndex type : CharacterIndex.values()) {
            intToTypeMap.put(type.index, type);
        }
    }

    public static class CharacterLocation extends Location<CharacterIndex> {

        public CharacterLocation(PlayerSide playerSide, CharacterIndex index) {
            super(playerSide, index);
        }

    }

    public CharacterIndex indexToLeft() {
        if (this == HERO)
            return UNKNOWN;
        return CharacterIndex.fromInteger(this.getInt() - 1);
    }

    public CharacterIndex indexToRight() {
        if (this == MINION_7)
            return UNKNOWN;
        return CharacterIndex.fromInteger(this.getInt() + 1);
    }
}
