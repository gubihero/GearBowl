package com.example.gearbowl;

import android.support.v4.app.Fragment;

public class TeamBuilderActivity extends SingleFragmentActivity {
	
	@Override
	protected Fragment createFragment() {
		return new TeamFragment();
	}

}
