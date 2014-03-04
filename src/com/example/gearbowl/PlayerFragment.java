package com.example.gearbowl;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class PlayerFragment extends Fragment {
	private Player mPlayer;
	
	public static final String PLAYER_NUM_ID = "PLAYER_NUM_ID";
	public static final String PLAYER_TEAM_ID = "PLAYER_TEAM_ID";
	private static final String TAG = "playerFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.d(TAG,"Oncreate");
        int playerNum = (int)getActivity().getIntent().getIntExtra(PLAYER_NUM_ID,0);
        //Log.d(TAG,"playernum");
        int teamNum = (int)getActivity().getIntent().getIntExtra(PLAYER_TEAM_ID, 0);
        //Log.d(TAG,"teamnum");
        mPlayer = Game.get(getActivity()).getTeamPlayer(teamNum, playerNum);
        //Log.d(TAG,"mPlayer");
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
            Bundle savedInstanceState) {
    	Log.d(TAG,"OncreateView");
        View v = inflater.inflate(R.layout.fragment_player, parent, false);
        Log.d(TAG,"inflated");
        TextView mNickName = (TextView)v.findViewById(R.id.player_nickname);
        mNickName.setText(mPlayer.getNickName());
        Log.d(TAG,"nickname");
    	ImageView mImage = (ImageView)v.findViewById(R.id.player_image);
    	//figure out what image i want to use here and how, hardcoded for now
    	mImage.setImageResource(R.drawable.ic_launcher);
    	Log.d(TAG,"image");
    	TextView mType = (TextView)v.findViewById(R.id.player_type);
    	mType.setText(mPlayer.getType());
    	Log.d(TAG,mPlayer.getType());
    	TextView mExp = (TextView)v.findViewById(R.id.player_exp);
    	mExp.setText(Integer.toString(mPlayer.getExperience()));
    	Log.d(TAG,Integer.toString(mPlayer.getExperience()));
    	TextView mCost = (TextView)v.findViewById(R.id.player_cost);
    	mCost.setText(String.valueOf(mPlayer.getCost()));
    	Log.d(TAG,"cost");
    	TextView mStrength = (TextView)v.findViewById(R.id.player_strength);
    	mStrength.setText(String.valueOf(mPlayer.getStrength()));
    	Log.d(TAG,"strength");
    	TextView mToughness = (TextView)v.findViewById(R.id.player_toughness);
    	mToughness.setText(String.valueOf(mPlayer.getToughness()));
    	Log.d(TAG,"toughness");
    	TextView mAgility = (TextView)v.findViewById(R.id.player_agility);
    	mAgility.setText(String.valueOf(mPlayer.getAgility()));
    	Log.d(TAG,"agility");
    	TextView mDodge = (TextView)v.findViewById(R.id.player_dodge);
    	mDodge.setText(String.valueOf(mPlayer.getDodge()));
    	Log.d(TAG,"dodge");
    	TextView mJump = (TextView)v.findViewById(R.id.player_jump);
    	mJump.setText(String.valueOf(mPlayer.getJump()));
    	Log.d(TAG,"jump");
    	TextView mHands = (TextView)v.findViewById(R.id.player_hands);
    	mHands.setText(String.valueOf(mPlayer.getHands()));
    	Log.d(TAG,"hands");
        return v;
    }

}
