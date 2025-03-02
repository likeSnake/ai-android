package com.skythinker.gptassistant.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

public abstract class AdapterAbstract extends BaseAdapter implements AdapterViewLifeCycle {

	protected Context ctx;
	protected Activity activity;
	protected LayoutInflater inflater;

	public AdapterAbstract(Context context) {
		ctx = context;
		inflater = LayoutInflater.from(context);

		if (context instanceof Activity) {
			activity = (Activity) context;
		}
	}

	@Override
	public void onClear() {

	}

	@Override
	public void onReload() {

	}

}
