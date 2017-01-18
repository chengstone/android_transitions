package universum.studios.android.samples.transition.ui.window;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.TextView;

import butterknife.BindView;
import universum.studios.android.samples.transition.R;
import universum.studios.android.samples.ui.SamplesActivity;
import universum.studios.android.transition.WindowTransition;

/**
 * @author Martin Albedinsky
 */
public final class WindowTransitionActivity extends SamplesActivity {

	@SuppressWarnings("unused")
	private static final String TAG = "WindowTransitionActivity";
	static final String EXTRA_WINDOW_TRANSITION = WindowTransitionActivity.class.getName() + ".EXTRA.WindowTransition";

	@BindView(R.id.text) TextView textView;
	private WindowTransition windowTransition;

	@NonNull
	static Intent createIntent(@NonNull Context context, @NonNull WindowTransition windowTransition) {
		return new Intent(context, WindowTransitionActivity.class).putExtra(
				EXTRA_WINDOW_TRANSITION,
				windowTransition
		);
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		requestFeature(FEATURE_DEPENDENCIES_INJECTION);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_window_transition);
		this.windowTransition = getIntent().getExtras().getParcelable(EXTRA_WINDOW_TRANSITION);
		if (windowTransition != null) {
			textView.setText(windowTransition.getName().replace("_", " "));
		}
	}

	@Override
	public void finish() {
		super.finish();
		if (windowTransition != null) windowTransition.overrideFinish(this);
	}
}
