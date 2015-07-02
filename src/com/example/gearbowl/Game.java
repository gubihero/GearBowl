package com.example.gearbowl;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

public class Game {  //holds the teams and game logic of the game screen.  Is a singleton
	private static Game sGame;
	private Context mGameContext;
	private ArrayList<Team> mTeams;
	private DBController mDB;
	private int mTurn;
	//public Map mMap; will have getter setter.
	
        
	private Game(Context appContext) {
		this.mGameContext = appContext;
		this.mTeams = new ArrayList<Team>();
		for (int iter = 0; iter < 4; iter++) {
			Team t = new Team();
			this.mTeams.add(t);
		}
		mDB = new DBController(mGameContext);
	}
	
	public static Game get(Context c) {
	    if (sGame == null) {
	    	sGame = new Game(c.getApplicationContext());
	    	Log.d("GameCreateGet","made a whole new game");
	    }
	    return sGame;
	}
	
	public int getTurn(){
		return this.mTurn;
	}
	
	public void setTurn(int newTurn) {
		this.mTurn = newTurn;
	}
	
	public Team getTeam(int p) {
		return this.mTeams.get(p);
	}
	public void setTeam(int p, Team t) {
		this.mTeams.set(p, t);
	}
	
	public String getTeamName(int p){
		return this.mTeams.get(p).getName();
	}
	
	public void setTeamName(int p, String name){
		this.mTeams.get(p).setName(name);
		
		//Team team = this.mTeams.get(p);
		//team.setName(name);
		//this.mTeams.set(p,team);
	}
	
	public Player getTeamPlayer(int team,int player) {
		return this.mTeams.get(team).getPlayerAtPosition(player);
	}
	
	public void setTeamPlayer(int team,int player, Player p) {
		this.mTeams.get(team).addPlayer(player, p);
	}
	
	public void saveTeam(int teamNum, String saveId){
		Team saveTeam = mTeams.get(teamNum);
		mDB.saveTeam(saveTeam,saveId);
	}
	
	public void updateSavedTeam(int teamNum,String saveId){
		Team saveTeam = mTeams.get(teamNum);
		mDB.updateSavedTeam(saveTeam,saveId);
	}
	
	public String[] getSaves(){
		ArrayList<String> savesArray = this.mDB.loadSaves();
		String[] saves = new String[savesArray.size()];
		savesArray.toArray(saves);
		return saves;
	}
	
	public ArrayList<String> getSavesArray(){
		return this.mDB.loadSaves();
	}
	
	//sets team in game class, and returns a team object to caller
	public Team loadTeam(int teamNum,String saveId){
		Team loadedTeam = mDB.loadTeam(saveId);
		mTeams.set(teamNum, loadedTeam);
		return loadedTeam;
	}
	public ArrayList<Player> loadUnits(){
		return this.mDB.loadUnits();
	}
	
	public int getTeamStatus (int teamNum) {
		return mTeams.get(teamNum).getStatus();
	}
	
	public void setTeamStatus (int teamNum, int newStatus) {
		mTeams.get(teamNum).setStatus(newStatus);
	}
}
