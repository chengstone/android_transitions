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
package universum.studios.android.transition.util;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.os.Build;
import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import universum.studios.android.test.instrumented.InstrumentedTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = Build.VERSION_CODES.HONEYCOMB)
public final class AnimatorWrapperTest extends InstrumentedTestCase {

	@SuppressWarnings("unused")
	private static final String TAG = "AnimatorWrapperTest";

	@Test
	public void testInstantiation() {
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		assertThat(wrapper.getWrappedAnimator(), is(mockAnimator));
		assertThat(wrapper.hasFeature(AnimatorWrapper.ALL), is(true));
	}

	@Test
	public void testRequestFeatures() {
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		wrapper.requestFeatures(0);
		assertThat(wrapper.hasFeature(AnimatorWrapper.ALL), is(false));
		wrapper.requestFeatures(AnimatorWrapper.START);
		assertThat(wrapper.hasFeature(AnimatorWrapper.START), is(true));
		assertThat(wrapper.hasFeature(AnimatorWrapper.PAUSE | AnimatorWrapper.RESUME | AnimatorWrapper.END | AnimatorWrapper.CANCEL), is(false));
		wrapper.requestFeatures(AnimatorWrapper.PAUSE);
		assertThat(wrapper.hasFeature(AnimatorWrapper.PAUSE), is(true));
		assertThat(wrapper.hasFeature(AnimatorWrapper.START | AnimatorWrapper.RESUME | AnimatorWrapper.END | AnimatorWrapper.CANCEL), is(false));
		wrapper.requestFeatures(AnimatorWrapper.RESUME);
		assertThat(wrapper.hasFeature(AnimatorWrapper.RESUME), is(true));
		assertThat(wrapper.hasFeature(AnimatorWrapper.START | AnimatorWrapper.PAUSE | AnimatorWrapper.END | AnimatorWrapper.CANCEL), is(false));
		wrapper.requestFeatures(AnimatorWrapper.END);
		assertThat(wrapper.hasFeature(AnimatorWrapper.END), is(true));
		assertThat(wrapper.hasFeature(AnimatorWrapper.START | AnimatorWrapper.PAUSE | AnimatorWrapper.RESUME | AnimatorWrapper.CANCEL), is(false));
		wrapper.requestFeatures(AnimatorWrapper.CANCEL);
		assertThat(wrapper.hasFeature(AnimatorWrapper.CANCEL), is(true));
		assertThat(wrapper.hasFeature(AnimatorWrapper.START | AnimatorWrapper.PAUSE | AnimatorWrapper.RESUME | AnimatorWrapper.END), is(false));
	}

	@Test
	public void testRequestFeature() {
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		wrapper.requestFeatures(0);
		wrapper.requestFeature(AnimatorWrapper.START);
		assertThat(wrapper.hasFeature(AnimatorWrapper.START), is(true));
		assertThat(wrapper.hasFeature(AnimatorWrapper.PAUSE | AnimatorWrapper.RESUME | AnimatorWrapper.END | AnimatorWrapper.CANCEL), is(false));
		wrapper.requestFeature(AnimatorWrapper.PAUSE);
		assertThat(wrapper.hasFeature(AnimatorWrapper.START | AnimatorWrapper.PAUSE), is(true));
		assertThat(wrapper.hasFeature(AnimatorWrapper.RESUME | AnimatorWrapper.END | AnimatorWrapper.CANCEL), is(false));
		wrapper.requestFeature(AnimatorWrapper.RESUME);
		assertThat(wrapper.hasFeature(AnimatorWrapper.START | AnimatorWrapper.PAUSE | AnimatorWrapper.RESUME), is(true));
		assertThat(wrapper.hasFeature(AnimatorWrapper.END | AnimatorWrapper.CANCEL), is(false));
		wrapper.requestFeature(AnimatorWrapper.END);
		assertThat(wrapper.hasFeature(AnimatorWrapper.END | AnimatorWrapper.START | AnimatorWrapper.PAUSE | AnimatorWrapper.RESUME), is(true));
		assertThat(wrapper.hasFeature(AnimatorWrapper.CANCEL), is(false));
		wrapper.requestFeature(AnimatorWrapper.CANCEL);
		assertThat(wrapper.hasFeature(AnimatorWrapper.ALL), is(true));
	}

	@Test
	public void testRemoveFeature() {
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		wrapper.requestFeatures(AnimatorWrapper.ALL);
		wrapper.removeFeature(AnimatorWrapper.START);
		assertThat(wrapper.hasFeature(AnimatorWrapper.START), is(false));
		assertThat(wrapper.hasFeature(AnimatorWrapper.PAUSE | AnimatorWrapper.RESUME | AnimatorWrapper.END | AnimatorWrapper.CANCEL), is(true));
		wrapper.removeFeature(AnimatorWrapper.PAUSE);
		assertThat(wrapper.hasFeature(AnimatorWrapper.START | AnimatorWrapper.PAUSE), is(false));
		assertThat(wrapper.hasFeature(AnimatorWrapper.RESUME | AnimatorWrapper.END | AnimatorWrapper.CANCEL), is(true));
		wrapper.removeFeature(AnimatorWrapper.RESUME);
		assertThat(wrapper.hasFeature(AnimatorWrapper.START | AnimatorWrapper.PAUSE | AnimatorWrapper.RESUME), is(false));
		assertThat(wrapper.hasFeature(AnimatorWrapper.END | AnimatorWrapper.CANCEL), is(true));
		wrapper.removeFeature(AnimatorWrapper.END);
		assertThat(wrapper.hasFeature(AnimatorWrapper.END | AnimatorWrapper.START | AnimatorWrapper.PAUSE | AnimatorWrapper.RESUME), is(false));
		assertThat(wrapper.hasFeature(AnimatorWrapper.CANCEL), is(true));
		wrapper.removeFeature(AnimatorWrapper.CANCEL);
		assertThat(wrapper.hasFeature(AnimatorWrapper.ALL), is(false));
	}

	@Test
	public void testAddRemoveListener() {
		final Animator mockAnimator = mock(Animator.class);
		final Animator.AnimatorListener mockListener = mock(Animator.AnimatorListener.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		wrapper.addListener(mockListener);
		wrapper.addListener(mockListener);
		wrapper.addListener(mockListener);
		assertThat(wrapper.getListeners(), is(notNullValue()));
		assertThat(wrapper.getListeners().size(), is(1));
		wrapper.removeListener(mockListener);
		wrapper.removeListener(mockListener);
		verify(mockAnimator, times(1)).addListener(any(Animator.AnimatorListener.class));
		verify(mockAnimator, times(0)).addListener(mockListener);
		verify(mockAnimator, times(1)).removeListener(any(Animator.AnimatorListener.class));
		verify(mockAnimator, times(0)).removeListener(mockListener);
		verifyNoMoreInteractions(mockAnimator);
		assertThat(wrapper.getListeners(), is(notNullValue()));
		assertThat(wrapper.getListeners().isEmpty(), is(true));
	}

	@Test
	@SdkSuppress(minSdkVersion = Build.VERSION_CODES.KITKAT)
	public void testAddRemovePauseListener() {
		final Animator mockAnimator = mock(Animator.class);
		final Animator.AnimatorPauseListener mockPauseListener = mock(Animator.AnimatorPauseListener.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		wrapper.addPauseListener(mockPauseListener);
		wrapper.addPauseListener(mockPauseListener);
		wrapper.addPauseListener(mockPauseListener);
		wrapper.removePauseListener(mockPauseListener);
		wrapper.removePauseListener(mockPauseListener);
		verify(mockAnimator, times(1)).addPauseListener(any(Animator.AnimatorPauseListener.class));
		verify(mockAnimator, times(0)).addPauseListener(mockPauseListener);
		verify(mockAnimator, times(1)).removePauseListener(any(Animator.AnimatorPauseListener.class));
		verify(mockAnimator, times(0)).removePauseListener(mockPauseListener);
		verifyNoMoreInteractions(mockAnimator);
	}

	@Test
	@SdkSuppress(minSdkVersion = Build.VERSION_CODES.KITKAT)
	public void testAddPauseListenerWithoutPauseFeature() {
		final Animator mockAnimator = mock(Animator.class);
		final Animator.AnimatorPauseListener mockPauseListener = mock(Animator.AnimatorPauseListener.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		wrapper.removeFeature(AnimatorWrapper.PAUSE);
		wrapper.addPauseListener(mockPauseListener);
		verify(mockAnimator, times(1)).addPauseListener(any(Animator.AnimatorPauseListener.class));
		verify(mockAnimator, times(0)).addPauseListener(mockPauseListener);
		verifyNoMoreInteractions(mockAnimator);
	}

	@Test
	@SdkSuppress(minSdkVersion = Build.VERSION_CODES.KITKAT)
	public void testAddPauseListenerWithoutResumeFeature() {
		final Animator mockAnimator = mock(Animator.class);
		final Animator.AnimatorPauseListener mockPauseListener = mock(Animator.AnimatorPauseListener.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		wrapper.removeFeature(AnimatorWrapper.RESUME);
		wrapper.addPauseListener(mockPauseListener);
		verify(mockAnimator, times(1)).addPauseListener(any(Animator.AnimatorPauseListener.class));
		verify(mockAnimator, times(0)).addPauseListener(mockPauseListener);
		verifyNoMoreInteractions(mockAnimator);
	}

	@Test
	@SdkSuppress(minSdkVersion = Build.VERSION_CODES.KITKAT)
	public void testAddPauseListenerWithoutPauseNorResumeFeature() {
		final Animator mockAnimator = mock(Animator.class);
		final Animator.AnimatorPauseListener mockPauseListener = mock(Animator.AnimatorPauseListener.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		wrapper.removeFeature(AnimatorWrapper.PAUSE | AnimatorWrapper.RESUME);
		wrapper.addPauseListener(mockPauseListener);
		verifyZeroInteractions(mockAnimator);
		assertThat(wrapper.getListeners(), is(notNullValue()));
		assertThat(wrapper.getListeners().isEmpty(), is(true));
	}

	@Test
	@SdkSuppress(minSdkVersion = Build.VERSION_CODES.KITKAT)
	public void testRemovePauseListenerNotRegistered() {
		final Animator mockAnimator = mock(Animator.class);
		final Animator.AnimatorPauseListener mockPauseListener = mock(Animator.AnimatorPauseListener.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		wrapper.removePauseListener(mockPauseListener);
		verifyZeroInteractions(mockAnimator);
	}

	@Test
	public void testGetListenersDefault() {
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		assertThat(wrapper.getListeners(), is(notNullValue()));
		assertThat(wrapper.getListeners().isEmpty(), is(true));
	}

	@Test
	@SdkSuppress(minSdkVersion = Build.VERSION_CODES.KITKAT)
	public void testRemoveAllListeners() {
		final Animator mockAnimator = mock(Animator.class);
		final Animator.AnimatorListener mockListener = mock(Animator.AnimatorListener.class);
		final Animator.AnimatorPauseListener mockPauseListener = mock(Animator.AnimatorPauseListener.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		wrapper.addListener(mockListener);
		wrapper.addPauseListener(mockPauseListener);
		wrapper.removeAllListeners();
		assertThat(wrapper.getListeners().isEmpty(), is(true));
		verify(mockAnimator, times(1)).removeAllListeners();
	}

	@Test
	public void testRemoveAllListenersWithoutRegisteredListeners() {
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		wrapper.removeAllListeners();
		verify(mockAnimator, times(1)).removeAllListeners();
	}

	@Test
	public void testSetStartDelay() {
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		wrapper.setStartDelay(150L);
		verify(mockAnimator, times(1)).setStartDelay(150L);
	}

	@Test
	public void testGetStartDelay() {
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		when(mockAnimator.getStartDelay()).thenReturn(250L);
		assertThat(wrapper.getStartDelay(), is(250L));
		verify(mockAnimator, times(1)).getStartDelay();
	}

	@Test
	public void testSetDuration() {
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		wrapper.setDuration(200L);
		verify(mockAnimator, times(1)).setDuration(200L);
	}

	@Test
	public void testGetDuration() {
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		when(mockAnimator.getDuration()).thenReturn(100L);
		assertThat(wrapper.getDuration(), is(100L));
		verify(mockAnimator, times(1)).getDuration();
	}

	@Test
	@SdkSuppress(minSdkVersion = Build.VERSION_CODES.JELLY_BEAN_MR2)
	public void testSetInterpolator() {
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		final TimeInterpolator mockInterpolator = mock(TimeInterpolator.class);
		wrapper.setInterpolator(mockInterpolator);
		verify(mockAnimator, times(1)).setInterpolator(mockInterpolator);
	}

	@Test
	@SdkSuppress(minSdkVersion = Build.VERSION_CODES.JELLY_BEAN_MR2)
	public void testGetInterpolator() {
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		final TimeInterpolator mockInterpolator = mock(TimeInterpolator.class);
		when(mockAnimator.getInterpolator()).thenReturn(mockInterpolator);
		assertThat(wrapper.getInterpolator(), is(mockInterpolator));
		verify(mockAnimator, times(1)).getInterpolator();
	}

	@Test
	public void testSetTarget() {
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		final Object target = new Object();
		wrapper.setTarget(target);
		verify(mockAnimator, times(1)).setTarget(target);
	}

	@Test
	public void testStart() {
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		wrapper.start();
		verify(mockAnimator, times(1)).start();
	}

	@Test
	public void testStartWithoutStartFeature() {
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		wrapper.removeFeature(AnimatorWrapper.START);
		wrapper.start();
		verifyZeroInteractions(mockAnimator);
	}

	@Test
	@SdkSuppress(minSdkVersion = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	public void testIsStarted() {
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		when(mockAnimator.isStarted()).thenReturn(true);
		assertThat(wrapper.isStarted(), is(true));
		verify(mockAnimator, times(1)).isStarted();
	}

	@Test
	@SdkSuppress(minSdkVersion = Build.VERSION_CODES.KITKAT)
	public void testPause() {
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		wrapper.pause();
		verify(mockAnimator, times(1)).pause();
	}

	@Test
	@SdkSuppress(minSdkVersion = Build.VERSION_CODES.KITKAT)
	public void testPauseWithoutPauseFeature() {
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		wrapper.removeFeature(AnimatorWrapper.PAUSE);
		wrapper.pause();
		verifyZeroInteractions(mockAnimator);
	}

	@Test
	@SdkSuppress(minSdkVersion = Build.VERSION_CODES.KITKAT)
	public void testIsPaused() {
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		when(mockAnimator.isPaused()).thenReturn(true);
		assertThat(wrapper.isPaused(), is(true));
		verify(mockAnimator, times(1)).isPaused();
	}

	@Test
	@SdkSuppress(minSdkVersion = Build.VERSION_CODES.KITKAT)
	public void testResume() {
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		wrapper.resume();
		verify(mockAnimator, times(1)).resume();
	}

	@Test
	@SdkSuppress(minSdkVersion = Build.VERSION_CODES.KITKAT)
	public void testResumeWithoutPauseFeature() {
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		wrapper.removeFeature(AnimatorWrapper.PAUSE);
		wrapper.resume();
		verify(mockAnimator, times(1)).resume();
	}

	@Test
	@SdkSuppress(minSdkVersion = Build.VERSION_CODES.KITKAT)
	public void testResumeWithoutResumeFeature() {
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		wrapper.removeFeature(AnimatorWrapper.RESUME);
		wrapper.resume();
		verify(mockAnimator, times(1)).resume();
	}

	@Test
	public void testResumeWithoutPauseNorResumeFeature() {
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		wrapper.removeFeature(AnimatorWrapper.PAUSE | AnimatorWrapper.RESUME);
		wrapper.resume();
		verifyZeroInteractions(mockAnimator);
	}

	@Test
	public void testEnd() {
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		wrapper.end();
		verify(mockAnimator, times(1)).end();
	}

	@Test
	public void testEndWithoutStartFeature() {
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		wrapper.removeFeature(AnimatorWrapper.START);
		wrapper.end();
		verify(mockAnimator, times(1)).end();
	}

	@Test
	public void testEndWithoutEndFeature() {
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		wrapper.removeFeature(AnimatorWrapper.END);
		wrapper.end();
		verify(mockAnimator, times(1)).end();
	}

	@Test
	public void testEndWithoutStartNorEndFeature() {
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		wrapper.removeFeature(AnimatorWrapper.START | AnimatorWrapper.END);
		wrapper.end();
		verifyZeroInteractions(mockAnimator);
	}

	@Test
	public void testCancel() {
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		wrapper.cancel();
		verify(mockAnimator, times(1)).cancel();
	}

	@Test
	public void testCancelWithoutStartFeature() {
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		wrapper.removeFeature(AnimatorWrapper.START);
		wrapper.cancel();
		verify(mockAnimator, times(1)).cancel();
	}

	@Test
	public void testCancelWithoutCancelFeature() {
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		wrapper.removeFeature(AnimatorWrapper.CANCEL);
		wrapper.cancel();
		verify(mockAnimator, times(1)).cancel();
	}

	@Test
	public void testCancelWithoutStartNorCancelFeature() {
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		wrapper.removeFeature(AnimatorWrapper.START | AnimatorWrapper.CANCEL);
		wrapper.cancel();
		verifyZeroInteractions(mockAnimator);
	}

	@Test
	public void testIsRunning() {
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		when(mockAnimator.isRunning()).thenReturn(true);
		assertThat(wrapper.isRunning(), is(true));
		verify(mockAnimator, times(1)).isRunning();
	}

	@Test
	public void testAnimatorListenerWrapperOnAnimationStart() {
		final Animator.AnimatorListener mockListener = mock(Animator.AnimatorListener.class);
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		final AnimatorWrapper.AnimatorListenerWrapper listenerWrapper = new AnimatorWrapper.AnimatorListenerWrapper(mockListener, wrapper);
		listenerWrapper.onAnimationStart(mockAnimator);
		verify(mockListener, times(1)).onAnimationStart(wrapper);
		verifyNoMoreInteractions(mockListener);
	}

	@Test
	public void testAnimatorListenerWrapperOnAnimationEnd() {
		final Animator.AnimatorListener mockListener = mock(Animator.AnimatorListener.class);
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		final AnimatorWrapper.AnimatorListenerWrapper listenerWrapper = new AnimatorWrapper.AnimatorListenerWrapper(mockListener, wrapper);
		listenerWrapper.onAnimationEnd(mockAnimator);
		verify(mockListener, times(1)).onAnimationEnd(wrapper);
		verifyNoMoreInteractions(mockListener);
	}

	@Test
	public void testAnimatorListenerWrapperOnAnimationCancel() {
		final Animator.AnimatorListener mockListener = mock(Animator.AnimatorListener.class);
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		final AnimatorWrapper.AnimatorListenerWrapper listenerWrapper = new AnimatorWrapper.AnimatorListenerWrapper(mockListener, wrapper);
		listenerWrapper.onAnimationCancel(mockAnimator);
		verify(mockListener, times(1)).onAnimationCancel(wrapper);
		verifyNoMoreInteractions(mockListener);
	}

	@Test
	public void testAnimatorListenerWrapperOnAnimationRepeat() {
		final Animator.AnimatorListener mockListener = mock(Animator.AnimatorListener.class);
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		final AnimatorWrapper.AnimatorListenerWrapper listenerWrapper = new AnimatorWrapper.AnimatorListenerWrapper(mockListener, wrapper);
		listenerWrapper.onAnimationRepeat(mockAnimator);
		verify(mockListener, times(1)).onAnimationRepeat(wrapper);
		verifyNoMoreInteractions(mockListener);
	}

	@Test
	@SdkSuppress(minSdkVersion = Build.VERSION_CODES.KITKAT)
	public void testAnimatorPauseListenerWrapperOnAnimationPause() {
		final Animator.AnimatorPauseListener mockListener = mock(Animator.AnimatorPauseListener.class);
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		final AnimatorWrapper.AnimatorPauseListenerWrapper listenerWrapper = new AnimatorWrapper.AnimatorPauseListenerWrapper(mockListener, wrapper);
		listenerWrapper.onAnimationPause(mockAnimator);
		verify(mockListener, times(1)).onAnimationPause(wrapper);
		verifyNoMoreInteractions(mockListener);
	}

	@Test
	@SdkSuppress(minSdkVersion = Build.VERSION_CODES.KITKAT)
	public void testAnimatorPauseListenerWrapperOnAnimationResume() {
		final Animator.AnimatorPauseListener mockListener = mock(Animator.AnimatorPauseListener.class);
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		final AnimatorWrapper.AnimatorPauseListenerWrapper listenerWrapper = new AnimatorWrapper.AnimatorPauseListenerWrapper(mockListener, wrapper);
		listenerWrapper.onAnimationResume(mockAnimator);
		verify(mockListener, times(1)).onAnimationResume(wrapper);
		verifyNoMoreInteractions(mockListener);
	}
}
