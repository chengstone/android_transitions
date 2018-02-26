/*
 * =================================================================================================
 *                             Copyright (C) 2018 Universum Studios
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
package universum.studios.android.transition.util;

import android.os.IBinder;
import android.view.View;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import universum.studios.android.test.local.LocalTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Martin Albedinsky
 */
public final class TransitionUtilsTest extends LocalTestCase {

    @Test(expected = IllegalAccessException.class)
    public void testInstantiation() throws Exception {
        TransitionUtils.class.newInstance();
    }

    @Test(expected = InvocationTargetException.class)
    public void testInstantiationWithAccessibleConstructor() throws Exception {
        final Constructor<TransitionUtils> constructor = TransitionUtils.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    @Test
	public void testIsViewAttachedToWindow() {
    	assertThat(TransitionUtils.isViewAttachedToWindow(createMockViewAttachedToWindow(true)), is(true));
    	assertThat(TransitionUtils.isViewAttachedToWindow(createMockViewAttachedToWindow(false)), is(false));
    }

    private static View createMockViewAttachedToWindow(boolean attachedToWindow) {
    	final View mockView = mock(View.class);
    	when(mockView.getWindowToken()).thenReturn(attachedToWindow ? mock(IBinder.class) : null);
    	return mockView;
    }
}
