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
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.transition.TransitionValues;
import android.transition.Visibility;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This transition tracks changes to the visibility of target views in the start and end scenes and
 * scales up or down views in the scene. Visibility is determined by the {@link View#setVisibility(int)}
 * state of the views.
 * <p>
 * A Scale transition can be described in a resource file by using the {@code transition} tag
 * with {@code class} attribute set to {@code com.albedinsky.android.ui.transition.Scale},
 * along with standard Xml attributes for {@link Visibility} transition.
 *
 * @author Martin Albedinsky
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class Scale extends Visibility {

	/**
	 * Interface ===================================================================================
	 */

	/**
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
	 * Static members ==============================================================================
	 */

	/**
	 * Members =====================================================================================
	 */

	/**
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
	public Scale(@ScaleMode int mode) {
		setMode(mode);
	}

	/**
	 * Creates a new instance of scale transition with animation property values set from the
	 * specified <var>attrs</var>.
	 *
	 * @param context Context used to obtain values from the specified <var>attrs</var>.
	 * @param attrs   Set of attributes from which to obtain animation property values for the scale
	 *                animation.
	 */
	public Scale(@NonNull Context context, @NonNull AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * Methods =====================================================================================
	 */

	/**
	 */
	@Override
	public Animator onAppear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
		// todo: take into count also request for custom scaling pivot
		return createAnimator(view, 0, 1);
	}

	/**
	 */
	@Override
	public Animator onDisappear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
		// todo: take into count also request for custom scaling pivot
		return createAnimator(view, 1, 0);
	}

	/**
	 * Same as {@link #createAnimator(View, float, float, int, int)} for scale pivot fractions.
	 *
	 * @param pivotXFraction Fraction for the pivot X from the range {@code [0.0, 1.0]}.
	 * @param pivotYFraction Fraction for the pivot Y from the range {@code [0.0, 1.0]}.
	 */
	@NonNull
	public static Animator createAnimator(@NonNull View view, float startScale, float endScale, float pivotXFraction, float pivotYFraction) {
		final int pivotX = Math.round(pivotXFraction * view.getWidth());
		final int pivotY = Math.round(pivotYFraction * view.getHeight());
		return createAnimator(view, startScale, endScale, pivotX, pivotY);
	}

	/**
	 * Creates a new instance of scale Animator for the specified <var>view</var>.
	 *
	 * @param view       The view for which to create the requested animator.
	 * @param startScale Scale from which to start the animation.
	 * @param endScale   Scale at which should the animation end.
	 * @param pivotX     X coordinate of the pivot in pixels along which to scale the view.
	 * @param pivotY     Y coordinate of the pivot in pixels along which to scale the view.
	 * @return Animator that will play scale animation for the specified view according to the
	 * specified parameters when started.
	 * @see #createAnimator(View, float, float)
	 */
	@NonNull
	public static Animator createAnimator(@NonNull View view, float startScale, float endScale, int pivotX, int pivotY) {
		ensureViewScalePivots(view, pivotX, pivotY);
		return createAnimator(view, startScale, endScale);
	}

	/**
	 * Ensures that the specified <var>view</var> has set the specified <var>pivotX</var> and
	 * <var>pivotY</var> values.
	 *
	 * @param view   The view of which pivots to ensure.
	 * @param pivotX The pivot X in pixels to be set to the given view.
	 * @param pivotY The pivot Y in pixels to be set to the given view.
	 */
	private static void ensureViewScalePivots(View view, int pivotX, int pivotY) {
		view.setPivotX(pivotX);
		view.setPivotY(pivotY);
	}

	/**
	 * Creates a new instance of scale Animator for the specified <var>view</var>.
	 *
	 * @param view       The view for which to create the requested animator.
	 * @param startScale Scale from which to start the animation.
	 * @param endScale   Scale at which should the animation end.
	 * @return Animator that will play scale animation for the specified view according to the
	 * specified parameters when started.
	 * @see #createAnimator(View, float, float, int, int)
	 */
	@NonNull
	public static Animator createAnimator(@NonNull View view, float startScale, float endScale) {
		ensureViewScale(view, startScale);
		final PropertyValuesHolder holderX = PropertyValuesHolder.ofFloat(PROPERTY_SCALE_X, startScale, endScale);
		final PropertyValuesHolder holderY = PropertyValuesHolder.ofFloat(PROPERTY_SCALE_Y, startScale, endScale);
		return ObjectAnimator.ofPropertyValuesHolder(view, holderX, holderY);
	}

	/**
	 * Ensures that the specified <var>view</var> has set the specified <var>scale</var>.
	 *
	 * @param view  The view of which scale to ensure.
	 * @param scale The scale to be set to the given view.
	 */
	private static void ensureViewScale(View view, float scale) {
		view.setScaleX(scale);
		view.setScaleY(scale);
	}

	/**
	 * Inner classes ===============================================================================
	 */
}
