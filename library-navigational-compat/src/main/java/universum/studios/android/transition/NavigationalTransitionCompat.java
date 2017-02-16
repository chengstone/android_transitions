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
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

/**
 * A {@link BaseNavigationalTransition} implementation that may be used also from a context of
 * <b>support</b> {@link Fragment}.
 * <p>
 * See {@link #start(Fragment)} and {@link #finish(Fragment)} for more information.
 *
 * @author Martin Albedinsky
 */
public class NavigationalTransitionCompat extends BaseNavigationalTransition<NavigationalTransitionCompat> {

	/**
	 * Interface ===================================================================================
	 */

	/**
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "NavigationalTransition";

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
	 * Creates a new instance of NavigationalTransitionCompat without transition activity.
	 */
	public NavigationalTransitionCompat() {
		super();
	}

	/**
	 * Creates a new instance of NavigationalTransitionCompat with the specified <var>classOfTransitionActivity</var>.
	 *
	 * @param classOfTransitionActivity The activity class that will be used to create an {@link Intent}
	 *                                  whenever starting this transition.
	 * @see #createIntent(Activity)
	 */
	public NavigationalTransitionCompat(@NonNull Class<? extends Activity> classOfTransitionActivity) {
		super(classOfTransitionActivity);
	}

	/**
	 * Methods =====================================================================================
	 */

	/**
	 * Starts this navigational transition using the given <var>caller</var> fragment with all
	 * transitions specified for this navigational transition.
	 * <p>
	 * <b>Note</b>, that unlike {@link ##start(Activity)} this cannot be used to start new activity
	 * with shared elements presented. For that purpose use {@link #start(Activity)} instead.
	 *
	 * @param caller The fragment that will be used to create and start an Intent created via
	 *               {@link #createIntent(Activity)}.
	 * @see #configureTransitionsOverlapping(Activity)
	 * @see #configureTransitions(Activity)
	 */
	public void start(@NonNull Fragment caller) {
		final Activity activity = caller.getActivity();
		configureTransitionsOverlapping(activity);
		configureTransitions(activity);
		onStart(caller);
	}

	/**
	 * Invoked whenever {@link #start(Fragment)} is called.
	 * <p>
	 * Default implementation starts an Intent created via {@link #createIntent(Activity)} using
	 * the given caller fragment via {@link Fragment#startActivity(Intent)} if there was no
	 * {@link #requestCode()} specified and via {@link Fragment#startActivityForResult(Intent, int)}
	 * if there was.
	 *
	 * @param caller The caller fragment that requested start of this navigational transition.
	 * @see #onFinish(Fragment)
	 */
	@SuppressLint("NewApi")
	@SuppressWarnings("ConstantConditions")
	protected void onStart(@NonNull Fragment caller) {
		final Activity activity = caller.getActivity();
		final Intent intent = createIntent(activity);
		if (MATERIAL_SUPPORT) {
			final Bundle options = makeSceneTransitionAnimation(activity).toBundle();
			if (mRequestCode == RC_NONE) caller.startActivity(intent, options);
			else caller.startActivityForResult(intent, mRequestCode, options);
		} else {
			if (mRequestCode == RC_NONE) caller.startActivity(intent);
			else caller.startActivityForResult(intent, mRequestCode);
		}
	}

	/**
	 * Finishes host activity of the given <var>caller</var> fragment in order to run its exit transitions.
	 *
	 * @param caller The fragment of which activity should be finished and of which exit transitions
	 *               should be started.
	 * @see #start(Fragment)
	 */
	public void finish(@NonNull Fragment caller) {
		onFinish(caller);
	}

	/**
	 * Invoked whenever {@link #finish(Fragment)} is called.
	 * <p>
	 * Default implementation invokes {@link #onFinish(Activity)} with a host activity of the given
	 * caller fragment.
	 *
	 * @param caller The fragment that requested finish of its host activity via this navigational transition.
	 * @see #onStart(Fragment)
	 */
	protected void onFinish(@NonNull Fragment caller) {
		onFinish(caller.getActivity());
	}

	/**
	 * Inner classes ===============================================================================
	 */
}
