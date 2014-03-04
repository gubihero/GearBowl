package com.example.gearbowl;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class TeamFragment extends ListFragment {
	
	private static final String TAG = "TeamFragment";
	
	private Team mTeam;
	private int mTeamNum;
	private ArrayList <Player> mPlayers;
	private int mTeamCost;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().setTitle(R.string.team_builder_title);
		//need to make the team number dynamic by passing the num in from the activity
		mTeamNum = (int)getActivity().getIntent().getIntExtra("TEAM_NUM", 0);
		mTeam = Game.get(getActivity()).getTeam(mTeamNum);
		mPlayers = mTeam.getPlayers();
		
		PlayerAdapter adapter = new PlayerAdapter(mPlayers);
		setListAdapter(adapter);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Player p = ((PlayerAdapter)getListAdapter()).getItem(position);
		//Log.d(TAG,"onListItemClick");
		//start up the player profile activity of the player clicked
		Intent i = new Intent(getActivity(), PlayerActivity.class);
		i.putExtra(PlayerFragment.PLAYER_NUM_ID, position);
		//send over the team number
		i.putExtra(PlayerFragment.PLAYER_TEAM_ID, mTeamNum);
		startActivity(i);
	}
	
	private class PlayerAdapter extends ArrayAdapter<Player> {
		
		public PlayerAdapter(ArrayList<Player> players) {
			super(getActivity(), 0, players);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			//make a view if none given
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_player, null);
			}
			//get the info from the player passed in
			Player p = getItem(position);
			
			EditText playerNickName =
					(EditText)convertView.findViewById(R.id.player_list_nickname);
			playerNickName.setText(p.getNickName());
			//mPlayerNickName.addTextChangedListener(new TextWatcher() {
		    //    ...
		    //});
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
