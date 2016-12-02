package models;

import java.util.ArrayList;

public class mapRow
{
	ArrayList<mapCell> columns;

	public mapRow()
	{
		columns = new ArrayList<mapCell>();
	}

	public mapCell getCell(int cellIndex)
	{
		return columns.get(cellIndex);
	}
}
