package com.cpic.rabbitfarm.fonts;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class PaperTextView extends TextView{

	public PaperTextView(Context context) {
		super(context);
		init(context);
	}
	
	public PaperTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public PaperTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		AssetManager asser = context.getAssets();
		Typeface font = Typeface.createFromAsset(asser, "fonts/cut_paper.ttf");
		setTypeface(font);
	}
}
