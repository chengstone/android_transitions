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
	 * Object that holds all necessary properties for the scale transition for the currently
	 * transitioning view.
	 */
	private final Info mInfo = new Info();

	/**
	 * todo:
	 */
	private Float mPivotX;

	/**
	 * todo:
	 */
	private Float mPivotY;

	/**
	 * todo:
	 */
	private float mPivotXFraction = 0.5f;

	/**
	 * todo:
	 */
	private float mPivotYFraction = 0.5f;

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
	 * Creates a new instance of Scale transition with animation property values set from the
	 * specified <var>attrs</var>.
	 *
	 * @param context Context used to obtain values from the specified <var>attrs</var>.
	 * @param attrs   Set of attributes from which to obtain animation property values for the scale
	 *                animation.
	 */
	public Scale(@NonNull Context context, @NonNull AttributeSet attrs) {
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

	/**
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
	public static Animator createAnimator(@NonNull View view, @FloatRange(from = 0, to = 1) float startScale, @FloatRange(from = 0, to = 1) float endScale) {
		return ObjectAnimator.ofPropertyValuesHolder(
				view,
				PropertyValuesHolder.ofFloat(PROPERTY_SCALE_X, startScale, endScale),
				PropertyValuesHolder.ofFloat(PROPERTY_SCALE_Y, startScale, endScale)
		);
	}

	/**
	 * todo:
	 *
	 * @param pivotX
	 */
	public void setPivotX(@Nullable Float pivotX) {
		this.mPivotX = pivotX;
	}

	/**
	 * todo:
	 *
	 * @return
	 */
	@Nullable
	public Float getPivotX() {
		return mPivotX;
	}

	/**
	 * todo:
	 *
	 * @param pivotY
	 */
	public void setPivotY(@Nullable Float pivotY) {
		this.mPivotY = pivotY;
	}

	/**
	 * todo:
	 *
	 * @return
	 */
	@Nullable
	public Float getPivotY() {
		return mPivotY;
	}

	/**
	 * todo:
	 * <p>
	 * Default value: <b>{@code 0.5f}</b>
	 *
	 * @param fractionX
	 */
	public void setPivotXFraction(@FloatRange(from = 0, to = 1) float fractionX) {
		this.mPivotXFraction = fractionX;
	}

	/**
	 * todo:
	 *
	 * @return
	 */
	@FloatRange(from = 0, to = 1)
	public float getPivotXFraction() {
		return mPivotXFraction;
	}

	/**
	 * todo:
	 * <p>
	 * Default value: <b>{@code 0.5f}</b>
	 *
	 * @param fractionY
	 */
	public void setPivotYFraction(@FloatRange(from = 0, to = 1) float fractionY) {
		this.mPivotYFraction = fractionY;
	}

	/**
	 * todo:
	 *
	 * @return
	 */
	@FloatRange(from = 0, to = 1)
	public float getPivotYFraction() {
		return mPivotYFraction;
	}

	/**
	 */
	@Override
	public Animator onAppear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
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
	public Animator onDisappear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
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
	private void calculateTransitionProperties(View view, float startScale, float endScale) {
		mInfo.startScale = startScale;
		mInfo.endScale = endScale;
		mInfo.pivotX = mPivotX != null ? mPivotX : (view.getWidth() * mPivotXFraction);
		mInfo.pivotY = mPivotY != null ? mPivotY : (view.getHeight() * mPivotYFraction);
	}

	/**
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
