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
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.VisibleForTesting;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.transition.Visibility;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import universum.studios.android.transition.util.TransitionUtils;

/**
 * A {@link Visibility} transition implementation that tracks changes to the visibility of target
 * views in the start and end scenes and translates/moves views in the scene. Visibility is
 * determined by both the {@link View#setVisibility(int)} state of the view as well as whether it
 * is parented in the current view hierarchy. Disappearing Views are limited as described in
 * {@link Visibility#onDisappear(android.view.ViewGroup, TransitionValues, int, TransitionValues, int)}.
 * <p>
 * This transition differs from {@link android.transition.Slide Slide} transition in a way that it
 * allows to translate a concrete view by a desired delta in both X and Y axes. The delta value by
 * which should be the view translated/moved may be specified either as fixed/absolute value or as
 * relative value to the the target view or to the scene where is the view presented. Delta values
 * for translate animation may be specified via {@link #setTranslationXDelta(float)} and
 * {@link #setTranslationYDelta(float)} and theirs "relativity" via {@link #setTranslationXRelativity(int)}
 * and {@link #setTranslationYRelativity(int)} where the specified relativity types will indicate
 * how the delta values should be treated.
 * <p>
 * Translate transition can be described in a resource file by using the {@code transition} tag with
 * {@code class} attribute set to {@code universum.studios.android.transition.Translate}, along with
 * Xml attributes referenced below.
 *
 * <h3>XML attributes</h3>
 * {@link R.styleable#Ui_Transition_Translate Translate Attributes}
 *
 * @author Martin Albedinsky
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
public class Translate extends Visibility {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "Translation";

	/**
	 * Defines an annotation for determining set of allowed modes for Translate transition.
	 */
	@Retention(RetentionPolicy.SOURCE)
	@IntDef(flag = true, value = {MODE_IN, MODE_OUT})
	public @interface TranslateMode {
	}

	/**
	 * Name of the property holding position on screen for animating view in {@link TransitionValues}.
	 */
	@VisibleForTesting
	static final String PROPERTY_TRANSITION_LOCATION_ON_SCREEN = Translate.class.getName() + ":transition.locationOnScreen";

	/*
	 * Interface ===================================================================================
	 */

	/**
	 * DeltaResolver specifies an interface that may be used to resolve delta value for translate
	 * animation along both X and Y axes.
	 *
	 * @author Martin Albedinsky
	 */
	public interface DeltaResolver {

		/**
		 * Resolves proper delta value for translate animation along X axis for the specified parameters.
		 * <p>
		 * Implementations should always return absolute delta value in pixels calculated from the
		 * given value depending on the specified relativity.
		 *
		 * @param sceneRoot       Root view of the scene containing the target view in its view
		 *                        hierarchy for which the translate animation will be played.
		 * @param view            The target view for which will be the translate animation played.
		 * @param valueRelativity Relativity type describing how the given <var>value</var> should
		 *                        be treated.
		 * @param value           Either absolute or relative delta value used to properly calculate
		 *                        the absolute delta depending on the specified <var>valueRelativity</var>.
		 * @return Resolved translation delta value for X axis in pixels.
		 */
		float resolveDeltaX(@NonNull ViewGroup sceneRoot, @NonNull View view, @Description.ValueRelativity int valueRelativity, float value);

		/**
		 * Resolves proper delta value for translate animation along Y axis for the specified parameters.
		 * <p>
		 * Implementations should always return absolute delta value in pixels calculated from the
		 * given value depending on the specified relativity.
		 *
		 * @param sceneRoot       Root view of the scene containing the target view in its view
		 *                        hierarchy for which the translate animation will be played.
		 * @param view            The target view for which will be the translate animation played.
		 * @param valueRelativity Relativity type describing how the given <var>value</var> should
		 *                        be treated.
		 * @param value           Either absolute or relative delta value used to properly calculate
		 *                        the absolute delta depending on the specified <var>valueRelativity</var>.
		 * @return Resolved translation delta value for X axis in pixels.
		 */
		float resolveDeltaY(@NonNull ViewGroup sceneRoot, @NonNull View view, @Description.ValueRelativity int valueRelativity, float value);
	}

	/*
	 * Static members ==============================================================================
	 */

	/**
	 * Default interpolator attached to {@link Animator} created via {@link #createAnimator(Transition, View, TransitionValues, int, int, float, float, float, float)}.
	 */
	public static final TimeInterpolator INTERPOLATOR = new FastOutSlowInInterpolator();

	/**
	 * Default implementation of {@link DeltaResolver} used to resolve translation delta values
	 * for {@link #onAppear(ViewGroup, TransitionValues, int, TransitionValues, int)} and
	 * {@link #onDisappear(ViewGroup, TransitionValues, int, TransitionValues, int)} methods.
	 */
	public static final DeltaResolver DELTA_RESOLVER = new DeltaResolver() {

		/**
		 */
		@Override
		public float resolveDeltaX(@NonNull ViewGroup sceneRoot, @NonNull View view, @Description.ValueRelativity int valueRelativity, float value) {
			return resolveDelta(valueRelativity, value, sceneRoot.getWidth(), view.getWidth());
		}

		/**
		 */
		@Override
		public float resolveDeltaY(@NonNull ViewGroup sceneRoot, @NonNull View view, @Description.ValueRelativity int valueRelativity, float value) {
			return resolveDelta(valueRelativity, value, sceneRoot.getHeight(), view.getHeight());
		}

		/**
		 * Resolves delta value for translation based on the specified parameters.
		 *
		 * @param valueRelativity Relativity of the given <var>value</var> determining how the value
		 *                        should be treated.
		 * @param value           Value from which to resolve translation delta.
		 * @param sceneSize       Size of the scene in which is presented the view to be translated.
		 * @param viewSize        Size of the view to be translated.
		 * @return Resolved translation delta according to the specified parameters.
		 */
		private float resolveDelta(@Description.ValueRelativity int valueRelativity, float value, int sceneSize, int viewSize) {
			switch (valueRelativity) {
				case Description.RELATIVE_TO_TARGET:
					return value * viewSize;
				case Description.RELATIVE_TO_SCENE:
					return value * sceneSize;
				case Description.NONE:
				default:
					return value;
			}
		}
	};

	/*
	 * Members =====================================================================================
	 */

	/**
	 * Delta by which should be the target view translated/moved in the scene along X axis. This may
	 * be either a fixed/absolute value in pixels or a relative/fraction value determining how much
	 * should be the view moved relative to its size (width) or relative to the size (width) of the
	 * root scene.
	 * <p>
	 * Relativity of this value is described by {@link #mTranslationXRelativity} value.
	 */
	private float mTranslationXDelta;

	/**
	 * Delta by which should be the target view translated/moved in the scene along Y axis. This may
	 * be either a fixed/absolute value in pixels or a relative/fraction value determining how much
	 * should be the view moved relative to its size (height) or relative to the size (height) of the
	 * root scene.
	 * <p>
	 * Relativity of this value is described by {@link #mTranslationYRelativity} value.
	 */
	private float mTranslationYDelta;

	/**
	 * Relativity type describing how should be treated value specified for {@link #mTranslationXDelta}.
	 * See relativity types defined by {@link Description.ValueRelativity @ValueRelativity} annotation.
	 */
	private int mTranslationXRelativity = Description.NONE;

	/**
	 * Relativity type describing how should be treated value specified for {@link #mTranslationYDelta}.
	 * See relativity types defined by {@link Description.ValueRelativity @ValueRelativity} annotation.
	 */
	private int mTranslationYRelativity = Description.NONE;

	/*
	 * Constructors ================================================================================
	 */

	/**
	 * Same as {@link #Translate(int)} with both {@link #MODE_IN} and {@link #MODE_OUT} modes combined.
	 */
	public Translate() {
		super();
	}

	/**
	 * Creates a new instance of Translate transition with the specified <var>mode</var>.
	 *
	 * @param mode One of {@link #MODE_IN} or {@link #MODE_OUT} or theirs combination.
	 */
	public Translate(@TranslateMode final int mode) {
		super();
		setMode(mode);
	}

	/**
	 * Creates a new instance of Translate transition with animation property values set from the
	 * specified <var>attrs</var>.
	 *
	 * @param context Context used to obtain values from the specified <var>attrs</var>.
	 * @param attrs   Set of attributes from which to obtain animation property values for the scale
	 *                animation.
	 */
	public Translate(@NonNull final Context context, @Nullable final AttributeSet attrs) {
		super(context, attrs);
		final Resources resources = context.getResources();
		final TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.Ui_Transition_Translate, 0, 0);
		for (int i = 0; i < attributes.getIndexCount(); i++) {
			final int index = attributes.getIndex(i);
			if (index == R.styleable.Ui_Transition_Translate_uiTranslationXDelta) {
				final Description description = Description.parseValue(resources, attributes.peekValue(index));
				this.mTranslationXRelativity = description.valueRelativity;
				this.mTranslationXDelta = description.value;
			} else if (index == R.styleable.Ui_Transition_Translate_uiTranslationYDelta) {
				final Description description = Description.parseValue(resources, attributes.peekValue(index));
				this.mTranslationYRelativity = description.valueRelativity;
				this.mTranslationYDelta = description.value;
			}
		}
		attributes.recycle();
	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Creates a new instance of Animator that animates both, translation X and translation Y, properties
	 * of the specified <var>view</var> with respect to its current translation values.
	 * <p>
	 * The created animator also properly handles cases when the translate animation is interrupted,
	 * paused or canceled, and possibly resumed later.
	 * <p>
	 * The returned animator will also have the default {@link #INTERPOLATOR} attached.
	 *
	 * @param transition       The transition that was requested to animate the given <var>view</var>.
	 * @param view             The view for which to create the requested animator.
	 * @param transitionValues Either start or end transition values containing view that is
	 *                         currently in the view hierarchy.
	 * @param viewX            Last known X position of the view on the screen captured by the transition.
	 * @param viewY            Last known Y position of the view on the screen captured by the transition.
	 * @param startX           Translation from which to start the animation along X axis.
	 * @param startY           Translation from which to start the animation along Y axis.
	 * @param endX             Translation at which should the animation end along X axis.
	 * @param endY             Translation at which should the animation end along Y axis.
	 * @return Animator that will play translate animation for the specified view according to the
	 * specified parameters when started or {@code null} if the start and end translation values are
	 * the same or the target view is already detached from window.
	 */
	@Nullable
	@SuppressWarnings("UnnecessaryLocalVariable")
	public static Animator createAnimator(
			@NonNull final Transition transition,
			@NonNull final View view,
			@NonNull final TransitionValues transitionValues,
			final int viewX,
			final int viewY,
			final float startX,
			final float startY,
			final float endX,
			final float endY
	) {
		if (!TransitionUtils.isViewAttachedToWindow(view)) {
			return null;
		}
		float animationStartX = startX;
		float animationStartY = startY;
		final float animationEndX = endX;
		final float animationEndY = endY;
		final float viewEndX = view.getTranslationX();
		final float viewEndY = view.getTranslationY();
		// Correct animation start coordinates by view's position on screen.
		final int[] startPosition = (int[]) transitionValues.view.getTag(R.id.ui_transition_tag_position);
		if (startPosition != null) {
			animationStartX = startPosition[0] - viewX + viewEndX;
			animationStartY = startPosition[1] - viewY + viewEndY;
		}
		if (animationStartX == animationEndX && animationStartY == animationEndY) {
			return null;
		}
		view.setTranslationX(animationStartX);
		view.setTranslationY(animationStartY);
		final int viewStartX = viewX + Math.round(startX - viewEndX);
		final int viewStartY = viewY + Math.round(startY - viewEndY);
		// Create path with animation properties and animator that will be used to play the desired
		// translate animation for the view.
		final Path path = new Path();
		path.moveTo(animationStartX, animationStartY);
		path.lineTo(animationEndX, animationEndY);
		final ObjectAnimator animator = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, View.TRANSLATION_Y, path);
		final TransitionAnimatorListener listener = new TransitionAnimatorListener(
				view,
				transitionValues.view,
				viewStartX, viewStartY,
				viewEndX, viewEndY
		);
		transition.addListener(listener);
		animator.addListener(listener);
		animator.addPauseListener(listener);
		animator.setInterpolator(INTERPOLATOR);
		return animator;
	}

	/**
	 * Sets a delta value by which should be the target view translated/moved in or out, absolutely
	 * or relatively, in the scene along X axis. This may be either a fixed/absolute value in pixels
	 * or a relative/fraction value determining how much should be the view moved relative to its
	 * size (width) or relative to the size (width) of the root scene.
	 * <p>
	 * Relativity of this value may be changed via {@link #setTranslationXRelativity(int)}.
	 * <p>
	 * Default value: {@code 0}
	 *
	 * @param xDelta The desired delta. If the delta will represent a relative fraction value, the
	 *               specified value should be from the range {@code [0.0, 1.0]}.
	 * @see R.attr#uiTranslationXDelta ui:uiTranslationXDelta
	 * @see #getTranslationXDelta()
	 * @see #getTranslationXRelativity()
	 */
	public void setTranslationXDelta(float xDelta) {
		this.mTranslationXDelta = xDelta;
	}

	/**
	 * Returns the delta by which should be the target view translated/moved in the root scene along
	 * X axis.
	 *
	 * @return Delta value for translation along X axis.
	 * @see #setTranslationXDelta(float)
	 */
	public float getTranslationXDelta() {
		return mTranslationXDelta;
	}

	/**
	 * Sets a delta value by which should be the target view translated/moved in or out, absolutely
	 * or relatively, in the scene along Y axis. This may be either a fixed/absolute value in pixels
	 * or a relative/fraction value determining how much should be the view moved relative to its
	 * size (height) or relative to the size (height) of the root scene.
	 * <p>
	 * Relativity of this value may be changed via {@link #setTranslationYRelativity(int)}.
	 * <p>
	 * Default value: {@code 0}
	 *
	 * @param yDelta The desired delta. If the delta will represent a relative fraction value, the
	 *               specified value should be from the range {@code [0.0, 1.0]}.
	 * @see R.attr#uiTranslationYDelta ui:uiTranslationYDelta
	 * @see #getTranslationYDelta()
	 * @see #getTranslationYRelativity()
	 */
	public void setTranslationYDelta(float yDelta) {
		this.mTranslationYDelta = yDelta;
	}

	/**
	 * Returns the delta by which should be the target view translated/moved in the root scene along
	 * Y axis.
	 *
	 * @return Delta value for translation along Y axis.
	 * @see #setTranslationYDelta(float)
	 */
	public float getTranslationYDelta() {
		return mTranslationYDelta;
	}

	/**
	 * Sets a relativity type that describes how should be treated delta value for translation along
	 * X axis specified via {@link #setTranslationXDelta(float)}. See {@link Description} for more
	 * information about supported relativity types and also how to choose a desired one.
	 * <p>
	 * Default value: {@link Description#NONE}
	 *
	 * @param xRelativity The desired relativity type. One of types defined by
	 *                    {@link Description.ValueRelativity @ValueRelativity} annotation.
	 * @see R.attr#uiTranslationXDelta ui:uiTranslationXDelta
	 * @see #getTranslationXRelativity()
	 */
	public void setTranslationXRelativity(@Description.ValueRelativity final int xRelativity) {
		this.mTranslationXRelativity = xRelativity;
	}

	/**
	 * Returns the relativity type describing value returned by {@link #getTranslationXDelta()}.
	 *
	 * @return Relativity of delta value for translation along X axis.
	 * @see #setTranslationXRelativity(int)
	 */
	@Description.ValueRelativity
	public int getTranslationXRelativity() {
		return mTranslationXRelativity;
	}

	/**
	 * Sets a relativity type that describes how should be treated delta value for translation along
	 * Y axis specified via {@link #setTranslationYDelta(float)}. See {@link Description} for more
	 * information about supported relativity types and also how to choose a desired one.
	 * <p>
	 * Default value: {@link Description#NONE}
	 *
	 * @param yRelativity The desired relativity type. One of types defined by
	 *                    {@link Description.ValueRelativity @ValueRelativity} annotation.
	 * @see R.attr#uiTranslationYDelta ui:uiTranslationYDelta
	 * @see #getTranslationYRelativity()
	 */
	public void setTranslationYRelativity(@Description.ValueRelativity final int yRelativity) {
		this.mTranslationYRelativity = yRelativity;
	}

	/**
	 * Returns the relativity type describing value returned by {@link #getTranslationYDelta()}.
	 *
	 * @return Relativity of delta value for translation along Y axis.
	 * @see #setTranslationYRelativity(int)
	 */
	@Description.ValueRelativity
	public int getTranslationYRelativity() {
		return mTranslationYRelativity;
	}

	/**
	 */
	@Override
	public void captureStartValues(@NonNull final TransitionValues transitionValues) {
		super.captureStartValues(transitionValues);
		this.captureValues(transitionValues);
	}

	/**
	 */
	@Override
	public void captureEndValues(@NonNull final TransitionValues transitionValues) {
		super.captureEndValues(transitionValues);
		this.captureValues(transitionValues);
	}

	/**
	 * Captures current location on screen for the view attached to the specified values and puts
	 * such value into the values map.
	 *
	 * @param values The values where to put captured values.
	 */
	private void captureValues(final TransitionValues values) {
		final int[] locationOnScreen = new int[2];
		values.view.getLocationOnScreen(locationOnScreen);
		values.values.put(PROPERTY_TRANSITION_LOCATION_ON_SCREEN, locationOnScreen);
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
		if (endValues == null) {
			return null;
		}
		final int[] locationOnScreen = (int[]) endValues.values.get(PROPERTY_TRANSITION_LOCATION_ON_SCREEN);
		if (locationOnScreen == null) {
			return null;
		}
		final float endX = view.getTranslationX();
		final float endY = view.getTranslationY();
		final float startX = endX + DELTA_RESOLVER.resolveDeltaX(sceneRoot, view, mTranslationXRelativity, mTranslationXDelta);
		final float startY = endY + DELTA_RESOLVER.resolveDeltaY(sceneRoot, view, mTranslationYRelativity, mTranslationYDelta);
		return createAnimator(
				this,
				view,
				endValues,
				locationOnScreen[0], locationOnScreen[1],
				startX, startY,
				endX, endY
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
		if (startValues == null) {
			return null;
		}
		final int[] locationOnScreen = (int[]) startValues.values.get(PROPERTY_TRANSITION_LOCATION_ON_SCREEN);
		if (locationOnScreen == null) {
			return null;
		}
		final float startX = view.getTranslationX();
		final float startY = view.getTranslationY();
		final float endX = startX + DELTA_RESOLVER.resolveDeltaX(sceneRoot, view, mTranslationXRelativity, mTranslationXDelta);
		final float endY = startY + DELTA_RESOLVER.resolveDeltaY(sceneRoot, view, mTranslationYRelativity, mTranslationYDelta);
		return createAnimator(
				this,
				view,
				startValues,
				locationOnScreen[0], locationOnScreen[1],
				startX, startY,
				endX, endY
		);
	}

	/*
	 * Inner classes ===============================================================================
	 */

	/**
	 * Describes a translation delta value specified via one of {@link R.attr#uiTranslationXDelta uiTranslationXDelta},
	 * {@link R.attr#uiTranslationYDelta uiTranslationYDelta} attributes.
	 *
	 * @author Martin Albedinsky
	 */
	public static final class Description {

		/**
		 * Constant used to identify that {@link Description#value} should be treated as absolute
		 * (float or dimension) value and not as relative one.
		 */
		public static final int NONE = 0x00;

		/**
		 * Constant used to identify that {@link Description#value} should be treated as relative
		 * fraction to the target view (its size) to be translated.
		 */
		public static final int RELATIVE_TO_TARGET = 0x01;

		/**
		 * Constant used to identify that {@link Description#value} should be treated as relative
		 * fraction to the scene root (its size) where the target view to be animated is presented.
		 */
		public static final int RELATIVE_TO_SCENE = 0x02;

		/**
		 * Defines an annotation for determining set of supported relativity types for translation
		 * delta value of Translate transition.
		 */
		@IntDef({NONE, RELATIVE_TO_TARGET, RELATIVE_TO_SCENE})
		@Retention(RetentionPolicy.SOURCE)
		public @interface ValueRelativity {
		}

		/**
		 * Relativity type parsed for this value description. One of types defined by
		 * {@link ValueRelativity @ValueRelativity} annotation.
		 *
		 * @see #parseValue(Resources, TypedValue)
		 */
		@ValueRelativity
		public final int valueRelativity;

		/**
		 * Value parsed for this description. This is either absolute value (float or dimension pixel
		 * size) or a fraction that should be treated relatively to either target view or scene root
		 * depending on {@link #valueRelativity} type.
		 *
		 * @see #parseValue(Resources, TypedValue)
		 */
		public final float value;

		/**
		 * Creates a new instance of Description with the specified <var>valueRelativity</var> and
		 * <var>value</var>.
		 *
		 * @param valueRelativity The value relativity type for the new description.
		 * @param value           The value for the new description.
		 */
		private Description(@ValueRelativity final int valueRelativity, final float value) {
			this.valueRelativity = valueRelativity;
			this.value = value;
		}

		/**
		 * Parses a new Description with translation delta value and value relativity type from the
		 * given <var>typedValue</var>.
		 *
		 * @param resources  Resources used to properly parse value type of <b>dimension</b>.
		 * @param typedValue Typed value containing translation delta value to be parsed.
		 * @return Description with value and relativity type parsed from the given <var>typedValue</var>.
		 */
		@NonNull
		public static Description parseValue(@NonNull Resources resources, @Nullable TypedValue typedValue) {
			int valueRelativity = NONE;
			float value = 0;
			if (typedValue != null) {
				switch (typedValue.type) {
					case TypedValue.TYPE_FLOAT:
						value = typedValue.getFloat();
						break;
					case TypedValue.TYPE_DIMENSION:
						value = TypedValue.complexToDimensionPixelSize(typedValue.data, resources.getDisplayMetrics());
						break;
					case TypedValue.TYPE_FRACTION:
						valueRelativity = (typedValue.data & TypedValue.COMPLEX_UNIT_MASK) == TypedValue.COMPLEX_UNIT_FRACTION_PARENT ?
								RELATIVE_TO_SCENE :
								RELATIVE_TO_TARGET;
						value = TypedValue.complexToFloat(typedValue.data);
						break;
					default:
						if (typedValue.type >= TypedValue.TYPE_FIRST_INT && typedValue.type <= TypedValue.TYPE_LAST_INT) {
							value = typedValue.data;
						}
						break;
				}
			}
			return new Description(valueRelativity, value);
		}
	}

	/**
	 * Listener that is used by {@link Translate} transition to change properties of the animating
	 * view according to the received animation callbacks.
	 */
	@VisibleForTesting
	static final class TransitionAnimatorListener extends AnimatorListenerAdapter implements TransitionListener {

		/**
		 * View of which properties to change due to received animation callbacks.
		 */
		private final View animatingView;

		/**
		 * Static (not animating) view that is currently in a view hierarchy.
		 */
		private final View staticView;

		/**
		 * Pair of current positions of translate animation.
		 */
		private int[] animationPosition;

		/**
		 * Position where should the translate animation start along X axis.
		 */
		private final float startX;

		/**
		 * Position at which should the translate animation start along Y axis.
		 */
		private final float startY;

		/**
		 * Position at which was the translate animation paused along X axis.
		 */
		private float pausedX;

		/**
		 * Position at which was the translate animation paused along Y axis.
		 */
		private float pausedY;

		/**
		 * Position at which should the translate animation end along X axis.
		 */
		private final float endX;

		/**
		 * Position at which should the translate animation end along Y axis.
		 */
		private final float endY;

		/**
		 * Creates a new instance of TransitionAnimatorListener with the specified views and translate
		 * animation properties.
		 *
		 * @param animatingView The animating view of which properties may be changed by the
		 *                      animator listener as result of received animation callbacks.
		 * @param staticView    Static (not animating) view that is currently in a view hierarchy.
		 * @param startX        Start position of the translate animation along X axis.
		 * @param startY        Start position of the translate animation along Y axis.
		 * @param endX          End position of the translate animation along X axis.
		 * @param endY          End position of the translate animation along Y axis.
		 */
		TransitionAnimatorListener(
				View animatingView,
				View staticView,
				float startX,
				float startY,
				float endX,
				float endY
		) {
			super();
			this.animatingView = animatingView;
			this.staticView = staticView;
			this.startX = startX - Math.round(animatingView.getTranslationX());
			this.startY = startY - Math.round(animatingView.getTranslationY());
			this.endX = endX;
			this.endY = endY;
			this.animationPosition = (int[]) staticView.getTag(R.id.ui_transition_tag_position);
			if (animationPosition != null) {
				staticView.setTag(R.id.ui_transition_tag_position, null);
			}
		}

		/**
		 */
		@Override
		public void onAnimationCancel(Animator animation) {
			if (animationPosition == null) {
				this.animationPosition = new int[2];
			}
			this.animationPosition[0] = Math.round(startX + animatingView.getTranslationX());
			this.animationPosition[1] = Math.round(startY + animatingView.getTranslationY());
			this.staticView.setTag(R.id.ui_transition_tag_position, animationPosition);
		}

		/**
		 */
		@Override
		public void onAnimationPause(Animator animation) {
			this.pausedX = animatingView.getTranslationX();
			this.pausedY = animatingView.getTranslationY();
			this.animatingView.setTranslationX(endX);
			this.animatingView.setTranslationY(endY);
		}

		/**
		 */
		@Override
		public void onAnimationResume(Animator animation) {
			this.animatingView.setTranslationX(pausedX);
			this.animatingView.setTranslationY(pausedY);
		}

		/**
		 */
		@Override
		public void onTransitionStart(@NonNull final Transition transition) {
			// Ignored.
		}

		/**
		 */
		@Override
		public void onTransitionEnd(@NonNull final Transition transition) {
			this.animatingView.setTranslationX(endX);
			this.animatingView.setTranslationY(endY);
		}

		/**
		 */
		@Override
		public void onTransitionCancel(@NonNull final Transition transition) {
			// Ignored.
		}

		/**
		 */
		@Override
		public void onTransitionPause(@NonNull final Transition transition) {
			// Ignored.
		}

		/**
		 */
		@Override
		public void onTransitionResume(@NonNull final Transition transition) {
			// Ignored.
		}
	}
}
