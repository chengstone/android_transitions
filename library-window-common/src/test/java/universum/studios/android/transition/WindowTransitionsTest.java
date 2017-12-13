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

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import universum.studios.android.test.local.LocalTestCase;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Martin Albedinsky
 */
public final class WindowTransitionsTest extends LocalTestCase {

	@Test(expected = IllegalAccessException.class)
	public void testInstantiation() throws Exception {
		WindowTransitions.class.newInstance();
	}

	@Test(expected = InvocationTargetException.class)
	public void testInstantiationWithAccessibleConstructor() throws Exception {
		final Constructor<WindowTransitions> constructor = WindowTransitions.class.getDeclaredConstructor();
		constructor.setAccessible(true);
		constructor.newInstance();
	}

	@Test
	public void testNONE() {
		assertThatTransitionHasAttributes(WindowTransitions.NONE,
				WindowTransition.NO_ANIMATION,
				WindowTransition.NO_ANIMATION,
				WindowTransition.NO_ANIMATION,
				WindowTransition.NO_ANIMATION,
				"NONE"
		);
	}

	@Test
	public void testCROSS_FADE() {
		assertThatTransitionHasAttributes(WindowTransitions.CROSS_FADE,
				R.anim.ui_window_fade_in,
				R.anim.ui_window_fade_out,
				R.anim.ui_window_fade_in_back,
				R.anim.ui_window_fade_out_back,
				"CROSS_FADE"
		);
	}

	@Test
	public void testCROSS_FADE_AND_HOLD() {
		assertThatTransitionHasAttributes(WindowTransitions.CROSS_FADE_AND_HOLD,
				R.anim.ui_window_fade_in,
				R.anim.ui_window_hold,
				R.anim.ui_window_hold_back,
				R.anim.ui_window_fade_out_back,
				"CROSS_FADE_AND_HOLD"
		);
	}

	@Test
	public void testSLIDE_TO_RIGHT() {
		assertThatTransitionHasAttributes(WindowTransitions.SLIDE_TO_RIGHT,
				R.anim.ui_window_slide_in_right,
				R.anim.ui_window_slide_out_right,
				R.anim.ui_window_slide_in_left_back,
				R.anim.ui_window_slide_out_left_back,
				"SLIDE_TO_RIGHT"
		);
	}

	@Test
	public void testSLIDE_TO_LEFT() {
		assertThatTransitionHasAttributes(WindowTransitions.SLIDE_TO_LEFT,
				R.anim.ui_window_slide_in_left,
				R.anim.ui_window_slide_out_left,
				R.anim.ui_window_slide_in_right_back,
				R.anim.ui_window_slide_out_right_back,
				"SLIDE_TO_LEFT"
		);
	}

	@Test
	public void testSLIDE_TO_TOP() {
		assertThatTransitionHasAttributes(WindowTransitions.SLIDE_TO_TOP,
				R.anim.ui_window_slide_in_top,
				R.anim.ui_window_slide_out_top,
				R.anim.ui_window_slide_in_bottom_back,
				R.anim.ui_window_slide_out_bottom_back,
				"SLIDE_TO_TOP"
		);
	}

	@Test
	public void testSLIDE_TO_BOTTOM() {
		assertThatTransitionHasAttributes(WindowTransitions.SLIDE_TO_BOTTOM,
				R.anim.ui_window_slide_in_bottom,
				R.anim.ui_window_slide_out_bottom,
				R.anim.ui_window_slide_in_top_back,
				R.anim.ui_window_slide_out_top_back,
				"SLIDE_TO_BOTTOM"
		);
	}

	@Test
	public void testSLIDE_TO_LEFT_AND_SCALE_OUT() {
		assertThatTransitionHasAttributes(WindowTransitions.SLIDE_TO_LEFT_AND_SCALE_OUT,
				R.anim.ui_window_slide_in_left,
				R.anim.ui_window_scale_out,
				R.anim.ui_window_scale_in_back,
				R.anim.ui_window_slide_out_right_back,
				"SLIDE_TO_LEFT_AND_SCALE_OUT"
		);
	}

	@Test
	public void testSLIDE_TO_RIGHT_AND_SCALE_OUT() {
		assertThatTransitionHasAttributes(WindowTransitions.SLIDE_TO_RIGHT_AND_SCALE_OUT,
				R.anim.ui_window_slide_in_right,
				R.anim.ui_window_scale_out,
				R.anim.ui_window_scale_in_back,
				R.anim.ui_window_slide_out_left_back,
				"SLIDE_TO_RIGHT_AND_SCALE_OUT"
		);
	}

	@Test
	public void testSLIDE_TO_TOP_AND_SCALE_OUT() {
		assertThatTransitionHasAttributes(WindowTransitions.SLIDE_TO_TOP_AND_SCALE_OUT,
				R.anim.ui_window_slide_in_top,
				R.anim.ui_window_scale_out,
				R.anim.ui_window_scale_in_back,
				R.anim.ui_window_slide_out_bottom_back,
				"SLIDE_TO_TOP_AND_SCALE_OUT"
		);
	}

	@Test
	public void testSLIDE_TO_BOTTOM_AND_SCALE_OUT() {
		assertThatTransitionHasAttributes(WindowTransitions.SLIDE_TO_BOTTOM_AND_SCALE_OUT,
				R.anim.ui_window_slide_in_bottom,
				R.anim.ui_window_scale_out,
				R.anim.ui_window_scale_in_back,
				R.anim.ui_window_slide_out_top_back,
				"SLIDE_TO_BOTTOM_AND_SCALE_OUT"
		);
	}

	private void assertThatTransitionHasAttributes(
			WindowTransition transition,
			int startEnterAnim,
			int startExitAnim,
			int finishEnterAnim,
			int finishExitAnim,
			String name
	) {
		assertThat(transition, is(not(nullValue())));
		assertThat(transition.getStartEnterAnimation(), is(startEnterAnim));
		assertThat(transition.getStartExitAnimation(), is(startExitAnim));
		assertThat(transition.getFinishEnterAnimation(), is(finishEnterAnim));
		assertThat(transition.getFinishExitAnimation(), is(finishExitAnim));
		assertThat(transition.getName(), is(name));
	}
}
