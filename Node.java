class Node<K, V> {
    private Entry<K, V> entry;
    private Node left;
    private Node right;
    private Node parent;

    Node(Entry<K,V> e) {
        entry = e;
    }

    Entry<K, V> getEntry() {
        return entry;
    }

    void setEntry(Entry<K, V> entry) {
        this.entry = entry;
    }

    Node<K, V> getLeft() {
        return left;
    }

    void setLeft(Node left) {
        this.left = left;
    }

    Node<K, V> getRight() {
        return right;
    }

    void setRight(Node right) {
        this.right = right;
    }

    Node<K, V> getParent() {
        return parent;
    }

    void setParent(Node parent) {
        this.parent = parent;
    }
}
