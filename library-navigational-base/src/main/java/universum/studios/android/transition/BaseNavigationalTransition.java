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
import android.annotation.TargetApi;
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

	/**
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

	/**
	 * Interface ===================================================================================
	 */

	/**
	 * Static members ==============================================================================
	 */

	/**
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
	 * Inflater that can be used to inflate a new transition or transition manager.
	 */
	private TransitionInflater mTransitionInflater;

	/**
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
	public BaseNavigationalTransition(@NonNull Class<? extends Activity> classOfTransitionActivity) {
		this.mClassOfTransitionActivity = classOfTransitionActivity;
	}

	/**
	 * Methods =====================================================================================
	 */

	/**
	 * Specifies a bundle with extras for the transition activity. The given bundle will be attached
	 * to this transition's {@link Intent} via {@link Intent#putExtras(Bundle)} when it is created
	 * via {@link #createIntent(Activity)}.
	 *
	 * @param extras The desired bundle with extras. May be {@code null} to clear the current one.
	 * @return This transition to allow methods chaining.
	 * @see #intentExtras()
	 */
	@SuppressWarnings("unchecked")
	public T intentExtras(@Nullable Bundle extras) {
		this.mIntentExtras = extras;
		return (T) this;
	}

	/**
	 * Returns the bundle with extras for the transition activity.
	 * <p>
	 * This will be either bundle specified via {@link #intentExtras(Bundle)} or created by default
	 * when this method is called for the first time.
	 *
	 * @return The extras bundle for the transition activity.
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
	 * @see Activity#startActivityForResult(Intent, int)
	 * @see Activity#startActivityForResult(Intent, int, Bundle)
	 * @see Fragment#startActivityForResult(Intent, int)
	 */
	@SuppressWarnings("unchecked")
	public T requestCode(@IntRange(from = RC_NONE) int requestCode) {
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
	 * Specifies a transition to be attached to a window of the caller activity whenever
	 * {@link #start(Activity)} is invoked.
	 *
	 * @return This transition to allow methods chaining.
	 * @see Window#setEnterTransition(Transition)
	 * @see #inflateTransition(Context, int)
	 */
	@SuppressWarnings("unchecked")
	public T enterTransition(@Nullable Transition transition) {
		this.mSpecifiedTransitions |= TRANSITION_ENTER;
		this.mEnterTransition = transition;
		return (T) this;
	}

	/**
	 * @return Transition specified via {@link #enterTransition(Transition)} or {@code null} by
	 * default.
	 */
	@Nullable
	public Transition enterTransition() {
		return mEnterTransition;
	}

	/**
	 * Specifies a transition to be attached to a window of the caller activity whenever
	 * {@link #start(Activity)} is invoked.
	 *
	 * @return This transition to allow methods chaining.
	 * @see Window#setReenterTransition(Transition)
	 * @see #inflateTransition(Context, int)
	 */
	@SuppressWarnings("unchecked")
	public T reenterTransition(@Nullable Transition transition) {
		this.mSpecifiedTransitions |= TRANSITION_REENTER;
		this.mReenterTransition = transition;
		return (T) this;
	}

	/**
	 * @return Transition specified via {@link #reenterTransition(Transition)} or {@code null} by
	 * default.
	 */
	@Nullable
	public Transition reenterTransition() {
		return mReenterTransition;
	}

	/**
	 * Specifies a transition to be attached to a window of the caller activity whenever
	 * {@link #start(Activity)} is invoked.
	 *
	 * @return This transition to allow methods chaining.
	 * @see Window#setReturnTransition(Transition)
	 * @see #inflateTransition(Context, int)
	 */
	@SuppressWarnings("unchecked")
	public T returnTransition(@Nullable Transition transition) {
		this.mSpecifiedTransitions |= TRANSITION_RETURN;
		this.mReturnTransition = transition;
		return (T) this;
	}

	/**
	 * @return Transition specified via {@link #returnTransition(Transition)} or {@code null} by
	 * default.
	 */
	@Nullable
	public Transition returnTransition() {
		return mReturnTransition;
	}

	/**
	 * Specifies a transition to be attached to a window of the caller activity whenever
	 * {@link #start(Activity)} is invoked.
	 *
	 * @return This transition to allow methods chaining.
	 * @see Window#setExitTransition(Transition)
	 * @see #inflateTransition(Context, int)
	 */
	@SuppressWarnings("unchecked")
	public T exitTransition(@Nullable Transition transition) {
		this.mSpecifiedTransitions |= TRANSITION_EXIT;
		this.mExitTransition = transition;
		return (T) this;
	}

	/**
	 * @return Transition specified via {@link #exitTransition(Transition)} or {@code null} by
	 * default.
	 */
	@Nullable
	public Transition exitTransition() {
		return mExitTransition;
	}

	/**
	 * Specifies a boolean flag to be set to a window of the caller activity whenever
	 * {@link #start(Activity)} is invoked.
	 *
	 * @return This transition to allow methods chaining.
	 * @see Window#setAllowEnterTransitionOverlap(boolean)
	 */
	@SuppressWarnings("unchecked")
	public T allowEnterTransitionOverlap(boolean allow) {
		this.mAllowEnterTransitionOverlap = allow;
		return (T) this;
	}

	/**
	 * @return {@code True} if overlapping of enter transition is enabled, {@code false} otherwise.
	 */
	public boolean allowEnterTransitionOverlap() {
		return mAllowEnterTransitionOverlap != null && mAllowEnterTransitionOverlap;
	}

	/**
	 * Specifies a boolean flag to be set to a window of the caller activity whenever
	 * {@link #start(Activity)} is invoked.
	 *
	 * @return This transition to allow methods chaining.
	 * @see Window#setAllowReturnTransitionOverlap(boolean)
	 */
	@SuppressWarnings("unchecked")
	public T allowReturnTransitionOverlap(boolean allow) {
		this.mAllowReturnTransitionOverlap = allow;
		return (T) this;
	}

	/**
	 * @return {@code True} if overlapping of return transition is enabled, {@code false} otherwise.
	 */
	public boolean allowReturnTransitionOverlap() {
		return mAllowReturnTransitionOverlap != null && mAllowReturnTransitionOverlap;
	}

	/**
	 * Specifies a transition for shared element to be attached to a window of the caller activity
	 * whenever {@link #start(Activity)} is invoked.
	 *
	 * @return This transition to allow methods chaining.
	 * @see Window#setSharedElementEnterTransition(Transition)
	 * @see #inflateTransition(Context, int)
	 */
	@SuppressWarnings("unchecked")
	public T sharedElementEnterTransition(@Nullable Transition transition) {
		this.mSpecifiedTransitions |= TRANSITION_SHARED_ELEMENT_ENTER;
		this.mSharedElementEnterTransition = transition;
		return (T) this;
	}

	/**
	 * @return Transition for shared element specified via {@link #sharedElementEnterTransition(Transition)}
	 * or {@code null} by default.
	 */
	@Nullable
	public Transition sharedElementEnterTransition() {
		return mSharedElementEnterTransition;
	}

	/**
	 * Specifies a transition for shared element to be attached to a window of the caller activity
	 * whenever {@link #start(Activity)} is invoked.
	 *
	 * @return This transition to allow methods chaining.
	 * @see Window#setSharedElementReenterTransition(Transition)
	 * @see #inflateTransition(Context, int)
	 */
	@SuppressWarnings("unchecked")
	public T sharedElementReenterTransition(@Nullable Transition transition) {
		this.mSpecifiedTransitions |= TRANSITION_SHARED_ELEMENT_REENTER;
		this.mSharedElementReenterTransition = transition;
		return (T) this;
	}

	/**
	 * @return Transition for shared element specified via {@link #sharedElementReenterTransition(Transition)}
	 * or {@code null} by default.
	 */
	@Nullable
	public Transition sharedElementReenterTransition() {
		return mSharedElementReenterTransition;
	}

	/**
	 * Specifies a transition for shared element to be attached to a window of the caller activity
	 * whenever {@link #start(Activity)} is invoked.
	 *
	 * @return This transition to allow methods chaining.
	 * @see Window#setSharedElementReturnTransition(Transition)
	 * @see #inflateTransition(Context, int)
	 */
	@SuppressWarnings("unchecked")
	public T sharedElementReturnTransition(@Nullable Transition transition) {
		this.mSpecifiedTransitions |= TRANSITION_SHARED_ELEMENT_RETURN;
		this.mSharedElementReturnTransition = transition;
		return (T) this;
	}

	/**
	 * @return Transition for shared element specified via {@link #sharedElementReturnTransition(Transition)}
	 * or {@code null} by default.
	 */
	@Nullable
	public Transition sharedElementReturnTransition() {
		return mSharedElementReturnTransition;
	}

	/**
	 * Specifies a transition for shared element to be attached to a window of the caller activity
	 * whenever {@link #start(Activity)} is invoked.
	 *
	 * @return This transition to allow methods chaining.
	 * @see Window#setSharedElementExitTransition(Transition)
	 * @see #inflateTransition(Context, int)
	 */
	@SuppressWarnings("unchecked")
	public T sharedElementExitTransition(@Nullable Transition transition) {
		this.mSpecifiedTransitions |= TRANSITION_SHARED_ELEMENT_EXIT;
		this.mSharedElementExitTransition = transition;
		return (T) this;
	}

	/**
	 * @return Transition for shared element specified via {@link #sharedElementExitTransition(Transition)}
	 * or {@code null} by default.
	 */
	@Nullable
	public Transition sharedElementExitTransition() {
		return mSharedElementExitTransition;
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
	public Transition inflateTransition(@NonNull Context context, int resource) {
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
	public TransitionManager inflateTransitionManager(@NonNull Context context, int resource, @NonNull ViewGroup sceneRoot) {
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
	public final T sharedElements(@NonNull Pair<View, String>... elements) {
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
	public T sharedElement(@NonNull View element, @NonNull String elementName) {
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
	 * Starts this navigational transition using the given <var>caller</var> activity with all
	 * transitions and shared elements specified for this navigational transition.
	 *
	 * @param caller The activity that will be used to create and start an Intent created via
	 *               {@link #createIntent(Activity)}.
	 * @see #configureTransitionsOverlapping(Activity)
	 * @see #configureTransitions(Activity)
	 */
	public void start(@NonNull Activity caller) {
		configureTransitionsOverlapping(caller);
		configureTransitions(caller);
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
	 * @see #attachTransitions(Window)
	 * @see #attachSharedElementTransitions(Window)
	 * @see #onFinish(Activity)
	 */
	@SuppressLint("NewApi")
	@SuppressWarnings("ConstantConditions")
	protected void onStart(@NonNull Activity caller) {
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
	protected void finishCallerDelayed(@NonNull final Activity caller, @IntRange(from = 0) long delay) {
		final View decorView = caller.getWindow().getDecorView();
		if (decorView != null) decorView.postDelayed(new Runnable() {

			/**
			 */
			@Override
			public void run() {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
					if (!caller.isDestroyed() && !caller.isFinishing()) {
						onFinishCaller(caller);
					}
				} else if (!caller.isFinishing()) {
					onFinishCaller(caller);
				}
			}
		}, delay);
	}

	/**
	 * Finishes the specified <var>caller</var> activity either via {@link Activity#finishAfterTransition()}
	 * or via {@link Activity#finish()} depending on the current API capabilities.
	 *
	 * @param caller The caller activity to finish.
	 */
	protected void finishCaller(@NonNull Activity caller) {
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
	protected void onFinishCaller(@NonNull Activity caller) {
		if (MATERIAL_SUPPORT) caller.finishAfterTransition();
		else caller.finish();
	}

	/**
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
	 */
	@SuppressLint("NewApi")
	public void configureTransitionsOverlapping(@NonNull Activity activity) {
		if (MATERIAL_SUPPORT) {
			final Window window = activity.getWindow();
			if (mAllowEnterTransitionOverlap != null)
				window.setAllowEnterTransitionOverlap(mAllowEnterTransitionOverlap);
			if (mAllowReturnTransitionOverlap != null)
				window.setAllowReturnTransitionOverlap(mAllowReturnTransitionOverlap);
		}
	}

	/**
	 * Attaches all transitions, including those for shared elements, specified for this navigational
	 * transition to a window of the specified <var>activity</var> via one of
	 * {@code Window#set...Transition(...)} methods.
	 * <p>
	 * <b>Note</b>, that for pre {@link Build.VERSION_CODES#LOLLIPOP LOLLIPOP} Android
	 * versions this method does nothing.
	 *
	 * @param activity The activity to which window should be transitions attached.
	 * @see Window#setEnterTransition(Transition)
	 * @see Window#setReturnTransition(Transition)
	 * @see Window#setReenterTransition(Transition)
	 * @see Window#setExitTransition(Transition)
	 * @see Window#setSharedElementEnterTransition(Transition)
	 * @see Window#setSharedElementReturnTransition(Transition)
	 * @see Window#setSharedElementReenterTransition(Transition)
	 * @see Window#setSharedElementExitTransition(Transition)
	 */
	public void configureTransitions(@NonNull Activity activity) {
		if (MATERIAL_SUPPORT) {
			final Window window = activity.getWindow();
			this.attachTransitions(window);
			this.attachSharedElementTransitions(window);
		}
	}

	/**
	 * Attaches all transitions specified for this navigational transition to the given <var>window</var>.
	 * <p>
	 * This will attach only transitions of which particular flags are contained within {@link #mSpecifiedTransitions}
	 * flags.
	 *
	 * @param window The window to which should be transitions attached.
	 */
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	private void attachTransitions(Window window) {
		if ((mSpecifiedTransitions & TRANSITION_ENTER) != 0)
			window.setEnterTransition(mEnterTransition);
		if ((mSpecifiedTransitions & TRANSITION_REENTER) != 0)
			window.setReenterTransition(mReenterTransition);
		if ((mSpecifiedTransitions & TRANSITION_RETURN) != 0)
			window.setReturnTransition(mReturnTransition);
		if ((mSpecifiedTransitions & TRANSITION_EXIT) != 0)
			window.setExitTransition(mExitTransition);
	}

	/**
	 * Attaches all transitions for shared elements specified for this navigational transition to
	 * the given <var>window</var>.
	 * <p>
	 * This will attach only transitions of which particular flags are contained within {@link #mSpecifiedTransitions}
	 * flags.
	 *
	 * @param window The window to which should be transitions attached.
	 */
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	private void attachSharedElementTransitions(Window window) {
		if ((mSpecifiedTransitions & TRANSITION_SHARED_ELEMENT_ENTER) != 0)
			window.setSharedElementEnterTransition(mSharedElementEnterTransition);
		if ((mSpecifiedTransitions & TRANSITION_SHARED_ELEMENT_REENTER) != 0)
			window.setSharedElementReenterTransition(mSharedElementReenterTransition);
		if ((mSpecifiedTransitions & TRANSITION_SHARED_ELEMENT_RETURN) != 0)
			window.setSharedElementReturnTransition(mSharedElementReturnTransition);
		if ((mSpecifiedTransitions & TRANSITION_SHARED_ELEMENT_EXIT) != 0)
			window.setSharedElementExitTransition(mSharedElementExitTransition);

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
	public ActivityOptions makeSceneTransitionAnimation(@NonNull Activity caller) {
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
	public Intent createIntent(@NonNull Activity caller) {
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
	public void finish(@NonNull Activity caller) {
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
	protected void onFinish(@NonNull Activity caller) {
		if (MATERIAL_SUPPORT) caller.finishAfterTransition();
		else caller.finish();
	}

	/**
	 * Inner classes ===============================================================================
	 */
}
