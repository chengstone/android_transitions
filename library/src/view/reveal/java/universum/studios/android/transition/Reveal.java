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
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.transition.TransitionValues;
import android.transition.Visibility;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import universum.studios.android.transition.util.AnimatorWrapper;

/**
 * This transition tracks changes to the visibility of target views in the start and end scenes and
 * reveals or conceals views in the scene. Visibility is determined by the {@link View#setVisibility(int)}
 * state of the views.
 * <p>
 * A Reveal transition uses {@link ViewAnimationUtils#createCircularReveal(View, int, int, float, float)}
 * to create a circular reveal/conceal animator that is used to animate its target views. A center
 * coordinates of revealing/concealing circle can be specified via {@link #setCenterX(Float)} and
 * {@link #setCenterY(Float)} or by using fractions via {@link #setCenterXFraction(float)} and
 * {@link #setCenterYFraction(float)}. If specifying a gravity for the center of the animating circle
 * is enough, the desired gravity flags can be specified via {@link #setCenterGravity(Integer)}.
 * <p>
 * A Reveal transition can be described in a resource file by using the {@code transition} tag
 * with {@code class} attribute set to {@code com.albedinsky.android.ui.transition.Reveal},
 * along with Xml attributes referenced below.
 *
 * <h3>XML attributes</h3>
 * {@link R.styleable#Ui_Transition_Reveal Reveal Attributes}
 *
 * @author Martin Albedinsky
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class Reveal extends Visibility {

	/**
	 * Interface ===================================================================================
	 */

	/**
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "Reveal";

	/**
	 * Defines an annotation for determining set of allowed modes for Reveal transition.
	 */
	@Retention(RetentionPolicy.SOURCE)
	@IntDef({REVEAL, CONCEAL})
	public @interface RevealMode {
	}

	/**
	 * Mode to indicate that Reveal transition should play <b>reveal</b> animation, that is to
	 * 'upscale' the animating circle of an animating view from its initial size to a larger one.
	 * <p>
	 * This mode can be used as well for <b>appearing</b> targets as for <b>disappearing</b> ones.
	 */
	public static final int REVEAL = MODE_IN;

	/**
	 * Mode to indicate that Reveal transition should play <b>conceal</b> animation, that is to
	 * 'downscale' the animating circle of an animating view from its initial size to a smaller one.
	 * <p>
	 * This mode can be used as well for <b>disappearing</b> targets as for <b>appearing</b> ones.
	 */
	public static final int CONCEAL = MODE_OUT;

	/**
	 * Static members ==============================================================================
	 */

	/**
	 * Members =====================================================================================
	 */

	/**
	 * Object holding all necessary values for the reveal transition that depends exclusively on
	 * the currently animating view.
	 */
	private final RevealInfo REVEAL_INFO = new RevealInfo();

	/**
	 * X coordinate for center of the reveal/conceal animation. If {@code null}, {@link #mCenterXFraction}
	 * should be used to calculate such coordinate.
	 */
	private Float mCenterX;

	/**
	 * Y coordinate for center of the reveal/conceal animation. If {@code null}, {@link #mCenterYFraction}
	 * should be used to calculate such coordinate.
	 */
	private Float mCenterY;

	/**
	 * Fraction from the range {@code [0.0, 1.0]} that can be used to calculate X coordinate for center
	 * of the reveal/conceal animation if center Y coordinate has not been specified.
	 */
	private Float mCenterXFraction = 0.5f;

	/**
	 * Fraction from the range {@code [0.0, 1.0]} that can be used to calculate Y coordinate for center
	 * of the reveal/conceal animation if center Y coordinate has not been specified.
	 */
	private Float mCenterYFraction = 0.5f;

	/**
	 * Gravity flags used to resolve center coordinates for the reveal/conceal animation.
	 * If {@code null}, values specified for {@link #mCenterX} and {@link #mCenterY} should be used
	 * as center coordinates.
	 */
	private Integer mCenterGravity;

	/**
	 * Value in pixels by which to offset {@link #mCenterY} vertically.
	 */
	private int mCenterVerticalOffset;

	/**
	 * Value in pixels by which to offset {@link #mCenterX} horizontally.
	 */
	private int mCenterHorizontalOffset;

	/**
	 * Start radius for circle of the reveal/conceal animation.
	 */
	private Float mStartRadius;

	/**
	 * End radius for circle of the reveal/conceal animation.
	 */
	private Float mEndRadius;

	/**
	 * Visibility flag set to a revealing view whenever the reveal animation starts.
	 */
	private int mStartVisibility = View.VISIBLE;

	/**
	 * Visibility flag set to a revealing view whenever the reveal animation ends.
	 */
	private int mEndVisibility = View.VISIBLE;

	/**
	 * Visibility flag set to a revealing view whenever {@link #onAppear(ViewGroup, View, TransitionValues, TransitionValues)}
	 * is invoked.
	 */
	private int mAppearVisibility = View.VISIBLE;

	/**
	 * Visibility flag set to a revealing view whenever {@link #onDisappear(ViewGroup, View, TransitionValues, TransitionValues)}
	 * is invoked.
	 */
	private int mDisappearVisibility = View.VISIBLE;

	/**
	 * Constructors ================================================================================
	 */

	/**
	 * Same as {@link #Reveal(int)} with {@link #REVEAL} mode.
	 */
	public Reveal() {
		this(REVEAL);
	}

	/**
	 * Creates a new instance of Reveal transition with the specified <var>mode</var>.
	 *
	 * @param mode One of {@link #REVEAL} or {@link #CONCEAL}.
	 */
	public Reveal(@RevealMode int mode) {
		setMode(mode);
	}

	/**
	 * Creates a new instance of Reveal transition with animation property values set from the
	 * specified <var>attrs</var>.
	 *
	 * @param context Context used to obtain values from the specified <var>attrs</var>.
	 * @param attrs   Set of attributes from which to obtain property values for the reveal animation.
	 */
	@SuppressWarnings("ResourceType")
	public Reveal(@NonNull Context context, @NonNull AttributeSet attrs) {
		super(context, attrs);
		final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Ui_Transition_Reveal, 0, 0);
		if (typedArray != null) {
			final int n = typedArray.getIndexCount();
			for (int i = 0; i < n; i++) {
				final int index = typedArray.getIndex(i);
				if (index == R.styleable.Ui_Transition_Reveal_uiRevealMode) {
					setMode(typedArray.getInteger(index, getMode()));
				} else if (index == R.styleable.Ui_Transition_Reveal_uiStartRadius) {
					this.mStartRadius = (float) typedArray.getDimensionPixelSize(index, 0);
				} else if (index == R.styleable.Ui_Transition_Reveal_uiEndRadius) {
					this.mEndRadius = (float) typedArray.getDimensionPixelSize(index, 0);
				} else if (index == R.styleable.Ui_Transition_Reveal_uiAppearVisibility) {
					this.mAppearVisibility = typedArray.getInteger(index, mAppearVisibility);
				} else if (index == R.styleable.Ui_Transition_Reveal_uiDisappearVisibility) {
					this.mDisappearVisibility = typedArray.getInteger(index, mDisappearVisibility);
				} else if (index == R.styleable.Ui_Transition_Reveal_uiStartVisibility) {
					this.mStartVisibility = typedArray.getInteger(index, mStartVisibility);
				} else if (index == R.styleable.Ui_Transition_Reveal_uiEndVisibility) {
					this.mEndVisibility = typedArray.getInteger(index, mEndVisibility);
				} else if (index == R.styleable.Ui_Transition_Reveal_uiCenterGravity) {
					this.mCenterGravity = typedArray.getInteger(index, 0);
				} else if (index == R.styleable.Ui_Transition_Reveal_uiCenterVerticalOffset) {
					this.mCenterVerticalOffset = typedArray.getDimensionPixelSize(index, 0);
				} else if (index == R.styleable.Ui_Transition_Reveal_uiCenterHorizontalOffset) {
					this.mCenterHorizontalOffset = typedArray.getDimensionPixelSize(index, 0);
				} else if (index == R.styleable.Ui_Transition_Reveal_android_centerX) {
					this.mCenterXFraction = typedArray.getFraction(index, 1, 1, mCenterXFraction);
				} else if (index == R.styleable.Ui_Transition_Reveal_android_centerY) {
					this.mCenterYFraction = typedArray.getFraction(index, 1, 1, mCenterYFraction);
				}
			}
			typedArray.recycle();
		}
	}

	/**
	 * Methods =====================================================================================
	 */

	/**
	 */
	@Override
	public void setMode(@RevealMode int mode) {
		super.setMode(mode);
	}

	/**
	 * Returns the reveal mode of this transition.
	 *
	 * @return One of {@link #REVEAL} or {@link #CONCEAL}.
	 */
	@RevealMode
	@SuppressWarnings("ResourceType")
	public int getMode() {
		return super.getMode();
	}

	/**
	 * Sets a start radius for animating circle of the reveal animation. Based on the current reveal
	 * mode, this radius should be either smaller than the end radius specified via {@link #setEndRadius(Float)}
	 * or grater one otherwise there will be nothing to reveal/conceal.
	 *
	 * @param radius The desired radius in pixels. May be {@code null} to use the default approach
	 *               to compute start radius based on the reveal mode.
	 * @see R.attr#uiStartRadius ui:uiStartRadius
	 * @see #getStartRadius()
	 * @see #setEndRadius(Float)
	 */
	public void setStartRadius(@Nullable Float radius) {
		this.mStartRadius = radius;
	}

	/**
	 * Returns the start radius for animating circle of the reveal animation.
	 *
	 * @return Start radius in pixels or {@code null} if this radius will be computed based on the
	 * reveal mode.
	 * @see #setStartRadius(Float)
	 */
	@Nullable
	public Float getStartRadius() {
		return mStartRadius;
	}

	/**
	 * Sets an end radius for animating circle of the reveal animation. Based on the current reveal
	 * mode, this radius should be either greater than the start radius specified via {@link #setStartRadius(Float)}
	 * or smaller one otherwise there will be nothing to reveal/conceal.
	 *
	 * @param radius The desired radius in pixels. May be {@code null} to use the default approach
	 *               to compute end radius based on the reveal mode.
	 * @see R.attr#uiEndRadius ui:uiEndRadius
	 * @see #getEndRadius()
	 * @see #setStartRadius(Float)
	 */
	public void setEndRadius(@Nullable Float radius) {
		this.mEndRadius = radius;
	}

	/**
	 * Returns the end radius for animating circle of the reveal animation.
	 *
	 * @return End radius in pixels or {@code null} if this radius will be computed based on the
	 * reveal mode.
	 * @see #setEndRadius(Float)
	 */
	@Nullable
	public Float getEndRadius() {
		return mEndRadius;
	}

	/**
	 * Sets a visibility flag that should be set to a revealing view whenever this transition starts.
	 *
	 * @param visibility One of {@link View#VISIBLE}, {@link View#INVISIBLE} or {@link View#GONE}.
	 * @see R.attr#uiStartVisibility ui:uiStartVisibility
	 * @see #getStartVisibility()
	 */
	public void setStartVisibility(int visibility) {
		this.mStartVisibility = visibility;
	}

	/**
	 * Returns the visibility flag that will be set to a revealing view whenever this transition
	 * (its animation) starts.
	 * <p>
	 * Default value: <b>{@link View#VISIBLE VISIBLE}</b>
	 *
	 * @return One of {@link View#VISIBLE}, {@link View#INVISIBLE} or {@link View#GONE}.
	 * @see #setStartVisibility(int)
	 */
	public int getStartVisibility() {
		return mStartVisibility;
	}

	/**
	 * Sets a visibility flag that should be set to a revealing view whenever this transition
	 * (its animation) ends.
	 *
	 * @param visibility One of {@link View#VISIBLE}, {@link View#INVISIBLE} or {@link View#GONE}.
	 * @see R.attr#uiEndVisibility ui:uiEndVisibility
	 * @see #getEndVisibility()
	 */
	public void setEndVisibility(int visibility) {
		this.mEndVisibility = visibility;
	}

	/**
	 * Returns the visibility flag that will be set to a revealing view whenever this transition ends.
	 * <p>
	 * Default value: <b>{@link View#VISIBLE VISIBLE}</b>
	 *
	 * @return One of {@link View#VISIBLE}, {@link View#INVISIBLE} or {@link View#GONE}.
	 * @see #setEndVisibility(int)
	 */
	public int getEndVisibility() {
		return mEndVisibility;
	}

	/**
	 * Sets a visibility flag that should be set to a revealing view whenever this transition has
	 * been requested to create an appear animator via {@link #onAppear(ViewGroup, View, TransitionValues, TransitionValues)}.
	 *
	 * @param visibility One of {@link View#VISIBLE}, {@link View#INVISIBLE} or {@link View#GONE}.
	 * @see R.attr#uiAppearVisibility ui:uiAppearVisibility
	 * @see #getAppearVisibility()
	 */
	public void setAppearVisibility(int visibility) {
		this.mAppearVisibility = visibility;
	}

	/**
	 * Returns the visibility flag that will be set to a revealing view whenever this transition is
	 * requested to create an appear animator.
	 * <p>
	 * Default value: <b>{@link View#VISIBLE VISIBLE}</b>
	 *
	 * @return One of {@link View#VISIBLE}, {@link View#INVISIBLE} or {@link View#GONE}.
	 * @see #setAppearVisibility(int)
	 */
	public int getAppearVisibility() {
		return mAppearVisibility;
	}

	/**
	 * Sets a visibility flag that should be set to a revealing view whenever this transition has
	 * been requested to create a disappear animator via {@link #onDisappear(ViewGroup, View, TransitionValues, TransitionValues)}.
	 *
	 * @param visibility One of {@link View#VISIBLE}, {@link View#INVISIBLE} or {@link View#GONE}.
	 * @see R.attr#uiDisappearVisibility ui:uiDisappearVisibility
	 * @see #getDisappearVisibility()
	 */
	public void setDisappearVisibility(int visibility) {
		this.mDisappearVisibility = visibility;
	}

	/**
	 * Returns the visibility flag that will be set to a revealing view whenever this transition is
	 * requested to created a disappear animator.
	 * <p>
	 * Default value: <b>{@link View#VISIBLE VISIBLE}</b>
	 *
	 * @return One of {@link View#VISIBLE}, {@link View#INVISIBLE} or {@link View#GONE}.
	 * @see #setDisappearVisibility(int)
	 */
	public int getDisappearVisibility() {
		return mDisappearVisibility;
	}

	/**
	 * Sets a gravity flags that should be used to resolve center for animating circle of the reveal
	 * animation.
	 * <p>
	 * Sometimes the gravity is not enough to specify exact center coordinates for the reveal animation.
	 * In such case you can move the center coordinates computed by the requested gravity by specifying
	 * horizontal and vertical offset via {@link #setCenterHorizontalOffset(int)} and
	 * {@link #setCenterVerticalOffset(int)}.
	 *
	 * @param gravity One of {@link Gravity#CENTER}, {@link Gravity#CENTER_VERTICAL}, {@link Gravity#CENTER_HORIZONTAL},
	 *                {@link Gravity#TOP}, {@link Gravity#BOTTOM}, {@link Gravity#START}, {@link Gravity#END}
	 *                or theirs combination. May be {@code null} to use either specified exact center
	 *                coordinates or the specified fractions to compute such coordinates.
	 * @see R.attr#uiCenterGravity ui:uiCenterGravity
	 * @see #getCenterGravity()
	 * @see #setCenterX(Float)
	 * @see #setCenterY(Float)
	 * @see #setCenterXFraction(float)
	 * @see #setCenterYFraction(float)
	 */
	public void setCenterGravity(@Nullable Integer gravity) {
		this.mCenterGravity = gravity;
	}

	/**
	 * Returns the gravity flags that will be used to resolve center for animating circle of the
	 * reveal animation.
	 * <p>
	 * Default value: <b>{@code null}</b>
	 *
	 * @return One of {@link Gravity#CENTER}, {@link Gravity#CENTER_VERTICAL}, {@link Gravity#CENTER_HORIZONTAL},
	 * {@link Gravity#TOP}, {@link Gravity#BOTTOM}, {@link Gravity#START}, {@link Gravity#END} or theirs
	 * combination or {@code null} if no gravity has been specified so either center coordinates
	 * specified via {@link #setCenterX(Float)} and {@link #setCenterY(Float)} will be used or computed
	 * using the current center fraction values.
	 * @see #setCenterGravity(Integer)
	 */
	@Nullable
	public Integer getCenterGravity() {
		return mCenterGravity;
	}

	/**
	 * Sets an offset for the <b>center x</b> coordinate for animating circle of the reveal animation.
	 *
	 * @param offset The desired offset in pixels. If negative the center x coordinate will be moved
	 *               the the left, if positive it will be moved to the right, otherwise remains
	 *               unchanged.
	 * @see R.attr#uiCenterHorizontalOffset ui:uiCenterHorizontalOffset
	 * @see #getCenterHorizontalOffset()
	 * @see #setCenterVerticalOffset(int)
	 */
	public void setCenterHorizontalOffset(int offset) {
		this.mCenterHorizontalOffset = offset;
	}

	/**
	 * Returns the offset for the <b>center x</b> coordinate for animating circle of the reveal animation.
	 * <p>
	 * Default value: <b>{@code 0}</b>
	 *
	 * @return Horizontal offset in pixels.
	 * @see #setCenterHorizontalOffset(int)
	 */
	public int getCenterHorizontalOffset() {
		return mCenterHorizontalOffset;
	}

	/**
	 * Sets an offset for the <b>center y</b> coordinate for animating circle of the reveal animation.
	 *
	 * @param offset The desired offset in pixels. If negative the center y coordinate will be moved
	 *               the the top, if positive it will be moved to the bottom, otherwise remains
	 *               unchanged.
	 * @see R.attr#uiCenterVerticalOffset ui:uiCenterVerticalOffset
	 * @see #getCenterVerticalOffset()
	 * @see #setCenterHorizontalOffset(int)
	 */
	public void setCenterVerticalOffset(int offset) {
		this.mCenterVerticalOffset = offset;
	}

	/**
	 * Returns the offset for the <b>center y</b> coordinate for animating circle of the reveal animation.
	 * <p>
	 * Default value: <b>{@code 0}</b>
	 *
	 * @return Vertical offset in pixels.
	 * @see #setCenterVerticalOffset(int)
	 */
	public int getCenterVerticalOffset() {
		return mCenterVerticalOffset;
	}

	/**
	 * Sets a center x coordinate for animating circle of the reveal animation.
	 *
	 * @param centerX The desired center x coordinate in pixels. May be {@code null} to use center
	 *                x fraction instead.
	 * @see #getCenterX()
	 * @see #setCenterXFraction(float)
	 * @see #setCenterY(Float)
	 */
	public void setCenterX(@Nullable Float centerX) {
		this.mCenterX = centerX;
	}

	/**
	 * Returns the x coordinate of the center for animating circle of the reveal animation.
	 * <p>
	 * Default value: <b>{@code null}</b>
	 *
	 * @return X coordinate of the center for animating circle in pixels or {@code null} if no coordinate
	 * has been specified so the fraction will be used to compute this center coordinate.
	 * @see #setCenterX(Float)
	 */
	@Nullable
	public Float getCenterX() {
		return mCenterX;
	}

	/**
	 * Sets a center y coordinate for animating circle of the reveal animation.
	 *
	 * @param centerY The desired center y coordinate in pixels. May be {@code null} to use center
	 *                y fraction instead.
	 * @see #getCenterY()
	 * @see #setCenterYFraction(float)
	 * @see #setCenterX(Float)
	 */
	public void setCenterY(@Nullable Float centerY) {
		this.mCenterY = centerY;
	}

	/**
	 * Returns the y coordinate of the center for animating circle of the reveal animation.
	 * <p>
	 * Default value: <b>{@code null}</b>
	 *
	 * @return Y coordinate of the center for animating circle in pixels or {@code null} if no coordinate
	 * has been specified so the fraction will be used to compute this center coordinate.
	 * @see #setCenterY(Float)
	 */
	@Nullable
	public Float getCenterY() {
		return mCenterY;
	}

	/**
	 * Sets a fraction for the <b>center x</b> coordinate. This fraction will be used to resolve
	 * center x coordinate of an animating view based on its current <b>width</b> if such value has
	 * not been specified.
	 * <p>
	 * Sometimes the fraction is not enough to specify exact center coordinate for the reveal animation.
	 * In such case you can move the center coordinates computed by the requested fractions by specifying
	 * horizontal and vertical offset via {@link #setCenterHorizontalOffset(int)} and
	 * {@link #setCenterVerticalOffset(int)}.
	 *
	 * @param fraction The desired fraction from the range {@code [0f, 1f]}.
	 * @see android.R.attr#centerX android:centerX
	 * @see #getCenterXFraction()
	 * @see #setCenterYFraction(float)
	 * @see #setCenterX(Float)
	 */
	public void setCenterXFraction(float fraction) {
		this.mCenterXFraction = fraction;
	}

	/**
	 * Returns the fraction of the <b>center x</b> coordinate.
	 * <p>
	 * Default value: <b>{@code 0.5f}</b>
	 *
	 * @return Fraction from the range {@code [0f, 1f]}.
	 * @see #setCenterXFraction(float)
	 */
	public float getCenterXFraction() {
		return mCenterXFraction;
	}

	/**
	 * Sets a fraction for the <b>center y</b> coordinate. This fraction will be used to resolve
	 * center y coordinate of an animating view based on its current <b>height</b> if such value has
	 * not been specified.
	 * <p>
	 * Sometimes the fraction is not enough to specify exact center coordinate for the reveal animation.
	 * In such case you can move the center coordinates computed by the requested fractions by specifying
	 * horizontal and vertical offset via {@link #setCenterHorizontalOffset(int)} and
	 * {@link #setCenterVerticalOffset(int)}.
	 *
	 * @param fraction The desired fraction from the range {@code [0f, 1f]}.
	 * @see android.R.attr#centerY android:centerY
	 * @see #getCenterYFraction()
	 * @see #setCenterXFraction(float)
	 * @see #setCenterY(Float)
	 */
	public void setCenterYFraction(float fraction) {
		this.mCenterYFraction = fraction;
	}

	/**
	 * Returns the fraction of the <b>center y</b> coordinate.
	 * <p>
	 * Default value: <b>{@code 0.5f}</b>
	 *
	 * @return Fraction from the range {@code [0f, 1f]}.
	 * @see #setCenterYFraction(float)
	 */
	public float getCenterYFraction() {
		return mCenterYFraction;
	}

	/**
	 */
	@Override
	public Animator onAppear(ViewGroup sceneRoot, final View view, TransitionValues startValues, TransitionValues endValues) {
		ensureTransitionProperties(view);
		final Animator animator = createAnimatorFromRevealInfo(view);
		animator.addListener(new AnimatorListenerAdapter() {

			@Override
			public void onAnimationStart(Animator animation) {
				view.setVisibility(mStartVisibility);
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				view.setVisibility(mEndVisibility);
			}
		});
		view.setVisibility(mAppearVisibility);
		return animator;
	}

	/**
	 */
	@Override
	public Animator onDisappear(ViewGroup sceneRoot, final View view, TransitionValues startValues, TransitionValues endValues) {
		ensureTransitionProperties(view);
		final Animator animator = createAnimatorFromRevealInfo(view);
		animator.addListener(new AnimatorListenerAdapter() {

			@Override
			public void onAnimationStart(Animator animation) {
				view.setVisibility(mStartVisibility);
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				view.setVisibility(mEndVisibility);
			}
		});
		view.setVisibility(mDisappearVisibility);
		return animator;
	}

	/**
	 * Ensures that all necessary properties for this transition are computed.
	 *
	 * @param view The view for which will be the reveal animation run.
	 * @see #ensureTransitionCenter(View)
	 * @see #ensureTransitionRadii(View)
	 */
	private void ensureTransitionProperties(View view) {
		ensureTransitionCenter(view);
		ensureTransitionRadii(view);
	}

	/**
	 * Ensures that the center coordinates for the reveal animation of the specified <var>view</var>
	 * are computed.
	 *
	 * @param view The view for which will be the reveal animation run.
	 */
	private void ensureTransitionCenter(View view) {
		final float[] center;
		if (mCenterGravity != null) {
			center = resolveGravityCenter(view);
		} else {
			center = resolveCenter(view, mCenterXFraction, mCenterYFraction);
		}

		final float centerX = mCenterX == null ? center[0] : mCenterX;
		final float centerY = mCenterY == null ? center[1] : mCenterY;

		REVEAL_INFO.centerX = centerX + mCenterHorizontalOffset;
		REVEAL_INFO.centerY = centerY + mCenterVerticalOffset;
	}

	/**
	 * Resolves center coordinates for the reveal animation of the specified <var>view</var> based on
	 * the current value of {@link #mCenterGravity}.
	 *
	 * @param view The view for which will be the reveal animation run.
	 * @return An array with center coordinates: centerX[0], centerY[1].
	 */
	private float[] resolveGravityCenter(View view) {
		final float[] center = new float[]{0, 0};

		final int viewWidth = view.getWidth();
		final int viewHeight = view.getHeight();

		// todo: resolve center coordinates based on RTL value ???
		// todo: resolve absolute gravity like FragmentLayout does and the center can be then
		// todo: properly resolved for RTL layout
		switch (mCenterGravity) {
			case Gravity.START:
			case Gravity.START | Gravity.TOP:
				// Initial values for center.
				break;
			case Gravity.START | Gravity.BOTTOM:
				center[1] = viewHeight;
				break;
			case Gravity.END:
			case Gravity.END | Gravity.TOP:
				center[0] = viewWidth;
				break;
			case Gravity.END | Gravity.BOTTOM:
				center[0] = viewWidth;
				center[1] = viewHeight;
				break;
			case Gravity.CENTER_VERTICAL | Gravity.END:
				center[0] = viewWidth;
			case Gravity.CENTER_VERTICAL | Gravity.START:
				center[1] = viewHeight / 2f;
				break;
			case Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM:
				center[1] = viewHeight;
			case Gravity.CENTER_HORIZONTAL | Gravity.TOP:
				center[0] = viewWidth / 2f;
				break;
			case Gravity.CENTER:
				center[0] = viewWidth / 2f;
				center[1] = viewHeight / 2f;
				break;
		}
		return center;
	}

	/**
	 * Ensures that the start and end radius for the reveal animation of the specified <var>view</var>
	 * are computed.
	 *
	 * @param view The view for which will be the reveal animation run.
	 */
	private void ensureTransitionRadii(View view) {
		final float[] radii = calculateTransitionRadii(view);
		REVEAL_INFO.startRadius = mStartRadius != null ? mStartRadius : radii[0];
		REVEAL_INFO.endRadius = mEndRadius != null ? mEndRadius : radii[1];
	}

	/**
	 * Calculates start and end radius for the reveal animation of the specified <var>view</var>
	 * based on the current {@link #getMode()}.
	 *
	 * @param view The view for which reveal animation to calculate start and end radius.
	 * @return An array with two radii: startRadius[0], endRadius[0].
	 */
	private float[] calculateTransitionRadii(View view) {
		float startRadius, endRadius;
		startRadius = endRadius = 0;
		switch (getMode()) {
			case REVEAL:
				startRadius = 0;
				endRadius = calculateTransitionRadius(view);
				break;
			case CONCEAL:
				startRadius = calculateTransitionRadius(view);
				endRadius = 0;
				break;
		}
		return new float[]{startRadius, endRadius};
	}

	/**
	 * Calculates radius for the reveal transition for the specified <var>view</var>.
	 * The radius is computed based on how big distance should be traveled from the current
	 * center coordinates to the outermost edge of the given view.
	 * <p>
	 * <b>Note, that this should be called after center coordinates has been calculated via
	 * {@link #ensureTransitionCenter(View)}</b>.
	 *
	 * @param view The view for which reveal animation to calculate the requested radius.
	 * @return Calculated radius that should be used as end/start radius for reveal animation
	 * depends on its current mode.
	 */
	private float calculateTransitionRadius(View view) {
		float horizontalDistance, verticalDistance;

		final float centerX = REVEAL_INFO.centerX;
		final float centerY = REVEAL_INFO.centerY;
		final float viewWidth = view.getWidth();
		final float viewHeight = view.getHeight();
		final float[] viewCenter = resolveCenter(view);

		if (centerX >= viewCenter[0]) {
			horizontalDistance = centerX / viewWidth * viewWidth;
		} else {
			horizontalDistance = (1 - (centerX / viewWidth)) * viewWidth;
		}

		if (centerY >= viewCenter[1]) {
			verticalDistance = centerY / viewHeight * viewHeight;
		} else {
			verticalDistance = (1 - (centerY / viewHeight)) * viewHeight;
		}

		return Math.round(Math.hypot(horizontalDistance, verticalDistance));
	}

	/**
	 * Creates a new instance of circular reveal Animator for the specified <var>view</var> with current
	 * reveal properties specified within {@link #REVEAL_INFO}.
	 * <p>
	 * <b>Note, that this animator will be already wrapped in {@link AnimatorWrapper} which will have
	 * PAUSE and RESUME features disabled to ensure that there will be no exception thrown by the
	 * Android system if it tries to pause already running Reveal transition where such situation
	 * is not properly handled by the system at this time.</b>
	 *
	 * @param view The view for which to create the requested animator.
	 * @return Animator that will play circular reveal animation when started.
	 */
	private Animator createAnimatorFromRevealInfo(View view) {
		final Animator animator = createAnimator(
				view,
				REVEAL_INFO.centerX,
				REVEAL_INFO.centerY,
				REVEAL_INFO.startRadius,
				REVEAL_INFO.endRadius
		);
		final AnimatorWrapper animatorWrapper = new AnimatorWrapper(animator);
		animatorWrapper.removeFeature(AnimatorWrapper.PAUSE | AnimatorWrapper.RESUME);
		return animatorWrapper;
	}

	/**
	 * Same as {@link #createAnimator(View, float, float, float, float)} where the center coordinates
	 * will be automatically resolved from the specified <var>view</var>.
	 *
	 * @see #resolveCenterPosition(View)
	 */
	@NonNull
	public static Animator createAnimator(@NonNull View view, float startRadius, float endRadius) {
		final float[] center = resolveCenterPosition(view);
		return createAnimator(view, center[0], center[1], startRadius, endRadius);
	}

	/**
	 * Creates a new instance of circular reveal Animator for the specified <var>view</var>.
	 *
	 * @param view        The view for which to create the requested animator.
	 * @param centerX     X coordinate of a center from where should the reveal animation start.
	 * @param centerY     Y coordinate of a center from where should the reveal animation start.
	 * @param startRadius Radius of the specified view at the start of the reveal animation.
	 * @param endRadius   Radius of the specified view at the end of the reveal animation.
	 * @return Animator that will play circular reveal animation for the specified view according
	 * to the specified parameters when started.
	 * @see ViewAnimationUtils#createCircularReveal(View, int, int, float, float)
	 */
	@NonNull
	public static Animator createAnimator(@NonNull View view, float centerX, float centerY, float startRadius, float endRadius) {
		return ViewAnimationUtils.createCircularReveal(view, Math.round(centerX), Math.round(centerY), startRadius, endRadius);
	}

	/**
	 * Calculates the radius of the specified <var>view</var> for the purpose of circular reveal animation
	 * based on its current <b>width</b> and <b>height</b>.
	 *
	 * @param view The view of which radius to calculate.
	 * @return Calculated radius that may be used as end/start radius for the desired reveal animation.
	 */
	public static float calculateRadius(@NonNull View view) {
		return Math.round(Math.hypot(view.getWidth(), view.getHeight()));
	}

	/**
	 * Same as {@link #resolveCenterPosition(View, float, float)} with fraction {@code 0.5f}
	 * for booth center coordinates.
	 */
	@NonNull
	public static float[] resolveCenterPosition(@NonNull View view) {
		return resolveCenterPosition(view, 0.5f, 0.5f);
	}

	/**
	 * Like {@link #resolveCenter(View, float, float)} this method will resolve center of the specified
	 * <var>view</var> based on its current <b>width</b> and <b>height</b> but also takes into count
	 * its current <b>x</b> and <b>y</b> coordinate.
	 *
	 * @param view            The view of which center position to resolve.
	 * @param centerXFraction Fraction to resolve x coordinate of the center. Should be from the range
	 *                        {@code [0f, 1f]}.
	 * @param centerYFraction Fraction to resolve y coordinate of the center. Should be from the range
	 *                        {@code [0f, 1f]}.
	 * @return An array with center coordinates: centerX[0], centerY[1].
	 */
	@NonNull
	public static float[] resolveCenterPosition(@NonNull View view, float centerXFraction, float centerYFraction) {
		final float[] center = resolveCenter(view, centerXFraction, centerYFraction);
		return new float[]{
				view.getX() + center[0],
				view.getY() + center[1]
		};
	}

	/**
	 * Same as {@link #resolveCenter(View, float, float)} with fraction {@code 0.5f} for booth center
	 * coordinates.
	 */
	@NonNull
	public static float[] resolveCenter(@NonNull View view) {
		return resolveCenter(view, 0.5f, 0.5f);
	}

	/**
	 * Resolves the center of the specified <var>view</var> based on its current <var>width</var> and
	 * <b>height</b> according to the requested fractions.
	 *
	 * @param view            The view of which center to resolve.
	 * @param centerXFraction Fraction to resolve x coordinate of the center. Should be from the range
	 *                        {@code [0f, 1f]}.
	 * @param centerYFraction Fraction to resolve y coordinate of the center. Should be from the range
	 *                        {@code [0f, 1f]}.
	 * @return An array with center coordinates: centerX[0], centerY[1].
	 */
	@NonNull
	public static float[] resolveCenter(@NonNull View view, float centerXFraction, float centerYFraction) {
		return new float[]{
				view.getWidth() * centerXFraction,
				view.getHeight() * centerYFraction
		};
	}

	/**
	 * Inner classes ===============================================================================
	 */

	/**
	 * Class holding necessary values for the reveal transition that depends exclusively on the
	 * currently animating view.
	 */
	private static final class RevealInfo {

		/**
		 * Reveal circle radius.
		 */
		float startRadius, endRadius;

		/**
		 * Reveal circle center coordinate.
		 */
		float centerX, centerY;
	}
}
