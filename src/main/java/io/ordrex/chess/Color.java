package io.ordrex.chess;

public enum Color {
    RED, BLACK;

    public static Color fromString(String s) {
        String v = s.trim().toLowerCase();
        if (v.startsWith("red")) return RED;
        if (v.startsWith("black")) return BLACK;
        throw new IllegalArgumentException("Unknown color: " + s);
    }
}

