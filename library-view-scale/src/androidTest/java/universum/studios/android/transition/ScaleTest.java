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
import android.transition.TransitionValues;
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
	public void testInterpolator() {
		assertThat(Scale.INTERPOLATOR, is(notNullValue()));
	}

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
		assertThatAnimatorIsValid(Scale.createAnimator(view, 0.25f, 0.75f), view, 0.25f, 0.25f);
		assertThatAnimatorIsValid(Scale.createAnimator(view, -0.25f, 0.75f), view, 0.0f, 0.0f);
		assertThatAnimatorIsValid(Scale.createAnimator(view, 0.25f, -0.75f), view, 0.25f, 0.25f);
	}

	@Test
	@SuppressWarnings("ResourceType")
	public void testCreateAnimatorWithSameStartAndEndValues() {
		final View view = new View(mContext);
		assertThat(Scale.createAnimator(view, 1.0f, 1.0f), is(nullValue()));
		assertThat(Scale.createAnimator(view, -0.25f, -0.25f), is(nullValue()));
	}

	@Test
	@SuppressWarnings("ResourceType")
	public void testCreateAnimatorForAxesSeparately() {
		final View view = new View(mContext);
		assertThatAnimatorIsValid(Scale.createAnimator(view, 0.25f, 0.75f, 0.15f, 0.85f), view, 0.25f, 0.75f);
		assertThatAnimatorIsValid(Scale.createAnimator(view, 0.25f, 0.75f, 0.15f, 0.75f), view, 0.25f, 0.75f);
		assertThatAnimatorIsValid(Scale.createAnimator(view, 0.15f, 0.75f, 0.15f, 0.85f), view, 0.15f, 0.75f);
		assertThatAnimatorIsValid(Scale.createAnimator(view, -0.25f, 0.75f, -0.15f, 1.25f), view, 0.0f, 0.75f);
		assertThatAnimatorIsValid(Scale.createAnimator(view, 0.25f, -0.75f, 0.0f, -0.75f), view, 0.25f, 0.0f);
	}

	@Test
	@SuppressWarnings("ResourceType")
	public void testCreateAnimatorForAxesSeparatelyWithSameStartAndEndValues() {
		final View view = new View(mContext);
		assertThat(Scale.createAnimator(view, 0.25f, 0.25f, 0.25f, 0.25f), is(nullValue()));
		assertThat(Scale.createAnimator(view, -0.35f, -0.35f, -0.35f, -0.35f), is(nullValue()));
	}

	private void assertThatAnimatorIsValid(Animator animator, View view, float startScaleX, float startScaleY) {
		assertThat(animator, is(notNullValue()));
		assertThat(animator, instanceOf(ObjectAnimator.class));
		final ObjectAnimator objectAnimator = (ObjectAnimator) animator;
		assertThat(objectAnimator.getTarget(), Is.<Object>is(view));
		final PropertyValuesHolder[] values = objectAnimator.getValues();
		assertThat(values, is(notNullValue()));
		assertThat(values.length, is(2));
		assertThat(values[0].getPropertyName(), is(Scale.PROPERTY_SCALE_X));
		assertThat(values[1].getPropertyName(), is(Scale.PROPERTY_SCALE_Y));
		assertThat(view.getScaleX(), is(startScaleX));
		assertThat(view.getScaleY(), is(startScaleY));
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
	public void testCaptureStartValues() {
		final View view = new View(mContext);
		view.setScaleX(0.75f);
		view.setScaleY(0.25f);
		final TransitionValues transitionValues = new TransitionValues();
		transitionValues.view = view;
		new Scale().captureStartValues(transitionValues);
		assertThat(transitionValues.values.isEmpty(), is(false));
		assertThat(transitionValues.values.get(Scale.PROPERTY_TRANSITION_SCALE_X), Is.<Object>is(0.75f));
		assertThat(transitionValues.values.get(Scale.PROPERTY_TRANSITION_SCALE_Y), Is.<Object>is(0.25f));
	}

	@Test
	public void testObtainStartScales() {
		final TransitionValues values = new TransitionValues();
		values.values.put(Scale.PROPERTY_TRANSITION_SCALE_X, 0.15f);
		values.values.put(Scale.PROPERTY_TRANSITION_SCALE_Y, 0.95f);
		final float[] scales = Scale.obtainStartScales(values, 0.5f, 0.25f);
		assertThat(scales, is(notNullValue()));
		assertThat(scales.length, is(2));
		assertThat(scales[0], is(0.15f));
		assertThat(scales[1], is(0.95f));
	}

	@Test
	public void testObtainStartScalesForEmptyTransitionValues() {
		final float[] scales = Scale.obtainStartScales(new TransitionValues(), 0.5f, 0.25f);
		assertThat(scales, is(notNullValue()));
		assertThat(scales.length, is(2));
		assertThat(scales[0], is(0.5f));
		assertThat(scales[1], is(0.25f));
	}

	@Test
	public void testObtainStartScalesForNullTransitionValues() {
		final float[] scales = Scale.obtainStartScales(null, 0.5f, 0.25f);
		assertThat(scales, is(notNullValue()));
		assertThat(scales.length, is(2));
		assertThat(scales[0], is(0.5f));
		assertThat(scales[1], is(0.25f));
	}

	@Test
	public void testOnAppearWithSpecifiedPivots() {
		final Scale scale = new Scale();
		scale.setPivotX(100f);
		scale.setPivotY(50f);
		final View view = new View(mContext);
		final Animator animator = scale.onAppear(new FrameLayout(mContext), view, null, null);
		assertThatAnimatorIsValid(animator, view, 0.0f, 0.0f);
		assertThat(view.getPivotX(), is(scale.getPivotX()));
		assertThat(view.getPivotY(), is(scale.getPivotY()));
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
		assertThatAnimatorIsValid(animator, view, 0.0f, 0.0f);
		assertThat(view.getPivotX(), is(25f));
		assertThat(view.getPivotY(), is(75f));
	}

	@Test
	public void testOnAppearWithSameStartAndEndValues() {
		final Scale scale = new Scale();
		scale.setPivotX(100f);
		scale.setPivotY(50f);
		final View view = new View(mContext);
		view.setScaleX(1.0f);
		view.setScaleY(1.0f);
		final TransitionValues startValues = new TransitionValues();
		startValues.view = view;
		scale.captureStartValues(startValues);
		final Animator animator = scale.onAppear(new FrameLayout(mContext), view, startValues, null);
		assertThatAnimatorIsValid(animator, view, 0.0f, 0.0f);
		assertThat(view.getPivotX(), is(100f));
		assertThat(view.getPivotY(), is(50f));
	}

	@Test
	public void testOnDisappearWithSpecifiedPivots() {
		final Scale scale = new Scale();
		scale.setPivotX(100f);
		scale.setPivotY(50f);
		final View view = new View(mContext);
		final Animator animator = scale.onDisappear(new FrameLayout(mContext), view, null, null);
		assertThatAnimatorIsValid(animator, view, 1.0f, 1.0f);
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
		final Animator animator = scale.onDisappear(new FrameLayout(mContext), view, new TransitionValues(), new TransitionValues());
		assertThatAnimatorIsValid(animator, view, 1.0f, 1.0f);
		assertThat(view.getPivotX(), is(33f));
		assertThat(view.getPivotY(), is(66f));
		assertThat(view.getScaleX(), is(1.0f));
		assertThat(view.getScaleY(), is(1.0f));
	}

	@Test
	public void testOnDisappearWithSameStartAndEndValues() {
		final Scale scale = new Scale();
		scale.setPivotX(100f);
		scale.setPivotY(50f);
		final View view = new View(mContext);
		view.setScaleX(0.0f);
		view.setScaleY(0.0f);
		final TransitionValues startValues = new TransitionValues();
		startValues.view = view;
		scale.captureStartValues(startValues);
		assertThat(scale.onDisappear(new FrameLayout(mContext), view, startValues, new TransitionValues()), is(nullValue()));
	}

	@Test
	public void testCalculateTransitionPropertiesWithSpecifiedPivots() {
		final Scale scale = new Scale();
		scale.setPivotX(100f);
		scale.setPivotY(50f);
		final View view = new View(mContext);
		scale.calculateTransitionProperties(view);
		final Scale.Info info = scale.getInfo();
		assertThatInfoHasProperties(
				info,
				100f,
				50f
		);
	}

	@Test
	public void testCalculateTransitionPropertiesWithSpecifiedPivotFractions() {
		final Scale scale = new Scale();
		scale.setPivotXFraction(0.33f);
		scale.setPivotYFraction(0.66f);
		final View view = new View(mContext);
		view.setLeft(0);
		view.setRight(100);
		view.setTop(0);
		view.setBottom(100);
		scale.calculateTransitionProperties(view);
		final Scale.Info info = scale.getInfo();
		assertThatInfoHasProperties(
				info,
				33f,
				66f
		);
	}

	private static void assertThatInfoHasProperties(Scale.Info info, float pivotX, float pivotY) {
		assertThat(info.pivotX, is(pivotX));
		assertThat(info.pivotY, is(pivotY));
	}
}
