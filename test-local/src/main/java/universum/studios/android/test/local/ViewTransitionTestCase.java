/*
 * *************************************************************************************************
 *                                 Copyright 2018 Universum Studios
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
package universum.studios.android.test.local;

import android.view.View;

import org.robolectric.Robolectric;

import androidx.annotation.NonNull;
import androidx.test.core.app.ApplicationProvider;

/**
 * A {@link RobolectricTestCase} implementation used as test case for view based transitions.
 *
 * @author Martin Albedinsky
 */
public abstract class ViewTransitionTestCase extends RobolectricTestCase {

	/**
	 * Creates a view that is attached to a window.
	 *
	 * @return View attached to window ready to be used.
	 *
	 * @see #createViewNotAttachedToWindow()
	 */
	@NonNull protected static View createViewAttachedToWindow() {
		final TestActivity activity = Robolectric.setupActivity(TestActivity.class);
		final View view = new View(activity);
		activity.setContentView(view);
		return view;
	}

	/**
	 * Creates a view instance that is not attached to window.
	 *
	 * @return View not attached to window ready to be used.
	 *
	 * @see #createViewAttachedToWindow()
	 */
	@NonNull protected static View createViewNotAttachedToWindow() {
		return new View(ApplicationProvider.getApplicationContext());
	}
}