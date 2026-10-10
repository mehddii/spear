package online.ccbync.spear;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Optional;

// Highly inspired by https://en.wikipedia.org/wiki/Radix_tree
class RadixTree<V> implements PrefixTree<V> {
    private Node<V> root;

    public RadixTree() {
        root = new Node<V>(new ArrayList<>(), null);
    }

    private record Node<V>(List<Edge<V>> edges, V value) {
        public boolean isLeaf() {
            return value != null;
        }
    }

    private static class Edge<V> {
        private String label;
        private Node<V> targetNode;

        Edge(String label, Node<V> targetNode) {
            this.label = label;
            this.targetNode = targetNode;
        }

        public String label() {
            return this.label;
        }

        public Node<V> targetNode() {
            return this.targetNode;
        }

        public void changeLabel(String label) {
            this.label = label;
        }
    }

    public Optional<V> lookup(String word) {
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

        if (node != null && suffixHead == word.length()) {
            return Optional.ofNullable(node.value());
        }

        return Optional.empty();
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

    public void add(String word, V value) {
        var node = root;
        int suffixHead = 0;

        while (true) {
            var suffix = word.substring(suffixHead);
            var edge = node.edges()
                .stream()
                .filter(e -> findCommonPrefix(suffix, e.label()).length() > 0)
                .findFirst();

            // No common prefix
            if (edge.isEmpty()) {
                node.edges().add(
                    new Edge<>(
                        suffix,
                        new Node<>(new ArrayList<>(), value)
                    )
                );

                return;
            } else {
                var edgeValue = edge.get();
                var label = edgeValue.label();
                var prefix = findCommonPrefix(suffix, label);

                // Exact prefix match
                if (suffix.startsWith(label)) {
                    // Already added
                    if (suffixHead + prefix.length() == word.length()) {
                        return;
                    }

                    if (node.isLeaf()) {
                        edgeValue.targetNode().edges().add(
                            new Edge<>(
                                suffix.substring(label.length()),
                                new Node<>(new ArrayList<>(), value)
                            )
                        );

                        return;
                    }
                } else if (prefix.length() > 0) {
                    // Partial prefix match
                    edgeValue.changeLabel(label.substring(prefix.length()));
                    node.edges().remove(edgeValue);

                    var newNode = new Node<V>(new ArrayList<>(), suffix.length() == prefix.length() ? value : null);
                    node.edges().add(new Edge<>(
                        prefix,
                        newNode
                    ));
                    newNode.edges().add(edgeValue);

                    if (suffix.length() != prefix.length()) {
                        newNode.edges().add(
                            new Edge<V>(
                                suffix.substring(prefix.length()),
                                new Node<V>(new ArrayList<>(), value)
                            )
                        );
                    }

                    return;
                }
            }
            node = edge.get().targetNode();
            suffixHead += edge.get().label().length();
        }
    }

    private record Pair<K, V>(K key, V value) {
    }

    // Follows the graphviz dot syntax
    public String toString() {
        var sb = new StringBuilder();

        Deque<Pair<Integer, Node<V>>> queue = new ArrayDeque<>();
        queue.offerLast(new Pair<>(1, root));
        int counter = 2;
        while (!queue.isEmpty()) {
            var pair = queue.pollFirst();

            for (var edge: pair.value().edges()) {
                queue.offerLast(new Pair<>(counter, edge.targetNode()));
                sb.append("" + pair.key() + " -> " + counter + " [label=\"" + edge.label() + "\"]\n");
                counter++;
            }
        }

        return sb.toString();
    }
}
