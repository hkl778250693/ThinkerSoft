/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package zxing.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.view.View;

import com.example.administrator.thinker_soft.R;
import com.google.zxing.ResultPoint;

import java.util.Collection;
import java.util.HashSet;

public final class ViewfinderView extends View {

	private static final int[] SCANNER_ALPHA = { 0, 64, 128, 192, 255, 192,
			128, 64 };
	private static final long ANIMATION_DELAY = 100L;
	private static final int OPAQUE = 0xFF;

	private final Paint paint;
	private Bitmap resultBitmap;
	private final int maskColor;
	private final int resultColor;
	private final int frameColor;
	private final int laserColor;
	private final int resultPointColor;
	private int scannerAlpha;
	private Collection<ResultPoint> possibleResultPoints;
	private Resources resources;
	private int lineWidth;
	private int lineHeight;
	private int moveLength;
	private boolean signal = true;
	private LinearGradient gradient;
	private int[] color =   
	        new int[]{Color.TRANSPARENT,Color.WHITE,Color.YELLOW,Color.YELLOW,Color.YELLOW,Color.WHITE,Color.TRANSPARENT}; 
	// This constructor is used when the class is built from an XML resource.
	public ViewfinderView(Context context, AttributeSet attrs) {
		super(context, attrs);

		// Initialize these once for performance rather than calling them every
		// time in onDraw().
		paint = new Paint();
		resources = getResources();
		maskColor = resources.getColor(R.color.viewfinder_mask);
		resultColor = resources.getColor(R.color.result_view);
		frameColor = resources.getColor(R.color.viewfinder_frame);
		laserColor = resources.getColor(R.color.viewfinder_laser);
		resultPointColor = resources.getColor(R.color.possible_result_points);
		scannerAlpha = 100;
		possibleResultPoints = new HashSet<ResultPoint>(5);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		lineWidth = this.getWidth();
		lineHeight = this.getHeight();
		if(moveLength+5>=lineHeight){
			signal = false;
		}else if(moveLength - 5 <= 0){
			signal = true;
		}	
		if(signal == true){
			moveLength = moveLength + 5;
		}else{
			moveLength = moveLength - 5;
		}
		paint.setColor(resources.getColor(R.color.onceagain));
		canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
		paint.setColor(resources.getColor(R.color.bblue));
		paint.setStrokeWidth(10);
		paint.setStyle(Style.FILL_AND_STROKE);
		gradient  = new LinearGradient(10,moveLength,getWidth()-10,moveLength,color,null,TileMode.REPEAT);  
		paint.setShader(gradient);
		paint.setAlpha(80);
		canvas.drawLine(10, moveLength, getWidth()-10, moveLength, paint);
	}
	public void drawViewfinder() {
		resultBitmap = null;
		invalidate();
	}

	/**
	 * Draw a bitmap with the result points highlighted instead of the live
	 * scanning display.
	 * 
	 * @param barcode
	 *            An image of the decoded barcode.
	 */
	public void drawResultBitmap(Bitmap barcode) {
		resultBitmap = barcode;
		invalidate();
	}

	public void addPossibleResultPoint(ResultPoint point) {
		possibleResultPoints.add(point);
	}
}
