package online.ccbync.spear;

import java.util.Map;

class RadixTree {
    private Node root;

    private class Node {
        private Map<String, Node> children;
        private boolean isLeaf;

        public boolean isLeaf() {
            return isLeaf;
        }
    }

    public boolean lookup(String path) {
        return false;
    }

    public void add(String path) {

    }
}
