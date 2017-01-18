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
 * A specific navigational transition implementation can be determined by its transition activity
 * class that can be supplied to the constructor {@link #BaseNavigationalTransition(Class)}. This class
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
 *              // ... change activities with cross-fade animation
 *              WindowTransition.CROSS_FADE.overrideStart(caller);
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
	 * Interface ===================================================================================
	 */

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
	private static final int TRANSITION_REENTER = 0x00000002;

	/**
	 * Flag determining whether a <b>return</b> transition has been specified via
	 * {@link #returnTransition(Transition)} or ot.
	 */
	private static final int TRANSITION_RETURN = 0x00000004;

	/**
	 * Flag determining whether an <b>exit</b> transition has been specified via
	 * {@link #exitTransition(Transition)} or ot.
	 */
	private static final int TRANSITION_EXIT = 0x00000008;

	/**
	 * Flag determining whether a shared element's <b>enter</b> transition has been specified via
	 * {@link #sharedElementEnterTransition(Transition)} or ot.
	 */
	private static final int TRANSITION_SHARED_ELEMENT_ENTER = 0x00080000;

	/**
	 * Flag determining whether a shared element's <b>reenter</b> transition has been specified via
	 * {@link #sharedElementReenterTransition(Transition)} or ot.
	 */
	private static final int TRANSITION_SHARED_ELEMENT_REENTER = 0x00100000;

	/**
	 * Flag determining whether a shared element's <b>return</b> transition has been specified via
	 * {@link #sharedElementReturnTransition(Transition)} or ot.
	 */
	private static final int TRANSITION_SHARED_ELEMENT_RETURN = 0x00200000;

	/**
	 * Flag determining whether a shared element's <b>exit</b> transition has been specified via
	 * {@link #sharedElementExitTransition(Transition)} or ot.
	 */
	private static final int TRANSITION_SHARED_ELEMENT_EXIT = 0x00400000;

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
	 * If set (other than {@link #RC_NONE}) starting of intent specific for this navigational transition
	 * will be performed via {@link Activity#startActivityForResult(Intent, int)}, {@link Activity#startActivityForResult(Intent, int, Bundle)}
	 * or {@link Fragment#startActivityForResult(Intent, int)} depends on the type of caller and
	 * current configuration of this transition.
	 */
	int mRequestCode = RC_NONE;

	/**
	 * Boolean flag indicating whether an <b>enter</b> transition can overlap or not.
	 */
	private Boolean mAllowEnterTransitionOverlap;

	/**
	 * Boolean flag indicating whether an <b>return</b> transition can overlap or not.
	 */
	private Boolean mAllowReturnTransitionOverlap;

	/**
	 * Set of flags determining which transitions has been set to this navigational transition via
	 * one of {@code set...Transition(...)} methods.
	 * <p>
	 * See transition flag specified at the beginning of this class.
	 */
	private int mTransitions;

	/**
	 * Transition that should be attached to caller's activity window if {@link #TRANSITION_ENTER}
	 * flag is contained within {@link #mTransitions} flags.
	 *
	 * @see Window#setEnterTransition(Transition)
	 */
	private Transition mEnterTransition;

	/**
	 * Transition that should be attached to caller's activity window if {@link #TRANSITION_REENTER}
	 * flag is contained within {@link #mTransitions} flags.
	 *
	 * @see Window#setReenterTransition(Transition)
	 */
	private Transition mReenterTransition;

	/**
	 * Transition that should be attached to caller's activity window if {@link #TRANSITION_RETURN}
	 * flag is contained within {@link #mTransitions} flags.
	 *
	 * @see Window#setReturnTransition(Transition)
	 */
	private Transition mReturnTransition;

	/**
	 * Transition that should be attached to caller's activity window if {@link #TRANSITION_EXIT}
	 * flag is contained within {@link #mTransitions} flags.
	 *
	 * @see Window#setExitTransition(Transition)
	 */
	private Transition mExitTransition;

	/**
	 * Transition for shared element that should be attached to caller's activity window if
	 * {@link #TRANSITION_SHARED_ELEMENT_ENTER} flag is contained within {@link #mTransitions} flags.
	 *
	 * @see Window#setSharedElementEnterTransition(Transition)
	 */
	private Transition mSharedElementEnterTransition;

	/**
	 * Transition for shared element that should be attached to caller's activity window if
	 * {@link #TRANSITION_SHARED_ELEMENT_REENTER} flag is contained within {@link #mTransitions} flags.
	 *
	 * @see Window#setSharedElementReenterTransition(Transition)
	 */
	private Transition mSharedElementReenterTransition;

	/**
	 * Transition for shared element that should be attached to caller's activity window if
	 * {@link #TRANSITION_SHARED_ELEMENT_RETURN} flag is contained within {@link #mTransitions} flags.
	 *
	 * @see Window#setSharedElementReturnTransition(Transition)
	 */
	private Transition mSharedElementReturnTransition;

	/**
	 * Transition for shared element that should be attached to caller's activity window if
	 * {@link #TRANSITION_SHARED_ELEMENT_EXIT} flag is contained within {@link #mTransitions} flags.
	 *
	 * @see Window#setSharedElementExitTransition(Transition)
	 */
	private Transition mSharedElementExitTransition;

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
	 *                                  that should be started whenever {@link #start(Activity)} is
	 *                                  called upon this new navigational transition.
	 */
	public BaseNavigationalTransition(@NonNull Class<? extends Activity> classOfTransitionActivity) {
		this.mClassOfTransitionActivity = classOfTransitionActivity;
	}

	/**
	 * Methods =====================================================================================
	 */

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
	public T requestCode(@IntRange(from = -1) int requestCode) {
		this.mRequestCode = requestCode;
		return (T) this;
	}

	/**
	 * Returns the request code specified via {@link #requestCode(int)}.
	 *
	 * @return Specified request code or {@link #RC_NONE} by default.
	 */
	@IntRange(from = -1)
	public int requestCode() {
		return mRequestCode;
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
	 * Specifies a transition to be attached to a window of the caller activity whenever
	 * {@link #start(Activity)} is invoked.
	 *
	 * @return This transition to allow methods chaining.
	 * @see Window#setEnterTransition(Transition)
	 */
	@SuppressWarnings("unchecked")
	public T enterTransition(@Nullable Transition transition) {
		this.setHasTransition(TRANSITION_ENTER, true);
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
	 */
	@SuppressWarnings("unchecked")
	public T reenterTransition(@Nullable Transition transition) {
		this.setHasTransition(TRANSITION_REENTER, true);
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
	 */
	@SuppressWarnings("unchecked")
	public T returnTransition(@Nullable Transition transition) {
		this.setHasTransition(TRANSITION_RETURN, true);
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
	 */
	@SuppressWarnings("unchecked")
	public T exitTransition(@Nullable Transition transition) {
		this.setHasTransition(TRANSITION_EXIT, true);
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
	 * Specifies a transition for shared element to be attached to a window of the caller activity
	 * whenever {@link #start(Activity)} is invoked.
	 *
	 * @return This transition to allow methods chaining.
	 * @see Window#setSharedElementEnterTransition(Transition)
	 */
	@SuppressWarnings("unchecked")
	public T sharedElementEnterTransition(@Nullable Transition transition) {
		this.setHasTransition(TRANSITION_SHARED_ELEMENT_ENTER, true);
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
	 */
	@SuppressWarnings("unchecked")
	public T sharedElementReenterTransition(@Nullable Transition transition) {
		this.setHasTransition(TRANSITION_SHARED_ELEMENT_REENTER, true);
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
	 */
	@SuppressWarnings("unchecked")
	public T sharedElementReturnTransition(@Nullable Transition transition) {
		this.setHasTransition(TRANSITION_SHARED_ELEMENT_RETURN, true);
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
	 */
	@SuppressWarnings("unchecked")
	public T sharedElementExitTransition(@Nullable Transition transition) {
		this.setHasTransition(TRANSITION_SHARED_ELEMENT_EXIT, true);
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
	 * @param resource Resource id of the desired transition to inflate.
	 * @param context  Context used to inflated the desired transition.
	 * @return Inflated transition.
	 */
	@Nullable
	@SuppressLint("NewApi")
	public Transition inflateTransition(int resource, @NonNull Context context) {
		if (!MATERIAL_SUPPORT) return null;
		this.ensureTransitionInflater(context);
		return mTransitionInflater.inflateTransition(resource);
	}

	/**
	 * Inflates a new instance of TransitionManager from the specified <var>resource</var>.
	 * <p>
	 * <b>Note</b>, that for pre {@link Build.VERSION_CODES#LOLLIPOP LOLLIPOP} Android
	 * versions this method does nothing and returns {@code null}.
	 *
	 * @param resource  Resource id of the desired transition manager to inflater.
	 * @param sceneRoot Root view for the scene with which will the manager operate.
	 * @return Inflated transition manager.
	 */
	@Nullable
	@SuppressLint("NewApi")
	public TransitionManager inflateTransitionManager(int resource, @NonNull ViewGroup sceneRoot) {
		if (!MATERIAL_SUPPORT) return null;
		this.ensureTransitionInflater(sceneRoot.getContext());
		return mTransitionInflater.inflateTransitionManager(resource, sceneRoot);
	}

	/**
	 * Ensures that the transition inflater is initialized.
	 *
	 * @param context Context used for initialization of the inflater if necessary.
	 */
	@SuppressLint("NewApi")
	private void ensureTransitionInflater(Context context) {
		if (mTransitionInflater == null) mTransitionInflater = TransitionInflater.from(context);
	}

	/**
	 * Specifies a flag determining whether a specific transition has been set to this navigational
	 * transition.
	 *
	 * @param transition Flag of the desired transition to add/remove to/from the current transitions.
	 * @param has        Boolean flag indicating whether to add or remove the specified <var>transition</var>
	 *                   flag.
	 */
	@SuppressWarnings("unused")
	private void setHasTransition(int transition, boolean has) {
		if (has) this.mTransitions |= transition;
		else this.mTransitions &= ~transition;
	}

	/**
	 * Bulk operation for {@link #addSharedElement(View, String)}.
	 *
	 * @param sharedElements Set of the shared elements and their names to be transferred to the
	 *                       called activity.
	 * @return This transition to allow methods chaining.
	 */
	@SafeVarargs
	@SuppressWarnings("unchecked")
	public final T addSharedElements(@NonNull Pair<View, String>... sharedElements) {
		this.ensureSharedElements();
		mSharedElements.addAll(Arrays.asList(sharedElements));
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
	 * @param sharedElement     The desired shared element view.
	 * @param sharedElementName Name of the specified shared element.
	 * @return This transition to allow methods chaining.
	 * @see #addSharedElements(Pair[])
	 * @see #makeSceneTransitionAnimation(Activity)
	 */
	@SuppressWarnings("unchecked")
	public T addSharedElement(@NonNull View sharedElement, @NonNull String sharedElementName) {
		this.ensureSharedElements();
		mSharedElements.add(new Pair<>(sharedElement, sharedElementName));
		return (T) this;
	}

	/**
	 * Ensures that the list with shared elements is initialized.
	 */
	private void ensureSharedElements() {
		if (mSharedElements == null) this.mSharedElements = new ArrayList<>();
	}

	/**
	 * Returns a list of all shared elements specified for this navigational transition.
	 *
	 * @return List of shared elements or {@code null} if there are no shared elements specified.
	 * @see #singleSharedElement()
	 */
	@Nullable
	public List<Pair<View, String>> sharedElements() {
		return mSharedElements;
	}

	/**
	 * Returns a single shared element from the {@code 0} position among the current shared elements.
	 * <p>
	 * Use this method to obtain single shared element when you specify only one for this navigational
	 * transition.
	 *
	 * @return Single shared element or {@code null} if there are no shared elements specified.
	 * @see #sharedElements()
	 */
	@Nullable
	public Pair<View, String> singleSharedElement() {
		return mSharedElements != null && !mSharedElements.isEmpty() ? mSharedElements.get(0) : null;
	}

	/**
	 * Clears all shared elements specified for this navigational transition.
	 *
	 * @return This transition to allow methods chaining.
	 * @see #sharedElements()
	 */
	@SuppressWarnings("unchecked")
	public T clearSharedElements() {
		if (mSharedElements != null) mSharedElements.clear();
		return (T) this;
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
	 * @param delay The desired delay after which to finish the caller activity.
	 */
	protected void finishCallerDelayed(@NonNull final Activity caller, long delay) {
		final View decorView = caller.getWindow().getDecorView();
		if (decorView != null) decorView.postDelayed(new Runnable() {
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
		if (!MATERIAL_SUPPORT) return;
		final Window window = activity.getWindow();
		if (mAllowEnterTransitionOverlap != null)
			window.setAllowEnterTransitionOverlap(mAllowEnterTransitionOverlap);
		if (mAllowReturnTransitionOverlap != null)
			window.setAllowReturnTransitionOverlap(mAllowReturnTransitionOverlap);
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
		if (!MATERIAL_SUPPORT) return;
		final Window window = activity.getWindow();
		this.attachTransitions(window);
		this.attachSharedElementTransitions(window);
	}

	/**
	 * Attaches all transitions specified for this navigational transition to the given <var>window</var>.
	 * <p>
	 * This will attach only transitions of which particular flags are contained within {@link #mTransitions}
	 * flags.
	 *
	 * @param window The window to which should be transitions attached.
	 */
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	private void attachTransitions(Window window) {
		if ((mTransitions & TRANSITION_ENTER) != 0)
			window.setEnterTransition(mEnterTransition);
		if ((mTransitions & TRANSITION_REENTER) != 0)
			window.setReenterTransition(mReenterTransition);
		if ((mTransitions & TRANSITION_RETURN) != 0)
			window.setReturnTransition(mReturnTransition);
		if ((mTransitions & TRANSITION_EXIT) != 0)
			window.setExitTransition(mExitTransition);
	}

	/**
	 * Attaches all transitions for shared elements specified for this navigational transition to
	 * the given <var>window</var>.
	 * <p>
	 * This will attach only transitions of which particular flags are contained within {@link #mTransitions}
	 * flags.
	 *
	 * @param window The window to which should be transitions attached.
	 */
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	private void attachSharedElementTransitions(Window window) {
		if ((mTransitions & TRANSITION_SHARED_ELEMENT_ENTER) != 0)
			window.setSharedElementEnterTransition(mSharedElementEnterTransition);
		if ((mTransitions & TRANSITION_SHARED_ELEMENT_REENTER) != 0)
			window.setSharedElementReenterTransition(mSharedElementReenterTransition);
		if ((mTransitions & TRANSITION_SHARED_ELEMENT_RETURN) != 0)
			window.setSharedElementReturnTransition(mSharedElementReturnTransition);
		if ((mTransitions & TRANSITION_SHARED_ELEMENT_EXIT) != 0)
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
		if (!MATERIAL_SUPPORT) return null;
		if (mSharedElements != null && !mSharedElements.isEmpty()) {
			final Pair<View, String>[] pairs = new Pair[mSharedElements.size()];
			mSharedElements.toArray(pairs);
			return ActivityOptions.makeSceneTransitionAnimation(caller, pairs);
		}
		return ActivityOptions.makeSceneTransitionAnimation(caller);
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
					"Navigational transition(" + getClass().getSimpleName() + ") does not have any class of intended activity attached."
			);
		}
		return new Intent(caller, mClassOfTransitionActivity);
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
