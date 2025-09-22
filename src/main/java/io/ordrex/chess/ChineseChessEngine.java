package io.ordrex.chess;

import java.util.Map;

public class ChineseChessEngine {
    private final Board board = new Board();

    public Board getBoard() {
        return board;
    }

    public void setupEmptyWith(Piece piece, Position pos) {
        board.clear();
        board.placePiece(pos, piece);
    }

    public void setupFromTable(Map<String, String> pieceToPos) {
        board.clear();
        for (Map.Entry<String, String> e : pieceToPos.entrySet()) {
            String desc = e.getKey();
            String posStr = e.getValue();
            Color color = desc.toLowerCase().startsWith("red") ? Color.RED : Color.BLACK;
            String[] parts = desc.trim().split(" ");
            String typeStr = parts[1];
            PieceType type = PieceType.fromString(typeStr);
            board.placePiece(Position.parse(posStr), new Piece(color, type));
        }
    }

    public MoveOutcome move(Color mover, PieceType pieceType, Position from, Position to) {
        Piece piece = board.getPiece(from);
        if (piece == null || piece.color != mover || piece.type != pieceType) {
            return new MoveOutcome(false, false);
        }
        Piece target = board.getPiece(to);
        if (target != null && target.color == mover) {
            return new MoveOutcome(false, false);
        }

        boolean legal = switch (pieceType) {
            case GENERAL -> validateGeneralMove(mover, from, to);
            case GUARD -> validateGuardMove(mover, from, to);
            case ROOK -> validateRookMove(from, to);
            case HORSE -> validateHorseMove(from, to);
            case CANNON -> validateCannonMove(from, to, target != null);
            case ELEPHANT -> validateElephantMove(mover, from, to);
            case SOLDIER -> validateSoldierMove(mover, from, to);
        };

        if (!legal) return new MoveOutcome(false, false);

        // simulate move
        board.removePiece(from);
        boolean capturedBlackGeneral = target != null && target.type == PieceType.GENERAL && target.color == Color.BLACK;
        boolean capturedRedGeneral = target != null && target.type == PieceType.GENERAL && target.color == Color.RED;
        if (!board.isEmpty(to)) board.removePiece(to);
        board.placePiece(to, piece);

        // Generals facing rule (illegal if after move they face directly)
        if (!capturedBlackGeneral && !capturedRedGeneral && generalsFace()) {
            // revert (not strictly needed for these tests, but keeps board sane)
            board.removePiece(to);
            board.placePiece(from, piece);
            if (target != null) board.placePiece(to, target);
            return new MoveOutcome(false, false);
        }

        // Illegal if mover leaves own general in check
        if (isInCheck(mover)) {
            board.removePiece(to);
            board.placePiece(from, piece);
            if (target != null) board.placePiece(to, target);
            return new MoveOutcome(false, false);
        }

        // Winning condition: capture Black General
        if (capturedBlackGeneral) {
            return new MoveOutcome(true, true, false, false, false);
        }
        if (capturedRedGeneral) {
            return new MoveOutcome(true, false, true, false, false);
        }
        // Check/checkmate on opponent after a legal move
        Color opponent = (mover == Color.RED) ? Color.BLACK : Color.RED;
        boolean oppInCheck = isInCheck(opponent);
        boolean oppCheckmated = oppInCheck && !hasAnyLegalMove(opponent);

        return new MoveOutcome(true, false, false, oppInCheck, oppCheckmated);
    }

    private boolean validateGeneralMove(Color mover, Position from, Position to) {
        // One step orthogonal within own palace; cannot leave palace
        int dr = Math.abs(to.row - from.row);
        int dc = Math.abs(to.col - from.col);
        if (dr + dc != 1) return false;
        if (mover == Color.RED) {
            if (to.row < 1 || to.row > 3 || to.col < 4 || to.col > 6) return false;
        } else {
            if (to.row < 8 || to.row > 10 || to.col < 4 || to.col > 6) return false;
        }
        return true;
    }

    private boolean validateGuardMove(Color mover, Position from, Position to) {
        int dr = Math.abs(to.row - from.row);
        int dc = Math.abs(to.col - from.col);
        if (dr != 1 || dc != 1) return false;
        if (mover == Color.RED) {
            return to.row >= 1 && to.row <= 3 && to.col >= 4 && to.col <= 6;
        } else {
            return to.row >= 8 && to.row <= 10 && to.col >= 4 && to.col <= 6;
        }
    }

    private boolean validateRookMove(Position from, Position to) {
        if (from.row != to.row && from.col != to.col) return false;
        return pathClear(from, to);
    }

    private boolean validateHorseMove(Position from, Position to) {
        int dr = to.row - from.row;
        int dc = to.col - from.col;
        int adr = Math.abs(dr);
        int adc = Math.abs(dc);
        if (!((adr == 2 && adc == 1) || (adr == 1 && adc == 2))) return false;
        // leg block square
        Position block;
        if (adr == 2) {
            block = new Position(from.row + (dr > 0 ? 1 : -1), from.col);
        } else {
            block = new Position(from.row, from.col + (dc > 0 ? 1 : -1));
        }
        return board.isEmpty(block);
    }

    private boolean validateCannonMove(Position from, Position to, boolean isCapture) {
        if (from.row != to.row && from.col != to.col) return false;
        int screens = countBetween(from, to);
        if (!isCapture) {
            return screens == 0;
        } else {
            return screens == 1; // exactly one screen when capturing
        }
    }

    private boolean validateElephantMove(Color mover, Position from, Position to) {
        int dr = to.row - from.row;
        int dc = to.col - from.col;
        if (Math.abs(dr) != 2 || Math.abs(dc) != 2) return false;
        // midpoint must be empty
        Position mid = new Position(from.row + (dr > 0 ? 1 : -1), from.col + (dc > 0 ? 1 : -1));
        if (!board.isEmpty(mid)) return false;
        // cannot cross river
        if (mover == Color.RED) {
            return to.row <= 5;
        } else {
            return to.row >= 6;
        }
    }

    private boolean validateSoldierMove(Color mover, Position from, Position to) {
        int dr = to.row - from.row;
        int dc = to.col - from.col;
        if (Math.abs(dc) + Math.abs(dr) != 1) return false; // one step only
        if (mover == Color.RED) {
            if (dr == -1) return false; // cannot move backward
            if (from.row <= 5) {
                return dr == 1 && dc == 0; // before crossing, only forward
            } else {
                return (dr == 1 && dc == 0) || (dr == 0 && Math.abs(dc) == 1);
            }
        } else { // BLACK
            if (dr == 1) return false; // cannot move backward for black (towards red)
            if (from.row >= 6) {
                return dr == -1 && dc == 0; // before crossing (from black perspective)
            } else {
                return (dr == -1 && dc == 0) || (dr == 0 && Math.abs(dc) == 1);
            }
        }
    }

    private boolean pathClear(Position from, Position to) {
        if (from.row == to.row) {
            int c1 = Math.min(from.col, to.col) + 1;
            int c2 = Math.max(from.col, to.col) - 1;
            for (int c = c1; c <= c2; c++) {
                if (!board.isEmpty(new Position(from.row, c))) return false;
            }
            return true;
        } else if (from.col == to.col) {
            int r1 = Math.min(from.row, to.row) + 1;
            int r2 = Math.max(from.row, to.row) - 1;
            for (int r = r1; r <= r2; r++) {
                if (!board.isEmpty(new Position(r, from.col))) return false;
            }
            return true;
        }
        return false;
    }

    private int countBetween(Position from, Position to) {
        int count = 0;
        if (from.row == to.row) {
            int c1 = Math.min(from.col, to.col) + 1;
            int c2 = Math.max(from.col, to.col) - 1;
            for (int c = c1; c <= c2; c++) {
                if (!board.isEmpty(new Position(from.row, c))) count++;
            }
        } else if (from.col == to.col) {
            int r1 = Math.min(from.row, to.row) + 1;
            int r2 = Math.max(from.row, to.row) - 1;
            for (int r = r1; r <= r2; r++) {
                if (!board.isEmpty(new Position(r, from.col))) count++;
            }
        }
        return count;
    }

    private boolean generalsFace() {
        Position red = null, black = null;
        for (Map.Entry<Position, Piece> e : board.snapshot().entrySet()) {
            if (e.getValue().type == PieceType.GENERAL) {
                if (e.getValue().color == Color.RED) red = e.getKey();
                else black = e.getKey();
            }
        }
        if (red == null || black == null) return false;
        if (red.col != black.col) return false;
        // check if path between is clear
        int r1 = Math.min(red.row, black.row) + 1;
        int r2 = Math.max(red.row, black.row) - 1;
        for (int r = r1; r <= r2; r++) {
            if (!board.isEmpty(new Position(r, red.col))) return false;
        }
        return true;
    }

    public boolean isInCheck(Color side) {
        Position general = findGeneral(side);
        if (general == null) return false; // no general found (shouldn't happen in normal play)
        Color attacker = (side == Color.RED) ? Color.BLACK : Color.RED;
        for (Map.Entry<Position, Piece> e : board.snapshot().entrySet()) {
            Piece p = e.getValue();
            if (p.color != attacker) continue;
            Position from = e.getKey();
            switch (p.type) {
                case ROOK -> {
                    if (from.row == general.row || from.col == general.col) {
                        if (pathClear(from, general)) return true;
                    }
                }
                case CANNON -> {
                    if (from.row == general.row || from.col == general.col) {
                        if (countBetween(from, general) == 1) return true;
                    }
                }
                case HORSE -> {
                    if (validateHorseMove(from, general)) return true;
                }
                case ELEPHANT -> {
                    if (validateElephantMove(attacker, from, general)) return true;
                }
                case GUARD -> {
                    if (validateGuardMove(attacker, from, general)) return true;
                }
                case SOLDIER -> {
                    if (validateSoldierMove(attacker, from, general)) return true;
                }
                case GENERAL -> {
                    if (validateGeneralMove(attacker, from, general)) return true;
                }
            }
        }
        return false;
    }

    public boolean hasAnyLegalMove(Color side) {
        Map<Position, Piece> snap = board.snapshot();
        for (Map.Entry<Position, Piece> e : snap.entrySet()) {
            Piece p = e.getValue();
            if (p.color != side) continue;
            Position from = e.getKey();
            for (int r = 1; r <= 10; r++) {
                for (int c = 1; c <= 9; c++) {
                    Position to = new Position(r, c);
                    Piece target = board.getPiece(to);
                    if (target != null && target.color == side) continue;
                    if (wouldBeLegalMove(side, p.type, from, to)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean wouldBeLegalMove(Color mover, PieceType pieceType, Position from, Position to) {
        Piece piece = board.getPiece(from);
        if (piece == null || piece.color != mover || piece.type != pieceType) return false;
        Piece target = board.getPiece(to);
        if (target != null && target.color == mover) return false;

        boolean legal = switch (pieceType) {
            case GENERAL -> validateGeneralMove(mover, from, to);
            case GUARD -> validateGuardMove(mover, from, to);
            case ROOK -> validateRookMove(from, to);
            case HORSE -> validateHorseMove(from, to);
            case CANNON -> validateCannonMove(from, to, target != null);
            case ELEPHANT -> validateElephantMove(mover, from, to);
            case SOLDIER -> validateSoldierMove(mover, from, to);
        };
        if (!legal) return false;

        // simulate and revert
        board.removePiece(from);
        Piece captured = null;
        if (!board.isEmpty(to)) {
            captured = board.getPiece(to);
            board.removePiece(to);
        }
        board.placePiece(to, piece);

        boolean ok = true;
        // Generals facing is illegal
        if (generalsFace()) ok = false;
        // Self-check is illegal
        if (ok && isInCheck(mover)) ok = false;

        // revert
        board.removePiece(to);
        board.placePiece(from, piece);
        if (captured != null) board.placePiece(to, captured);

        return ok;
    }

    private Position findGeneral(Color side) {
        for (Map.Entry<Position, Piece> e : board.snapshot().entrySet()) {
            Piece p = e.getValue();
            if (p.type == PieceType.GENERAL && p.color == side) return e.getKey();
        }
        return null;
    }
}
