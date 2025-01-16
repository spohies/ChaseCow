import java.util.Comparator;

public class SortByName implements Comparator<Item> {
    @Override
    public int compare(Item item1, Item item2) {
        if (item1 == null && item2 == null) {
            return 0; 
        }
        if (item1 == null) {
            return -1;
        }
        if (item2 == null) {
            return 1;
        }
        return item1.getName().compareTo(item2.getName());
    }
}
