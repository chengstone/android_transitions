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
package universum.studios.android.transition.util;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.os.Build;

import org.junit.Test;
import org.robolectric.annotation.Config;

import universum.studios.android.test.local.RobolectricTestCase;

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
@Config(sdk = Build.VERSION_CODES.O)
public final class AnimatorWrapperTest extends RobolectricTestCase {

	@Test public void testInstantiation() {
		// Act:
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		// Assert:
		assertThat(wrapper.getWrappedAnimator(), is(mockAnimator));
		assertThat(wrapper.hasFeature(AnimatorWrapper.ALL), is(true));
		assertThat(wrapper.getListeners(), is(notNullValue()));
		assertThat(wrapper.getListeners().isEmpty(), is(true));
	}

	@Test public void testRequestFeatures() {
		// Arrange:
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		// Act + Assert:
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

	@Test public void testRequestFeature() {
		// Arrange:
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		wrapper.requestFeatures(0);
		// Act + Assert:
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

	@Test public void testRemoveFeature() {
		// Arrange:
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		wrapper.requestFeatures(AnimatorWrapper.ALL);
		// Act + Assert:
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

	@Test public void testAddRemoveListener() {
		// Arrange:
		final Animator mockAnimator = mock(Animator.class);
		final Animator.AnimatorListener mockListener = mock(Animator.AnimatorListener.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		// Act + Assert:
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

	@Test public void testAddRemovePauseListener() {
		// Arrange:
		final Animator mockAnimator = mock(Animator.class);
		final Animator.AnimatorPauseListener mockPauseListener = mock(Animator.AnimatorPauseListener.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		// Act + Assert:
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

	@Test public void testAddPauseListenerWithoutPauseFeature() {
		// Arrange:
		final Animator mockAnimator = mock(Animator.class);
		final Animator.AnimatorPauseListener mockPauseListener = mock(Animator.AnimatorPauseListener.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		wrapper.removeFeature(AnimatorWrapper.PAUSE);
		// Act:
		wrapper.addPauseListener(mockPauseListener);
		// Assert:
		verify(mockAnimator, times(1)).addPauseListener(any(Animator.AnimatorPauseListener.class));
		verify(mockAnimator, times(0)).addPauseListener(mockPauseListener);
		verifyNoMoreInteractions(mockAnimator);
	}

	@Test public void testAddPauseListenerWithoutResumeFeature() {
		// Arrange:
		final Animator mockAnimator = mock(Animator.class);
		final Animator.AnimatorPauseListener mockPauseListener = mock(Animator.AnimatorPauseListener.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		wrapper.removeFeature(AnimatorWrapper.RESUME);
		// Act:
		wrapper.addPauseListener(mockPauseListener);
		// Assert:
		verify(mockAnimator, times(1)).addPauseListener(any(Animator.AnimatorPauseListener.class));
		verify(mockAnimator, times(0)).addPauseListener(mockPauseListener);
		verifyNoMoreInteractions(mockAnimator);
	}

	@Test public void testAddPauseListenerWithoutPauseNorResumeFeature() {
		// Arrange:
		final Animator mockAnimator = mock(Animator.class);
		final Animator.AnimatorPauseListener mockPauseListener = mock(Animator.AnimatorPauseListener.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		wrapper.removeFeature(AnimatorWrapper.PAUSE | AnimatorWrapper.RESUME);
		// Act:
		wrapper.addPauseListener(mockPauseListener);
		// Assert:
		verifyZeroInteractions(mockAnimator);
		assertThat(wrapper.getListeners(), is(notNullValue()));
		assertThat(wrapper.getListeners().isEmpty(), is(true));
	}

	@Test public void testRemovePauseListenerNotRegistered() {
		// Arrange:
		final Animator mockAnimator = mock(Animator.class);
		final Animator.AnimatorPauseListener mockPauseListener = mock(Animator.AnimatorPauseListener.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		// Act:
		wrapper.removePauseListener(mockPauseListener);
		// Assert:
		verifyZeroInteractions(mockAnimator);
	}

	@Test public void testRemoveAllListeners() {
		// Arrange:
		final Animator mockAnimator = mock(Animator.class);
		final Animator.AnimatorListener mockListener = mock(Animator.AnimatorListener.class);
		final Animator.AnimatorPauseListener mockPauseListener = mock(Animator.AnimatorPauseListener.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		wrapper.addListener(mockListener);
		wrapper.addPauseListener(mockPauseListener);
		// Act:
		wrapper.removeAllListeners();
		// Assert:
		assertThat(wrapper.getListeners().isEmpty(), is(true));
		verify(mockAnimator).removeAllListeners();
	}

	@Test public void testRemoveAllListenersWithoutRegisteredListeners() {
		// Arrange:
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		// Act:
		wrapper.removeAllListeners();
		// Assert:
		verify(mockAnimator).removeAllListeners();
	}

	@Test public void testSetStartDelay() {
		// Arrange:
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		// Act:
		wrapper.setStartDelay(150L);
		// Assert:
		verify(mockAnimator, times(1)).setStartDelay(150L);
	}

	@Test public void testGetStartDelay() {
		// Arrange:
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		when(mockAnimator.getStartDelay()).thenReturn(250L);
		// Act + Assert:
		assertThat(wrapper.getStartDelay(), is(250L));
		verify(mockAnimator).getStartDelay();
	}

	@Test public void testSetDuration() {
		// Arrange:
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		// Act:
		wrapper.setDuration(200L);
		// Assert:
		verify(mockAnimator, times(1)).setDuration(200L);
	}

	@Test public void testGetDuration() {
		// Arrange:
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		when(mockAnimator.getDuration()).thenReturn(100L);
		// Act + Assert:
		assertThat(wrapper.getDuration(), is(100L));
		verify(mockAnimator).getDuration();
	}

	@Test public void testSetInterpolator() {
		// Arrange:
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		final TimeInterpolator mockInterpolator = mock(TimeInterpolator.class);
		// Act:
		wrapper.setInterpolator(mockInterpolator);
		// Assert:
		verify(mockAnimator).setInterpolator(mockInterpolator);
	}

	@Test public void testGetInterpolator() {
		// Arrange:
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		final TimeInterpolator mockInterpolator = mock(TimeInterpolator.class);
		when(mockAnimator.getInterpolator()).thenReturn(mockInterpolator);
		// Act + Assert:
		assertThat(wrapper.getInterpolator(), is(mockInterpolator));
		verify(mockAnimator).getInterpolator();
	}

	@Test public void testSetTarget() {
		// Arrange:
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		final Object target = new Object();
		// Act:
		wrapper.setTarget(target);
		// Assert:
		verify(mockAnimator).setTarget(target);
	}

	@Test public void testStart() {
		// Arrange:
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		// Act:
		wrapper.start();
		// Assert:
		verify(mockAnimator).start();
	}

	@Test public void testStartWithoutStartFeature() {
		// Arrange:
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		wrapper.removeFeature(AnimatorWrapper.START);
		// Act:
		wrapper.start();
		// Assert:
		verifyZeroInteractions(mockAnimator);
	}

	@Test public void testIsStarted() {
		// Arrange:
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		when(mockAnimator.isStarted()).thenReturn(true);
		// Act + Assert:
		assertThat(wrapper.isStarted(), is(true));
		verify(mockAnimator).isStarted();
	}

	@Test public void testPause() {
		// Arrange:
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		// Act:
		wrapper.pause();
		// Assert:
		verify(mockAnimator).pause();
	}

	@Test public void testPauseWithoutPauseFeature() {
		// Arrange:
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		wrapper.removeFeature(AnimatorWrapper.PAUSE);
		// Act:
		wrapper.pause();
		// Assert:
		verifyZeroInteractions(mockAnimator);
	}

	@Test public void testIsPaused() {
		// Arrange:
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		when(mockAnimator.isPaused()).thenReturn(true);
		// Act + Assert:
		assertThat(wrapper.isPaused(), is(true));
		verify(mockAnimator).isPaused();
	}

	@Test public void testResume() {
		// Arrange:
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		// Act:
		wrapper.resume();
		// Assert:
		verify(mockAnimator).resume();
	}

	@Test public void testResumeWithoutPauseFeature() {
		// Arrange:
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		wrapper.removeFeature(AnimatorWrapper.PAUSE);
		// Act:
		wrapper.resume();
		// Assert:
		verify(mockAnimator).resume();
	}

	@Test public void testResumeWithoutResumeFeature() {
		// Arrange:
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		wrapper.removeFeature(AnimatorWrapper.RESUME);
		// Act:
		wrapper.resume();
		// Assert:
		verify(mockAnimator).resume();
	}

	@Test public void testResumeWithoutPauseNorResumeFeature() {
		// Arrange:
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		wrapper.removeFeature(AnimatorWrapper.PAUSE | AnimatorWrapper.RESUME);
		// Act:
		wrapper.resume();
		// Assert:
		verifyZeroInteractions(mockAnimator);
	}

	@Test public void testEnd() {
		// Arrange:
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		// Act:
		wrapper.end();
		// Assert:
		verify(mockAnimator).end();
	}

	@Test public void testEndWithoutStartFeature() {
		// Arrange:
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		wrapper.removeFeature(AnimatorWrapper.START);
		// Act:
		wrapper.end();
		// Assert:
		verify(mockAnimator).end();
	}

	@Test public void testEndWithoutEndFeature() {
		// Arrange:
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		wrapper.removeFeature(AnimatorWrapper.END);
		// Act:
		wrapper.end();
		// Assert:
		verify(mockAnimator).end();
	}

	@Test public void testEndWithoutStartNorEndFeature() {
		// Arrange:
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		wrapper.removeFeature(AnimatorWrapper.START | AnimatorWrapper.END);
		// Act:
		wrapper.end();
		// Assert:
		verifyZeroInteractions(mockAnimator);
	}

	@Test public void testCancel() {
		// Arrange:
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		// Act:
		wrapper.cancel();
		// Assert:
		verify(mockAnimator).cancel();
	}

	@Test public void testCancelWithoutStartFeature() {
		// Arrange:
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		wrapper.removeFeature(AnimatorWrapper.START);
		// Act:
		wrapper.cancel();
		// Assert:
		verify(mockAnimator).cancel();
	}

	@Test public void testCancelWithoutCancelFeature() {
		// Arrange:
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		wrapper.removeFeature(AnimatorWrapper.CANCEL);
		// Act:
		wrapper.cancel();
		// Assert:
		verify(mockAnimator).cancel();
	}

	@Test public void testCancelWithoutStartNorCancelFeature() {
		// Arrange:
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		wrapper.removeFeature(AnimatorWrapper.START | AnimatorWrapper.CANCEL);
		// Act:
		wrapper.cancel();
		// Assert:
		verifyZeroInteractions(mockAnimator);
	}

	@Test public void testIsRunning() {
		// Arrange:
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		when(mockAnimator.isRunning()).thenReturn(true);
		// Act + Assert:
		assertThat(wrapper.isRunning(), is(true));
		verify(mockAnimator).isRunning();
	}

	@Test public void testAnimatorListenerWrapperOnAnimationStart() {
		// Arrange:
		final Animator.AnimatorListener mockListener = mock(Animator.AnimatorListener.class);
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		final AnimatorWrapper.AnimatorListenerWrapper listenerWrapper = new AnimatorWrapper.AnimatorListenerWrapper(mockListener, wrapper);
		// Act:
		listenerWrapper.onAnimationStart(mockAnimator);
		// Assert:
		verify(mockListener).onAnimationStart(wrapper);
		verifyNoMoreInteractions(mockListener);
	}

	@Test public void testAnimatorListenerWrapperOnAnimationEnd() {
		// Arrange:
		final Animator.AnimatorListener mockListener = mock(Animator.AnimatorListener.class);
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		final AnimatorWrapper.AnimatorListenerWrapper listenerWrapper = new AnimatorWrapper.AnimatorListenerWrapper(mockListener, wrapper);
		// Act:
		listenerWrapper.onAnimationEnd(mockAnimator);
		// Assert:
		verify(mockListener).onAnimationEnd(wrapper);
		verifyNoMoreInteractions(mockListener);
	}

	@Test public void testAnimatorListenerWrapperOnAnimationCancel() {
		// Arrange:
		final Animator.AnimatorListener mockListener = mock(Animator.AnimatorListener.class);
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		final AnimatorWrapper.AnimatorListenerWrapper listenerWrapper = new AnimatorWrapper.AnimatorListenerWrapper(mockListener, wrapper);
		// Act:
		listenerWrapper.onAnimationCancel(mockAnimator);
		// Assert:
		verify(mockListener).onAnimationCancel(wrapper);
		verifyNoMoreInteractions(mockListener);
	}

	@Test public void testAnimatorListenerWrapperOnAnimationRepeat() {
		// Arrange:
		final Animator.AnimatorListener mockListener = mock(Animator.AnimatorListener.class);
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		final AnimatorWrapper.AnimatorListenerWrapper listenerWrapper = new AnimatorWrapper.AnimatorListenerWrapper(mockListener, wrapper);
		// Act:
		listenerWrapper.onAnimationRepeat(mockAnimator);
		// Assert:
		verify(mockListener).onAnimationRepeat(wrapper);
		verifyNoMoreInteractions(mockListener);
	}

	@Test public void testAnimatorPauseListenerWrapperOnAnimationPause() {
		// Arrange:
		final Animator.AnimatorPauseListener mockListener = mock(Animator.AnimatorPauseListener.class);
		final Animator mockAnimator = mock(Animator.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		final AnimatorWrapper.AnimatorPauseListenerWrapper listenerWrapper = new AnimatorWrapper.AnimatorPauseListenerWrapper(mockListener, wrapper);
		// Act:
		listenerWrapper.onAnimationPause(mockAnimator);
		// Assert:
		verify(mockListener).onAnimationPause(wrapper);
		verifyNoMoreInteractions(mockListener);
	}

	@Test public void testAnimatorPauseListenerWrapperOnAnimationResume() {
		// Arrange:
		final Animator mockAnimator = mock(Animator.class);
		final Animator.AnimatorPauseListener mockListener = mock(Animator.AnimatorPauseListener.class);
		final AnimatorWrapper wrapper = new AnimatorWrapper(mockAnimator);
		final AnimatorWrapper.AnimatorPauseListenerWrapper listenerWrapper = new AnimatorWrapper.AnimatorPauseListenerWrapper(mockListener, wrapper);
		// Act:
		listenerWrapper.onAnimationResume(mockAnimator);
		// Assert:
		verify(mockListener).onAnimationResume(wrapper);
		verifyNoMoreInteractions(mockListener);
	}
}