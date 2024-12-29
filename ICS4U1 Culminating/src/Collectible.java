public class Collectible extends Item{
    private boolean isComsumable;
    private int bonusHP;

    public Collectible(String description, boolean isComsumable, int bonusHP) {
        super(description);
        this.isComsumable = isComsumable;
        this.bonusHP = bonusHP;
    }

    public boolean getComsumability() {
        return this.isComsumable;
    }

    public int getBonusHP() {
        return this.bonusHP;
    }
}
