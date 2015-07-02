package com.example.gearbowl;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class PlayerFragment extends Fragment {
	private Player mPlayer;
	private EditText mNickName;
	private TextView mType;
	private int mTeamNum;
	private int mPlayerNum;
	public static final String PLAYER_NUM_ID = "PLAYER_NUM_ID";
	public static final String PLAYER_TEAM_ID = "PLAYER_TEAM_ID";
	private static final String TAG = "playerFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        mPlayerNum = (int)getArguments().getInt(PLAYER_NUM_ID);
        mTeamNum = (int)getArguments().getInt(PLAYER_TEAM_ID);
       
        mPlayer = Game.get(getActivity()).getTeamPlayer(mTeamNum, mPlayerNum);
       
    }
    
    public static PlayerFragment newInstance(int playerNum,int teamNum){
    	Bundle args = new Bundle();
    	args.putInt(PLAYER_NUM_ID, playerNum);
    	args.putInt(PLAYER_TEAM_ID, teamNum);
    	
    	PlayerFragment fragment = new PlayerFragment();
    	fragment.setArguments(args);
    	
    	return fragment;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
            Bundle savedInstanceState) {
    	//Log.d(TAG,"OncreateView");
        View v = inflater.inflate(R.layout.fragment_player, parent, false);
        //Log.d(TAG,"inflated");
        mNickName = (EditText)v.findViewById(R.id.player_nickname);
        mNickName.setText(mPlayer.getNickName());
        mNickName.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				//run save team function
				//hard coded saveId for now, until i get the save dialogue up
				mNickName.setText("");
				
			}
		});
        mNickName.addTextChangedListener(new TextWatcher() {
	        @Override
	        public void afterTextChanged(Editable arg0) {
	        	String newNickName = mNickName.getText().toString();
	        	mPlayer.setNickName(newNickName);
	        	Game.get(getActivity()).setTeamPlayer(mTeamNum, mPlayerNum, mPlayer);
	        }
	        
	        @Override 
	        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
	        	
	        }
	        
	        @Override
	        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
	        	
	        }
	    });
        
    	ImageView mImage = (ImageView)v.findViewById(R.id.player_image);
    	
    	mImage.setImageResource(mPlayer.getImage());
    	
    	mType = (TextView)v.findViewById(R.id.player_type);
    	mType.setText(mPlayer.getType());
    	
    	TextView mExp = (TextView)v.findViewById(R.id.player_exp);
    	mExp.setText(Integer.toString(mPlayer.getExperience()));
    	
    	TextView mCost = (TextView)v.findViewById(R.id.player_cost);
    	mCost.setText(String.valueOf(mPlayer.getCost()));
    	
    	TextView mStrength = (TextView)v.findViewById(R.id.player_strength);
    	mStrength.setText(String.valueOf(mPlayer.getStrength()));
    	
    	TextView mToughness = (TextView)v.findViewById(R.id.player_toughness);
    	mToughness.setText(String.valueOf(mPlayer.getToughness()));
    	
    	TextView mAgility = (TextView)v.findViewById(R.id.player_agility);
    	mAgility.setText(String.valueOf(mPlayer.getAgility()));
    	
    	TextView mDodge = (TextView)v.findViewById(R.id.player_dodge);
    	mDodge.setText(String.valueOf(mPlayer.getDodge()));
    	
    	TextView mJump = (TextView)v.findViewById(R.id.player_jump);
    	mJump.setText(String.valueOf(mPlayer.getJump()));
    	
    	TextView mHands = (TextView)v.findViewById(R.id.player_hands);
    	mHands.setText(String.valueOf(mPlayer.getHands()));
    	
    	TextView mAP = (TextView)v.findViewById(R.id.player_ap);
    	mAP.setText(String.valueOf(mPlayer.getAP()));
    	
        return v;
    }

}
