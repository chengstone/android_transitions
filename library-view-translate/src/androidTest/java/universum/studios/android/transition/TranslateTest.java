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

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.OrderingComparison.greaterThanOrEqualTo;
import static org.hamcrest.number.OrderingComparison.lessThanOrEqualTo;
import static org.junit.Assume.assumeTrue;

/**
 * @author Martin Albedinsky
 */
@SdkSuppress(minSdkVersion = Build.VERSION_CODES.LOLLIPOP)
public final class TranslateTest extends InstrumentedTestCase {

	@SuppressWarnings("ConstantConditions")
	@Test public void testInflation() {
		assumeTrue(TestUtils.hasLibraryRootPackageName(context));
		// Act:
		final Translate translate = (Translate) TestUtils.inflateTransition(context, "translate");
		// Assert:
		assertThat(translate, is(notNullValue()));
		assertThat(translate.getMode(), is(Scale.MODE_IN | Scale.MODE_OUT));
		assertThat(translate.getTranslationXDelta(), is(48f));
		assertThat(translate.getTranslationXRelativity(), is(Translate.Description.NONE));
		assertThat(translate.getTranslationYDelta(), is(24f));
		assertThat(translate.getTranslationYRelativity(), is(Translate.Description.NONE));
	}

	@SuppressWarnings("ConstantConditions")
	@Test public void testInflationForTranslationRelativeToTarget() {
		assumeTrue(TestUtils.hasLibraryRootPackageName(context));
		// Act:
		final Translate translate = (Translate) TestUtils.inflateTransition(context, "translate_relative_to_target");
		// Assert:
		assertThat(translate, is(notNullValue()));
		assertThat(translate.getMode(), is(Scale.MODE_IN | Scale.MODE_OUT));
		assertThat(translate.getTranslationXDelta(), allOf(greaterThanOrEqualTo(0.799f), lessThanOrEqualTo(0.801f)));
		assertThat(translate.getTranslationXRelativity(), is(Translate.Description.RELATIVE_TO_TARGET));
		assertThat(translate.getTranslationYDelta(), allOf(greaterThanOrEqualTo(0.199f), lessThanOrEqualTo(0.201f)));
		assertThat(translate.getTranslationYRelativity(), is(Translate.Description.RELATIVE_TO_TARGET));
	}

	@SuppressWarnings("ConstantConditions")
	@Test public void testInflationForTranslationRelativeToScene() {
		assumeTrue(TestUtils.hasLibraryRootPackageName(context));
		// Act:
		final Translate translate = (Translate) TestUtils.inflateTransition(context, "translate_relative_to_scene");
		// Assert:
		assertThat(translate, is(notNullValue()));
		assertThat(translate.getMode(), is(Scale.MODE_IN | Scale.MODE_OUT));
		assertThat(translate.getTranslationXDelta(), allOf(greaterThanOrEqualTo(0.659f), lessThanOrEqualTo(0.661f)));
		assertThat(translate.getTranslationXRelativity(), is(Translate.Description.RELATIVE_TO_SCENE));
		assertThat(translate.getTranslationYDelta(), allOf(greaterThanOrEqualTo(0.329f), lessThanOrEqualTo(0.331f)));
		assertThat(translate.getTranslationYRelativity(), is(Translate.Description.RELATIVE_TO_SCENE));
	}
}