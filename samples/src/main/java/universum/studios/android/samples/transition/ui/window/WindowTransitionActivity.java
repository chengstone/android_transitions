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
package universum.studios.android.samples.transition.ui.window;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.TextView;

import butterknife.BindView;
import universum.studios.android.samples.transition.R;
import universum.studios.android.support.samples.ui.SamplesActivity;
import universum.studios.android.transition.WindowTransition;

/**
 * @author Martin Albedinsky
 */
public final class WindowTransitionActivity extends SamplesActivity {

	static final String EXTRA_WINDOW_TRANSITION = WindowTransitionActivity.class.getName() + ".EXTRA.WindowTransition";

	@BindView(R.id.note) TextView mTextView;
	private WindowTransition mWindowTransition;

	@NonNull static Intent createIntent(@NonNull final Context context, @NonNull final WindowTransition windowTransition) {
		return new Intent(context, WindowTransitionActivity.class).putExtra(
				EXTRA_WINDOW_TRANSITION,
				windowTransition
		);
	}

	@Override protected void onCreate(@Nullable final Bundle savedInstanceState) {
		requestFeature(FEATURE_DEPENDENCIES_INJECTION | FEATURE_TOOLBAR);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_window_transition);
		this.mWindowTransition = getIntent().getExtras().getParcelable(EXTRA_WINDOW_TRANSITION);
		if (mWindowTransition != null) {
			mTextView.setText(mWindowTransition.getName().replace("_", " "));
		}
	}

	@Override public void finish() {
		super.finish();
		if (mWindowTransition != null) mWindowTransition.overrideFinish(this);
	}
}