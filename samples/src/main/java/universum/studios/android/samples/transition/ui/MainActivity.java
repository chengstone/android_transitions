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
package universum.studios.android.samples.transition.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import universum.studios.android.fragment.manage.FragmentController;
import universum.studios.android.fragment.transition.FragmentTransitions;
import universum.studios.android.samples.transition.R;
import universum.studios.android.samples.transition.ui.view.ViewTransitionsFragment;
import universum.studios.android.samples.transition.ui.window.WindowTransitionsFragment;
import universum.studios.android.samples.ui.SamplesNavigationActivity;

/**
 * @author Martin Albedinsky
 */
public final class MainActivity extends SamplesNavigationActivity {

	@SuppressWarnings("unused")
	private static final String TAG = "MainActivity";

	private FragmentController fragmentController;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.fragmentController = new FragmentController(this);
		this.fragmentController.setViewContainerId(R.id.samples_container);
		if (savedInstanceState == null) {
			fragmentController.newRequest(new HomeFragment()).execute();
		}
	}

	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item) {
		switch (item.getItemId()) {
			case R.id.navigation_item_home:
				fragmentController.newRequest(new HomeFragment())
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
		}
		return true;
	}
}
