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

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Build;
import android.transition.TransitionValues;
import android.view.View;
import android.widget.FrameLayout;

import org.hamcrest.core.Is;
import org.junit.Test;
import org.robolectric.annotation.Config;

import universum.studios.android.test.local.ViewTransitionTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * @author Martin Albedinsky
 */
@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
public final class ScaleTest extends ViewTransitionTestCase {

	@Test public void testInterpolator() {
		// Assert:
		assertThat(Scale.INTERPOLATOR, is(notNullValue()));
	}

	@Test public void testInstantiation() {
		// Act:
		final Scale scale = new Scale();
		// Assert:
		assertThat(scale.getMode(), is(Scale.MODE_IN | Scale.MODE_OUT));
		assertThat(scale.getPivotX(), is(nullValue()));
		assertThat(scale.getPivotY(), is(nullValue()));
		assertThat(scale.getPivotXFraction(), is(0.5f));
		assertThat(scale.getPivotYFraction(), is(0.5f));
	}

	@Test public void testInstantiationWithMode() {
		// Act + Assert:
		assertThat(new Scale(Scale.MODE_IN).getMode(), is(Scale.MODE_IN));
		assertThat(new Scale(Scale.MODE_OUT).getMode(), is(Scale.MODE_OUT));
		assertThat(new Scale(Scale.MODE_IN | Scale.MODE_OUT).getMode(), is(Scale.MODE_IN | Scale.MODE_OUT));
	}

	@Test public void testInstantiationWithAttributeSet() {
		// Act:
		final Scale scale = new Scale(application, null);
		// Assert:
		assertThat(scale.getMode(), is(Scale.MODE_IN | Scale.MODE_OUT));
	}

	@SuppressWarnings("ResourceType")
	@Test public void testCreateAnimator() {
		// Arrange:
		final View view = createViewAttachedToWindow();
		// Act + Assert:
		assertThatAnimatorIsValid(Scale.createAnimator(view, 0.25f, 0.75f), view, 0.25f, 0.25f);
		assertThatAnimatorIsValid(Scale.createAnimator(view, -0.25f, 0.75f), view, 0.0f, 0.0f);
		assertThatAnimatorIsValid(Scale.createAnimator(view, 0.25f, -0.75f), view, 0.25f, 0.25f);
	}

	@SuppressWarnings("ResourceType")
	@Test public void testCreateAnimatorWithSameStartAndEndValues() {
		// Arrange:
		final View view = createViewAttachedToWindow();
		// Act + Assert:
		assertThat(Scale.createAnimator(view, 1.0f, 1.0f), is(nullValue()));
		assertThat(Scale.createAnimator(view, -0.25f, -0.25f), is(nullValue()));
	}

	@SuppressWarnings("ResourceType")
	@Test public void testCreateAnimatorForAxesSeparately() {
		// Arrange:
		final View view = createViewAttachedToWindow();
		// Act + Assert:
		assertThatAnimatorIsValid(Scale.createAnimator(view, 0.25f, 0.75f, 0.15f, 0.85f), view, 0.25f, 0.75f);
		assertThatAnimatorIsValid(Scale.createAnimator(view, 0.25f, 0.75f, 0.15f, 0.75f), view, 0.25f, 0.75f);
		assertThatAnimatorIsValid(Scale.createAnimator(view, 0.15f, 0.75f, 0.15f, 0.85f), view, 0.15f, 0.75f);
		assertThatAnimatorIsValid(Scale.createAnimator(view, -0.25f, 0.75f, -0.15f, 1.25f), view, 0.0f, 0.75f);
		assertThatAnimatorIsValid(Scale.createAnimator(view, 0.25f, -0.75f, 0.0f, -0.75f), view, 0.25f, 0.0f);
	}

	@SuppressWarnings("ResourceType")
	@Test public void testCreateAnimatorForAxesSeparatelyWithSameStartAndEndValues() {
		// Arrange:
		final View view = createViewAttachedToWindow();
		// Act + Assert:
		assertThat(Scale.createAnimator(view, 0.25f, 0.25f, 0.25f, 0.25f), is(nullValue()));
		assertThat(Scale.createAnimator(view, -0.35f, -0.35f, -0.35f, -0.35f), is(nullValue()));
	}

	@Test public void testCreateAnimatorWithViewNotAttachedToWindow() {
		// Arrange:
		final View view = createViewNotAttachedToWindow();
		// Act + Assert:
		assertThat(Scale.createAnimator(view, 0.0f, 1.0f), is(nullValue()));
	}

	@Test public void testPivotX() {
		// Arrange:
		final Scale scale = new Scale();
		// Act + Assert:
		for (int i = 0; i < 100; i++) {
			scale.setPivotX(i + 0.25f);
			assertThat(scale.getPivotX(), is(i + 0.25f));
		}
	}

	@Test public void testSetGetPivotY() {
		// Arrange:
		final Scale scale = new Scale();
		// Act + Assert:
		for (int i = 0; i < 100; i++) {
			scale.setPivotY(i + 0.25f);
			assertThat(scale.getPivotY(), is(i + 0.25f));
		}
	}

	@Test public void testPivotXFraction() {
		// Arrange:
		final Scale scale = new Scale();
		// Act + Assert:
		scale.setPivotXFraction(1.0f);
		assertThat(scale.getPivotXFraction(), is(1.0f));
		scale.setPivotXFraction(0.25f);
		assertThat(scale.getPivotXFraction(), is(0.25f));
		scale.setPivotXFraction(2f);
		assertThat(scale.getPivotXFraction(), is(1.0f));
		scale.setPivotXFraction(-1f);
		assertThat(scale.getPivotXFraction(), is(0.0f));
	}

	@Test public void testPivotYFraction() {
		// Arrange:
		final Scale scale = new Scale();
		// Act + Assert:
		scale.setPivotYFraction(1.0f);
		assertThat(scale.getPivotYFraction(), is(1.0f));
		scale.setPivotYFraction(0.25f);
		assertThat(scale.getPivotYFraction(), is(0.25f));
		scale.setPivotYFraction(2f);
		assertThat(scale.getPivotYFraction(), is(1.0f));
		scale.setPivotYFraction(-1f);
		assertThat(scale.getPivotYFraction(), is(0.0f));
	}

	@Test public void testCaptureStartValues() {
		// Arrange:
		final View view = new View(application);
		view.setScaleX(0.75f);
		view.setScaleY(0.25f);
		final TransitionValues transitionValues = new TransitionValues();
		transitionValues.view = view;
		final Scale scale = new Scale();
		// Act:
		scale.captureStartValues(transitionValues);
		// Assert:
		assertThat(transitionValues.values.isEmpty(), is(false));
		assertThat(transitionValues.values.get(Scale.PROPERTY_TRANSITION_SCALE_X), Is.<Object>is(0.75f));
		assertThat(transitionValues.values.get(Scale.PROPERTY_TRANSITION_SCALE_Y), Is.<Object>is(0.25f));
	}

	@Test public void testObtainStartScales() {
		// Arrange:
		final TransitionValues values = new TransitionValues();
		values.values.put(Scale.PROPERTY_TRANSITION_SCALE_X, 0.15f);
		values.values.put(Scale.PROPERTY_TRANSITION_SCALE_Y, 0.95f);
		// Act:
		final float[] scales = Scale.obtainStartScales(values, 0.5f, 0.25f);
		// Assert:
		assertThat(scales, is(notNullValue()));
		assertThat(scales.length, is(2));
		assertThat(scales[0], is(0.15f));
		assertThat(scales[1], is(0.95f));
	}

	@Test public void testObtainStartScalesForEmptyTransitionValues() {
		// Act:
		final float[] scales = Scale.obtainStartScales(new TransitionValues(), 0.5f, 0.25f);
		// Assert:
		assertThat(scales, is(notNullValue()));
		assertThat(scales.length, is(2));
		assertThat(scales[0], is(0.5f));
		assertThat(scales[1], is(0.25f));
	}

	@Test public void testObtainStartScalesForNullTransitionValues() {
		// Act:
		final float[] scales = Scale.obtainStartScales(null, 0.5f, 0.25f);
		// Assert:
		assertThat(scales, is(notNullValue()));
		assertThat(scales.length, is(2));
		assertThat(scales[0], is(0.5f));
		assertThat(scales[1], is(0.25f));
	}

	@Test public void testOnAppearWithSpecifiedPivots() {
		// Arrange:
		final Scale scale = new Scale();
		scale.setPivotX(100f);
		scale.setPivotY(50f);
		final View view = createViewAttachedToWindow();
		// Act:
		final Animator animator = scale.onAppear(new FrameLayout(application), view, null, null);
		// Assert:
		assertThatAnimatorIsValid(animator, view, 0.0f, 0.0f);
		assertThat(view.getPivotX(), is(scale.getPivotX()));
		assertThat(view.getPivotY(), is(scale.getPivotY()));
	}

	@Test public void testOnAppearWithSpecifiedPivotsFraction() {
		// Arrange:
		final Scale scale = new Scale();
		scale.setPivotXFraction(0.25f);
		scale.setPivotYFraction(0.75f);
		final View view = createViewAttachedToWindow();
		view.setLeft(0);
		view.setRight(100);
		view.setTop(0);
		view.setBottom(100);
		// Act:
		final Animator animator = scale.onAppear(new FrameLayout(application), view, null, null);
		// Assert:
		assertThatAnimatorIsValid(animator, view, 0.0f, 0.0f);
		assertThat(view.getPivotX(), is(25f));
		assertThat(view.getPivotY(), is(75f));
	}

	@Test public void testOnAppearWithSameStartAndEndValues() {
		// Arrange:
		final Scale scale = new Scale();
		scale.setPivotX(100f);
		scale.setPivotY(50f);
		final View view = createViewAttachedToWindow();
		view.setScaleX(1.0f);
		view.setScaleY(1.0f);
		final TransitionValues startValues = new TransitionValues();
		startValues.view = view;
		scale.captureStartValues(startValues);
		// Act:
		final Animator animator = scale.onAppear(new FrameLayout(application), view, startValues, null);
		// Assert:
		assertThatAnimatorIsValid(animator, view, 0.0f, 0.0f);
		assertThat(view.getPivotX(), is(100f));
		assertThat(view.getPivotY(), is(50f));
	}

	@Test public void testOnAppearWithViewNotAttachedToWindow() {
		// Arrange:
		final View view = createViewNotAttachedToWindow();
		final Scale scale = new Scale();
		// Act + Assert:
		assertThat(scale.onAppear(new FrameLayout(application), view, null, null), is(nullValue()));
	}

	@Test public void testOnDisappearWithSpecifiedPivots() {
		// Arrange:
		final Scale scale = new Scale();
		scale.setPivotX(100f);
		scale.setPivotY(50f);
		final View view = createViewAttachedToWindow();
		// Act:
		final Animator animator = scale.onDisappear(new FrameLayout(application), view, null, null);
		// Assert:
		assertThatAnimatorIsValid(animator, view, 1.0f, 1.0f);
		assertThat(view.getPivotX(), is(scale.getPivotX()));
		assertThat(view.getPivotY(), is(scale.getPivotY()));
		assertThat(view.getScaleX(), is(1.0f));
		assertThat(view.getScaleY(), is(1.0f));
	}

	@Test public void testOnDisappearWithSpecifiedPivotsFraction() {
		// Arrange:
		final Scale scale = new Scale();
		scale.setPivotXFraction(0.33f);
		scale.setPivotYFraction(0.66f);
		final View view = createViewAttachedToWindow();
		view.setLeft(0);
		view.setRight(100);
		view.setTop(0);
		view.setBottom(100);
		// Act:
		final Animator animator = scale.onDisappear(new FrameLayout(application), view, new TransitionValues(), new TransitionValues());
		// Assert:
		assertThatAnimatorIsValid(animator, view, 1.0f, 1.0f);
		assertThat(view.getPivotX(), is(33f));
		assertThat(view.getPivotY(), is(66f));
		assertThat(view.getScaleX(), is(1.0f));
		assertThat(view.getScaleY(), is(1.0f));
	}

	@Test public void testOnDisappearWithSameStartAndEndValues() {
		// Arrange:
		final Scale scale = new Scale();
		scale.setPivotX(100f);
		scale.setPivotY(50f);
		final View view = createViewAttachedToWindow();
		view.setScaleX(0.0f);
		view.setScaleY(0.0f);
		final TransitionValues startValues = new TransitionValues();
		startValues.view = view;
		scale.captureStartValues(startValues);
		// Act + Assert:
		assertThat(scale.onDisappear(new FrameLayout(application), view, startValues, new TransitionValues()), is(nullValue()));
	}

	@Test public void testOnDisappearWithViewNotAttachedToWindow() {
		// Arrange:
		final View view = createViewNotAttachedToWindow();
		final Scale scale = new Scale();
		// Act:
		// Assert:
		assertThat(scale.onDisappear(new FrameLayout(application), view, null, null), is(nullValue()));
	}

	@Test public void testCalculateTransitionPropertiesWithSpecifiedPivots() {
		// Arrange:
		final Scale scale = new Scale();
		scale.setPivotX(100f);
		scale.setPivotY(50f);
		final View view = new View(application);
		// Act:
		scale.calculateTransitionProperties(view);
		// Assert:
		final Scale.Info info = scale.getInfo();
		assertThatInfoHasProperties(info, 100f, 50f);
	}

	@Test public void testCalculateTransitionPropertiesWithSpecifiedPivotFractions() {
		// Arrange:
		final Scale scale = new Scale();
		scale.setPivotXFraction(0.33f);
		scale.setPivotYFraction(0.66f);
		final View view = new View(application);
		view.setLeft(0);
		view.setRight(100);
		view.setTop(0);
		view.setBottom(100);
		// Act:
		scale.calculateTransitionProperties(view);
		// Assert:
		final Scale.Info info = scale.getInfo();
		assertThatInfoHasProperties(info, 33f, 66f);
	}

	private static void assertThatInfoHasProperties(Scale.Info info, float pivotX, float pivotY) {
		// Assert:
		assertThat(info.pivotX, is(pivotX));
		assertThat(info.pivotY, is(pivotY));
	}

	private void assertThatAnimatorIsValid(Animator animator, View view, float startScaleX, float startScaleY) {
		// Assert:
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
}