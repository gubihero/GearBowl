package com.example.gearbowl;

import java.util.ArrayList;

public class Team {
	private int mStatus;  //0 if inactive, 1 if active, 2 if A.I.
	private String mName;  //team name
	private ArrayList<Player> mPlayers; //list of player objects attached to team
	private int mCost;

	public Team() {
		this.mStatus = 0;
		this.mName = "Empty";
		this.mPlayers = new ArrayList<Player>();    
		for (int iter = 0; iter < 9; iter++) {
			Player p = new Player();
			this.mPlayers.add(p);
		}
	}
	
	public void addPlayer(int position,Player p) {
		if (position <= 9) {
			this.mPlayers.set(position,p);
		}
	}
	
	public void removePlayer(int position) {
		if (position < this.mPlayers.size() && position >= 0) {
			Player empty = new Player();
			this.mPlayers.set(position, empty);
		}
	}
	
	public ArrayList<Player> getPlayers() {
		return this.mPlayers;
	}
	
	public Player getPlayerAtPosition(int position){
		return this.mPlayers.get(position);
	}
	
	public int getStatus() {
		return this.mStatus;
	}
	
	public void setStatus(int newStatus) {
		this.mStatus = newStatus;
	}
	
	public String getName() {
		return this.mName;
	}
	
	public void setName(String newName) {
		this.mName = newName;
	}
	
	public int getCost() {
		return this.mCost;
	}
	
	public void setCost(int newCost) {
		this.mCost = newCost;
	}
}
