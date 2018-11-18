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
import android.view.Gravity;
import android.view.View;

import org.junit.Rule;
import org.junit.Test;

import androidx.test.filters.SdkSuppress;
import androidx.test.rule.ActivityTestRule;
import universum.studios.android.test.instrumented.InstrumentedTestCase;
import universum.studios.android.test.instrumented.TestActivity;
import universum.studios.android.test.instrumented.TestUtils;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assume.assumeTrue;

/**
 * @author Martin Albedinsky
 */
@SdkSuppress(minSdkVersion = Build.VERSION_CODES.LOLLIPOP)
public final class RevealTest extends InstrumentedTestCase {

	@Rule public final ActivityTestRule<TestActivity> ACTIVITY_RULE = new ActivityTestRule<>(TestActivity.class, false, false);

	@SuppressWarnings("ConstantConditions")
	@Test public void testInflationOfRevealTransition() {
		assumeTrue(TestUtils.hasLibraryRootPackageName(context));
		// Act:
		final Reveal reveal = (Reveal) TestUtils.inflateTransition(context, "reveal");
		// Assert:
		assertThat(reveal, is(notNullValue()));
		assertThat(reveal.getMode(), is(Reveal.REVEAL));
	}

	@SuppressWarnings("ConstantConditions")
	@Test public void testInflationOfConcealTransition() {
		assumeTrue(TestUtils.hasLibraryRootPackageName(context));
		// Act:
		final Reveal reveal = (Reveal) TestUtils.inflateTransition(context, "reveal");
		// Assert:
		assertThat(reveal, is(notNullValue()));
		assertThat(reveal.getMode(), is(Reveal.REVEAL));
	}

	@SuppressWarnings("ConstantConditions")
	@Test public void testInflationOfTransitionWithAttributes() {
		assumeTrue(TestUtils.hasLibraryRootPackageName(context));
		// Act:
		final Reveal reveal = (Reveal) TestUtils.inflateTransition(context, "reveal_with_attributes");
		// Assert:
		assertThat(reveal, is(notNullValue()));
		assertThat(reveal.getMode(), is(Reveal.REVEAL));
		assertThat(reveal.getStartRadius(), is(8f));
		assertThat(reveal.getEndRadius(), is(16f));
		assertThat(reveal.getAppearVisibility(), is(View.VISIBLE));
		assertThat(reveal.getDisappearVisibility(), is(View.INVISIBLE));
		assertThat(reveal.getStartVisibility(), is(View.VISIBLE));
		assertThat(reveal.getEndVisibility(), is(View.INVISIBLE));
		assertThat(reveal.getCenterGravity(), is(Gravity.END | Gravity.BOTTOM));
		assertThat(reveal.getCenterHorizontalOffset(), is(-16));
		assertThat(reveal.getCenterVerticalOffset(), is(-16));
	}
}