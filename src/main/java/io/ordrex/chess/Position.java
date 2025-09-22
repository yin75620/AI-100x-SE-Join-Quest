package io.ordrex.chess;

import java.util.Objects;

public class Position {
    public final int row; // 1..10
    public final int col; // 1..9

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public static Position parse(String s) {
        // Format: (r, c)
        String t = s.trim();
        if (!t.startsWith("(") || !t.endsWith(")")) {
            throw new IllegalArgumentException("Invalid position: " + s);
        }
        String[] parts = t.substring(1, t.length() - 1).split(",");
        int r = Integer.parseInt(parts[0].trim());
        int c = Integer.parseInt(parts[1].trim());
        return new Position(r, c);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position position = (Position) o;
        return row == position.row && col == position.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}

