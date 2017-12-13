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
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.number.OrderingComparison.greaterThanOrEqualTo;
import static org.hamcrest.number.OrderingComparison.lessThanOrEqualTo;
import static org.junit.Assume.assumeTrue;

/**
 * @author Martin Albedinsky
 */
@SdkSuppress(minSdkVersion = Build.VERSION_CODES.LOLLIPOP)
public final class TranslateTest extends InstrumentedTestCase {

	@Test
	@SuppressWarnings("ConstantConditions")
	public void testInflation() {
		assumeTrue(TestUtils.hasLibraryRootPackageName(mContext));
		final Translate translate = (Translate) TestUtils.inflateTransition(mContext, "translate");
		assertThat(translate, is(notNullValue()));
		assertThat(translate.getMode(), is(Scale.MODE_IN | Scale.MODE_OUT));
		assertThat(translate.getTranslationXDelta(), is(48f));
		assertThat(translate.getTranslationXRelativity(), is(Translate.Description.NONE));
		assertThat(translate.getTranslationYDelta(), is(24f));
		assertThat(translate.getTranslationYRelativity(), is(Translate.Description.NONE));
	}

	@Test
	@SuppressWarnings("ConstantConditions")
	public void testInflationForTranslationRelativeToTarget() {
		assumeTrue(TestUtils.hasLibraryRootPackageName(mContext));
		final Translate translate = (Translate) TestUtils.inflateTransition(mContext, "translate_relative_to_target");
		assertThat(translate, is(notNullValue()));
		assertThat(translate.getMode(), is(Scale.MODE_IN | Scale.MODE_OUT));
		assertThat(translate.getTranslationXDelta(), allOf(greaterThanOrEqualTo(0.799f), lessThanOrEqualTo(0.801f)));
		assertThat(translate.getTranslationXRelativity(), is(Translate.Description.RELATIVE_TO_TARGET));
		assertThat(translate.getTranslationYDelta(), allOf(greaterThanOrEqualTo(0.199f), lessThanOrEqualTo(0.201f)));
		assertThat(translate.getTranslationYRelativity(), is(Translate.Description.RELATIVE_TO_TARGET));
	}

	@Test
	@SuppressWarnings("ConstantConditions")
	public void testInflationForTranslationRelativeToScene() {
		assumeTrue(TestUtils.hasLibraryRootPackageName(mContext));
		final Translate translate = (Translate) TestUtils.inflateTransition(mContext, "translate_relative_to_scene");
		assertThat(translate, is(notNullValue()));
		assertThat(translate.getMode(), is(Scale.MODE_IN | Scale.MODE_OUT));
		assertThat(translate.getTranslationXDelta(), allOf(greaterThanOrEqualTo(0.659f), lessThanOrEqualTo(0.661f)));
		assertThat(translate.getTranslationXRelativity(), is(Translate.Description.RELATIVE_TO_SCENE));
		assertThat(translate.getTranslationYDelta(), allOf(greaterThanOrEqualTo(0.329f), lessThanOrEqualTo(0.331f)));
		assertThat(translate.getTranslationYRelativity(), is(Translate.Description.RELATIVE_TO_SCENE));
	}
}
