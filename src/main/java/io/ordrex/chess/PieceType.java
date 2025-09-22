package io.ordrex.chess;

public enum PieceType {
    GENERAL,
    GUARD,
    ROOK,
    HORSE,
    CANNON,
    ELEPHANT,
    SOLDIER;

    public static PieceType fromString(String s) {
        String v = s.trim().toLowerCase();
        if (v.contains("general")) return GENERAL;
        if (v.contains("guard")) return GUARD;
        if (v.contains("rook")) return ROOK;
        if (v.contains("horse")) return HORSE;
        if (v.contains("cannon")) return CANNON;
        if (v.contains("elephant")) return ELEPHANT;
        if (v.contains("soldier") || v.contains("pawn")) return SOLDIER;
        throw new IllegalArgumentException("Unknown piece type: " + s);
    }
}

