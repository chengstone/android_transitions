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
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import universum.studios.android.samples.transition.R;
import universum.studios.android.transition.ExtraWindowTransitions;
import universum.studios.android.transition.WindowTransition;
import universum.studios.android.transition.WindowTransitions;
import universum.studios.android.widget.adapter.SimpleRecyclerAdapter;
import universum.studios.android.widget.adapter.holder.RecyclerViewHolder;

/**
 * @author Martin Albedinsky
 */
final class WindowTransitionsAdapter extends SimpleRecyclerAdapter<WindowTransitionsAdapter, WindowTransitionsAdapter.ItemHolder, WindowTransition> {

	static final int ACTION_CLICK = 0x01;

	WindowTransitionsAdapter(@NonNull Context context) {
		super(context, new WindowTransition[]{
				// Common --------------------------------------------------------------------------
				WindowTransitions.NONE,
				WindowTransitions.CROSS_FADE,
				WindowTransitions.CROSS_FADE_AND_HOLD,
				WindowTransitions.SLIDE_TO_LEFT,
				WindowTransitions.SLIDE_TO_RIGHT,
				WindowTransitions.SLIDE_TO_TOP,
				WindowTransitions.SLIDE_TO_BOTTOM,
				WindowTransitions.SLIDE_TO_LEFT_AND_SCALE_OUT,
				WindowTransitions.SLIDE_TO_RIGHT_AND_SCALE_OUT,
				WindowTransitions.SLIDE_TO_TOP_AND_SCALE_OUT,
				WindowTransitions.SLIDE_TO_BOTTOM_AND_SCALE_OUT,
				// Extra ---------------------------------------------------------------------------
				ExtraWindowTransitions.SLIDE_TO_LEFT_AND_HOLD,
				ExtraWindowTransitions.SLIDE_TO_RIGHT_AND_HOLD,
				ExtraWindowTransitions.SLIDE_TO_TOP_AND_HOLD,
				ExtraWindowTransitions.SLIDE_TO_BOTTOM_AND_HOLD
		});
	}

	@Override public ItemHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
		return new ItemHolder(inflateView(R.layout.item_list_window_transition, parent));
	}

	@Override public void onBindViewHolder(@NonNull final ItemHolder holder, final int position) {
		((TextView) holder.itemView).setText(getItem(position).getName().replace("_", " "));
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