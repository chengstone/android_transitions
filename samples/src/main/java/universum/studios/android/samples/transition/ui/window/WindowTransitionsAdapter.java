package universum.studios.android.samples.transition.ui.window;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import universum.studios.android.samples.transition.R;
import universum.studios.android.samples.ui.widget.SamplesRecyclerAdapter;
import universum.studios.android.transition.ExtraWindowTransitions;
import universum.studios.android.transition.WindowTransition;
import universum.studios.android.transition.WindowTransitions;

/**
 * @author Martin Albedinsky
 */
final class WindowTransitionsAdapter extends SamplesRecyclerAdapter<WindowTransition, WindowTransitionsAdapter.ItemHolder> {

	@SuppressWarnings("unused")
	private static final String TAG = "WindowTransitionsAdapter";

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

	@Override
	public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new ItemHolder(inflate(R.layout.item_list_window_transition, parent));
	}

	@Override
	public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
		((TextView) holder.itemView).setText(getItem(position).getName().replace("_", " "));
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
