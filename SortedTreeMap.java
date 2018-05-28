import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

public class SortedTreeMap<K extends Comparable<? super K>, V> implements ISortedTreeMap<K, V> {
    private Comparator comp;
    private Node<K, V> root;
    private int size;

    SortedTreeMap(Comparator c) {
        root = null;
        size = 0;
        comp = c;
    }

    @Override
    public Entry<K, V> min() {
        if(root == null) {
            return null;
        }
        Node<K, V> current = root;
        while(current.getLeft() != null) {
            current = current.getLeft();
        }
        return current.getEntry();
    }

    @Override
    public Entry<K, V> max() {
        if(root == null) {
            return null;
        }
        Node<K, V> current = root;
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
            root = new Node<>(entry);
            ++size;
            return null;
        }

        return recursiveAdd(entry, root);
    }

    private V recursiveAdd(Entry<K, V> entry, Node<K, V> node) {
        V toReturn = null;

        if(comp.compare(node.getEntry().key, entry.key) < 0) {
            if(node.getRight() == null) {
                Node<K, V> newNode = new Node<>(entry);
                node.setRight(newNode);
                newNode.setParent(node);
                ++size;
            } else {
                toReturn = recursiveAdd(entry, node.getRight());
            }
        } else if(comp.compare(node.getEntry().key, entry.key) > 0) {
            if(node.getLeft() == null) {
                Node<K, V> newNode = new Node<>(entry);
                node.setLeft(newNode);
                newNode.setParent(node);
                ++size;
            } else {
                toReturn = recursiveAdd(entry, node.getLeft());
            }
        } else if(comp.compare(node.getEntry().key, entry.key) == 0) {
            toReturn = node.getEntry().value;
            node.setEntry(entry);
        }
        return toReturn;
    }

    @Override
    public void replace(K key, V value) throws NoSuchElementException {
        if(root == null) {
            throw new NoSuchElementException();
        }

        Node<K, V> current = root;
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
                current.setEntry(new Entry<>(key, value));
                return;
            }
        }

        throw new NoSuchElementException();
    }

    @Override
    public void replace(K key, BiFunction<K, V, V> f) throws NoSuchElementException {
        if(root == null) {
            throw new NoSuchElementException();
        }

        Node<K, V> current = root;
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
                current.setEntry(new Entry<>(key, f.apply(key, current.getEntry().value)));
                return;
            }
        }

        throw new NoSuchElementException();
    }

    @Override
    public V remove(Object key) throws NoSuchElementException {
        if(root == null) {
            throw new NoSuchElementException();
        }

        Node<K, V> current = root;
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
                V toReturn = current.getEntry().value;

                if(current == root) {
                    if(current.getRight() != null) {
                        root = current.getRight();
                        root.setParent(null);
                        setLeftmostOfRightsFirstLeft(current);
                    } else if(current.getLeft() != null) {
                        root = current.getLeft();
                        root.setParent(null);
                    } else {
                        root = null;
                    }
                } else if(current.getRight() != null) {
                    setLeftmostOfRightsFirstLeft(current);
                    if(current.getParent().getLeft() == current) {
                        current.getParent().setLeft(current.getRight());
                    } else {
                        current.getParent().setRight(current.getRight());
                    }
                    current.getRight().setParent(current.getParent());
                } else if (current.getLeft() != null) {
                    current.getLeft().setParent(current.getParent());
                    if(current.getParent().getLeft() == current) {
                        current.getParent().setLeft(current.getLeft());
                    } else {
                        current.getParent().setRight(current.getLeft());
                    }
                }

                if (current.getParent() != null) {
                    if (current.getParent().getLeft() == current) {
                        current.getParent().setLeft(null);
                    } else if (current.getParent().getRight() == current) {
                        current.getParent().setRight(null);
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

    private void setLeftmostOfRightsFirstLeft(Node<K, V> current) {
        if(current.getLeft() != null) {
            Node<K, V> rightMin = current.getRight();
            while (rightMin.getLeft() != null) {
                rightMin = rightMin.getLeft();
            }
            rightMin.setLeft(current.getLeft());
            current.getLeft().setParent(rightMin);
        }
    }

    @Override
    public V getValue(Object key) throws NoSuchElementException {
        if(root == null) {
            throw new NoSuchElementException();
        }

        Node<K, V> current = root;
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
                return current.getEntry().value;
            }
        }

        throw new NoSuchElementException();
    }

    @Override
    public boolean containsKey(K key) {
        Node<K, V> current = root;
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

    private boolean preorderContains(Node<K, V> root, V value) {
        if(root == null) {
            return false;
        }
        if(comp.compare(root.getEntry().value, value) == 0) {
            return true;
        }
        boolean left = preorderContains(root.getLeft(), value);
        boolean right = preorderContains(root.getRight(), value);
        return left || right;
    }

    @Override
    public Iterable<K> keys() {
        return new Iterable<K>() {
            @Override
            public Iterator<K> iterator() {
                return new Iterator<K>() {
                    Iterator<Entry<K, V>> entries = entries().iterator();
                    @Override
                    public boolean hasNext() {
                        return entries.hasNext();
                    }

                    @Override
                    public K next() {
                        return entries.next().key;
                    }
                };
            }

            @Override
            public void forEach(Consumer<? super K> consumer) {
                for (K k : this)
                    consumer.accept(k);
            }
        };
    }

    @Override
    public Iterable<V> values() {
        return new Iterable<V>() {
            @Override
            public Iterator<V> iterator() {
                return new Iterator<V>() {
                    Iterator<Entry<K, V>> entries = entries().iterator();
                    @Override
                    public boolean hasNext() {
                        return entries.hasNext();
                    }

                    @Override
                    public V next() {
                        return entries.next().value;
                    }
                };
            }

            @Override
            public void forEach(Consumer<? super V> consumer) {
                for (V v : this)
                    consumer.accept(v);
            }
        };
    }

    @Override
    public Iterable<Entry<K, V>> entries() {
        return new Iterable<Entry<K, V>>() {
            @Override
            public Iterator<Entry<K, V>> iterator() {
                return new Iterator<Entry<K, V>>() {
                    Node<K, V> next = getFirst();
                    @Override
                    public boolean hasNext() {
                        return next != null;
                    }

                    @Override
                    public Entry<K, V> next() {
                        Node<K, V> node = next;
                        if (node == null) {
                            throw new NoSuchElementException();
                        }
                        next = successor(node);
                        return node.getEntry();
                    }

                    Node<K, V> successor(Node<K, V> node) {
                        if (node == null) {
                            return null;
                        }
                        else if (node.getRight()!= null) {
                            Node<K, V> right = node.getRight();
                            while (right.getLeft()!= null) {
                                right = right.getLeft();
                            }
                            return right;
                        } else {
                            Node<K, V> parent = node.getParent();
                            Node<K, V> child = node;
                            while (parent != null && child == parent.getRight()) {
                                child = parent;
                                parent = parent.getParent();
                            }
                            return parent;
                        }
                    }
                };
            }

            @Override
            public void forEach(Consumer<? super Entry<K, V>> consumer) {
                for (Entry<K, V> e : this)
                    consumer.accept(e);
            }
        };
    }

    private Node getFirst() {
        if(root == null) {
            return null;
        }
        Node first = root;
        while(first.getLeft() != null) {
            first = first.getLeft();
        }
        return first;
    }

    @Override
    public Entry<K, V> higherOrEqualEntry(K key) {
        Iterator<Entry<K, V>> entries = entries().iterator();
        Entry<K, V> curr;

        while(entries.hasNext()) {
            curr = entries.next();
            if(comp.compare(curr.key, key) == 0) {
                return curr;
            } else if(comp.compare(curr.key, key) > 0) {
                return curr;
            }
        }
        return null;
    }

    @Override
    public Entry<K, V> lowerOrEqualEntry(K key) {
        Iterator<Entry<K, V>> entries = entries().iterator();
        Entry<K, V> curr;
        Entry<K, V> toReturn = null;

        while(entries.hasNext()) {
            curr = entries.next();
            if(comp.compare(curr.key, key) == 0) {
                return curr;
            }
            if(comp.compare(curr.key, key) < 1) {
                if(toReturn == null) {
                    toReturn = curr;
                }
                if(comp.compare(curr.key, toReturn.key) > 0) {
                    toReturn = curr;
                }
            }
        }
        return toReturn;
    }

    @Override
    public void merge(ISortedTreeMap<K, V> other) {
        for (Entry<K, V> kvEntry : other.entries()) {
            this.add(kvEntry);
        }
    }

    @Override
    public void removeIf(BiPredicate<K, V> p) {
        if(!isEmpty()) {
            for (Entry<K, V> entry : entries()) {
                if (p.test(entry.key, entry.value)) {
                    remove(entry.key);
                }
            }
        }
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
}
