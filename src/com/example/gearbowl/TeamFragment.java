package com.example.gearbowl;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

public class TeamFragment extends ListFragment {
	
	private static final String TAG = "TeamFragment";
	
	private Team mTeam;
	private int mTeamNum;
	private EditText mTeamName;
	private ImageView mPlayerDisplay;
	private ListView mPlayersList;
	private ArrayList <Player> mPlayers;
	private ArrayList <Player> mUnits;
	private int mCurrentUnitIndex = 0;
	private int mTeamCost;
	private PlayerAdapter mAdapter;
	//active hire button, fire button
	private ToggleButton mHireButton;
	private ToggleButton mFireButton;
	private boolean mHireMode;
	private boolean mFireMode;
	private static final String DIALOG_SAVE = "save";
	private static final int REQUEST_SAVE = 0;
	
	private static final String DIALOG_LOAD = "load";
	private static final int REQUEST_LOAD = 1;
	
	private String mSaveKey;
	private String mLoadKey;
	
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().setTitle(R.string.team_builder_title);
		
		mTeamNum = (int)getActivity().getIntent().getIntExtra("TEAM_NUM", 0);
		mTeam = Game.get(getActivity()).getTeam(mTeamNum);
		mPlayers = mTeam.getPlayers();
		mUnits = Game.get(getActivity()).loadUnits();
		
		mAdapter = new PlayerAdapter(mPlayers);
		setListAdapter(mAdapter); 
		mHireMode = false;
		mFireMode = false;
		
		
	}
	
	public void switchUnit(int dir){
		//1 right, -1 left
		if (mCurrentUnitIndex + dir >= mUnits.size()) {
			mCurrentUnitIndex = 0;
		} else if (mCurrentUnitIndex + dir < 0) {
			mCurrentUnitIndex = mUnits.size() - 1;
		} else {
			mCurrentUnitIndex += dir; 
		}
		mPlayerDisplay.setImageResource(mUnits.get(mCurrentUnitIndex).getImage());
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
            Bundle savedInstanceState) {
    	
        View v = inflater.inflate(R.layout.list_container, parent, false);
       
        mTeamName = (EditText)v.findViewById(R.id.team_name);
        mTeamName.setText(mTeam.getName());
        mTeamName.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				//run save team function
				//hard coded saveId for now, until i get the save dialogue up
				mTeamName.setText("");
				
			}
		});
        mTeamName.addTextChangedListener(new TextWatcher() {
	        @Override
	        public void afterTextChanged(Editable arg0) {
	        	String newTeamName = mTeamName.getText().toString();
	        	mTeam.setName(newTeamName);
	        	Game.get(getActivity()).setTeamName(mTeamNum,newTeamName);
	        }
	        
	        @Override 
	        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
	        	
	        }
	        
	        @Override
	        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
	        	
	        }
	    });
        
    	Button mSave = (Button)v.findViewById(R.id.team_save);
    	mSave.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				//run save team function
				//will pull up the saveDialogFragment
				FragmentManager fm = getActivity()
						.getSupportFragmentManager();
				SaveFragment dialog = new SaveFragment();
				dialog.setTargetFragment(TeamFragment.this,REQUEST_SAVE);
				dialog.show(fm, DIALOG_SAVE);
				//the DBsave will happen in onActivityResult
			}
		});
    	
    	Button mLoad = (Button)v.findViewById(R.id.team_load);
    	mLoad.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				//run load team function fragment 
				FragmentManager fm = getActivity()
						.getSupportFragmentManager();
				LoadFragment dialog = new LoadFragment();
				dialog.setTargetFragment(TeamFragment.this,REQUEST_LOAD);
				dialog.show(fm, DIALOG_LOAD);
				//The DBload will happen in onActivityResult
			}
		});
    	
    	ImageButton mLeft = (ImageButton)v.findViewById(R.id.player_left);
    	mLeft.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				//cycle left through player profiles to hire fire
				switchUnit(-1);
			}
		});
    	mPlayerDisplay = (ImageView)v.findViewById(R.id.player_selection_image);
    	
    	mPlayerDisplay.setImageResource(R.drawable.smashbot);
    	ImageButton mRight = (ImageButton)v.findViewById(R.id.player_right);
    	mRight.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				//cycle right through player profiles to hire fire
				switchUnit(1);
			}
		});
    	
    	mHireButton = (ToggleButton)v.findViewById(R.id.player_hire);
    	mHireButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				//hire current player selected
				if (mHireMode) {
					mHireMode = false;
					mHireButton.setChecked(mHireMode);
				} else {
					mHireMode = true;
					mHireButton.setChecked(mHireMode);
					if(mFireMode){
						mFireMode = false;
						mFireButton.setChecked(mFireMode);
					}
				}
			}
		});
    	mFireButton = (ToggleButton)v.findViewById(R.id.player_fire);
    	mFireButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				//fire current player selected
				if (mFireMode) {
					mFireMode = false;
					mFireButton.setChecked(mFireMode);
				} else {
					mFireMode = true;
					mFireButton.setChecked(mFireMode);
					if(mHireMode){
						mHireMode = false;
						mHireButton.setChecked(mHireMode);
					}
				}
				
			}
		});
    	
    	//mPlayersList = (ListView)v.findViewById(R.id.);
    	Button mClear = (Button)v.findViewById(R.id.team_clear);
    	mClear.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				//clear current team
				Team clearTeam = new Team();
				Game.get(getActivity()).setTeam(mTeamNum, clearTeam);
				mPlayers = clearTeam.getPlayers();
				mAdapter.clear();
				mAdapter.addAll(mPlayers);
				mAdapter.notifyDataSetChanged();
				mTeamName.setText("Empty");
				if (mHireMode) {
					mHireMode = false;
					mHireButton.setChecked(mHireMode);
				}
				if (mFireMode) {
					mFireMode = false;
					mFireButton.setChecked(mFireMode);
				}
				
				
			}
		});
        return v;
    }
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) {
			return;
		}
		if (requestCode == REQUEST_SAVE) {
			// if the fragment returns with REQUEST_SAVE
			//Save team to DB
			mSaveKey = data.getStringExtra("saveString");
			ArrayList <String> saves = Game.get(getActivity()).getSavesArray();
			if (saves.contains(mSaveKey)){
				Game.get(getActivity()).updateSavedTeam(mTeamNum, mSaveKey);
			} else {
				Game.get(getActivity()).saveTeam(mTeamNum, mSaveKey);
			}
			Game.get(getActivity()).saveTeam(mTeamNum, mSaveKey);
		}
		if (requestCode == REQUEST_LOAD) {
			//if fragment returns with REQUEST_LOAD
			//load team from DB
			mLoadKey = data.getStringExtra("loadString");
			mTeam = Game.get(getActivity()).loadTeam(mTeamNum, mLoadKey);
			mAdapter.clear();
			mAdapter.addAll(mTeam.getPlayers());
			mAdapter.notifyDataSetChanged();
			mTeamName.setText(mTeam.getName());
		}
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		Log.d("TeamFragment","inOnResume");
		((PlayerAdapter)getListAdapter()).notifyDataSetChanged();
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		if(mHireMode){
			Player player = new Player();
			player.setType(mUnits.get(mCurrentUnitIndex).getType());
			player.setStrength(mUnits.get(mCurrentUnitIndex).getStrength());
			player.setToughness(mUnits.get(mCurrentUnitIndex).getToughness());
			player.setAgility(mUnits.get(mCurrentUnitIndex).getAgility());
			player.setDodge(mUnits.get(mCurrentUnitIndex).getDodge());
			player.setHands(mUnits.get(mCurrentUnitIndex).getHands());
			player.setJump(mUnits.get(mCurrentUnitIndex).getJump());
			player.setCost(mUnits.get(mCurrentUnitIndex).getCost());
			player.setAP(mUnits.get(mCurrentUnitIndex).getAP());
			player.setImage(mUnits.get(mCurrentUnitIndex).getImage());
			Game.get(getActivity()).setTeamPlayer(mTeamNum, position, player);
			mAdapter.clear();
			mAdapter.addAll(Game.get(getActivity()).getTeam(mTeamNum).getPlayers());
			mAdapter.notifyDataSetChanged();
		} else if (mFireMode) {
			Player fired = new Player();
			Game.get(getActivity()).setTeamPlayer(mTeamNum, position, fired);
			mAdapter.clear();
			mAdapter.addAll(Game.get(getActivity()).getTeam(mTeamNum).getPlayers());
			mAdapter.notifyDataSetChanged();
		} else {
			Player p = ((PlayerAdapter)getListAdapter()).getItem(position);
			
			//start up the player profile activity of the player clicked
			Intent i = new Intent(getActivity(), PlayerPagerActivity.class);
			i.putExtra(PlayerFragment.PLAYER_NUM_ID, position);
			//send over the team number
			i.putExtra(PlayerFragment.PLAYER_TEAM_ID, mTeamNum);
			startActivity(i);
		}
	}
	
	private class PlayerAdapter extends ArrayAdapter<Player> {
		
		public PlayerAdapter(ArrayList<Player> players) {
			super(getActivity(), 0, players);
		}
		
		public void addAll(ArrayList<Player> players) {
			for (int iter = 0; iter < players.size(); iter++) {
				this.add(players.get(iter));
			}
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			//make a view if none given
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_player, null);
			}
			//get the info from the player passed in
			Player p = getItem(position);
			
			TextView playerNickName =
					(TextView)convertView.findViewById(R.id.player_list_nickname);
			playerNickName.setText(p.getNickName());
			TextView playerType = 
					(TextView)convertView.findViewById(R.id.player_list_type);
			playerType.setText(p.getType());
			TextView playerExp =
					(TextView)convertView.findViewById(R.id.player_list_exp);
			playerExp.setText(p.getExperience()+"xp");
			TextView playerCost = 
					(TextView)convertView.findViewById(R.id.player_list_cost);
			playerCost.setText("$" + p.getCost());
			
			return convertView;
		}
	}
}
