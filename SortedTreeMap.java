import java.util.ArrayList;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public class SortedTreeMap<K extends Comparable<? super K>, V> implements ISortedTreeMap<K, V> {
    private Comparator comp;
    private Node root;
    private int size;

    public SortedTreeMap(Comparator c) {
        root = null;
        size = 0;
        comp = c;
    }

    @Override
    public Entry<K, V> min() {
        Node current = root;
        while(current.getLeft() != null) {
            current = current.getLeft();
        }
        return current.getEntry();
    }

    @Override
    public Entry<K, V> max() {
        Node current = root;
        while(current.getRight() != null) {
            current = current.getRight();
        }
        return current.getEntry();
    }

    @Override
    public V add(K key, V value) {
        return add(new Entry<>(key, value));
    }

    @Override
    public V add(Entry<K, V> entry) {
        if(root == null) {
            root = new Node(entry);
            ++size;
            return null;
        }

        Node current = root;
        for(int i = 0; i < size; ++i) {
            // If current is smaller than new
            if(comp.compare(current.getEntry().key, entry.key) < 0) {
                if(current.getRight() == null) {
                    Node node = new Node(entry);
                    current.setRight(node);
                    node.setParent(current);
                    ++size;
                    return null;
                }
                current = current.getRight();
            }
            // If current is bigger than new
            if(comp.compare(current.getEntry().key, entry.key) > 0) {
                if(current.getLeft() == null) {
                    Node node = new Node(entry);
                    current.setLeft(node);
                    node.setParent(current);
                    ++size;
                    return null;
                }
                current = current.getLeft();
            }
            // If current is equal to new
            if(comp.compare(current.getEntry().key, entry.key) == 0) {
                V toReturn = (V) current.getEntry().value;
                current.setEntry(entry);
                return toReturn;
            }
        }

        return null;
    }

    @Override
    public void replace(K key, V value) throws NoSuchElementException {
        if(root == null) {
            throw new NoSuchElementException();
        }

        Node current = root;
        for(int i = 0; i < size; ++i) {
            // If current is smaller than new
            if(comp.compare(current.getEntry().key, key) < 0 && current.getRight() != null) {
                current = current.getRight();
            }
            // If current is bigger than new
            if(comp.compare(current.getEntry().key, key) > 0 && current.getLeft() != null) {
                current = current.getLeft();
            }
            // If current is equal to new
            if(comp.compare(current.getEntry().key, key) == 0) {
                current.setEntry(new Entry(key, value));
            }
        }

        throw new NoSuchElementException();
    }

    @Override
    public void replace(K key, BiFunction<K, V, V> f) throws NoSuchElementException {
        if(root == null) {
            throw new NoSuchElementException();
        }

        Node current = root;
        for(int i = 0; i < size; ++i) {
            // If current is smaller than new
            if(comp.compare(current.getEntry().key, key) < 0 && current.getRight() != null) {
                current = current.getRight();
            }
            // If current is bigger than new
            if(comp.compare(current.getEntry().key, key) > 0 && current.getLeft() != null) {
                current = current.getLeft();
            }
            // If current is equal to new
            if(comp.compare(current.getEntry().key, key) == 0) {
                current.setEntry(new Entry(key, f.apply(key, (V) current.getEntry().value)));
            }
        }

        throw new NoSuchElementException();
    }

    @Override
    public V remove(Object key) throws NoSuchElementException {
        if(root == null) {
            throw new NoSuchElementException();
        }

        Node current = root;
        for(int i = 0; i < size; ++i) {
            // If current is smaller than new
            if(comp.compare(current.getEntry().key, key) < 0 && current.getRight() != null) {
                current = current.getRight();
            }
            // If current is bigger than new
            if(comp.compare(current.getEntry().key, key) > 0 && current.getLeft() != null) {
                current = current.getLeft();
            }
            // If current is equal to new
            if(comp.compare(current.getEntry().key, key) == 0) {
                V toReturn = (V) current.getEntry().value;

                if(current == root) {
                    if(current.getRight() != null) {
                        root = current.getRight();

                        Node rightMin = current.getRight();
                        while(rightMin.getLeft() != null) {
                            rightMin = rightMin.getLeft();
                        }
                        rightMin.setLeft(current.getLeft());
                        current.getLeft().setParent(rightMin);
                    } else {
                        root = current.getLeft();
                    }
                    root.setParent(null);
                } else {
                    if(current.getRight() != null) {
                        Node rightMin = current.getRight();
                        while(rightMin.getLeft() != null) {
                            rightMin = rightMin.getLeft();
                        }
                        rightMin.setLeft(current.getLeft());
                        current.getLeft().setParent(rightMin);

                        if(current.getParent().getLeft() == current) {
                            current.getParent().setLeft(current.getRight());
                        } else {
                            current.getParent().setRight(current.getRight());
                        }
                        current.getRight().setParent(current.getParent());


                    }
                }

                current.setParent(null);
                current.setRight(null);
                current.setLeft(null);
                current.setEntry(null);
                --size;
                return toReturn;
            }
        }

        throw new NoSuchElementException();
    }

    @Override
    public V getValue(Object key) throws NoSuchElementException {
        if(root == null) {
            throw new NoSuchElementException();
        }

        Node current = root;
        for(int i = 0; i < size; ++i) {
            // If current is smaller than new
            if(comp.compare(current.getEntry().key, key) < 0 && current.getRight() != null) {
                current = current.getRight();
            }
            // If current is bigger than new
            if(comp.compare(current.getEntry().key, key) > 0 && current.getLeft() != null) {
                current = current.getLeft();
            }
            // If current is equal to new
            if(comp.compare(current.getEntry().key, key) == 0) {
                V toReturn = (V) current.getEntry().value;
                return toReturn;
            }
        }

        throw new NoSuchElementException();
    }

    @Override
    public boolean containsKey(K key) {
        Node current = root;
        for(int i = 0; i < size; ++i) {
            // If current is smaller than new
            if(comp.compare(current.getEntry().key, key) < 0 && current.getRight() != null) {
                current = current.getRight();
            }
            // If current is bigger than new
            if(comp.compare(current.getEntry().key, key) > 0 && current.getLeft() != null) {
                current = current.getLeft();
            }
            // If current is equal to new
            if(comp.compare(current.getEntry().key, key) == 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsValue(V value) {
        return preorderContains(root, value);
    }

    private boolean preorderContains(Node root, V value) {
        if(root == null) {
            return false;
        }
        if(comp.compare(root.getEntry().value, value) == 0) {
            return true;
        }
        boolean left = preorderContains(root.getLeft(), value);
        boolean right = preorderContains(root.getRight(), value);
        return left ? left : right;
    }

    @Override
    public Iterable<K> keys() {
        ArrayList<K> keys = new ArrayList<>();
        inorderKeys(root, keys);
        return keys;
    }

    private void inorderKeys(Node root, ArrayList<K> keys) {
        if(root.getLeft() != null) {
            inorderKeys(root.getLeft(), keys);
        }
        keys.add((K) root.getEntry().key);
        if(root.getRight() != null) {
            inorderKeys(root.getRight(), keys);
        }
    }

    @Override
    public Iterable<V> values() {
        return null;
    }

    @Override
    public Iterable<Entry<K, V>> entries() {
        return null;
    }

    @Override
    public Entry<K, V> higherOrEqualEntry(K key) {
        return null;
    }

    @Override
    public Entry<K, V> lowerOrEqualEntry(K key) {
        return null;
    }

    @Override
    public void merge(ISortedTreeMap<K, V> other) {

    }

    @Override
    public void removeIf(BiPredicate<K, V> p) {

    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    public Node getRoot() {
        return root;
    }

    public Comparator getComp() {
        return comp;
    }
}
