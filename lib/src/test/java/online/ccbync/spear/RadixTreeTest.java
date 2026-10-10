package online.ccbync.spear;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

// Test cases inspired by: https://en.wikipedia.org/wiki/Radix_tree#Insertion
class RadixTreeTest {
    RadixTree<Integer> rt;

    @BeforeEach
    void init() {
        rt = new RadixTree<>();
        rt.add("test", 1);
        rt.add("slow", 2);
    }

    @Test
    void lookupExistingWord() {
        assertThat(rt.lookup("test")).isPresent();
    }

    @Test
    void lookupNonExistingWord() {
        assertThat(rt.lookup("tester")).isEmpty();
    }

    @Test
    void duplicateInsertion() {
        rt.add("water", 3);
        rt.add("water", 3);

        assertThat(rt.lookup("water")).isPresent();
        var expected = """
        1 -> 2 [label="test"]
        1 -> 3 [label="slow"]
        1 -> 4 [label="water"]
        """;
        assertThat(rt.toString()).isEqualTo(expected);
    }

    @Test
    void outOfOrderInsertion() {
        rt.add("runner", 0);
        rt.add("run", 0);

        assertThat(rt.lookup("run")).isPresent();
        assertThat(rt.lookup("runner")).isPresent();
        var expected = """
        1 -> 2 [label="test"]
        1 -> 3 [label="slow"]
        1 -> 4 [label="run"]
        4 -> 5 [label="ner"]
        """;
        assertThat(rt.toString()).isEqualTo(expected);
    }

    @Test
    void noCommonPrefixInsertion() {
        rt.add("water", 0);

        var expected = """
        1 -> 2 [label="test"]
        1 -> 3 [label="slow"]
        1 -> 4 [label="water"]
        """;
        assertThat(rt.toString()).isEqualTo(expected);
    }

    @Test
    void exactPrefixMatch() {
        rt.add("water", 0);
        rt.add("slower", 0);

        var expected = """
        1 -> 2 [label="test"]
        1 -> 3 [label="slow"]
        1 -> 4 [label="water"]
        3 -> 5 [label="er"]
        """;
        assertThat(rt.toString()).isEqualTo(expected);
    }

    @Test
    void partialPrefixMatch() {
        rt.add("water", 0);
        rt.add("team", 0);

        var expected = """
        1 -> 2 [label="slow"]
        1 -> 3 [label="water"]
        1 -> 4 [label="te"]
        4 -> 5 [label="st"]
        4 -> 6 [label="am"]
        """;
        assertThat(rt.toString()).isEqualTo(expected);
    }
}
