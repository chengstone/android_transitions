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

import org.junit.Test;

import universum.studios.android.test.instrumented.InstrumentedTestCase;
import universum.studios.android.test.instrumented.TestUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assume.assumeTrue;

/**
 * @author Martin Albedinsky
 */
@SdkSuppress(minSdkVersion = Build.VERSION_CODES.LOLLIPOP)
public final class ScaleTest extends InstrumentedTestCase {

	@Test
	@SuppressWarnings("ConstantConditions")
	public void testInflationWithSpecifiedPivotFractions() {
		assumeTrue(TestUtils.hasLibraryRootPackageName(mContext));
		final Scale scale = (Scale) TestUtils.inflateTransition(mContext, "scale_with_pivot_fractions");
		assertThat(scale, is(notNullValue()));
		assertThat(scale.getMode(), is(Scale.MODE_IN | Scale.MODE_OUT));
		assertThat(scale.getPivotXFraction(), is(0.75f));
		assertThat(scale.getPivotYFraction(), is(0.25f));
		assertThat(scale.getPivotX(), is(nullValue()));
		assertThat(scale.getPivotY(), is(nullValue()));
	}

	@Test
	@SuppressWarnings("ConstantConditions")
	public void testInflationWithSpecifiedPivots() {
		assumeTrue(TestUtils.hasLibraryRootPackageName(mContext));
		final Scale scale = (Scale) TestUtils.inflateTransition(mContext, "scale_with_pivots");
		assertThat(scale, is(notNullValue()));
		assertThat(scale.getMode(), is(Scale.MODE_IN | Scale.MODE_OUT));
		assertThat(scale.getPivotXFraction(), is(0.5f));
		assertThat(scale.getPivotYFraction(), is(0.5f));
		assertThat(scale.getPivotX(), is(333f));
		assertThat(scale.getPivotY(), is(111f));
	}
}
