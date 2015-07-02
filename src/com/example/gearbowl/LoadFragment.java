package com.example.gearbowl;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class LoadFragment extends DialogFragment {

	private String mLoad;
	private String[] mLoadKeys;
	
	private void sendResult(int resultCode) {
		if (getTargetFragment() == null) {
			return;
		}
		
		Intent i = new Intent();
		i.putExtra("loadString", mLoad);
		getTargetFragment()
			.onActivityResult(getTargetRequestCode(), resultCode, i);
	}
	
	@Override
	public Dialog onCreateDialog(Bundle saveInstanceState) {
		mLoadKeys = Game.get(getActivity()).getSaves();
		
		AlertDialog.Builder loadDialogBuilder = new AlertDialog.Builder(getActivity())
		.setTitle(R.string.load_button)
		.setItems(mLoadKeys, new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int which) {
                   // The 'which' argument contains the index position
                   // of the selected item
            	   mLoad = mLoadKeys[which];
            	   sendResult(Activity.RESULT_OK);
               }
		})
		.setNegativeButton(android.R.string.cancel, null);
		return loadDialogBuilder.create();
	}
}
