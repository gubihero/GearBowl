package com.example.gearbowl;

import java.util.ArrayList;
import java.util.Collections;

import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.util.algorithm.path.ICostFunction;
import org.andengine.util.algorithm.path.IPathFinderMap;
import org.andengine.util.algorithm.path.Path;
import org.andengine.util.algorithm.path.astar.IAStarHeuristic;
import org.andengine.util.algorithm.path.astar.ManhattanHeuristic;

import android.util.Log;


/**
 * @author Nicolas Gramlich
 * @since 23:16:17 - 16.08.2010
 */
public class OldAStarPathFinder<T>{
        // ===========================================================
        // Constants
        // ===========================================================

        // ===========================================================
        // Fields
        // ===========================================================

        private static final String TAG = "OldAStarPathFinder";
		private final ArrayList<Node> mVisitedNodes = new ArrayList<Node>();
        private final ArrayList<Node> mOpenNodes = new ArrayList<Node>();

        private final TMXTiledMap mTiledMap;
        private final int mMaxSearchDepth;

        private final Node[][] mNodes;
        private final boolean mAllowDiagonalMovement;

        private final IAStarHeuristic<T> mAStarHeuristic;
		private IPathFinderMap<T> mPathFinderMap;
		private ICostFunction<T> mCostFunction;

        // ===========================================================
        // Constructors
        // ===========================================================

        public OldAStarPathFinder(final TMXTiledMap pTiledMap, final int pMaxSearchDepth, final boolean pAllowDiagonalMovement, 
        		final IPathFinderMap<T> pPathFinderMap, final ICostFunction<T> pCostFunction) {
                this( pTiledMap, pMaxSearchDepth, pAllowDiagonalMovement, pPathFinderMap, new ManhattanHeuristic<T>(), pCostFunction);
        }

        public OldAStarPathFinder(final TMXTiledMap pTiledMap, final int pMaxSearchDepth, final boolean pAllowDiagonalMovement, final IPathFinderMap<T> pPathFinderMap,
        		final IAStarHeuristic<T> pAStarHeuristic, final ICostFunction<T> pCostFunction) {
                this.mAStarHeuristic = pAStarHeuristic;
                this.mTiledMap = pTiledMap;
                this.mPathFinderMap = pPathFinderMap;
                this.mMaxSearchDepth = pMaxSearchDepth;
                this.mAllowDiagonalMovement = pAllowDiagonalMovement;
                this.mCostFunction = pCostFunction;

                this.mNodes = new Node[pTiledMap.getTileRows()][pTiledMap.getTileColumns()];
                final Node[][] nodes = this.mNodes;
                for(int x = pTiledMap.getTileColumns() - 1; x >= 0; x--) {
                        for(int y = pTiledMap.getTileRows() - 1; y >= 0; y--) {
                                nodes[y][x] = new Node(x, y);
                        }
                }
        }

        // ===========================================================
        // Getter & Setter
        // ===========================================================

        // ===========================================================
        // Methods for/from SuperClass/Interfaces
        // ===========================================================


        public Path findPath(final int pFromTileColumn, final int pFromTileRow, final int pToTileColumn, final int pToTileRow, final T pEntity) {
        	return findPath(pFromTileColumn, pFromTileRow, pToTileColumn, pToTileRow, pEntity, Integer.MAX_VALUE);
        }
        
        public Path findPath(final int pFromTileColumn, final int pFromTileRow, final int pToTileColumn, final int pToTileRow, final T pEntity, final int pMaxCost) {
                if(this.mPathFinderMap.isBlocked(pToTileColumn, pToTileRow, pEntity)) {
                        return null;
                }

                /* Drag some fields to local variables. */
                final ArrayList<Node> openNodes = this.mOpenNodes;
                final ArrayList<Node> visitedNodes = this.mVisitedNodes;

                final Node[][] nodes = this.mNodes;
                final Node fromNode = nodes[pFromTileRow][pFromTileColumn];
                final Node toNode = nodes[pToTileRow][pToTileColumn];

                final IAStarHeuristic<T> aStarHeuristic = this.mAStarHeuristic;
                final boolean allowDiagonalMovement = this.mAllowDiagonalMovement;
                final int maxSearchDepth = this.mMaxSearchDepth;

                /* Initialize algorithm. */
                fromNode.mCost = 0;
                fromNode.mDepth = 0;
                toNode.mParent = null;

                visitedNodes.clear();

                openNodes.clear();
                openNodes.add(fromNode);

                int currentDepth = 0;
                
                while(currentDepth < maxSearchDepth &&!openNodes.isEmpty()) {
                	
                    /* The first Node in the open list is the one with the lowest cost. */
                    final Node current = openNodes.remove(0);
                    if(current == toNode) {
                            break;
                    }

                    visitedNodes.add(current);

                    /* Loop over all neighbors of this tile. */
                    for(int dX = -1; dX <= 1; dX++) {
                        for(int dY = -1; dY <= 1; dY++) {
                            if((dX == 0) && (dY == 0)) {
                                    continue;
                            }

                            if(!allowDiagonalMovement) {
                                    if((dX != 0) && (dY != 0)) {
                                            continue;
                                    }
                            }

                            final int neighborTileColumn = dX + current.mTileColumn;
                            final int neighborTileRow = dY + current.mTileRow;

                            if(!this.isTileBlocked(pEntity, pFromTileColumn, pFromTileRow, neighborTileColumn, neighborTileRow)) {
                                final float neighborCost = current.mCost + mCostFunction.getCost(mPathFinderMap, current.mTileColumn, current.mTileRow, neighborTileColumn, neighborTileRow, pEntity);
                                final Node neighbor = nodes[neighborTileRow][neighborTileColumn];

                                /* Re-evaluate if there is a better path. */
                                if(neighborCost < neighbor.mCost) {
                                    // TODO Is this ever possible with AStar ??
                                    if(openNodes.contains(neighbor)) {
                                            openNodes.remove(neighbor);
                                    }
                                    if(visitedNodes.contains(neighbor)) {
                                            visitedNodes.remove(neighbor);
                                    }
                                }

                                if(!openNodes.contains(neighbor) && !(visitedNodes.contains(neighbor))) {
                                    neighbor.mCost = neighborCost;
                                    if(neighbor.mCost <= pMaxCost) {
                                            neighbor.mExpectedRestCost = aStarHeuristic.getExpectedRestCost(mPathFinderMap, pEntity, neighborTileColumn, neighborTileRow, pToTileColumn, pToTileRow);
                                            currentDepth = Math.max(currentDepth, neighbor.setParent(current));
                                            openNodes.add(neighbor);

                                            /* Ensure always the node with the lowest cost+heuristic
                                             * will be used next, simply by sorting. */
                                            Collections.sort(openNodes);
                                    }
                                }
                            }
                        }
                    }
                }

        		
                /* Check if a path was found. */
                if(toNode.mParent == null) {
                        return null;
                }

        		/* Calculate path length. */
        		int length = 1;
                Node tmp = nodes[pToTileRow][pToTileColumn];
        		while(tmp != fromNode) {
        			tmp = tmp.mParent;
        			length++;
        		}

        		/* Traceback path. */
        		final Path path = new Path(length);
        		int index = length - 1;
                tmp = nodes[pToTileRow][pToTileColumn];
        		while(tmp != fromNode) {
        			path.set(index, tmp.mTileColumn, tmp.mTileRow);
        			tmp = tmp.mParent;
        			index--;
        		}
        		path.set(0, pFromTileColumn , pFromTileRow);
                return path;
        }

        // ===========================================================
        // Methods
        // ===========================================================

        protected boolean isTileBlocked(final T pEntity, final int pFromTileColumn, final int pFromTileRow, final int pToTileColumn, final int pToTileRow) {
                if((pToTileColumn < 0) || (pToTileRow < 0) || (pToTileColumn >= this.mTiledMap.getTileColumns()) || (pToTileRow >= this.mTiledMap.getTileRows())) {
                        return true;
                } else if((pFromTileColumn == pToTileColumn) && (pFromTileRow == pToTileRow)) {
                        return true;
                }
                return this.mPathFinderMap.isBlocked(pToTileColumn, pToTileRow, pEntity);
        }

        // ===========================================================
        // Inner and Anonymous Classes
        // ===========================================================

        private static class Node implements Comparable<Node> {
                // ===========================================================
                // Constants
                // ===========================================================

                // ===========================================================
                // Fields
                // ===========================================================

                Node mParent;
                int mDepth;

                final int mTileColumn;
                final int mTileRow;

                float mCost;
                float mExpectedRestCost;

                // ===========================================================
                // Constructors
                // ===========================================================

                public Node(final int pTileColumn, final int pTileRow) {
                        this.mTileColumn = pTileColumn;
                        this.mTileRow = pTileRow;
                }

                // ===========================================================
                // Getter & Setter
                // ===========================================================

                public int setParent(final Node parent) {
                        this.mDepth = parent.mDepth + 1;
                        this.mParent = parent;

                        return this.mDepth;
                }

                // ===========================================================
                // Methods for/from SuperClass/Interfaces
                // ===========================================================

                @Override
                public int compareTo(final Node pOther) {
                        final float totalCost = this.mExpectedRestCost + this.mCost;
                        final float totalCostOther = pOther.mExpectedRestCost + pOther.mCost;

                        if (totalCost < totalCostOther) {
                                return -1;
                        } else if (totalCost > totalCostOther) {
                                return 1;
                        } else {
                                return 0;
                        }
                }

                // ===========================================================
                // Methods
                // ===========================================================

                // ===========================================================
                // Inner and Anonymous Classes
                // ===========================================================
        }
}