package io.ordrex.chess;

import java.util.HashMap;
import java.util.Map;

public class Board {
    private final Map<Position, Piece> pieces = new HashMap<>();

    public void clear() {
        pieces.clear();
    }

    public void placePiece(Position pos, Piece piece) {
        pieces.put(pos, piece);
    }

    public Piece getPiece(Position pos) {
        return pieces.get(pos);
    }

    public void removePiece(Position pos) {
        pieces.remove(pos);
    }

    public boolean isEmpty(Position pos) {
        return !pieces.containsKey(pos);
    }

    public Map<Position, Piece> snapshot() {
        return new HashMap<>(pieces);
    }
}

