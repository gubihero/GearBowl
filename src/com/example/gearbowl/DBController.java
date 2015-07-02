package com.example.gearbowl;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBController extends SQLiteOpenHelper {
	public DBController(Context applicationcontext) {
		super(applicationcontext, "GearBowlsqlite.db",null,9);
	}
	
	@Override
	public void onCreate(SQLiteDatabase database) {
		// TODO Auto-generated method stub
		//create the team save table
		Log.d("DBController","OnCreate");
		String createPlayersQuery;
		createPlayersQuery = 
				"CREATE TABLE players (playerId INTEGER PRIMARY KEY, saveId TEXT, teamName TEXT," +
				"playerNum INTEGER, playerName TEXT, playerType TEXT, playerStrength INTEGER," +
				"playerToughness INTEGER, playerAgility INTEGER, playerDodge INTEGER," +
				"playerJump INTEGER, playerHands INTEGER, playerExp INTEGER, playerCost INTEGER," +
				"playerAP INTEGER, playerImage INTEGER)";
		database.execSQL(createPlayersQuery);
		//create units table
		
		String createUnitQuery;
		createUnitQuery = "CREATE TABLE units (playerId INTEGER PRIMARY KEY," +
				"playerType TEXT, playerStrength INTEGER," +
				"playerToughness INTEGER, playerAgility INTEGER, playerDodge INTEGER," +
				"playerJump INTEGER, playerHands INTEGER, playerCost INTEGER, " +
				"playerAP INTEGER, playerImage INTEGER)";
		database.execSQL(createUnitQuery);
		//prepopulate units table
		//Smasher
		
		ContentValues smasherValues = new ContentValues();
		smasherValues.put("playerType", "smashbot");
		smasherValues.put("playerStrength", 70);
		smasherValues.put("playerToughness", 60);
		smasherValues.put("playerAgility", 30);
		smasherValues.put("playerDodge", 20);
		smasherValues.put("playerJump", 10);
		smasherValues.put("playerHands", 40);
		smasherValues.put("playerCost", 80);
		smasherValues.put("playerAP", 50);
		//replace with real image later
		smasherValues.put("playerImage", R.drawable.smashbot);
		
		database.insert("units", null, smasherValues);
		//normbot
		ContentValues normbotValues = new ContentValues();
		normbotValues.put("playerType", "normbot");
		normbotValues.put("playerStrength", 50);
		normbotValues.put("playerToughness", 50);
		normbotValues.put("playerAgility", 50);
		normbotValues.put("playerDodge", 50);
		normbotValues.put("playerJump", 50);
		normbotValues.put("playerHands", 60);
		normbotValues.put("playerCost", 40);
		normbotValues.put("playerAP", 60);
		normbotValues.put("playerImage", R.drawable.normbot);
		database.insert("units", null, normbotValues);
		//speedbot
		ContentValues speedbotValues = new ContentValues();
		speedbotValues.put("playerType", "speedbot");
		speedbotValues.put("playerStrength", 30);
		speedbotValues.put("playerToughness", 20);
		speedbotValues.put("playerAgility", 70);
		speedbotValues.put("playerDodge", 70);
		speedbotValues.put("playerJump", 70);
		speedbotValues.put("playerHands", 50);
		speedbotValues.put("playerCost", 30);
		speedbotValues.put("playerAP", 80);
		speedbotValues.put("playerImage", R.drawable.speedbot);
		database.insert("units", null, speedbotValues);
		//crushbot
		ContentValues crushbotValues = new ContentValues();
		crushbotValues.put("playerType", "crushbot");
		crushbotValues.put("playerStrength", 60);
		crushbotValues.put("playerToughness", 40);
		crushbotValues.put("playerAgility", 40);
		crushbotValues.put("playerDodge", 60);
		crushbotValues.put("playerJump", 60);
		crushbotValues.put("playerHands", 70);
		crushbotValues.put("playerCost", 60);
		crushbotValues.put("playerAP", 70);
		crushbotValues.put("playerImage", R.drawable.crushbot);
		database.insert("units", null, crushbotValues);
		
		//database.close();
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int old_version, int new_version) {
		// TODO Auto-generated method stub
		String dropQuery;
		dropQuery = "DROP TABLE IF EXISTS players";
		database.execSQL(dropQuery);
		String dropUnits;
		dropUnits = "DROP TABLE IF EXISTS units";
		database.execSQL(dropUnits);
		onCreate(database);
	}
	
	public void updateSavedTeam(Team team, String saveId){
		ArrayList<Player> players = team.getPlayers();
		SQLiteDatabase database = this.getWritableDatabase();
		Log.d("DBController","updateSaveTeam");
		for (int i = 0; i<players.size();i++){
			ContentValues values = new ContentValues();
			values.put("saveId",saveId);
			values.put("teamName",team.getName());
			values.put("playerNum", i);
			values.put("playerName", players.get(i).getNickName());
			values.put("playerType", players.get(i).getType());
			values.put("playerStrength", players.get(i).getStrength());
			values.put("playerToughness", players.get(i).getToughness());
			values.put("playerAgility", players.get(i).getAgility());
			values.put("playerDodge", players.get(i).getDodge());
			values.put("playerJump", players.get(i).getJump());
			values.put("playerHands", players.get(i).getHands());
			values.put("playerExp", players.get(i).getExperience());
			values.put("playerCost", players.get(i).getCost());
			values.put("playerAP", players.get(i).getAP());
			values.put("playerImage", players.get(i).getImage());
			database.update("players", values, "saveId = ? AND playerNum = ?",new String[]{saveId,Integer.toString(i)});
		}
		database.close();
	}
	
	public void saveTeam(Team team, String saveId){
		ArrayList<Player> players = team.getPlayers();
		SQLiteDatabase database = this.getWritableDatabase();
		Log.d("DBController","saveTeam");
		for (int i = 0; i<players.size();i++){
			ContentValues values = new ContentValues();
			values.put("saveId",saveId);
			values.put("teamName",team.getName());
			values.put("playerNum", i);
			values.put("playerName", players.get(i).getNickName());
			values.put("playerType", players.get(i).getType());
			values.put("playerStrength", players.get(i).getStrength());
			values.put("playerToughness", players.get(i).getToughness());
			values.put("playerAgility", players.get(i).getAgility());
			values.put("playerDodge", players.get(i).getDodge());
			values.put("playerJump", players.get(i).getJump());
			values.put("playerHands", players.get(i).getHands());
			values.put("playerExp", players.get(i).getExperience());
			values.put("playerCost", players.get(i).getCost());
			values.put("playerAP", players.get(i).getAP());
			values.put("playerImage", players.get(i).getImage());
			database.insert("players", null, values);
		}
		database.close();
	}
	
	public Team loadTeam(String saveId){
		Team loadedTeam = new Team();
		SQLiteDatabase database = this.getReadableDatabase();
		Log.d("DBController","loadTeam");
		  String selectQuery = 
				  "SELECT * FROM players where saveId='"+saveId+"'";
		  Cursor cursor = database.rawQuery(selectQuery, null);
		  if (cursor.moveToFirst()) {
			  loadedTeam.setName(cursor.getString(2));
		    do {
		      Player player = new Player();
		      int pos = cursor.getInt(3);
		      player.setNickName(cursor.getString(4));
		      player.setType(cursor.getString(5));
		      player.setStrength(cursor.getInt(6));
		      player.setToughness(cursor.getInt(7));
		      player.setAgility(cursor.getInt(8));
		      player.setDodge(cursor.getInt(9));
		      player.setJump(cursor.getInt(10));
		      player.setHands(cursor.getInt(11));
		      player.setExperience(cursor.getInt(12));
		      player.setCost(cursor.getInt(13));
		      player.setAP(cursor.getInt(14));
		      player.setImage(cursor.getInt(15));
		      loadedTeam.addPlayer(pos, player);
		    } while (cursor.moveToNext());
		  }           
		  database.close();
		return loadedTeam;
	}
	
	public ArrayList<Player> loadUnits(){
		
		ArrayList<Player> units = new ArrayList<Player>();
		SQLiteDatabase database = this.getReadableDatabase();
		
		  String selectQuery = 
				  "SELECT * FROM units";
		  Cursor cursor = database.rawQuery(selectQuery, null);
		  if (cursor.moveToFirst()) {
		    do {
		      Player player = new Player();
		      player.setType(cursor.getString(1));
		      player.setStrength(cursor.getInt(2));
		      player.setToughness(cursor.getInt(3));
		      player.setAgility(cursor.getInt(4));
		      player.setDodge(cursor.getInt(5));
		      player.setJump(cursor.getInt(6));
		      player.setHands(cursor.getInt(7));
		      player.setCost(cursor.getInt(8));
		      player.setAP(cursor.getInt(9));
		      player.setImage(cursor.getInt(10));
		      units.add(player);
		    } while (cursor.moveToNext());
		  }     
		  database.close();
		  
		 return units;
	}
	
	public ArrayList<String> loadSaves(){
		ArrayList<String> saves = new ArrayList<String>();
		SQLiteDatabase database = this.getReadableDatabase();
		String getSavesQuery = "SELECT DISTINCT saveId FROM players";
		Cursor cursor = database.rawQuery(getSavesQuery, null);
		if (cursor.moveToFirst()) {
		    do {
		      String save = cursor.getString(0);
		      saves.add(save);
		    } while (cursor.moveToNext());
		  }     
		  database.close();
		
		return saves;
	}

}
