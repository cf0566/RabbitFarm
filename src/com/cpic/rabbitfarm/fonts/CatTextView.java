package com.cpic.rabbitfarm.fonts;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CatTextView extends TextView{

	public CatTextView(Context context) {
		super(context);
		init(context);
	}
	
	public CatTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public CatTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		AssetManager asser = context.getAssets();
		Typeface font = Typeface.createFromAsset(asser, "fonts/cat.ttf");
		setTypeface(font);
	}
}
