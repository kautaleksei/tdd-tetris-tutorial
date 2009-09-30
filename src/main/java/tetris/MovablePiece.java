/*
 * Copyright (c) 2008-2009  Esko Luontola, www.orfjackal.net
 *
 * You may use and modify this source code freely for personal non-commercial use.
 * This source code may NOT be used as course material without prior written agreement.
 */

package tetris;

import java.util.*;

/**
 * @author Esko Luontola
 */
public class MovablePiece implements RotatableGrid {

    // Coordinates in use:
    // 'outer'  = coordinate in the parent grid (game board)
    // 'inner'  = coordinate in the contained piece
    // 'offset' = the inner coordinate [0,0] in outer coordinates

    private final Point offset;
    private final RotatableGrid innerPiece;

    public MovablePiece(RotatableGrid innerPiece) {
        this(new Point(0, 0), innerPiece);
    }

    private MovablePiece(Point offset, RotatableGrid innerPiece) {
        this.offset = offset;
        this.innerPiece = innerPiece;
    }

    public boolean outsideBoard(Grid board) {
        for (Point inner : Grids.allPointsOf(this)) {
            if (innerPiece.cellAt(inner) != EMPTY
                    && outsideBoard(inner, board)) {
                return true;
            }
        }
        return false;
    }

    private boolean outsideBoard(Point inner, Grid board) {
        Point outer = asOuter(inner);
        return outer.row >= board.rows()
                || outer.col < 0
                || outer.col >= board.columns();
    }

    public boolean isAt(Point outer) {
        Point inner = asInner(outer);
        return inner.row >= 0 && inner.row < innerPiece.rows()
                && inner.col >= 0 && inner.col < innerPiece.columns()
                && innerPiece.cellAt(inner) != EMPTY;
    }

    public MovablePiece moveTo(Point offset) {
        return new MovablePiece(offset, innerPiece);
    }

    public MovablePiece moveDown() {
        return new MovablePiece(offset.moveDown(), innerPiece);
    }

    public MovablePiece moveLeft() {
        return new MovablePiece(offset.moveLeft(), innerPiece);
    }

    public MovablePiece moveRight() {
        return new MovablePiece(offset.moveRight(), innerPiece);
    }

    public MovablePiece rotateClockwise() {
        return new MovablePiece(offset, innerPiece.rotateClockwise());
    }

    public MovablePiece rotateCounterClockwise() {
        return new MovablePiece(offset, innerPiece.rotateCounterClockwise());
    }

    public int rows() {
        return innerPiece.rows();
    }

    public int columns() {
        return innerPiece.columns();
    }

    public char cellAt(Point inner) {
        return innerPiece.cellAt(inner);
    }

    public char cellAtOuter(Point outer) {
        return cellAt(asInner(outer));
    }

    public List<Point> blocksOnBoard() {
        List<Point> innerPoints = Grids.allNonEmptyPointsOf(innerPiece);
        List<Point> outerPoints = new ArrayList<Point>();
        for (Point inner : innerPoints) {
            outerPoints.add(asOuter(inner));
        }
        return outerPoints;
    }

    private Point asInner(Point outer) {
        return new Point(outer.row - offset.row, outer.col - offset.col);
    }

    private Point asOuter(Point inner) {
        return new Point(inner.row + offset.row, inner.col + offset.col);
    }
}
