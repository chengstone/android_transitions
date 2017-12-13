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
public final class NavigationalTransitionCompatTest extends RobolectricTestCase {

	@Test
	public void testInstantiation() {
		final TestTransition transition = new TestTransition();
		assertThat(transition.getActivityClass(), is(nullValue()));
	}

	@Test
	public void testInstantiationWithActivityClass() {
		final TestTransition transition = new TestTransition(TestCompatActivity.class);
		assertSame(transition.getActivityClass(), TestCompatActivity.class);
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.KITKAT)
	public void testStartForFragmentAtKitKatApiLevel() throws Exception {
		final FragmentActivity activity = Robolectric.buildActivity(TestCompatActivity.class).create().start().resume().get();
		final FragmentManager fragmentManager = activity.getSupportFragmentManager();
		final TestFragment fragment = new TestFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		new TestTransition(SecondaryTestActivity.class).exitTransition(null).start(fragment);
		assertThat(activity.isFinishing(), is(false));
		assertThat(fragment.hasBeenStartActivityCalled(TestFragment.START_ACTIVITY_WITH_OPTIONS), is(true));
		assertThat(fragment.hasBeenStartActivityCalled(
				TestFragment.START_ACTIVITY | TestFragment.START_ACTIVITY_FOR_RESULT | TestFragment.START_ACTIVITY_FOR_RESULT_WITH_OPTIONS
		), is(false));
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	public void testStartForFragmentAtLollipopApiLevel() throws Exception {
		final FragmentActivity activity = Robolectric.buildActivity(TestCompatActivity.class).create().start().resume().get();
		final FragmentManager fragmentManager = activity.getSupportFragmentManager();
		final TestFragment fragment = new TestFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		final Transition transition = new Fade();
		new TestTransition(SecondaryTestActivity.class).exitTransition(transition).start(fragment);
		assertThat(activity.getWindow().getExitTransition(), is(transition));
		assertThat(activity.isFinishing(), is(false));
		assertThat(fragment.hasBeenStartActivityCalled(TestFragment.START_ACTIVITY_WITH_OPTIONS), is(true));
		assertThat(fragment.hasBeenStartActivityCalled(
				TestFragment.START_ACTIVITY | TestFragment.START_ACTIVITY_FOR_RESULT | TestFragment.START_ACTIVITY_FOR_RESULT_WITH_OPTIONS
		), is(false));
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.KITKAT)
	public void testOnStartForFragmentAtKitKatApiLevel() throws Exception {
		final FragmentActivity activity = Robolectric.buildActivity(TestCompatActivity.class).create().start().resume().get();
		final FragmentManager fragmentManager = activity.getSupportFragmentManager();
		final TestFragment fragment = new TestFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		new TestTransition(SecondaryTestActivity.class).onStart(fragment);
		assertThat(activity.isFinishing(), is(false));
		assertThat(fragment.hasBeenStartActivityCalled(TestFragment.START_ACTIVITY_WITH_OPTIONS), is(true));
		assertThat(fragment.hasBeenStartActivityCalled(
				TestFragment.START_ACTIVITY | TestFragment.START_ACTIVITY_FOR_RESULT | TestFragment.START_ACTIVITY_FOR_RESULT_WITH_OPTIONS
		), is(false));
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	public void testOnStartForFragmentAtLollipopApiLevel() throws Exception {
		final FragmentActivity activity = Robolectric.buildActivity(TestCompatActivity.class).create().start().resume().get();
		final FragmentManager fragmentManager = activity.getSupportFragmentManager();
		final TestFragment fragment = new TestFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		new TestTransition(SecondaryTestActivity.class).onStart(fragment);
		assertThat(activity.isFinishing(), is(false));
		assertThat(fragment.hasBeenStartActivityCalled(TestFragment.START_ACTIVITY_WITH_OPTIONS), is(true));
		assertThat(fragment.hasBeenStartActivityCalled(
				TestFragment.START_ACTIVITY | TestFragment.START_ACTIVITY_FOR_RESULT | TestFragment.START_ACTIVITY_FOR_RESULT_WITH_OPTIONS
		), is(false));
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.KITKAT)
	public void testOnStartForFragmentWithRequestCodeAtKitKatApiLevel() throws Exception {
		final FragmentActivity activity = Robolectric.buildActivity(TestCompatActivity.class).create().start().resume().get();
		final FragmentManager fragmentManager = activity.getSupportFragmentManager();
		final TestFragment fragment = new TestFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		new TestTransition(SecondaryTestActivity.class).requestCode(100).onStart(fragment);
		assertThat(activity.isFinishing(), is(false));
		assertThat(fragment.hasBeenStartActivityCalled(TestFragment.START_ACTIVITY_FOR_RESULT), is(true));
		assertThat(fragment.hasBeenStartActivityCalled(
				TestFragment.START_ACTIVITY | TestFragment.START_ACTIVITY_WITH_OPTIONS | TestFragment.START_ACTIVITY_FOR_RESULT_WITH_OPTIONS
		), is(false));
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	public void testOnStartForFragmentWithRequestCodeAtLollipopApiLevel() throws Exception {
		final FragmentActivity activity = Robolectric.buildActivity(TestCompatActivity.class).create().start().resume().get();
		final FragmentManager fragmentManager = activity.getSupportFragmentManager();
		final TestFragment fragment = new TestFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		new TestTransition(SecondaryTestActivity.class).requestCode(100).onStart(fragment);
		assertThat(activity.isFinishing(), is(false));
		assertThat(fragment.hasBeenStartActivityCalled(TestFragment.START_ACTIVITY_FOR_RESULT_WITH_OPTIONS), is(true));
		assertThat(fragment.hasBeenStartActivityCalled(
				TestFragment.START_ACTIVITY | TestFragment.START_ACTIVITY_WITH_OPTIONS | TestFragment.START_ACTIVITY_FOR_RESULT
		), is(false));
	}

	@Test
	public void testFinishForFragment() throws Exception {
		final FragmentActivity activity = Robolectric.buildActivity(TestCompatActivity.class).create().start().resume().get();
		final FragmentManager fragmentManager = activity.getSupportFragmentManager();
		final Fragment fragment = new TestFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		new TestTransition().finish(fragment);
		assertThat(activity.isFinishing(), is(true));
	}

	@Test
	public void testOnFinishForFragment() throws Exception {
		final FragmentActivity activity = Robolectric.buildActivity(TestCompatActivity.class).create().start().resume().get();
		final FragmentManager fragmentManager = activity.getSupportFragmentManager();
		final Fragment fragment = new TestFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		new TestTransition().onFinish(fragment);
		assertThat(activity.isFinishing(), is(true));
	}

	private static final class TestTransition extends NavigationalTransitionCompat {

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

		@NonNull
		@Override
		public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
