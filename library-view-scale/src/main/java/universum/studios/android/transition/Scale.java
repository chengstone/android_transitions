/*
 * =================================================================================================
 *                             Copyright (C) 2017 Universum Studios
 * =================================================================================================
 *         Licensed under the Apache License, Version 2.0 or later (further "License" only).
 * -------------------------------------------------------------------------------------------------
 * You may use this file only in compliance with the License. More details and copy of this License 
 * you may obtain at
 * 
 * 		http://www.apache.org/licenses/LICENSE-2.0
 * 
 * You can redistribute, modify or publish any part of the code written within this file but as it 
 * is described in the License, the software distributed under the License is distributed on an 
 * "AS IS" BASIS, WITHOUT WARRANTIES or CONDITIONS OF ANY KIND.
 * 
 * See the License for the specific language governing permissions and limitations under the License.
 * =================================================================================================
 */
package universum.studios.android.transition;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.FloatRange;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.transition.TransitionValues;
import android.transition.Visibility;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import universum.studios.android.transition.view.scale.R;

/**
 * A {@link Visibility} transition implementation that tracks changes to the visibility of target
 * views in the start and end scenes and scales up or down views in the scene. Visibility is
 * determined by the {@link View#setVisibility(int)} state of the views.
 * <p>
 * Scale transition can be described in a resource file by using the {@code transition} tag with
 * {@code class} attribute set to {@code universum.studios.android.transition.Scale}, along with
 * Xml attributes referenced below.
 *
 * <h3>XML attributes</h3>
 * {@link R.styleable#Ui_Transition_Scale Scale Attributes}
 *
 * @author Martin Albedinsky
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class Scale extends Visibility {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "Scale";

	/**
	 * Defines an annotation for determining set of allowed modes for Scale transition.
	 */
	@Retention(RetentionPolicy.SOURCE)
	@IntDef(flag = true, value = {MODE_IN, MODE_OUT})
	public @interface ScaleMode {
	}

	/**
	 * Name of the view's property used to scale size of a specific view along its x axis.
	 */
	public static final String PROPERTY_SCALE_X = "scaleX";

	/**
	 * Name of the view's property used to scale size of a specific view along its y axis.
	 */
	public static final String PROPERTY_SCALE_Y = "scaleY";

	/**
	 * Default value for scale fraction.
	 */
	private static final float SCALE_FRACTION = 0.5f;

	/*
	 * Interface ===================================================================================
	 */

	/*
	 * Static members ==============================================================================
	 */

	/*
	 * Members =====================================================================================
	 */

	/**
	 * Object that holds all necessary properties for the scale transition for the currently
	 * transitioning view.
	 */
	private final Info mInfo = new Info();

	/**
	 * X coordinate for scaling pivot of the animating view. If {@code null}, {@link #mPivotXFraction}
	 * should be used to calculate this coordinate.
	 */
	private Float mPivotX;

	/**
	 * Y coordinate for scaling pivot of the animating view. If {@code null}, {@link #mPivotYFraction}
	 * should be used to calculate this coordinate.
	 */
	private Float mPivotY;

	/**
	 * Fraction from the {@code [0.0, 1.0]} range that can be used to calculate X coordinate for scaling
	 * pivot of the animating view if {@link #mPivotX} coordinate has not been specified.
	 */
	private float mPivotXFraction = SCALE_FRACTION;

	/**
	 * Fraction from the {@code [0.0, 1.0]} range that can be used to calculate Y coordinate for scaling
	 * pivot of the animating view if {@link #mPivotY} coordinate has not been specified.
	 */
	private float mPivotYFraction = SCALE_FRACTION;

	/*
	 * Constructors ================================================================================
	 */

	/**
	 * Same as {@link #Scale(int)} with both {@link #MODE_IN} and {@link #MODE_OUT} modes combined.
	 */
	public Scale() {
		this(MODE_IN | MODE_OUT);
	}

	/**
	 * Creates a new instance of Scale transition with the specified <var>mode</var>.
	 *
	 * @param mode One of {@link #MODE_IN} or {@link #MODE_OUT} or theirs combination.
	 */
	public Scale(@ScaleMode final int mode) {
		super();
		setMode(mode);
	}

	/**
	 * Creates a new instance of Scale transition with animation property values set from the
	 * specified <var>attrs</var>.
	 *
	 * @param context Context used to obtain values from the specified <var>attrs</var>.
	 * @param attrs   Set of attributes from which to obtain animation property values for the scale
	 *                animation.
	 */
	public Scale(@NonNull final Context context, @NonNull final AttributeSet attrs) {
		super(context, attrs);
		final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Ui_Transition_Scale, 0, 0);
		final int n = typedArray.getIndexCount();
		for (int i = 0; i < n; i++) {
			final int index = typedArray.getIndex(i);
			if (index == R.styleable.Ui_Transition_Scale_android_pivotX) {
				this.mPivotXFraction = typedArray.getFraction(index, 1, 1, mPivotXFraction);
			} else if (index == R.styleable.Ui_Transition_Scale_android_pivotY) {
				this.mPivotYFraction = typedArray.getFraction(index, 1, 1, mPivotYFraction);
			} else if (index == R.styleable.Ui_Transition_Scale_android_transformPivotX) {
				this.mPivotX = (float) typedArray.getDimensionPixelSize(index, 0);
			} else if (index == R.styleable.Ui_Transition_Scale_android_transformPivotY) {
				this.mPivotY = (float) typedArray.getDimensionPixelSize(index, 0);
			}
		}
		typedArray.recycle();
	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Creates a new instance of scale Animator for the specified <var>view</var>.
	 *
	 * @param view       The view for which to create the requested animator.
	 * @param startScale Scale from which to start the animation.
	 * @param endScale   Scale at which should the animation end.
	 * @return Animator that will play scale animation for the specified view according to the
	 * specified parameters when started.
	 */
	@NonNull
	public static Animator createAnimator(
			@NonNull final View view,
			@FloatRange(from = 0, to = 1) final float startScale,
			@FloatRange(from = 0, to = 1) final float endScale
	) {
		return ObjectAnimator.ofPropertyValuesHolder(
				view,
				PropertyValuesHolder.ofFloat(PROPERTY_SCALE_X, startScale, endScale),
				PropertyValuesHolder.ofFloat(PROPERTY_SCALE_Y, startScale, endScale)
		);
	}

	/**
	 * Sets a pivot x coordinate for scale animation.
	 * <p>
	 * Default value: <b>{@code null}</b>
	 *
	 * @param pivotX The desired pivot x coordinate in pixels. May be {@code null} to use pivot
	 *               fraction specified via {@link #setPivotXFraction(float)} instead.
	 * @see #getPivotX()
	 * @see #setPivotXFraction(float)
	 * @see #setPivotY(Float)
	 */
	public void setPivotX(@Nullable final Float pivotX) {
		this.mPivotX = pivotX;
	}

	/**
	 * Returns the x coordinate for pivot of the scale animation.
	 *
	 * @return Pivot in pixels or {@code null} if no pivot has been specified.
	 * @see #setPivotX(Float)
	 */
	@Nullable
	public Float getPivotX() {
		return mPivotX;
	}

	/**
	 * Sets a pivot y coordinate for scale animation.
	 * <p>
	 * Default value: <b>{@code null}</b>
	 *
	 * @param pivotY The desired pivot y coordinate in pixels. May be {@code null} to use pivot
	 *               fraction specified via {@link #setPivotYFraction(float)} instead.
	 * @see #getPivotY()
	 * @see #setPivotYFraction(float)
	 * @see #setPivotX(Float)
	 */
	public void setPivotY(@Nullable final Float pivotY) {
		this.mPivotY = pivotY;
	}

	/**
	 * Returns the y coordinate for pivot of the scale animation.
	 *
	 * @return Pivot in pixels or {@code null} if no pivot has been specified.
	 * @see #setPivotY(Float)
	 */
	@Nullable
	public Float getPivotY() {
		return mPivotY;
	}

	/**
	 * Sets a fraction for the <b>pivot x</b> coordinate. This fraction will be used to resolve
	 * pivot x coordinate of an animating view depending on its current <b>width</b> if such value
	 * has not been specified via {@link #setPivotX(Float)}.
	 * <p>
	 * Default value: <b>{@code 0.5}</b>
	 *
	 * @param fractionX The desired fraction from the {@code [0.0, 1.0]} range.
	 * @see #getPivotYFraction()
	 * @see #setPivotXFraction(float)
	 * @see #setPivotX(Float)
	 */
	public void setPivotXFraction(@FloatRange(from = 0, to = 1) final float fractionX) {
		this.mPivotXFraction = fractionX;
	}

	/**
	 * Returns the fraction of the <b>pivot x</b> coordinate.
	 *
	 * @return Fraction from the {@code [0.0, 1.0]} range.
	 * @see #setPivotXFraction(float)
	 */
	@FloatRange(from = 0, to = 1)
	public float getPivotXFraction() {
		return mPivotXFraction;
	}

	/**
	 * Sets a fraction for the <b>pivot y</b> coordinate. This fraction will be used to resolve
	 * pivot y coordinate of an animating view depending on its current <b>height</b> if such value
	 * has not been specified via {@link #setPivotY(Float)}.
	 * <p>
	 * Default value: <b>{@code 0.5}</b>
	 *
	 * @param fractionY The desired fraction from the {@code [0.0, 1.0]} range.
	 * @see #getPivotXFraction()
	 * @see #setPivotYFraction(float)
	 * @see #setPivotY(Float)
	 */
	public void setPivotYFraction(@FloatRange(from = 0, to = 1) final float fractionY) {
		this.mPivotYFraction = fractionY;
	}

	/**
	 * Returns the fraction of the <b>pivot y</b> coordinate.
	 *
	 * @return Fraction from the {@code [0.0, 1.0]} range.
	 * @see #setPivotYFraction(float)
	 */
	@FloatRange(from = 0, to = 1)
	public float getPivotYFraction() {
		return mPivotYFraction;
	}

	/**
	 */
	@Override
	public Animator onAppear(final ViewGroup sceneRoot, final View view, final TransitionValues startValues, final TransitionValues endValues) {
		calculateTransitionProperties(view, 0f, 1f);
		view.setPivotX(mInfo.pivotX);
		view.setPivotY(mInfo.pivotY);
		view.setScaleX(mInfo.startScale);
		view.setScaleY(mInfo.startScale);
		return createAnimator(view, mInfo.startScale, mInfo.endScale);
	}

	/**
	 */
	@Override
	public Animator onDisappear(final ViewGroup sceneRoot, final View view, final TransitionValues startValues, final TransitionValues endValues) {
		calculateTransitionProperties(view, 1f, 0f);
		view.setPivotX(mInfo.pivotX);
		view.setPivotY(mInfo.pivotY);
		view.setScaleX(mInfo.startScale);
		view.setScaleY(mInfo.startScale);
		return createAnimator(view, mInfo.startScale, mInfo.endScale);
	}

	/**
	 * Ensures that all necessary properties for this transition are calculated.
	 *
	 * @param view The view to which will be the transition applied.
	 */
	private void calculateTransitionProperties(final View view, final float startScale, final float endScale) {
		mInfo.startScale = startScale;
		mInfo.endScale = endScale;
		mInfo.pivotX = mPivotX == null ? (view.getWidth() * mPivotXFraction) : mPivotX;
		mInfo.pivotY = mPivotY == null ? (view.getHeight() * mPivotYFraction) : mPivotY;
	}

	/*
	 * Inner classes ===============================================================================
	 */

	/**
	 * Class holding necessary values for the scale transition that are exclusively associated with
	 * the currently transitioning view.
	 */
	private static final class Info {

		/**
		 * Scale value.
		 */
		float startScale, endScale;

		/**
		 * Scale pivot coordinate.
		 */
		float pivotX, pivotY;
	}
}
