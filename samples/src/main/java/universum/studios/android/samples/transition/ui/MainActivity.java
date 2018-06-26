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
package universum.studios.android.samples.transition.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import universum.studios.android.support.fragment.manage.FragmentController;
import universum.studios.android.support.fragment.transition.FragmentTransitions;
import universum.studios.android.samples.transition.R;
import universum.studios.android.samples.transition.ui.view.ViewTransitionsFragment;
import universum.studios.android.samples.transition.ui.window.WindowTransitionsFragment;
import universum.studios.android.support.samples.ui.SamplesMainFragment;
import universum.studios.android.support.samples.ui.SamplesNavigationActivity;

/**
 * @author Martin Albedinsky
 */
public final class MainActivity extends SamplesNavigationActivity {

	private FragmentController fragmentController;

	@Override protected void onCreate(@Nullable final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.fragmentController = new FragmentController(this);
		this.fragmentController.setViewContainerId(R.id.samples_container);
	}

	@Override protected boolean onHandleNavigationItemSelected(@NonNull final MenuItem item) {
		switch (item.getItemId()) {
			case R.id.navigation_item_home:
				fragmentController.newRequest(new SamplesMainFragment())
						.transition(FragmentTransitions.CROSS_FADE)
						.replaceSame(true)
						.execute();
				return true;
			case R.id.navigation_item_window_transitions:
				fragmentController.newRequest(new WindowTransitionsFragment())
						.transition(FragmentTransitions.CROSS_FADE)
						.replaceSame(true)
						.execute();
				return true;
			case R.id.navigation_item_view_transitions:
				fragmentController.newRequest(new ViewTransitionsFragment())
						.transition(FragmentTransitions.CROSS_FADE)
						.replaceSame(true)
						.execute();
				return true;
			default:
				return super.onHandleNavigationItemSelected(item);
		}
	}
}