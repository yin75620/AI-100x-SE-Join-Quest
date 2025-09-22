package io.ordrex.chess.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.ordrex.chess.*;
import org.junit.jupiter.api.Assertions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChineseChessStepDefinitions {
    private final ChineseChessEngine engine = new ChineseChessEngine();
    private MoveOutcome lastOutcome;

    @Given("the board is empty except for a {word} {word} at \\({int}, {int}\\)")
    public void board_empty_except_one(String colorStr, String typeStr, int row, int col) {
        Color color = Color.fromString(colorStr);
        PieceType type = PieceType.fromString(typeStr);
        engine.setupEmptyWith(new Piece(color, type), new Position(row, col));
    }

    @Given("the board has:")
    public void the_board_has(DataTable table) {
        List<Map<String, String>> rows = table.asMaps(String.class, String.class);
        Map<String, String> pieces = new HashMap<>();
        for (Map<String, String> row : rows) {
            pieces.put(row.get("Piece"), row.get("Position"));
        }
        engine.setupFromTable(pieces);
    }

    @When("Red moves the {word} from \\({int}, {int}\\) to \\({int}, {int}\\)")
    public void red_moves_piece(String typeStr, int r1, int c1, int r2, int c2) {
        PieceType type = PieceType.fromString(typeStr);
        lastOutcome = engine.move(Color.RED, type, new Position(r1, c1), new Position(r2, c2));
    }

    @Then("the move is legal")
    public void the_move_is_legal() {
        Assertions.assertNotNull(lastOutcome, "No move was made");
        Assertions.assertTrue(lastOutcome.legal, "Expected move to be legal");
    }

    @Then("the move is illegal")
    public void the_move_is_illegal() {
        Assertions.assertNotNull(lastOutcome, "No move was made");
        Assertions.assertFalse(lastOutcome.legal, "Expected move to be illegal");
    }

    @Then("Red wins immediately")
    public void red_wins_immediately() {
        Assertions.assertNotNull(lastOutcome, "No move was made");
        Assertions.assertTrue(lastOutcome.legal, "Move should be legal");
        Assertions.assertTrue(lastOutcome.redWins, "Red should win by capturing Black General");
    }

    @Then("the game is not over just from that capture")
    public void the_game_is_not_over_from_that_capture() {
        Assertions.assertNotNull(lastOutcome, "No move was made");
        Assertions.assertTrue(lastOutcome.legal, "Move should be legal");
        Assertions.assertFalse(lastOutcome.redWins, "Game should continue");
    }
}
