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
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

/**
 * <b>This class has been deprecated and will be removed in the next release.</b>
 * <p>
 * A {@link BaseNavigationalTransition} implementation that may be used also from a context of
 * <b>support</b> {@link Fragment}.
 * <p>
 * See {@link #start(Fragment)} and {@link #finish(Fragment)} for more information.
 *
 * @author Martin Albedinsky
 * @since 1.0
 *
 * @deprecated Use {@link NavigationalCompatTransition} instead.
 */
@Deprecated
public class NavigationalTransitionCompat extends NavigationalCompatTransition {

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
	 *
	 * @see #createIntent(Activity)
	 */
	public NavigationalTransitionCompat(@NonNull final Class<? extends Activity> classOfTransitionActivity) {
		super(classOfTransitionActivity);
	}
}