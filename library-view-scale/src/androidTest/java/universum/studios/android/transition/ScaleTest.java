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

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Build;
import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.FrameLayout;

import org.hamcrest.core.Is;
import org.junit.Test;
import org.junit.runner.RunWith;

import universum.studios.android.test.BaseInstrumentedTest;
import universum.studios.android.test.TestUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assume.assumeTrue;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = Build.VERSION_CODES.LOLLIPOP)
public final class ScaleTest extends BaseInstrumentedTest {

	@SuppressWarnings("unused")
	private static final String TAG = "ScaleTest";

	@Test
	public void testInstantiation() {
		assertThat(new Scale().getMode(), is(Scale.MODE_IN | Scale.MODE_OUT));
	}

	@Test
	public void testInstantiationWithMode() {
		assertThat(new Scale(Scale.MODE_IN).getMode(), is(Scale.MODE_IN));
		assertThat(new Scale(Scale.MODE_OUT).getMode(), is(Scale.MODE_OUT));
		assertThat(new Scale(Scale.MODE_IN | Scale.MODE_OUT).getMode(), is(Scale.MODE_IN | Scale.MODE_OUT));
	}

	@Test
	public void testInstantiationWithAttributeSet() {
		assertThat(new Scale(mContext, null).getMode(), is(Scale.MODE_IN | Scale.MODE_OUT));
	}

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

	@Test
	@SuppressWarnings("ResourceType")
	public void testCreateAnimator() {
		final View view = new View(mContext);
		assertThatAnimatorForViewIsValid(Scale.createAnimator(view, 0.25f, 0.75f), view);
		assertThatAnimatorForViewIsValid(Scale.createAnimator(view, -0.25f, 0.75f), view);
		assertThatAnimatorForViewIsValid(Scale.createAnimator(view, 0.25f, -0.75f), view);
	}

	private void assertThatAnimatorForViewIsValid(Animator animator, View view) {
		assertThat(animator, is(notNullValue()));
		assertThat(animator, instanceOf(ObjectAnimator.class));
		final ObjectAnimator objectAnimator = (ObjectAnimator) animator;
		assertThat(objectAnimator.getTarget(), Is.<Object>is(view));
		final PropertyValuesHolder[] values = objectAnimator.getValues();
		assertThat(values, is(notNullValue()));
		assertThat(values.length, is(2));
		assertThat(values[0].getPropertyName(), is(Scale.PROPERTY_SCALE_X));
		assertThat(values[1].getPropertyName(), is(Scale.PROPERTY_SCALE_Y));
	}

	@Test
	public void testSetGetPivotX() {
		final Scale scale = new Scale();
		for (int i = 0; i < 100; i++) {
			scale.setPivotX(i + 0.25f);
			assertThat(scale.getPivotX(), is(i + 0.25f));
		}
	}

	@Test
	public void testGetPivotXDefault() {
		assertThat(new Scale().getPivotX(), is(nullValue()));
	}

	@Test
	public void testSetGetPivotY() {
		final Scale scale = new Scale();
		for (int i = 0; i < 100; i++) {
			scale.setPivotY(i + 0.25f);
			assertThat(scale.getPivotY(), is(i + 0.25f));
		}
	}

	@Test
	public void testGetPivotYDefault() {
		assertThat(new Scale().getPivotY(), is(nullValue()));
	}

	@Test
	@SuppressWarnings("ResourceType")
	public void testSetGetPivotXFraction() {
		final Scale scale = new Scale();
		scale.setPivotXFraction(1.0f);
		assertThat(scale.getPivotXFraction(), is(1.0f));
		scale.setPivotXFraction(0.25f);
		assertThat(scale.getPivotXFraction(), is(0.25f));
		scale.setPivotXFraction(2f);
		assertThat(scale.getPivotXFraction(), is(1.0f));
		scale.setPivotXFraction(-1f);
		assertThat(scale.getPivotXFraction(), is(0.0f));
	}

	@Test
	public void testGetPivotXFractionDefault() {
		assertThat(new Scale().getPivotXFraction(), is(0.5f));
	}

	@Test
	@SuppressWarnings("ResourceType")
	public void testSetGetPivotYFraction() {
		final Scale scale = new Scale();
		scale.setPivotYFraction(1.0f);
		assertThat(scale.getPivotYFraction(), is(1.0f));
		scale.setPivotYFraction(0.25f);
		assertThat(scale.getPivotYFraction(), is(0.25f));
		scale.setPivotYFraction(2f);
		assertThat(scale.getPivotYFraction(), is(1.0f));
		scale.setPivotYFraction(-1f);
		assertThat(scale.getPivotYFraction(), is(0.0f));
	}

	@Test
	public void testGetPivotYFractionDefault() {
		assertThat(new Scale().getPivotYFraction(), is(0.5f));
	}

	@Test
	public void testOnAppearWithSpecifiedPivots() {
		final Scale scale = new Scale();
		scale.setPivotX(100f);
		scale.setPivotY(50f);
		final View view = new View(mContext);
		final Animator animator = scale.onAppear(new FrameLayout(mContext), view, null, null);
		assertThatAnimatorForViewIsValid(animator, view);
		assertThat(view.getPivotX(), is(scale.getPivotX()));
		assertThat(view.getPivotY(), is(scale.getPivotY()));
		assertThat(view.getScaleX(), is(0.0f));
		assertThat(view.getScaleY(), is(0.0f));
	}

	@Test
	public void testOnAppearWithSpecifiedPivotsFraction() {
		final Scale scale = new Scale();
		scale.setPivotXFraction(0.25f);
		scale.setPivotYFraction(0.75f);
		final View view = new View(mContext);
		view.setLeft(0);
		view.setRight(100);
		view.setTop(0);
		view.setBottom(100);
		final Animator animator = scale.onAppear(new FrameLayout(mContext), view, null, null);
		assertThatAnimatorForViewIsValid(animator, view);
		assertThat(view.getPivotX(), is(25f));
		assertThat(view.getPivotY(), is(75f));
		assertThat(view.getScaleX(), is(0.0f));
		assertThat(view.getScaleY(), is(0.0f));
	}

	@Test
	public void testOnDisappearWithSpecifiedPivots() {
		final Scale scale = new Scale();
		scale.setPivotX(100f);
		scale.setPivotY(50f);
		final View view = new View(mContext);
		final Animator animator = scale.onDisappear(new FrameLayout(mContext), view, null, null);
		assertThatAnimatorForViewIsValid(animator, view);
		assertThat(view.getPivotX(), is(scale.getPivotX()));
		assertThat(view.getPivotY(), is(scale.getPivotY()));
		assertThat(view.getScaleX(), is(1.0f));
		assertThat(view.getScaleY(), is(1.0f));
	}

	@Test
	public void testOnDisappearWithSpecifiedPivotsFraction() {
		final Scale scale = new Scale();
		scale.setPivotXFraction(0.33f);
		scale.setPivotYFraction(0.66f);
		final View view = new View(mContext);
		view.setLeft(0);
		view.setRight(100);
		view.setTop(0);
		view.setBottom(100);
		final Animator animator = scale.onDisappear(new FrameLayout(mContext), view, null, null);
		assertThatAnimatorForViewIsValid(animator, view);
		assertThat(view.getPivotX(), is(33f));
		assertThat(view.getPivotY(), is(66f));
		assertThat(view.getScaleX(), is(1.0f));
		assertThat(view.getScaleY(), is(1.0f));
	}
}
