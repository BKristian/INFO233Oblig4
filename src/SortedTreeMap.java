package src;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public class SortedTreeMap<K extends Comparable<? super K>, V> implements ISortedTreeMap<K, V> {
    private Comparator comp;
    private Node root;
    private int size;

    public SortedTreeMap(Comparator c) {
        comp = c;
    }

    @Override
    public Entry<K, V> min() {
        return null;
    }

    @Override
    public Entry<K, V> max() {
        return null;
    }

    @Override
    public V add(K key, V value) {
        Node newNode = new Node(new Entry<>(key, value));
        if(root == null) {
            root = newNode;
            return null;
        }

        Node current = root;
        for(int i = 0; i < size - 1; ++i) {
            // If current is smaller than new
            if(comp.compare(current.getEntry().key, newNode.getEntry().key) < 0) {
                if(current.getRight() == null) {
                    current.setRight(newNode);
                    newNode.setParent(current);
                    return null;
                }
                current = current.getRight();
            }
            // If current is bigger than new
            if(comp.compare(current.getEntry().key, newNode.getEntry().key) > 0) {
                if(current.getLeft() == null) {
                    current.setLeft(newNode);
                    newNode.setParent(current);
                    return null;
                }
                current = current.getLeft();
            }
            // If current is equal to new
            if(comp.compare(current.getEntry().key, newNode.getEntry().key) == 0) {
                V toReturn = (V) current.getEntry().value;
                newNode.setLeft(current.getLeft());
                newNode.setRight(current.getRight());
                if(current.getParent() != null) {
                    newNode.setParent(current.getParent());
                    if (newNode.getParent().getLeft() == current) {
                        newNode.getParent().setLeft(newNode);
                    } else {
                        newNode.getParent().setRight(newNode);
                    }
                }
                return toReturn;
            }
        }

        ++size;
        return null;
    }

    @Override
    public V add(Entry<K, V> entry) {
        if(root == null) {
            root = new Node(entry);
            return null;
        }

        ++size;
        return null;
    }

    @Override
    public void replace(K key, V value) throws NoSuchElementException {

    }

    @Override
    public void replace(K key, BiFunction<K, V, V> f) throws NoSuchElementException {

    }

    @Override
    public V remove(Object key) throws NoSuchElementException {
        return null;
    }

    @Override
    public V getValue(Object key) throws NoSuchElementException {
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        return false;
    }

    @Override
    public boolean containsValue(V value) {
        return false;
    }

    @Override
    public Iterable<K> keys() {
        return null;
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
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {

    }
}
