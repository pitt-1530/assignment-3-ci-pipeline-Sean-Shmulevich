package edu.pitt.se;

import java.util.List;

public class PlaylistRecommender {

    public static String classifyEnergy(List<Integer> bpms) {
        // TODO: Implement classifyEnergy()
        if (bpms == null || bpms.isEmpty()) {
            throw new IllegalArgumentException("BPM list cannot be null or empty");
        }

        double avgBpm = bpms.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElseThrow(); // safe since list has to be non-empty in this execution context.

        return (avgBpm >= 140) ? "HIGH"
             : (avgBpm >= 100) ? "MEDIUM"
             : "LOW";
    }

    public static boolean isValidTrackTitle(String title) {
        if (title == null) return false;

        // this is realistically the best thing to do given the use case.
        // Song titles with whitespace in the start or end should be trimmed
        // Any whitespace before or after the string should be a mistake.
        String trimmed = title.trim();

        if (trimmed.length() < 1 || trimmed.length() > 30) return false;

        // Only alphabetic + spaces
        return trimmed.matches("[A-Za-z ]+");
    }

    public static int normalizeVolume(int volumeDb) {
        // TODO: Implement normalizeVolume()
        if(volumeDb < 0) return 0;
        else if(volumeDb > 100) return 100;
        return volumeDb;
    }
}
