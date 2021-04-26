package data;

import java.util.ArrayList;

public class Champion {

    ArrayList<Pokemon> topPokemon;

    public Champion(ArrayList<Pokemon> topPokemon) {
        this.topPokemon = topPokemon;
    }

    public Champion() {
    }

    public ArrayList<Pokemon> getTopPokemon() {
        return topPokemon;
    }

    public void setTopPokemon(ArrayList<Pokemon> topPokemon) {
        this.topPokemon = topPokemon;
    }
}
