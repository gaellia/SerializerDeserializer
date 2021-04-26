package data;

public class Pokemon {

    private int dexNum;
    private int level;
    private boolean canEvolve;

    public Pokemon(int dexNum, int level, boolean canEvolve) {
        this.dexNum = dexNum;
        this.level = level;
        this.canEvolve = canEvolve;
    }

    public Pokemon() {
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getDexNum() {
        return dexNum;
    }

    public void setDexNum(int dexNum) {
        this.dexNum = dexNum;
    }

    public boolean isCanEvolve() {
        return canEvolve;
    }

    public void setCanEvolve(boolean canEvolve) { this.canEvolve = canEvolve; }

    @Override
    public String toString()
    {
        return "# " + this.getDexNum();
    }
}
