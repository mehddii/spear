package online.ccbync.spear;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

// Test cases from: https://en.wikipedia.org/wiki/Radix_tree#Insertion
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
        assertThat(rt.lookup("test")).isEqualTo(true);
    }

    @Test
    void lookupNonExistingWord() {
        assertThat(rt.lookup("tester")).isEqualTo(false);
    }
}
