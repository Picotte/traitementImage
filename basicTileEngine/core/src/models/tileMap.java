package models;

import java.util.ArrayList;

public class tileMap implements tileBasedMap {
	ArrayList<mapRow> rows = new ArrayList<mapRow>();
	int mapWidth = 50;
	int mapHeight = 50;
	int cost_deplacement = 0;

	public static final int GRASS = 2;
	public static final int WOOD = 3;
	public static final int WATER = 0;

	public static final int SOLDIER = 4;
	public static final int DESTINATION = 5;

	public int[][] terrain = new int[mapWidth][mapHeight];
	private int[][] units = new int[mapWidth][mapHeight];

	private boolean[][] visited = new boolean[mapWidth][mapHeight];

	public tileMap() {
		super();

		for (int y = 0; y < mapHeight; y++) {

			mapRow currentRow = new mapRow();

			for (int x = 0; x < mapWidth; x++) {
				currentRow.columns.add(new mapCell(GRASS));
			}

			rows.add(currentRow);

		}

		//generateTest(5, 12);
	}

	public mapRow getRow(int rowIndex) {
		return rows.get(rowIndex);
	}

	public void generateTest(int posX, int posY, int posX2, int posY2) {
		// Début de la création

		for (int y = 0; y < mapHeight; y++) {


			for (int x = 0; x < mapWidth; x++) {
				if ((x == 0)||(y == 0) || (x == 18) || (y == 24) ||(x > 2 && x < 12 && y == 7) || (y > 2 && y < 12 && x == 12) || (x > 7 && x < 15 && y == 17) || (x > 7 && x < 15 && y == 22) || (y > 16 && y < 23 && x == 7)) {
                    rows.get(x).columns.get(y).setTileID(WATER);
                    terrain[x][y] = rows.get(x).columns.get(y).getTileID();
                }
				else if((x > 4 && x < 15 && y == 13) || (y > 5 && y < 14 && x == 15) || (y > 7 && y < 16 && x == 4))
				{
					rows.get(x).columns.get(y).setTileID(WOOD);
					terrain[x][y] = rows.get(x).columns.get(y).getTileID();
				}
                else {
                    rows.get(x).columns.get(y).setTileID(GRASS);
                    terrain[x][y] = rows.get(x).columns.get(y).getTileID();

                }
            }
		}
		rows.get(posY2).columns.get(posX2).setTileID(SOLDIER);
		rows.get(posY).columns.get(posX).setTileID(DESTINATION);


		// Fin de la création


	}
	public void createPath(int posX, int posY, int posX2, int posY2)
	{

	}

	public int getTerrain(int x, int y)
	{
		return terrain[x][y];
	}

	@Override
	public int getWidth()
	{
		return mapWidth;
	}

	@Override
	public int getHeight() {
		return mapHeight;
	}

	@Override
	public boolean isBlocked(int y, int x) {
		if (getTerrain(x,y) != 0) {
            return false;
		} else
		{
			return true;
		}
	}

	@Override
	public float getPrixDeplacement(int startx, int starty, int targety, int targetx)
	{
		if (getTerrain(targetx,targety) != 3)
		{
			return this.cost_deplacement+10;
		}
		else
		{
			return this.cost_deplacement+20;
		}
	}

	@Override
	public void pathFinderVisit(int x, int y)
	{
		visited[x][y] = true;
	}
}
