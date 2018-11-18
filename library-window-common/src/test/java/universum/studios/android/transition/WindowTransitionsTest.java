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

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import universum.studios.android.test.local.LocalTestCase;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Martin Albedinsky
 */
public final class WindowTransitionsTest extends LocalTestCase {

	@Test(expected = IllegalAccessException.class)
	public void testInstantiation() throws Exception {
		// Act:
		WindowTransitions.class.newInstance();
	}

	@Test(expected = InvocationTargetException.class)
	public void testInstantiationWithAccessibleConstructor() throws Exception {
		// Arrange:
		final Constructor<WindowTransitions> constructor = WindowTransitions.class.getDeclaredConstructor();
		constructor.setAccessible(true);
		// Act:
		constructor.newInstance();
	}

	@Test public void testNONE() {
		// Assert:
		assertThatTransitionHasAttributes(WindowTransitions.NONE,
				WindowTransition.NO_ANIMATION,
				WindowTransition.NO_ANIMATION,
				WindowTransition.NO_ANIMATION,
				WindowTransition.NO_ANIMATION,
				"NONE"
		);
	}

	@Test public void testCROSS_FADE() {
		// Assert:
		assertThatTransitionHasAttributes(WindowTransitions.CROSS_FADE,
				R.anim.transition_window_fade_in,
				R.anim.transition_window_fade_out,
				R.anim.transition_window_fade_in_back,
				R.anim.transition_window_fade_out_back,
				"CROSS_FADE"
		);
	}

	@Test public void testCROSS_FADE_AND_HOLD() {
		// Assert:
		assertThatTransitionHasAttributes(WindowTransitions.CROSS_FADE_AND_HOLD,
				R.anim.transition_window_fade_in,
				R.anim.transition_window_hold,
				R.anim.transition_window_hold_back,
				R.anim.transition_window_fade_out_back,
				"CROSS_FADE_AND_HOLD"
		);
	}

	@Test public void testSLIDE_TO_RIGHT() {
		// Assert:
		assertThatTransitionHasAttributes(WindowTransitions.SLIDE_TO_RIGHT,
				R.anim.transition_window_slide_in_right,
				R.anim.transition_window_slide_out_right,
				R.anim.transition_window_slide_in_left_back,
				R.anim.transition_window_slide_out_left_back,
				"SLIDE_TO_RIGHT"
		);
	}

	@Test public void testSLIDE_TO_LEFT() {
		// Assert:
		assertThatTransitionHasAttributes(WindowTransitions.SLIDE_TO_LEFT,
				R.anim.transition_window_slide_in_left,
				R.anim.transition_window_slide_out_left,
				R.anim.transition_window_slide_in_right_back,
				R.anim.transition_window_slide_out_right_back,
				"SLIDE_TO_LEFT"
		);
	}

	@Test public void testSLIDE_TO_TOP() {
		// Assert:
		assertThatTransitionHasAttributes(WindowTransitions.SLIDE_TO_TOP,
				R.anim.transition_window_slide_in_top,
				R.anim.transition_window_slide_out_top,
				R.anim.transition_window_slide_in_bottom_back,
				R.anim.transition_window_slide_out_bottom_back,
				"SLIDE_TO_TOP"
		);
	}

	@Test public void testSLIDE_TO_BOTTOM() {
		// Assert:
		assertThatTransitionHasAttributes(WindowTransitions.SLIDE_TO_BOTTOM,
				R.anim.transition_window_slide_in_bottom,
				R.anim.transition_window_slide_out_bottom,
				R.anim.transition_window_slide_in_top_back,
				R.anim.transition_window_slide_out_top_back,
				"SLIDE_TO_BOTTOM"
		);
	}

	@Test public void testSLIDE_TO_LEFT_AND_SCALE_OUT() {
		// Assert:
		assertThatTransitionHasAttributes(WindowTransitions.SLIDE_TO_LEFT_AND_SCALE_OUT,
				R.anim.transition_window_slide_in_left,
				R.anim.transition_window_scale_out,
				R.anim.transition_window_scale_in_back,
				R.anim.transition_window_slide_out_right_back,
				"SLIDE_TO_LEFT_AND_SCALE_OUT"
		);
	}

	@Test public void testSLIDE_TO_RIGHT_AND_SCALE_OUT() {
		// Assert:
		assertThatTransitionHasAttributes(WindowTransitions.SLIDE_TO_RIGHT_AND_SCALE_OUT,
				R.anim.transition_window_slide_in_right,
				R.anim.transition_window_scale_out,
				R.anim.transition_window_scale_in_back,
				R.anim.transition_window_slide_out_left_back,
				"SLIDE_TO_RIGHT_AND_SCALE_OUT"
		);
	}

	@Test public void testSLIDE_TO_TOP_AND_SCALE_OUT() {
		// Assert:
		assertThatTransitionHasAttributes(WindowTransitions.SLIDE_TO_TOP_AND_SCALE_OUT,
				R.anim.transition_window_slide_in_top,
				R.anim.transition_window_scale_out,
				R.anim.transition_window_scale_in_back,
				R.anim.transition_window_slide_out_bottom_back,
				"SLIDE_TO_TOP_AND_SCALE_OUT"
		);
	}

	@Test public void testSLIDE_TO_BOTTOM_AND_SCALE_OUT() {
		// Assert:
		assertThatTransitionHasAttributes(WindowTransitions.SLIDE_TO_BOTTOM_AND_SCALE_OUT,
				R.anim.transition_window_slide_in_bottom,
				R.anim.transition_window_scale_out,
				R.anim.transition_window_scale_in_back,
				R.anim.transition_window_slide_out_top_back,
				"SLIDE_TO_BOTTOM_AND_SCALE_OUT"
		);
	}

	private void assertThatTransitionHasAttributes(
			final WindowTransition transition,
			final int startEnterAnim,
			final int startExitAnim,
			final int finishEnterAnim,
			final int finishExitAnim,
			final String name
	) {
		// Assert:
		assertThat(transition, is(notNullValue()));
		assertThat(transition.getStartEnterAnimation(), is(startEnterAnim));
		assertThat(transition.getStartExitAnimation(), is(startExitAnim));
		assertThat(transition.getFinishEnterAnimation(), is(finishEnterAnim));
		assertThat(transition.getFinishExitAnimation(), is(finishExitAnim));
		assertThat(transition.getName(), is(name));
	}
}