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

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import universum.studios.android.samples.transition.R;
import universum.studios.android.widget.adapter.SimpleRecyclerAdapter;
import universum.studios.android.widget.adapter.holder.RecyclerViewHolder;

/**
 * @author Martin Albedinsky
 */
final class ViewTransitionsAdapter extends SimpleRecyclerAdapter<ViewTransitionsAdapter, ViewTransitionsAdapter.ItemHolder, ViewTransition> {

	static final int ACTION_CLICK = 0x01;

	ViewTransitionsAdapter(@NonNull final Context context) {
		super(context, new ViewTransition[]{
				ViewTransitions.reveal(),
				ViewTransitions.scale(),
				ViewTransitions.translate()
		});
	}

	@Override public ItemHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
		return new ItemHolder(inflateView(R.layout.item_list_window_transition, parent));
	}

	@Override public void onBindViewHolder(@NonNull final ItemHolder holder, final int position) {
		((TextView) holder.itemView).setText(getItem(position).getName());
	}

	final class ItemHolder extends RecyclerViewHolder implements View.OnClickListener {

		ItemHolder(@NonNull final View itemView) {
			super(itemView);
			itemView.setOnClickListener(this);
		}

		@Override public void onClick(@NonNull final View view) {
			notifyDataSetActionSelected(ACTION_CLICK, getAdapterPosition(), null);
		}
	}
}