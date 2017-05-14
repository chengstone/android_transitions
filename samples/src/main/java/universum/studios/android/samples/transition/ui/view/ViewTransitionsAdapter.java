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

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import universum.studios.android.samples.transition.R;
import universum.studios.android.widget.adapter.SimpleRecyclerAdapter;

/**
 * @author Martin Albedinsky
 */
final class ViewTransitionsAdapter extends SimpleRecyclerAdapter<ViewTransition, ViewTransitionsAdapter.ItemHolder> {

	@SuppressWarnings("unused")
	private static final String TAG = "WindowTransitionsAdapter";

	static final int ACTION_CLICK = 0x01;

	ViewTransitionsAdapter(@NonNull Context context) {
		super(context, new ViewTransition[]{
				ViewTransitions.reveal(),
				ViewTransitions.scale(),
				ViewTransitions.translate()
		});
	}

	@Override
	public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new ItemHolder(inflate(R.layout.item_list_window_transition, parent));
	}

	@Override
	public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
		((TextView) holder.itemView).setText(getItem(position).getName());
	}

	final class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		ItemHolder(View itemView) {
			super(itemView);
			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			notifyDataSetActionSelected(ACTION_CLICK, getAdapterPosition(), null);
		}
	}
}