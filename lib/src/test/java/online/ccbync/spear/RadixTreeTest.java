package online.ccbync.spear;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

// Test cases inspired by: https://en.wikipedia.org/wiki/Radix_tree#Insertion
class RadixTreeTest {
    RadixTree rt;

    @BeforeEach
    void init() {
        rt = new RadixTree();
        rt.add("test");
        rt.add("slow");
    }

    @Test
    void lookupExistingWord() {
        assertThat(rt.lookup("test")).isTrue();
    }

    @Test
    void lookupNonExistingWord() {
        assertThat(rt.lookup("tester")).isFalse();
    }

    @Test
    void duplicateInsertion() {
        rt.add("water");
        rt.add("water");

        assertThat(rt.lookup("water")).isTrue();
        var expected = """
        1 -> 2 [label="test"]
        1 -> 3 [label="slow"]
        1 -> 4 [label="water"]
        """;
        assertThat(rt.toString()).isEqualTo(expected);
    }

    @Test
    void outOfOrderInsertion() {
        rt.add("runner");
        rt.add("run");

        assertThat(rt.lookup("run")).isTrue();
        assertThat(rt.lookup("runner")).isTrue();
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
        rt.add("water");

        var expected = """
        1 -> 2 [label="test"]
        1 -> 3 [label="slow"]
        1 -> 4 [label="water"]
        """;
        assertThat(rt.toString()).isEqualTo(expected);
    }

    @Test
    void exactPrefixMatch() {
        rt.add("water");
        rt.add("slower");

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
        rt.add("water");
        rt.add("team");

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
