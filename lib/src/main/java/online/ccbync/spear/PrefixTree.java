package online.ccbync.spear;

import java.util.Optional;
import java.util.List;
import java.util.regex.Pattern;

interface PrefixTree<V> {
   Optional<V> lookup(String key);
   void add(String key, V value);
   void add(String key, V value, List<Triplet<String, Pair<Class<?>, Integer>, Pattern>> metadata);
}
