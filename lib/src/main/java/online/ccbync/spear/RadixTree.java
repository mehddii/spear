package online.ccbync.spear;

import java.util.ArrayList;
import java.util.List;

// Highly inspired by https://en.wikipedia.org/wiki/Radix_tree
class RadixTree {
    private Node root;

    private class Node {
        private List<Edge> edges;
        private boolean isLeaf;

        Node(List<Edge> edges, boolean isLeaf) {
            this.edges = edges;
            this.isLeaf = isLeaf;
        }

        public boolean isLeaf() {
            return this.isLeaf;
        }

        public List<Edge> edges() {
            return this.edges;
        }

        public void switchType() {
            this.isLeaf = !this.isLeaf;
        }
    }

    private record Edge(String label, Node targetNode) {
    }

    public boolean lookup(String word) {
        var node = root;
        int suffixHead = 0;

        while (node != null && !node.isLeaf() && suffixHead < word.length()) {
            var suffix = word.substring(suffixHead);
            var edge = node.edges()
                .stream()
                .filter(e -> suffix.startsWith(e.label()))
                .findFirst();

            if (edge.isEmpty()) {
                node = null;
            } else {
                node = edge.get().targetNode();
                suffixHead += edge.get().label().length();
            }
        }

        return node != null && node.isLeaf() && suffixHead == word.length();
    }

    public void add(String word) {
        var node = root;
        int suffixHead = 0;

        while (!node.isLeaf()) {
            var suffix = word.substring(suffixHead);
            var edge = node.edges()
                .stream()
                .filter(e -> suffix.startsWith(e.label()))
                .findFirst();

            if (edge.isEmpty()) {
                node.edges().add(
                    new Edge(
                        suffix,
                        new Node(new ArrayList<>(), true)
                    )
                );

                return;
            }
            node = edge.get().targetNode();
            suffixHead += edge.get().label().length();
        }

        if (suffixHead == word.length()) {
            return;
        }

        node.switchType();
        node.edges().add(
            new Edge(
                word.substring(suffixHead),
                new Node(new ArrayList<>(), true)
            )
        );
    }
}
