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
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import org.junit.Test;
import org.robolectric.annotation.Config;

import universum.studios.android.test.local.ViewTransitionTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.mockito.Mockito.mock;

/**
 * @author Martin Albedinsky
 */
@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
public final class RevealTest extends ViewTransitionTestCase {

	@Test public void testInstantiation() {
		// Act:
		final Reveal reveal = new Reveal();
		// Assert:
		assertThat(reveal.getMode(), is(Reveal.REVEAL));
		assertThat(reveal.getStartRadius(), is(nullValue()));
		assertThat(reveal.getEndRadius(), is(nullValue()));
		assertThat(reveal.getStartVisibility(), is(View.VISIBLE));
		assertThat(reveal.getEndVisibility(), is(View.VISIBLE));
		assertThat(reveal.getAppearVisibility(), is(View.VISIBLE));
		assertThat(reveal.getDisappearVisibility(), is(View.VISIBLE));
		assertThat(reveal.getCenterGravity(), is(nullValue()));
		assertThat(reveal.getCenterHorizontalOffset(), is(0));
		assertThat(reveal.getCenterVerticalOffset(), is(0));
		assertThat(reveal.getCenterX(), is(nullValue()));
		assertThat(reveal.getCenterY(), is(nullValue()));
		assertThat(reveal.getCenterXFraction(), is(0.5f));
		assertThat(reveal.getCenterYFraction(), is(0.5f));
	}

	@Test public void testInstantiationWithMode() {
		// Act + Assert:
		assertThat(new Reveal(Reveal.REVEAL).getMode(), is(Reveal.REVEAL));
		assertThat(new Reveal(Reveal.CONCEAL).getMode(), is(Reveal.CONCEAL));
	}

	@Test public void testInstantiationWithAttributeSet() {
		// Act:
		final Reveal reveal = new Reveal(application, null);
		// Assert:
		assertThat(reveal.getMode(), is(Reveal.REVEAL));
	}

	@Test public void testCalculateRadius() {
		// Arrange:
		final View view = new View(application);
		view.setLeft(0);
		view.setRight(100);
		view.setTop(0);
		view.setBottom(200);
		// Act + Assert:
		assertThat(Reveal.calculateRadius(view), is((float) Math.sqrt(Math.pow(view.getWidth(), 2) + Math.pow(view.getHeight(), 2))));
	}

	@Test public void testCalculateRadiusForDimensions() {
		// Act + Assert:
		assertThat(Reveal.calculateRadius(100, 200), is((float) Math.sqrt(Math.pow(100, 2) + Math.pow(200, 2))));
	}

	@Test public void testResolveCenterPosition() {
		// Arrange:
		final View view = new View(application);
		view.setLeft(0);
		view.setRight(100);
		view.setTop(0);
		view.setBottom(200);
		final float[] centerPosition = Reveal.resolveCenterPosition(view);
		// Act:
		final float[] center = Reveal.resolveCenter(view);
		// Assert:
		assertThat(centerPosition, is(notNullValue()));
		assertThat(centerPosition.length, is(2));
		assertThat(centerPosition[0], is(view.getX() + center[0]));
		assertThat(centerPosition[1], is(view.getY() + center[1]));
	}

	@Test public void testResolveCenterPositionForFractions() {
		// Arrange:
		final View view = new View(application);
		view.setLeft(0);
		view.setRight(100);
		view.setTop(0);
		view.setBottom(200);
		final float[] centerPosition = Reveal.resolveCenterPosition(view, 0.25f, 0.50f);
		// Act:
		final float[] center = Reveal.resolveCenter(view, 0.25f, 0.50f);
		// Assert:
		assertThat(centerPosition, is(notNullValue()));
		assertThat(centerPosition.length, is(2));
		assertThat(centerPosition[0], is(view.getX() + center[0]));
		assertThat(centerPosition[1], is(view.getY() + center[1]));
	}

	@Test public void testResolveCenter() {
		// Arrange:
		final View view = new View(application);
		view.setLeft(0);
		view.setRight(100);
		view.setTop(0);
		view.setBottom(200);
		// Act:
		final float[] center = Reveal.resolveCenter(view);
		// Assert:
		assertThat(center, is(notNullValue()));
		assertThat(center.length, is(2));
		assertThat(center[0], is(view.getWidth() * 0.50f));
		assertThat(center[1], is(view.getHeight() * 0.50f));
	}

	@Test public void testResolveCenterForFractions() {
		// Arrange:
		final View view = new View(application);
		view.setLeft(0);
		view.setRight(100);
		view.setTop(0);
		view.setBottom(200);
		// Act:
		final float[] center = Reveal.resolveCenter(view, 0.25f, 0.80f);
		// Assert:
		assertThat(center, is(notNullValue()));
		assertThat(center.length, is(2));
		assertThat(center[0], is(view.getWidth() * 0.25f));
		assertThat(center[1], is(view.getHeight() * 0.80f));
	}

	@Test public void testCreateAnimator() {
		// Arrange:
		final View view = createViewAttachedToWindow();
		// Act + Assert:
		assertThat(Reveal.createAnimator(view, 0, 100), is(notNullValue()));
	}

	@Test public void testCreateAnimatorForCenterCoordinates() {
		// Arrange:
		final View view = createViewAttachedToWindow();
		// Act + Assert:
		assertThat(Reveal.createAnimator(view, view.getWidth() / 2, view.getHeight() / 2, 0, 100), is(notNullValue()));
	}

	@Test public void testCreateAnimatorWithSameStartAndEndRadii() {
		// Arrange:
		final View view = new View(application);
		// Act + Assert:
		assertThat(Reveal.createAnimator(view, 100f, 100f), is(nullValue()));
	}

	@Test public void testMode() {
		// Arrange:
		final Reveal reveal = new Reveal();
		// Act + Assert:
		reveal.setMode(Reveal.CONCEAL);
		assertThat(reveal.getMode(), is(Reveal.CONCEAL));
		reveal.setMode(Reveal.REVEAL);
		assertThat(reveal.getMode(), is(Reveal.REVEAL));
	}

	@Test public void testStartRadius() {
		// Arrange:
		final Reveal reveal = new Reveal();
		// Act = Assert:
		for (int i = 0; i < 100; i++) {
			reveal.setStartRadius(i + 5f);
			assertThat(reveal.getStartRadius(), is(i + 5f));
		}
	}

	@Test public void testEndRadius() {
		// Arrange:
		final Reveal reveal = new Reveal();
		// Act + Assert:
		for (int i = 0; i < 100; i++) {
			reveal.setEndRadius(i + 5f);
			assertThat(reveal.getEndRadius(), is(i + 5f));
		}
	}

	@Test public void testStartVisibility() {
		// Arrange:
		final Reveal reveal = new Reveal();
		// Act + Assert:
		reveal.setStartVisibility(View.VISIBLE);
		assertThat(reveal.getStartVisibility(), is(View.VISIBLE));
		reveal.setStartVisibility(View.GONE);
		assertThat(reveal.getStartVisibility(), is(View.GONE));
	}

	@Test public void testEndVisibility() {
		// Arrange:
		final Reveal reveal = new Reveal();
		// Act + Assert:
		reveal.setEndVisibility(View.VISIBLE);
		assertThat(reveal.getEndVisibility(), is(View.VISIBLE));
		reveal.setEndVisibility(View.GONE);
		assertThat(reveal.getEndVisibility(), is(View.GONE));
	}

	@Test public void testAppearVisibility() {
		// Arrange:
		final Reveal reveal = new Reveal();
		// Act + Assert:
		reveal.setAppearVisibility(View.VISIBLE);
		assertThat(reveal.getAppearVisibility(), is(View.VISIBLE));
		reveal.setAppearVisibility(View.GONE);
		assertThat(reveal.getAppearVisibility(), is(View.GONE));
	}

	@Test public void testDisappearVisibility() {
		// Arrange:
		final Reveal reveal = new Reveal();
		// Act + Assert:
		reveal.setDisappearVisibility(View.VISIBLE);
		assertThat(reveal.getDisappearVisibility(), is(View.VISIBLE));
		reveal.setDisappearVisibility(View.GONE);
		assertThat(reveal.getDisappearVisibility(), is(View.GONE));
	}

	@Test public void testCenterGravity() {
		// Arrange:
		final Reveal reveal = new Reveal();
		// Act + Assert:
		reveal.setCenterGravity(Gravity.START | Gravity.BOTTOM);
		assertThat(reveal.getCenterGravity(), is(Gravity.START | Gravity.BOTTOM));
		reveal.setCenterGravity(Gravity.END);
		assertThat(reveal.getCenterGravity(), is(Gravity.END));
	}

	@Test public void testCenterHorizontalOffset() {
		// Arrange:
		final Reveal reveal = new Reveal();
		// Act + Assert:
		for (int i = 0; i < 100; i++) {
			reveal.setCenterHorizontalOffset(i);
			assertThat(reveal.getCenterHorizontalOffset(), is(i));
		}
	}

	@Test public void testCenterVerticalOffset() {
		// Arrange:
		final Reveal reveal = new Reveal();
		// Act + Assert:
		for (int i = 0; i < 100; i++) {
			reveal.setCenterVerticalOffset(i);
			assertThat(reveal.getCenterVerticalOffset(), is(i));
		}
	}

	@Test public void testCenterX() {
		// Arrange:
		final Reveal reveal = new Reveal();
		// Act + Assert:
		for (int i = 0; i < 100; i++) {
			reveal.setCenterX(i + 0.5f);
			assertThat(reveal.getCenterX(), is(i + 0.5f));
		}
	}

	@Test public void testCenterY() {
		// Arrange:
		final Reveal reveal = new Reveal();
		// Act + Assert:
		for (int i = 0; i < 100; i++) {
			reveal.setCenterY(i + 0.5f);
			assertThat(reveal.getCenterY(), is(i + 0.5f));
		}
	}

	@SuppressWarnings("ResourceType")
	@Test public void testCenterXFraction() {
		// Arrange:
		final Reveal reveal = new Reveal();
		// Act + Assert:
		reveal.setCenterXFraction(1.0f);
		assertThat(reveal.getCenterXFraction(), is(1.0f));
		reveal.setCenterXFraction(0.25f);
		assertThat(reveal.getCenterXFraction(), is(0.25f));
		reveal.setCenterXFraction(2f);
		assertThat(reveal.getCenterXFraction(), is(1.0f));
		reveal.setCenterXFraction(-1f);
		assertThat(reveal.getCenterXFraction(), is(0.0f));
	}

	@SuppressWarnings("ResourceType")
	@Test public void testCenterYFraction() {
		// Arrange:
		final Reveal reveal = new Reveal();
		// Act + Assert:
		reveal.setCenterYFraction(1.0f);
		assertThat(reveal.getCenterYFraction(), is(1.0f));
		reveal.setCenterYFraction(0.25f);
		assertThat(reveal.getCenterYFraction(), is(0.25f));
		reveal.setCenterYFraction(2f);
		assertThat(reveal.getCenterYFraction(), is(1.0f));
		reveal.setCenterYFraction(-1f);
		assertThat(reveal.getCenterYFraction(), is(0.0f));
	}

	@Test public void testOnAppear() {
		// Arrange:
		final View view = createViewAttachedToWindow();
		final Reveal reveal = new Reveal();
		// Act + Assert:
		assertThat(reveal.onAppear(new FrameLayout(application), view, null, null), is(notNullValue()));
	}

	@Test public void testOnAppearWithViewNotAttachedToWindow() {
		// Arrange:
		final View view = createViewNotAttachedToWindow();
		final Reveal reveal = new Reveal();
		// Act + Assert:
		assertThat(reveal.onAppear(new FrameLayout(application), view, null, null), is(nullValue()));
	}

	@Test public void testOnDisappear() {
		// Arrange:
		final View view = createViewAttachedToWindow();
		final Reveal reveal = new Reveal();
		// Act + Assert:
		assertThat(reveal.onDisappear(new FrameLayout(application), view, null, null), is(notNullValue()));
	}

	@Test public void testOnDisappearWithViewNotAttachedToWindow() {
		// Arrange:
		final View view = createViewNotAttachedToWindow();
		final Reveal reveal = new Reveal();
		// Act + Assert:
		assertThat(reveal.onDisappear(new FrameLayout(application), view, null, null), is(nullValue()));
	}

	@Test public void testCalculateTransitionPropertiesForCenterCoordinates() {
		// Arrange:
		final View view = new View(application);
		view.setLeft(0);
		view.setRight(100);
		view.setTop(0);
		view.setBottom(100);
		final Reveal reveal = new Reveal();
		reveal.setCenterX(75f);
		reveal.setCenterY(25f);
		// Act:
		reveal.calculateTransitionProperties(view);
		// Assert:
		assertThatInfoHasProperties(
				reveal.getInfo(),
				0,
				Reveal.calculateRadius(view.getWidth() * 0.75f, view.getHeight() * 0.75f),
				75f,
				25f
		);
	}

	@Test public void testCalculateTransitionPropertiesForCenterGravity() {
		// Arrange:
		final View view = new View(application);
		view.setLeft(0);
		view.setRight(100);
		view.setTop(0);
		view.setBottom(100);
		final Reveal reveal = new Reveal();
		// Act + Assert:
		// START -----------------------------------------------------------------------------------
		reveal.setCenterGravity(Gravity.START);
		reveal.calculateTransitionProperties(view);
		assertThatInfoHasProperties(
				reveal.getInfo(),
				0,
				Reveal.calculateRadius(view.getWidth(), view.getHeight()),
				0,
				0
		);
		// START | BOTTOM --------------------------------------------------------------------------
		reveal.setCenterGravity(Gravity.START | Gravity.BOTTOM);
		reveal.calculateTransitionProperties(view);
		assertThatInfoHasProperties(
				reveal.getInfo(),
				0,
				Reveal.calculateRadius(view.getWidth(), view.getHeight()),
				0,
				view.getHeight()
		);
		// START | CENTER_VERTICAL -----------------------------------------------------------------
		reveal.setCenterGravity(Gravity.START | Gravity.CENTER_VERTICAL);
		reveal.calculateTransitionProperties(view);
		assertThatInfoHasProperties(
				reveal.getInfo(),
				0,
				Reveal.calculateRadius(view.getWidth(), view.getHeight() / 2f),
				0,
				view.getHeight() / 2f
		);
		// END -------------------------------------------------------------------------------------
		reveal.setCenterGravity(Gravity.END);
		reveal.calculateTransitionProperties(view);
		assertThatInfoHasProperties(
				reveal.getInfo(),
				0,
				Reveal.calculateRadius(view.getWidth(), view.getHeight()),
				view.getWidth(),
				0
		);
		// END | BOTTOM ----------------------------------------------------------------------------
		reveal.setCenterGravity(Gravity.END | Gravity.BOTTOM);
		reveal.calculateTransitionProperties(view);
		assertThatInfoHasProperties(
				reveal.getInfo(),
				0,
				Reveal.calculateRadius(view.getWidth(), view.getHeight()),
				view.getWidth(),
				view.getHeight()
		);
		// END | CENTER_VERTICAL -------------------------------------------------------------------
		reveal.setCenterGravity(Gravity.END | Gravity.CENTER_VERTICAL);
		reveal.calculateTransitionProperties(view);
		assertThatInfoHasProperties(
				reveal.getInfo(),
				0,
				Reveal.calculateRadius(view.getWidth(), view.getHeight() / 2f),
				view.getWidth(),
				view.getHeight() / 2f
		);
		// TOP | CENTER_HORIZONTAL -----------------------------------------------------------------
		reveal.setCenterGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
		reveal.calculateTransitionProperties(view);
		assertThatInfoHasProperties(
				reveal.getInfo(),
				0,
				Reveal.calculateRadius(view.getWidth() / 2f, view.getHeight()),
				view.getWidth() / 2f,
				0
		);
		// BOTTOM | CENTER_HORIZONTAL --------------------------------------------------------------
		reveal.setCenterGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
		reveal.calculateTransitionProperties(view);
		assertThatInfoHasProperties(
				reveal.getInfo(),
				0,
				Reveal.calculateRadius(view.getWidth() / 2f, view.getHeight()),
				view.getWidth() / 2f,
				view.getHeight()
		);
		// CENTER ----------------------------------------------------------------------------------
		reveal.setCenterGravity(Gravity.CENTER);
		reveal.calculateTransitionProperties(view);
		assertThatInfoHasProperties(
				reveal.getInfo(),
				0,
				Reveal.calculateRadius(view.getWidth() / 2f, view.getHeight() / 2f),
				view.getWidth() / 2f,
				view.getHeight() / 2f
		);
	}

	@Test public void testCalculateTransitionPropertiesForConcealTransition() {
		// Arrange:
		final View view = new View(application);
		view.setLeft(0);
		view.setRight(100);
		view.setTop(0);
		view.setBottom(100);
		final Reveal reveal = new Reveal(Reveal.CONCEAL);
		reveal.setCenterX(35f);
		reveal.setCenterY(65f);
		// Act:
		reveal.calculateTransitionProperties(view);
		// Assert:
		assertThatInfoHasProperties(
				reveal.getInfo(),
				Reveal.calculateRadius(view.getWidth() * 0.65f, view.getHeight() * 0.65f),
				0,
				35f,
				65f
		);
	}

	@Test public void testCalculateTransitionPropertiesForStartRadii() {
		// Arrange:
		final View view = new View(application);
		view.setLeft(0);
		view.setRight(100);
		view.setTop(0);
		view.setBottom(100);
		final Reveal reveal = new Reveal();
		reveal.setStartRadius(40f);
		reveal.setEndRadius(80f);
		// Act:
		reveal.calculateTransitionProperties(view);
		// Assert:
		assertThatInfoHasProperties(
				reveal.getInfo(),
				40f,
				80f,
				view.getWidth() / 2f,
				view.getHeight() / 2f
		);
	}

	private static void assertThatInfoHasProperties(Reveal.Info info, float startRadius, float endRadius, float centerX, float centerY) {
		// Assert:
		assertThat(info.startRadius, is(startRadius));
		assertThat(info.endRadius, is(endRadius));
		assertThat(info.centerX, is(centerX));
		assertThat(info.centerY, is(centerY));
	}

	@Test public void testTransitionAnimatorListenerOnAnimationStart() {
		// Arrange:
		final View view = new View(application);
		final Reveal.TransitionAnimatorListener listener = new Reveal.TransitionAnimatorListener(view, View.INVISIBLE, View.GONE);
		// Act:
		listener.onAnimationStart(mock(Animator.class));
		// Assert:
		assertThat(view.getVisibility(), is(View.INVISIBLE));
	}

	@Test public void testTransitionAnimatorListenerOnAnimationEnd() {
		// Arrange:
		final View view = new View(application);
		final Reveal.TransitionAnimatorListener listener = new Reveal.TransitionAnimatorListener(view, View.INVISIBLE, View.GONE);
		// Act:
		listener.onAnimationEnd(mock(Animator.class));
		// Assert:
		assertThat(view.getVisibility(), is(View.GONE));
	}
}