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

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;

import universum.studios.android.samples.transition.R;
import universum.studios.android.support.samples.ui.SamplesActivity;

/**
 * @author Martin Albedinsky
 */
public final class RevealTransitionActivityB extends SamplesActivity {

	@SuppressWarnings("unused")
	private static final String TAG = "RevealTransitionActivityB";

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reveal_b);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			postponeEnterTransition();
			getWindow().getDecorView().postDelayed(new Runnable() {

				@Override
				@TargetApi(Build.VERSION_CODES.LOLLIPOP)
				public void run() {
					if (!isFinishing()) startPostponedEnterTransition();
				}
			}, 700);
		}
	}
}