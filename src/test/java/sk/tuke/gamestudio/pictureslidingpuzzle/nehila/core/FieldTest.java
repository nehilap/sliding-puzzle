package sk.tuke.gamestudio.pictureslidingpuzzle.nehila.core;

import org.junit.Test;
import sk.tuke.gamestudio.game.puzzle.nehila.core.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FieldTest {
	
	private final int fieldSize;
	private final Field field;
	
	public FieldTest() {
		Random rd = new Random();
		fieldSize = rd.nextInt(25) + 3;
		field = new Field(fieldSize);
	}
	
	@Test
	public void checkEmptyMove() {
		int emptyTileRow = 0;
		int emptyTileColumn = 0;
		for(int row = 0; row < fieldSize; row++) {
			for(int column = 0; column < fieldSize; column++) {
				if(field.getTile(row, column) instanceof EmptyTile) {
					emptyTileRow = row;
					emptyTileColumn = column;
					break;
				}
			}
		}
		
		field.moveFragment(emptyTileRow, emptyTileColumn);
		
		assertTrue("Fragment movement was executed when it shouldn't be possible to move", field.getTile(emptyTileRow, emptyTileColumn) instanceof EmptyTile);
	}
	
	@Test
	public void checkFragmentMovableMove() {
		field.generateSolved();
		
		int emptyTileRow = fieldSize - 1;
		int emptyTileColumn = fieldSize - 1;
		
		Tile tilePreMove = field.getTile(emptyTileRow, emptyTileColumn - 1);
		
		field.moveFragment(emptyTileRow, emptyTileColumn - 1);
		
		assertTrue("Fragment movement was not executed correctly", field.getTile(emptyTileRow, emptyTileColumn - 1) instanceof EmptyTile);
		assertTrue(!(field.getTile(emptyTileRow, emptyTileColumn) instanceof EmptyTile));
		assertEquals(tilePreMove, field.getTile(emptyTileRow, emptyTileColumn));
	}
	
	@Test
	public void checkFragmentMovableEmptyMove() {
		field.generateSolved();
		
		int rowPreMove = fieldSize - 1;
		int colPreMove = fieldSize - 2;
		field.moveFragment(rowPreMove, colPreMove);
		
		assertTrue("Empty tile was not moved correctly", field.getTile(rowPreMove, colPreMove) instanceof EmptyTile);
	}
	
	@Test
	public void checkFragmentUnmovableMove() {
		field.generateSolved();
		
		int rowPreMove = 1;
		int colPreMove = 1;
		
		Tile tilePreMove = field.getTile(rowPreMove, colPreMove);
		field.moveFragment(rowPreMove, colPreMove);
		
		assertEquals("Fragment movement was executed when it shouldn't be possible to move", tilePreMove, field.getTile(rowPreMove, colPreMove));
	}
	
	@Test
	public void checkFragmentUnmovableMoveAround() {
		field.generateSolved();
		
		int rowPreMove = 1;
		int colPreMove = 1;
		
		List<Tile> tilesPreMove = new ArrayList<>();
		tilesPreMove.add(field.getTile(rowPreMove - 1, colPreMove));
		tilesPreMove.add(field.getTile(rowPreMove, colPreMove - 1));
		tilesPreMove.add(field.getTile(rowPreMove, colPreMove + 1));
		tilesPreMove.add(field.getTile(rowPreMove + 1, colPreMove));
		
		field.moveFragment(rowPreMove, colPreMove);
		
		List<Tile> tilesPostMove = new ArrayList<>();
		tilesPostMove.add(field.getTile(rowPreMove - 1, colPreMove));
		tilesPostMove.add(field.getTile(rowPreMove, colPreMove - 1));
		tilesPostMove.add(field.getTile(rowPreMove, colPreMove + 1));
		tilesPostMove.add(field.getTile(rowPreMove + 1, colPreMove));
		
		
		assertEquals("Fragment movement was executed when nothing should change", tilesPreMove, tilesPostMove);
	}
	
	@Test
	public void checkFragmentCount() {
		int fragmentCount = 0;
		
		for(int i = 0; i < fieldSize; i++) {
			for(int j = 0; j < fieldSize; j++) {
				if(field.getTile(i, j) instanceof Fragment) {
					fragmentCount++;
				}
			}
		}
		
		assertEquals("Field was initialized incorrectly," + " field should have " + (fieldSize * fieldSize - 1) + " fragments, got" + fragmentCount, fieldSize * fieldSize - 1, fragmentCount);
	}
	
	@Test
	public void checkEmptyCount() {
		int emptyCount = 0;
		
		for(int i = 0; i < fieldSize; i++) {
			for(int j = 0; j < fieldSize; j++) {
				if(field.getTile(i, j) instanceof EmptyTile) {
					emptyCount++;
				}
			}
		}
		
		assertEquals("Field was initialized incorrectly," + " field should have 1 empty tile, got " + emptyCount, 1, emptyCount);
	}
	
	@Test
	public void checkSolvedField() {
		field.generateSolved();
		
		// we have to move fragment back and forth, to trigger check if field is solved
		field.moveFragment(fieldSize - 1, fieldSize - 2);
		field.moveFragment(fieldSize - 1, fieldSize - 1);
		
		assertEquals(field.getGameState(), GameState.SOLVED);
	}
	
	@Test
	public void checkUnsolvedField() {
		assertEquals(field.getGameState(), GameState.PLAYING);
	}
}
