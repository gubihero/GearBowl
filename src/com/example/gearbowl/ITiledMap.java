package com.example.gearbowl;
//remake of ITiledMap interface that seemed to get left out of GLES2
public interface ITiledMap<T> {
	public int getTileColumns();
    public int getTileRows();

    public void onTileVisitedByPathFinder(final int pTileColumn, int pTileRow);

    public boolean isTileBlocked(final T pEntity, final int pTileColumn, final int pTileRow);

    public float getStepCost(final T pEntity, final int pFromTileColumn, final int pFromTileRow, final int pToTileColumn, final int pToTileRow);
}
