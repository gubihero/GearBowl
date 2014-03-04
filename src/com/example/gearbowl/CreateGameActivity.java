package com.example.gearbowl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class CreateGameActivity extends Activity {
	private int ToggleOneTest; 
	
	private Button mPlayButton;
	private Button mBackButton;
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
		
		mBackButton = (Button)findViewById(R.id.back_button);
		mBackButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				// Go back to main screen activity
				Intent i = new Intent(CreateGameActivity.this, MainMenuActivity.class);
				startActivity(i);
			}
		});
		mPlayButton = (Button)findViewById(R.id.play_button);
		mPlayButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				// Start Game
				// Create New Game Instance with all the teams and map selected
				//Intent i = new Intent(CreateGameActivity.this, GameActivity.class);
				//startActivity(i);
			}
		});
		 mTeamOneCreate= (Button)findViewById(R.id.team_one_create);
		 mTeamOneCreate.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				// Send team number 1 over to the team builder page through the intent,
				// edit over there than send it back
				Intent i = new Intent(CreateGameActivity.this, TeamBuilderActivity.class);
				i.putExtra("TEAM_NUM", 1);
				startActivity(i);
			}
		});
		 mTeamTwoCreate= (Button)findViewById(R.id.team_two_create);
		 mTeamTwoCreate.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				// Send team number 2 over to the team builder page through the intent,
				// edit over there than send it back
				//Intent i = new Intent(CreateGameActivity.this, TeamBuilder.class);
				//startActivity(i);
			}
		});
		 mTeamThreeCreate= (Button)findViewById(R.id.team_three_create);
		 mTeamThreeCreate.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				// Send team number 3 over to the team builder page through the intent,
				// edit over there than send it back
				//Intent i = new Intent(CreateGameActivity.this, TeamBuilder.class);
				//startActivity(i);
			}
		});
		 mTeamFourCreate= (Button)findViewById(R.id.team_four_create);
		 mTeamFourCreate.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				// Send team number 4 over to the team builder page through the intent,
				// edit over there than send it back
				//Intent i = new Intent(CreateGameActivity.this, TeamBuilder.class);
				//startActivity(i);
			}
		});
		//specifically to test toggling off and on teams
		 ToggleOneTest = 1;
			
			
		 mTeamOneToggle= (ImageButton)findViewById(R.id.team_one_button);
		 mTeamOneToggle.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				//toggle through active, ai, inactive for team one
				if (ToggleOneTest == 1){
					ToggleOneTest = 2;
					Log.d("ToggleOneTest","1");
					mTeamOneToggle.setImageResource(R.drawable.red_team_button_dark_small);
				} 
				else {
					ToggleOneTest = 1;
					Log.d("ToggleOneTest","2");
					mTeamOneToggle.setImageResource(R.drawable.red_team_button_small);
					
				}
			}
		});
		 mTeamTwoToggle= (ImageButton)findViewById(R.id.team_two_button);
		 mTeamTwoToggle.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				//toggle through active, ai, inactive for team two
				
			}
		});
		 mTeamThreeToggle= (ImageButton)findViewById(R.id.team_three_button);
		 mTeamThreeToggle.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				//toggle through active, ai, inactive for team three
				
			}
		});
		 mTeamFourToggle= (ImageButton)findViewById(R.id.team_four_button);
		 mTeamFourToggle.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				//toggle through active, ai, inactive for team four
				
			}
		});
		 mMapLeft= (ImageButton)findViewById(R.id.map_left_button);
		 mMapLeft.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				//scan through minimaps to the left
				
			}
		});
		 mMapRight= (ImageButton)findViewById(R.id.map_right_button);
		 mMapRight.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				//scan though minimaps to the right
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_game, menu);
		return true;
	}

}
