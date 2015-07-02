package com.example.gearbowl;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

public class SaveFragment extends DialogFragment {
	private String mSave;
	private EditText mSaveText;
	private String[] mSaveKeys;
	
	private void sendResult(int resultCode) {
		if (getTargetFragment() == null) {
			return;
		}
		
		Intent i = new Intent();
		i.putExtra("saveString", mSave);
		getTargetFragment()
			.onActivityResult(getTargetRequestCode(), resultCode, i);
	}
	
	@Override
	public Dialog onCreateDialog(Bundle saveInstanceState) {
		mSaveKeys = Game.get(getActivity()).getSaves();
		View v = getActivity().getLayoutInflater()
			        .inflate(R.layout.dialog_save, null);
		mSaveText = (EditText)v.findViewById(R.id.saveText);
		mSaveText.addTextChangedListener(new TextWatcher() {
	        @Override
	        public void afterTextChanged(Editable arg0) {
	        	String newSave = mSaveText.getText().toString();
	        	mSave = newSave;
	        }
	        
	        @Override 
	        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
	        	
	        }
	        
	        @Override
	        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
	        	
	        }
	    });
		return new AlertDialog.Builder(getActivity())
			.setTitle(R.string.save_button)
			.setView(v)
			.setItems(mSaveKeys, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int which) {
	                   // The 'which' argument contains the index position
	                   // of the selected item
	            	   mSave = mSaveKeys[which];
	            	   sendResult(Activity.RESULT_OK);
	               }
			})
			.setPositiveButton(android.R.string.ok,new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    sendResult(Activity.RESULT_OK);
                }
            })
			.setNegativeButton(android.R.string.cancel, null)
			.create();
	}
}
