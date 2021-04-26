package data;

public class Leader {

    private Pokemon[] members = new Pokemon[3];

    public Leader(Pokemon[] members) {
        this.members = members;
    }

    public Leader() {
    }

    public Pokemon[] getMembers() {
        return members;
    }

    public void setMembers(Pokemon[] members) {
        this.members = members;
    }
}
