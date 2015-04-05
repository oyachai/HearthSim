package com.hearthsim.card;

public class CardMock extends Card {
    private byte baseManaCost = -1;
    private String name;

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public byte getBaseManaCost() {
        return this.baseManaCost >= 0 ? this.baseManaCost : super.getBaseManaCost();
    }

    public void setBaseManaCost(byte value) {
        this.baseManaCost = value;
    }

    @Override
    public CardMock deepCopy() {
        CardMock copy = (CardMock)super.deepCopy();
        copy.name = this.name;
        copy.baseManaCost = this.baseManaCost;
        return copy;
    }
}
