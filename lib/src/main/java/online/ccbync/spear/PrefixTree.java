package online.ccbync.spear;

import java.util.Optional;

interface PrefixTree<V> {
   Optional<V> lookup(String key);
   void add(String key, V value);
}
