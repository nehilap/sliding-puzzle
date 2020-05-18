package sk.tuke.gamestudio.game.puzzle.nehila.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Field {
	private final Tile[][] field;
	private final int fieldSize;
	private final String gameGuide;
	private GameState gameState;
	private int actionCount;
	
	public Field(int fieldSize) {
		this.field = new Tile[fieldSize][fieldSize];
		this.fieldSize = fieldSize;
		this.gameState = GameState.PLAYING;
		this.actionCount = 0;
		
		this.gameGuide = "Your task is to arrange the puzzle pieces so that they form a picture or so that they are in numerical order, and you can move the boxes vertically or horizontally, " + "only if they are adjacent to an empty tile. The scoring system works according to the formula: (number of actions / number of tiles) * 10 The fewer points you have, the better!";
		
		generate();
	}
	
	private void generate() {
		List<Tile> indexes = new ArrayList<>();
		int numberOfFragments = fieldSize * fieldSize - 1;
		for(int i = 0; i < numberOfFragments; i++) {
			indexes.add(new Fragment(i));
		}
		indexes.add(new EmptyTile());
		
		Collections.shuffle(indexes);
		
		// i / fieldSize == row
		// i % fieldSize == column
		for(int i = 0; i < numberOfFragments + 1; i++) {
			field[i / fieldSize][i % fieldSize] = indexes.get(i);
		}
		
		makeSolvable();
	}
	
	private void makeSolvable() {
		int emptyTileRow = 0;
		int emptyTileColumn = 0;
		for(int i = 0; i < fieldSize; i++) {
			for(int j = 0; j < fieldSize; j++) {
				if(field[i][j] instanceof EmptyTile) {
					emptyTileRow = i;
					emptyTileColumn = j;
					break;
				}
			}
		}
		
		if(!isSolvable(emptyTileRow)) {
			if(emptyTileRow == 0 && (emptyTileColumn == 1 || emptyTileColumn == 0)) {
				swapTiles(fieldSize - 1, fieldSize - 1, fieldSize - 1, fieldSize - 2);
			} else {
				swapTiles(0, 0, 0, 1);
			}
		}
	}
	
	private void swapTiles(int row1, int col1, int row2, int col2) {
		Tile tile1 = field[row1][col1];
		field[row1][col1] = field[row2][col2];
		field[row2][col2] = tile1;
	}
	
	private boolean isSolvable(int emptyTileRow) {
		if(fieldSize % 2 == 1) {
			return (getTotalParity() % 2 == 0);
		} else {
			return ((getTotalParity() + fieldSize - (emptyTileRow + 1)) % 2 == 0);
		}
	}
	
	private int getTotalParity() {
		int parity = 0;
		for(int row = 0; row < fieldSize; row++) {
			for(int column = 0; column < fieldSize; column++) {
				if(!(field[row][column] instanceof EmptyTile)) {
					parity += getParityOfTile(row, column);
				}
			}
		}
		return parity;
	}
	
	private int getParityOfTile(int row, int column) {
		int parity = 0;
		int numberOfTiles = fieldSize * fieldSize;
		
		// index polička keby bolo 1 rozmerne pole
		int tileIndexVal = ((Fragment) field[row][column]).getIndex();
		
		// transformujem na 1 rozmerne indexy, neskôr prepočitam
		// i / fieldSize == row
		// i % fieldSize == column
		int index = row * fieldSize + column;
		for(int tmpIndex = index + 1; tmpIndex < numberOfTiles; tmpIndex++) {
			int tmpRow = tmpIndex / fieldSize;
			int tmpColumn = tmpIndex % fieldSize;
			if(field[tmpRow][tmpColumn] instanceof EmptyTile) {
				continue;
			}
			
			if(tileIndexVal > ((Fragment) field[tmpRow][tmpColumn]).getIndex()) {
				parity++;
			}
		}
		return parity;
	}
	
	/**
	 * FOR TESTING PURPOSE
	 */
	public void generateSolved() {
		List<Integer> indexes = new ArrayList<>();
		int numberOfFragments = fieldSize * fieldSize - 1;
		for(int i = 0; i < numberOfFragments; i++) {
			indexes.add(i);
		}
		
		// i / fieldSize == row
		// i % fieldSize == column
		for(int i = 0; i < numberOfFragments; i++) {
			field[i / fieldSize][i % fieldSize] = new Fragment(indexes.get(i));
		}
		field[fieldSize - 1][fieldSize - 1] = new EmptyTile();
	}
	
	public boolean moveFragment(int row, int column) {
		if(row >= fieldSize || column >= fieldSize || field[row][column] instanceof EmptyTile) {
			return false;
		}
		
		int[][] positions = {{row - 1, column}, {row, column - 1}, {row, column + 1}, {row + 1, column}};
		
		boolean moved = false;
		for(int[] position : positions) {
			int rowT = position[0];
			int columnT = position[1];
			if(canBeMoved(rowT, columnT)) {
				field[rowT][columnT] = field[row][column];
				field[row][column] = new EmptyTile();
				
				actionCount++;
				
				moved = true;
				break;
			}
		}
		
		if(isSolved()) {
			this.gameState = GameState.SOLVED;
		}
		return moved;
	}
	
	private boolean canBeMoved(int row, int column) {
		return row >= 0 && row < fieldSize && column >= 0 && column < fieldSize && field[row][column] instanceof EmptyTile;
	}
	
	private boolean isSolved() {
		int lastIndex = -1;
		
		if(!(field[fieldSize - 1][fieldSize - 1] instanceof EmptyTile)) {
			return false;
		}
		
		for(int i = 0; i < fieldSize; i++) {
			for(int j = 0; j < fieldSize; j++) {
				Tile tile = field[i][j];
				if(tile instanceof Fragment) {
					if(((Fragment) tile).getIndex() > lastIndex) {
						lastIndex = ((Fragment) tile).getIndex();
					} else {
						return false;
					}
				}
			}
		}
		
		return true;
	}
	
	public int calculateScore() {
		return (int) (Math.round((double) actionCount / (fieldSize * fieldSize) * 10.));
	}
	
	public Tile[][] getField() {
		return field;
	}
	
	public Tile getTile(int row, int column) {
		return field[row][column];
	}
	
	public int getFieldSize() {
		return fieldSize;
	}
	
	public GameState getGameState() {
		return gameState;
	}
	
	public int getActionCount() {
		return actionCount;
	}
	
	public void setActionCount(int actionCount) {
		this.actionCount = actionCount;
	}
	
	public String getGameGuide() {
		return gameGuide;
	}
}
