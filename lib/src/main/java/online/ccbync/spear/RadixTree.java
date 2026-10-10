package online.ccbync.spear;

import java.util.ArrayList;
import java.util.List;

// Highly inspired by https://en.wikipedia.org/wiki/Radix_tree
class RadixTree {
    private Node root;

    public RadixTree() {
        root = new Node(new ArrayList<>(), true);
    }

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

    private class Edge {
        private String label;
        private Node targetNode;

        Edge(String label, Node targetNode) {
            this.label = label;
            this.targetNode = targetNode;
        }

        public String label() {
            return this.label;
        }

        public Node targetNode() {
            return this.targetNode;
        }

        public void changeLabel(String label) {
            this.label = label;
        }
    }

    public boolean lookup(String word) {
        var node = root;
        int suffixHead = 0;

        while (node != null && suffixHead < word.length()) {
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

        return node != null && suffixHead == word.length();
    }

    private String findCommonPrefix(String a, String b) {
        int n = Math.min(a.length(), b.length());

        for (int i = 0; i < n; i++) {
            if (a.charAt(i) != b.charAt(i)) {
                return i != 0 ? a.substring(0, i) : "";
            }
        }

        return a.length() == n ? a : b;
    }

    public void add(String word) {
        var node = root;
        int suffixHead = 0;

        while (!node.isLeaf()) {
            var suffix = word.substring(suffixHead);
            var edge = node.edges()
                .stream()
                .filter(e -> findCommonPrefix(suffix, e.label()).length() > 0)
                .findFirst();

            // No common prefix
            if (edge.isEmpty()) {
                node.edges().add(
                    new Edge(
                        suffix,
                        new Node(new ArrayList<>(), true)
                    )
                );

                return;
            } else {
                // Already added
                if (suffixHead == word.length()) {
                    return;
                }

                // Exact prefix match
                var edgeValue = edge.get();
                var label = edgeValue.label();
                if (suffix.startsWith(label) && node.isLeaf()) {
                    edgeValue.targetNode().edges().add(
                        new Edge(
                            suffix.substring(label.length()),
                            new Node(new ArrayList<>(), true)
                        )
                    );
                    node.switchType();

                    return;
                }

                // Partial prefix match
                if (findCommonPrefix(suffix, label).length() > 0) {
                    var prefix = findCommonPrefix(suffix, label);
                    edgeValue.changeLabel(label.substring(prefix.length()));
                    node.edges().remove(edgeValue);

                    var newNode = new Node(new ArrayList<>(), false);
                    node.edges().add(new Edge(
                        prefix,
                        newNode
                    ));
                    newNode.edges().add(edgeValue);
                    newNode.edges().add(
                        new Edge(
                            suffix.substring(prefix.length()),
                            new Node(new ArrayList<>(), true)
                        )
                    );

                    return;
                }
            }
            node = edge.get().targetNode();
            suffixHead += edge.get().label().length();
        }
    }
}
