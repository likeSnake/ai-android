package com.skythinker.gptassistant.adapter;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public abstract class AdapterGeneritic<T> extends AdapterAbstract {

	protected final List<T> values = new ArrayList<T>();

	public AdapterGeneritic(Context context) {
		super(context);
	}

	@Override
	public int getCount() {
		return values.size();
	}

	@Override
	public final T getItem(int pos) {
		return values.get(pos);
	}

	@Override
	public final long getItemId(int pos) {
		return pos;
	}

	public void onLoadValues(List<T> list) {
		values.clear();
		if (list != null) {
			values.addAll(list);
		}
		notifyDataSetChanged();
	}

	@Override
	public void onClear() {
		values.clear();
		notifyDataSetChanged();
	}
}
