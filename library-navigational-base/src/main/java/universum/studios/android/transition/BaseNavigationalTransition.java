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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * BaseNavigationalTransition represents a simple wrapper around window {@link Transition Transitions}
 * framework to simplify and also to made transitioning between two {@link Activity Activities}
 * independent from the current Android version, that is, to hide such transitions implementation
 * from the context where it is used.
 * <p>
 * A specific navigational transition implementation may be associated with its corresponding activity
 * class that can be supplied to {@link #BaseNavigationalTransition(Class)} constructor. This class
 * is used to create an instance of Intent that will be by default started whenever {@link #start(Activity)}
 * is invoked. Both {@link #onStart(Activity)} and {@link #onFinish(Activity)} methods cen be overridden
 * by a specific transition implementations to match theirs specific needs.
 *
 * <h3>Sample</h3>
 * <pre>
 * public abstract class DetailTransition extends BaseNavigationalTransition {
 *
 *     DetailTransition() {
 *         super(DetailActivity.class);
 *     }
 *
 *     &#64;NonNull
 *     public static DetailTransition get() {
 *          return UiConfig.MATERIAL_SUPPORT ? new MaterialTransition() : new DefaultTransition();
 *     }
 *
 *     &#64;Override
 *     public Intent createIntent(&#64;NonNull Activity caller) {
 *         final Intent intent = super.createIntent(caller);
 *         // ... put here any extras that need to be delivered to the called activity
 *         return intent;
 *     }
 *
 *     static final class DefaultTransition extends DetailTransition {
 *
 *          &#64;Override
 *          protected void onStart(&#64;NonNull Activity caller) {
 *              super.onStart(caller);
 *              // ... change activities with custom window animation
 *              WindowTransition.overrideStart(caller);
 *          }
 *
 *          &#64;Override
 *          protected void onFinish(&#64;NonNull Activity caller) {
 *              super.onFinish(caller);
 *              // ... change activities without animation
 *              caller.overridePendingTransition(0, 0);
 *          }
 *     }
 *
 *     // ... other implementations specific for a different platform versions
 *
 *     static final class MaterialTransition extends DetailTransition {
 *
 *          &#64;Override
 *          protected void onStart(&#64;NonNull Activity caller) {
 *              // ... made some modifications to the caller's UI (if necessary) before the transitions
 *              // ... are triggered.
 *              super.onStart(caller);
 *          }
 *
 *          &#64;Override
 *          protected void onFinish(&#64;NonNull Activity caller) {
 *              super.onFinish(caller);
 *              // ... no need to override in most cases
 *          }
 *     }
 * }
 * </pre>
 *
 * @author Martin Albedinsky
 */
public abstract class BaseNavigationalTransition<T extends BaseNavigationalTransition> {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	//private static final String TAG = "BaseNavigationalTransition";

	/**
	 * Boolean flag indicating whether the current Android device can run Material Design like navigational
	 * transition (i. e. with shared elements, ...) or not.
	 */
	public static final boolean MATERIAL_SUPPORT = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;

	/**
	 * Flag determining that no request code has been specified for a specific navigational transition.
	 */
	public static final int RC_NONE = -1;

	/**
	 * Flag determining whether an <b>enter</b> transition has been specified via
	 * {@link #enterTransition(Transition)} or ot.
	 */
	private static final int TRANSITION_ENTER = 0x00000001;

	/**
	 * Flag determining whether a <b>reenter</b> transition has been specified via
	 * {@link #reenterTransition(Transition)} or ot.
	 */
	private static final int TRANSITION_REENTER = 0x00000001 << 1;

	/**
	 * Flag determining whether a <b>return</b> transition has been specified via
	 * {@link #returnTransition(Transition)} or ot.
	 */
	private static final int TRANSITION_RETURN = 0x00000001 << 2;

	/**
	 * Flag determining whether an <b>exit</b> transition has been specified via
	 * {@link #exitTransition(Transition)} or ot.
	 */
	private static final int TRANSITION_EXIT = 0x00000001 << 3;

	/**
	 * Flag determining whether a shared element's <b>enter</b> transition has been specified via
	 * {@link #sharedElementEnterTransition(Transition)} or ot.
	 */
	private static final int TRANSITION_SHARED_ELEMENT_ENTER = 0x00000001 << 4;

	/**
	 * Flag determining whether a shared element's <b>reenter</b> transition has been specified via
	 * {@link #sharedElementReenterTransition(Transition)} or ot.
	 */
	private static final int TRANSITION_SHARED_ELEMENT_REENTER = 0x00000001 << 5;

	/**
	 * Flag determining whether a shared element's <b>return</b> transition has been specified via
	 * {@link #sharedElementReturnTransition(Transition)} or ot.
	 */
	private static final int TRANSITION_SHARED_ELEMENT_RETURN = 0x00000001 << 6;

	/**
	 * Flag determining whether a shared element's <b>exit</b> transition has been specified via
	 * {@link #sharedElementExitTransition(Transition)} or ot.
	 */
	private static final int TRANSITION_SHARED_ELEMENT_EXIT = 0x00000001 << 7;

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
	 * Class of activity that should be started as new Intent whenever this navigational transition
	 * is started via {@link #start(Activity)}.
	 */
	protected final Class<? extends Activity> mClassOfTransitionActivity;

	/**
	 * Bundle containing extras for the activity transition activity.
	 */
	private Bundle mIntentExtras;

	/**
	 * If set (other than {@link #RC_NONE}) starting of intent specific for this navigational transition
	 * will be performed via {@link Activity#startActivityForResult(Intent, int)}, {@link Activity#startActivityForResult(Intent, int, Bundle)}
	 * or {@link Fragment#startActivityForResult(Intent, int)} depends on the type of caller and
	 * current configuration of this transition.
	 */
	int mRequestCode = RC_NONE;

	/**
	 * Transition that should be attached to caller's activity window if {@link #TRANSITION_ENTER}
	 * flag is contained within {@link #mSpecifiedTransitions} flags.
	 *
	 * @see Window#setEnterTransition(Transition)
	 */
	private Transition mEnterTransition;

	/**
	 * Transition that should be attached to caller's activity window if {@link #TRANSITION_REENTER}
	 * flag is contained within {@link #mSpecifiedTransitions} flags.
	 *
	 * @see Window#setReenterTransition(Transition)
	 */
	private Transition mReenterTransition;

	/**
	 * Transition that should be attached to caller's activity window if {@link #TRANSITION_RETURN}
	 * flag is contained within {@link #mSpecifiedTransitions} flags.
	 *
	 * @see Window#setReturnTransition(Transition)
	 */
	private Transition mReturnTransition;

	/**
	 * Transition that should be attached to caller's activity window if {@link #TRANSITION_EXIT}
	 * flag is contained within {@link #mSpecifiedTransitions} flags.
	 *
	 * @see Window#setExitTransition(Transition)
	 */
	private Transition mExitTransition;

	/**
	 * Transition for shared element that should be attached to caller's activity window if
	 * {@link #TRANSITION_SHARED_ELEMENT_ENTER} flag is contained within {@link #mSpecifiedTransitions} flags.
	 *
	 * @see Window#setSharedElementEnterTransition(Transition)
	 */
	private Transition mSharedElementEnterTransition;

	/**
	 * Transition for shared element that should be attached to caller's activity window if
	 * {@link #TRANSITION_SHARED_ELEMENT_REENTER} flag is contained within {@link #mSpecifiedTransitions} flags.
	 *
	 * @see Window#setSharedElementReenterTransition(Transition)
	 */
	private Transition mSharedElementReenterTransition;

	/**
	 * Transition for shared element that should be attached to caller's activity window if
	 * {@link #TRANSITION_SHARED_ELEMENT_RETURN} flag is contained within {@link #mSpecifiedTransitions} flags.
	 *
	 * @see Window#setSharedElementReturnTransition(Transition)
	 */
	private Transition mSharedElementReturnTransition;

	/**
	 * Transition for shared element that should be attached to caller's activity window if
	 * {@link #TRANSITION_SHARED_ELEMENT_EXIT} flag is contained within {@link #mSpecifiedTransitions} flags.
	 *
	 * @see Window#setSharedElementExitTransition(Transition)
	 */
	private Transition mSharedElementExitTransition;

	/**
	 * Transition flags determining which transitions has been specified for this navigational transition.
	 */
	private int mSpecifiedTransitions;

	/**
	 * Boolean flag indicating whether an <b>enter</b> transition can overlap or not.
	 */
	private Boolean mAllowEnterTransitionOverlap;

	/**
	 * Boolean flag indicating whether an <b>return</b> transition can overlap or not.
	 */
	private Boolean mAllowReturnTransitionOverlap;

	/**
	 * List containing all shared elements specified for this navigational transition. If not empty,
	 * these elements should be transferred to the called activity through Bundle created from
	 * ActivityOptions that can be created via {@link #makeSceneTransitionAnimation(Activity)}.
	 */
	private List<Pair<View, String>> mSharedElements;

	/**
	 * Boolean flag indicating whether transitioning shared elements should use overlay or not.
	 */
	private Boolean mSharedElementUseOverlay;

	/**
	 * Inflater that can be used to inflate a new transition or transition manager.
	 */
	private TransitionInflater mTransitionInflater;

	/*
	 * Constructors ================================================================================
	 */

	/**
	 * Creates a new instance of NavigationalTransition without transition activity class.
	 */
	public BaseNavigationalTransition() {
		this.mClassOfTransitionActivity = null;
	}

	/**
	 * Creates a new instance of NavigationalTransition with the specified class of transition activity.
	 *
	 * @param classOfTransitionActivity The activity class that should be used to create a new Intent
	 *                                  that to be started whenever {@link #start(Activity)} is called
	 *                                  upon this navigational transition.
	 */
	public BaseNavigationalTransition(@NonNull final Class<? extends Activity> classOfTransitionActivity) {
		this.mClassOfTransitionActivity = classOfTransitionActivity;
	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Returns the class of activity specified for this navigational transition.
	 *
	 * @return This transitions's associated activity class or {@code null} if not activity class
	 * has been specified.
	 * @see #BaseNavigationalTransition(Class)
	 */
	@Nullable
	public final Class<? extends Activity> getActivityClass() {
		return mClassOfTransitionActivity;
	}

	/**
	 * Specifies a bundle with extras for the <b>incoming</b> activity. The given bundle will be
	 * attached to this transition's {@link Intent} via {@link Intent#putExtras(Bundle)} when it is
	 * created via {@link #createIntent(Activity)}.
	 *
	 * @param extras The desired bundle with extras. May be {@code null} to clear the current one.
	 * @return This transition to allow methods chaining.
	 * @see #intentExtras()
	 */
	@SuppressWarnings("unchecked")
	public T intentExtras(@Nullable final Bundle extras) {
		this.mIntentExtras = extras;
		return (T) this;
	}

	/**
	 * Returns the bundle with extras for the <b>incoming</b> activity.
	 * <p>
	 * This will be either bundle specified via {@link #intentExtras(Bundle)} or created by default
	 * when this method is called for the first time.
	 *
	 * @return The extras bundle for the <b>incoming</b> activity.
	 * @see #intentExtras(Bundle)
	 */
	@NonNull
	public Bundle intentExtras() {
		return mIntentExtras == null ? (mIntentExtras = new Bundle()) : mIntentExtras;
	}

	/**
	 * Specifies a request code that should be used to start activity specific for this navigational
	 * transition <b>for result</b>.
	 * <p>
	 * Default value: <b>{@link #RC_NONE}</b>
	 *
	 * @param requestCode The desired request code. Can be {@link #RC_NONE} to not start activity
	 *                    for result.
	 * @return This transition to allow methods chaining.
	 * @see #requestCode()
	 * @see Activity#startActivityForResult(Intent, int)
	 * @see Activity#startActivityForResult(Intent, int, Bundle)
	 * @see Fragment#startActivityForResult(Intent, int)
	 */
	@SuppressWarnings("unchecked")
	public T requestCode(@IntRange(from = RC_NONE) final int requestCode) {
		this.mRequestCode = requestCode;
		return (T) this;
	}

	/**
	 * Returns the request code specified via {@link #requestCode(int)}.
	 *
	 * @return Specified request code or {@link #RC_NONE} by default.
	 */
	@IntRange(from = RC_NONE)
	public int requestCode() {
		return mRequestCode;
	}

	/**
	 * Specifies enter transition to be attached to a window of an <b>incoming</b> activity.
	 *
	 * @return This transition to allow methods chaining.
	 * @see #enterTransition()
	 * @see Window#setEnterTransition(Transition)
	 * @see #inflateTransition(Context, int)
	 * @see #configureIncomingTransitions(Activity)
	 */
	@SuppressWarnings("unchecked")
	public T enterTransition(@Nullable final Transition transition) {
		this.mSpecifiedTransitions |= TRANSITION_ENTER;
		this.mEnterTransition = transition;
		return (T) this;
	}

	/**
	 * Returns enter transition for the <b>incoming</b> activity.
	 *
	 * @return Transition specified via {@link #enterTransition(Transition)} or {@code null} by
	 * default.
	 */
	@Nullable
	public Transition enterTransition() {
		return mEnterTransition;
	}

	/**
	 * Specifies reenter transition to be attached to a window of an <b>outgoing</b> activity whenever
	 * {@link #start(Activity)} is invoked.
	 *
	 * @return This transition to allow methods chaining.
	 * @see #reenterTransition()
	 * @see Window#setReenterTransition(Transition)
	 * @see #inflateTransition(Context, int)
	 * @see #configureOutgoingTransitions(Activity)
	 */
	@SuppressWarnings("unchecked")
	public T reenterTransition(@Nullable final Transition transition) {
		this.mSpecifiedTransitions |= TRANSITION_REENTER;
		this.mReenterTransition = transition;
		return (T) this;
	}

	/**
	 * Returns reenter transition for the <b>outgoing</b> activity.
	 *
	 * @return Transition specified via {@link #reenterTransition(Transition)} or {@code null} by
	 * default.
	 */
	@Nullable
	public Transition reenterTransition() {
		return mReenterTransition;
	}

	/**
	 * Specifies return transition to be attached to a window of an <b>incoming</b> activity.
	 *
	 * @return This transition to allow methods chaining.
	 * @see #returnTransition()
	 * @see Window#setReturnTransition(Transition)
	 * @see #inflateTransition(Context, int)
	 * @see #configureIncomingTransitions(Activity)
	 */
	@SuppressWarnings("unchecked")
	public T returnTransition(@Nullable final Transition transition) {
		this.mSpecifiedTransitions |= TRANSITION_RETURN;
		this.mReturnTransition = transition;
		return (T) this;
	}

	/**
	 * Returns return transition for the <b>incoming</b> activity.
	 *
	 * @return Transition specified via {@link #returnTransition(Transition)} or {@code null} by
	 * default.
	 */
	@Nullable
	public Transition returnTransition() {
		return mReturnTransition;
	}

	/**
	 * Specifies exit transition to be attached to a window of an <b>outgoing</b> activity whenever
	 * {@link #start(Activity)} is invoked.
	 *
	 * @return This transition to allow methods chaining.
	 * @see #exitTransition()
	 * @see Window#setExitTransition(Transition)
	 * @see #inflateTransition(Context, int)
	 * @see #configureOutgoingTransitions(Activity)
	 */
	@SuppressWarnings("unchecked")
	public T exitTransition(@Nullable final Transition transition) {
		this.mSpecifiedTransitions |= TRANSITION_EXIT;
		this.mExitTransition = transition;
		return (T) this;
	}

	/**
	 * Returns exit transition for the <b>outgoing</b> activity.
	 *
	 * @return Transition specified via {@link #exitTransition(Transition)} or {@code null} by
	 * default.
	 */
	@Nullable
	public Transition exitTransition() {
		return mExitTransition;
	}

	/**
	 * Specifies a boolean flag to be set to a window of an <b>incoming</b> activity in order to
	 * indicate whether enter transition may overlap or not.
	 * <p>
	 * Default value: <b>{@code unspecified}</b>
	 *
	 * @return This transition to allow methods chaining.
	 * @see #allowEnterTransitionOverlap()
	 * @see Window#setAllowEnterTransitionOverlap(boolean)
	 * @see #configureIncomingTransitions(Activity)
	 */
	@SuppressWarnings("unchecked")
	public T allowEnterTransitionOverlap(final boolean allow) {
		this.mAllowEnterTransitionOverlap = allow;
		return (T) this;
	}

	/**
	 * Returns boolean flag indicating whether enter transition may overlap or not.
	 * <p>
	 * This method returns {@code true} if overlapping has not been specified, which is a default
	 * behaviour of {@link Window#getAllowEnterTransitionOverlap()}.
	 *
	 * @return {@code True} if overlapping of enter transition is enabled, {@code false} otherwise.
	 */
	public boolean allowEnterTransitionOverlap() {
		return mAllowEnterTransitionOverlap == null || mAllowEnterTransitionOverlap;
	}

	/**
	 * Specifies a boolean flag to be set to a window of an <b>incoming</b> activity in order to
	 * indicate whether return transition may overlap or not.
	 * <p>
	 * Default value: <b>{@code unspecified}</b>
	 *
	 * @return This transition to allow methods chaining.
	 * @see #allowReturnTransitionOverlap()
	 * @see Window#setAllowReturnTransitionOverlap(boolean)
	 * @see #configureIncomingTransitions(Activity)
	 */
	@SuppressWarnings("unchecked")
	public T allowReturnTransitionOverlap(final boolean allow) {
		this.mAllowReturnTransitionOverlap = allow;
		return (T) this;
	}

	/**
	 * Returns boolean flag indicating whether return transition may overlap or not.
	 * <p>
	 * This method returns {@code true} if overlapping has not been specified, which is a default
	 * behaviour of {@link Window#getAllowReturnTransitionOverlap()}.
	 *
	 * @return {@code True} if overlapping of return transition is enabled, {@code false} otherwise.
	 */
	public boolean allowReturnTransitionOverlap() {
		return mAllowReturnTransitionOverlap == null || mAllowReturnTransitionOverlap;
	}

	/**
	 * Specifies enter transition for shared element to be attached to a window of an <b>incoming</b>
	 * activity.
	 *
	 * @return This transition to allow methods chaining.
	 * @see #sharedElementEnterTransition()
	 * @see Window#setSharedElementEnterTransition(Transition)
	 * @see #inflateTransition(Context, int)
	 * @see #configureIncomingTransitions(Activity)
	 */
	@SuppressWarnings("unchecked")
	public T sharedElementEnterTransition(@Nullable final Transition transition) {
		this.mSpecifiedTransitions |= TRANSITION_SHARED_ELEMENT_ENTER;
		this.mSharedElementEnterTransition = transition;
		return (T) this;
	}

	/**
	 * Returns shared element enter transition for the <b>incoming</b> activity.
	 *
	 * @return Transition for shared element specified via {@link #sharedElementEnterTransition(Transition)}
	 * or {@code null} by default.
	 */
	@Nullable
	public Transition sharedElementEnterTransition() {
		return mSharedElementEnterTransition;
	}

	/**
	 * Specifies reenter transition for shared element to be attached to a window of an <b>outgoing</b>
	 * activity whenever {@link #start(Activity)} is invoked.
	 *
	 * @return This transition to allow methods chaining.
	 * @see #sharedElementReenterTransition()
	 * @see Window#setSharedElementReenterTransition(Transition)
	 * @see #inflateTransition(Context, int)
	 * @see #configureOutgoingTransitions(Activity)
	 */
	@SuppressWarnings("unchecked")
	public T sharedElementReenterTransition(@Nullable final Transition transition) {
		this.mSpecifiedTransitions |= TRANSITION_SHARED_ELEMENT_REENTER;
		this.mSharedElementReenterTransition = transition;
		return (T) this;
	}

	/**
	 * Returns shared element reenter transition for the <b>outgoing</b> activity.
	 *
	 * @return Transition for shared element specified via {@link #sharedElementReenterTransition(Transition)}
	 * or {@code null} by default.
	 */
	@Nullable
	public Transition sharedElementReenterTransition() {
		return mSharedElementReenterTransition;
	}

	/**
	 * Specifies return transition for shared element to be attached to a window of an <b>incoming</b>
	 * activity.
	 *
	 * @return This transition to allow methods chaining.
	 * @see #sharedElementReturnTransition()
	 * @see Window#setSharedElementReturnTransition(Transition)
	 * @see #inflateTransition(Context, int)
	 * @see #configureIncomingTransitions(Activity)
	 */
	@SuppressWarnings("unchecked")
	public T sharedElementReturnTransition(@Nullable final Transition transition) {
		this.mSpecifiedTransitions |= TRANSITION_SHARED_ELEMENT_RETURN;
		this.mSharedElementReturnTransition = transition;
		return (T) this;
	}

	/**
	 * Returns shared element return transition for the <b>incoming</b> activity.
	 *
	 * @return Transition for shared element specified via {@link #sharedElementReturnTransition(Transition)}
	 * or {@code null} by default.
	 */
	@Nullable
	public Transition sharedElementReturnTransition() {
		return mSharedElementReturnTransition;
	}

	/**
	 * Specifies exit transition for shared element to be attached to a window of an <b>outgoing</b>
	 * activity whenever {@link #start(Activity)} is invoked.
	 *
	 * @return This transition to allow methods chaining.
	 * @see #sharedElementExitTransition()
	 * @see Window#setSharedElementExitTransition(Transition)
	 * @see #inflateTransition(Context, int)
	 */
	@SuppressWarnings("unchecked")
	public T sharedElementExitTransition(@Nullable final Transition transition) {
		this.mSpecifiedTransitions |= TRANSITION_SHARED_ELEMENT_EXIT;
		this.mSharedElementExitTransition = transition;
		return (T) this;
	}

	/**
	 * Returns shared element exit transition for the <b>outgoing</b> activity.
	 *
	 * @return Transition for shared element specified via {@link #sharedElementExitTransition(Transition)}
	 * or {@code null} by default.
	 */
	@Nullable
	public Transition sharedElementExitTransition() {
		return mSharedElementExitTransition;
	}

	/**
	 * Specifies a boolean flag to be set to a window of an <b>incoming</b> or <b>outgoing</b> activity
	 * in order to indicate whether shared elements should use overlay or not.
	 * <p>
	 * Default value: <b>{@code unspecified}</b>
	 *
	 * @return This transition to allow methods chaining.
	 * @see #sharedElementsUseOverlay()
	 * @see Window#setSharedElementsUseOverlay(boolean)
	 * @see #configureIncomingTransitions(Activity)
	 * @see #configureOutgoingTransitions(Activity)
	 */
	@SuppressWarnings("unchecked")
	public T sharedElementsUseOverlay(final boolean useOverlay) {
		this.mSharedElementUseOverlay = useOverlay;
		return (T) this;
	}

	/**
	 * Returns boolean flag indicating whether shared elements should use overlay or not.
	 * <p>
	 * This method returns {@code true} if usage of overlay has not been specified, which is a default
	 * behaviour of {@link Window#getSharedElementsUseOverlay()}.
	 *
	 * @return {@code True} if shared elements will use overlay, {@code false} otherwise.
	 * @see #sharedElementsUseOverlay(boolean)
	 */
	public boolean sharedElementsUseOverlay() {
		return mSharedElementUseOverlay == null || mSharedElementUseOverlay;
	}

	/**
	 * Inflates a new instance of Transition from the specified <var>resource</var>.
	 * <p>
	 * <b>Note</b>, that for pre {@link Build.VERSION_CODES#LOLLIPOP LOLLIPOP} Android
	 * versions this method does nothing and returns {@code null}.
	 *
	 * @param context  Context used to inflate the desired transition.
	 * @param resource Resource id of the desired transition to inflate.
	 * @return Inflated transition.
	 * @see #inflateTransitionManager(Context, int, ViewGroup)
	 */
	@Nullable
	@SuppressLint("NewApi")
	public Transition inflateTransition(@NonNull final Context context, final int resource) {
		if (MATERIAL_SUPPORT) {
			if (mTransitionInflater == null) {
				this.mTransitionInflater = TransitionInflater.from(context);
			}
			return mTransitionInflater.inflateTransition(resource);
		}
		return null;
	}

	/**
	 * Inflates a new instance of TransitionManager from the specified <var>resource</var>.
	 * <p>
	 * <b>Note</b>, that for pre {@link Build.VERSION_CODES#LOLLIPOP LOLLIPOP} Android
	 * versions this method does nothing and returns {@code null}.
	 *
	 * @param context   Context used to inflate the desired transition.
	 * @param resource  Resource id of the desired transition manager to inflater.
	 * @param sceneRoot Root view for the scene with which will the manager operate.
	 * @return Inflated transition manager.
	 * @see #inflateTransition(Context, int)
	 */
	@Nullable
	@SuppressLint("NewApi")
	public TransitionManager inflateTransitionManager(@NonNull final Context context, final int resource, @NonNull final ViewGroup sceneRoot) {
		if (MATERIAL_SUPPORT) {
			if (mTransitionInflater == null) {
				this.mTransitionInflater = TransitionInflater.from(context);
			}
			return mTransitionInflater.inflateTransitionManager(resource, sceneRoot);
		}
		return null;
	}

	/**
	 * Bulk method for adding shared element pairs into this navigational transition.
	 *
	 * @param elements The desired shared elements pairs.
	 * @return This transition to allow methods chaining.
	 * @see #sharedElement(View, String)
	 */
	@SafeVarargs
	@SuppressWarnings("unchecked")
	public final T sharedElements(@NonNull final Pair<View, String>... elements) {
		if (mSharedElements == null) {
			this.mSharedElements = new ArrayList<>(1);
		}
		mSharedElements.addAll(Arrays.asList(elements));
		return (T) this;
	}

	/**
	 * Adds shared element that should be transferred to the transitioning (called) activity via
	 * {@link ActivityOptions} as Bundle.
	 * <p>
	 * All shared elements and their names added into this navigational transition will be bundled
	 * from ActivityOptions created via {@link #makeSceneTransitionAnimation(Activity)} and transferred
	 * to the called activity whenever {@link #start(Activity)} is called.
	 * <p>
	 * <b>Note</b>, that each shared element must have a unique name.
	 *
	 * @param element     The view to be shared via transition.
	 * @param elementName The name of the shared element.
	 * @return This transition to allow methods chaining.
	 * @see #sharedElements(Pair[])
	 * @see #makeSceneTransitionAnimation(Activity)
	 */
	@SuppressWarnings("unchecked")
	public T sharedElement(@NonNull final View element, @NonNull final String elementName) {
		if (mSharedElements == null) {
			this.mSharedElements = new ArrayList<>(1);
		}
		mSharedElements.add(new Pair<>(element, elementName));
		return (T) this;
	}

	/**
	 * Returns a list of all shared elements specified for this navigational transition.
	 *
	 * @return List of shared elements or {@code null} if there are no shared elements specified.
	 * @see #sharedElements(Pair[])
	 * @see #sharedElement(View, String)
	 * @see #singleSharedElement()
	 */
	@Nullable
	public List<Pair<View, String>> sharedElements() {
		return mSharedElements;
	}

	/**
	 * Returns the single shared element at the {@code 0} position among the current shared elements.
	 * <p>
	 * This method may be used to obtain a single shared element when there is specified only one for
	 * this navigational transition.
	 *
	 * @return Single shared element or {@code null} if there are no shared elements specified.
	 * @see #sharedElements()
	 * @see #sharedElement(View, String)
	 */
	@Nullable
	public Pair<View, String> singleSharedElement() {
		return mSharedElements != null && !mSharedElements.isEmpty() ? mSharedElements.get(0) : null;
	}

	/**
	 * Starts this navigational transition using the given <var>caller</var> activity with transitions
	 * and shared elements that are configured for the activity via {@link #configureOutgoingTransitions(Activity)}.
	 *
	 * @param caller The activity that will be used to create and start an Intent created via
	 *               {@link #createIntent(Activity)}.
	 */
	public void start(@NonNull final Activity caller) {
		configureOutgoingTransitions(caller);
		onStart(caller);
	}

	/**
	 * Invoked whenever {@link #start(Activity)} is called.
	 * <p>
	 * Default implementation starts an Intent created via {@link #createIntent(Activity)} using
	 * the given caller activity via {@link Activity#startActivity(Intent)} for the pre LOLLIPOP
	 * Android versions and for the post LOLLIPOP via {@link Activity#startActivity(Intent, Bundle)}
	 * where will be passed Bundle created from {@link ActivityOptions} that has been requested via
	 * {@link #makeSceneTransitionAnimation(Activity)}.
	 * <p>
	 * If there was specified some {@link #requestCode()} the intent will be started via
	 * {@link Activity#startActivityForResult(Intent, int)} for the pre LOLLIPOP Android versions
	 * and for the post LOLLIPOP via {@link Activity#startActivityForResult(Intent, int, Bundle)}.
	 * <p>
	 * <b>Note</b>, that the specified caller activity has already attached transitions to its
	 * window, that has been specified for this navigational transition via one of {@code set...Transition(...)}
	 * methods.
	 *
	 * @param caller The caller activity that requested start of this navigational transition.
	 * @see #onFinish(Activity)
	 */
	@SuppressLint("NewApi")
	@SuppressWarnings("ConstantConditions")
	protected void onStart(@NonNull final Activity caller) {
		final Intent intent = createIntent(caller);
		if (MATERIAL_SUPPORT) {
			final Bundle options = makeSceneTransitionAnimation(caller).toBundle();
			if (mRequestCode == RC_NONE) caller.startActivity(intent, options);
			else caller.startActivityForResult(intent, mRequestCode, options);
		} else {
			if (mRequestCode == RC_NONE) caller.startActivity(intent);
			else caller.startActivityForResult(intent, mRequestCode);
		}
	}

	/**
	 * Like {@link #finishCaller(Activity)} but this will postpone the finishing by the requested
	 * <var>delay</var>.
	 *
	 * @param caller The caller activity to finish.
	 * @param delay  The desired delay after which to finish the caller activity.
	 */
	protected void finishCallerDelayed(@NonNull final Activity caller, @IntRange(from = 0) final long delay) {
		final View decorView = caller.getWindow().getDecorView();
		if (decorView != null) decorView.postDelayed(new Runnable() {

			/**
			 */
			@Override
			public void run() {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && caller.isDestroyed()) {
					return;
				}
				if (caller.isFinishing()) {
					return;
				}
				onFinishCaller(caller);
			}
		}, delay);
	}

	/**
	 * Finishes the specified <var>caller</var> activity either via {@link Activity#finishAfterTransition()}
	 * or via {@link Activity#finish()} depending on the current API capabilities.
	 *
	 * @param caller The caller activity to finish.
	 */
	protected void finishCaller(@NonNull final Activity caller) {
		onFinishCaller(caller);
	}

	/**
	 * Invoked whenever {@link #finishCaller(Activity)} or {@link #finishCallerDelayed(Activity, long)}
	 * is called.
	 * <p>
	 * Default implementation finishes the caller activity either via {@link Activity#finishAfterTransition()}
	 * or via {@link Activity#finish()} depending on the current API capabilities.
	 *
	 * @param caller The caller activity to finish.
	 */
	@SuppressLint("NewApi")
	protected void onFinishCaller(@NonNull final Activity caller) {
		if (MATERIAL_SUPPORT) caller.finishAfterTransition();
		else caller.finish();
	}

	/**
	 * <b>This method has been deprecated and will be removed in the next release.</b>
	 * <p>
	 * Specifies a boolean flags for a window of the specified <var>activity</var> determining whether
	 * an enter or return transition can overlap or not based on the requested values for this
	 * navigational transition.
	 * <p>
	 * <b>Note</b>, that for pre {@link Build.VERSION_CODES#LOLLIPOP LOLLIPOP} Android
	 * versions this method does nothing.
	 *
	 * @param activity The activity for which window to specify whether an enter or return transition
	 *                 can overlap or not.
	 * @see Window#setAllowEnterTransitionOverlap(boolean)
	 * @see Window#setAllowReturnTransitionOverlap(boolean)
	 * @deprecated Use {@link #configureIncomingTransitions(Activity)} instead.
	 */
	@Deprecated
	public void configureTransitionsOverlapping(@NonNull final Activity activity) {
		if (MATERIAL_SUPPORT) {
			final Window window = activity.getWindow();
			if (mAllowEnterTransitionOverlap != null)
				window.setAllowEnterTransitionOverlap(mAllowEnterTransitionOverlap);
			if (mAllowReturnTransitionOverlap != null)
				window.setAllowReturnTransitionOverlap(mAllowReturnTransitionOverlap);
		}
	}

	/**
	 * This method groups calls to {@link #configureIncomingTransitions(Activity)} and
	 * {@link #configureOutgoingTransitions(Activity)} into one call.
	 *
	 * @param activity The activity of which window transitions to configure.
	 */
	public void configureTransitions(@NonNull final Activity activity) {
		configureIncomingTransitions(activity);
		configureOutgoingTransitions(activity);
	}

	/**
	 * Performs configuration of the given <var>activity's</var> window by attaching to it <b>enter</b>
	 * and <b>return</b> transitions (those for shared elements including) specified for this navigational
	 * transition. Also the configuration related to transitions overlapping will be performed here.
	 * <p>
	 * This method should be called from {@link Activity#onCreate(Bundle)} by the activity to which
	 * is the calling activity transitioning. See also {@link #configureOutgoingTransitions(Activity)}.
	 * <p>
	 * <b>Note</b>, that for pre {@link Build.VERSION_CODES#LOLLIPOP LOLLIPOP} Android  versions this
	 * method does nothing.
	 *
	 * @param activity The activity of which window transitions to configure.
	 * @see Window#setEnterTransition(Transition)
	 * @see Window#setReturnTransition(Transition)
	 * @see Window#setSharedElementEnterTransition(Transition)
	 * @see Window#setSharedElementReturnTransition(Transition)
	 * @see Window#setAllowEnterTransitionOverlap(boolean)
	 * @see Window#setAllowReturnTransitionOverlap(boolean)
	 */
	public void configureIncomingTransitions(@NonNull final Activity activity) {
		if (MATERIAL_SUPPORT) {
			final Window window = activity.getWindow();
			if ((mSpecifiedTransitions & TRANSITION_ENTER) != 0) {
				window.setEnterTransition(mEnterTransition);
			}
			if ((mSpecifiedTransitions & TRANSITION_RETURN) != 0) {
				window.setReturnTransition(mReturnTransition);
			}
			if ((mSpecifiedTransitions & TRANSITION_SHARED_ELEMENT_ENTER) != 0) {
				window.setSharedElementEnterTransition(mSharedElementEnterTransition);
			}
			if ((mSpecifiedTransitions & TRANSITION_SHARED_ELEMENT_RETURN) != 0) {
				window.setSharedElementReturnTransition(mSharedElementReturnTransition);
			}
			if (mAllowEnterTransitionOverlap != null) {
				window.setAllowEnterTransitionOverlap(mAllowEnterTransitionOverlap);
			}
			if (mAllowReturnTransitionOverlap != null) {
				window.setAllowReturnTransitionOverlap(mAllowReturnTransitionOverlap);
			}
			if (mSharedElementUseOverlay != null) {
				window.setSharedElementsUseOverlay(mSharedElementUseOverlay);
			}
		}
	}

	/**
	 * Performs configuration of the given <var>activity's</var> window by attaching to it <b>reenter</b>
	 * and <b>exit</b> transitions (those for shared elements including) specified for this navigational
	 * transition.
	 * <p>
	 * This method is by default invoked whenever {@link #start(Activity)} is called for this
	 * navigational transition. Its counterpart, {@link #configureIncomingTransitions(Activity)},
	 * should be called by the activity to which is the calling activity transitioning.
	 * <p>
	 * <b>Note</b>, that for pre {@link Build.VERSION_CODES#LOLLIPOP LOLLIPOP} Android  versions this
	 * method does nothing.
	 *
	 * @param activity The activity of which window transitions to configure.
	 * @see Window#setReenterTransition(Transition)
	 * @see Window#setExitTransition(Transition)
	 * @see Window#setSharedElementReenterTransition(Transition)
	 * @see Window#setSharedElementExitTransition(Transition)
	 */
	public void configureOutgoingTransitions(@NonNull final Activity activity) {
		if (MATERIAL_SUPPORT) {
			final Window window = activity.getWindow();
			if ((mSpecifiedTransitions & TRANSITION_REENTER) != 0) {
				window.setReenterTransition(mReenterTransition);
			}
			if ((mSpecifiedTransitions & TRANSITION_EXIT) != 0) {
				window.setExitTransition(mExitTransition);
			}
			if ((mSpecifiedTransitions & TRANSITION_SHARED_ELEMENT_REENTER) != 0) {
				window.setSharedElementReenterTransition(mSharedElementReenterTransition);
			}
			if ((mSpecifiedTransitions & TRANSITION_SHARED_ELEMENT_EXIT) != 0) {
				window.setSharedElementExitTransition(mSharedElementExitTransition);
			}
			if (mSharedElementUseOverlay != null) {
				window.setSharedElementsUseOverlay(mSharedElementUseOverlay);
			}
		}
	}

	/**
	 * Creates a new instance of ActivityOptions for the specified <var>caller</var> activity.
	 * <p>
	 * <b>Note</b>, that for pre {@link Build.VERSION_CODES#LOLLIPOP LOLLIPOP} Android
	 * versions this method does nothing and returns {@code null}.
	 *
	 * @param caller The activity for which to create the ActivityOptions.
	 * @return Activity options with or without attached shared elements that has been added into
	 * this navigational transition (if any).
	 */
	@Nullable
	@SuppressLint("NewApi")
	@SuppressWarnings("unchecked")
	public ActivityOptions makeSceneTransitionAnimation(@NonNull final Activity caller) {
		if (MATERIAL_SUPPORT) {
			if (mSharedElements != null && !mSharedElements.isEmpty()) {
				final Pair<View, String>[] pairs = new Pair[mSharedElements.size()];
				mSharedElements.toArray(pairs);
				return ActivityOptions.makeSceneTransitionAnimation(caller, pairs);
			}
			return ActivityOptions.makeSceneTransitionAnimation(caller);
		}
		return null;
	}

	/**
	 * Creates an intent that can be used to start activity for which is this transition created.
	 *
	 * @param caller Activity to be used as context when creating the requested intent.
	 * @return New Intent or {@code null} it this navigational transition has no class of transition
	 * activity specified.
	 */
	@NonNull
	public Intent createIntent(@NonNull final Activity caller) {
		if (mClassOfTransitionActivity == null) {
			throw new UnsupportedOperationException(
					"Navigational transition(" + getClass().getSimpleName() + ") does not have any class of intended activity specified."
			);
		}
		final Intent intent = new Intent(caller, mClassOfTransitionActivity);
		if (mIntentExtras != null) {
			intent.putExtras(mIntentExtras);
		}
		return intent;
	}

	/**
	 * Finishes the given <var>caller</var> activity in order to run its exit transitions.
	 *
	 * @param caller The activity that should be finished and of which exit transitions should be started.
	 * @see #start(Activity)
	 */
	public void finish(@NonNull final Activity caller) {
		onFinish(caller);
	}

	/**
	 * Invoked whenever {@link #finish(Activity)} is called.
	 * <p>
	 * Default implementation finishes the given caller activity for the pre LOLLIPOP Android versions
	 * via {@link Activity#finish()} and for the post LOLLIPOP via {@link Activity#finishAfterTransition()}.
	 * <p>
	 * Derived classes can override this method in order to run custom window transitions for the
	 * pre LOLLIPOP Android versions via {@link Activity#overridePendingTransition(int, int)}.
	 *
	 * @param caller The activity that requested its finish via this navigational transition.
	 * @see #onStart(Activity)
	 */
	@SuppressLint("NewApi")
	protected void onFinish(@NonNull final Activity caller) {
		if (MATERIAL_SUPPORT) caller.finishAfterTransition();
		else caller.finish();
	}

	/*
	 * Inner classes ===============================================================================
	 */
}
