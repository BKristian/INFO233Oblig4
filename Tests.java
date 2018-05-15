import org.junit.Test;

import java.util.Comparator;

public class Tests {
    @Test
    public void addTest() {
        SortedTreeMap tree = new SortedTreeMap(Comparator.naturalOrder());
        for(int i = 0; i < 100; ++i) {
            tree.add(Math.floor(Math.random() * 10), i);
        }
    }

    @Test
    public void removeTest() {
        SortedTreeMap tree = new SortedTreeMap(Comparator.naturalOrder());
        tree.add(new Entry(5.0, -1));
        for(int i = 0; i < 100; ++i) {
            tree.add(Math.floor(Math.random() * 10), i);
        }

        tree.remove(5.0);
    }
}
