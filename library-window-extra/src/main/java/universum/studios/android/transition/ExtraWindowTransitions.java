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

/**
 * Factory providing <b>extra</b> {@link WindowTransition WindowTransitions}.
 * <ul>
 * <li>{@link #SLIDE_TO_RIGHT_AND_HOLD}</li>
 * <li>{@link #SLIDE_TO_LEFT_AND_HOLD}</li>
 * <li>{@link #SLIDE_TO_TOP_AND_HOLD}</li>
 * <li>{@link #SLIDE_TO_BOTTOM_AND_HOLD}</li>
 * </ul>
 *
 * @author Martin Albedinsky
 */
@SuppressWarnings("unused")
public final class ExtraWindowTransitions {

	/**
	 * Like {@link WindowTransitions#SLIDE_TO_RIGHT} but this transition will hold an outgoing window
	 * still and only a new incoming window will be animated (same when playing back animations).
	 *
	 * <h3>Powered by animations:</h3>
	 * <ul>
	 * <li><b>Enter:</b> {@link R.anim#ui_window_slide_in_right}</li>
	 * <li><b>Exit:</b> {@link R.anim#ui_window_hold}</li>
	 * <li><b>Enter (back):</b> {@link R.anim#ui_window_hold}</li>
	 * <li><b>Exit (back):</b> {@link R.anim#ui_window_slide_out_left_back}</li>
	 * </ul>
	 */
	public static final WindowTransition SLIDE_TO_RIGHT_AND_HOLD = new BasicWindowTransition(
			// Enter animation.
			R.anim.ui_window_slide_in_right,
			// Exit animation.
			R.anim.ui_window_hold,
			// Enter back animation.
			R.anim.ui_window_hold_back,
			// Exit back animation.
			R.anim.ui_window_slide_out_left_back,
			"SLIDE_TO_RIGHT_AND_HOLD"
	);

	/**
	 * Like {@link WindowTransitions#SLIDE_TO_LEFT} but this transition will hold an outgoing window
	 * still and only a new incoming window will be animated (same when playing back animations).
	 *
	 * <h3>Powered by animations:</h3>
	 * <ul>
	 * <li><b>Enter:</b> {@link R.anim#ui_window_slide_in_left}</li>
	 * <li><b>Exit:</b> {@link R.anim#ui_window_hold}</li>
	 * <li><b>Enter (back):</b> {@link R.anim#ui_window_hold}</li>
	 * <li><b>Exit (back):</b> {@link R.anim#ui_window_slide_out_right_back}</li>
	 * </ul>
	 */
	public static final WindowTransition SLIDE_TO_LEFT_AND_HOLD = new BasicWindowTransition(
			// Enter animation.
			R.anim.ui_window_slide_in_left,
			// Exit animation.
			R.anim.ui_window_hold,
			// Enter back animation.
			R.anim.ui_window_hold_back,
			// Exit back animation.
			R.anim.ui_window_slide_out_right_back,
			"SLIDE_TO_LEFT_AND_HOLD"
	);

	/**
	 * Like {@link WindowTransitions#SLIDE_TO_TOP} but this transition will hold an outgoing window
	 * still and only a new incoming window will be animated (same when playing back animations).
	 *
	 * <h3>Powered by animations:</h3>
	 * <ul>
	 * <li><b>Enter:</b> {@link R.anim#ui_window_slide_in_top}</li>
	 * <li><b>Exit:</b> {@link R.anim#ui_window_hold}</li>
	 * <li><b>Enter (back):</b> {@link R.anim#ui_window_hold}</li>
	 * <li><b>Exit (back):</b> {@link R.anim#ui_window_slide_out_bottom_back}</li>
	 * </ul>
	 */
	public static final WindowTransition SLIDE_TO_TOP_AND_HOLD = new BasicWindowTransition(
			// Enter animation.
			R.anim.ui_window_slide_in_top,
			// Exit animation.
			R.anim.ui_window_hold,
			// Enter back animation.
			R.anim.ui_window_hold_back,
			// Exit back animation.
			R.anim.ui_window_slide_out_bottom_back,
			"SLIDE_TO_TOP_AND_HOLD"
	);

	/**
	 * Like {@link WindowTransitions#SLIDE_TO_BOTTOM} but this transition will hold an outgoing window
	 * still and only a new incoming window will be animated (same when playing back animations).
	 *
	 * <h3>Powered by animations:</h3>
	 * <ul>
	 * <li><b>Enter:</b> {@link R.anim#ui_window_slide_in_bottom}</li>
	 * <li><b>Exit:</b> {@link R.anim#ui_window_hold}</li>
	 * <li><b>Enter (back):</b> {@link R.anim#ui_window_hold}</li>
	 * <li><b>Exit (back):</b> {@link R.anim#ui_window_slide_out_top_back}</li>
	 * </ul>
	 */
	public static final WindowTransition SLIDE_TO_BOTTOM_AND_HOLD = new BasicWindowTransition(
			// Enter animation.
			R.anim.ui_window_slide_in_bottom,
			// Exit animation.
			R.anim.ui_window_hold,
			// Enter back animation.
			R.anim.ui_window_hold_back,
			// Exit back animation.
			R.anim.ui_window_slide_out_top_back,
			"SLIDE_TO_BOTTOM_AND_HOLD"
	);

	/**
	 */
	private ExtraWindowTransitions() {
		// Not allowed to be instantiated publicly.
		throw new UnsupportedOperationException();
	}
}
