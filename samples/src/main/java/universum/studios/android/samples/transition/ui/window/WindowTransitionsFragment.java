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

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import universum.studios.android.samples.transition.R;
import universum.studios.android.support.samples.ui.SamplesFragment;
import universum.studios.android.transition.WindowTransition;
import universum.studios.android.widget.adapter.OnDataSetActionListener;

/**
 * @author Martin Albedinsky
 */
public final class WindowTransitionsFragment extends SamplesFragment implements OnDataSetActionListener {

	private WindowTransitionsAdapter adapter;

	@Override @Nullable public View onCreateView(
			@NonNull final LayoutInflater inflater,
			@Nullable final ViewGroup container,
			@Nullable final Bundle savedInstanceState
	) {
		return inflater.inflate(R.layout.fragment_recycler, container, false);
	}

	@Override public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		final RecyclerView recyclerView = view.findViewById(android.R.id.list);
		recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
		this.adapter = new WindowTransitionsAdapter(requireActivity());
		this.adapter.registerOnDataSetActionListener(this);
		recyclerView.setAdapter(adapter);
	}

	@Override public boolean onDataSetActionSelected(final int action, final int position, final long id, @Nullable final Object data) {
		switch (action) {
			case WindowTransitionsAdapter.ACTION_CLICK:
				final Activity activity = getActivity();
				final WindowTransition transition = adapter.getItem(position);
				activity.startActivity(WindowTransitionActivity.createIntent(activity, transition));
				transition.overrideStart(activity);
				return true;
		}
		return false;
	}
}