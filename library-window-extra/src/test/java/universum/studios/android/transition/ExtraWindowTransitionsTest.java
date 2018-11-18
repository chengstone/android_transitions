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
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Martin Albedinsky
 */
public final class ExtraWindowTransitionsTest extends LocalTestCase {

	@Test(expected = IllegalAccessException.class)
	public void testInstantiation() throws Exception {
		// Act:
		ExtraWindowTransitions.class.newInstance();
	}

	@Test(expected = InvocationTargetException.class)
	public void testInstantiationWithAccessibleConstructor() throws Exception {
		// Arrange:
		final Constructor<ExtraWindowTransitions> constructor = ExtraWindowTransitions.class.getDeclaredConstructor();
		constructor.setAccessible(true);
		// Act:
		constructor.newInstance();
	}

	@Test public void testSLIDE_TO_RIGHT_AND_HOLD() {
		// Assert:
		assertThatTransitionHasAttributes(ExtraWindowTransitions.SLIDE_TO_RIGHT_AND_HOLD,
				R.anim.transition_window_slide_in_right,
				R.anim.transition_window_hold,
				R.anim.transition_window_hold_back,
				R.anim.transition_window_slide_out_left_back,
				"SLIDE_TO_RIGHT_AND_HOLD"
		);
	}

	@Test public void testSLIDE_TO_LEFT_AND_HOLD() {
		// Assert:
		assertThatTransitionHasAttributes(ExtraWindowTransitions.SLIDE_TO_LEFT_AND_HOLD,
				R.anim.transition_window_slide_in_left,
				R.anim.transition_window_hold,
				R.anim.transition_window_hold_back,
				R.anim.transition_window_slide_out_right_back,
				"SLIDE_TO_LEFT_AND_HOLD"
		);
	}

	@Test public void testSLIDE_TO_TOP_AND_HOLD() {
		// Assert:
		assertThatTransitionHasAttributes(ExtraWindowTransitions.SLIDE_TO_TOP_AND_HOLD,
				R.anim.transition_window_slide_in_top,
				R.anim.transition_window_hold,
				R.anim.transition_window_hold_back,
				R.anim.transition_window_slide_out_bottom_back,
				"SLIDE_TO_TOP_AND_HOLD"
		);
	}

	@Test public void testSLIDE_TO_BOTTOM_AND_HOLD() {
		// Assert:
		assertThatTransitionHasAttributes(ExtraWindowTransitions.SLIDE_TO_BOTTOM_AND_HOLD,
				R.anim.transition_window_slide_in_bottom,
				R.anim.transition_window_hold,
				R.anim.transition_window_hold_back,
				R.anim.transition_window_slide_out_top_back,
				"SLIDE_TO_BOTTOM_AND_HOLD"
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