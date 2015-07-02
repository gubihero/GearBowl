package com.example.gearbowl;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

public class PlayerPagerActivity extends FragmentActivity {
	private ViewPager mViewPager;
	private ArrayList<Player> mPlayers;
	private int mTeamNum;
	public static final String PLAYER_TEAM_ID = "PLAYER_TEAM_ID";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.viewPager);
		setContentView(mViewPager);
		
		mTeamNum = (int)getIntent().getIntExtra("PLAYER_TEAM_ID", 0);
		int mPlayerNum = (int)getIntent().getIntExtra("PLAYER_NUM_ID", 0);
		Team mTeam = Game.get(this).getTeam(mTeamNum);
		mPlayers = mTeam.getPlayers();
		
		FragmentManager fm = getSupportFragmentManager();
		mViewPager.setAdapter(new FragmentStatePagerAdapter(fm){
			@Override
			public int getCount() {
				return mPlayers.size();
			}
			
			@Override
			public Fragment getItem(int pos) {
				return PlayerFragment.newInstance(pos, mTeamNum);
			}
		});
		
		 mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
		        public void onPageScrollStateChanged(int state) { }

		        public void onPageScrolled(int pos, float posOffset, int posOffsetPixels) { }

		        public void onPageSelected(int pos) {
		            Player player = mPlayers.get(pos);
		            if (player.getNickName() != null) {
		                setTitle(player.getNickName());
		            }
		        }
		    });
		
		mViewPager.setCurrentItem(mPlayerNum);
	}

	
}
