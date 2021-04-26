package data;

public class Trainer {

    private String name;
    private Pokemon favePokemon;
    private Trainer faveTrainer;

    public Trainer(String name, Pokemon favePokemon, Trainer faveTrainer) {
        this.name = name;
        this.favePokemon = favePokemon;
        this.faveTrainer = faveTrainer;
    }

    public Trainer() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Pokemon getFavePokemon() {
        return favePokemon;
    }

    public void setFavePokemon(Pokemon favePokemon) {
        this.favePokemon = favePokemon;
    }

    public Trainer getFaveTrainer() {
        return faveTrainer;
    }

    public void setFaveTrainer(Trainer faveTrainer) {
        this.faveTrainer = faveTrainer;
    }

    @Override
    public String toString()
    {
        return this.getName();
    }
}
