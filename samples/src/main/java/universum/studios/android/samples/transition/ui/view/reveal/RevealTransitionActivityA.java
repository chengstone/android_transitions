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
package universum.studios.android.samples.transition.ui.view.reveal;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;
import universum.studios.android.samples.transition.R;
import universum.studios.android.samples.transition.ui.view.ViewTransitions;
import universum.studios.android.samples.ui.SamplesActivity;
import universum.studios.android.transition.NavigationalCompatTransition;

/**
 * @author Martin Albedinsky
 */
public final class RevealTransitionActivityA extends SamplesActivity {

	@BindView(R.id.reveal_overlay) View revealOverlayView;

	@Override protected void onCreate(@Nullable final Bundle savedInstanceState) {
		requestFeature(FEATURE_DEPENDENCIES_INJECTION);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reveal_a);
	}

	@SuppressWarnings("unused")
	@OnClick({R.id.fab}) void onFabClick(@NonNull final View fab) {
		revealOverlayView.setVisibility(View.VISIBLE);
		new NavigationalCompatTransition(RevealTransitionActivityB.class).start(this);
	}

	@Override public void onBackPressed() {
		ViewTransitions.navigationalTransition(getClass()).finish(this);
	}
}