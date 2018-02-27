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
import android.support.test.filters.SdkSuppress;
import android.support.test.rule.ActivityTestRule;
import android.view.Gravity;
import android.view.View;

import org.junit.Rule;
import org.junit.Test;

import universum.studios.android.test.instrumented.InstrumentedTestCase;
import universum.studios.android.test.instrumented.TestActivity;
import universum.studios.android.test.instrumented.TestUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assume.assumeTrue;

/**
 * @author Martin Albedinsky
 */
@SdkSuppress(minSdkVersion = Build.VERSION_CODES.LOLLIPOP)
public final class RevealTest extends InstrumentedTestCase {

	@Rule
	public final ActivityTestRule<TestActivity> ACTIVITY_RULE = new ActivityTestRule<>(TestActivity.class, false, false);

	@Test
	@SuppressWarnings("ConstantConditions")
	public void testInflationOfRevealTransition() {
		assumeTrue(TestUtils.hasLibraryRootPackageName(mContext));
		final Reveal reveal = (Reveal) TestUtils.inflateTransition(mContext, "reveal");
		assertThat(reveal, is(notNullValue()));
		assertThat(reveal.getMode(), is(Reveal.REVEAL));
	}

	@Test
	@SuppressWarnings("ConstantConditions")
	public void testInflationOfConcealTransition() {
		assumeTrue(TestUtils.hasLibraryRootPackageName(mContext));
		final Reveal reveal = (Reveal) TestUtils.inflateTransition(mContext, "reveal");
		assertThat(reveal, is(notNullValue()));
		assertThat(reveal.getMode(), is(Reveal.REVEAL));
	}

	@Test
	@SuppressWarnings("ConstantConditions")
	public void testInflationOfTransitionWithAttributes() {
		assumeTrue(TestUtils.hasLibraryRootPackageName(mContext));
		final Reveal reveal = (Reveal) TestUtils.inflateTransition(mContext, "reveal_with_attributes");
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
