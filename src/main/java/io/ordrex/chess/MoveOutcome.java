package io.ordrex.chess;

public class MoveOutcome {
    public final boolean legal;
    public final boolean redWins;      // by capture
    public final boolean blackWins;    // by capture
    public final boolean opponentInCheck;       // after a legal move
    public final boolean opponentCheckmated;    // after a legal move

    public MoveOutcome(boolean legal, boolean redWins) {
        this.legal = legal;
        this.redWins = redWins;
        this.blackWins = false;
        this.opponentInCheck = false;
        this.opponentCheckmated = false;
    }

    public MoveOutcome(boolean legal, boolean redWins, boolean blackWins, boolean opponentInCheck, boolean opponentCheckmated) {
        this.legal = legal;
        this.redWins = redWins;
        this.blackWins = blackWins;
        this.opponentInCheck = opponentInCheck;
        this.opponentCheckmated = opponentCheckmated;
    }
}
