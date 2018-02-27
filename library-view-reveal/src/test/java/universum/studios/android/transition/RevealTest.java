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

	@Test
	public void testInstantiation() {
		assertThat(new Reveal().getMode(), is(Reveal.REVEAL));
	}

	@Test
	public void testInstantiationWithMode() {
		assertThat(new Reveal(Reveal.REVEAL).getMode(), is(Reveal.REVEAL));
		assertThat(new Reveal(Reveal.CONCEAL).getMode(), is(Reveal.CONCEAL));
	}

	@Test
	public void testInstantiationWithAttributeSet() {
		assertThat(new Reveal(mApplication, null).getMode(), is(Reveal.REVEAL));
	}

	@Test
	public void testCalculateRadius() {
		final View view = new View(mApplication);
		view.setLeft(0);
		view.setRight(100);
		view.setTop(0);
		view.setBottom(200);
		assertThat(Reveal.calculateRadius(view), is((float) Math.sqrt(Math.pow(view.getWidth(), 2) + Math.pow(view.getHeight(), 2))));
	}

	@Test
	public void testCalculateRadiusForDimensions() {
		assertThat(Reveal.calculateRadius(100, 200), is((float) Math.sqrt(Math.pow(100, 2) + Math.pow(200, 2))));
	}

	@Test
	public void testResolveCenterPosition() {
		final View view = new View(mApplication);
		view.setLeft(0);
		view.setRight(100);
		view.setTop(0);
		view.setBottom(200);
		final float[] centerPosition = Reveal.resolveCenterPosition(view);
		final float[] center = Reveal.resolveCenter(view);
		assertThat(centerPosition, is(notNullValue()));
		assertThat(centerPosition.length, is(2));
		assertThat(centerPosition[0], is(view.getX() + center[0]));
		assertThat(centerPosition[1], is(view.getY() + center[1]));
	}

	@Test
	public void testResolveCenterPositionForFractions() {
		final View view = new View(mApplication);
		view.setLeft(0);
		view.setRight(100);
		view.setTop(0);
		view.setBottom(200);
		final float[] centerPosition = Reveal.resolveCenterPosition(view, 0.25f, 0.50f);
		final float[] center = Reveal.resolveCenter(view, 0.25f, 0.50f);
		assertThat(centerPosition, is(notNullValue()));
		assertThat(centerPosition.length, is(2));
		assertThat(centerPosition[0], is(view.getX() + center[0]));
		assertThat(centerPosition[1], is(view.getY() + center[1]));
	}

	@Test
	public void testResolveCenter() {
		final View view = new View(mApplication);
		view.setLeft(0);
		view.setRight(100);
		view.setTop(0);
		view.setBottom(200);
		final float[] center = Reveal.resolveCenter(view);
		assertThat(center, is(notNullValue()));
		assertThat(center.length, is(2));
		assertThat(center[0], is(view.getWidth() * 0.50f));
		assertThat(center[1], is(view.getHeight() * 0.50f));
	}

	@Test
	public void testResolveCenterForFractions() {
		final View view = new View(mApplication);
		view.setLeft(0);
		view.setRight(100);
		view.setTop(0);
		view.setBottom(200);
		final float[] center = Reveal.resolveCenter(view, 0.25f, 0.80f);
		assertThat(center, is(notNullValue()));
		assertThat(center.length, is(2));
		assertThat(center[0], is(view.getWidth() * 0.25f));
		assertThat(center[1], is(view.getHeight() * 0.80f));
	}

	@Test
	public void testCreateAnimator() {
		assertThat(Reveal.createAnimator(createViewAttachedToWindow(), 0, 100), is(notNullValue()));
	}

	@Test
	public void testCreateAnimatorForCenterCoordinates() {
		final View view = createViewAttachedToWindow();
		assertThat(Reveal.createAnimator(view, view.getWidth() / 2, view.getHeight() / 2, 0, 100), is(notNullValue()));
	}

	@Test
	public void testCreateAnimatorWithSameStartAndEndRadii() {
		assertThat(Reveal.createAnimator(new View(mApplication), 100f, 100f), is(nullValue()));
	}

	@Test
	public void testSetGetMode() {
		final Reveal reveal = new Reveal();
		reveal.setMode(Reveal.CONCEAL);
		assertThat(reveal.getMode(), is(Reveal.CONCEAL));
		reveal.setMode(Reveal.REVEAL);
		assertThat(reveal.getMode(), is(Reveal.REVEAL));
	}

	@Test
	public void testSetGetStartRadius() {
		final Reveal reveal = new Reveal();
		for (int i = 0; i < 100; i++) {
			reveal.setStartRadius(i + 5f);
			assertThat(reveal.getStartRadius(), is(i + 5f));
		}
	}

	@Test
	public void testGetStartRadiusDefault() {
		assertThat(new Reveal().getStartRadius(), is(nullValue()));
	}

	@Test
	public void testSetGetEndRadius() {
		final Reveal reveal = new Reveal();
		for (int i = 0; i < 100; i++) {
			reveal.setEndRadius(i + 5f);
			assertThat(reveal.getEndRadius(), is(i + 5f));
		}
	}

	@Test
	public void testGetEndRadiusDefault() {
		assertThat(new Reveal().getEndRadius(), is(nullValue()));
	}

	@Test
	public void testSetGetStartVisibility() {
		final Reveal reveal = new Reveal();
		reveal.setStartVisibility(View.VISIBLE);
		assertThat(reveal.getStartVisibility(), is(View.VISIBLE));
		reveal.setStartVisibility(View.GONE);
		assertThat(reveal.getStartVisibility(), is(View.GONE));
	}

	@Test
	public void testGetStartVisibilityDefault() {
		assertThat(new Reveal().getStartVisibility(), is(View.VISIBLE));
	}

	@Test
	public void testSetGetEndVisibility() {
		final Reveal reveal = new Reveal();
		reveal.setEndVisibility(View.VISIBLE);
		assertThat(reveal.getEndVisibility(), is(View.VISIBLE));
		reveal.setEndVisibility(View.GONE);
		assertThat(reveal.getEndVisibility(), is(View.GONE));
	}

	@Test
	public void testGetEndVisibilityDefault() {
		assertThat(new Reveal().getEndVisibility(), is(View.VISIBLE));
	}

	@Test
	public void testSetGetAppearVisibility() {
		final Reveal reveal = new Reveal();
		reveal.setAppearVisibility(View.VISIBLE);
		assertThat(reveal.getAppearVisibility(), is(View.VISIBLE));
		reveal.setAppearVisibility(View.GONE);
		assertThat(reveal.getAppearVisibility(), is(View.GONE));
	}

	@Test
	public void testGetAppearVisibilityDefault() {
		assertThat(new Reveal().getAppearVisibility(), is(View.VISIBLE));
	}

	@Test
	public void testSetGetDisappearVisibility() {
		final Reveal reveal = new Reveal();
		reveal.setDisappearVisibility(View.VISIBLE);
		assertThat(reveal.getDisappearVisibility(), is(View.VISIBLE));
		reveal.setDisappearVisibility(View.GONE);
		assertThat(reveal.getDisappearVisibility(), is(View.GONE));
	}

	@Test
	public void testGetDisappearVisibilityDefault() {
		assertThat(new Reveal().getDisappearVisibility(), is(View.VISIBLE));
	}

	@Test
	public void testSetGetCenterGravity() {
		final Reveal reveal = new Reveal();
		reveal.setCenterGravity(Gravity.START | Gravity.BOTTOM);
		assertThat(reveal.getCenterGravity(), is(Gravity.START | Gravity.BOTTOM));
		reveal.setCenterGravity(Gravity.END);
		assertThat(reveal.getCenterGravity(), is(Gravity.END));
	}

	@Test
	public void testGetCenterGravityDefault() {
		assertThat(new Reveal().getCenterGravity(), is(nullValue()));
	}

	@Test
	public void testSetGetCenterHorizontalOffset() {
		final Reveal reveal = new Reveal();
		for (int i = 0; i < 100; i++) {
			reveal.setCenterHorizontalOffset(i);
			assertThat(reveal.getCenterHorizontalOffset(), is(i));
		}
	}

	@Test
	public void testGetCenterHorizontalOffsetDefault() {
		assertThat(new Reveal().getCenterHorizontalOffset(), is(0));
	}

	@Test
	public void testSetGetCenterVerticalOffset() {
		final Reveal reveal = new Reveal();
		for (int i = 0; i < 100; i++) {
			reveal.setCenterVerticalOffset(i);
			assertThat(reveal.getCenterVerticalOffset(), is(i));
		}
	}

	@Test
	public void testGetCenterVerticalOffsetDefault() {
		assertThat(new Reveal().getCenterVerticalOffset(), is(0));
	}

	@Test
	public void testSetGetCenterX() {
		final Reveal reveal = new Reveal();
		for (int i = 0; i < 100; i++) {
			reveal.setCenterX(i + 0.5f);
			assertThat(reveal.getCenterX(), is(i + 0.5f));
		}
	}

	@Test
	public void testGetCenterXDefault() {
		assertThat(new Reveal().getCenterX(), is(nullValue()));
	}

	@Test
	public void testSetGetCenterY() {
		final Reveal reveal = new Reveal();
		for (int i = 0; i < 100; i++) {
			reveal.setCenterY(i + 0.5f);
			assertThat(reveal.getCenterY(), is(i + 0.5f));
		}
	}

	@Test
	public void testGetCenterYDefault() {
		assertThat(new Reveal().getCenterY(), is(nullValue()));
	}

	@Test
	@SuppressWarnings("ResourceType")
	public void testSetGetCenterXFraction() {
		final Reveal reveal = new Reveal();
		reveal.setCenterXFraction(1.0f);
		assertThat(reveal.getCenterXFraction(), is(1.0f));
		reveal.setCenterXFraction(0.25f);
		assertThat(reveal.getCenterXFraction(), is(0.25f));
		reveal.setCenterXFraction(2f);
		assertThat(reveal.getCenterXFraction(), is(1.0f));
		reveal.setCenterXFraction(-1f);
		assertThat(reveal.getCenterXFraction(), is(0.0f));
	}

	@Test
	public void testGetCenterXFractionDefault() {
		assertThat(new Reveal().getCenterXFraction(), is(0.5f));
	}

	@Test
	@SuppressWarnings("ResourceType")
	public void testSetGetCenterYFraction() {
		final Reveal reveal = new Reveal();
		reveal.setCenterYFraction(1.0f);
		assertThat(reveal.getCenterYFraction(), is(1.0f));
		reveal.setCenterYFraction(0.25f);
		assertThat(reveal.getCenterYFraction(), is(0.25f));
		reveal.setCenterYFraction(2f);
		assertThat(reveal.getCenterYFraction(), is(1.0f));
		reveal.setCenterYFraction(-1f);
		assertThat(reveal.getCenterYFraction(), is(0.0f));
	}

	@Test
	public void testGetCenterYFractionDefault() {
		assertThat(new Reveal().getCenterYFraction(), is(0.5f));
	}

	@Test
	public void testOnAppear() {
		assertThat(new Reveal().onAppear(new FrameLayout(mApplication), createViewAttachedToWindow(), null, null), is(notNullValue()));
	}

	@Test
	public void testOnAppearWithViewNotAttachedToWindow() {
		assertThat(new Reveal().onAppear(new FrameLayout(mApplication), createViewNotAttachedToWindow(), null, null), is(nullValue()));
	}

	@Test
	public void testOnDisappear() {
		assertThat(new Reveal().onDisappear(new FrameLayout(mApplication), createViewAttachedToWindow(), null, null), is(notNullValue()));
	}

	@Test
	public void testOnDisappearWithViewNotAttachedToWindow() {
		assertThat(new Reveal().onDisappear(new FrameLayout(mApplication), createViewNotAttachedToWindow(), null, null), is(nullValue()));
	}

	@Test
	public void testCalculateTransitionPropertiesForCenterCoordinates() {
		final View view = new View(mApplication);
		view.setLeft(0);
		view.setRight(100);
		view.setTop(0);
		view.setBottom(100);
		final Reveal reveal = new Reveal();
		reveal.setCenterX(75f);
		reveal.setCenterY(25f);
		reveal.calculateTransitionProperties(view);
		assertThatInfoHasProperties(
				reveal.getInfo(),
				0,
				Reveal.calculateRadius(view.getWidth() * 0.75f, view.getHeight() * 0.75f),
				75f,
				25f
		);
	}

	@Test
	public void testCalculateTransitionPropertiesForCenterGravity() {
		final View view = new View(mApplication);
		view.setLeft(0);
		view.setRight(100);
		view.setTop(0);
		view.setBottom(100);
		final Reveal reveal = new Reveal();
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

	@Test
	public void testCalculateTransitionPropertiesForConcealTransition() {
		final View view = new View(mApplication);
		view.setLeft(0);
		view.setRight(100);
		view.setTop(0);
		view.setBottom(100);
		final Reveal reveal = new Reveal(Reveal.CONCEAL);
		reveal.setCenterX(35f);
		reveal.setCenterY(65f);
		reveal.calculateTransitionProperties(view);
		assertThatInfoHasProperties(
				reveal.getInfo(),
				Reveal.calculateRadius(view.getWidth() * 0.65f, view.getHeight() * 0.65f),
				0,
				35f,
				65f
		);
	}

	@Test
	public void testCalculateTransitionPropertiesForStartRadii() {
		final View view = new View(mApplication);
		view.setLeft(0);
		view.setRight(100);
		view.setTop(0);
		view.setBottom(100);
		final Reveal reveal = new Reveal();
		reveal.setStartRadius(40f);
		reveal.setEndRadius(80f);
		reveal.calculateTransitionProperties(view);
		assertThatInfoHasProperties(
				reveal.getInfo(),
				40f,
				80f,
				view.getWidth() / 2f,
				view.getHeight() / 2f
		);
	}

	private static void assertThatInfoHasProperties(Reveal.Info info, float startRadius, float endRadius, float centerX, float centerY) {
		assertThat(info.startRadius, is(startRadius));
		assertThat(info.endRadius, is(endRadius));
		assertThat(info.centerX, is(centerX));
		assertThat(info.centerY, is(centerY));
	}

	@Test
	public void testTransitionAnimatorListenerOnAnimationStart() {
		final View view = new View(mApplication);
		new Reveal.TransitionAnimatorListener(view, View.INVISIBLE, View.GONE).onAnimationStart(mock(Animator.class));
		assertThat(view.getVisibility(), is(View.INVISIBLE));
	}

	@Test
	public void testTransitionAnimatorListenerOnAnimationEnd() {
		final View view = new View(mApplication);
		new Reveal.TransitionAnimatorListener(view, View.INVISIBLE, View.GONE).onAnimationEnd(mock(Animator.class));
		assertThat(view.getVisibility(), is(View.GONE));
	}
}
