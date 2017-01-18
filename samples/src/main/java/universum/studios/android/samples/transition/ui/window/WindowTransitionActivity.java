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
package universum.studios.android.samples.transition.ui.window;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.TextView;

import butterknife.BindView;
import universum.studios.android.samples.transition.R;
import universum.studios.android.samples.ui.SamplesActivity;
import universum.studios.android.transition.WindowTransition;

/**
 * @author Martin Albedinsky
 */
public final class WindowTransitionActivity extends SamplesActivity {

	@SuppressWarnings("unused")
	private static final String TAG = "WindowTransitionActivity";
	static final String EXTRA_WINDOW_TRANSITION = WindowTransitionActivity.class.getName() + ".EXTRA.WindowTransition";

	@BindView(R.id.text) TextView mTextView;
	private WindowTransition mWindowTransition;

	@NonNull
	static Intent createIntent(@NonNull Context context, @NonNull WindowTransition windowTransition) {
		return new Intent(context, WindowTransitionActivity.class).putExtra(
				EXTRA_WINDOW_TRANSITION,
				windowTransition
		);
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		requestFeature(FEATURE_DEPENDENCIES_INJECTION);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_window_transition);
		this.mWindowTransition = getIntent().getExtras().getParcelable(EXTRA_WINDOW_TRANSITION);
		if (mWindowTransition != null) {
			mTextView.setText(mWindowTransition.getName().replace("_", " "));
		}
	}

	@Override
	public void finish() {
		super.finish();
		if (mWindowTransition != null) mWindowTransition.overrideFinish(this);
	}
}
