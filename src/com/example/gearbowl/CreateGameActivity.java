package com.example.gearbowl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class CreateGameActivity extends Activity { 
	
	private Button mPlayButton;
	private Button mClearButton;
	private Button mTeamOneCreate;
	private Button mTeamTwoCreate;
	private Button mTeamThreeCreate;
	private Button mTeamFourCreate;
	private ImageButton mTeamOneToggle;
	private ImageButton mTeamTwoToggle;
	private ImageButton mTeamThreeToggle;
	private ImageButton mTeamFourToggle;
	private TextView mTeamOneName;
	private TextView mTeamTwoName;
	private TextView mTeamThreeName;
	private TextView mTeamFourName;
	private ImageButton mMapLeft;
	private ImageButton mMapRight;
	
	private ImageView mMapView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_game);
		
		mTeamOneName = (TextView)findViewById(R.id.team_one_name);
		mTeamOneName.setText(Game.get(getApplicationContext()).getTeamName(0));
		
		mTeamTwoName = (TextView)findViewById(R.id.team_two_name);
		mTeamTwoName.setText(Game.get(getApplicationContext()).getTeamName(1));
		
		mTeamThreeName = (TextView)findViewById(R.id.team_three_name);
		mTeamThreeName.setText(Game.get(getApplicationContext()).getTeamName(2));
		
		mTeamFourName = (TextView)findViewById(R.id.team_four_name);
		mTeamFourName.setText(Game.get(getApplicationContext()).getTeamName(3));
		
		
		mClearButton = (Button)findViewById(R.id.create_clear_button);
		mClearButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				Team clearTeam = new Team();
				for(int iter = 0;iter<4;iter++){
					Game.get(getApplicationContext()).setTeam(iter, clearTeam);
				}
				mTeamOneName.setText(Game.get(getApplicationContext()).getTeamName(0));
				
				mTeamTwoName.setText(Game.get(getApplicationContext()).getTeamName(1));
			
				mTeamThreeName.setText(Game.get(getApplicationContext()).getTeamName(2));
				
				mTeamFourName.setText(Game.get(getApplicationContext()).getTeamName(3));
				
			}
		});
		mPlayButton = (Button)findViewById(R.id.play_button);
		mPlayButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				// Start Game
				// Create New Game Instance with all the teams and map selected
				Intent i = new Intent(CreateGameActivity.this, GameActivity.class);
				startActivity(i);
			}
		});
		 mTeamOneCreate= (Button)findViewById(R.id.team_one_create);
		 mTeamOneCreate.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				// Send team number 1 over to the team builder page through the intent,
				// edit over there than send it back
				Intent i = new Intent(CreateGameActivity.this, TeamBuilderActivity.class);
				i.putExtra("TEAM_NUM", 0);
				startActivity(i);
			}
		});
		 mTeamTwoCreate= (Button)findViewById(R.id.team_two_create);
		 mTeamTwoCreate.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				// Send team number 2 over to the team builder page through the intent,
				// edit over there than send it back
				Intent i = new Intent(CreateGameActivity.this, TeamBuilderActivity.class);
				i.putExtra("TEAM_NUM", 1);
				startActivity(i);
			}
		});
		 mTeamThreeCreate= (Button)findViewById(R.id.team_three_create);
		 mTeamThreeCreate.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				// Send team number 3 over to the team builder page through the intent,
				// edit over there than send it back
				Intent i = new Intent(CreateGameActivity.this, TeamBuilderActivity.class);
				i.putExtra("TEAM_NUM", 2);
				startActivity(i);
			}
		});
		 mTeamFourCreate= (Button)findViewById(R.id.team_four_create);
		 mTeamFourCreate.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				// Send team number 4 over to the team builder page through the intent,
				// edit over there than send it back
				Intent i = new Intent(CreateGameActivity.this, TeamBuilderActivity.class);
				i.putExtra("TEAM_NUM", 3);
				startActivity(i);
			}
		});	
		 mTeamOneToggle= (ImageButton)findViewById(R.id.team_one_button);
		 mTeamOneToggle.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				//toggle through active, inactive for team one for now
				if (Game.get(getApplicationContext()).getTeamStatus(0) == 0){
					Game.get(getApplicationContext()).setTeamStatus(0, 1);
					mTeamOneToggle.setImageResource(R.drawable.red_team_button_small);
				} 
				else if (Game.get(getApplicationContext()).getTeamStatus(0)==1) {
					Game.get(getApplicationContext()).setTeamStatus(0, 0);
					mTeamOneToggle.setImageResource(R.drawable.red_team_button_dark_small);
				}
			}
		});
		 mTeamTwoToggle = (ImageButton)findViewById(R.id.team_two_button);
		 mTeamTwoToggle.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				//toggle through active, ai, inactive for team two
				if (Game.get(getApplicationContext()).getTeamStatus(1) == 0){
					Game.get(getApplicationContext()).setTeamStatus(1, 1);
					mTeamTwoToggle.setImageResource(R.drawable.green_team_button_small);
				} 
				else if (Game.get(getApplicationContext()).getTeamStatus(1)==1) {
					Game.get(getApplicationContext()).setTeamStatus(1, 0);
					mTeamTwoToggle.setImageResource(R.drawable.green_team_button_dark_small);
				}
			}
		});
		 mTeamThreeToggle = (ImageButton)findViewById(R.id.team_three_button);
		 mTeamThreeToggle.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				//toggle through active, ai, inactive for team three
				if (Game.get(getApplicationContext()).getTeamStatus(2) == 0){
					Game.get(getApplicationContext()).setTeamStatus(2, 1);
					mTeamThreeToggle.setImageResource(R.drawable.blue_team_button_small);
				} 
				else if (Game.get(getApplicationContext()).getTeamStatus(2)==1) {
					Game.get(getApplicationContext()).setTeamStatus(2, 0);
					mTeamThreeToggle.setImageResource(R.drawable.blue_team_button_dark_small);
				}
			}
		});
		 mTeamFourToggle = (ImageButton)findViewById(R.id.team_four_button);
		 mTeamFourToggle.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				//toggle through active, ai, inactive for team four
				if (Game.get(getApplicationContext()).getTeamStatus(3) == 0){
					Game.get(getApplicationContext()).setTeamStatus(3, 1);
					mTeamFourToggle.setImageResource(R.drawable.yellow_team_button_small);
				} 
				else if (Game.get(getApplicationContext()).getTeamStatus(3)==1) {
					Game.get(getApplicationContext()).setTeamStatus(3, 0);
					mTeamFourToggle.setImageResource(R.drawable.yellow_team_button_dark_small);
				}
			}
		});
		 mMapLeft = (ImageButton)findViewById(R.id.map_left_button);
		 mMapLeft.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				//scan through minimaps to the left
				
			}
		});
		 mMapRight = (ImageButton)findViewById(R.id.map_right_button);
		 mMapRight.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				//scan though minimaps to the right
				
			}
		});
	}
	
	@Override
	public void onResume() {
		super.onResume();
		//figure it out
		mTeamOneName.setText(Game.get(getApplicationContext()).getTeamName(0));
		
		mTeamTwoName.setText(Game.get(getApplicationContext()).getTeamName(1));
	
		mTeamThreeName.setText(Game.get(getApplicationContext()).getTeamName(2));
		
		mTeamFourName.setText(Game.get(getApplicationContext()).getTeamName(3));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_game, menu);
		return true;
	}

}
