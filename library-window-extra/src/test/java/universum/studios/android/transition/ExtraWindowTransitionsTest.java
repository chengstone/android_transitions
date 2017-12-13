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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * @author Martin Albedinsky
 */
public final class ExtraWindowTransitionsTest extends LocalTestCase {

	@Test(expected = IllegalAccessException.class)
	public void testInstantiation() throws Exception {
		ExtraWindowTransitions.class.newInstance();
	}

	@Test(expected = InvocationTargetException.class)
	public void testInstantiationWithAccessibleConstructor() throws Exception {
		final Constructor<ExtraWindowTransitions> constructor = ExtraWindowTransitions.class.getDeclaredConstructor();
		constructor.setAccessible(true);
		constructor.newInstance();
	}

	@Test
	public void testSLIDE_TO_RIGHT_AND_HOLD() {
		assertThatTransitionHasAttributes(ExtraWindowTransitions.SLIDE_TO_RIGHT_AND_HOLD,
				R.anim.ui_window_slide_in_right,
				R.anim.ui_window_hold,
				R.anim.ui_window_hold_back,
				R.anim.ui_window_slide_out_left_back,
				"SLIDE_TO_RIGHT_AND_HOLD"
		);
	}

	@Test
	public void testSLIDE_TO_LEFT_AND_HOLD() {
		assertThatTransitionHasAttributes(ExtraWindowTransitions.SLIDE_TO_LEFT_AND_HOLD,
				R.anim.ui_window_slide_in_left,
				R.anim.ui_window_hold,
				R.anim.ui_window_hold_back,
				R.anim.ui_window_slide_out_right_back,
				"SLIDE_TO_LEFT_AND_HOLD"
		);
	}

	@Test
	public void testSLIDE_TO_TOP_AND_HOLD() {
		assertThatTransitionHasAttributes(ExtraWindowTransitions.SLIDE_TO_TOP_AND_HOLD,
				R.anim.ui_window_slide_in_top,
				R.anim.ui_window_hold,
				R.anim.ui_window_hold_back,
				R.anim.ui_window_slide_out_bottom_back,
				"SLIDE_TO_TOP_AND_HOLD"
		);
	}

	@Test
	public void testSLIDE_TO_BOTTOM_AND_HOLD() {
		assertThatTransitionHasAttributes(ExtraWindowTransitions.SLIDE_TO_BOTTOM_AND_HOLD,
				R.anim.ui_window_slide_in_bottom,
				R.anim.ui_window_hold,
				R.anim.ui_window_hold_back,
				R.anim.ui_window_slide_out_top_back,
				"SLIDE_TO_BOTTOM_AND_HOLD"
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
