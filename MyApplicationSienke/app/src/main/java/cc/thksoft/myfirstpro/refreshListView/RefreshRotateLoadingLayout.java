/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes. Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. You may obtain a copy of the
 * License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed
 * to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 *******************************************************************************/
package cc.thksoft.myfirstpro.refreshListView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView.ScaleType;


import com.example.administrator.thinker_soft.R;

import cc.thksoft.myfirstpro.refreshListView.RefreshPullToRefreshBase.Mode;


public class RefreshRotateLoadingLayout extends RefreshLoadingLayout {

  static final int ROTATION_ANIMATION_DURATION = 1200;

  private final Animation mRotateAnimation;

  private final Matrix mHeaderImageMatrix;

  private float mRotationPivotX, mRotationPivotY;

  public RefreshRotateLoadingLayout(Context context, Mode mode, int scrollDirection, TypedArray attrs, boolean topsignal, boolean bottomsignal) {
    super(context, mode, scrollDirection, attrs,topsignal,bottomsignal);

    mHeaderImage.setScaleType(ScaleType.MATRIX);
    mHeaderImageMatrix = new Matrix();
    mHeaderImage.setImageMatrix(mHeaderImageMatrix);

    mRotateAnimation =
        new RotateAnimation(0, 720, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
            0.5f);
    mRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
    mRotateAnimation.setDuration(ROTATION_ANIMATION_DURATION);
    mRotateAnimation.setRepeatCount(Animation.INFINITE);
    mRotateAnimation.setRepeatMode(Animation.RESTART);
  }

  public void onLoadingDrawableSet(Drawable imageDrawable) {
    if (null != imageDrawable) {
      mRotationPivotX = imageDrawable.getIntrinsicWidth() / 2f;
      mRotationPivotY = imageDrawable.getIntrinsicHeight() / 2f;
    }
  }

  protected void onPullImpl(float scaleOfLayout) {
    mHeaderImageMatrix.setRotate(scaleOfLayout * 90, mRotationPivotX, mRotationPivotY);
    mHeaderImage.setImageMatrix(mHeaderImageMatrix);
  }

  @Override
  protected void refreshingImpl() {
    mHeaderImage.startAnimation(mRotateAnimation);
  }

  @Override
  protected void resetImpl() {
    mHeaderImage.clearAnimation();
    resetImageRotation();
  }

  private void resetImageRotation() {
    if (null != mHeaderImageMatrix) {
      mHeaderImageMatrix.reset();
      mHeaderImage.setImageMatrix(mHeaderImageMatrix);
    }
  }

  @Override
  protected void pullToRefreshImpl() {
    // NO-OP
  }

  @Override
  protected void releaseToRefreshImpl() {
    // NO-OP
  }

  @Override
  protected int getDefaultStartDrawableResId(final int scrollDirection) {
    return R.mipmap.refresh_default_ptr_rotate;
  }

  @Override
  protected int getDefaultEndDrawableResId(final int scrollDirection) {
    return R.mipmap.refresh_default_ptr_rotate;
  }

}
