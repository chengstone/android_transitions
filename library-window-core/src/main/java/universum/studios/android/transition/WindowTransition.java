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

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.AnimRes;
import android.support.annotation.NonNull;

/**
 * WindowTransition provides a foursome of animation resources that may be used to override default
 * activity window transitions via {@link #overrideStart(Activity)} and {@link #overrideFinish(Activity)}.
 *
 * <h3>Intended usage</h3>
 * <pre>
 * // Activity A implementation.
 * public class ActivityA extends Activity {
 *
 *     public void startActivityB() {
 *         startActivity(new Intent(this, ActivityB.class));
 *         WindowTransition.overrideStart(this);
 *     }
 * }
 *
 * // Activity B implementation.
 * public class ActivityB extends Activity {
 *
 *     &#64;Override
 *     public void finish() {
 *         super.finish();
 *         WindowTransition.overrideFinish(this);
 *     }
 * }
 * </pre>
 *
 * @author Martin Albedinsky
 */
public interface WindowTransition extends Parcelable {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Constant used to identify no animation resource provided by window transition.
	 */
	int NO_ANIMATION = 0;

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Returns the animation resource for a new entering/starting window used in {@link #overrideStart(Activity)}
	 * as <var>enter</var> animation.
	 *
	 * @return Animation resource or {@link #NO_ANIMATION} if no animation should be played for
	 * entering activity window.
	 */
	@AnimRes
	int getStartEnterAnimation();

	/**
	 * Returns the animation resource for the current exiting/pausing window used in {@link #overrideStart(Activity)}
	 * as <var>exit</var> animation.
	 *
	 * @return Animation resource or {@link #NO_ANIMATION} if no animation should be played for
	 * exiting activity window.
	 */
	@AnimRes
	int getStartExitAnimation();

	/**
	 * Returns the animation resource for an old entering/resuming window used in {@link #overrideFinish(Activity)}
	 * as <var>enter</var> animation.
	 *
	 * @return Animation resource or {@link #NO_ANIMATION} if no animation should be played for
	 * entering back-stacked activity window.
	 */
	@AnimRes
	int getFinishEnterAnimation();

	/**
	 * Returns the animation resource for the current exiting/finishing window used in {@link #overrideFinish(Activity)}
	 * as <var>exit</var> animation.
	 *
	 * @return Animation resource or {@link #NO_ANIMATION} if no animation should be played for
	 * exiting back-stacked activity window.
	 */
	@AnimRes
	int getFinishExitAnimation();

	/**
	 * Returns the name of this window transition.
	 *
	 * @return Name of this transition.
	 */
	@NonNull
	String getName();

	/**
	 * Overrides pending transitions of the specified <var>activity</var> using {@link #getStartEnterAnimation()}
	 * and {@link #getStartExitAnimation()} animations specified for this window transition.
	 * <p>
	 * This method may be called to override default start animations when starting a new activity.
	 * The specified activity can be either a caller activity that starts a new activity via {@link Intent}
	 * or the started activity. In the first case this should be called immediately after
	 * {@link Activity#startActivity(Intent)} has been called. In the second case this method should
	 * be called from within {@link Activity#onStart()} of the started activity.
	 *
	 * @param activity The caller or starting activity of which pending transition to override.
	 * @see #overrideFinish(Activity)
	 */
	void overrideStart(@NonNull Activity activity);

	/**
	 * Overrides pending transitions of the specified <var>activity</var> using {@link #getFinishEnterAnimation()}
	 * and {@link #getFinishExitAnimation()} animations specified for this window transition.
	 * <p>
	 * This method may be called to override default finish animations when finishing currently visible
	 * activity from within {@link Activity#finish()} method.
	 *
	 * @param activity The finishing activity of which pending transition to override.
	 * @see #overrideStart(Activity)
	 */
	void overrideFinish(@NonNull Activity activity);
}
