package com.example.gearbowl;

import android.support.v4.app.Fragment;

public class PlayerActivity extends SingleFragmentActivity {
	
	@Override
	protected Fragment createFragment() {
		return new PlayerFragment();
	}
}
