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
import universum.studios.android.samples.ui.SamplesFragment;
import universum.studios.android.samples.ui.widget.SamplesOnDataSetActionListener;
import universum.studios.android.transition.WindowTransition;

/**
 * @author Martin Albedinsky
 */
public final class WindowTransitionsFragment extends SamplesFragment
		implements
		SamplesOnDataSetActionListener<WindowTransitionsAdapter> {

	@SuppressWarnings("unused")
	private static final String TAG = "WindowTransitionsFragment";

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_window_transitions, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		final RecyclerView recyclerView = (RecyclerView) view.findViewById(android.R.id.list);
		recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
		final WindowTransitionsAdapter adapter = new WindowTransitionsAdapter(getActivity());
		adapter.setOnDataSetActionListener(this);
		recyclerView.setAdapter(adapter);
	}

	@Override
	public boolean onDataSetActionSelected(@NonNull WindowTransitionsAdapter adapter, int action, int position, long id, @Nullable Object data) {
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
