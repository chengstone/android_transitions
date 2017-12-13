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

	@Test
	public void testInflateTransition() {
		final TestTransition navigationalTransition = new TestTransition();
		final int transitionResource = TestResources.resourceIdentifier(
				mContext,
				TestResources.TRANSITION,
				"transition_fade"
		);
		final Transition transition = navigationalTransition.inflateTransition(mContext, transitionResource);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			assertThat(transition, is(not(nullValue())));
			assertThat(transition, instanceOf(Fade.class));
			assertThat(navigationalTransition.inflateTransition(mContext, transitionResource), is(not(transition)));
		} else {
			assertThat(transition, is(nullValue()));
		}
	}

	@Test
	public void testInflateTransitionManager() {
		final TestTransition navigationalTransition = new TestTransition();
		final int transitionManagerResource = TestResources.resourceIdentifier(
				mContext,
				TestResources.TRANSITION,
				"transition_manager"
		);
		final ViewGroup sceneRoot = new FrameLayout(mContext);
		final TransitionManager transitionManager = navigationalTransition.inflateTransitionManager(mContext, transitionManagerResource, sceneRoot);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			assertThat(transitionManager, is(not(nullValue())));
			assertThat(navigationalTransition.inflateTransitionManager(mContext, transitionManagerResource, sceneRoot), is(not(transitionManager)));
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
