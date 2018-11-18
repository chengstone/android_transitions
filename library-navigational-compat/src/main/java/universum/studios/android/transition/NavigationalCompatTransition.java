/*
 * *************************************************************************************************
 *                                 Copyright 2017 Universum Studios
 * *************************************************************************************************
 *                  Licensed under the Apache License, Version 2.0 (the "License")
 * -------------------------------------------------------------------------------------------------
 * You may not use this file except in compliance with the License. You may obtain a copy of the
 * License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied.
 *
 * See the License for the specific language governing permissions and limitations under the License.
 * *************************************************************************************************
 */
package universum.studios.android.transition;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * <b>This class has been deprecated and will be removed in the next release.</b>
 * <p>
 * A {@link BaseNavigationalTransition} implementation that may be used also from a context of
 * <b>support</b> {@link Fragment}.
 * <p>
 * See {@link #start(Fragment)} and {@link #finish(Fragment)} for more information.
 *
 * @author Martin Albedinsky
 * @since 1.2
 */
public class NavigationalCompatTransition extends BaseNavigationalTransition<NavigationalCompatTransition> {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "NavigationalTransition";

	/*
	 * Interface ===================================================================================
	 */

	/*
	 * Static members ==============================================================================
	 */

	/*
	 * Members =====================================================================================
	 */

	/*
	 * Constructors ================================================================================
	 */

	/**
	 * Creates a new instance of NavigationalTransitionCompat without transition activity.
	 */
	public NavigationalCompatTransition() {
		super();
	}

	/**
	 * Creates a new instance of NavigationalTransitionCompat with the specified <var>classOfTransitionActivity</var>.
	 *
	 * @param classOfTransitionActivity The activity class that will be used to create an {@link Intent}
	 *                                  whenever starting this transition.
	 *
	 * @see #createIntent(Activity)
	 */
	public NavigationalCompatTransition(@NonNull final Class<? extends Activity> classOfTransitionActivity) {
		super(classOfTransitionActivity);
	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Starts this navigational transition using the given <var>caller</var> fragment with transitions
	 * and shared elements that are configured for the caller's parent activity via
	 * {@link #configureOutgoingTransitions(Activity)}.
	 * <p>
	 * <b>Note</b> that unlike {@link #start(Activity)} this cannot be used to start new activity
	 * with shared elements presented. For that purpose use {@link #start(Activity)} instead.
	 *
	 * @param caller The fragment that will be used to create and start an Intent created via
	 *               {@link #createIntent(Activity)}.
	 *
	 * @see #configureTransitions(Activity)
	 */
	public void start(@NonNull final Fragment caller) {
		configureOutgoingTransitions(caller.requireActivity());
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
	 *
	 * @see #onFinish(Fragment)
	 */
	@SuppressLint("NewApi")
	@SuppressWarnings("ConstantConditions")
	protected void onStart(@NonNull final Fragment caller) {
		final Activity activity = caller.requireActivity();
		final Intent intent = createIntent(activity);
		if (MATERIAL_SUPPORT) {
			final Bundle options = makeSceneTransitionAnimation(activity).toBundle();
			if (requestCode == RC_NONE) caller.startActivity(intent, options);
			else caller.startActivityForResult(intent, requestCode, options);
		} else {
			if (requestCode == RC_NONE) caller.startActivity(intent);
			else caller.startActivityForResult(intent, requestCode);
		}
	}

	/**
	 * Finishes host activity of the given <var>caller</var> fragment in order to run its exit transitions.
	 *
	 * @param caller The fragment of which activity should be finished and of which exit transitions
	 *               should be started.
	 *
	 * @see #start(Fragment)
	 */
	public void finish(@NonNull final Fragment caller) {
		onFinish(caller);
	}

	/**
	 * Invoked whenever {@link #finish(Fragment)} is called.
	 * <p>
	 * Default implementation invokes {@link #onFinish(Activity)} with a host activity of the given
	 * caller fragment.
	 *
	 * @param caller The fragment that requested finish of its host activity via this navigational transition.
	 *
	 * @see #onStart(Fragment)
	 */
	protected void onFinish(@NonNull final Fragment caller) {
		onFinish(caller.requireActivity());
	}

	/*
	 * Inner classes ===============================================================================
	 */
}