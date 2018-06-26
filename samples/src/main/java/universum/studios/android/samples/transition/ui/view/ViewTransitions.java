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
package universum.studios.android.samples.transition.ui.view;

import android.app.Activity;
import android.support.annotation.NonNull;

import universum.studios.android.samples.transition.ui.view.reveal.RevealTransitionActivityA;
import universum.studios.android.samples.transition.ui.view.scale.ScaleTransitionActivityA;
import universum.studios.android.samples.transition.ui.view.translate.TranslateTransitionActivityA;
import universum.studios.android.transition.BaseNavigationalTransition;
import universum.studios.android.transition.NavigationalTransitionCompat;
import universum.studios.android.transition.Reveal;
import universum.studios.android.transition.Scale;
import universum.studios.android.transition.Translate;
import universum.studios.android.transition.WindowTransitions;

/**
 * @author Martin Albedinsky
 */
public final class ViewTransitions {

	@NonNull static ViewTransition reveal() {
		return new ViewTransition(Reveal.class.getSimpleName()) {

			@Override void start(@NonNull final Activity activity) {
				navigationalTransition(RevealTransitionActivityA.class).start(activity);
			}
		};
	}

	@NonNull static ViewTransition scale() {
		return new ViewTransition(Scale.class.getSimpleName()) {

			@Override void start(@NonNull final Activity activity) {
				navigationalTransition(ScaleTransitionActivityA.class).start(activity);
			}
		};
	}

	@NonNull static ViewTransition translate() {
		return new ViewTransition(Translate.class.getSimpleName()) {

			@Override void start(@NonNull final Activity activity) {
				navigationalTransition(TranslateTransitionActivityA.class).start(activity);
			}
		};
	}

	@NonNull public static BaseNavigationalTransition navigationalTransition(@NonNull final Class<? extends Activity> classOfTransitionActivity) {
		return new TransitionImpl(classOfTransitionActivity);
	}

	private static final class TransitionImpl extends NavigationalTransitionCompat {

		TransitionImpl(@NonNull final Class<? extends Activity> classOfTransitionActivity) {
			super(classOfTransitionActivity);
		}

		@Override protected void onStart(@NonNull final Activity caller) {
			caller.startActivity(createIntent(caller));
			WindowTransitions.SLIDE_TO_LEFT_AND_SCALE_OUT.overrideStart(caller);
		}

		@Override protected void onFinish(@NonNull final Activity caller) {
			caller.finish();
			WindowTransitions.SLIDE_TO_LEFT_AND_SCALE_OUT.overrideFinish(caller);
		}
	}
}