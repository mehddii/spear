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

    private String findCommonPrefix(String a, String b) {
        int n = Math.min(a.length(), b.length());

        for (int i = 0; i < n; i++) {
            if (a.charAt(i) != b.charAt(i)) {
                return i != 0 ? a.substring(0, i - 1) : "";
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

            if (edge.isEmpty()) {
                node.edges().add(
                    new Edge(
                        suffix,
                        new Node(new ArrayList<>(), true)
                    )
                );

                return;
            } else if (node.isLeaf()) {
                if (suffixHead == word.length()) {
                    return;
                }

                node.switchType();
                if (suffix.startsWith(edge.get().label())) {
                    node.edges().add(
                        new Edge(
                            word.substring(suffixHead),
                            new Node(new ArrayList<>(), true)
                        )
                    );
                } else {
                    var prefix = findCommonPrefix(suffix, edge.get().label());
                    int startIndex = prefix.length() - 1;
                    edge.get().changeLabel(prefix);
                    edge.get().targetNode().edges().add(
                        new Edge(
                            edge.get().label().substring(startIndex),
                            new Node(new ArrayList<>(), true)
                        )
                    );
                    edge.get().targetNode().edges().add(
                        new Edge(
                            suffix.substring(startIndex),
                            new Node(new ArrayList<>(), true)
                        )
                    );
                }
            }
            node = edge.get().targetNode();
            suffixHead += edge.get().label().length();
        }
    }
}
