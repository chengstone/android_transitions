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

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentHostCallback;

import java.lang.reflect.Field;

import static org.mockito.Mockito.mock;

/**
 * @author Martin Albedinsky
 */
@SuppressWarnings("unused") final class NavigationalCompatTests {

	private static abstract class MockFragmentFactory {

		abstract Fragment createMockFragmentWithActivity(FragmentActivity activity) throws Exception;
	}

	private static final class CompatMockFragmentFactory extends MockFragmentFactory {

		@Override
		Fragment createMockFragmentWithActivity(FragmentActivity activity) throws Exception {
			final Fragment mockFragment = mock(Fragment.class);
			final FragmentHostCallback mockHostCallback = mock(FragmentHostCallback.class);
			final Field activityField = FragmentHostCallback.class.getDeclaredField("mActivity");
			activityField.setAccessible(true);
			activityField.set(mockHostCallback, activity);
			final Field hostField = Fragment.class.getDeclaredField("mHost");
			hostField.setAccessible(true);
			hostField.set(mockFragment, mockHostCallback);
			return mockFragment;
		}
	}

	private static final MockFragmentFactory MOCK_FRAGMENT_FACTORY = new CompatMockFragmentFactory();

	@NonNull
	static Fragment createMockFragmentWithActivity(@NonNull FragmentActivity activity) throws Exception {
		return MOCK_FRAGMENT_FACTORY.createMockFragmentWithActivity(activity);
	}
}