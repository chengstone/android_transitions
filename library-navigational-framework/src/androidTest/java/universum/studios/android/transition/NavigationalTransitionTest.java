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
package universum.studios.android.transition;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.test.annotation.UiThreadTest;
import android.support.test.filters.SdkSuppress;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.transition.Transition;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import universum.studios.android.test.InstrumentedTestCase;
import universum.studios.android.test.instrumented.SecondaryTestActivity;
import universum.studios.android.test.instrumented.TestActivity;
import universum.studios.android.test.instrumented.TestUtils;

import static junit.framework.Assert.assertSame;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = Build.VERSION_CODES.HONEYCOMB)
public final class NavigationalTransitionTest extends InstrumentedTestCase {

	@SuppressWarnings("unused")
	private static final String TAG = "NavigationalTransitionTest";

	@Rule
	public final ActivityTestRule<TestActivity> ACTIVITY_RULE = new ActivityTestRule<>(TestActivity.class);

	@Test
	public void testInstantiation() {
		final TestTransition transition = new TestTransition();
		assertThat(transition.getActivityClass(), is(nullValue()));
	}

	@Test
	public void testInstantiationWithActivityClass() {
		final TestTransition transition = new TestTransition(TestActivity.class);
		assertSame(transition.getActivityClass(), TestActivity.class);
	}

	@Test
	@UiThreadTest
	public void testStartForFragment() throws Exception {
		final Activity activity = ACTIVITY_RULE.getActivity();
		final FragmentManager fragmentManager = activity.getFragmentManager();
		final TestFragment fragment = new TestFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		final Transition transition = TestUtils.inflateTransition(mContext, "transition_fade");
		new TestTransition(SecondaryTestActivity.class).exitTransition(transition).start(fragment);
		assertThat(activity.isFinishing(), is(false));
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			assertThat(activity.getWindow().getExitTransition(), is(transition));
			assertThat(fragment.hasBeenStartActivityCalled(TestFragment.START_ACTIVITY_WITH_OPTIONS), is(true));
			assertThat(fragment.hasBeenStartActivityCalled(
					TestFragment.START_ACTIVITY | TestFragment.START_ACTIVITY_FOR_RESULT | TestFragment.START_ACTIVITY_FOR_RESULT_WITH_OPTIONS

			), is(false));
		} else {
			assertThat(fragment.hasBeenStartActivityCalled(TestFragment.START_ACTIVITY_WITH_OPTIONS), is(true));
			assertThat(fragment.hasBeenStartActivityCalled(
					TestFragment.START_ACTIVITY | TestFragment.START_ACTIVITY_FOR_RESULT | TestFragment.START_ACTIVITY_FOR_RESULT_WITH_OPTIONS

			), is(false));
		}
	}

	@Test
	@UiThreadTest
	public void testOnStartForFragment() throws Exception {
		final Activity activity = ACTIVITY_RULE.getActivity();
		final FragmentManager fragmentManager = activity.getFragmentManager();
		final TestFragment fragment = new TestFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		new TestTransition(SecondaryTestActivity.class).onStart(fragment);
		assertThat(activity.isFinishing(), is(false));
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			assertThat(fragment.hasBeenStartActivityCalled(TestFragment.START_ACTIVITY_WITH_OPTIONS), is(true));
			assertThat(fragment.hasBeenStartActivityCalled(
					TestFragment.START_ACTIVITY | TestFragment.START_ACTIVITY_FOR_RESULT | TestFragment.START_ACTIVITY_FOR_RESULT_WITH_OPTIONS

			), is(false));
		} else {
			assertThat(fragment.hasBeenStartActivityCalled(TestFragment.START_ACTIVITY_WITH_OPTIONS), is(true));
			assertThat(fragment.hasBeenStartActivityCalled(
					TestFragment.START_ACTIVITY | TestFragment.START_ACTIVITY_FOR_RESULT | TestFragment.START_ACTIVITY_FOR_RESULT_WITH_OPTIONS

			), is(false));
		}
	}

	@Test
	@UiThreadTest
	public void testOnStartForFragmentWithRequestCode() throws Exception {
		final Activity activity = ACTIVITY_RULE.getActivity();
		final FragmentManager fragmentManager = activity.getFragmentManager();
		final TestFragment fragment = new TestFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		new TestTransition(SecondaryTestActivity.class).requestCode(100).onStart(fragment);
		assertThat(activity.isFinishing(), is(false));
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			assertThat(fragment.hasBeenStartActivityCalled(TestFragment.START_ACTIVITY_FOR_RESULT_WITH_OPTIONS), is(true));
			assertThat(fragment.hasBeenStartActivityCalled(
					TestFragment.START_ACTIVITY | TestFragment.START_ACTIVITY_WITH_OPTIONS | TestFragment.START_ACTIVITY_FOR_RESULT

			), is(false));
		} else {
			assertThat(fragment.hasBeenStartActivityCalled(TestFragment.START_ACTIVITY_FOR_RESULT), is(true));
			assertThat(fragment.hasBeenStartActivityCalled(
					TestFragment.START_ACTIVITY | TestFragment.START_ACTIVITY_WITH_OPTIONS | TestFragment.START_ACTIVITY_FOR_RESULT_WITH_OPTIONS

			), is(false));
		}
	}

	@Test
	@UiThreadTest
	public void testFinishForFragment() throws Exception {
		final Activity activity = ACTIVITY_RULE.getActivity();
		final FragmentManager fragmentManager = activity.getFragmentManager();
		final Fragment fragment = new TestFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		new TestTransition().finish(fragment);
		assertThat(activity.isFinishing(), is(true));
	}

	@Test
	@UiThreadTest
	public void testOnFinishForFragment() throws Exception {
		final Activity activity = ACTIVITY_RULE.getActivity();
		final FragmentManager fragmentManager = activity.getFragmentManager();
		final Fragment fragment = new TestFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		new TestTransition().onFinish(fragment);
		assertThat(activity.isFinishing(), is(true));
	}

	private static final class TestTransition extends NavigationalTransition {

		TestTransition() {
			super();
		}

		TestTransition(@NonNull Class<? extends Activity> classOfTransitionActivity) {
			super(classOfTransitionActivity);
		}
	}

	public static final class TestFragment extends Fragment {

		static final int START_ACTIVITY = 0x0000001;
		static final int START_ACTIVITY_WITH_OPTIONS = 0x0000001 << 1;
		static final int START_ACTIVITY_FOR_RESULT = 0x0000001 << 2;
		static final int START_ACTIVITY_FOR_RESULT_WITH_OPTIONS = 0x0000001 << 3;

		@IntDef(flag = true, value = {
				START_ACTIVITY,
				START_ACTIVITY_WITH_OPTIONS,
				START_ACTIVITY_FOR_RESULT,
				START_ACTIVITY_FOR_RESULT_WITH_OPTIONS
		})
		@Retention(RetentionPolicy.SOURCE)
		@interface StartActivityCall {
		}

		@StartActivityCall private int startActivityCalls;

		@Nullable
		@Override
		public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
			return new View(inflater.getContext());
		}

		@Override
		public void startActivity(Intent intent) {
			super.startActivity(intent);
			this.startActivityCalls |= START_ACTIVITY;
		}

		@Override
		public void startActivity(Intent intent, Bundle options) {
			super.startActivity(intent, options);
			this.startActivityCalls |= START_ACTIVITY_WITH_OPTIONS;
		}

		@Override
		public void startActivityForResult(Intent intent, int requestCode) {
			super.startActivityForResult(intent, requestCode);
			this.startActivityCalls |= START_ACTIVITY_FOR_RESULT;
		}

		@Override
		public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
			super.startActivityForResult(intent, requestCode, options);
			this.startActivityCalls |= START_ACTIVITY_FOR_RESULT_WITH_OPTIONS;
		}

		boolean hasBeenStartActivityCalled(@StartActivityCall int startActivityCall) {
			return (startActivityCalls & startActivityCall) == startActivityCall;
		}
	}
}
