package universum.studios.android.samples.transition.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import universum.studios.android.samples.transition.R;
import universum.studios.android.samples.ui.SamplesFragment;

/**
 * @author Martin Albedinsky
 */
public final class HomeFragment extends SamplesFragment {

	@SuppressWarnings("unused")
	private static final String TAG = "HomeFragment";

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_home, container, false);
	}
}
