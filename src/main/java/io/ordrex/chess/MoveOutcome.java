package io.ordrex.chess;

public class MoveOutcome {
    public final boolean legal;
    public final boolean redWins;

    public MoveOutcome(boolean legal, boolean redWins) {
        this.legal = legal;
        this.redWins = redWins;
    }
}

