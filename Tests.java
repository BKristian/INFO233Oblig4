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
}
