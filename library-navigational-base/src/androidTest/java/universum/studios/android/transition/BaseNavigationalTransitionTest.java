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
import android.support.annotation.NonNull;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import universum.studios.android.test.BaseInstrumentedTest;
import universum.studios.android.test.TestActivity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class BaseNavigationalTransitionTest extends BaseInstrumentedTest {

	@SuppressWarnings("unused")
	private static final String TAG = "BaseNavigationalTransitionTest";

	private BaseNavigationalTransition<NavigationalTransitionImpl> mEmptyTransition;
	private BaseNavigationalTransition<NavigationalTransitionImpl> mTransitionWithActivity;

	@Override
	public void beforeTest() throws Exception {
		super.beforeTest();
		this.mEmptyTransition = new NavigationalTransitionImpl();
		this.mTransitionWithActivity = new NavigationalTransitionImpl(TestActivity.class);
	}

	@Override
	public void afterTest() throws Exception {
		super.afterTest();
		this.mEmptyTransition = null;
		this.mTransitionWithActivity = null;
	}

	@Test
	public void testSharedElementsUseOverlay() {
		assertThat(mEmptyTransition.sharedElementsUseOverlay(), is(false));
		mEmptyTransition.sharedElementsUseOverlay(true);
		assertThat(mEmptyTransition.sharedElementsUseOverlay(), is(true));
	}

	private static final class NavigationalTransitionImpl extends BaseNavigationalTransition<NavigationalTransitionImpl> {

		NavigationalTransitionImpl() {
			super();
		}

		NavigationalTransitionImpl(@NonNull Class<? extends Activity> classOfTransitionActivity) {
			super(classOfTransitionActivity);
		}
	}
}
