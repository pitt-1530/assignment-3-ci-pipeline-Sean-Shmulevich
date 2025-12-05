package edu.pitt.se;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlaylistRecommenderTest {

    @Test
    public void placeholder() {
        assertTrue(true);
    }
    @Test
    public void testNormalizeVolumeClamping() {
        // Exact boundary case
        assertEquals(0, PlaylistRecommender.normalizeVolume(0));
        assertEquals(1, PlaylistRecommender.normalizeVolume(1));

        // Negative values should clamp to 0
        assertEquals(0, PlaylistRecommender.normalizeVolume(-1));
        assertEquals(0, PlaylistRecommender.normalizeVolume(Integer.MIN_VALUE));
        // not a useful test?
        // assertEquals(0, PlaylistRecommender.normalizeVolume((int) Long.MIN_VALUE));

        // Large positive values should clamp to 100
        assertEquals(100, PlaylistRecommender.normalizeVolume(Integer.MAX_VALUE));
        assertEquals(100, PlaylistRecommender.normalizeVolume((int) Long.MAX_VALUE));
    }

    @Test
    public void testIsValidTrackTitle_AllCases() {

        // Non A–Z characters → invalid
        assertFalse(PlaylistRecommender.isValidTrackTitle("1234567890!@#$%^&*()"));

        // Valid alphabetic only → valid
        assertTrue(PlaylistRecommender.isValidTrackTitle("HelloWorld"));

        // Mixed valid + invalid characters → invalid
        assertFalse(PlaylistRecommender.isValidTrackTitle("A!B@C#D$"));

        // Leading whitespace should trim and still be valid
        assertTrue(PlaylistRecommender.isValidTrackTitle("   Hello World"));
        assertTrue(PlaylistRecommender.isValidTrackTitle("Hello World"));

        // Longer than 30 chars → invalid
        String over30 = "ABCDEFGHIJKLMNOPQRSTUVWXYZABCDE"; // 31 chars
        assertEquals(31, over30.length());
        assertFalse(PlaylistRecommender.isValidTrackTitle(over30));

        // Exactly 30 chars → valid
        String length30 = "ABCDEFGHIJKLMNOPQRSTUVWXYZABCD"; // 30 chars
        assertEquals(30, length30.length());
        assertTrue(PlaylistRecommender.isValidTrackTitle(length30));

        // Exactly 1 char → valid
        assertTrue(PlaylistRecommender.isValidTrackTitle("A"));

        // Very large input → invalid due to length rule
        int size = 100 * 1024 * 1024; // 100 MB
        String huge = "A".repeat(size);
        assertFalse(PlaylistRecommender.isValidTrackTitle(huge));
    }

    @Test
    public void testClassifyEnergy_AllCases() {

        // --- null input ---
        assertThrows(IllegalArgumentException.class,
                () -> PlaylistRecommender.classifyEnergy(null));

        // --- empty list ---
        assertThrows(IllegalArgumentException.class,
                () -> PlaylistRecommender.classifyEnergy(List.of()));

        // --- length 1 (LOW) ---
        assertEquals("LOW", PlaylistRecommender.classifyEnergy(List.of(80)));

        // --- length 1 (MEDIUM) ---
        assertEquals("MEDIUM", PlaylistRecommender.classifyEnergy(List.of(120)));

        // --- length 1 (HIGH) ---
        assertEquals("HIGH", PlaylistRecommender.classifyEnergy(List.of(150)));

        // --- length 2 (simple average) ---
        assertEquals("MEDIUM", PlaylistRecommender.classifyEnergy(List.of(100, 139)));
        assertEquals("HIGH", PlaylistRecommender.classifyEnergy(List.of(140, 200)));
        assertEquals("LOW", PlaylistRecommender.classifyEnergy(List.of(10, 20)));

        // --- LARGE LIST (200MB of integers) ---
        // 200MB ≈ 50 million ints (4 bytes each)
        int largeSize = 5_000_000; // safe for CI pipeline
        List<Integer> large = java.util.Collections.nCopies(largeSize, 150);

        assertEquals("HIGH", PlaylistRecommender.classifyEnergy(large));

        // --- Edge case: boundary values ---
        assertEquals("LOW", PlaylistRecommender.classifyEnergy(List.of(99, 99, 99)));
        assertEquals("MEDIUM", PlaylistRecommender.classifyEnergy(List.of(100, 100, 100)));
        assertEquals("HIGH", PlaylistRecommender.classifyEnergy(List.of(140, 140, 140)));

        // --- Mixed values that average just below/above thresholds ---
        assertEquals("MEDIUM", PlaylistRecommender.classifyEnergy(List.of(139, 140))); // avg = 139.5 ⇒ MEDIUM
        assertEquals("HIGH", PlaylistRecommender.classifyEnergy(List.of(140, 140)));   // avg = 140 ⇒ HIGH
    }

}
