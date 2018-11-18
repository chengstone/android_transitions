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

/**
 * <b>This class has been deprecated and will be removed in version 1.4.0.</b>
 * <p>
 * Factory providing <b>extra</b> {@link WindowTransition WindowTransitions}.
 * <ul>
 * <li>{@link #SLIDE_TO_RIGHT_AND_HOLD}</li>
 * <li>{@link #SLIDE_TO_LEFT_AND_HOLD}</li>
 * <li>{@link #SLIDE_TO_TOP_AND_HOLD}</li>
 * <li>{@link #SLIDE_TO_BOTTOM_AND_HOLD}</li>
 * </ul>
 *
 * @author Martin Albedinsky
 * @since 1.0
 *
 * @deprecated Use {@link WindowExtraTransitions} instead.
 */
@SuppressWarnings("unused")
public final class ExtraWindowTransitions {

	/**
	 * See {@link WindowExtraTransitions#SLIDE_TO_RIGHT_AND_HOLD}.
	 */
	public static final WindowTransition SLIDE_TO_RIGHT_AND_HOLD = WindowExtraTransitions.SLIDE_TO_RIGHT_AND_HOLD;

	/**
	 * See {@link WindowExtraTransitions#SLIDE_TO_LEFT_AND_HOLD}.
	 */
	public static final WindowTransition SLIDE_TO_LEFT_AND_HOLD = WindowExtraTransitions.SLIDE_TO_LEFT_AND_HOLD;

	/**
	 * See {@link WindowExtraTransitions#SLIDE_TO_TOP_AND_HOLD}.
	 */
	public static final WindowTransition SLIDE_TO_TOP_AND_HOLD = WindowExtraTransitions.SLIDE_TO_TOP_AND_HOLD;

	/**
	 * See {@link WindowExtraTransitions#SLIDE_TO_BOTTOM_AND_HOLD}.
	 */
	public static final WindowTransition SLIDE_TO_BOTTOM_AND_HOLD = WindowExtraTransitions.SLIDE_TO_BOTTOM_AND_HOLD;

	/**
	 */
	private ExtraWindowTransitions() {
		// Not allowed to be instantiated publicly.
		throw new UnsupportedOperationException();
	}
}