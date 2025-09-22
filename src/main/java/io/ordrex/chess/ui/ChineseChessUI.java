package io.ordrex.chess.ui;

import io.ordrex.chess.Board;
import io.ordrex.chess.ChineseChessEngine;
import io.ordrex.chess.MoveOutcome;
import io.ordrex.chess.Piece;
import io.ordrex.chess.PieceType;
import io.ordrex.chess.Position;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

public class ChineseChessUI extends JFrame {
    private final ChineseChessEngine engine = new ChineseChessEngine();
    private io.ordrex.chess.Color turn = io.ordrex.chess.Color.RED;
    private boolean gameOver = false;
    private Position selected = null;

    private final JLabel status = new JLabel("Ready");
    private final BoardPanel boardPanel = new BoardPanel();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChineseChessUI ui = new ChineseChessUI();
            ui.setVisible(true);
        });
    }

    public ChineseChessUI() {
        super("Chinese Chess (象棋)");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(700, 800);
        setLocationRelativeTo(null);

        JPanel top = new JPanel();
        JButton btnNew = new JButton("New Game");
        JButton btnClear = new JButton("Clear");
        JButton btnResign = new JButton("Resign");
        JButton btnEnd = new JButton("End");
        top.add(btnNew);
        top.add(btnClear);
        top.add(btnResign);
        top.add(btnEnd);

        btnNew.addActionListener(e -> {
            setupStandard();
            gameOver = false;
            selected = null;
            turn = io.ordrex.chess.Color.RED;
            updateStatus("New game started. Red to move.");
            boardPanel.repaint();
        });
        btnClear.addActionListener(e -> {
            engine.getBoard().clear();
            gameOver = false;
            selected = null;
            turn = io.ordrex.chess.Color.RED;
            updateStatus("Board cleared. Red to move.");
            boardPanel.repaint();
        });
        btnResign.addActionListener(e -> {
            if (gameOver) return;
            gameOver = true;
            io.ordrex.chess.Color winner = (turn == io.ordrex.chess.Color.RED) ? io.ordrex.chess.Color.BLACK : io.ordrex.chess.Color.RED;
            JOptionPane.showMessageDialog(this, (winner == io.ordrex.chess.Color.RED ? "Red" : "Black") + " wins by resignation.");
            updateStatus("Game over.");
        });
        btnEnd.addActionListener(e -> {
            gameOver = true;
            updateStatus("Game ended.");
        });

        add(top, BorderLayout.NORTH);
        add(boardPanel, BorderLayout.CENTER);
        add(status, BorderLayout.SOUTH);

        setupStandard();
        updateStatus("Red to move.");
    }

    private void updateStatus(String s) {
        status.setText(s);
    }

    private void setupStandard() {
        Board b = engine.getBoard();
        b.clear();
        // Red
        b.placePiece(new Position(1, 1), new Piece(io.ordrex.chess.Color.RED, PieceType.ROOK));
        b.placePiece(new Position(1, 2), new Piece(io.ordrex.chess.Color.RED, PieceType.HORSE));
        b.placePiece(new Position(1, 3), new Piece(io.ordrex.chess.Color.RED, PieceType.ELEPHANT));
        b.placePiece(new Position(1, 4), new Piece(io.ordrex.chess.Color.RED, PieceType.GUARD));
        b.placePiece(new Position(1, 5), new Piece(io.ordrex.chess.Color.RED, PieceType.GENERAL));
        b.placePiece(new Position(1, 6), new Piece(io.ordrex.chess.Color.RED, PieceType.GUARD));
        b.placePiece(new Position(1, 7), new Piece(io.ordrex.chess.Color.RED, PieceType.ELEPHANT));
        b.placePiece(new Position(1, 8), new Piece(io.ordrex.chess.Color.RED, PieceType.HORSE));
        b.placePiece(new Position(1, 9), new Piece(io.ordrex.chess.Color.RED, PieceType.ROOK));
        b.placePiece(new Position(3, 2), new Piece(io.ordrex.chess.Color.RED, PieceType.CANNON));
        b.placePiece(new Position(3, 8), new Piece(io.ordrex.chess.Color.RED, PieceType.CANNON));
        for (int c = 1; c <= 9; c += 2) b.placePiece(new Position(4, c), new Piece(io.ordrex.chess.Color.RED, PieceType.SOLDIER));
        // Black
        b.placePiece(new Position(10, 1), new Piece(io.ordrex.chess.Color.BLACK, PieceType.ROOK));
        b.placePiece(new Position(10, 2), new Piece(io.ordrex.chess.Color.BLACK, PieceType.HORSE));
        b.placePiece(new Position(10, 3), new Piece(io.ordrex.chess.Color.BLACK, PieceType.ELEPHANT));
        b.placePiece(new Position(10, 4), new Piece(io.ordrex.chess.Color.BLACK, PieceType.GUARD));
        b.placePiece(new Position(10, 5), new Piece(io.ordrex.chess.Color.BLACK, PieceType.GENERAL));
        b.placePiece(new Position(10, 6), new Piece(io.ordrex.chess.Color.BLACK, PieceType.GUARD));
        b.placePiece(new Position(10, 7), new Piece(io.ordrex.chess.Color.BLACK, PieceType.ELEPHANT));
        b.placePiece(new Position(10, 8), new Piece(io.ordrex.chess.Color.BLACK, PieceType.HORSE));
        b.placePiece(new Position(10, 9), new Piece(io.ordrex.chess.Color.BLACK, PieceType.ROOK));
        b.placePiece(new Position(8, 2), new Piece(io.ordrex.chess.Color.BLACK, PieceType.CANNON));
        b.placePiece(new Position(8, 8), new Piece(io.ordrex.chess.Color.BLACK, PieceType.CANNON));
        for (int c = 1; c <= 9; c += 2) b.placePiece(new Position(7, c), new Piece(io.ordrex.chess.Color.BLACK, PieceType.SOLDIER));
        gameOver = false;
        turn = io.ordrex.chess.Color.RED;
        selected = null;
        boardPanel.repaint();
    }

    private class BoardPanel extends JPanel {
        private final int cell = 60;     // distance between intersections
        private final int padding = 40;  // margin around board

        BoardPanel() {
            // Board intersections: 9 columns => width spans 8 cells; 10 rows => height spans 9 cells
            setPreferredSize(new Dimension(padding*2 + cell*8, padding*2 + cell*9));
            setBackground(Color.WHITE);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (gameOver) return;
                    int x = e.getX();
                    int y = e.getY();
                    int colIdx = Math.round((x - padding) / (float) cell); // 0..8 nearest intersection
                    int rowIdxFromTop = Math.round((y - padding) / (float) cell); // 0..9
                    if (colIdx < 0 || colIdx > 8 || rowIdxFromTop < 0 || rowIdxFromTop > 9) return;
                    int col = colIdx + 1;
                    int row = 10 - rowIdxFromTop; // top is row 10
                    Position pos = new Position(row, col);
                    Piece p = engine.getBoard().getPiece(pos);
                    if (selected == null) {
                    if (p != null && p.color == turn) {
                            selected = pos;
                            repaint();
                        }
                    } else {
                        Position from = selected;
                        Piece selPiece = engine.getBoard().getPiece(from);
                        if (selPiece == null || selPiece.color != turn) {
                            selected = null;
                            repaint();
                            return;
                        }
                        MoveOutcome out = engine.move(turn, selPiece.type, from, pos);
                        if (out.legal) {
                            selected = null;
                            repaint();
                            if (out.redWins || out.blackWins) {
                                gameOver = true;
                                String winSide = out.redWins ? "Red" : "Black";
                                JOptionPane.showMessageDialog(ChineseChessUI.this, winSide + " wins by capture.");
                                updateStatus("Game over.");
                            } else if (out.opponentCheckmated) {
                                gameOver = true;
                                String winSide = (turn == io.ordrex.chess.Color.RED) ? "Red" : "Black";
                                JOptionPane.showMessageDialog(ChineseChessUI.this, winSide + " checkmates! Game over.");
                                updateStatus("Game over.");
                            } else {
                                // normal move
                                turn = (turn == io.ordrex.chess.Color.RED) ? io.ordrex.chess.Color.BLACK : io.ordrex.chess.Color.RED;
                                if (out.opponentInCheck) {
                                    updateStatus((turn == io.ordrex.chess.Color.RED ? "Red" : "Black") + " to move. Check!");
                                } else {
                                    updateStatus((turn == io.ordrex.chess.Color.RED ? "Red" : "Black") + " to move.");
                                }
                            }
                        } else {
                            // if clicked own piece, change selection
                            if (p != null && p.color == turn) {
                                selected = pos;
                            } else {
                                selected = null;
                            }
                            repaint();
                            Toolkit.getDefaultToolkit().beep();
                        }
                    }
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = padding*2 + cell*8;
            int h = padding*2 + cell*9;
            // Grid lines
            g2.setColor(new java.awt.Color(220, 180, 120));
            g2.fillRect(padding - 10, padding - 10, cell*8 + 20, cell*9 + 20);
            g2.setColor(Color.BLACK);
            for (int i = 0; i < 10; i++) {
                int y = padding + i * cell;
                g2.drawLine(padding, y, padding + cell*8, y); // full width across 8 cells
            }
            for (int j = 0; j < 9; j++) {
                int x = padding + j * cell;
                g2.drawLine(x, padding, x, padding + cell*4);
                g2.drawLine(x, padding + cell*5, x, padding + cell*9);
            }
            // Palace diagonals
            drawPalace(g2, 10, 8); // Black palace (top)
            drawPalace(g2, 3, 1);  // Red palace (bottom)

            // River label
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 24f));
            String river = "楚河    漢界";
            int riverY = padding + Math.round(4.5f * cell) + 10;
            g2.drawString(river, padding + cell, riverY);

            // Selection highlight
            if (selected != null) {
                Point p = toScreen(selected);
                int r = cell/2 - 2;
                g2.setColor(new java.awt.Color(0, 120, 255, 80));
                g2.fillOval(p.x - r, p.y - r, 2*r, 2*r);
            }

            // Pieces
            for (Map.Entry<Position, Piece> e : engine.getBoard().snapshot().entrySet()) {
                drawPiece(g2, e.getKey(), e.getValue());
            }
        }

        private void drawPalace(Graphics2D g2, int rowTop, int rowBottom) {
            int yTop = padding + (10 - rowTop) * cell;
            int yBottom = padding + (10 - rowBottom) * cell;
            int xLeft = padding + (4 - 1) * cell;
            int xRight = padding + (6 - 1) * cell;
            g2.drawLine(xLeft, yTop, xRight, yBottom);
            g2.drawLine(xRight, yTop, xLeft, yBottom);
        }

        private void drawPiece(Graphics2D g2, Position pos, Piece piece) {
            Point p = toScreen(pos);
            int r = cell/2 - 6;
            g2.setColor(Color.WHITE);
            g2.fillOval(p.x - r, p.y - r, 2*r, 2*r);
            g2.setColor(Color.BLACK);
            g2.drawOval(p.x - r, p.y - r, 2*r, 2*r);

            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 24f));
            g2.setColor(piece.color == io.ordrex.chess.Color.RED ? new java.awt.Color(180, 0, 0) : java.awt.Color.BLACK);
            String text = pieceText(piece);
            FontMetrics fm = g2.getFontMetrics();
            int tw = fm.stringWidth(text);
            int th = fm.getAscent();
            g2.drawString(text, p.x - tw/2, p.y + th/3);
        }

        private Point toScreen(Position pos) {
            int colIdx = pos.col - 1; // 0..8
            int rowIdxFromTop = 10 - pos.row; // 0..9
            int x = padding + colIdx * cell; // intersections are at grid lines
            int y = padding + rowIdxFromTop * cell;
            return new Point(x, y);
        }

        private String pieceText(Piece p) {
            boolean red = p.color == io.ordrex.chess.Color.RED;
            return switch (p.type) {
                case GENERAL -> red ? "帥" : "將";
                case GUARD -> red ? "仕" : "士";
                case ELEPHANT -> red ? "相" : "象";
                case HORSE -> "馬";
                case ROOK -> "車";
                case CANNON -> "炮";
                case SOLDIER -> red ? "兵" : "卒";
            };
        }
    }
}
