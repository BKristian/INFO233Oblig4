package src;

public class Node {
    private Entry entry;
    private Node left;
    private Node right;
    private Node parent;
    private boolean black;

    public Node(Entry e) {
        entry = e;
    }

    public Entry getEntry() {
        return entry;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public boolean getBlack() {
        return black;
    }

    public void setBlack(boolean black) {
        this.black = black;
    }
}
