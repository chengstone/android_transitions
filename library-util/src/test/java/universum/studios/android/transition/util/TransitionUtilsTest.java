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
package universum.studios.android.transition.util;

import android.os.IBinder;
import android.view.View;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import universum.studios.android.test.local.LocalTestCase;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Martin Albedinsky
 */
public final class TransitionUtilsTest extends LocalTestCase {

    @Test(expected = IllegalAccessException.class)
    public void testInstantiation() throws Exception {
	    // Act:
        TransitionUtils.class.newInstance();
    }

    @Test(expected = InvocationTargetException.class)
    public void testInstantiationWithAccessibleConstructor() throws Exception {
	    // Arrange:
    	final Constructor<TransitionUtils> constructor = TransitionUtils.class.getDeclaredConstructor();
	    constructor.setAccessible(true);
	    // Act:
	    constructor.newInstance();
    }

    @Test public void testIsViewAttachedToWindow() {
	    // Act + Assert:
    	assertThat(TransitionUtils.isViewAttachedToWindow(createMockViewAttachedToWindow(true)), is(true));
    	assertThat(TransitionUtils.isViewAttachedToWindow(createMockViewAttachedToWindow(false)), is(false));
    }

    private static View createMockViewAttachedToWindow(final boolean attachedToWindow) {
    	final View mockView = mock(View.class);
    	when(mockView.getWindowToken()).thenReturn(attachedToWindow ? mock(IBinder.class) : null);
    	return mockView;
    }
}