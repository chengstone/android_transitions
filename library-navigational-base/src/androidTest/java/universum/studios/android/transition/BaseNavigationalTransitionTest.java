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

import android.os.Build;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.junit.Test;

import universum.studios.android.test.instrumented.InstrumentedTestCase;
import universum.studios.android.test.instrumented.TestResources;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * @author Martin Albedinsky
 */
public final class BaseNavigationalTransitionTest extends InstrumentedTestCase {

	@Test public void testInflateTransition() {
		// Arrange:
		final TestTransition navigationalTransition = new TestTransition();
		final int transitionResource = TestResources.resourceIdentifier(
				context,
				TestResources.TRANSITION,
				"transition_fade"
		);
		// Act:
		final Transition transition = navigationalTransition.inflateTransition(context, transitionResource);
		// Assert:
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			assertThat(transition, is(not(nullValue())));
			assertThat(transition, instanceOf(Fade.class));
			assertThat(navigationalTransition.inflateTransition(context, transitionResource), is(not(transition)));
		} else {
			assertThat(transition, is(nullValue()));
		}
	}

	@Test public void testInflateTransitionManager() {
		// Arrange:
		final TestTransition navigationalTransition = new TestTransition();
		final int transitionManagerResource = TestResources.resourceIdentifier(
				context,
				TestResources.TRANSITION,
				"transition_manager"
		);
		final ViewGroup sceneRoot = new FrameLayout(context);
		// Act:
		final TransitionManager transitionManager = navigationalTransition.inflateTransitionManager(context, transitionManagerResource, sceneRoot);
		// Assert:
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			assertThat(transitionManager, is(not(nullValue())));
			assertThat(navigationalTransition.inflateTransitionManager(context, transitionManagerResource, sceneRoot), is(not(transitionManager)));
		} else {
			assertThat(transitionManager, is(nullValue()));
		}
	}

	private static final class TestTransition extends BaseNavigationalTransition<TestTransition> {

		TestTransition() {
			super();
		}
	}
}