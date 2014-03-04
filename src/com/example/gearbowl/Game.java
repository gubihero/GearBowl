package com.example.gearbowl;

import java.util.ArrayList;

import android.content.Context;

public class Game {  //holds the teams and game logic of the game screen.  Is a singleton
	private static Game sGame;
	private Context mGameContext;
	private ArrayList<Team> mTeams;
	private int mTurn;
	//public Map mMap; will have getter setter.
	
        
	private Game(Context appContext) {
		this.mGameContext = appContext;
		this.mTeams = new ArrayList<Team>();
		for (int iter = 0; iter < 4; iter++) {
			Team t = new Team();
			this.mTeams.add(t);
		}
	}
	
	public static Game get(Context c) {
	    if (sGame == null) {
	    	sGame = new Game(c.getApplicationContext());
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
	
	public Player getTeamPlayer(int team,int player) {
		return this.mTeams.get(team).getPlayerAtPosition(player);
	}
	
	public void setTeamPlayer(int team,int player, Player p) {
		this.mTeams.get(team).addPlayer(player, p);
	}
}
