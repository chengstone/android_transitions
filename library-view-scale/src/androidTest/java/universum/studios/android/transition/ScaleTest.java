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

import org.junit.Test;

import androidx.test.filters.SdkSuppress;
import universum.studios.android.test.instrumented.InstrumentedTestCase;
import universum.studios.android.test.instrumented.TestUtils;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assume.assumeTrue;

/**
 * @author Martin Albedinsky
 */
@SdkSuppress(minSdkVersion = Build.VERSION_CODES.LOLLIPOP)
public final class ScaleTest extends InstrumentedTestCase {

	@SuppressWarnings("ConstantConditions")
	@Test public void testInflationWithSpecifiedPivotFractions() {
		assumeTrue(TestUtils.hasLibraryRootPackageName(context));
		// Act:
		final Scale scale = (Scale) TestUtils.inflateTransition(context, "scale_with_pivot_fractions");
		// Assert:
		assertThat(scale, is(notNullValue()));
		assertThat(scale.getMode(), is(Scale.MODE_IN | Scale.MODE_OUT));
		assertThat(scale.getPivotXFraction(), is(0.75f));
		assertThat(scale.getPivotYFraction(), is(0.25f));
		assertThat(scale.getPivotX(), is(nullValue()));
		assertThat(scale.getPivotY(), is(nullValue()));
	}

	@SuppressWarnings("ConstantConditions")
	@Test public void testInflationWithSpecifiedPivots() {
		assumeTrue(TestUtils.hasLibraryRootPackageName(context));
		// Act:
		final Scale scale = (Scale) TestUtils.inflateTransition(context, "scale_with_pivots");
		// Assert:
		assertThat(scale, is(notNullValue()));
		assertThat(scale.getMode(), is(Scale.MODE_IN | Scale.MODE_OUT));
		assertThat(scale.getPivotXFraction(), is(0.5f));
		assertThat(scale.getPivotYFraction(), is(0.5f));
		assertThat(scale.getPivotX(), is(333f));
		assertThat(scale.getPivotY(), is(111f));
	}
}