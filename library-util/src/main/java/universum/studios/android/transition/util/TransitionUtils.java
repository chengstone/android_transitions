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
package universum.studios.android.transition.util;

import android.support.annotation.NonNull;
import android.view.View;

/**
 * Class providing utility methods which may be useful in transitions context.
 *
 * @author Martin Albedinsky
 * @since 1.1
 */
public final class TransitionUtils {

	/**
	 */
	private TransitionUtils() {
		// Not allowed to be instantiated publicly.
		throw new UnsupportedOperationException();
	}

	/**
	 * Checks whether the given <var>view</var> is attached to window or not.
	 *
	 * @param view The desired view to be checked.
	 * @return {@code True} if view is attached to window at this time, {@code false} otherwise.
	 */
	public static boolean isViewAttachedToWindow(@NonNull final View view) {
		return view.getWindowToken() != null;
	}
}