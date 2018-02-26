/*
 * =================================================================================================
 *                             Copyright (C) 2018 Universum Studios
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
package universum.studios.android.test.local;

import android.support.annotation.NonNull;
import android.view.View;

import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;

/**
 * A {@link RobolectricTestCase} implementation used as test case for view based transitions.
 *
 * @author Martin Albedinsky
 */
public abstract class ViewTransitionTestCase extends RobolectricTestCase {

	/**
	 * Log TAG.
	 */
	@SuppressWarnings("unused")
	private static final String TAG = "ViewTransitionTestCase";

	/**
	 * Creates a view that is attached to a window.
	 *
	 * @return View attached to window ready to be used.
	 * @see #createViewNotAttachedToWindow()
	 */
	@NonNull
	protected static View createViewAttachedToWindow() {
		final TestActivity activity = Robolectric.setupActivity(TestActivity.class);
		final View view = new View(activity);
		activity.setContentView(view);
		return view;
	}

	/**
	 * Creates a view instance that is not attached to window.
	 *
	 * @return View not attached to window ready to be used.
	 * @see #createViewAttachedToWindow()
	 */
	@NonNull
	protected static View createViewNotAttachedToWindow() {
		return new View(RuntimeEnvironment.application);
	}
}
