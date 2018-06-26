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
import android.app.ActivityOptions;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.transition.Fade;
import android.transition.Transition;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;

import org.junit.Test;
import org.robolectric.annotation.Config;

import java.util.List;

import universum.studios.android.test.local.RobolectricTestCase;
import universum.studios.android.test.local.SecondaryTestActivity;
import universum.studios.android.test.local.TestActivity;

import static junit.framework.Assert.assertSame;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * @author Martin Albedinsky
 */
public final class BaseNavigationalTransitionTest extends RobolectricTestCase {

	private static final int WINDOW_FEATURE_ACTIVITY_TRANSITIONS = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ?
			Window.FEATURE_ACTIVITY_TRANSITIONS :
			13;

	private static void waitFor(final long duration) {
		try {
			Thread.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test public void testInstantiation() {
		// Act:
		final TestTransition transition = new TestTransition();
		// Assert:
		assertThat(transition.getActivityClass(), is(nullValue()));
		assertThat(transition.intentExtras(), is(notNullValue()));
		assertThat(transition.intentExtras().isEmpty(), is(true));
		assertThat(transition.requestCode(), is(BaseNavigationalTransition.RC_NONE));
		assertThat(transition.enterTransition(), is(nullValue()));
		assertThat(transition.reenterTransition(), is(nullValue()));
		assertThat(transition.returnTransition(), is(nullValue()));
		assertThat(transition.exitTransition(), is(nullValue()));
		assertThat(transition.exitTransition(), is(nullValue()));
		assertThat(transition.allowReturnTransitionOverlap(), is(true));
		assertThat(transition.sharedElementEnterTransition(), is(nullValue()));
		assertThat(transition.sharedElementReenterTransition(), is(nullValue()));
		assertThat(transition.sharedElementReturnTransition(), is(nullValue()));
		assertThat(transition.sharedElementExitTransition(), is(nullValue()));
		assertThat(transition.sharedElementsUseOverlay(), is(true));
		assertThat(transition.sharedElements(), is(nullValue()));
		assertThat(transition.singleSharedElement(), is(nullValue()));
	}

	@Test public void testInstantiationWithActivity() {
		// Act:
		final TestTransition transition = new TestTransition(TestActivity.class);
		// Assert:
		assertSame(transition.getActivityClass(), TestActivity.class);
	}

	@Test public void testIntentExtras() {
		// Arrange:
		final TestTransition transition = new TestTransition();
		// Act:
		transition.intentExtras().putString("extra_text", "Extra text.");
		transition.intentExtras().putInt("extra_int", 1000);
		// Assert:
		assertThat(transition.intentExtras().getString("extra_text"), is("Extra text."));
		assertThat(transition.intentExtras().getInt("extra_int"), is(1000));
	}

	@Test public void testRequestCode() {
		// Arrange:
		// Act:
		// Assert:
		assertThat(new TestTransition().requestCode(50).requestCode(), is(50));
	}

	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	@Test public void testEnterTransition() {
		// Arrange:
		// Act:
		// Assert:
		final Transition transition = new Fade();
		assertThat(new TestTransition().enterTransition(transition).enterTransition(), is(transition));
	}

	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	@Test public void testReenterTransition() {
		// Arrange:
		// Act:
		// Assert:
		final Transition transition = new Fade();
		assertThat(new TestTransition().reenterTransition(transition).reenterTransition(), is(transition));
	}

	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	@Test public void testReturnTransition() {
		// Arrange:
		// Act:
		// Assert:
		final Transition transition = new Fade();
		assertThat(new TestTransition().returnTransition(transition).returnTransition(), is(transition));
	}

	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	@Test public void testExitTransition() {
		// Arrange:
		// Act:
		// Assert:
		final Transition transition = new Fade();
		assertThat(new TestTransition().exitTransition(transition).exitTransition(), is(transition));
	}

	@Test public void testAllowEnterTransitionOverlap() {
		// Arrange:
		// Act:
		// Assert:
		assertThat(new TestTransition().allowEnterTransitionOverlap(false).allowEnterTransitionOverlap(), is(false));
		assertThat(new TestTransition().allowEnterTransitionOverlap(true).allowEnterTransitionOverlap(), is(true));
	}

	@Test public void testAllowReturnTransitionOverlap() {
		// Arrange:
		// Act:
		// Assert:
		assertThat(new TestTransition().allowReturnTransitionOverlap(false).allowReturnTransitionOverlap(), is(false));
		assertThat(new TestTransition().allowReturnTransitionOverlap(true).allowReturnTransitionOverlap(), is(true));
	}

	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	@Test public void testSharedElementEnterTransition() {
		// Arrange:
		// Act:
		// Assert:
		final Transition transition = new Fade();
		assertThat(new TestTransition().sharedElementEnterTransition(transition).sharedElementEnterTransition(), is(transition));
	}

	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	@Test public void testSharedElementReenterTransition() {
		// Arrange:
		// Act:
		// Assert:
		final Transition transition = new Fade();
		assertThat(new TestTransition().sharedElementReenterTransition(transition).sharedElementReenterTransition(), is(transition));
	}

	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	@Test public void testSharedElementReturnTransition() {
		// Arrange:
		// Act:
		// Assert:
		final Transition transition = new Fade();
		assertThat(new TestTransition().sharedElementReturnTransition(transition).sharedElementReturnTransition(), is(transition));
	}

	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	@Test public void testSharedElementExitTransition() {
		// Arrange:
		// Act:
		// Assert:
		final Transition transition = new Fade();
		assertThat(new TestTransition().sharedElementExitTransition(transition).sharedElementExitTransition(), is(transition));
	}

	@Test public void testSharedElementUseOverlay() {
		// Arrange:
		// Act:
		// Assert:
		assertThat(new TestTransition().sharedElementsUseOverlay(false).sharedElementsUseOverlay(), is(false));
		assertThat(new TestTransition().sharedElementsUseOverlay(true).sharedElementsUseOverlay(), is(true));
	}

	@SuppressWarnings("ConstantConditions")
	@Test public void testSharedElements() {
		// Arrange:
		final TestTransition transition = new TestTransition();
		final View firstElement = new View(application);
		final View secondElement = new View(application);
		final View thirdElement = new View(application);
		// Act:
		transition.sharedElements(
				new Pair<>(firstElement, "first_element"),
				new Pair<>(secondElement, "second_element")
		);
		transition.sharedElements(new Pair<>(thirdElement, "third_element"));
		// Assert:
		final List<Pair<View, String>> sharedElements = transition.sharedElements();
		assertThat(sharedElements, is(notNullValue()));
		assertThat(sharedElements.size(), is(3));
		assertThat(sharedElements.get(0).first, is(firstElement));
		assertThat(sharedElements.get(0).second, is("first_element"));
		assertThat(sharedElements.get(1).first, is(secondElement));
		assertThat(sharedElements.get(1).second, is("second_element"));
		assertThat(sharedElements.get(2).first, is(thirdElement));
		assertThat(sharedElements.get(2).second, is("third_element"));
	}

	@SuppressWarnings("ConstantConditions")
	@Test public void testSharedElement() {
		// Arrange:
		final TestTransition transition = new TestTransition();
		final View firstElement = new View(application);
		final View secondElement = new View(application);
		final View thirdElement = new View(application);
		// Act:
		transition.sharedElement(firstElement, "first_element");
		transition.sharedElement(secondElement, "second_element");
		transition.sharedElement(thirdElement, "third_element");
		// Assert:
		final List<Pair<View, String>> sharedElements = transition.sharedElements();
		assertThat(sharedElements, is(notNullValue()));
		assertThat(sharedElements.size(), is(3));
		assertThat(sharedElements.get(0).first, is(firstElement));
		assertThat(sharedElements.get(0).second, is("first_element"));
		assertThat(sharedElements.get(1).first, is(secondElement));
		assertThat(sharedElements.get(1).second, is("second_element"));
		assertThat(sharedElements.get(2).first, is(thirdElement));
		assertThat(sharedElements.get(2).second, is("third_element"));
	}

	@SuppressWarnings("ConstantConditions")
	@Test public void testSingleSharedElement() {
		// Arrange:
		final TestTransition transition = new TestTransition();
		final View firstElement = new View(application);
		// Act + Assert:
		transition.sharedElement(firstElement, "first_element");
		assertThat(transition.singleSharedElement(), is(notNullValue()));
		assertThat(transition.singleSharedElement().first, is(firstElement));
		assertThat(transition.singleSharedElement().second, is("first_element"));
		final View secondElement = new View(application);
		transition.sharedElement(secondElement, "second_element");
		assertThat(transition.singleSharedElement(), is(notNullValue()));
		assertThat(transition.singleSharedElement().first, is(firstElement));
		assertThat(transition.singleSharedElement().second, is("first_element"));
	}

	@SuppressWarnings("ConstantConditions")
	@Test public void testSingleSharedElementOnEmptySharedElements() {
		// Arrange:
		final TestTransition transition = new TestTransition();
		final View firstElement = new View(application);
		transition.sharedElement(firstElement, "first_element");
		transition.sharedElements().clear();
		// Act + Assert:
		assertThat(transition.singleSharedElement(), is(nullValue()));
	}

	@Config(sdk = Build.VERSION_CODES.KITKAT)
	@Test public void testStartAtKitKatApiLevel() {
		// Arrange:
		final Activity mockActivity = mock(TestActivity.class);
		final Window mockWindow = mock(Window.class);
		when(mockActivity.getWindow()).thenReturn(mockWindow);
		final BaseNavigationalTransition navTransition = new TestTransition(SecondaryTestActivity.class);
		// Act:
		navTransition.start(mockActivity);
		// Assert:
		verify(mockActivity).startActivity(any(Intent.class));
		verify(mockActivity, times(0)).startActivityForResult(any(Intent.class), anyInt());
	}

	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	@Test public void testStartAtLollipopApiLevel() {
		// Arrange:
		final Activity mockActivity = mock(TestActivity.class);
		final Window mockWindow = mock(Window.class);
		when(mockActivity.getWindow()).thenReturn(mockWindow);
		final Transition transition = new Fade();
		final BaseNavigationalTransition navTransition = new TestTransition(SecondaryTestActivity.class).exitTransition(transition);
		// Act:
		navTransition.start(mockActivity);
		// Assert:
		verify(mockWindow).hasFeature(WINDOW_FEATURE_ACTIVITY_TRANSITIONS);
		verify(mockWindow).setExitTransition(transition);
		verifyNoMoreInteractions(mockWindow);
		verify(mockActivity).startActivity(any(Intent.class), (Bundle) isNull());
		verify(mockActivity, times(0)).startActivityForResult(any(Intent.class), anyInt(), any(Bundle.class));
	}

	@Config(sdk = Build.VERSION_CODES.KITKAT)
	@Test public void testOnStartAtKitKatApiLevel() {
		// Arrange:
		final Activity mockActivity = mock(TestActivity.class);
		final Window mockWindow = mock(Window.class);
		when(mockActivity.getWindow()).thenReturn(mockWindow);
		final BaseNavigationalTransition navTransition = new TestTransition(SecondaryTestActivity.class);
		// Act:
		navTransition.onStart(mockActivity);
		// Assert:
		verify(mockActivity).startActivity(any(Intent.class));
		verify(mockActivity, times(0)).startActivityForResult(any(Intent.class), anyInt());
	}

	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	@Test public void testOnStartAtLollipopApiLevel() {
		// Arrange:
		final Activity mockActivity = mock(TestActivity.class);
		final Window mockWindow = mock(Window.class);
		when(mockActivity.getWindow()).thenReturn(mockWindow);
		final BaseNavigationalTransition navTransition = new TestTransition(SecondaryTestActivity.class);
		// Act:
		navTransition.onStart(mockActivity);
		// Assert:
		verify(mockWindow).hasFeature(WINDOW_FEATURE_ACTIVITY_TRANSITIONS);
		verifyNoMoreInteractions(mockWindow);
		verify(mockActivity).startActivity(any(Intent.class), (Bundle) isNull());
		verify(mockActivity, times(0)).startActivityForResult(any(Intent.class), anyInt(), any(Bundle.class));
	}

	@Config(sdk = Build.VERSION_CODES.KITKAT)
	@Test public void testOnStartWithRequestCodeAtKitKatApiLevel() {
		// Arrange:
		final Activity mockActivity = mock(TestActivity.class);
		final Window mockWindow = mock(Window.class);
		when(mockActivity.getWindow()).thenReturn(mockWindow);
		final BaseNavigationalTransition navTransition = new TestTransition(SecondaryTestActivity.class).requestCode(150);
		// Act:
		navTransition.onStart(mockActivity);
		// Assert:
		verify(mockActivity).startActivityForResult(any(Intent.class), eq(150));
		verify(mockActivity, times(0)).startActivity(any(Intent.class));
	}

	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	@Test public void testOnStartWithRequestCodeAtLollipopApiLevel() {
		// Arrange:
		final Activity mockActivity = mock(TestActivity.class);
		final Window mockWindow = mock(Window.class);
		when(mockActivity.getWindow()).thenReturn(mockWindow);
		final BaseNavigationalTransition navTransition = new TestTransition(SecondaryTestActivity.class).requestCode(150);
		// Act:
		navTransition.onStart(mockActivity);
		// Assert:
		verify(mockWindow).hasFeature(WINDOW_FEATURE_ACTIVITY_TRANSITIONS);
		verifyNoMoreInteractions(mockWindow);
		verify(mockActivity).startActivityForResult(any(Intent.class), eq(150), (Bundle) isNull());
		verify(mockActivity, times(0)).startActivity(any(Intent.class), any(Bundle.class));
	}

	@Config(sdk = Build.VERSION_CODES.JELLY_BEAN)
	@Test public void testFinishCallerDelayedAtJellyBeanApiLevel() {
		// Arrange:
		final Activity mockActivity = mock(TestActivity.class);
		final Window mockWindow = mock(Window.class);
		final View decorView = new FrameLayout(application) {

			@Override public boolean postDelayed(@NonNull final Runnable action, final long delayMillis) {
				waitFor(delayMillis);
				action.run();
				return true;
			}
		};
		when(mockWindow.getDecorView()).thenReturn(decorView);
		when(mockActivity.getWindow()).thenReturn(mockWindow);
		when(mockActivity.isFinishing()).thenReturn(false);
		final BaseNavigationalTransition navTransition = new TestTransition();
		// Act:
		navTransition.finishCallerDelayed(mockActivity, 100);
		// Assert:
		verify(mockActivity).getWindow();
		verify(mockWindow).getDecorView();
		verifyNoMoreInteractions(mockWindow);
		verify(mockActivity).finish();
		verify(mockActivity).isFinishing();
		verifyNoMoreInteractions(mockActivity);
	}

	@Config(sdk = Build.VERSION_CODES.JELLY_BEAN_MR1)
	@Test public void testFinishCallerDelayedOnJellyBeanMR1ApiLevel() {
		// Arrange:
		final Activity mockActivity = mock(TestActivity.class);
		final Window mockWindow = mock(Window.class);
		final View decorView = new FrameLayout(application) {

			@Override public boolean postDelayed(@NonNull final Runnable action, final long delayMillis) {
				waitFor(delayMillis);
				action.run();
				return true;
			}
		};
		when(mockWindow.getDecorView()).thenReturn(decorView);
		when(mockActivity.getWindow()).thenReturn(mockWindow);
		when(mockActivity.isFinishing()).thenReturn(false);
		when(mockActivity.isDestroyed()).thenReturn(false);
		final BaseNavigationalTransition navTransition = new TestTransition();
		// Act:
		navTransition.finishCallerDelayed(mockActivity, 100);
		// Assert:
		verify(mockActivity).getWindow();
		verify(mockWindow).getDecorView();
		verifyNoMoreInteractions(mockWindow);
		verify(mockActivity).finish();
		verify(mockActivity).isDestroyed();
		verify(mockActivity).isFinishing();
		verifyNoMoreInteractions(mockActivity);
	}

	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	@Test public void testFinishCallerDelayedAtLollipopApiLevel() {
		// Arrange:
		final Activity mockActivity = mock(TestActivity.class);
		final Window mockWindow = mock(Window.class);
		final View decorView = new FrameLayout(application) {

			@Override public boolean postDelayed(@NonNull final Runnable action, final long delayMillis) {
				waitFor(delayMillis);
				action.run();
				return true;
			}
		};
		when(mockWindow.getDecorView()).thenReturn(decorView);
		when(mockActivity.getWindow()).thenReturn(mockWindow);
		when(mockActivity.isFinishing()).thenReturn(false);
		when(mockActivity.isDestroyed()).thenReturn(false);
		final BaseNavigationalTransition navTransition = new TestTransition();
		// Act:
		navTransition.finishCallerDelayed(mockActivity, 100);
		// Assert:
		verify(mockActivity).getWindow();
		verify(mockWindow).getDecorView();
		verifyNoMoreInteractions(mockWindow);
		verify(mockActivity).finishAfterTransition();
		verify(mockActivity).isDestroyed();
		verify(mockActivity).isFinishing();
		verifyNoMoreInteractions(mockActivity);
	}

	@Config(sdk = Build.VERSION_CODES.JELLY_BEAN_MR2)
	@Test public void testFinishCallerDelayedForDestroyedCaller() {
		// Arrange:
		final Activity mockActivity = mock(TestActivity.class);
		final Window mockWindow = mock(Window.class);
		final View decorView = new FrameLayout(application) {

			@Override public boolean postDelayed(@NonNull final Runnable action, final long delayMillis) {
				waitFor(delayMillis);
				action.run();
				return true;
			}
		};
		when(mockWindow.getDecorView()).thenReturn(decorView);
		when(mockActivity.getWindow()).thenReturn(mockWindow);
		when(mockActivity.isDestroyed()).thenReturn(true);
		final BaseNavigationalTransition navTransition = new TestTransition();
		// Act:
		navTransition.finishCallerDelayed(mockActivity, 100);
		// Assert:
		verify(mockActivity).getWindow();
		verify(mockWindow).getDecorView();
		verifyNoMoreInteractions(mockWindow);
		verify(mockActivity).isDestroyed();
		verifyNoMoreInteractions(mockActivity);
	}

	@Config(sdk = Build.VERSION_CODES.JELLY_BEAN)
	@Test public void testFinishCallerDelayedForFinishingCallerAtJellyBeanApiLevel() {
		// Arrange:
		final Activity mockActivity = mock(TestActivity.class);
		final Window mockWindow = mock(Window.class);
		final View decorView = new FrameLayout(application) {

			@Override public boolean postDelayed(@NonNull final Runnable action, final long delayMillis) {
				waitFor(delayMillis);
				action.run();
				return true;
			}
		};
		when(mockWindow.getDecorView()).thenReturn(decorView);
		when(mockActivity.getWindow()).thenReturn(mockWindow);
		when(mockActivity.isFinishing()).thenReturn(true);
		final BaseNavigationalTransition navTransition = new TestTransition();
		// Act:
		navTransition.finishCallerDelayed(mockActivity, 100);
		// Assert:
		verify(mockActivity).getWindow();
		verify(mockWindow).getDecorView();
		verifyNoMoreInteractions(mockWindow);
		verify(mockActivity).isFinishing();
		verifyNoMoreInteractions(mockActivity);
	}

	@Config(sdk = Build.VERSION_CODES.JELLY_BEAN_MR1)
	@Test public void testFinishCallerDelayedForFinishingCallerOnJellyBeanMR1ApiLevel() {
		// Arrange:
		final Activity mockActivity = mock(TestActivity.class);
		final Window mockWindow = mock(Window.class);
		final View decorView = new FrameLayout(application) {

			@Override public boolean postDelayed(@NonNull final Runnable action, final long delayMillis) {
				waitFor(delayMillis);
				action.run();
				return true;
			}
		};
		when(mockWindow.getDecorView()).thenReturn(decorView);
		when(mockActivity.getWindow()).thenReturn(mockWindow);
		when(mockActivity.isFinishing()).thenReturn(true);
		when(mockActivity.isDestroyed()).thenReturn(false);
		final BaseNavigationalTransition navTransition = new TestTransition();
		// Act:
		navTransition.finishCallerDelayed(mockActivity, 100);
		// Assert:
		verify(mockActivity).getWindow();
		verify(mockWindow).getDecorView();
		verifyNoMoreInteractions(mockWindow);
		verify(mockActivity).isDestroyed();
		verify(mockActivity).isFinishing();
		verifyNoMoreInteractions(mockActivity);
	}

	@Test public void testFinishCallerDelayedWithoutDecorView() {
		// Arrange:
		final Activity mockActivity = mock(TestActivity.class);
		final Window mockWindow = mock(Window.class);
		when(mockWindow.getDecorView()).thenReturn(null);
		when(mockActivity.getWindow()).thenReturn(mockWindow);
		final BaseNavigationalTransition navTransition = new TestTransition();
		// Act:
		navTransition.finishCallerDelayed(mockActivity, 250);
		// Assert:
		waitFor(250);
		verify(mockActivity).getWindow();
		verifyNoMoreInteractions(mockActivity);
		verify(mockWindow).getDecorView();
		verifyNoMoreInteractions(mockWindow);
	}

	@Config(sdk = Build.VERSION_CODES.KITKAT)
	@Test public void testFinishCallerAtKitKatApiLevel() {
		// Arrange:
		final Activity mockActivity = mock(TestActivity.class);
		final BaseNavigationalTransition navTransition = new TestTransition();
		// Act:
		navTransition.finishCaller(mockActivity);
		// Assert:
		verify(mockActivity).finish();
		verifyNoMoreInteractions(mockActivity);
	}

	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	@Test public void testFinishCallerAtLollipopApiLevel() {
		// Arrange:
		final Activity mockActivity = mock(TestActivity.class);
		final BaseNavigationalTransition navTransition = new TestTransition();
		// Act:
		navTransition.finishCaller(mockActivity);
		// Assert:
		verify(mockActivity).finishAfterTransition();
		verifyNoMoreInteractions(mockActivity);
	}

	@Config(sdk = Build.VERSION_CODES.KITKAT)
	@Test public void testOnFinishCallerAtKitKatApiLevel() {
		// Arrange:
		final Activity mockActivity = mock(TestActivity.class);
		final BaseNavigationalTransition navTransition = new TestTransition();
		// Act:
		navTransition.onFinishCaller(mockActivity);
		// Assert:
		verify(mockActivity).finish();
		verifyNoMoreInteractions(mockActivity);
	}

	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	@Test public void testOnFinishCallerAtLollipopApiLevel() {
		// Arrange:
		final Activity mockActivity = mock(TestActivity.class);
		final BaseNavigationalTransition navTransition = new TestTransition();
		// Act:
		navTransition.onFinishCaller(mockActivity);
		// Assert:
		verify(mockActivity).finishAfterTransition();
		verifyNoMoreInteractions(mockActivity);
	}

	@Config(sdk = Build.VERSION_CODES.KITKAT)
	@Test public void testConfigureTransitionsAtKitKatApiLevel() {
		// Arrange:
		final Activity mockActivity = mock(TestActivity.class);
		final Window mockWindow = mock(Window.class);
		when(mockWindow.getDecorView()).thenReturn(null);
		when(mockActivity.getWindow()).thenReturn(mockWindow);
		final BaseNavigationalTransition navTransition = new TestTransition()
				.enterTransition(null)
				.returnTransition(null)
				.reenterTransition(null)
				.exitTransition(null)
				.allowEnterTransitionOverlap(true)
				.allowReturnTransitionOverlap(true)
				.sharedElementEnterTransition(null)
				.sharedElementReturnTransition(null)
				.sharedElementReenterTransition(null)
				.sharedElementExitTransition(null)
				.sharedElementsUseOverlay(true);
		// Act:
		navTransition.configureTransitions(mockActivity);
		// Assert:
		verifyZeroInteractions(mockWindow);
	}
	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	@Test public void testConfigureTransitionsAtLollipopApiLevel() {
		// Arrange:
		final Activity mockActivity = mock(TestActivity.class);
		final Window mockWindow = mock(Window.class);
		when(mockWindow.getDecorView()).thenReturn(null);
		when(mockActivity.getWindow()).thenReturn(mockWindow);
		final Transition enterTransition = new Fade();
		final Transition returnTransition = new Fade();
		final Transition reenterTransition = new Fade();
		final Transition exitTransition = new Fade();
		final Transition sharedElementEnterTransition = new Fade();
		final Transition sharedElementReturnTransition = new Fade();
		final Transition sharedElementReenterTransition = new Fade();
		final Transition sharedElementExitTransition = new Fade();
		final BaseNavigationalTransition navTransition = new TestTransition()
				.enterTransition(enterTransition)
				.returnTransition(returnTransition)
				.reenterTransition(reenterTransition)
				.exitTransition(exitTransition)
				.allowEnterTransitionOverlap(true)
				.allowReturnTransitionOverlap(true)
				.sharedElementEnterTransition(sharedElementEnterTransition)
				.sharedElementReturnTransition(sharedElementReturnTransition)
				.sharedElementReenterTransition(sharedElementReenterTransition)
				.sharedElementExitTransition(sharedElementExitTransition)
				.sharedElementsUseOverlay(true);
		// Act:
		navTransition.configureTransitions(mockActivity);
		// Assert:
		verify(mockWindow).setEnterTransition(enterTransition);
		verify(mockWindow).setReturnTransition(returnTransition);
		verify(mockWindow).setReenterTransition(reenterTransition);
		verify(mockWindow).setExitTransition(exitTransition);
		verify(mockWindow).setSharedElementEnterTransition(sharedElementEnterTransition);
		verify(mockWindow).setSharedElementReturnTransition(sharedElementReturnTransition);
		verify(mockWindow).setSharedElementReenterTransition(sharedElementReenterTransition);
		verify(mockWindow).setSharedElementExitTransition(sharedElementExitTransition);
		verify(mockWindow).setAllowEnterTransitionOverlap(true);
		verify(mockWindow).setAllowReturnTransitionOverlap(true);
		verify(mockWindow, times(2)).setSharedElementsUseOverlay(true);
	}

	@Test public void testConfigureTransitionsWithUnspecifiedTransitions() {
		// Arrange:
		final Activity mockActivity = mock(TestActivity.class);
		final Window mockWindow = mock(Window.class);
		when(mockWindow.getDecorView()).thenReturn(null);
		when(mockActivity.getWindow()).thenReturn(mockWindow);
		final BaseNavigationalTransition navTransition = new TestTransition();
		// Act:
		navTransition.configureTransitions(mockActivity);
		// Assert:
		verifyZeroInteractions(mockWindow);
	}

	@Config(sdk = Build.VERSION_CODES.KITKAT)
	@Test public void testConfigureIncomingTransitionsAtKitKatApiLevel() {
		// Arrange:
		final Activity mockActivity = mock(TestActivity.class);
		final Window mockWindow = mock(Window.class);
		when(mockWindow.getDecorView()).thenReturn(null);
		when(mockActivity.getWindow()).thenReturn(mockWindow);
		final BaseNavigationalTransition navTransition = new TestTransition()
				.enterTransition(null)
				.returnTransition(null)
				.sharedElementEnterTransition(null)
				.sharedElementReturnTransition(null)
				.allowEnterTransitionOverlap(true)
				.allowReturnTransitionOverlap(true)
				.sharedElementsUseOverlay(true);
		// Act:
		navTransition.configureIncomingTransitions(mockActivity);
		// Assert:
		verifyZeroInteractions(mockWindow);
	}

	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	@Test public void testConfigureIncomingTransitionsAtLollipopApiLevel() {
		// Arrange:
		final Activity mockActivity = mock(TestActivity.class);
		final Window mockWindow = mock(Window.class);
		when(mockWindow.getDecorView()).thenReturn(null);
		when(mockActivity.getWindow()).thenReturn(mockWindow);
		final Transition enterTransition = new Fade();
		final Transition returnTransition = new Fade();
		final Transition sharedElementEnterTransition = new Fade();
		final Transition sharedElementReturnTransition = new Fade();
		final BaseNavigationalTransition navTransition = new TestTransition()
				.enterTransition(enterTransition)
				.returnTransition(returnTransition)
				.sharedElementEnterTransition(sharedElementEnterTransition)
				.sharedElementReturnTransition(sharedElementReturnTransition)
				.allowEnterTransitionOverlap(true)
				.allowReturnTransitionOverlap(true)
				.sharedElementsUseOverlay(true);
		// Act:
		navTransition.configureIncomingTransitions(mockActivity);
		// Assert:
		verify(mockWindow).setEnterTransition(enterTransition);
		verify(mockWindow).setReturnTransition(returnTransition);
		verify(mockWindow).setSharedElementEnterTransition(sharedElementEnterTransition);
		verify(mockWindow).setSharedElementReturnTransition(sharedElementReturnTransition);
		verify(mockWindow).setAllowEnterTransitionOverlap(true);
		verify(mockWindow).setAllowReturnTransitionOverlap(true);
		verify(mockWindow).setSharedElementsUseOverlay(true);
		verifyNoMoreInteractions(mockWindow);
	}

	@Test public void testConfigureIncomingTransitionsWithUnspecifiedTransitions() {
		// Arrange:
		final Activity mockActivity = mock(TestActivity.class);
		final Window mockWindow = mock(Window.class);
		when(mockWindow.getDecorView()).thenReturn(null);
		when(mockActivity.getWindow()).thenReturn(mockWindow);
		final BaseNavigationalTransition navTransition = new TestTransition();
		// Act:
		navTransition.configureIncomingTransitions(mockActivity);
		// Assert:
		verifyZeroInteractions(mockWindow);
	}

	@Config(sdk = Build.VERSION_CODES.KITKAT)
	@Test public void testConfigureOutgoingTransitionsAtKitKatApiLevel() {
		// Arrange:
		final Activity mockActivity = mock(TestActivity.class);
		final Window mockWindow = mock(Window.class);
		when(mockWindow.getDecorView()).thenReturn(null);
		when(mockActivity.getWindow()).thenReturn(mockWindow);
		final BaseNavigationalTransition navTransition = new TestTransition()
				.reenterTransition(null)
				.exitTransition(null)
				.sharedElementReenterTransition(null)
				.sharedElementExitTransition(null)
				.sharedElementsUseOverlay(true);
		// Act:
		navTransition.configureOutgoingTransitions(mockActivity);
		// Assert:
		verifyZeroInteractions(mockWindow);
	}

	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	@Test public void testConfigureOutgoingTransitionsAtLollipopApiLevel() {
		// Arrange:
		final Activity mockActivity = mock(TestActivity.class);
		final Window mockWindow = mock(Window.class);
		when(mockWindow.getDecorView()).thenReturn(null);
		when(mockActivity.getWindow()).thenReturn(mockWindow);
		final Transition reenterTransition = new Fade();
		final Transition exitTransition = new Fade();
		final Transition sharedElementReenterTransition = new Fade();
		final Transition sharedElementExitTransition = new Fade();
		final BaseNavigationalTransition navTransition = new TestTransition()
				.reenterTransition(reenterTransition)
				.exitTransition(exitTransition)
				.sharedElementReenterTransition(sharedElementReenterTransition)
				.sharedElementExitTransition(sharedElementExitTransition)
				.sharedElementsUseOverlay(true);
		// Act:
		navTransition.configureOutgoingTransitions(mockActivity);
		// Assert:
		verify(mockWindow).setReenterTransition(reenterTransition);
		verify(mockWindow).setExitTransition(exitTransition);
		verify(mockWindow).setSharedElementReenterTransition(sharedElementReenterTransition);
		verify(mockWindow).setSharedElementExitTransition(sharedElementExitTransition);
		verify(mockWindow).setSharedElementsUseOverlay(true);
		verifyNoMoreInteractions(mockWindow);
	}

	@Test public void testConfigureOutgoingTransitionsWithUnspecifiedTransitions() {
		// Arrange:
		final Activity mockActivity = mock(TestActivity.class);
		final Window mockWindow = mock(Window.class);
		when(mockWindow.getDecorView()).thenReturn(null);
		when(mockActivity.getWindow()).thenReturn(mockWindow);
		final BaseNavigationalTransition navTransition = new TestTransition();
		// Act:
		navTransition.configureOutgoingTransitions(mockActivity);
		// Assert:
		verifyZeroInteractions(mockWindow);
	}

	@Config(sdk = Build.VERSION_CODES.KITKAT)
	@Test public void testMakeSceneTransitionAnimationAtKitKatApiLevel() {
		// Arrange:
		final Activity mockActivity = mock(TestActivity.class);
		final Window mockWindow = mock(Window.class);
		when(mockWindow.getDecorView()).thenReturn(null);
		when(mockActivity.getWindow()).thenReturn(mockWindow);
		final TestTransition transition = new TestTransition();
		// Act + Assert:
		assertThat(transition.makeSceneTransitionAnimation(mockActivity), is(nullValue()));
	}

	@SuppressWarnings("ConstantConditions")
	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	@Test public void testMakeSceneTransitionAnimationAtLollipopApiLevel() {
		// Arrange:
		final Activity mockActivity = mock(TestActivity.class);
		final Window mockWindow = mock(Window.class);
		when(mockWindow.getDecorView()).thenReturn(null);
		when(mockActivity.getWindow()).thenReturn(mockWindow);
		final TestTransition transition = new TestTransition();
		// Act:
		final ActivityOptions options = transition.makeSceneTransitionAnimation(mockActivity);
		// Assert:
		assertThat(options, is(notNullValue()));
		assertThat(options.toBundle(), is(nullValue()));
	}

	@Config(sdk = Build.VERSION_CODES.KITKAT)
	@Test public void testMakeSceneTransitionAnimationWithSharedElementsAtKitKatApiLevel() {
		// Arrange:
		final Activity mockActivity = mock(TestActivity.class);
		final Window mockWindow = mock(Window.class);
		when(mockWindow.getDecorView()).thenReturn(null);
		when(mockActivity.getWindow()).thenReturn(mockWindow);
		final View firstElement = new View(application);
		final View secondElement = new View(application);
		final TestTransition transition = new TestTransition();
		transition.sharedElements(
				new Pair<>(firstElement, "first_element"),
				new Pair<>(secondElement, "second_element")
		);
		// Act + Assert:
		assertThat(transition.makeSceneTransitionAnimation(mockActivity), is(nullValue()));
	}

	@SuppressWarnings("ConstantConditions")
	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	@Test public void testMakeSceneTransitionAnimationWithSharedElementsAtLollipopApiLevel() {
		// Arrange:
		final Activity mockActivity = mock(TestActivity.class);
		final Window mockWindow = mock(Window.class);
		when(mockWindow.getDecorView()).thenReturn(null);
		when(mockActivity.getWindow()).thenReturn(mockWindow);
		final View firstElement = new View(application);
		final View secondElement = new View(application);
		final TestTransition transition = new TestTransition();
		transition.sharedElements(
				new Pair<>(firstElement, "first_element"),
				new Pair<>(secondElement, "second_element")
		);
		// Act:
		final ActivityOptions options = transition.makeSceneTransitionAnimation(mockActivity);
		// Assert:
		assertThat(options, is(notNullValue()));
		assertThat(options.toBundle(), is(nullValue()));
	}

	@SuppressWarnings("ConstantConditions")
	@Config(sdk = Build.VERSION_CODES.KITKAT)
	@Test public void testMakeSceneTransitionAnimationWithEmptySharedElementsAtKitKatApiLevel() {
		// Arrange:
		final Activity mockActivity = mock(TestActivity.class);
		final Window mockWindow = mock(Window.class);
		when(mockWindow.getDecorView()).thenReturn(null);
		when(mockActivity.getWindow()).thenReturn(mockWindow);
		final View firstElement = new View(application);
		final View secondElement = new View(application);
		final TestTransition transition = new TestTransition();
		transition.sharedElements(
				new Pair<>(firstElement, "first_element"),
				new Pair<>(secondElement, "second_element")
		);
		transition.sharedElements().clear();
		// Act + Assert:
		assertThat(transition.makeSceneTransitionAnimation(mockActivity), is(nullValue()));
	}

	@SuppressWarnings("ConstantConditions")
	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	@Test public void testMakeSceneTransitionAnimationWithEmptySharedElementsAtLollipopApiLevel() {
		// Arrange:
		final Activity mockActivity = mock(TestActivity.class);
		final Window mockWindow = mock(Window.class);
		when(mockWindow.getDecorView()).thenReturn(null);
		when(mockActivity.getWindow()).thenReturn(mockWindow);
		final View firstElement = new View(application);
		final View secondElement = new View(application);
		final TestTransition transition = new TestTransition();
		transition.sharedElements(
				new Pair<>(firstElement, "first_element"),
				new Pair<>(secondElement, "second_element")
		);
		transition.sharedElements().clear();
		// Act:
		final ActivityOptions options = transition.makeSceneTransitionAnimation(mockActivity);
		// Assert:
		assertThat(options, is(notNullValue()));
		assertThat(options.toBundle(), is(nullValue()));
	}

	@SuppressWarnings("ConstantConditions")
	@Test public void testCreateIntent() {
		// Arrange:
		final Activity mockActivity = mock(TestActivity.class);
		when(mockActivity.getPackageName()).thenReturn(application.getPackageName());
		final BaseNavigationalTransition navTransition = new TestTransition(TestActivity.class);
		// Act:
		final Intent intent = navTransition.createIntent(mockActivity);
		// Assert:
		assertThat(intent, is(notNullValue()));
		final ComponentName component = intent.getComponent();
		assertThat(component, is(notNullValue()));
		assertThat(component.getClassName(), is(TestActivity.class.getName()));
		assertThat(component.getPackageName(), is(application.getPackageName()));
		assertThat(intent.getExtras(), is(nullValue()));
	}

	@SuppressWarnings("ConstantConditions")
	@Test public void testCreateIntentWithExtras() {
		// Arrange:
		final Activity mockActivity = mock(TestActivity.class);
		when(mockActivity.getPackageName()).thenReturn(application.getPackageName());
		final BaseNavigationalTransition navTransition = new TestTransition(TestActivity.class);
		navTransition.intentExtras().putString("extra_string", "Extra text.");
		// Act:
		final Intent intent = navTransition.createIntent(mockActivity);
		// Assert:
		assertThat(intent, is(notNullValue()));
		final ComponentName component = intent.getComponent();
		assertThat(component, is(notNullValue()));
		assertThat(component.getClassName(), is(TestActivity.class.getName()));
		assertThat(component.getPackageName(), is(application.getPackageName()));
		final Bundle extras = intent.getExtras();
		assertThat(extras, is(notNullValue()));
		assertThat(extras.getString("extra_string"), is("Extra text."));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testCreateIntentWithoutSpecifiedActivityClass() {
		// Arrange:
		final BaseNavigationalTransition navTransition = new TestTransition();
		// Act:
		navTransition.createIntent(mock(TestActivity.class));
	}

	@Config(sdk = Build.VERSION_CODES.KITKAT)
	@Test public void testFinishAtKitKatApiLevel() {
		// Arrange:
		final Activity mockActivity = mock(TestActivity.class);
		final BaseNavigationalTransition navTransition = new TestTransition();
		// Act:
		navTransition.finish(mockActivity);
		// Assert:
		verify(mockActivity).finish();
		verifyNoMoreInteractions(mockActivity);
	}

	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	@Test public void testFinishAtLollipopApiLevel() {
		// Arrange:
		final Activity mockActivity = mock(TestActivity.class);
		final BaseNavigationalTransition navTransition = new TestTransition();
		// Act:
		navTransition.finish(mockActivity);
		// Assert:
		verify(mockActivity).finishAfterTransition();
		verifyNoMoreInteractions(mockActivity);
	}

	@Config(sdk = Build.VERSION_CODES.KITKAT)
	@Test public void testOnFinishAtKitKatApiLevel() {
		// Arrange:
		final Activity mockActivity = mock(TestActivity.class);
		final BaseNavigationalTransition navTransition = new TestTransition();
		// Act:
		navTransition.onFinish(mockActivity);
		// Assert:
		verify(mockActivity).finish();
		verifyNoMoreInteractions(mockActivity);
	}

	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	@Test public void testOnFinishAtLollipopApiLevel() {
		// Arrange:
		final Activity mockActivity = mock(TestActivity.class);
		final BaseNavigationalTransition navTransition = new TestTransition();
		// Act:
		navTransition.onFinish(mockActivity);
		// Assert:
		verify(mockActivity).finishAfterTransition();
		verifyNoMoreInteractions(mockActivity);
	}

	private static final class TestTransition extends BaseNavigationalTransition<TestTransition> {

		TestTransition() {
			super();
		}

		TestTransition(@NonNull final Class<? extends Activity> classOfTransitionActivity) {
			super(classOfTransitionActivity);
		}
	}
}