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

	@Test
	public void testInstantiation() {
		final TestTransition transition = new TestTransition();
		assertThat(transition.getActivityClass(), is(nullValue()));
	}

	@Test
	public void testInstantiationWithActivity() {
		final TestTransition transition = new TestTransition(TestActivity.class);
		assertSame(transition.getActivityClass(), TestActivity.class);
	}

	@Test
	public void testIntentExtras() {
		final TestTransition transition = new TestTransition();
		transition.intentExtras().putString("extra_text", "Extra text.");
		transition.intentExtras().putInt("extra_int", 1000);
		assertThat(transition.intentExtras().getString("extra_text"), is("Extra text."));
		assertThat(transition.intentExtras().getInt("extra_int"), is(1000));
	}

	@Test
	public void testIntentExtrasDefault() {
		final Bundle extras = new TestTransition().intentExtras();
		assertThat(extras, is(notNullValue()));
		assertThat(extras.isEmpty(), is(true));
	}

	@Test
	public void testRequestCode() {
		assertThat(new TestTransition().requestCode(50).requestCode(), is(50));
	}

	@Test
	public void testRequestCodeDefault() {
		assertThat(new TestTransition().requestCode(), is(TestTransition.RC_NONE));
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	public void testEnterTransition() {
		final Transition transition = new Fade();
		assertThat(new TestTransition().enterTransition(transition).enterTransition(), is(transition));
	}

	@Test
	public void testEnterTransitionDefault() {
		assertThat(new TestTransition().enterTransition(), is(nullValue()));
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	public void testReenterTransition() {
		final Transition transition = new Fade();
		assertThat(new TestTransition().reenterTransition(transition).reenterTransition(), is(transition));
	}

	@Test
	public void testReenterTransitionDefault() {
		assertThat(new TestTransition().reenterTransition(), is(nullValue()));
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	public void testReturnTransition() {
		final Transition transition = new Fade();
		assertThat(new TestTransition().returnTransition(transition).returnTransition(), is(transition));
	}

	@Test
	public void testReturnTransitionDefault() {
		assertThat(new TestTransition().returnTransition(), is(nullValue()));
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	public void testExitTransition() {
		final Transition transition = new Fade();
		assertThat(new TestTransition().exitTransition(transition).exitTransition(), is(transition));
	}

	@Test
	public void testExitTransitionDefault() {
		assertThat(new TestTransition().exitTransition(), is(nullValue()));
	}

	@Test
	public void testAllowEnterTransitionOverlap() {
		assertThat(new TestTransition().allowEnterTransitionOverlap(false).allowEnterTransitionOverlap(), is(false));
		assertThat(new TestTransition().allowEnterTransitionOverlap(true).allowEnterTransitionOverlap(), is(true));
	}

	@Test
	public void testAllowEnterTransitionOverlapDefault() {
		assertThat(new TestTransition().allowEnterTransitionOverlap(), is(true));
	}

	@Test
	public void testAllowReturnTransitionOverlap() {
		assertThat(new TestTransition().allowReturnTransitionOverlap(false).allowReturnTransitionOverlap(), is(false));
		assertThat(new TestTransition().allowReturnTransitionOverlap(true).allowReturnTransitionOverlap(), is(true));
	}

	@Test
	public void testAllowReturnTransitionOverlapDefault() {
		assertThat(new TestTransition().allowReturnTransitionOverlap(), is(true));
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	public void testSharedElementEnterTransition() {
		final Transition transition = new Fade();
		assertThat(new TestTransition().sharedElementEnterTransition(transition).sharedElementEnterTransition(), is(transition));
	}

	@Test
	public void testSharedElementEnterTransitionDefault() {
		assertThat(new TestTransition().sharedElementEnterTransition(), is(nullValue()));
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	public void testSharedElementReenterTransition() {
		final Transition transition = new Fade();
		assertThat(new TestTransition().sharedElementReenterTransition(transition).sharedElementReenterTransition(), is(transition));
	}

	@Test
	public void testSharedElementReenterTransitionDefault() {
		assertThat(new TestTransition().sharedElementReenterTransition(), is(nullValue()));
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	public void testSharedElementReturnTransition() {
		final Transition transition = new Fade();
		assertThat(new TestTransition().sharedElementReturnTransition(transition).sharedElementReturnTransition(), is(transition));
	}

	@Test
	public void testSharedElementReturnTransitionDefault() {
		assertThat(new TestTransition().sharedElementReturnTransition(), is(nullValue()));
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	public void testSharedElementExitTransition() {
		final Transition transition = new Fade();
		assertThat(new TestTransition().sharedElementExitTransition(transition).sharedElementExitTransition(), is(transition));
	}

	@Test
	public void testSharedElementExitTransitionDefault() {
		assertThat(new TestTransition().sharedElementExitTransition(), is(nullValue()));
	}

	@Test
	public void testSharedElementUseOverlay() {
		assertThat(new TestTransition().sharedElementsUseOverlay(false).sharedElementsUseOverlay(), is(false));
		assertThat(new TestTransition().sharedElementsUseOverlay(true).sharedElementsUseOverlay(), is(true));
	}

	@Test
	public void testSharedElementUseOverlayDefault() {
		assertThat(new TestTransition().sharedElementsUseOverlay(), is(true));
	}

	@Test
	@SuppressWarnings("ConstantConditions")
	public void testSharedElements() {
		final TestTransition transition = new TestTransition();
		final View firstElement = new View(mApplication);
		final View secondElement = new View(mApplication);
		final View thirdElement = new View(mApplication);
		transition.sharedElements(
				new Pair<>(firstElement, "first_element"),
				new Pair<>(secondElement, "second_element")
		);
		transition.sharedElements(new Pair<>(thirdElement, "third_element"));
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

	@Test
	public void testSharedElementsDefault() {
		assertThat(new TestTransition().sharedElements(), is(nullValue()));
	}

	@Test
	@SuppressWarnings("ConstantConditions")
	public void testSharedElement() {
		final TestTransition transition = new TestTransition();
		final View firstElement = new View(mApplication);
		final View secondElement = new View(mApplication);
		final View thirdElement = new View(mApplication);
		transition.sharedElement(firstElement, "first_element");
		transition.sharedElement(secondElement, "second_element");
		transition.sharedElement(thirdElement, "third_element");
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

	@Test
	@SuppressWarnings("ConstantConditions")
	public void testSingleSharedElement() {
		final TestTransition transition = new TestTransition();
		final View firstElement = new View(mApplication);
		transition.sharedElement(firstElement, "first_element");
		assertThat(transition.singleSharedElement(), is(notNullValue()));
		assertThat(transition.singleSharedElement().first, is(firstElement));
		assertThat(transition.singleSharedElement().second, is("first_element"));
		final View secondElement = new View(mApplication);
		transition.sharedElement(secondElement, "second_element");
		assertThat(transition.singleSharedElement(), is(notNullValue()));
		assertThat(transition.singleSharedElement().first, is(firstElement));
		assertThat(transition.singleSharedElement().second, is("first_element"));
	}

	@Test
	@SuppressWarnings("ConstantConditions")
	public void testSingleSharedElementOnEmptySharedElements() {
		final TestTransition transition = new TestTransition();
		final View firstElement = new View(mApplication);
		transition.sharedElement(firstElement, "first_element");
		transition.sharedElements().clear();
		assertThat(transition.singleSharedElement(), is(nullValue()));
	}

	@Test
	public void testSingleSharedElementDefault() {
		assertThat(new TestTransition().singleSharedElement(), is(nullValue()));
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.KITKAT)
	public void testStartAtKitKatApiLevel() {
		final Activity mockActivity = mock(TestActivity.class);
		final Window mockWindow = mock(Window.class);
		when(mockActivity.getWindow()).thenReturn(mockWindow);
		new TestTransition(SecondaryTestActivity.class).start(mockActivity);
		verify(mockActivity, times(1)).startActivity(any(Intent.class));
		verify(mockActivity, times(0)).startActivityForResult(any(Intent.class), anyInt());
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	public void testStartAtLollipopApiLevel() {
		final Activity mockActivity = mock(TestActivity.class);
		final Window mockWindow = mock(Window.class);
		when(mockActivity.getWindow()).thenReturn(mockWindow);
		final Transition transition = new Fade();
		new TestTransition(SecondaryTestActivity.class).exitTransition(transition).start(mockActivity);
		verify(mockWindow, times(1)).hasFeature(WINDOW_FEATURE_ACTIVITY_TRANSITIONS);
		verify(mockWindow, times(1)).setExitTransition(transition);
		verifyNoMoreInteractions(mockWindow);
		verify(mockActivity, times(1)).startActivity(any(Intent.class), (Bundle) isNull());
		verify(mockActivity, times(0)).startActivityForResult(any(Intent.class), anyInt(), any(Bundle.class));
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.KITKAT)
	public void testOnStartAtKitKatApiLevel() {
		final Activity mockActivity = mock(TestActivity.class);
		final Window mockWindow = mock(Window.class);
		when(mockActivity.getWindow()).thenReturn(mockWindow);
		new TestTransition(SecondaryTestActivity.class).onStart(mockActivity);
		verify(mockActivity, times(1)).startActivity(any(Intent.class));
		verify(mockActivity, times(0)).startActivityForResult(any(Intent.class), anyInt());
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	public void testOnStartAtLollipopApiLevel() {
		final Activity mockActivity = mock(TestActivity.class);
		final Window mockWindow = mock(Window.class);
		when(mockActivity.getWindow()).thenReturn(mockWindow);
		new TestTransition(SecondaryTestActivity.class).onStart(mockActivity);
		verify(mockWindow, times(1)).hasFeature(WINDOW_FEATURE_ACTIVITY_TRANSITIONS);
		verifyNoMoreInteractions(mockWindow);
		verify(mockActivity, times(1)).startActivity(any(Intent.class), (Bundle) isNull());
		verify(mockActivity, times(0)).startActivityForResult(any(Intent.class), anyInt(), any(Bundle.class));
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.KITKAT)
	public void testOnStartWithRequestCodeAtKitKatApiLevel() {
		final Activity mockActivity = mock(TestActivity.class);
		final Window mockWindow = mock(Window.class);
		when(mockActivity.getWindow()).thenReturn(mockWindow);
		new TestTransition(SecondaryTestActivity.class).requestCode(150).onStart(mockActivity);
		verify(mockActivity, times(1)).startActivityForResult(any(Intent.class), eq(150));
		verify(mockActivity, times(0)).startActivity(any(Intent.class));
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	public void testOnStartWithRequestCodeAtLollipopApiLevel() {
		final Activity mockActivity = mock(TestActivity.class);
		final Window mockWindow = mock(Window.class);
		when(mockActivity.getWindow()).thenReturn(mockWindow);
		new TestTransition(SecondaryTestActivity.class).requestCode(150).onStart(mockActivity);
		verify(mockWindow, times(1)).hasFeature(WINDOW_FEATURE_ACTIVITY_TRANSITIONS);
		verifyNoMoreInteractions(mockWindow);
		verify(mockActivity, times(1)).startActivityForResult(any(Intent.class), eq(150), (Bundle) isNull());
		verify(mockActivity, times(0)).startActivity(any(Intent.class), any(Bundle.class));
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.JELLY_BEAN)
	public void testFinishCallerDelayedAtJellyBeanApiLevel() {
		final Activity mockActivity = mock(TestActivity.class);
		final Window mockWindow = mock(Window.class);
		final View decorView = new FrameLayout(mApplication) {

			@Override
			public boolean postDelayed(Runnable action, long delayMillis) {
				waitFor(delayMillis);
				action.run();
				return true;
			}
		};
		when(mockWindow.getDecorView()).thenReturn(decorView);
		when(mockActivity.getWindow()).thenReturn(mockWindow);
		when(mockActivity.isFinishing()).thenReturn(false);
		new TestTransition().finishCallerDelayed(mockActivity, 100);
		verify(mockActivity, times(1)).getWindow();
		verify(mockWindow, times(1)).getDecorView();
		verifyNoMoreInteractions(mockWindow);
		verify(mockActivity, times(1)).finish();
		verify(mockActivity, times(1)).isFinishing();
		verifyNoMoreInteractions(mockActivity);
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.JELLY_BEAN_MR1)
	public void testFinishCallerDelayedOnJellyBeanMR1ApiLevel() {
		final Activity mockActivity = mock(TestActivity.class);
		final Window mockWindow = mock(Window.class);
		final View decorView = new FrameLayout(mApplication) {

			@Override
			public boolean postDelayed(Runnable action, long delayMillis) {
				waitFor(delayMillis);
				action.run();
				return true;
			}
		};
		when(mockWindow.getDecorView()).thenReturn(decorView);
		when(mockActivity.getWindow()).thenReturn(mockWindow);
		when(mockActivity.isFinishing()).thenReturn(false);
		when(mockActivity.isDestroyed()).thenReturn(false);
		new TestTransition().finishCallerDelayed(mockActivity, 100);
		verify(mockActivity, times(1)).getWindow();
		verify(mockWindow, times(1)).getDecorView();
		verifyNoMoreInteractions(mockWindow);
		verify(mockActivity, times(1)).finish();
		verify(mockActivity, times(1)).isDestroyed();
		verify(mockActivity, times(1)).isFinishing();
		verifyNoMoreInteractions(mockActivity);
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	public void testFinishCallerDelayedAtLollipopApiLevel() {
		final Activity mockActivity = mock(TestActivity.class);
		final Window mockWindow = mock(Window.class);
		final View decorView = new FrameLayout(mApplication) {

			@Override
			public boolean postDelayed(Runnable action, long delayMillis) {
				waitFor(delayMillis);
				action.run();
				return true;
			}
		};
		when(mockWindow.getDecorView()).thenReturn(decorView);
		when(mockActivity.getWindow()).thenReturn(mockWindow);
		when(mockActivity.isFinishing()).thenReturn(false);
		when(mockActivity.isDestroyed()).thenReturn(false);
		new TestTransition().finishCallerDelayed(mockActivity, 100);
		verify(mockActivity, times(1)).getWindow();
		verify(mockWindow, times(1)).getDecorView();
		verifyNoMoreInteractions(mockWindow);
		verify(mockActivity, times(1)).finishAfterTransition();
		verify(mockActivity, times(1)).isDestroyed();
		verify(mockActivity, times(1)).isFinishing();
		verifyNoMoreInteractions(mockActivity);
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.JELLY_BEAN_MR2)
	public void testFinishCallerDelayedForDestroyedCaller() {
		final Activity mockActivity = mock(TestActivity.class);
		final Window mockWindow = mock(Window.class);
		final View decorView = new FrameLayout(mApplication) {

			@Override
			public boolean postDelayed(Runnable action, long delayMillis) {
				waitFor(delayMillis);
				action.run();
				return true;
			}
		};
		when(mockWindow.getDecorView()).thenReturn(decorView);
		when(mockActivity.getWindow()).thenReturn(mockWindow);
		when(mockActivity.isDestroyed()).thenReturn(true);
		new TestTransition().finishCallerDelayed(mockActivity, 100);
		verify(mockActivity, times(1)).getWindow();
		verify(mockWindow, times(1)).getDecorView();
		verifyNoMoreInteractions(mockWindow);
		verify(mockActivity, times(1)).isDestroyed();
		verifyNoMoreInteractions(mockActivity);
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.JELLY_BEAN)
	public void testFinishCallerDelayedForFinishingCallerAtJellyBeanApiLevel() {
		final Activity mockActivity = mock(TestActivity.class);
		final Window mockWindow = mock(Window.class);
		final View decorView = new FrameLayout(mApplication) {

			@Override
			public boolean postDelayed(Runnable action, long delayMillis) {
				waitFor(delayMillis);
				action.run();
				return true;
			}
		};
		when(mockWindow.getDecorView()).thenReturn(decorView);
		when(mockActivity.getWindow()).thenReturn(mockWindow);
		when(mockActivity.isFinishing()).thenReturn(true);
		new TestTransition().finishCallerDelayed(mockActivity, 100);
		verify(mockActivity, times(1)).getWindow();
		verify(mockWindow, times(1)).getDecorView();
		verifyNoMoreInteractions(mockWindow);
		verify(mockActivity, times(1)).isFinishing();
		verifyNoMoreInteractions(mockActivity);
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.JELLY_BEAN_MR1)
	public void testFinishCallerDelayedForFinishingCallerOnJellyBeanMR1ApiLevel() {
		final Activity mockActivity = mock(TestActivity.class);
		final Window mockWindow = mock(Window.class);
		final View decorView = new FrameLayout(mApplication) {

			@Override
			public boolean postDelayed(Runnable action, long delayMillis) {
				waitFor(delayMillis);
				action.run();
				return true;
			}
		};
		when(mockWindow.getDecorView()).thenReturn(decorView);
		when(mockActivity.getWindow()).thenReturn(mockWindow);
		when(mockActivity.isFinishing()).thenReturn(true);
		when(mockActivity.isDestroyed()).thenReturn(false);
		new TestTransition().finishCallerDelayed(mockActivity, 100);
		verify(mockActivity, times(1)).getWindow();
		verify(mockWindow, times(1)).getDecorView();
		verifyNoMoreInteractions(mockWindow);
		verify(mockActivity, times(1)).isDestroyed();
		verify(mockActivity, times(1)).isFinishing();
		verifyNoMoreInteractions(mockActivity);
	}

	@Test
	public void testFinishCallerDelayedWithoutDecorView() {
		final Activity mockActivity = mock(TestActivity.class);
		final Window mockWindow = mock(Window.class);
		when(mockWindow.getDecorView()).thenReturn(null);
		when(mockActivity.getWindow()).thenReturn(mockWindow);
		new TestTransition().finishCallerDelayed(mockActivity, 250);
		waitFor(250);
		verify(mockActivity, times(1)).getWindow();
		verifyNoMoreInteractions(mockActivity);
		verify(mockWindow, times(1)).getDecorView();
		verifyNoMoreInteractions(mockWindow);
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.KITKAT)
	public void testFinishCallerAtKitKatApiLevel() {
		final Activity mockActivity = mock(TestActivity.class);
		new TestTransition().finishCaller(mockActivity);
		verify(mockActivity, times(1)).finish();
		verifyNoMoreInteractions(mockActivity);
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	public void testFinishCallerAtLollipopApiLevel() {
		final Activity mockActivity = mock(TestActivity.class);
		new TestTransition().finishCaller(mockActivity);
		verify(mockActivity, times(1)).finishAfterTransition();
		verifyNoMoreInteractions(mockActivity);
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.KITKAT)
	public void testOnFinishCallerAtKitKatApiLevel() {
		final Activity mockActivity = mock(TestActivity.class);
		new TestTransition().onFinishCaller(mockActivity);
		verify(mockActivity, times(1)).finish();
		verifyNoMoreInteractions(mockActivity);
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	public void testOnFinishCallerAtLollipopApiLevel() {
		final Activity mockActivity = mock(TestActivity.class);
		new TestTransition().onFinishCaller(mockActivity);
		verify(mockActivity, times(1)).finishAfterTransition();
		verifyNoMoreInteractions(mockActivity);
	}

	@Test
	public void testConfigureTransitions() {
		final Activity mockActivity = mock(TestActivity.class);
		final Window mockWindow = mock(Window.class);
		when(mockWindow.getDecorView()).thenReturn(null);
		when(mockActivity.getWindow()).thenReturn(mockWindow);
		new TestTransition().configureTransitions(mockActivity);
	}

	@Test
	public void testConfigureTransitionsWithUnspecifiedTransitions() {
		final Activity mockActivity = mock(TestActivity.class);
		final Window mockWindow = mock(Window.class);
		when(mockWindow.getDecorView()).thenReturn(null);
		when(mockActivity.getWindow()).thenReturn(mockWindow);
		new TestTransition().configureTransitions(mockActivity);
		verifyZeroInteractions(mockWindow);
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.KITKAT)
	public void testConfigureIncomingTransitionsAtKitKatApiLevel() {
		final Activity mockActivity = mock(TestActivity.class);
		final Window mockWindow = mock(Window.class);
		when(mockWindow.getDecorView()).thenReturn(null);
		when(mockActivity.getWindow()).thenReturn(mockWindow);
		new TestTransition()
				.enterTransition(null)
				.returnTransition(null)
				.sharedElementEnterTransition(null)
				.sharedElementReturnTransition(null)
				.allowEnterTransitionOverlap(true)
				.allowReturnTransitionOverlap(true)
				.sharedElementsUseOverlay(true)
				.configureIncomingTransitions(mockActivity);
		verifyZeroInteractions(mockWindow);
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	public void testConfigureIncomingTransitionsAtLollipopApiLevel() {
		final Activity mockActivity = mock(TestActivity.class);
		final Window mockWindow = mock(Window.class);
		when(mockWindow.getDecorView()).thenReturn(null);
		when(mockActivity.getWindow()).thenReturn(mockWindow);
		final Transition enterTransition = new Fade();
		final Transition returnTransition = new Fade();
		final Transition sharedElementEnterTransition = new Fade();
		final Transition sharedElementReturnTransition = new Fade();
		new TestTransition()
				.enterTransition(enterTransition)
				.returnTransition(returnTransition)
				.sharedElementEnterTransition(sharedElementEnterTransition)
				.sharedElementReturnTransition(sharedElementReturnTransition)
				.allowEnterTransitionOverlap(true)
				.allowReturnTransitionOverlap(true)
				.sharedElementsUseOverlay(true)
				.configureIncomingTransitions(mockActivity);
		verify(mockWindow, times(1)).setEnterTransition(enterTransition);
		verify(mockWindow, times(1)).setReturnTransition(returnTransition);
		verify(mockWindow, times(1)).setSharedElementEnterTransition(sharedElementEnterTransition);
		verify(mockWindow, times(1)).setSharedElementReturnTransition(sharedElementReturnTransition);
		verify(mockWindow, times(1)).setAllowEnterTransitionOverlap(true);
		verify(mockWindow, times(1)).setAllowReturnTransitionOverlap(true);
		verify(mockWindow, times(1)).setSharedElementsUseOverlay(true);
		verifyNoMoreInteractions(mockWindow);
	}

	@Test
	public void testConfigureIncomingTransitionsWithUnspecifiedTransitions() {
		final Activity mockActivity = mock(TestActivity.class);
		final Window mockWindow = mock(Window.class);
		when(mockWindow.getDecorView()).thenReturn(null);
		when(mockActivity.getWindow()).thenReturn(mockWindow);
		new TestTransition().configureIncomingTransitions(mockActivity);
		verifyZeroInteractions(mockWindow);
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.KITKAT)
	public void testConfigureOutgoingTransitionsAtKitKatApiLevel() {
		final Activity mockActivity = mock(TestActivity.class);
		final Window mockWindow = mock(Window.class);
		when(mockWindow.getDecorView()).thenReturn(null);
		when(mockActivity.getWindow()).thenReturn(mockWindow);
		new TestTransition()
				.reenterTransition(null)
				.exitTransition(null)
				.sharedElementReenterTransition(null)
				.sharedElementExitTransition(null)
				.sharedElementsUseOverlay(true)
				.configureOutgoingTransitions(mockActivity);
		verifyZeroInteractions(mockWindow);
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	public void testConfigureOutgoingTransitionsAtLollipopApiLevel() {
		final Activity mockActivity = mock(TestActivity.class);
		final Window mockWindow = mock(Window.class);
		when(mockWindow.getDecorView()).thenReturn(null);
		when(mockActivity.getWindow()).thenReturn(mockWindow);
		final Transition reenterTransition = new Fade();
		final Transition exitTransition = new Fade();
		final Transition sharedElementReenterTransition = new Fade();
		final Transition sharedElementExitTransition = new Fade();
		new TestTransition()
				.reenterTransition(reenterTransition)
				.exitTransition(exitTransition)
				.sharedElementReenterTransition(sharedElementReenterTransition)
				.sharedElementExitTransition(sharedElementExitTransition)
				.sharedElementsUseOverlay(true)
				.configureOutgoingTransitions(mockActivity);
		verify(mockWindow, times(1)).setReenterTransition(reenterTransition);
		verify(mockWindow, times(1)).setExitTransition(exitTransition);
		verify(mockWindow, times(1)).setSharedElementReenterTransition(sharedElementReenterTransition);
		verify(mockWindow, times(1)).setSharedElementExitTransition(sharedElementExitTransition);
		verify(mockWindow, times(1)).setSharedElementsUseOverlay(true);
		verifyNoMoreInteractions(mockWindow);
	}

	@Test
	public void testConfigureOutgoingTransitionsWithUnspecifiedTransitions() {
		final Activity mockActivity = mock(TestActivity.class);
		final Window mockWindow = mock(Window.class);
		when(mockWindow.getDecorView()).thenReturn(null);
		when(mockActivity.getWindow()).thenReturn(mockWindow);
		new TestTransition().configureOutgoingTransitions(mockActivity);
		verifyZeroInteractions(mockWindow);
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.KITKAT)
	public void testMakeSceneTransitionAnimationAtKitKatApiLevel() {
		final Activity mockActivity = mock(TestActivity.class);
		final Window mockWindow = mock(Window.class);
		when(mockWindow.getDecorView()).thenReturn(null);
		when(mockActivity.getWindow()).thenReturn(mockWindow);
		final TestTransition transition = new TestTransition();
		assertThat(transition.makeSceneTransitionAnimation(mockActivity), is(nullValue()));
	}

	@Test
	@SuppressWarnings("ConstantConditions")
	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	public void testMakeSceneTransitionAnimationAtLollipopApiLevel() {
		final Activity mockActivity = mock(TestActivity.class);
		final Window mockWindow = mock(Window.class);
		when(mockWindow.getDecorView()).thenReturn(null);
		when(mockActivity.getWindow()).thenReturn(mockWindow);
		final TestTransition transition = new TestTransition();
		final ActivityOptions options = transition.makeSceneTransitionAnimation(mockActivity);
		assertThat(options, is(notNullValue()));
		assertThat(options.toBundle(), is(nullValue()));
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.KITKAT)
	public void testMakeSceneTransitionAnimationWithSharedElementsAtKitKatApiLevel() {
		final Activity mockActivity = mock(TestActivity.class);
		final Window mockWindow = mock(Window.class);
		when(mockWindow.getDecorView()).thenReturn(null);
		when(mockActivity.getWindow()).thenReturn(mockWindow);
		final View firstElement = new View(mApplication);
		final View secondElement = new View(mApplication);
		final TestTransition transition = new TestTransition();
		transition.sharedElements(
				new Pair<>(firstElement, "first_element"),
				new Pair<>(secondElement, "second_element")
		);
		assertThat(transition.makeSceneTransitionAnimation(mockActivity), is(nullValue()));
	}

	@Test
	@SuppressWarnings("ConstantConditions")
	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	public void testMakeSceneTransitionAnimationWithSharedElementsAtLollipopApiLevel() {
		final Activity mockActivity = mock(TestActivity.class);
		final Window mockWindow = mock(Window.class);
		when(mockWindow.getDecorView()).thenReturn(null);
		when(mockActivity.getWindow()).thenReturn(mockWindow);
		final View firstElement = new View(mApplication);
		final View secondElement = new View(mApplication);
		final TestTransition transition = new TestTransition();
		transition.sharedElements(
				new Pair<>(firstElement, "first_element"),
				new Pair<>(secondElement, "second_element")
		);
		final ActivityOptions options = transition.makeSceneTransitionAnimation(mockActivity);
		assertThat(options, is(notNullValue()));
		assertThat(options.toBundle(), is(nullValue()));
	}

	@Test
	@SuppressWarnings("ConstantConditions")
	@Config(sdk = Build.VERSION_CODES.KITKAT)
	public void testMakeSceneTransitionAnimationWithEmptySharedElementsAtKitKatApiLevel() {
		final Activity mockActivity = mock(TestActivity.class);
		final Window mockWindow = mock(Window.class);
		when(mockWindow.getDecorView()).thenReturn(null);
		when(mockActivity.getWindow()).thenReturn(mockWindow);
		final View firstElement = new View(mApplication);
		final View secondElement = new View(mApplication);
		final TestTransition transition = new TestTransition();
		transition.sharedElements(
				new Pair<>(firstElement, "first_element"),
				new Pair<>(secondElement, "second_element")
		);
		transition.sharedElements().clear();
		assertThat(transition.makeSceneTransitionAnimation(mockActivity), is(nullValue()));
	}

	@Test
	@SuppressWarnings("ConstantConditions")
	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	public void testMakeSceneTransitionAnimationWithEmptySharedElementsAtLollipopApiLevel() {
		final Activity mockActivity = mock(TestActivity.class);
		final Window mockWindow = mock(Window.class);
		when(mockWindow.getDecorView()).thenReturn(null);
		when(mockActivity.getWindow()).thenReturn(mockWindow);
		final View firstElement = new View(mApplication);
		final View secondElement = new View(mApplication);
		final TestTransition transition = new TestTransition();
		transition.sharedElements(
				new Pair<>(firstElement, "first_element"),
				new Pair<>(secondElement, "second_element")
		);
		transition.sharedElements().clear();
		final ActivityOptions options = transition.makeSceneTransitionAnimation(mockActivity);
		assertThat(options, is(notNullValue()));
		assertThat(options.toBundle(), is(nullValue()));
	}

	@Test
	@SuppressWarnings("ConstantConditions")
	public void testCreateIntent() {
		final Activity mockActivity = mock(TestActivity.class);
		when(mockActivity.getPackageName()).thenReturn(mApplication.getPackageName());
		final Intent intent = new TestTransition(TestActivity.class).createIntent(mockActivity);
		assertThat(intent, is(notNullValue()));
		final ComponentName component = intent.getComponent();
		assertThat(component, is(notNullValue()));
		assertThat(component.getClassName(), is(TestActivity.class.getName()));
		assertThat(component.getPackageName(), is(mApplication.getPackageName()));
		assertThat(intent.getExtras(), is(nullValue()));
	}

	@Test
	@SuppressWarnings("ConstantConditions")
	public void testCreateIntentWithExtras() {
		final Activity mockActivity = mock(TestActivity.class);
		when(mockActivity.getPackageName()).thenReturn(mApplication.getPackageName());
		final TestTransition transition = new TestTransition(TestActivity.class);
		transition.intentExtras().putString("extra_string", "Extra text.");
		final Intent intent = transition.createIntent(mockActivity);
		assertThat(intent, is(notNullValue()));
		final ComponentName component = intent.getComponent();
		assertThat(component, is(notNullValue()));
		assertThat(component.getClassName(), is(TestActivity.class.getName()));
		assertThat(component.getPackageName(), is(mApplication.getPackageName()));
		final Bundle extras = intent.getExtras();
		assertThat(extras, is(notNullValue()));
		assertThat(extras.getString("extra_string"), is("Extra text."));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testCreateIntentWithoutSpecifiedActivityClass() {
		new TestTransition().createIntent(mock(TestActivity.class));
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.KITKAT)
	public void testFinishAtKitKatApiLevel() {
		final Activity mockActivity = mock(TestActivity.class);
		new TestTransition().finish(mockActivity);
		verify(mockActivity, times(1)).finish();
		verifyNoMoreInteractions(mockActivity);
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	public void testFinishAtLollipopApiLevel() {
		final Activity mockActivity = mock(TestActivity.class);
		new TestTransition().finish(mockActivity);
		verify(mockActivity, times(1)).finishAfterTransition();
		verifyNoMoreInteractions(mockActivity);
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.KITKAT)
	public void testOnFinishAtKitKatApiLevel() {
		final Activity mockActivity = mock(TestActivity.class);
		new TestTransition().onFinish(mockActivity);
		verify(mockActivity, times(1)).finish();
		verifyNoMoreInteractions(mockActivity);
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	public void testOnFinishAtLollipopApiLevel() {
		final Activity mockActivity = mock(TestActivity.class);
		new TestTransition().onFinish(mockActivity);
		verify(mockActivity, times(1)).finishAfterTransition();
		verifyNoMoreInteractions(mockActivity);
	}

	private static final class TestTransition extends BaseNavigationalTransition<TestTransition> {

		TestTransition() {
			super();
		}

		TestTransition(@NonNull Class<? extends Activity> classOfTransitionActivity) {
			super(classOfTransitionActivity);
		}
	}
}
