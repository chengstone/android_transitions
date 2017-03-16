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
package universum.studios.android.samples.transition.ui.view;

import android.app.Activity;
import android.support.annotation.NonNull;

import universum.studios.android.transition.NavigationalTransition;
import universum.studios.android.transition.Reveal;
import universum.studios.android.transition.Scale;
import universum.studios.android.transition.WindowTransitions;

/**
 * @author Martin Albedinsky
 */
final class ViewTransitions {

	@SuppressWarnings("unused")
	private static final String TAG = "ViewTransitions";

	@NonNull
	static ViewTransition reveal() {
		return new ViewTransition(Reveal.class.getSimpleName()) {

			@Override
			void start(@NonNull Activity activity) {
				navigationalTransition(RevealTransitionActivityA.class).start(activity);
			}
		};
	}

	@NonNull
	static ViewTransition scale() {
		return new ViewTransition(Scale.class.getSimpleName()) {

			@Override
			void start(@NonNull Activity activity) {
				navigationalTransition(ScaleTransitionActivityA.class).start(activity);
			}
		};
	}

	@NonNull
	static NavigationalTransition navigationalTransition(@NonNull Class<? extends Activity> classOfTransitionActivity) {
		return new NavTransition(classOfTransitionActivity);
	}

	private static final class NavTransition extends NavigationalTransition {

		NavTransition(@NonNull Class<? extends Activity> classOfTransitionActivity) {
			super(classOfTransitionActivity);
		}

		@Override
		protected void onStart(@NonNull Activity caller) {
			caller.startActivity(createIntent(caller));
			WindowTransitions.SLIDE_TO_LEFT_AND_SCALE_OUT.overrideStart(caller);
		}

		@Override
		protected void onFinish(@NonNull Activity caller) {
			caller.finish();
			WindowTransitions.SLIDE_TO_LEFT_AND_SCALE_OUT.overrideFinish(caller);
		}
	}
}