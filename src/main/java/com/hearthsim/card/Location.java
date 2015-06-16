package com.hearthsim.card;

import com.hearthsim.model.PlayerSide;
import org.json.JSONObject;

/**
 * Created by oyachai on 6/15/15.
 */
public class Location<T> {

    private final PlayerSide playerSide;
    private final T index;

    public Location(PlayerSide playerSide, T index) {
        this.playerSide = playerSide;
        this.index = index;
    }

    public PlayerSide getPlayerSide() {
        return this.playerSide;
    }

    public T getIndex() {
        return this.index;
    }

    @Override
    public int hashCode() {
        int result = playerSide != null ? playerSide.hashCode() : 0;
        result = 31 * result + index.hashCode();
        return result;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (!(o.getClass() == this.getClass())) {
            return false;
        }

        Location<T> other = (Location<T>) o;

        if (other.playerSide != this.playerSide) {
            return false;
        }

        if (other.index != this.index) {
            return false;
        }

        return true;
    }

    @SuppressWarnings("UnusedDeclaration")
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();

        json.put("playerSide", playerSide);
        json.put("index", index);

        return json;
    }

    public String toString() {
        return playerSide.toString() + ":" + index;
    }


}
