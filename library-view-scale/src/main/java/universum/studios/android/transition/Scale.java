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
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.FloatRange;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.Size;
import android.support.annotation.VisibleForTesting;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.transition.TransitionValues;
import android.transition.Visibility;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import universum.studios.android.transition.util.TransitionUtils;

/**
 * A {@link Visibility} transition implementation that tracks changes to the visibility of target
 * views in the start and end scenes and scales up or down views in the scene. Visibility is
 * determined by both the {@link View#setVisibility(int)} state of the view as well as whether it
 * is parented in the current view hierarchy. Disappearing Views are limited as described in
 * {@link Visibility#onDisappear(android.view.ViewGroup, TransitionValues, int, TransitionValues, int)}.
 * <p>
 * Scaling pivot for both X and Y axes, as concrete positions, may be specified via {@link #setPivotX(Float)}
 * and {@link #setPivotY(Float)}. In order too specify X and Y pivots as relative positions/fractions,
 * {@link #setPivotXFraction(float)} along with {@link #setPivotYFraction(float)} may be used.
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
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
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
	 * Minimum value for scale property.
	 */
	public static final float MIN = 0.0f;

	/**
	 * Maximum value for scale property
	 */
	public static final float MAX = 1.0f;

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

	/**
	 * Start scale for animation when created for purpose of {@link #onAppear(ViewGroup, View, TransitionValues, TransitionValues)}.
	 */
	private static final float START_SCALE_ON_APPEAR = 0.0f;

	/**
	 * Start scale for animation when created for purpose of {@link #onDisappear(ViewGroup, View, TransitionValues, TransitionValues)}.
	 */
	private static final float START_SCALE_ON_DISAPPEAR = 1.0f;

	/**
	 * Name of the property holding scale X value for animating view in {@link TransitionValues}.
	 */
	@VisibleForTesting
	static final String PROPERTY_TRANSITION_SCALE_X = Scale.class.getName() + ":transition.scaleX";

	/**
	 * Name of the property holding scale Y value for animating view in {@link TransitionValues}.
	 */
	@VisibleForTesting
	static final String PROPERTY_TRANSITION_SCALE_Y = Scale.class.getName() + ":transition.scaleY";

	/*
	 * Interface ===================================================================================
	 */

	/*
	 * Static members ==============================================================================
	 */

	/**
	 * Default interpolator attached to {@link Animator} created via {@link #createAnimator(View, float, float, float, float)}.
	 */
	public static final TimeInterpolator INTERPOLATOR = new FastOutSlowInInterpolator();

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
	public Scale(@NonNull final Context context, @Nullable final AttributeSet attrs) {
		super(context, attrs);
		final TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.Ui_Transition_Scale, 0, 0);
		final int attributeCount = attributes.getIndexCount();
		for (int i = 0; i < attributeCount; i++) {
			final int attrIndex = attributes.getIndex(i);
			if (attrIndex == R.styleable.Ui_Transition_Scale_android_pivotX) {
				this.mPivotXFraction = attributes.getFraction(attrIndex, 1, 1, mPivotXFraction);
			} else if (attrIndex == R.styleable.Ui_Transition_Scale_android_pivotY) {
				this.mPivotYFraction = attributes.getFraction(attrIndex, 1, 1, mPivotYFraction);
			} else if (attrIndex == R.styleable.Ui_Transition_Scale_android_transformPivotX) {
				this.mPivotX = (float) attributes.getDimensionPixelSize(attrIndex, 0);
			} else if (attrIndex == R.styleable.Ui_Transition_Scale_android_transformPivotY) {
				this.mPivotY = (float) attributes.getDimensionPixelSize(attrIndex, 0);
			}
		}
		attributes.recycle();
	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Creates a new instance of Animator that animates both, scale X and scale Y, properties of
	 * the specified <var>view</var>.
	 * <p>
	 * The returned animator will also have the default {@link #INTERPOLATOR} attached.
	 *
	 * @param view       The view for which to create the requested animator.
	 * @param startScale Scale from which to start the animation along X and Y axis.
	 * @param endScale   Scale at which should the animation end along X and Y axis.
	 * @return Animator that will play scale animation for the specified view according to the
	 * specified parameters when started or {@code null} if the start and end scale values are the same.
	 * @see #createAnimator(View, float, float, float, float)
	 */
	@Nullable
	public static Animator createAnimator(
			@NonNull final View view,
			@FloatRange(from = MIN, to = MAX) final float startScale,
			@FloatRange(from = MIN, to = MAX) final float endScale
	) {
		return createAnimator(view, startScale, startScale, endScale, endScale);
	}

	/**
	 * Creates a new instance of Animator that animates both, scale X and scale Y, properties of
	 * the specified <var>view</var>.
	 * <p>
	 * The returned animator will also have the default {@link #INTERPOLATOR} attached.
	 *
	 * @param view        The view for which to create the requested animator.
	 * @param startScaleX Scale from which to start the animation along X axis.
	 * @param startScaleY Scale from which to start the animation along Y axis.
	 * @param endScaleX   Scale at which should the animation end along X axis.
	 * @param endScaleY   Scale at which should the animation end along Y axis.
	 * @return Animator that will play scale animation for the specified view according to the
	 * specified parameters when started or {@code null} if the start and end scale values are the
	 * same or the target view is already detached from window.
	 */
	@Nullable
	public static Animator createAnimator(
			@NonNull final View view,
			@FloatRange(from = MIN, to = MAX) final float startScaleX,
			@FloatRange(from = MIN, to = MAX) final float startScaleY,
			@FloatRange(from = MIN, to = MAX) final float endScaleX,
			@FloatRange(from = MIN, to = MAX) final float endScaleY
	) {
		if (!TransitionUtils.isViewAttachedToWindow(view)) {
			return null;
		}
		final float startX = Math.max(0, Math.min(1, startScaleX));
		final float startY = Math.max(0, Math.min(1, startScaleY));
		final float endX = Math.max(0, Math.min(1, endScaleX));
		final float endY = Math.max(0, Math.min(1, endScaleY));
		if (startX == endX && startY == endY) {
			return null;
		}
		view.setScaleX(startX);
		view.setScaleY(startY);
		final ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(
				view,
				PropertyValuesHolder.ofFloat(PROPERTY_SCALE_X, startX, endX),
				PropertyValuesHolder.ofFloat(PROPERTY_SCALE_Y, startY, endY)
		);
		animator.setInterpolator(INTERPOLATOR);
		return animator;
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
	 * @see android.R.attr#transformPivotX android:transformPivotX
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
	 * @see android.R.attr#transformPivotY android:transformPivotY
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
	 * @see android.R.attr#pivotX android:pivotX
	 */
	public void setPivotXFraction(@FloatRange(from = 0, to = 1) final float fractionX) {
		this.mPivotXFraction = Math.max(0, Math.min(1, fractionX));
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
	 * @see android.R.attr#pivotY android:pivotY
	 */
	public void setPivotYFraction(@FloatRange(from = 0, to = 1) final float fractionY) {
		this.mPivotYFraction = Math.max(0, Math.min(1, fractionY));
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
	public void captureStartValues(@NonNull final TransitionValues transitionValues) {
		super.captureStartValues(transitionValues);
		transitionValues.values.put(PROPERTY_TRANSITION_SCALE_X, transitionValues.view.getScaleX());
		transitionValues.values.put(PROPERTY_TRANSITION_SCALE_Y, transitionValues.view.getScaleY());
	}

	/**
	 * Obtains start scale values for X and Y axis from the given <var>startValues</var> object.
	 *
	 * @param startValues   Transition values that should possibly contain captured start values.
	 * @param defaultStartX Default X scale value to be returned if the <var>startValues</var> does
	 *                      not contain the scale X value.
	 * @param defaultStartY Default Y scale value to be returned if the <var>startValues</var> does
	 *                      not contain the scale Y value.
	 * @return Array containing scale X value at {@code [0]} and scale Y value at {@code [1]} either
	 * obtained from the start values or the default one.
	 * @see #captureStartValues(TransitionValues)
	 */
	@Size(2)
	@VisibleForTesting
	static float[] obtainStartScales(final TransitionValues startValues, final float defaultStartX, final float defaultStartY) {
		final float[] startScales = new float[]{defaultStartX, defaultStartY};
		if (startValues == null) {
			return startScales;
		}
		final Float startX = (Float) startValues.values.get(PROPERTY_TRANSITION_SCALE_X);
		if (startX != null) {
			startScales[0] = startX;
		}
		final Float startY = (Float) startValues.values.get(PROPERTY_TRANSITION_SCALE_Y);
		if (startY != null) {
			startScales[1] = startY;
		}
		return startScales;
	}

	/**
	 */
	@Nullable
	@Override
	public Animator onAppear(
			@NonNull final ViewGroup sceneRoot,
			@NonNull final View view,
			@Nullable final TransitionValues startValues,
			@Nullable final TransitionValues endValues
	) {
		calculateTransitionProperties(view);
		view.setPivotX(mInfo.pivotX);
		view.setPivotY(mInfo.pivotY);
		final float[] startScales = obtainStartScales(startValues, START_SCALE_ON_APPEAR, START_SCALE_ON_APPEAR);
		return createAnimator(
				view,
				startScales[0] == MAX ? MIN : startScales[0],
				startScales[1] == MAX ? MIN : startScales[1],
				MAX,
				MAX
		);
	}

	/**
	 */
	@Nullable
	@Override
	public Animator onDisappear(
			@NonNull final ViewGroup sceneRoot,
			@NonNull final View view,
			@Nullable final TransitionValues startValues,
			@Nullable final TransitionValues endValues
	) {
		calculateTransitionProperties(view);
		view.setPivotX(mInfo.pivotX);
		view.setPivotY(mInfo.pivotY);
		final float[] startScales = obtainStartScales(startValues, START_SCALE_ON_DISAPPEAR, START_SCALE_ON_DISAPPEAR);
		return createAnimator(view, startScales[0], startScales[1], MIN, MIN);
	}

	/**
	 * Ensures that all necessary properties for this transition are calculated.
	 *
	 * @param view The view to which will be the transition applied.
	 */
	@VisibleForTesting
	void calculateTransitionProperties(final View view) {
		mInfo.pivotX = mPivotX == null ? (view.getWidth() * mPivotXFraction) : mPivotX;
		mInfo.pivotY = mPivotY == null ? (view.getHeight() * mPivotYFraction) : mPivotY;
	}

	/**
	 * Returns info for the current scale animation configuration.
	 *
	 * @return Current scale info.
	 * @see #calculateTransitionProperties(View)
	 */
	@NonNull
	@VisibleForTesting
	final Info getInfo() {
		return mInfo;
	}

	/*
	 * Inner classes ===============================================================================
	 */

	/**
	 * Class holding necessary values for the scale transition that are exclusively associated with
	 * the currently transitioning view.
	 */
	@VisibleForTesting
	static final class Info {

		/**
		 * Scale pivot coordinate.
		 */
		float pivotX, pivotY;
	}
}
