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
import android.support.annotation.Size;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @author Martin Albedinsky
 */
@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
public final class TranslateTest extends ViewTransitionTestCase {

	@Test public void testInstantiation() {
		// Act:
		final Translate translate = new Translate();
		// Assert:
		assertThat(translate.getMode(), is(Scale.MODE_IN | Scale.MODE_OUT));
		assertThat(translate.getTranslationXDelta(), is(0f));
		assertThat(translate.getTranslationYDelta(), is(0f));
		assertThat(translate.getTranslationXRelativity(), is(Translate.Description.NONE));
		assertThat(translate.getTranslationYRelativity(), is(Translate.Description.NONE));
	}

	@Test public void testInstantiationWithMode() {
		// Act + Assert:
		assertThat(new Translate(Scale.MODE_IN).getMode(), is(Scale.MODE_IN));
		assertThat(new Translate(Scale.MODE_OUT).getMode(), is(Scale.MODE_OUT));
		assertThat(new Translate(Scale.MODE_IN | Scale.MODE_OUT).getMode(), is(Scale.MODE_IN | Scale.MODE_OUT));
	}

	@Test public void testInstantiationWithAttributeSet() {
		// Act:
		final Translate translate = new Translate(application, null);
		// Assert:
		assertThat(translate.getMode(), is(Scale.MODE_IN | Scale.MODE_OUT));
	}

	@Test public void testCreateAnimator() {
		// Arrange:
		final Translate translate = new Translate();
		final View view = createViewAttachedToWindow();
		final TransitionValues values = new TransitionValues();
		values.view = view;
		// Act + Assert:
		assertThatAnimatorForViewIsValid(Translate.createAnimator(
				translate,
				view,
				values,
				0, 0,
				0f, 0f,
				100f, 100f
		), view);
	}

	@Test public void testCreateAnimatorWithKnownLocationOnScreen() {
		// Arrange:
		final Translate translate = new Translate();
		final View view = createViewAttachedToWindow();
		view.setTag(R.id.ui_transition_tag_position, new int[]{50, 50});
		final TransitionValues values = new TransitionValues();
		values.view = view;
		// Act + Assert:
		assertThatAnimatorForViewIsValid(Translate.createAnimator(
				translate,
				view,
				values,
				0, 0,
				0f, 0f,
				100f, 100f
		), view);
	}

	@Test public void testCreateAnimatorForTranslationChangedOnlyAlongXAxis() {
		// Arrange:
		final Translate translate = new Translate();
		final View view = createViewAttachedToWindow();
		final TransitionValues values = new TransitionValues();
		values.view = view;
		// Act + Assert:
		assertThatAnimatorForViewIsValid(Translate.createAnimator(
				translate,
				view,
				values,
				0, 0,
				0f, 0f,
				100f, 0f
		), view);
	}

	@Test public void testCreateAnimatorForTranslationChangedOnlyAlongYAxis() {
		// Arrange:
		final Translate translate = new Translate();
		final View view = createViewAttachedToWindow();
		final TransitionValues values = new TransitionValues();
		values.view = view;
		// Act + Assert:
		assertThatAnimatorForViewIsValid(Translate.createAnimator(
				translate,
				view,
				values,
				0, 0,
				0f, 0f,
				0f, 100f
		), view);
	}

	@Test public void testCreateAnimatorForZeroTranslationDeltas() {
		// Arrange:
		final Translate translate = new Translate();
		final View view = new View(application);
		final TransitionValues values = new TransitionValues();
		values.view = view;
		// Act + Assert:
		assertThat(Translate.createAnimator(
				translate,
				view,
				values,
				0, 0,
				0f, 0f,
				0f, 0f
		), is(nullValue()));
	}

	@Test public void testCreateAnimatorWithViewNotAttachedToWindow() {
		// Act + Assert:
		assertThat(Translate.createAnimator(
				new Translate(),
				createViewNotAttachedToWindow(),
				new TransitionValues(),
				0, 0,
				0f, 0f,
				0f, 0f
		), is(nullValue()));
	}

	@Test public void testTranslationXDelta() {
		// Arrange:
		final Translate translate = new Translate();
		// Act + Assert:
		translate.setTranslationXDelta(200f);
		assertThat(translate.getTranslationXDelta(), is(200f));
		assertThat(translate.getTranslationYDelta(), is(0f));
		translate.setTranslationXDelta(-100f);
		assertThat(translate.getTranslationXDelta(), is(-100f));
		assertThat(translate.getTranslationYDelta(), is(0f));
	}

	@Test public void testTranslationYDelta() {
		// Arrange:
		final Translate translate = new Translate();
		// Act + Assert:
		translate.setTranslationYDelta(-200f);
		assertThat(translate.getTranslationYDelta(), is(-200f));
		assertThat(translate.getTranslationXDelta(), is(0f));
		translate.setTranslationYDelta(100f);
		assertThat(translate.getTranslationYDelta(), is(100f));
		assertThat(translate.getTranslationXDelta(), is(0f));
	}

	@Test public void testTranslationXRelativity() {
		// Arrange:
		final Translate translate = new Translate();
		// Act + Assert:
		translate.setTranslationXRelativity(Translate.Description.RELATIVE_TO_TARGET);
		assertThat(translate.getTranslationXRelativity(), is(Translate.Description.RELATIVE_TO_TARGET));
		translate.setTranslationXRelativity(Translate.Description.RELATIVE_TO_SCENE);
		assertThat(translate.getTranslationXRelativity(), is(Translate.Description.RELATIVE_TO_SCENE));
		translate.setTranslationXRelativity(Translate.Description.NONE);
		assertThat(translate.getTranslationXRelativity(), is(Translate.Description.NONE));
	}

	@Test public void testSetGetTranslationYRelativity() {
		// Arrange:
		final Translate translate = new Translate();
		// Act + Assert:
		translate.setTranslationYRelativity(Translate.Description.RELATIVE_TO_TARGET);
		assertThat(translate.getTranslationYRelativity(), is(Translate.Description.RELATIVE_TO_TARGET));
		translate.setTranslationYRelativity(Translate.Description.RELATIVE_TO_SCENE);
		assertThat(translate.getTranslationYRelativity(), is(Translate.Description.RELATIVE_TO_SCENE));
		translate.setTranslationYRelativity(Translate.Description.NONE);
		assertThat(translate.getTranslationYRelativity(), is(Translate.Description.NONE));
	}

	@Test public void testCaptureStartValues() {
		// Arrange:
		final int[] mockLocationOnScreen = new int[]{200, 400};
		final View mockView = new View(application) {

			@Override public void getLocationOnScreen(@Size(value = 2) final int[] outLocation) {
				outLocation[0] = mockLocationOnScreen[0];
				outLocation[1] = mockLocationOnScreen[1];
			}
		};
		final TransitionValues values = new TransitionValues();
		values.view = mockView;
		final Translate translate = new Translate();
		// Act:
		translate.captureStartValues(values);
		// Assert:
		final int[] locationOnScreen = (int[]) values.values.get(Translate.PROPERTY_TRANSITION_LOCATION_ON_SCREEN);
		assertThat(locationOnScreen, is(notNullValue()));
		assertThat(locationOnScreen.length, is(2));
		assertThat(locationOnScreen[0], is(mockLocationOnScreen[0]));
		assertThat(locationOnScreen[1], is(mockLocationOnScreen[1]));
	}

	@Test public void testCaptureEndValues() {
		// Arrange:
		final int[] mockLocationOnScreen = new int[]{100, 300};
		final View mockView = new View(application) {

			@Override public void getLocationOnScreen(@Size(value = 2) final int[] outLocation) {
				outLocation[0] = mockLocationOnScreen[0];
				outLocation[1] = mockLocationOnScreen[1];
			}
		};
		final TransitionValues values = new TransitionValues();
		values.view = mockView;
		final Translate translate = new Translate();
		// Act:
		translate.captureEndValues(values);
		// Assert:
		final int[] locationOnScreen = (int[]) values.values.get(Translate.PROPERTY_TRANSITION_LOCATION_ON_SCREEN);
		assertThat(locationOnScreen, is(notNullValue()));
		assertThat(locationOnScreen.length, is(2));
		assertThat(locationOnScreen[0], is(mockLocationOnScreen[0]));
		assertThat(locationOnScreen[1], is(mockLocationOnScreen[1]));
	}

	@Test public void testOnAppear() {
		// Arrange:
		final View view = createViewAttachedToWindow();
		final TransitionValues values = new TransitionValues();
		values.view = view;
		values.values.put(Translate.PROPERTY_TRANSITION_LOCATION_ON_SCREEN, new int[]{0, 0});
		final Translate translate = new Translate();
		translate.setTranslationXDelta(-100f);
		translate.setTranslationYDelta(-100f);
		// Act + Assert:
		assertThatAnimatorForViewIsValid(translate.onAppear(new FrameLayout(application), view, null, values), view);
	}

	@Test public void testOnAppearWithZeroTranslationDeltas() {
		// Arrange:
		final Translate translate = new Translate();
		final View view = new View(application);
		final TransitionValues values = new TransitionValues();
		values.view = view;
		values.values.put(Translate.PROPERTY_TRANSITION_LOCATION_ON_SCREEN, new int[]{0, 0});
		// Act + Assert:
		assertThat(translate.onAppear(new FrameLayout(application), view, null, values), is(nullValue()));
	}

	@Test public void testOnAppearWithNullEndValues() {
		// Arrange:
		final Translate translate = new Translate();
		// Act + Assert:
		assertThat(translate.onAppear(new FrameLayout(application), new View(application), null, null), is(nullValue()));
	}

	@Test public void testOnAppearWithEndValuesWithoutLocationOnScreen() {
		// Arrange:
		final View view = new View(application);
		final TransitionValues values = new TransitionValues();
		values.view = view;
		final Translate translate = new Translate();
		// Act + Assert:
		assertThat(translate.onAppear(new FrameLayout(application), view, null, values), is(nullValue()));
	}

	@Test public void testOnAppearWithViewNotAttachedToWindow() {
		// Arrange:
		final View view = createViewNotAttachedToWindow();
		final Translate translate = new Translate();
		// Act + Assert:
		assertThat(translate.onAppear(new FrameLayout(application), view, null, null), is(nullValue()));
	}

	@Test public void testOnDisappear() {
		// Arrange:
		final View view = createViewAttachedToWindow();
		final TransitionValues values = new TransitionValues();
		values.view = view;
		values.values.put(Translate.PROPERTY_TRANSITION_LOCATION_ON_SCREEN, new int[]{0, 0});
		final Translate translate = new Translate();
		translate.setTranslationXDelta(100f);
		translate.setTranslationYDelta(100f);
		// Act + Assert:
		assertThatAnimatorForViewIsValid(translate.onDisappear(new FrameLayout(application), view, values, null), view);
	}

	@Test public void testOnDisappearWithZeroTranslationDeltas() {
		// Arrange:
		final View view = new View(application);
		final TransitionValues values = new TransitionValues();
		values.view = view;
		values.values.put(Translate.PROPERTY_TRANSITION_LOCATION_ON_SCREEN, new int[]{0, 0});
		// Act + Assert:
		assertThat(new Translate().onDisappear(new FrameLayout(application), view, values, null), is(nullValue()));
	}

	@Test public void testOnDisappearWithNullStartValues() {
		// Arrange:
		final Translate translate = new Translate();
		// Act + Assert:
		assertThat(translate.onDisappear(new FrameLayout(application), new View(application), null, null), is(nullValue()));
	}

	@Test public void testOnDisappearWithStartValuesWithoutLocationOnScreen() {
		// Arrange:
		final View view = new View(application);
		final TransitionValues values = new TransitionValues();
		values.view = view;
		final Translate translate = new Translate();
		// Act + Assert:
		assertThat(translate.onDisappear(new FrameLayout(application), view, values, null), is(nullValue()));
	}

	@Test public void testOnDisappearWithViewNotAttachedToWindow() {
		// Arrange:
		final View view = createViewNotAttachedToWindow();
		final Translate translate = new Translate();
		// Act:
		// Assert:
		assertThat(translate.onDisappear(new FrameLayout(application), view, null, null), is(nullValue()));
	}

	@Test public void testTransitionAnimatorListenerOnAnimationCancel() {
		// Arrange:
		final View animatingView = new View(application);
		animatingView.setTranslationX(100f);
		animatingView.setTranslationY(200f);
		final View viewInHierarchy = new View(application);
		viewInHierarchy.setTag(R.id.ui_transition_tag_position, new int[]{0, 0});
		final Translate.TransitionAnimatorListener listener = new Translate.TransitionAnimatorListener(
				animatingView,
				viewInHierarchy,
				15f, 35f,
				0f, 0f
		);
		// Act:
		listener.onAnimationCancel(mock(Animator.class));
		// Assert:
		final int[] transitionPosition = (int[]) viewInHierarchy.getTag(R.id.ui_transition_tag_position);
		assertThat(transitionPosition, is(notNullValue()));
		assertThat(transitionPosition.length, is(2));
		assertThat(transitionPosition[0], is(Math.round(15f)));
		assertThat(transitionPosition[1], is(Math.round(35f)));
	}

	@Test public void testTransitionAnimatorListenerOnAnimationPauseResume() {
		// Arrange:
		final View animatingView = new View(application);
		final View viewInHierarchy = new View(application);
		final Translate.TransitionAnimatorListener listener = new Translate.TransitionAnimatorListener(
				animatingView,
				viewInHierarchy,
				0, 0,
				100f, 200f
		);
		final Animator animator = mock(Animator.class);
		animatingView.setTranslationX(50f);
		animatingView.setTranslationY(150f);
		// Act + Assert:
		listener.onAnimationPause(animator);
		assertThat(animatingView.getTranslationX(), is(100f));
		assertThat(animatingView.getTranslationY(), is(200f));
		listener.onAnimationResume(animator);
		assertThat(animatingView.getTranslationX(), is(50f));
		assertThat(animatingView.getTranslationY(), is(150f));
	}

	@Test public void testTransitionAnimatorListenerOnTransitionEnd() {
		// Arrange:
		final View animatingView = new View(application);
		final View viewInHierarchy = new View(application);
		final Translate.TransitionAnimatorListener listener = new Translate.TransitionAnimatorListener(
				animatingView,
				viewInHierarchy,
				0, 0,
				15f, 35f
		);
		// Act:
		listener.onTransitionEnd(new Translate());
		// Assert:
		assertThat(animatingView.getTranslationX(), is(15f));
		assertThat(animatingView.getTranslationY(), is(35f));
	}

	@Test public void testTransitionAnimatorListenerOnTransitionCancel() {
		// Arrange:
		final View mockView = mock(FrameLayout.class);
		final Translate.TransitionAnimatorListener listener = new Translate.TransitionAnimatorListener(
				mockView,
				mockView,
				1f, 2f,
				3f, 4f
		);
		// Act:
		listener.onTransitionCancel(new Translate());
		// Assert:
		verify(mockView).getTranslationX();
		verify(mockView).getTranslationY();
		verify(mockView).getTag(R.id.ui_transition_tag_position);
		verifyNoMoreInteractions(mockView);
	}

	@Test public void testTransitionAnimatorListenerOnTransitionStart() {
		// Arrange:
		final View mockView = mock(FrameLayout.class);
		final Translate.TransitionAnimatorListener listener = new Translate.TransitionAnimatorListener(
				mockView,
				mockView,
				1f, 2f,
				3f, 4f
		);
		// Act:
		listener.onTransitionStart(new Translate());
		// Assert:
		verify(mockView).getTranslationX();
		verify(mockView).getTranslationY();
		verify(mockView).getTag(R.id.ui_transition_tag_position);
		verifyNoMoreInteractions(mockView);
	}

	@Test public void testTransitionAnimatorListenerOnTransitionPause() {
		// Arrange:
		final View mockView = mock(FrameLayout.class);
		final Translate.TransitionAnimatorListener listener = new Translate.TransitionAnimatorListener(
				mockView,
				mockView,
				1f, 2f,
				3f, 4f
		);
		// Act:
		listener.onTransitionPause(new Translate());
		// Assert:
		verify(mockView).getTranslationX();
		verify(mockView).getTranslationY();
		verify(mockView).getTag(R.id.ui_transition_tag_position);
		verifyNoMoreInteractions(mockView);
	}

	@Test public void testTransitionAnimatorListenerOnTransitionResume() {
		// Arrange:
		final View mockView = mock(FrameLayout.class);
		final Translate.TransitionAnimatorListener listener = new Translate.TransitionAnimatorListener(
				mockView,
				mockView,
				1f, 2f,
				3f, 4f
		);
		// Act:
		listener.onTransitionResume(new Translate());
		// Assert:
		verify(mockView).getTranslationX();
		verify(mockView).getTranslationY();
		verify(mockView).getTag(R.id.ui_transition_tag_position);
		verifyNoMoreInteractions(mockView);
	}

	private void assertThatAnimatorForViewIsValid(Animator animator, View view) {
		// Assert:
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
}