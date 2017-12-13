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

import android.app.Activity;
import android.os.Parcel;

import org.junit.Test;

import universum.studios.android.test.local.RobolectricTestCase;
import universum.studios.android.test.local.TestActivity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @author Martin Albedinsky
 */
public final class BasicWindowTransitionTest extends RobolectricTestCase {

	@Test
	public void testInstantiationInOut() {
		final int enterAnimation = android.R.anim.fade_in;
		final int exitAnimation = android.R.anim.fade_out;
		final BasicWindowTransition transition = new BasicWindowTransition(enterAnimation, exitAnimation);
		assertThat(transition.getStartEnterAnimation(), is(enterAnimation));
		assertThat(transition.getStartExitAnimation(), is(exitAnimation));
		assertThat(transition.getFinishEnterAnimation(), is(BasicWindowTransition.NO_ANIMATION));
		assertThat(transition.getFinishExitAnimation(), is(BasicWindowTransition.NO_ANIMATION));
		assertThat(transition.getName(), is("UNSPECIFIED"));
	}

	@Test
	public void testInstantiationInOutBack() {
		final int startEnterAnimation = android.R.anim.fade_in;
		final int startExitAnimation = android.R.anim.fade_out;
		final int finishEnterAnimation = android.R.anim.slide_in_left;
		final int finishExitAnimation = android.R.anim.slide_out_right;
		final BasicWindowTransition transition = new BasicWindowTransition(startEnterAnimation, startExitAnimation, finishEnterAnimation, finishExitAnimation);
		assertThat(transition.getStartEnterAnimation(), is(startEnterAnimation));
		assertThat(transition.getStartExitAnimation(), is(startExitAnimation));
		assertThat(transition.getFinishEnterAnimation(), is(finishEnterAnimation));
		assertThat(transition.getFinishExitAnimation(), is(finishExitAnimation));
		assertThat(transition.getName(), is("UNSPECIFIED"));
	}

	@Test
	public void testInstantiationInOutBackAndName() {
		final int startEnterAnimation = android.R.anim.fade_in;
		final int startExitAnimation = android.R.anim.fade_out;
		final int finishEnterAnimation = android.R.anim.slide_in_left;
		final int finishExitAnimation = android.R.anim.slide_out_right;
		final BasicWindowTransition transition = new BasicWindowTransition(startEnterAnimation, startExitAnimation, finishEnterAnimation, finishExitAnimation, "TEST_TRANSITION");
		assertThat(transition.getStartEnterAnimation(), is(startEnterAnimation));
		assertThat(transition.getStartExitAnimation(), is(startExitAnimation));
		assertThat(transition.getFinishEnterAnimation(), is(finishEnterAnimation));
		assertThat(transition.getFinishExitAnimation(), is(finishExitAnimation));
		assertThat(transition.getName(), is("TEST_TRANSITION"));
	}

	@Test
	public void testCreatorCreateFromParcel() {
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
		final BasicWindowTransition transition = BasicWindowTransition.CREATOR.createFromParcel(parcel);
		assertThat(transition.getStartEnterAnimation(), is(startEnterAnimation));
		assertThat(transition.getStartExitAnimation(), is(startExitAnimation));
		assertThat(transition.getFinishEnterAnimation(), is(finishEnterAnimation));
		assertThat(transition.getFinishExitAnimation(), is(finishExitAnimation));
		assertThat(transition.getName(), is("TEST_TRANSITION"));
		parcel.recycle();
	}

	@Test
	public void testCreatorNewArray() {
		final BasicWindowTransition[] array = BasicWindowTransition.CREATOR.newArray(10);
		for (final BasicWindowTransition anArray : array) {
			assertThat(anArray, is(nullValue()));
		}
	}

	@Test
	public void testWriteToParcel() {
		final int startEnterAnimation = android.R.anim.fade_in;
		final int startExitAnimation = android.R.anim.fade_out;
		final int finishEnterAnimation = android.R.anim.slide_in_left;
		final int finishExitAnimation = android.R.anim.slide_out_right;
		final BasicWindowTransition transition = new BasicWindowTransition(startEnterAnimation, startExitAnimation, finishEnterAnimation, finishExitAnimation, "TEST_TRANSITION");
		final Parcel parcel = Parcel.obtain();
		transition.writeToParcel(parcel, 0);
		parcel.setDataPosition(0);
		assertThat(parcel.readInt(), is(startEnterAnimation));
		assertThat(parcel.readInt(), is(startExitAnimation));
		assertThat(parcel.readInt(), is(finishEnterAnimation));
		assertThat(parcel.readInt(), is(finishExitAnimation));
		assertThat(parcel.readString(), is("TEST_TRANSITION"));
		parcel.recycle();
	}

	@Test
	public void testDescribeContents() {
		assertThat(new BasicWindowTransition(0, 0).describeContents(), is(0));
	}

	@Test
	public void testOverrideStart() {
		final Activity mockActivity = mock(TestActivity.class);
		final int startEnterAnimation = android.R.anim.fade_in;
		final int startExitAnimation = android.R.anim.fade_out;
		final int finishEnterAnimation = android.R.anim.slide_in_left;
		final int finishExitAnimation = android.R.anim.slide_out_right;
		final BasicWindowTransition transition = new BasicWindowTransition(startEnterAnimation, startExitAnimation, finishEnterAnimation, finishExitAnimation);
		transition.overrideStart(mockActivity);
		verify(mockActivity, times(1)).overridePendingTransition(startEnterAnimation, startExitAnimation);
		verifyNoMoreInteractions(mockActivity);
	}

	@Test
	public void testOverrideStartSimple() {
		final Activity mockActivity = mock(TestActivity.class);
		final int enterAnimation = android.R.anim.fade_in;
		final int exitAnimation = android.R.anim.fade_out;
		final BasicWindowTransition transition = new BasicWindowTransition(enterAnimation, exitAnimation);
		transition.overrideStart(mockActivity);
		verify(mockActivity, times(1)).overridePendingTransition(enterAnimation, exitAnimation);
		verifyNoMoreInteractions(mockActivity);
	}

	@Test
	public void testOverrideFinish() {
		final Activity mockActivity = mock(TestActivity.class);
		final int startEnterAnimation = android.R.anim.fade_in;
		final int startExitAnimation = android.R.anim.fade_out;
		final int finishEnterAnimation = android.R.anim.slide_in_left;
		final int finishExitAnimation = android.R.anim.slide_out_right;
		final BasicWindowTransition transition = new BasicWindowTransition(startEnterAnimation, startExitAnimation, finishEnterAnimation, finishExitAnimation);
		transition.overrideFinish(mockActivity);
		verify(mockActivity, times(1)).overridePendingTransition(finishEnterAnimation, finishExitAnimation);
		verifyNoMoreInteractions(mockActivity);
	}

	@Test
	public void testOverrideFinishSimple() {
		final Activity mockActivity = mock(TestActivity.class);
		final int enterAnimation = android.R.anim.fade_in;
		final int exitAnimation = android.R.anim.fade_out;
		final BasicWindowTransition transition = new BasicWindowTransition(enterAnimation, exitAnimation);
		transition.overrideFinish(mockActivity);
		verify(mockActivity, times(1)).overridePendingTransition(BasicWindowTransition.NO_ANIMATION, BasicWindowTransition.NO_ANIMATION);
		verifyNoMoreInteractions(mockActivity);
	}
}
