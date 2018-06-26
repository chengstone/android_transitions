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

import android.app.Activity;
import android.os.Parcel;

import org.junit.Test;

import universum.studios.android.test.local.RobolectricTestCase;
import universum.studios.android.test.local.TestActivity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @author Martin Albedinsky
 */
public final class BasicWindowTransitionTest extends RobolectricTestCase {

	@Test public void testInstantiationInOut() {
		// Arrange:
		final int enterAnimation = android.R.anim.fade_in;
		final int exitAnimation = android.R.anim.fade_out;
		// Act:
		final BasicWindowTransition transition = new BasicWindowTransition(enterAnimation, exitAnimation);
		// Assert:
		assertThat(transition.getStartEnterAnimation(), is(enterAnimation));
		assertThat(transition.getStartExitAnimation(), is(exitAnimation));
		assertThat(transition.getFinishEnterAnimation(), is(BasicWindowTransition.NO_ANIMATION));
		assertThat(transition.getFinishExitAnimation(), is(BasicWindowTransition.NO_ANIMATION));
		assertThat(transition.getName(), is("UNSPECIFIED"));
	}

	@Test public void testInstantiationInOutBack() {
		// Arrange:
		final int startEnterAnimation = android.R.anim.fade_in;
		final int startExitAnimation = android.R.anim.fade_out;
		final int finishEnterAnimation = android.R.anim.slide_in_left;
		final int finishExitAnimation = android.R.anim.slide_out_right;
		// Act:
		final BasicWindowTransition transition = new BasicWindowTransition(startEnterAnimation, startExitAnimation, finishEnterAnimation, finishExitAnimation);
		// Assert:
		assertThat(transition.getStartEnterAnimation(), is(startEnterAnimation));
		assertThat(transition.getStartExitAnimation(), is(startExitAnimation));
		assertThat(transition.getFinishEnterAnimation(), is(finishEnterAnimation));
		assertThat(transition.getFinishExitAnimation(), is(finishExitAnimation));
		assertThat(transition.getName(), is("UNSPECIFIED"));
	}

	@Test public void testInstantiationInOutBackAndName() {
		// Arrange:
		final int startEnterAnimation = android.R.anim.fade_in;
		final int startExitAnimation = android.R.anim.fade_out;
		final int finishEnterAnimation = android.R.anim.slide_in_left;
		final int finishExitAnimation = android.R.anim.slide_out_right;
		// Act:
		final BasicWindowTransition transition = new BasicWindowTransition(startEnterAnimation, startExitAnimation, finishEnterAnimation, finishExitAnimation, "TEST_TRANSITION");
		// Assert:
		assertThat(transition.getStartEnterAnimation(), is(startEnterAnimation));
		assertThat(transition.getStartExitAnimation(), is(startExitAnimation));
		assertThat(transition.getFinishEnterAnimation(), is(finishEnterAnimation));
		assertThat(transition.getFinishExitAnimation(), is(finishExitAnimation));
		assertThat(transition.getName(), is("TEST_TRANSITION"));
	}

	@Test public void testCreatorCreateFromParcel() {
		// Arrange:
		final int startEnterAnimation = android.R.anim.fade_in;
		final int startExitAnimation = android.R.anim.fade_out;
		final int finishEnterAnimation = android.R.anim.slide_in_left;
		final int finishExitAnimation = android.R.anim.slide_out_right;
		final Parcel parcel = Parcel.obtain();
		parcel.writeInt(startEnterAnimation);
		parcel.writeInt(startExitAnimation);
		parcel.writeInt(finishEnterAnimation);
		parcel.writeInt(finishExitAnimation);
		parcel.writeString("TEST_TRANSITION");
		parcel.setDataPosition(0);
		// Act:
		final BasicWindowTransition transition = BasicWindowTransition.CREATOR.createFromParcel(parcel);
		// Assert:
		assertThat(transition.getStartEnterAnimation(), is(startEnterAnimation));
		assertThat(transition.getStartExitAnimation(), is(startExitAnimation));
		assertThat(transition.getFinishEnterAnimation(), is(finishEnterAnimation));
		assertThat(transition.getFinishExitAnimation(), is(finishExitAnimation));
		assertThat(transition.getName(), is("TEST_TRANSITION"));
		parcel.recycle();
	}

	@Test public void testCreatorNewArray() {
		// Act:
		final BasicWindowTransition[] array = BasicWindowTransition.CREATOR.newArray(10);
		// Assert:
		for (final BasicWindowTransition anArray : array) {
			assertThat(anArray, is(nullValue()));
		}
	}

	@Test public void testWriteToParcel() {
		// Arrange:
		final int startEnterAnimation = android.R.anim.fade_in;
		final int startExitAnimation = android.R.anim.fade_out;
		final int finishEnterAnimation = android.R.anim.slide_in_left;
		final int finishExitAnimation = android.R.anim.slide_out_right;
		final BasicWindowTransition transition = new BasicWindowTransition(startEnterAnimation, startExitAnimation, finishEnterAnimation, finishExitAnimation, "TEST_TRANSITION");
		final Parcel parcel = Parcel.obtain();
		// Act:
		transition.writeToParcel(parcel, 0);
		// Assert:
		parcel.setDataPosition(0);
		assertThat(parcel.readInt(), is(startEnterAnimation));
		assertThat(parcel.readInt(), is(startExitAnimation));
		assertThat(parcel.readInt(), is(finishEnterAnimation));
		assertThat(parcel.readInt(), is(finishExitAnimation));
		assertThat(parcel.readString(), is("TEST_TRANSITION"));
		parcel.recycle();
	}

	@Test public void testDescribeContents() {
		// Arrange:
		final BasicWindowTransition transition = new BasicWindowTransition(0, 0);
		// Act + Assert:
		assertThat(transition.describeContents(), is(0));
	}

	@Test public void testOverrideStart() {
		// Arrange:
		final Activity mockActivity = mock(TestActivity.class);
		final int startEnterAnimation = android.R.anim.fade_in;
		final int startExitAnimation = android.R.anim.fade_out;
		final int finishEnterAnimation = android.R.anim.slide_in_left;
		final int finishExitAnimation = android.R.anim.slide_out_right;
		final BasicWindowTransition transition = new BasicWindowTransition(startEnterAnimation, startExitAnimation, finishEnterAnimation, finishExitAnimation);
		// Act:
		transition.overrideStart(mockActivity);
		// Assert:
		verify(mockActivity).overridePendingTransition(startEnterAnimation, startExitAnimation);
		verifyNoMoreInteractions(mockActivity);
	}

	@Test public void testOverrideStartSimple() {
		// Arrange:
		final Activity mockActivity = mock(TestActivity.class);
		final int enterAnimation = android.R.anim.fade_in;
		final int exitAnimation = android.R.anim.fade_out;
		final BasicWindowTransition transition = new BasicWindowTransition(enterAnimation, exitAnimation);
		// Act:
		transition.overrideStart(mockActivity);
		// Assert:
		verify(mockActivity).overridePendingTransition(enterAnimation, exitAnimation);
		verifyNoMoreInteractions(mockActivity);
	}

	@Test public void testOverrideFinish() {
		// Arrange:
		final Activity mockActivity = mock(TestActivity.class);
		final int startEnterAnimation = android.R.anim.fade_in;
		final int startExitAnimation = android.R.anim.fade_out;
		final int finishEnterAnimation = android.R.anim.slide_in_left;
		final int finishExitAnimation = android.R.anim.slide_out_right;
		final BasicWindowTransition transition = new BasicWindowTransition(startEnterAnimation, startExitAnimation, finishEnterAnimation, finishExitAnimation);
		// Act:
		transition.overrideFinish(mockActivity);
		// Assert:
		verify(mockActivity).overridePendingTransition(finishEnterAnimation, finishExitAnimation);
		verifyNoMoreInteractions(mockActivity);
	}

	@Test public void testOverrideFinishSimple() {
		// Arrange:
		final Activity mockActivity = mock(TestActivity.class);
		final int enterAnimation = android.R.anim.fade_in;
		final int exitAnimation = android.R.anim.fade_out;
		final BasicWindowTransition transition = new BasicWindowTransition(enterAnimation, exitAnimation);
		// Act:
		transition.overrideFinish(mockActivity);
		// Assert:
		verify(mockActivity).overridePendingTransition(BasicWindowTransition.NO_ANIMATION, BasicWindowTransition.NO_ANIMATION);
		verifyNoMoreInteractions(mockActivity);
	}
}