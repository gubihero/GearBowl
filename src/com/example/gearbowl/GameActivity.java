package com.example.gearbowl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.PathModifier;
import org.andengine.entity.modifier.PathModifier.IPathModifierListener;
import org.andengine.entity.modifier.PathModifier.Path;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXLoader.ITMXTilePropertiesListener;
import org.andengine.extension.tmx.TMXProperties;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTileProperty;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.extension.tmx.util.exception.TMXLoadException;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.bitmap.AssetBitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.Constants;
import org.andengine.util.algorithm.path.ICostFunction;
import org.andengine.util.algorithm.path.IPathFinderMap;
import org.andengine.util.debug.Debug;
import org.andengine.util.modifier.IModifier;

import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.widget.Toast;


public class GameActivity extends SimpleBaseGameActivity implements IOnSceneTouchListener {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 480;
	private static final int CAMERA_HEIGHT = 320;
	
	private static final int TILE_WIDTH = 32;
	//speed is time so lower time, faster speed
	private static final float mSpeed = (float) 0.3;

	// ===========================================================
	// Fields
	// ===========================================================
	private Scene mScene;
	private BoundCamera mCamera;
	
	//for loading the player animated sprites
	// speedbot texture
	private ITexture mSpeedBotRedTexture;
	private TiledTextureRegion mSpeedBotRedTextureRegion;
	private ITexture mSpeedBotBlueTexture;
	private TiledTextureRegion mSpeedBotBlueTextureRegion;
	private ITexture mSpeedBotGreenTexture;
	private TiledTextureRegion mSpeedBotGreenTextureRegion;
	private ITexture mSpeedBotYellowTexture;
	private TiledTextureRegion mSpeedBotYellowTextureRegion;
	//smashbot texture
	private ITexture mSmashBotRedTexture;
	private TiledTextureRegion mSmashBotRedTextureRegion;
	private ITexture mSmashBotBlueTexture;
	private TiledTextureRegion mSmashBotBlueTextureRegion;
	private ITexture mSmashBotGreenTexture;
	private TiledTextureRegion mSmashBotGreenTextureRegion;
	private ITexture mSmashBotYellowTexture;
	private TiledTextureRegion mSmashBotYellowTextureRegion;
	//crushbot texture
	private ITexture mCrushBotRedTexture;
	private TiledTextureRegion mCrushBotRedTextureRegion;
	private ITexture mCrushBotBlueTexture;
	private TiledTextureRegion mCrushBotBlueTextureRegion;
	private ITexture mCrushBotGreenTexture;
	private TiledTextureRegion mCrushBotGreenTextureRegion;
	private ITexture mCrushBotYellowTexture;
	private TiledTextureRegion mCrushBotYellowTextureRegion;
	//normbot texture
	private ITexture mNormBotRedTexture;
	private TiledTextureRegion mNormBotRedTextureRegion;
	private ITexture mNormBotBlueTexture;
	private TiledTextureRegion mNormBotBlueTextureRegion;
	private ITexture mNormBotGreenTexture;
	private TiledTextureRegion mNormBotGreenTextureRegion;
	private ITexture mNormBotYellowTexture;
	private TiledTextureRegion mNormBotYellowTextureRegion;
	// rest of the stuff
	private TMXTiledMap mTMXTiledMap;
	protected int mCactusCount;
	private AnimatedSprite mPlayer;
	
	//specifically added to try and catch user touch
	private TMXTile mCurrentTile;
	private TMXTile mSelectedTile;
	private TMXLayer mTmxLayer;
	
	//astar
	private org.andengine.util.algorithm.path.Path mA_path;
	private OldAStarPathFinder<TMXLayer> mFinder;
	private Path mCurrentPath;
	private boolean mIsWalking;
	private int mWaypointIndex;
	private PathModifier mMoveModifier;
	private IEntityModifier mPathTemp;
	
	
	
	//textures for hud buttons
	private BuildableBitmapTextureAtlas mBuildableBitmapTextureAtlas;
	
	private ITextureRegion mRightNormalTextureRegion;
	private ITextureRegion mLeftNormalTextureRegion;
	private ITextureRegion mHitNormalTextureRegion;
	private ITextureRegion mHitSelectedTextureRegion;
	private ITextureRegion mMoveNormalTextureRegion;
	private ITextureRegion mMoveSelectedTextureRegion;
	private ITextureRegion mEndTurnNormalTextureRegion;
	private ITextureRegion mEndTurnSelectedTextureRegion;
	
	//currentplayer
	private int mCurrAP;
	private int mCurrPlayerNum;
	private String mCurrentName;
	
	private Text mAP;
	private Text mPlayerNum;
	private Text mPlayerName;
	
	//current team members
	private int mCurrTeamNum;
	
	private Text mTeamNum;
	
	//game members that will be used to set up
	//interactive tiles array
	//get wall tiles
	private int[] mTiles = new int[900];
	private int mTileAPCost = 10;
	private int mHitAPCost = 20;
	
	private int mTurn;
	
	private ArrayList<Integer> mWarpTiles = new ArrayList<Integer>();
	//random for picking random warp
	private Random mRand = new Random();
	
	private boolean[] mActiveTeamList = new boolean[4];
	
	// move mode flag
	private boolean mMoveMode = false;
	private boolean mSelectedTileToMove = false;
	// hit mode flag
	private boolean mHitMode = false;
	
	private Rectangle mSelectedTileRectangle;
	
	private Rectangle mHitNorthTileRectangle;
	private Rectangle mHitSouthTileRectangle;
	private Rectangle mHitEastTileRectangle;
	private Rectangle mHitWestTileRectangle;
	
	private boolean mNorthHittable = false;
	private boolean mSouthHittable = false;
	private boolean mEastHittable = false;
	private boolean mWestHittable = false;
	
	//arrays to hold player sprites
	//and player info
	private ArrayList <ArrayList<AnimatedSprite>> mTeamSprites;
	//positions of players
	private int[] mPlayerMap = new int[900];
	
	//ap's of current team
	private int[] mCurrTeamAPList = new int[9];

	// ===========================================================
	// Constructors
	// ===========================================================
	// HUD Text and HUD
    private BitmapTextureAtlas mFontTexture;
    private Font mFont;
    HUD mHud = new HUD();
    
    // Game Setup
    
    public void setupGame() {
    	//set up arrays to
    	//set up active team list, so that end turn will only toggle through active teams
    	mTurn = 0;
    	mCurrTeamNum = 0;
    	mTeamSprites = new ArrayList <ArrayList <AnimatedSprite>>();
    	boolean setCurrTeamNum = false;
    	for(int i=0;i<4;i++){
    		if (Game.get(getApplicationContext()).getTeamStatus(i) == 1) {
    			mActiveTeamList[i] = true;
    			if(!setCurrTeamNum){ 
    				//set current team number to the first active team
    				mCurrTeamNum = i;
    				mTeamNum.setText("TEAM: "+Integer.toString(i+1));
    				setCurrTeamNum = true;
    			}
    		} else {
    			mActiveTeamList[i] = false;
    		}
    		//make arrays to put on team list
    		mTeamSprites.add(new ArrayList<AnimatedSprite>());
    		for(int j = 0; j<9; j++) {
    			mTeamSprites.get(i).add(null);
    		}
    	}
    	for(int j = 0; j<9; j++) {
			mCurrTeamAPList[j] = 0;
		}
    	for (int p = 0;p<900;p++){
    		mPlayerMap[p] = -1;
    	}
    }
    //makes a new animated player sprite, adds to team sprite lists, and places on board,sets as current player
    //and attaches chase camera
    public void makePlayerSprite(int team, int player) {
    	final float centerX = CAMERA_WIDTH / 2;
		final float centerY = CAMERA_HEIGHT / 2;
    	String newPlayerType = Game.get(getApplicationContext()).getTeamPlayer(team, player).getType();
    	//pick the right colored image for the players team
    	//pick image according to type 
    	if (newPlayerType.compareTo("Empty") == 0) {
    		mTeamSprites.get(team).add(player, null);
    	} else if (newPlayerType.compareTo("speedbot") == 0) {
    		TiledTextureRegion ptexture = this.mSpeedBotRedTextureRegion;
    		if(team==0){
    			ptexture = this.mSpeedBotRedTextureRegion;
    		} else if (team == 1) {
    			ptexture = this.mSpeedBotBlueTextureRegion;
    		} else if (team == 2) {
    			ptexture = this.mSpeedBotGreenTextureRegion;
    		} else if (team == 3) {
    			ptexture = this.mSpeedBotYellowTextureRegion;
    		} else {
    			Log.d("makePlayerSprite","wrong team num");
    		}
    		AnimatedSprite newPlayerSprite = new AnimatedSprite(centerX, centerY,
    				ptexture, this.getVertexBufferObjectManager());
    		newPlayerSprite.setOffsetCenterY(0);
    		int tilewidth = mTMXTiledMap.getTileWidth();
    		TMXTile warpStart = warp();
    		
    		newPlayerSprite.setPosition(mTmxLayer.getTileX(warpStart.getTileColumn())+(tilewidth/2), mTmxLayer.getTileY(warpStart.getTileRow()));
    		this.mCamera.setChaseEntity(newPlayerSprite);
    		mTeamSprites.get(team).add(player, newPlayerSprite);
    		mPlayer = newPlayerSprite;
    		mScene.attachChild(mPlayer);
    	} else if (newPlayerType.compareTo("smashbot") == 0) {
    		TiledTextureRegion ptexture = this.mSmashBotRedTextureRegion;
    		if(team==0){
    			ptexture = this.mSmashBotRedTextureRegion;
    		} else if (team == 1) {
    			ptexture = this.mSmashBotBlueTextureRegion;
    		} else if (team == 2) {
    			ptexture = this.mSmashBotGreenTextureRegion;
    		} else if (team == 3) {
    			ptexture = this.mSmashBotYellowTextureRegion;
    		} else {
    			Log.d("makePlayerSprite","wrong team num");
    		}
    		AnimatedSprite newPlayerSprite = new AnimatedSprite(centerX, centerY,
    				ptexture, this.getVertexBufferObjectManager());
    		newPlayerSprite.setOffsetCenterY(0);
    		int tilewidth = mTMXTiledMap.getTileWidth();
    		TMXTile warpStart = warp();
    		
    		newPlayerSprite.setPosition(mTmxLayer.getTileX(warpStart.getTileColumn())+(tilewidth/2), mTmxLayer.getTileY(warpStart.getTileRow()));
    		this.mCamera.setChaseEntity(newPlayerSprite);
    		mTeamSprites.get(team).add(player, newPlayerSprite);
    		mPlayer = newPlayerSprite;
    		mScene.attachChild(mPlayer);
    	} else if (newPlayerType.compareTo("normbot") == 0) {
    		TiledTextureRegion ptexture = this.mNormBotRedTextureRegion;
    		if(team==0){
    			ptexture = this.mNormBotRedTextureRegion;
    		} else if (team == 1) {
    			ptexture = this.mNormBotBlueTextureRegion;
    		} else if (team == 2) {
    			ptexture = this.mNormBotGreenTextureRegion;
    		} else if (team == 3) {
    			ptexture = this.mNormBotYellowTextureRegion;
    		} else {
    			Log.d("makePlayerSprite","wrong team num");
    		}
    		AnimatedSprite newPlayerSprite = new AnimatedSprite(centerX, centerY,
    				ptexture, this.getVertexBufferObjectManager());
    		newPlayerSprite.setOffsetCenterY(0);
    		int tilewidth = mTMXTiledMap.getTileWidth();
    		TMXTile warpStart = warp();
    		
    		newPlayerSprite.setPosition(mTmxLayer.getTileX(warpStart.getTileColumn())+(tilewidth/2), mTmxLayer.getTileY(warpStart.getTileRow()));
    		this.mCamera.setChaseEntity(newPlayerSprite);
    		mTeamSprites.get(team).add(player, newPlayerSprite);
    		mPlayer = newPlayerSprite;
    		mScene.attachChild(mPlayer);
    	} else if (newPlayerType.compareTo("crushbot") == 0) {
    		TiledTextureRegion ptexture = this.mCrushBotRedTextureRegion;
    		if(team==0){
    			ptexture = this.mCrushBotRedTextureRegion;
    		} else if (team == 1) {
    			ptexture = this.mCrushBotBlueTextureRegion;
    		} else if (team == 2) {
    			ptexture = this.mCrushBotGreenTextureRegion;
    		} else if (team == 3) {
    			ptexture = this.mCrushBotYellowTextureRegion;
    		} else {
    			Log.d("makePlayerSprite","wrong team num");
    		}
    		AnimatedSprite newPlayerSprite = new AnimatedSprite(centerX, centerY,
    				ptexture, this.getVertexBufferObjectManager());
    		newPlayerSprite.setOffsetCenterY(0);
    		int tilewidth = mTMXTiledMap.getTileWidth();
    		TMXTile warpStart = warp();
    		
    		newPlayerSprite.setPosition(mTmxLayer.getTileX(warpStart.getTileColumn())+(tilewidth/2), mTmxLayer.getTileY(warpStart.getTileRow()));
    		this.mCamera.setChaseEntity(newPlayerSprite);
    		mTeamSprites.get(team).add(player, newPlayerSprite);
    		mPlayer = newPlayerSprite;
    		mScene.attachChild(mPlayer);
    	} else {
    		Log.d("makePlayerSprite","incorrect type "+newPlayerType);
    	}
    }
    
    public TMXTile warp() {
    	int mapsizeintiles = 30;
    	int randwarp = mWarpTiles.get(mRand.nextInt(mWarpTiles.size()));
    	Log.d("warp that tile","randwarpnum:"+Integer.toString(randwarp));
    	TMXTile warpTile = mTmxLayer.getTMXTile(randwarp/mapsizeintiles, randwarp%mapsizeintiles);
    	return warpTile;
    }
    
    public void getTeamAP(int team){
    	Team mteam = Game.get(getApplicationContext()).getTeam(team);
    	for (int i = 0; i<9; i++) {
    		mCurrTeamAPList[i] = mteam.getPlayerAtPosition(i).getAP();
    	}
    }
    //erase old position, put in new position for that player
    public void updatePlayerPosition(int team,int player){
    	for(int i=0;i<900;i++){
    		if(mPlayerMap[i] == 10*team+player){
    			mPlayerMap[i] = -1;
    		}
    	}
    	mPlayerMap[mCurrentTile.getTileColumn()*30+mCurrentTile.getTileRow()] = 10*team+player;
    }
    
    public void setHitTiles(boolean on) {
    	if (on) {
    		//north
    		mHitNorthTileRectangle.setPosition(mTmxLayer.getTileX(mCurrentTile.getTileColumn()), mTmxLayer.getTileY(mCurrentTile.getTileRow()-1));
    		if(mPlayerMap[mCurrentTile.getTileColumn()*30+(mCurrentTile.getTileRow()-1)]>=0 && 
    				mPlayerMap[mCurrentTile.getTileColumn()*30+(mCurrentTile.getTileRow()-1)]/10 != mCurrTeamNum){
    			mHitNorthTileRectangle.setColor(0, 1, 0, 0.25f);
    			mNorthHittable = true;
    		} else {
    			mHitNorthTileRectangle.setColor(1, 0, 0, 0.25f);
    			mNorthHittable = false;
    		}
    		//south
    		mHitSouthTileRectangle.setPosition(mTmxLayer.getTileX(mCurrentTile.getTileColumn()), mTmxLayer.getTileY(mCurrentTile.getTileRow()+1));
    		if(mPlayerMap[mCurrentTile.getTileColumn()*30+(mCurrentTile.getTileRow()+1)]>=0 && 
			mPlayerMap[mCurrentTile.getTileColumn()*30+(mCurrentTile.getTileRow()+1)]/10 != mCurrTeamNum){
    			mHitSouthTileRectangle.setColor(0, 1, 0, 0.25f);
    			mSouthHittable = true;
    		} else {
    			mHitSouthTileRectangle.setColor(1, 0, 0, 0.25f);
    			mSouthHittable = false;
    		}
    		//east
    		mHitEastTileRectangle.setPosition(mTmxLayer.getTileX(mCurrentTile.getTileColumn()-1), mTmxLayer.getTileY(mCurrentTile.getTileRow()));
    		if(mPlayerMap[(mCurrentTile.getTileColumn()-1)*30+(mCurrentTile.getTileRow())]>=0 && 
			mPlayerMap[(mCurrentTile.getTileColumn()-1)*30+(mCurrentTile.getTileRow())]/10 != mCurrTeamNum){
    			mHitEastTileRectangle.setColor(0, 1, 0, 0.25f);
    			mEastHittable = true;
    		} else {
    			mHitEastTileRectangle.setColor(1, 0, 0, 0.25f);
    			mEastHittable = false;
    		}
    		//west
    		mHitWestTileRectangle.setPosition(mTmxLayer.getTileX(mCurrentTile.getTileColumn()+1), mTmxLayer.getTileY(mCurrentTile.getTileRow()));
    		if(mPlayerMap[(mCurrentTile.getTileColumn()+1)*30+(mCurrentTile.getTileRow())]>=0 && 
			mPlayerMap[(mCurrentTile.getTileColumn()+1)*30+(mCurrentTile.getTileRow())]/10 != mCurrTeamNum){
    			mHitWestTileRectangle.setColor(0, 1, 0, 0.25f);
    			mWestHittable = true;
    		} else {
    			mHitWestTileRectangle.setColor(1, 0, 0, 0.25f);
    			mWestHittable = false;
    		}
    		
    	} else {
    		mHitNorthTileRectangle.setColor(1, 0, 0, 0.0f);
    		mHitSouthTileRectangle.setColor(1, 0, 0, 0.0f);
    		mHitEastTileRectangle.setColor(1, 0, 0, 0.0f);
    		mHitWestTileRectangle.setColor(1, 0, 0, 0.0f);
    		
    		mNorthHittable = false;
    		mSouthHittable = false;
    		mEastHittable = false;
    		mWestHittable = false;
    	}
    	
    }
    
    public void hit(int team, int player, int direction) {
    	//1 north, 2 south, 3 east, 4 west
    	mCurrTeamAPList[mCurrPlayerNum] = mCurrTeamAPList[mCurrPlayerNum]-10;
        mAP.setText("AP: "+Integer.toString(mCurrTeamAPList[mCurrPlayerNum]));
    	Log.d("hit","team: "+Integer.toString(team)+"player: "+Integer.toString(player));
    	Player agressor = Game.get(getApplicationContext()).getTeamPlayer(mCurrTeamNum, mCurrPlayerNum);
    	Player victim = Game.get(getApplicationContext()).getTeamPlayer(team, player);
    	//animate
    	final float[] playerFootCordinates = mPlayer.convertLocalCoordinatesToSceneCoordinates(16, 1);
    	mPlayer.clearEntityModifiers();
    	float oX = playerFootCordinates[Constants.VERTEX_INDEX_X];
    	float oY = playerFootCordinates[Constants.VERTEX_INDEX_Y];
    	      
    	Path mHitPath = new Path(3).to(oX,oY).to(oX,oY+16).to(oX,oY-1);
    	if (direction == 1) {
        	mHitPath = new Path(3).to(oX,oY).to(oX,oY+16).to(oX,oY-1);
        	 mPlayer.animate(new long[]{200, 200, 200}, 0, 2, true);
    	} else if (direction == 2) {
    		mHitPath = new Path(3).to(oX,oY).to(oX,oY-16).to(oX,oY-1);
    		mPlayer.animate(new long[]{200, 200, 200}, 6, 8, true);
    	} else if (direction == 3) {
    		mHitPath = new Path(3).to(oX,oY).to(oX-16,oY).to(oX,oY-1);
    		mPlayer.animate(new long[]{200, 200, 200}, 9, 11, true);
    	} else if (direction == 4) {
    		mHitPath = new Path(3).to(oX,oY).to(oX+16,oY).to(oX,oY-1);
    		mPlayer.animate(new long[]{200, 200, 200}, 3, 5, true);
    	} else {
    		//should never get here
    	}
        	PathModifier mHitPathModifier = new PathModifier(.5f, mHitPath, new IEntityModifierListener() {
                
                public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
                                       
                }
                               
                public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
                               
                }
            }, new IPathModifierListener() {
                               
                public void onPathWaypointStarted(final PathModifier pPathModifier, final IEntity pEntity,  int pWaypointIndex) {
                	                   
            }
                
                public void onPathWaypointFinished(PathModifier pPathModifier, IEntity pEntity,
                    int pWaypointIndex) {
                                       
                }
                               
                public void onPathStarted(PathModifier pPathModifier, IEntity pEntity) {
         
                }
                               
                public void onPathFinished(PathModifier pPathModifier, IEntity pEntity) {
                        //Stops the animation of the player once it stops
                        mPlayer.stopAnimation();
                        // turn off move mode
                }
            });
           
        	
            
        	mPlayer.registerEntityModifier(mHitPathModifier);
        	
        	
    	
    	
    	//do calculations
    	//did it connect?  a.agl - v.dge
    	//how hard did it hit? a.str - v.tgh
    	int connection = mRand.nextInt(agressor.getAgility()) - mRand.nextInt(victim.getDodge());
    	int hitStrength = mRand.nextInt(agressor.getStrength()) - mRand.nextInt(victim.getToughness());
    	if (connection > -20) {
    		//hit connected
    		if(hitStrength >= 10){
    			//victim dies
    			Log.d("hit","victim died");
    			mTeamSprites.get(team).get(player).setVisible(false);
    			mTeamSprites.get(team).remove(player);
    			mTeamSprites.get(team).add(player, null);
    			for(int i=0;i<900;i++){
    	    		if(mPlayerMap[i] == 10*team+player){
    	    			mPlayerMap[i] = -1;
    	    		}
    	    	}
    			
    			
    		//} else if (hitStrength <= -10) {
    			//aggressor dies
    			//Log.d("hit","agressor died");
    		//} else if (hitStrength > 10) {
    			//victim stunned
    			//Log.d("hit","victim stunned");
    		//} else if (hitStrength < -10) {
    			//aggressor stunned
    			//Log.d("hit","agressor stunned");
    		} else {
    			//nothing happened
    		}
    	} else {
    		//hit missed
    		//nothing happens
    	}
    	
    	Log.d("hit","dodgeStrength:"+Integer.toString(connection));
    	Log.d("hit","hitStrength:"+Integer.toString(hitStrength));
    	//no effect, stun, kill
    }
	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public EngineOptions onCreateEngineOptions() {

		this.mCamera = new BoundCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		this.mCamera.setBoundsEnabled(false);
		
		
		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera);
	}

	@Override
	public void onCreateResources() throws IOException {
		//make all diff player textures
		//speedbot texture
		this.mSpeedBotRedTexture = new AssetBitmapTexture(this.getTextureManager(), this.getAssets(), "gfx/speedbotfullred.png", TextureOptions.DEFAULT);
		this.mSpeedBotRedTextureRegion = TextureRegionFactory.extractTiledFromTexture(this.mSpeedBotRedTexture, 3, 8);
		this.mSpeedBotRedTexture.load();
		this.mSpeedBotBlueTexture = new AssetBitmapTexture(this.getTextureManager(), this.getAssets(), "gfx/speedbotfullblue.png", TextureOptions.DEFAULT);
		this.mSpeedBotBlueTextureRegion = TextureRegionFactory.extractTiledFromTexture(this.mSpeedBotBlueTexture, 3, 8);
		this.mSpeedBotBlueTexture.load();
		this.mSpeedBotGreenTexture = new AssetBitmapTexture(this.getTextureManager(), this.getAssets(), "gfx/speedbotfullgreen.png", TextureOptions.DEFAULT);
		this.mSpeedBotGreenTextureRegion = TextureRegionFactory.extractTiledFromTexture(this.mSpeedBotGreenTexture, 3, 8);
		this.mSpeedBotGreenTexture.load();
		this.mSpeedBotYellowTexture = new AssetBitmapTexture(this.getTextureManager(), this.getAssets(), "gfx/speedbotfullyellow.png", TextureOptions.DEFAULT);
		this.mSpeedBotYellowTextureRegion = TextureRegionFactory.extractTiledFromTexture(this.mSpeedBotYellowTexture, 3, 8);
		this.mSpeedBotYellowTexture.load();
		//smashbot texture
		this.mSmashBotRedTexture = new AssetBitmapTexture(this.getTextureManager(), this.getAssets(), "gfx/smashbotfullred.png", TextureOptions.DEFAULT);
		this.mSmashBotRedTextureRegion = TextureRegionFactory.extractTiledFromTexture(this.mSmashBotRedTexture, 3, 8);
		this.mSmashBotRedTexture.load();
		this.mSmashBotBlueTexture = new AssetBitmapTexture(this.getTextureManager(), this.getAssets(), "gfx/smashbotfullblue.png", TextureOptions.DEFAULT);
		this.mSmashBotBlueTextureRegion = TextureRegionFactory.extractTiledFromTexture(this.mSmashBotBlueTexture, 3, 8);
		this.mSmashBotBlueTexture.load();
		this.mSmashBotGreenTexture = new AssetBitmapTexture(this.getTextureManager(), this.getAssets(), "gfx/smashbotfullgreen.png", TextureOptions.DEFAULT);
		this.mSmashBotGreenTextureRegion = TextureRegionFactory.extractTiledFromTexture(this.mSmashBotGreenTexture, 3, 8);
		this.mSmashBotGreenTexture.load();
		this.mSmashBotYellowTexture = new AssetBitmapTexture(this.getTextureManager(), this.getAssets(), "gfx/smashbotfullyellow.png", TextureOptions.DEFAULT);
		this.mSmashBotYellowTextureRegion = TextureRegionFactory.extractTiledFromTexture(this.mSmashBotYellowTexture, 3, 8);
		this.mSmashBotYellowTexture.load();
		//crushbot texture
		this.mCrushBotRedTexture = new AssetBitmapTexture(this.getTextureManager(), this.getAssets(), "gfx/crushbotfullred.png", TextureOptions.DEFAULT);
		this.mCrushBotRedTextureRegion = TextureRegionFactory.extractTiledFromTexture(this.mCrushBotRedTexture, 3, 8);
		this.mCrushBotRedTexture.load();
		this.mCrushBotBlueTexture = new AssetBitmapTexture(this.getTextureManager(), this.getAssets(), "gfx/crushbotfullblue.png", TextureOptions.DEFAULT);
		this.mCrushBotBlueTextureRegion = TextureRegionFactory.extractTiledFromTexture(this.mCrushBotBlueTexture, 3, 8);
		this.mCrushBotBlueTexture.load();
		this.mCrushBotGreenTexture = new AssetBitmapTexture(this.getTextureManager(), this.getAssets(), "gfx/crushbotfullgreen.png", TextureOptions.DEFAULT);
		this.mCrushBotGreenTextureRegion = TextureRegionFactory.extractTiledFromTexture(this.mCrushBotGreenTexture, 3, 8);
		this.mCrushBotGreenTexture.load();
		this.mCrushBotYellowTexture = new AssetBitmapTexture(this.getTextureManager(), this.getAssets(), "gfx/crushbotfullyellow.png", TextureOptions.DEFAULT);
		this.mCrushBotYellowTextureRegion = TextureRegionFactory.extractTiledFromTexture(this.mCrushBotYellowTexture, 3, 8);
		this.mCrushBotYellowTexture.load();
		//normbot texture
		this.mNormBotRedTexture = new AssetBitmapTexture(this.getTextureManager(), this.getAssets(), "gfx/normbotfullred.png", TextureOptions.DEFAULT);
		this.mNormBotRedTextureRegion = TextureRegionFactory.extractTiledFromTexture(this.mNormBotRedTexture, 3, 8);
		this.mNormBotRedTexture.load();
		this.mNormBotBlueTexture = new AssetBitmapTexture(this.getTextureManager(), this.getAssets(), "gfx/normbotfullblue.png", TextureOptions.DEFAULT);
		this.mNormBotBlueTextureRegion = TextureRegionFactory.extractTiledFromTexture(this.mNormBotBlueTexture, 3, 8);
		this.mNormBotBlueTexture.load();
		this.mNormBotGreenTexture = new AssetBitmapTexture(this.getTextureManager(), this.getAssets(), "gfx/normbotfullgreen.png", TextureOptions.DEFAULT);
		this.mNormBotGreenTextureRegion = TextureRegionFactory.extractTiledFromTexture(this.mNormBotGreenTexture, 3, 8);
		this.mNormBotGreenTexture.load();
		this.mNormBotYellowTexture = new AssetBitmapTexture(this.getTextureManager(), this.getAssets(), "gfx/normbotfullyellow.png", TextureOptions.DEFAULT);
		this.mNormBotYellowTextureRegion = TextureRegionFactory.extractTiledFromTexture(this.mNormBotYellowTexture, 3, 8);
		this.mNormBotYellowTexture.load();
		
		
		// HUD text
        this.mFontTexture = new BitmapTextureAtlas(null, 128, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        this.mFont = new Font(null, this.mFontTexture, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 22, true, Color.WHITE);
        this.mEngine.getTextureManager().loadTexture(this.mFontTexture);
        this.mEngine.getFontManager().loadFont(this.mFont);
        
        //HUD Button
        this.mBuildableBitmapTextureAtlas = new BuildableBitmapTextureAtlas(this.getTextureManager(), 512, 512);
		this.mRightNormalTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBuildableBitmapTextureAtlas, this, "gfx/right_arrow_small.png");
		this.mLeftNormalTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBuildableBitmapTextureAtlas, this, "gfx/left_arrow_small.png");
		this.mHitNormalTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBuildableBitmapTextureAtlas, this, "gfx/hit_button.png");
		this.mHitSelectedTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBuildableBitmapTextureAtlas, this, "gfx/hitselected.png");
		this.mMoveNormalTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBuildableBitmapTextureAtlas, this, "gfx/walk.png");
		this.mMoveSelectedTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBuildableBitmapTextureAtlas, this, "gfx/walkselected.png");
		this.mEndTurnNormalTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBuildableBitmapTextureAtlas, this, "gfx/end_turn.png");
		this.mEndTurnSelectedTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBuildableBitmapTextureAtlas, this, "gfx/endselected.png");
		try {
			this.mBuildableBitmapTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
			this.mBuildableBitmapTextureAtlas.load();
		} catch (TextureAtlasBuilderException e) {
			Debug.e(e);
		}
		
	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		mScene = new Scene();
		

		try {
			final TMXLoader tmxLoader = new TMXLoader(this.getAssets(), this.mEngine.getTextureManager(), TextureOptions.BILINEAR_PREMULTIPLYALPHA, this.getVertexBufferObjectManager(), new ITMXTilePropertiesListener() {
				@Override
				public void onTMXTileWithPropertiesCreated(final TMXTiledMap pTMXTiledMap, final TMXLayer pTMXLayer, final TMXTile pTMXTile, final TMXProperties<TMXTileProperty> pTMXTileProperties) {
					/* We are going to count the tiles that have the property "cactus=true" set. */
					
					//set tile properties in tiles
					//0 = normal
					//1 = goal
					//2 = warp
					//3 = bin
					//4 = wall
					if(pTMXTileProperties.containsTMXProperty("wall", "true")) {
						mTiles[pTMXTile.getGlobalTileID()] = 4;
					} else if(pTMXTileProperties.containsTMXProperty("bin", "true")) {
						mTiles[pTMXTile.getGlobalTileID()] = 3;
					} else if(pTMXTileProperties.containsTMXProperty("warp", "true")) {
						mTiles[pTMXTile.getGlobalTileID()] = 2;
						//add tile nums to warptiles for randomaccess
						mWarpTiles.add(pTMXTile.getTileColumn()*30+pTMXTile.getTileRow());
						
					} else if(pTMXTileProperties.containsTMXProperty("goal", "true")) {
						mTiles[pTMXTile.getGlobalTileID()] = 1;
					} else {
						mTiles[pTMXTile.getGlobalTileID()] = 0;
					}
					
				}
			});
			this.mTMXTiledMap = tmxLoader.loadFromAsset("tmx/battlemap.tmx");
			this.mTMXTiledMap.setOffsetCenter(0, 0);

		} catch (final TMXLoadException e) {
			Debug.e(e);
		}
		// These must be defined for the findpath() method to work
        final IPathFinderMap<TMXLayer> pmap = new IPathFinderMap<TMXLayer>() 
        {
            // Lets you customize what blocks you want to be considered blocked
            @Override
            public boolean isBlocked(final int pToTileColumn, final int pToTileRow,TMXLayer pTile)
            {   
            	//This is the array of all the tiles that can be collided with
            	//I added them manually. It makes it easier if you've organized your tile maps
            	//since you wouldn't need the for loop
            	int tileoffset = 29;
            	TMXTile block = mTmxLayer.getTMXTile(pToTileColumn, pToTileRow);
            	if(mTiles[block.getGlobalTileID()] == 4) {
            		Log.d("isBlocked",Integer.toString(block.getGlobalTileID())+" is Blocked");
            		return true;
            	} else if (mPlayerMap[pToTileColumn*30+tileoffset-pToTileRow] != -1){
            		return true;
            	}
                // Return false by default = no tiles blocked
                return false;
            }
        };
           ICostFunction<TMXLayer> cfunc = new ICostFunction<TMXLayer>()
        		   {
            // This is the key function to understand for AStar pathing
            // Returns the cost of the next tile in the path
            @Override
            public float getCost(final IPathFinderMap<TMXLayer> ipmap, final int pFromTileColumn, final int pFromTileRow, final int pToTileColumn, final int pToTileRow, final TMXLayer pTile) {
           	
            	//I coded this it because it caused more problems than it was worth, but this is where the path value is decided
            		
                    //grab the first property from a tile at pToTileColumn x pToTileRow
                    //TMXProperty cost = pTile.getTMXTile(pToTileColumn, pToTileRow).getTMXTileProperties(mTMXTiledMap).get(0);
                    //Gets the value of the string
                    //return Float.parseFloat(cost.getValue());
            	return mTileAPCost;
                   
            }
           
        };
        

        mScene.attachChild(this.mTMXTiledMap);
		 
		
        mScene.setOnSceneTouchListener(this); 
		
		
		/* Make the camera not exceed the bounds of the TMXEntity. */
		this.mCamera.setBoundsEnabled(false);
		this.mCamera.setBounds(0, 0, this.mTMXTiledMap.getWidth(), this.mTMXTiledMap.getHeight());
		this.mCamera.setBoundsEnabled(true);

		final float centerX = CAMERA_WIDTH / 2;
		final float centerY = CAMERA_HEIGHT / 2;
		// HUD background gray box
		final Rectangle scorebBackground = new Rectangle(0, 0, CAMERA_WIDTH*2, 64, this.getVertexBufferObjectManager());
		scorebBackground.setColor((float)0.02, (float)0.235,(float)0.37, 1);
		mHud.attachChild(scorebBackground);
		
		
		// make the HUD button sprites
		final ButtonSprite rightButton = new ButtonSprite(64, 16, mRightNormalTextureRegion, this.getVertexBufferObjectManager(), 
				new OnClickListener() {
			@Override
            public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
            //shift right through current team player list    
			//change current player instance
				do {
					mCurrPlayerNum++;
					if(mCurrPlayerNum > 8){
						mCurrPlayerNum = 0;
					}
					
				} while (mTeamSprites.get(mCurrTeamNum).get(mCurrPlayerNum) == null) ;
				mPlayerNum.setText(Integer.toString(mCurrPlayerNum+1));
				mPlayerName.setText(Game.get(getApplicationContext()).getTeamPlayer(mCurrTeamNum, mCurrPlayerNum).getNickName());
				mAP.setText("AP: "+Integer.toString(mCurrTeamAPList[mCurrPlayerNum]));
				mPlayer = mTeamSprites.get(mCurrTeamNum).get(mCurrPlayerNum);
				mCamera.setChaseEntity(mPlayer);
				mHitMode = false;
				setHitTiles(mHitMode);
			}
        });
		
		
		final ButtonSprite leftButton = new ButtonSprite(16, 16, mLeftNormalTextureRegion, this.getVertexBufferObjectManager(), 
				new OnClickListener() {
			@Override
            public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
            //shift left through current team player list    
			//change current player instance
				do {
					mCurrPlayerNum--;
					if(mCurrPlayerNum < 0){
						mCurrPlayerNum = 8;
					}
					
				} while (mTeamSprites.get(mCurrTeamNum).get(mCurrPlayerNum) == null);
				mPlayerNum.setText(Integer.toString(mCurrPlayerNum+1));
				mPlayerName.setText(Game.get(getApplicationContext()).getTeamPlayer(mCurrTeamNum, mCurrPlayerNum).getNickName());
				mAP.setText("AP: "+Integer.toString(mCurrTeamAPList[mCurrPlayerNum]));
				mPlayer = mTeamSprites.get(mCurrTeamNum).get(mCurrPlayerNum);
				mCamera.setChaseEntity(mPlayer);
				mHitMode = false;
				setHitTiles(mHitMode);

			}
        });
		final ButtonSprite hitButton = new ButtonSprite(centerX+44, 16, mHitNormalTextureRegion, mHitSelectedTextureRegion, this.getVertexBufferObjectManager(), 
				new OnClickListener() {
			@Override
            public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
            // on click one bring back tiles with players with range of 1
				if (mHitMode) {
					mHitMode = false;
					setHitTiles(mHitMode);
				} else {
					mHitMode = true;
					setHitTiles(mHitMode);
				}
			}
        });
		final ButtonSprite walkButton = new ButtonSprite(centerX+80, 16, mMoveNormalTextureRegion, mMoveSelectedTextureRegion, this.getVertexBufferObjectManager(), 
				new OnClickListener() {
			@Override
            public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
            //on click one go into move mode
			//on click two if in move mode, get out of move mode
			
			mMoveMode = !mMoveMode;
			mSelectedTileRectangle.setColor(0, 1, 0, 0.25f);
				//toggle on button?
			mHitMode = false;
			setHitTiles(mHitMode);
			}
        });
		final ButtonSprite endTurnButton = new ButtonSprite(CAMERA_WIDTH-16, 16, mEndTurnNormalTextureRegion, mEndTurnSelectedTextureRegion, this.getVertexBufferObjectManager(), 
				new OnClickListener() {
			@Override
            public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
            //end current teams turn, start next teams turn.
				do {
					mCurrTeamNum++;
					if(mCurrTeamNum > 3){
						mCurrTeamNum = 0;
						mTurn++;
					}
					
				} while (!mActiveTeamList[mCurrTeamNum]) ;
				mTeamNum.setText("TEAM: "+Integer.toString(mCurrTeamNum+1));
				getTeamAP(mCurrTeamNum);
				if (mTurn < 9) {
					makePlayerSprite(mCurrTeamNum, mTurn);
					mCurrPlayerNum = mTurn;
					
				} else {
					mCurrPlayerNum = 0;
					
				}

				mPlayerNum.setText(Integer.toString(mCurrPlayerNum+1));
				mPlayerName.setText(Game.get(getApplicationContext()).getTeamPlayer(mCurrTeamNum, mCurrPlayerNum).getNickName());
				mAP.setText("AP: "+Integer.toString(mCurrTeamAPList[mCurrPlayerNum]));
				mHitMode = false;
				setHitTiles(mHitMode);
				
			}
        });
		rightButton.setScale((float) .5);
		leftButton.setScale((float) .5);
		//hitButton.setScale((float) .75);
		//walkButton.setScale((float) .75);
		//endTurnButton.setScale((float) .75);
		
		mHud.registerTouchArea(rightButton);
		mHud.registerTouchArea(leftButton);
		mHud.registerTouchArea(hitButton);
		mHud.registerTouchArea(walkButton);
		mHud.registerTouchArea(endTurnButton);
		mHud.attachChild(rightButton);
		mHud.attachChild(leftButton);
		mHud.attachChild(hitButton);
		mHud.attachChild(walkButton);
		mHud.attachChild(endTurnButton);
		
		// make ap, and current player num, name texts/////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////
		mAP = new Text(192, 2, mFont, "AP 0123456789",  this.getVertexBufferObjectManager());
		mAP.setAnchorCenter(0,0);
		mAP.setText("AP: 0");
		mHud.attachChild(mAP);
		mPlayerNum = new Text(34, 2, mFont, "123456789",  this.getVertexBufferObjectManager());
		mPlayerNum.setAnchorCenter(0,0);
		mPlayerNum.setText("1");
		mHud.attachChild(mPlayerNum);
		mPlayerName = new Text(80, 2, mFont, "ABCDEFGHIJKLMOPQRSTUVWXYZ0123456789",  this.getVertexBufferObjectManager());
		mPlayerName.setAnchorCenter(0,0);
		mPlayerName.setText("BOB");
		mHud.attachChild(mPlayerName);
		//current team text for hud
		mTeamNum = new Text(CAMERA_WIDTH-132,2, mFont, "TEAM: 1234", this.getVertexBufferObjectManager());
		mTeamNum.setAnchorCenter(0, 0);
		mTeamNum.setText("TEAM: 1");
		mHud.attachChild(mTeamNum);
		
		mCamera.setHUD(mHud);
		// Declare the AStarPathFinder
        // First Param: above ITiledMap
        // Second Param: Max Search Depth - Care, if this is too small your program will crash
        // Third Param: allow diagonal movement or not
        // Fourth Param: Heuristics you want to use in the A* algorithm (optional)
        mFinder = new OldAStarPathFinder<TMXLayer>(mTMXTiledMap, 30, false, pmap, cfunc);
      //call setupGame to setup the game logic
      	this.setupGame();	
      	
		/* Now we are going to create a rectangle that will  always highlight the tile below the feet of the pEntity. */
		final Rectangle currentTileRectangle = new Rectangle(0, 0, this.mTMXTiledMap.getTileWidth(), this.mTMXTiledMap.getTileHeight(), this.getVertexBufferObjectManager());
		/* Set the OffsetCenter to 0/0, so that it aligns with the TMXTiles. */
		currentTileRectangle.setOffsetCenter(0, 0);
		currentTileRectangle.setColor(1, 0, 0, 0.25f);
		mScene.attachChild(currentTileRectangle);
		
		//make a rectangle that becomes visible when people are trying to select where to walk, and becomes invisible after
		mSelectedTileRectangle = new Rectangle(0, 0, this.mTMXTiledMap.getTileWidth(), this.mTMXTiledMap.getTileHeight(), this.getVertexBufferObjectManager());
		/* Set the OffsetCenter to 0/0, so that it aligns with the TMXTiles. */
		mSelectedTileRectangle.setOffsetCenter(0, 0);
		mSelectedTileRectangle.setColor(0, 1, 0, 0.0f);
		mScene.attachChild(mSelectedTileRectangle);
		//set the hit tiles up, that will get setup
		mHitNorthTileRectangle = new Rectangle(0, 0, this.mTMXTiledMap.getTileWidth(), this.mTMXTiledMap.getTileHeight(), this.getVertexBufferObjectManager());
		/* Set the OffsetCenter to 0/0, so that it aligns with the TMXTiles. */
		mHitNorthTileRectangle.setOffsetCenter(0, 0);
		mHitNorthTileRectangle.setColor(1, 0, 0, 0.0f);
		mScene.attachChild(mHitNorthTileRectangle);
		
		mHitSouthTileRectangle = new Rectangle(0, 0, this.mTMXTiledMap.getTileWidth(), this.mTMXTiledMap.getTileHeight(), this.getVertexBufferObjectManager());
		/* Set the OffsetCenter to 0/0, so that it aligns with the TMXTiles. */
		mHitSouthTileRectangle.setOffsetCenter(0, 0);
		mHitSouthTileRectangle.setColor(1, 0, 0, 0.0f);
		mScene.attachChild(mHitSouthTileRectangle);
		
		mHitEastTileRectangle = new Rectangle(0, 0, this.mTMXTiledMap.getTileWidth(), this.mTMXTiledMap.getTileHeight(), this.getVertexBufferObjectManager());
		/* Set the OffsetCenter to 0/0, so that it aligns with the TMXTiles. */
		mHitEastTileRectangle.setOffsetCenter(0, 0);
		mHitEastTileRectangle.setColor(1, 0, 0, 0.0f);
		mScene.attachChild(mHitEastTileRectangle);
		
		mHitWestTileRectangle = new Rectangle(0, 0, this.mTMXTiledMap.getTileWidth(), this.mTMXTiledMap.getTileHeight(), this.getVertexBufferObjectManager());
		/* Set the OffsetCenter to 0/0, so that it aligns with the TMXTiles. */
		mHitWestTileRectangle.setOffsetCenter(0, 0);
		mHitWestTileRectangle.setColor(1, 0, 0, 0.0f);
		mScene.attachChild(mHitWestTileRectangle);

		/* The layer for the player to walk on. */
		mTmxLayer = this.mTMXTiledMap.getTMXLayers().get(0);
		while (mActiveTeamList[mCurrTeamNum] == false) {
			mCurrTeamNum++;
			if(mCurrTeamNum > 3) {
				mCurrTeamNum = 0;
			}
		}
		mTeamNum.setText("TEAM: "+Integer.toString(mCurrTeamNum+1));
      	makePlayerSprite(mCurrTeamNum, mTurn);
      	getTeamAP(mCurrTeamNum);
      	mPlayerNum.setText(Integer.toString(mCurrPlayerNum+1));
		mPlayerName.setText(Game.get(getApplicationContext()).getTeamPlayer(mCurrTeamNum, mCurrPlayerNum).getNickName());
		mAP.setText("AP: "+Integer.toString(mCurrTeamAPList[mCurrPlayerNum]));
		
		
		mScene.registerUpdateHandler(new IUpdateHandler() {
			@Override
			public void reset() { }

			@Override
			public void onUpdate(final float pSecondsElapsed) {
				/* Get the scene-coordinates of the players feet. */
				
				final float[] playerFootCordinates = mPlayer.convertLocalCoordinatesToSceneCoordinates(16, 1);
				
				/* Get the tile the feet of the player are currently waking on. */
				mCurrentTile = mTmxLayer.getTMXTileAt(playerFootCordinates[Constants.VERTEX_INDEX_X],
						playerFootCordinates[Constants.VERTEX_INDEX_Y]);
				
				if(mCurrentTile != null) {
					// tmxTile.setTextureRegion(null); <-- Eraser-style removing of tiles =D
					// this might be useful to try and figure out how to change tile faces
					currentTileRectangle.setPosition(mTmxLayer.getTileX(mCurrentTile.getTileColumn()), mTmxLayer.getTileY(mCurrentTile.getTileRow()));
					
				}
				
			}
		});
		

		return mScene;
	}
	
	@Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent){
    	//if the user touches the screen then...
    	if(pSceneTouchEvent.isActionDown()){
    		//Calls the Walk to method, which has two parameters
    		//First & Second Param: are the touched x and y position 
    		//Third Param: is the scene
    		final float[] pToTiles = pScene.convertLocalCoordinatesToSceneCoordinates(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
    		TMXTile tileTouched = mTmxLayer.getTMXTileAt(
	                   pToTiles[Constants.VERTEX_INDEX_X],
	                   pToTiles[Constants.VERTEX_INDEX_Y]);
            //Gets the tile at the touched location
    		if(mMoveMode&&!mSelectedTileToMove ) {
    			mSelectedTile = tileTouched;
    			mSelectedTileToMove = true;
    			mSelectedTileRectangle.setColor(0, 1, 0, 0.25f);
    			mSelectedTileRectangle.setPosition(mTmxLayer.getTileX(mSelectedTile.getTileColumn()), mTmxLayer.getTileY(mSelectedTile.getTileRow()));
    		} else if(mMoveMode && mSelectedTileToMove) {
    			if(mSelectedTile == tileTouched){
    				walkTo(pSceneTouchEvent.getX(), pSceneTouchEvent.getY(), pScene);   
    			} else {
    				mSelectedTile = tileTouched;  
    				mSelectedTileRectangle.setPosition(mTmxLayer.getTileX(mSelectedTile.getTileColumn()), mTmxLayer.getTileY(mSelectedTile.getTileRow()));
    				
    			}
    		} else if (mHitMode) {
    			if(tileTouched == mTmxLayer.getTMXTile(mCurrentTile.getTileColumn(), mCurrentTile.getTileRow()-1) && mNorthHittable){
    				//north hit tile
    				int teamPlayer = mPlayerMap[mCurrentTile.getTileColumn()*30+(mCurrentTile.getTileRow()-1)];
    				hit(teamPlayer/10,teamPlayer%10,1);
    			} else if (tileTouched == mTmxLayer.getTMXTile(mCurrentTile.getTileColumn(), mCurrentTile.getTileRow()+1) && mSouthHittable) {
    				//south hit tile
    				int teamPlayer = mPlayerMap[mCurrentTile.getTileColumn()*30+(mCurrentTile.getTileRow()+1)];
    				hit(teamPlayer/10,teamPlayer%10,2);
    			} else if (tileTouched == mTmxLayer.getTMXTile((mCurrentTile.getTileColumn()-1), mCurrentTile.getTileRow()) && mEastHittable) {
    				//east hit tile
    				int teamPlayer = mPlayerMap[(mCurrentTile.getTileColumn()-1)*30+(mCurrentTile.getTileRow())];
    				hit(teamPlayer/10,teamPlayer%10,3);
    			} else if (tileTouched == mTmxLayer.getTMXTile((mCurrentTile.getTileColumn()+1), mCurrentTile.getTileRow()) && mWestHittable) {
    				//west hit tile
    				int teamPlayer = mPlayerMap[(mCurrentTile.getTileColumn()+1)*30+(mCurrentTile.getTileRow())];
    				hit(teamPlayer/10,teamPlayer%10,4);
    			} else {
    				Log.d("hit TouchAction","you tried to hit something weird...don't do that!");
    			}
    			
    		} else {
    			Log.d("onSceneTouchEvent", "X:"+Integer.toString((int) pSceneTouchEvent.getX())+"Y:"+Integer.toString((int) pSceneTouchEvent.getY()));
    		}
                    
    	}
    	return true;
    }

	// ===========================================================
	// Methods
	// ===========================================================
	//
	
	
	
	//This method controls the path the sprite will take once a location is passed into it
    public void walkTo(final float pX, final float pY, Scene pScene){
    	
        //Puts the touch events into an array
        final float[] pToTiles = pScene.convertLocalCoordinatesToSceneCoordinates(pX, pY);
        						
        //Gets the tile at the touched location
        final TMXTile tmxTilePlayerTo =  mTmxLayer.getTMXTileAt(
               pToTiles[Constants.VERTEX_INDEX_X],
               pToTiles[Constants.VERTEX_INDEX_Y]
        );
                       
    /*********/ //if is walking and there is a A_path ******************
        if (mIsWalking == true && mA_path != null) {
        	//Calls the Walk to next waypoint method,
    		//First & Second Param: are the touched x and y position 
    		//Third Param: is the scene
            walkToNextWayPoint(pX,pY,pScene);
        }
                   
        else if (mA_path == null ) {                
            // Create the path with findPath()
            // First Param: TMXLayer in this case, templated through the above ITiledMap and AStarPathFinder functions
            // Second Param: Max Cost of a complete path, NOT A SINGLE TILE! - if value is too low your program will crash
            // Third & Fourth Param: From Column and From Row - Starting tile location
            // Fifth & Sixth Param: To Column and To Row - Ending tile location  
        	//Log.d("in walk to","Col,From:"+Integer.toString(mCurrentTile.getTileColumn())+"Row,From:"+Integer.toString(mCurrentTile.getTileRow()));
        	//Log.d("in walk to","Col,To:"+Integer.toString(tmxTilePlayerTo.getTileColumn())+"Row,To:"+Integer.toString(tmxTilePlayerTo.getTileRow()));
        	//needed the tileoffset, because the new GLes 2 anchor center branch took an opposite y from previous version
        	int tileoffset = 29;
            mA_path = mFinder.findPath(
                 //Sprite's initial tile location
            	 mCurrentTile.getTileColumn(),
            	 tileoffset-mCurrentTile.getTileRow(),
                 //Sprite's final tile location
                 tmxTilePlayerTo.getTileColumn(),
                 tileoffset-tmxTilePlayerTo.getTileRow(),
                 mTmxLayer,mCurrTeamAPList[mCurrPlayerNum]);
     
            //Calls the Load path method
            loadPathFound();
        }
    }
    //If the sprite is moving and the screen is touched then this method will be called.
    //It creates a sub path that creates a smooth transition to the center of a tile before rerouting
    private void walkToNextWayPoint(final float pX, final float pY, final Scene pScene) {
    	//Unregisters the current paths
        mPlayer.unregisterEntityModifier(mMoveModifier);
        mPlayer.unregisterEntityModifier(mPathTemp); //mPathTemp is another global PathModifier
        
        //Creates a copy of the path currently being walked
        final Path lPath = (Path) mCurrentPath.deepCopy();
        
        //create a new path with length 2 from current sprite position to next original path waypoint
        final Path path = new Path(2);
        //Continues the path for one more tile 
        
        path
            .to(mPlayer.getX(), mPlayer.getY())
            .to(lPath.getCoordinatesX()[mWaypointIndex+1], lPath.getCoordinatesY()[mWaypointIndex+1]);
        
        //recalculate the speed. TILE_WIDTH is the tmx tile width, use yours
        float lSpeed = path.getLength() * mSpeed / (TILE_WIDTH); 
                   
        //Create the modifier of this subpath
        mPathTemp = new PathModifier(lSpeed, path, new IEntityModifierListener() {
                           
            public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
                                   
            }
                           
            public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
                           
            }
        }, new IPathModifierListener() {
                           
            public void onPathWaypointStarted(final PathModifier pPathModifier, final IEntity pEntity,  int pWaypointIndex) {
            	                   
        }
            
            public void onPathWaypointFinished(PathModifier pPathModifier, IEntity pEntity,
                int pWaypointIndex) {
                                   
            }
                           
            public void onPathStarted(PathModifier pPathModifier, IEntity pEntity) {
     
            }
                           
            public void onPathFinished(PathModifier pPathModifier, IEntity pEntity) {
            		//Once the subpath is finished the a new one is created at the new touched location
                    mA_path = null;
                    walkTo(pX, pY,pScene);
                    //Stops the animation of the player once it stops
                    mPlayer.stopAnimation();
                    // turn off move mode
                    
            }
        });
        //Registers the newly created path modifier           
        mPlayer.registerEntityModifier(mPathTemp);          
    }
    
    //Creates the A* Path and executes it
    private void loadPathFound() {
    	   
        if (mA_path != null) {
            //Global var
            mCurrentPath = new Path(mA_path.getLength());
            int tilewidth = mTMXTiledMap.getTileWidth();
            int tileheight = mTMXTiledMap.getTileHeight();
             
            mCurrTeamAPList[mCurrPlayerNum] = mCurrTeamAPList[mCurrPlayerNum]-(mA_path.getLength()-1)*10;
            mAP.setText("AP: "+Integer.toString(mCurrTeamAPList[mCurrPlayerNum]));
            
            for(int i = 0; i < mA_path.getLength(); i++){
                mCurrentPath.to(mA_path.getX(i) * tilewidth+(tilewidth/2), mA_path.getY(i) * tileheight);
                //Log.d("loadPath","Col,To:"+Integer.toString(mA_path.getX(i))+"Row,To:"+Integer.toString(mA_path.getY(i)));
            }
            
            doPath();
        }
    }
    //Creates the modifier for the A*Path
    private void doPath() {
    	 
        //Create this mMoveModifier as Global, there is mSpeed  too -> player speed
        mMoveModifier = new PathModifier(mSpeed * mA_path.getLength(), mCurrentPath, new IEntityModifierListener() {
     
            public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
     
            }
           
            public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
     
            }
        }, new PathModifier.IPathModifierListener() {


			public void onPathWaypointStarted(final PathModifier pPathModifier, final IEntity pEntity,  int pWaypointIndex) {
				 
                //Character animation are controlled here
                switch(mA_path.getDirectionToNextStep(pWaypointIndex)) {
                case DOWN:
                	//down with ball
                        //mPlayer.animate(new long[]{200, 200, 200}, 18, 20, true);
                    //down withoutball
                        mPlayer.animate(new long[]{200, 200, 200}, 6, 8, true);
                        break;
                case RIGHT :
                	//right with ball
                    	//mPlayer.animate(new long[]{200, 200, 200}, 15, 17, true);
                    	///right withoutball
                        mPlayer.animate(new long[]{200, 200, 200}, 3, 5, true);
                        break;
                case UP:
                		//up with ball
                		//mPlayer.animate(new long[]{200, 200, 200}, 12, 14, true);
                		//up without ball
                        mPlayer.animate(new long[]{200, 200, 200}, 0, 2, true);
                        break;
                case LEFT:
	                	//left with ball
	            		//mPlayer.animate(new long[]{200, 200, 200}, 21, 23, true);
	            		//left without ball
                        mPlayer.animate(new long[]{200, 200, 200}, 9, 11, true);
                        break;
                default:
                        break;
                }            
                           
                //Keep the waypointIndex in a Global Var
                mWaypointIndex = pWaypointIndex;
           
            }
                           
            public void onPathWaypointFinished(PathModifier pPathModifier, IEntity pEntity,
                    int pWaypointIndex) {
           
            }
                                   
            public void onPathStarted(PathModifier pPathModifier, IEntity pEntity) {
     
                //Set a global var
                mIsWalking = true;
            }
                                   
            public void onPathFinished(PathModifier pPathModifier, IEntity pEntity) {
     
                //Stop walking and set A_path to null
                mIsWalking = false;
                mA_path = null;
                mPlayer.stopAnimation();
                mMoveMode = false;
                //change the selected move tile rect back to transparent
                mSelectedTileRectangle.setColor(0, 1, 0, 0.0f);
                updatePlayerPosition(mCurrTeamNum,mCurrPlayerNum);
            }
     
        });
     
        mPlayer.registerEntityModifier(mMoveModifier);
    }

	
	
	
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
