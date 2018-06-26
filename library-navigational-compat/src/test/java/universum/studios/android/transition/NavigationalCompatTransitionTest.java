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
package universum.studios.android.transition;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.transition.Fade;
import android.transition.Transition;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.junit.Test;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import universum.studios.android.test.local.RobolectricTestCase;
import universum.studios.android.test.local.SecondaryTestActivity;
import universum.studios.android.test.local.TestCompatActivity;

import static junit.framework.Assert.assertSame;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * @author Martin Albedinsky
 */
public final class NavigationalCompatTransitionTest extends RobolectricTestCase {

	@Test public void testInstantiation() {
		// Act:
		final TestTransition navTransition = new TestTransition();
		// Assert:
		assertThat(navTransition.getActivityClass(), is(nullValue()));
	}

	@Test public void testInstantiationWithActivityClass() {
		// Act:
		final TestTransition navTransition = new TestTransition(TestCompatActivity.class);
		// Assert:
		assertSame(navTransition.getActivityClass(), TestCompatActivity.class);
	}

	@Config(sdk = Build.VERSION_CODES.KITKAT)
	@Test public void testStartForFragmentAtKitKatApiLevel() {
		// Arrange:
		final FragmentActivity activity = Robolectric.buildActivity(TestCompatActivity.class).create().start().resume().get();
		final FragmentManager fragmentManager = activity.getSupportFragmentManager();
		final TestFragment fragment = new TestFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		final NavigationalCompatTransition navTransition = new TestTransition(SecondaryTestActivity.class).exitTransition(null);
		// Act:
		navTransition.start(fragment);
		// Assert:
		assertThat(activity.isFinishing(), is(false));
		assertThat(fragment.hasBeenStartActivityCalled(TestFragment.START_ACTIVITY_WITH_OPTIONS), is(true));
		assertThat(fragment.hasBeenStartActivityCalled(
				TestFragment.START_ACTIVITY | TestFragment.START_ACTIVITY_FOR_RESULT | TestFragment.START_ACTIVITY_FOR_RESULT_WITH_OPTIONS
		), is(false));
	}

	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	@Test public void testStartForFragmentAtLollipopApiLevel() {
		// Arrange:
		final FragmentActivity activity = Robolectric.buildActivity(TestCompatActivity.class).create().start().resume().get();
		final FragmentManager fragmentManager = activity.getSupportFragmentManager();
		final TestFragment fragment = new TestFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		final Transition transition = new Fade();
		final NavigationalCompatTransition navTransition = new TestTransition(SecondaryTestActivity.class).exitTransition(transition);
		// Act:
		navTransition.start(fragment);
		// Assert:
		assertThat(activity.getWindow().getExitTransition(), is(transition));
		assertThat(activity.isFinishing(), is(false));
		assertThat(fragment.hasBeenStartActivityCalled(TestFragment.START_ACTIVITY_WITH_OPTIONS), is(true));
		assertThat(fragment.hasBeenStartActivityCalled(
				TestFragment.START_ACTIVITY | TestFragment.START_ACTIVITY_FOR_RESULT | TestFragment.START_ACTIVITY_FOR_RESULT_WITH_OPTIONS
		), is(false));
	}

	@Config(sdk = Build.VERSION_CODES.KITKAT)
	@Test public void testOnStartForFragmentAtKitKatApiLevel() {
		// Arrange:
		final FragmentActivity activity = Robolectric.buildActivity(TestCompatActivity.class).create().start().resume().get();
		final FragmentManager fragmentManager = activity.getSupportFragmentManager();
		final TestFragment fragment = new TestFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		final NavigationalCompatTransition navTransition = new TestTransition(SecondaryTestActivity.class);
		// Act:
		navTransition.onStart(fragment);
		// Assert:
		assertThat(activity.isFinishing(), is(false));
		assertThat(fragment.hasBeenStartActivityCalled(TestFragment.START_ACTIVITY_WITH_OPTIONS), is(true));
		assertThat(fragment.hasBeenStartActivityCalled(
				TestFragment.START_ACTIVITY | TestFragment.START_ACTIVITY_FOR_RESULT | TestFragment.START_ACTIVITY_FOR_RESULT_WITH_OPTIONS
		), is(false));
	}

	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	@Test public void testOnStartForFragmentAtLollipopApiLevel() {
		// Arrange:
		final FragmentActivity activity = Robolectric.buildActivity(TestCompatActivity.class).create().start().resume().get();
		final FragmentManager fragmentManager = activity.getSupportFragmentManager();
		final TestFragment fragment = new TestFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		final NavigationalCompatTransition navTransition = new TestTransition(SecondaryTestActivity.class);
		// Act:
		navTransition.onStart(fragment);
		// Assert:
		assertThat(activity.isFinishing(), is(false));
		assertThat(fragment.hasBeenStartActivityCalled(TestFragment.START_ACTIVITY_WITH_OPTIONS), is(true));
		assertThat(fragment.hasBeenStartActivityCalled(
				TestFragment.START_ACTIVITY | TestFragment.START_ACTIVITY_FOR_RESULT | TestFragment.START_ACTIVITY_FOR_RESULT_WITH_OPTIONS
		), is(false));
	}

	@Config(sdk = Build.VERSION_CODES.KITKAT)
	@Test public void testOnStartForFragmentWithRequestCodeAtKitKatApiLevel() throws Exception {
		// Arrange:
		final FragmentActivity activity = Robolectric.buildActivity(TestCompatActivity.class).create().start().resume().get();
		final FragmentManager fragmentManager = activity.getSupportFragmentManager();
		final TestFragment fragment = new TestFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		final NavigationalCompatTransition navTransition = new TestTransition(SecondaryTestActivity.class).requestCode(100);
		// Act:
		navTransition.onStart(fragment);
		// Assert:
		assertThat(activity.isFinishing(), is(false));
		assertThat(fragment.hasBeenStartActivityCalled(TestFragment.START_ACTIVITY_FOR_RESULT), is(true));
		assertThat(fragment.hasBeenStartActivityCalled(
				TestFragment.START_ACTIVITY | TestFragment.START_ACTIVITY_WITH_OPTIONS | TestFragment.START_ACTIVITY_FOR_RESULT_WITH_OPTIONS
		), is(false));
	}

	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	@Test public void testOnStartForFragmentWithRequestCodeAtLollipopApiLevel() {
		// Arrange:
		final FragmentActivity activity = Robolectric.buildActivity(TestCompatActivity.class).create().start().resume().get();
		final FragmentManager fragmentManager = activity.getSupportFragmentManager();
		final TestFragment fragment = new TestFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		final NavigationalCompatTransition navTransition = new TestTransition(SecondaryTestActivity.class).requestCode(100);
		// Act:
		navTransition.onStart(fragment);
		// Assert:
		assertThat(activity.isFinishing(), is(false));
		assertThat(fragment.hasBeenStartActivityCalled(TestFragment.START_ACTIVITY_FOR_RESULT_WITH_OPTIONS), is(true));
		assertThat(fragment.hasBeenStartActivityCalled(
				TestFragment.START_ACTIVITY | TestFragment.START_ACTIVITY_WITH_OPTIONS | TestFragment.START_ACTIVITY_FOR_RESULT
		), is(false));
	}

	@Test public void testFinishForFragment() {
		// Arrange:
		final FragmentActivity activity = Robolectric.buildActivity(TestCompatActivity.class).create().start().resume().get();
		final FragmentManager fragmentManager = activity.getSupportFragmentManager();
		final Fragment fragment = new TestFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		final NavigationalCompatTransition navTransition = new TestTransition();
		// Act:
		navTransition.finish(fragment);
		// Assert:
		assertThat(activity.isFinishing(), is(true));
	}

	@Test public void testOnFinishForFragment() {
		// Arrange:
		final FragmentActivity activity = Robolectric.buildActivity(TestCompatActivity.class).create().start().resume().get();
		final FragmentManager fragmentManager = activity.getSupportFragmentManager();
		final Fragment fragment = new TestFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		final NavigationalCompatTransition navTransition = new TestTransition();
		// Act:
		navTransition.onFinish(fragment);
		// Assert:
		assertThat(activity.isFinishing(), is(true));
	}

	private static final class TestTransition extends NavigationalCompatTransition {

		TestTransition() {
			super();
		}

		TestTransition(@NonNull final Class<? extends Activity> classOfTransitionActivity) {
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
		@interface StartActivityCall {}

		@StartActivityCall private int startActivityCalls;

		@Override @NonNull public View onCreateView(
				@NonNull final LayoutInflater inflater,
				@Nullable final ViewGroup container,
				@Nullable final Bundle savedInstanceState
		) {
			return new View(inflater.getContext());
		}

		@Override public void startActivity(@NonNull final Intent intent) {
			super.startActivity(intent);
			this.startActivityCalls |= START_ACTIVITY;
		}

		@Override public void startActivity(@NonNull final Intent intent, @Nullable final Bundle options) {
			super.startActivity(intent, options);
			this.startActivityCalls |= START_ACTIVITY_WITH_OPTIONS;
		}

		@Override public void startActivityForResult(@NonNull final Intent intent, final int requestCode) {
			super.startActivityForResult(intent, requestCode);
			this.startActivityCalls |= START_ACTIVITY_FOR_RESULT;
		}

		@Override public void startActivityForResult(@NonNull final Intent intent, final int requestCode, @Nullable final Bundle options) {
			super.startActivityForResult(intent, requestCode, options);
			this.startActivityCalls |= START_ACTIVITY_FOR_RESULT_WITH_OPTIONS;
		}

		boolean hasBeenStartActivityCalled(@StartActivityCall final int startActivityCall) {
			return (startActivityCalls & startActivityCall) == startActivityCall;
		}
	}
}