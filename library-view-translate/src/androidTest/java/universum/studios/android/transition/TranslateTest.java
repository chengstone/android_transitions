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
import android.support.annotation.Size;
import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;
import android.transition.TransitionValues;
import android.view.View;
import android.widget.FrameLayout;

import org.hamcrest.core.Is;
import org.junit.Test;
import org.junit.runner.RunWith;

import universum.studios.android.test.InstrumentedTestCase;
import universum.studios.android.test.instrumented.TestUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.number.OrderingComparison.greaterThanOrEqualTo;
import static org.hamcrest.number.OrderingComparison.lessThanOrEqualTo;
import static org.junit.Assume.assumeTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = Build.VERSION_CODES.LOLLIPOP)
public final class TranslateTest extends InstrumentedTestCase {

	@SuppressWarnings("unused")
	private static final String TAG = "TranslateTest";

	@Test
	public void testInstantiation() {
		assertThat(new Translate().getMode(), is(Scale.MODE_IN | Scale.MODE_OUT));
	}

	@Test
	public void testInstantiationWithMode() {
		assertThat(new Translate(Scale.MODE_IN).getMode(), is(Scale.MODE_IN));
		assertThat(new Translate(Scale.MODE_OUT).getMode(), is(Scale.MODE_OUT));
		assertThat(new Translate(Scale.MODE_IN | Scale.MODE_OUT).getMode(), is(Scale.MODE_IN | Scale.MODE_OUT));
	}

	@Test
	public void testInstantiationWithAttributeSet() {
		assertThat(new Translate(mContext, null).getMode(), is(Scale.MODE_IN | Scale.MODE_OUT));
	}

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

	@Test
	public void testCreateAnimator() {
		final Translate translate = new Translate();
		final View view = new View(mContext);
		final TransitionValues values = new TransitionValues();
		values.view = view;
		assertThatAnimatorForViewIsValid(Translate.createAnimator(
				translate,
				view,
				values,
				0, 0,
				0f, 0f,
				100f, 100f
		), view);
	}

	@Test
	public void testCreateAnimatorWithKnownLocationOnScreen() {
		final Translate translate = new Translate();
		final View view = new View(mContext);
		view.setTag(R.id.ui_transition_tag_position, new int[]{50, 50});
		final TransitionValues values = new TransitionValues();
		values.view = view;
		assertThatAnimatorForViewIsValid(Translate.createAnimator(
				translate,
				view,
				values,
				0, 0,
				0f, 0f,
				100f, 100f
		), view);
	}

	@Test
	public void testCreateAnimatorForTranslationChangedOnlyAlongXAxis() {
		final Translate translate = new Translate();
		final View view = new View(mContext);
		final TransitionValues values = new TransitionValues();
		values.view = view;
		assertThatAnimatorForViewIsValid(Translate.createAnimator(
				translate,
				view,
				values,
				0, 0,
				0f, 0f,
				100f, 0f
		), view);
	}

	@Test
	public void testCreateAnimatorForTranslationChangedOnlyAlongYAxis() {
		final Translate translate = new Translate();
		final View view = new View(mContext);
		final TransitionValues values = new TransitionValues();
		values.view = view;
		assertThatAnimatorForViewIsValid(Translate.createAnimator(
				translate,
				view,
				values,
				0, 0,
				0f, 0f,
				0f, 100f
		), view);
	}

	@Test
	public void testCreateAnimatorForZeroTranslationDeltas() {
		final Translate translate = new Translate();
		final View view = new View(mContext);
		final TransitionValues values = new TransitionValues();
		values.view = view;
		assertThat(Translate.createAnimator(
				translate,
				view,
				values,
				0, 0,
				0f, 0f,
				0f, 0f
		), is(nullValue()));
	}

	private void assertThatAnimatorForViewIsValid(Animator animator, View view) {
		assertThat(animator, is(notNullValue()));
		assertThat(animator, instanceOf(ObjectAnimator.class));
		final ObjectAnimator objectAnimator = (ObjectAnimator) animator;
		assertThat(objectAnimator.getTarget(), Is.<Object>is(view));
		final PropertyValuesHolder[] values = objectAnimator.getValues();
		assertThat(values, is(notNullValue()));
		assertThat(values.length, is(2));
		assertThat(values[0].getPropertyName(), is(View.TRANSLATION_X.getName()));
		assertThat(values[1].getPropertyName(), is(View.TRANSLATION_Y.getName()));
	}

	@Test
	public void testSetGetTranslationXDelta() {
		final Translate translate = new Translate();
		translate.setTranslationXDelta(200f);
		assertThat(translate.getTranslationXDelta(), is(200f));
		assertThat(translate.getTranslationYDelta(), is(0f));
		translate.setTranslationXDelta(-100f);
		assertThat(translate.getTranslationXDelta(), is(-100f));
		assertThat(translate.getTranslationYDelta(), is(0f));
	}

	@Test
	public void testGetTranslationXDeltaDefault() {
		assertThat(new Translate().getTranslationXDelta(), is(0f));
	}

	@Test
	public void testSetGetTranslationYDelta() {
		final Translate translate = new Translate();
		translate.setTranslationYDelta(-200f);
		assertThat(translate.getTranslationYDelta(), is(-200f));
		assertThat(translate.getTranslationXDelta(), is(0f));
		translate.setTranslationYDelta(100f);
		assertThat(translate.getTranslationYDelta(), is(100f));
		assertThat(translate.getTranslationXDelta(), is(0f));
	}

	@Test
	public void testGetTranslationYDeltaDefault() {
		assertThat(new Translate().getTranslationYDelta(), is(0f));
	}

	@Test
	public void testSetGetTranslationXRelativity() {
		final Translate translate = new Translate();
		translate.setTranslationXRelativity(Translate.Description.RELATIVE_TO_TARGET);
		assertThat(translate.getTranslationXRelativity(), is(Translate.Description.RELATIVE_TO_TARGET));
		translate.setTranslationXRelativity(Translate.Description.RELATIVE_TO_SCENE);
		assertThat(translate.getTranslationXRelativity(), is(Translate.Description.RELATIVE_TO_SCENE));
		translate.setTranslationXRelativity(Translate.Description.NONE);
		assertThat(translate.getTranslationXRelativity(), is(Translate.Description.NONE));
	}

	@Test
	public void testGetTranslationXRelativityDefault() {
		assertThat(new Translate().getTranslationXRelativity(), is(Translate.Description.NONE));
	}

	@Test
	public void testSetGetTranslationYRelativity() {
		final Translate translate = new Translate();
		translate.setTranslationYRelativity(Translate.Description.RELATIVE_TO_TARGET);
		assertThat(translate.getTranslationYRelativity(), is(Translate.Description.RELATIVE_TO_TARGET));
		translate.setTranslationYRelativity(Translate.Description.RELATIVE_TO_SCENE);
		assertThat(translate.getTranslationYRelativity(), is(Translate.Description.RELATIVE_TO_SCENE));
		translate.setTranslationYRelativity(Translate.Description.NONE);
		assertThat(translate.getTranslationYRelativity(), is(Translate.Description.NONE));
	}

	@Test
	public void testGetTranslationYRelativityDefault() {
		assertThat(new Translate().getTranslationYRelativity(), is(Translate.Description.NONE));
	}

	@Test
	public void testCaptureStartValues() {
		final int[] mockLocationOnScreen = new int[]{200, 400};
		final View mockView = new View(mContext) {

			@Override
			public void getLocationOnScreen(@Size(value = 2) int[] outLocation) {
				outLocation[0] = mockLocationOnScreen[0];
				outLocation[1] = mockLocationOnScreen[1];
			}
		};
		final TransitionValues values = new TransitionValues();
		values.view = mockView;
		new Translate().captureStartValues(values);
		final int[] locationOnScreen = (int[]) values.values.get(Translate.PROPERTY_TRANSITION_LOCATION_ON_SCREEN);
		assertThat(locationOnScreen, is(notNullValue()));
		assertThat(locationOnScreen.length, is(2));
		assertThat(locationOnScreen[0], is(mockLocationOnScreen[0]));
		assertThat(locationOnScreen[1], is(mockLocationOnScreen[1]));
	}

	@Test
	public void testCaptureEndValues() {
		final int[] mockLocationOnScreen = new int[]{100, 300};
		final View mockView = new View(mContext) {

			@Override
			public void getLocationOnScreen(@Size(value = 2) int[] outLocation) {
				outLocation[0] = mockLocationOnScreen[0];
				outLocation[1] = mockLocationOnScreen[1];
			}
		};
		final TransitionValues values = new TransitionValues();
		values.view = mockView;
		new Translate().captureEndValues(values);
		final int[] locationOnScreen = (int[]) values.values.get(Translate.PROPERTY_TRANSITION_LOCATION_ON_SCREEN);
		assertThat(locationOnScreen, is(notNullValue()));
		assertThat(locationOnScreen.length, is(2));
		assertThat(locationOnScreen[0], is(mockLocationOnScreen[0]));
		assertThat(locationOnScreen[1], is(mockLocationOnScreen[1]));
	}

	@Test
	public void testOnAppear() {
		final View view = new View(mContext);
		final TransitionValues values = new TransitionValues();
		values.view = view;
		values.values.put(Translate.PROPERTY_TRANSITION_LOCATION_ON_SCREEN, new int[]{0, 0});
		final Translate translate = new Translate();
		translate.setTranslationXDelta(-100f);
		translate.setTranslationYDelta(-100f);
		assertThatAnimatorForViewIsValid(translate.onAppear(new FrameLayout(mContext), view, null, values), view);
	}

	@Test
	public void testOnAppearWithZeroTranslationDeltas() {
		final Translate translate = new Translate();
		final View view = new View(mContext);
		final TransitionValues values = new TransitionValues();
		values.view = view;
		values.values.put(Translate.PROPERTY_TRANSITION_LOCATION_ON_SCREEN, new int[]{0, 0});
		assertThat(translate.onAppear(new FrameLayout(mContext), view, null, values), is(nullValue()));
	}

	@Test
	public void testOnAppearWithNullEndValues() {
		assertThat(new Translate().onAppear(new FrameLayout(mContext), new View(mContext), null, null), is(nullValue()));
	}

	@Test
	public void testOnAppearWithEndValuesWithoutLocationOnScreen() {
		final View view = new View(mContext);
		final TransitionValues values = new TransitionValues();
		values.view = view;
		assertThat(new Translate().onAppear(new FrameLayout(mContext), view, null, values), is(nullValue()));
	}

	@Test
	public void testOnDisappear() {
		final View view = new View(mContext);
		final TransitionValues values = new TransitionValues();
		values.view = view;
		values.values.put(Translate.PROPERTY_TRANSITION_LOCATION_ON_SCREEN, new int[]{0, 0});
		final Translate translate = new Translate();
		translate.setTranslationXDelta(100f);
		translate.setTranslationYDelta(100f);
		assertThatAnimatorForViewIsValid(translate.onDisappear(new FrameLayout(mContext), view, values, null), view);
	}

	@Test
	public void testOnDisappearWithZeroTranslationDeltas() {
		final View view = new View(mContext);
		final TransitionValues values = new TransitionValues();
		values.view = view;
		values.values.put(Translate.PROPERTY_TRANSITION_LOCATION_ON_SCREEN, new int[]{0, 0});
		assertThat(new Translate().onDisappear(new FrameLayout(mContext), view, values, null), is(nullValue()));
	}

	@Test
	public void testOnDisappearWithNullStartValues() {
		assertThat(new Translate().onDisappear(new FrameLayout(mContext), new View(mContext), null, null), is(nullValue()));
	}

	@Test
	public void testOnDisappearWithStartValuesWithoutLocationOnScreen() {
		final View view = new View(mContext);
		final TransitionValues values = new TransitionValues();
		values.view = view;
		assertThat(new Translate().onDisappear(new FrameLayout(mContext), view, values, null), is(nullValue()));
	}

	@Test
	public void testTransitionAnimatorListenerOnAnimationCancel() {
		final View animatingView = new View(mContext);
		animatingView.setTranslationX(100f);
		animatingView.setTranslationY(200f);
		final View viewInHierarchy = new View(mContext);
		viewInHierarchy.setTag(R.id.ui_transition_tag_position, new int[]{0, 0});
		new Translate.TransitionAnimatorListener(
				animatingView,
				viewInHierarchy,
				15f, 35f,
				0f, 0f
		).onAnimationCancel(mock(Animator.class));
		final int[] transitionPosition = (int[]) viewInHierarchy.getTag(R.id.ui_transition_tag_position);
		assertThat(transitionPosition, is(notNullValue()));
		assertThat(transitionPosition.length, is(2));
		assertThat(transitionPosition[0], is(Math.round(15f)));
		assertThat(transitionPosition[1], is(Math.round(35f)));
	}

	@Test
	public void testTransitionAnimatorListenerOnAnimationPauseResume() {
		final View animatingView = new View(mContext);
		final View viewInHierarchy = new View(mContext);
		final Translate.TransitionAnimatorListener listener = new Translate.TransitionAnimatorListener(
				animatingView,
				viewInHierarchy,
				0, 0,
				100f, 200f
		);
		final Animator animator = mock(Animator.class);
		animatingView.setTranslationX(50f);
		animatingView.setTranslationY(150f);
		listener.onAnimationPause(animator);
		assertThat(animatingView.getTranslationX(), is(100f));
		assertThat(animatingView.getTranslationY(), is(200f));
		listener.onAnimationResume(animator);
		assertThat(animatingView.getTranslationX(), is(50f));
		assertThat(animatingView.getTranslationY(), is(150f));
	}

	@Test
	public void testTransitionAnimatorListenerOnTransitionEnd() {
		final View animatingView = new View(mContext);
		final View viewInHierarchy = new View(mContext);
		new Translate.TransitionAnimatorListener(
				animatingView,
				viewInHierarchy,
				0, 0,
				15f, 35f
		).onTransitionEnd(new Translate());
		assertThat(animatingView.getTranslationX(), is(15f));
		assertThat(animatingView.getTranslationY(), is(35f));
	}

	@Test
	public void testTransitionAnimatorListenerOnTransitionCancel() {
		final View mockView = mock(FrameLayout.class);
		new Translate.TransitionAnimatorListener(
				mockView,
				mockView,
				1f, 2f,
				3f, 4f
		).onTransitionCancel(new Translate());
		verify(mockView, times(1)).getTranslationX();
		verify(mockView, times(1)).getTranslationY();
		verify(mockView, times(1)).getTag(R.id.ui_transition_tag_position);
		verifyNoMoreInteractions(mockView);
	}

	@Test
	public void testTransitionAnimatorListenerOnTransitionStart() {
		final View mockView = mock(FrameLayout.class);
		new Translate.TransitionAnimatorListener(
				mockView,
				mockView,
				1f, 2f,
				3f, 4f
		).onTransitionStart(new Translate());
		verify(mockView, times(1)).getTranslationX();
		verify(mockView, times(1)).getTranslationY();
		verify(mockView, times(1)).getTag(R.id.ui_transition_tag_position);
		verifyNoMoreInteractions(mockView);
	}

	@Test
	public void testTransitionAnimatorListenerOnTransitionPause() {
		final View mockView = mock(FrameLayout.class);
		new Translate.TransitionAnimatorListener(
				mockView,
				mockView,
				1f, 2f,
				3f, 4f
		).onTransitionPause(new Translate());
		verify(mockView, times(1)).getTranslationX();
		verify(mockView, times(1)).getTranslationY();
		verify(mockView, times(1)).getTag(R.id.ui_transition_tag_position);
		verifyNoMoreInteractions(mockView);
	}

	@Test
	public void testTransitionAnimatorListenerOnTransitionResume() {
		final View mockView = mock(FrameLayout.class);
		new Translate.TransitionAnimatorListener(
				mockView,
				mockView,
				1f, 2f,
				3f, 4f
		).onTransitionResume(new Translate());
		verify(mockView, times(1)).getTranslationX();
		verify(mockView, times(1)).getTranslationY();
		verify(mockView, times(1)).getTag(R.id.ui_transition_tag_position);
		verifyNoMoreInteractions(mockView);
	}
}
