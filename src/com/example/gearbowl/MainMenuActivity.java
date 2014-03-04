package com.example.gearbowl;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainMenuActivity extends Activity {

	//private static final String TAG = "MainMenuActivity";
	private Button mNewGameButton;
	private Button mLoadGameButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
	 	mNewGameButton = (Button)findViewById(R.id.new_game_button);
	 	mNewGameButton.setOnClickListener(new View.OnClickListener(){
        	@Override
        	public void onClick(View v) {
        		// start the create game activity
        		Intent i = new Intent(MainMenuActivity.this, CreateGameActivity.class);
        		startActivity(i);
        	}
        });
		mLoadGameButton = (Button)findViewById(R.id.load_game_button);
		mLoadGameButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				// start the load game activity
				//Intent i = new Intent(MainMenuActivity.this, LoadGameActivity.class);
				//startActivity(i);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

}
