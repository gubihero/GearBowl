package com.example.gearbowl;

public class Player {
	private String mType;
	private String mNickName;
	private int mStatus; //0 = empty, 1 = active, 2 = knocked down, 3 = stunned stage 1, 4 = stunned stage 2
	//player stats
	private int mStrength;
	private int mToughness;
	private int mAgility;
	private int mDodge;
	private int mJump;
	private int mHands;
	private int mExperience;
	private int mCost;
	
	public Player() {
		this.mType = "Empty";
		this.mNickName = "Empty";
		this.mStatus = 0;
		this.mStrength = 0;
		this.mToughness = 0;
		this.mAgility = 0;
		this.mDodge = 0;
		this.mJump = 0;
		this.mHands = 0;
		this.mExperience = 0;
		this.mCost = 0;
	}
	
	@Override
	public String toString() {
		return this.mNickName;
	}

	public String getType() {
		return mType;
	}

	public void setType(String mType) {
		this.mType = mType;
	}

	public String getNickName() {
		return mNickName;
	}

	public void setNickName(String mNickName) {
		this.mNickName = mNickName;
	}
	
	public int getStatus() {
		return mStatus;
	}

	public void setStatus(int mStatus) {
		this.mStatus = mStatus;
	}

	public int getStrength() {
		return mStrength;
	}

	public void setStrength(int mStrength) {
		this.mStrength = mStrength;
	}

	public int getToughness() {
		return mToughness;
	}

	public void setToughness(int mToughness) {
		this.mToughness = mToughness;
	}

	public int getAgility() {
		return mAgility;
	}

	public void setAgility(int mAgility) {
		this.mAgility = mAgility;
	}

	public int getDodge() {
		return mDodge;
	}

	public void setDodge(int mDodge) {
		this.mDodge = mDodge;
	}

	public int getJump() {
		return mJump;
	}

	public void setJump(int mJump) {
		this.mJump = mJump;
	}

	public int getHands() {
		return mHands;
	}

	public void setHands(int mHands) {
		this.mHands = mHands;
	}

	public int getExperience() {
		return mExperience;
	}

	public void setExperience(int mExperience) {
		this.mExperience = mExperience;
	}

	public int getCost() {
		return mCost;
	}

	public void setCost(int mCost) {
		this.mCost = mCost;
	}
}
