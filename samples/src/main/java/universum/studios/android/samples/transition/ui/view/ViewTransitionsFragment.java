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

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import universum.studios.android.samples.transition.R;
import universum.studios.android.samples.ui.SamplesFragment;
import universum.studios.android.widget.adapter.OnDataSetActionListener;

/**
 * @author Martin Albedinsky
 */
public final class ViewTransitionsFragment extends SamplesFragment implements OnDataSetActionListener {

	private ViewTransitionsAdapter adapter;

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
		this.adapter = new ViewTransitionsAdapter(getActivity());
		this.adapter.registerOnDataSetActionListener(this);
		recyclerView.setAdapter(adapter);
	}

	@Override public boolean onDataSetActionSelected(final int action, final int position, final long id, @Nullable final Object data) {
		switch (action) {
			case ViewTransitionsAdapter.ACTION_CLICK:
				adapter.getItem(position).start(requireActivity());
				return true;
			default:
				return false;
		}
	}
}