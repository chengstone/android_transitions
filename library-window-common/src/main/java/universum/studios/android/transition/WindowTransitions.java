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
 * Factory providing <b>common</b> {@link WindowTransition WindowTransitions}.
 * <ul>
 * <li>{@link #NONE}</li>
 * <li>{@link #CROSS_FADE}</li>
 * <li>{@link #SLIDE_TO_RIGHT}</li>
 * <li>{@link #SLIDE_TO_LEFT}</li>
 * <li>{@link #SLIDE_TO_BOTTOM}</li>
 * <li>{@link #SLIDE_TO_TOP}</li>
 * <li>{@link #SLIDE_TO_RIGHT_AND_SCALE_OUT}</li>
 * <li>{@link #SLIDE_TO_LEFT_AND_SCALE_OUT}</li>
 * <li>{@link #SLIDE_TO_TOP_AND_SCALE_OUT}</li>
 * <li>{@link #SLIDE_TO_BOTTOM_AND_SCALE_OUT}</li>
 * </ul>
 *
 * @author Martin Albedinsky
 */
@SuppressWarnings("unused")
public final class WindowTransitions {

	/**
	 * Transition that may be used to change two windows without any animation.
	 */
	public static final WindowTransition NONE = new BasicWindowTransition(
			WindowTransition.NO_ANIMATION,
			WindowTransition.NO_ANIMATION,
			WindowTransition.NO_ANIMATION,
			WindowTransition.NO_ANIMATION,
			"NONE"
	);

	/**
	 * Transition that may be used to fade a new incoming window into the screen and an outgoing
	 * (the current one) will be faded out of the screen.
	 *
	 * <h3>Powered by animations:</h3>
	 * <ul>
	 * <li><b>Enter:</b> {@link R.anim#ui_window_fade_in}</li>
	 * <li><b>Exit:</b> {@link R.anim#ui_window_fade_out}</li>
	 * <li><b>Enter (back):</b> {@link R.anim#ui_window_fade_in_back}</li>
	 * <li><b>Exit (back):</b> {@link R.anim#ui_window_fade_out_back}</li>
	 * </ul>
	 */
	public static final WindowTransition CROSS_FADE = new BasicWindowTransition(
			// Enter animation.
			R.anim.ui_window_fade_in,
			// Exit animation.
			R.anim.ui_window_fade_out,
			// Enter back animation.
			R.anim.ui_window_fade_in_back,
			// Exit back animation.
			R.anim.ui_window_fade_out_back,
			"CROSS_FADE"
	);

	/**
	 * Like {@link #CROSS_FADE} but this will hold an outgoing window still and only a new incoming
	 * window will be animated (same when playing back animations).
	 *
	 * <h3>Powered by animations:</h3>
	 * <ul>
	 * <li><b>Enter:</b> {@link R.anim#ui_window_fade_in}</li>
	 * <li><b>Exit:</b> {@link R.anim#ui_window_hold}</li>
	 * <li><b>Enter (back):</b> {@link R.anim#ui_window_hold}</li>
	 * <li><b>Exit (back):</b> {@link R.anim#ui_window_fade_out_back}</li>
	 * </ul>
	 */
	public static final WindowTransition CROSS_FADE_AND_HOLD = new BasicWindowTransition(
			// Enter animation.
			R.anim.ui_window_fade_in,
			// Exit animation.
			R.anim.ui_window_hold,
			// Enter back animation.
			R.anim.ui_window_hold_back,
			// Exit back animation.
			R.anim.ui_window_fade_out_back,
			"CROSS_FADE_AND_HOLD"
	);

	/**
	 * Transition that may be used to slide a new incoming window into the screen from the left and
	 * an outgoing (the current one) will be slided out of the screen to the right.
	 *
	 * <h3>Powered by animations:</h3>
	 * <ul>
	 * <li><b>Enter:</b> {@link R.anim#ui_window_slide_in_right}</li>
	 * <li><b>Exit:</b> {@link R.anim#ui_window_slide_out_right}</li>
	 * <li><b>Enter (back):</b> {@link R.anim#ui_window_slide_in_left_back}</li>
	 * <li><b>Exit (back):</b> {@link R.anim#ui_window_slide_out_left_back}</li>
	 * </ul>
	 */
	public static final WindowTransition SLIDE_TO_RIGHT = new BasicWindowTransition(
			// Enter animation.
			R.anim.ui_window_slide_in_right,
			// Exit animation.
			R.anim.ui_window_slide_out_right,
			// Enter back animation.
			R.anim.ui_window_slide_in_left_back,
			// Exit back animation.
			R.anim.ui_window_slide_out_left_back,
			"SLIDE_TO_RIGHT"
	);

	/**
	 * Transition that may be used to slide a new incoming window into the screen from the right and
	 * an outgoing (the current one) will be slided out of the screen to the left.
	 *
	 * <h3>Powered by animations:</h3>
	 * <ul>
	 * <li><b>Enter:</b> {@link R.anim#ui_window_slide_in_left}</li>
	 * <li><b>Exit:</b> {@link R.anim#ui_window_slide_out_left}</li>
	 * <li><b>Enter (back):</b> {@link R.anim#ui_window_slide_in_right_back}</li>
	 * <li><b>Exit (back):</b> {@link R.anim#ui_window_slide_out_right_back}</li>
	 * </ul>
	 */
	public static final WindowTransition SLIDE_TO_LEFT = new BasicWindowTransition(
			// Enter animation.
			R.anim.ui_window_slide_in_left,
			// Exit animation.
			R.anim.ui_window_slide_out_left,
			// Enter back animation.
			R.anim.ui_window_slide_in_right_back,
			// Exit back animation.
			R.anim.ui_window_slide_out_right_back,
			"SLIDE_TO_LEFT"
	);

	/**
	 * Transition that may be used to slide a new incoming window into the screen from the bottom and
	 * an outgoing (the current one) will be slided out of the screen to the top.
	 *
	 * <h3>Powered by animations:</h3>
	 * <ul>
	 * <li><b>Enter:</b> {@link R.anim#ui_window_slide_in_top}</li>
	 * <li><b>Exit:</b> {@link R.anim#ui_window_slide_out_top}</li>
	 * <li><b>Enter (back):</b> {@link R.anim#ui_window_slide_in_bottom_back}</li>
	 * <li><b>Exit (back):</b> {@link R.anim#ui_window_slide_out_bottom_back}</li>
	 * </ul>
	 */
	public static final WindowTransition SLIDE_TO_TOP = new BasicWindowTransition(
			// Enter animation.
			R.anim.ui_window_slide_in_top,
			// Exit animation.
			R.anim.ui_window_slide_out_top,
			// Enter back animation.
			R.anim.ui_window_slide_in_bottom_back,
			// Exit back animation.
			R.anim.ui_window_slide_out_bottom_back,
			"SLIDE_TO_TOP"
	);

	/**
	 * Transition that may be used to slide a new incoming window into the screen from the top and
	 * an outgoing (the current one) will be slided out of the screen to the bottom.
	 *
	 * <h3>Powered by animations:</h3>
	 * <ul>
	 * <li><b>Enter:</b> {@link R.anim#ui_window_slide_in_bottom}</li>
	 * <li><b>Exit:</b> {@link R.anim#ui_window_slide_out_bottom}</li>
	 * <li><b>Enter (back):</b> {@link R.anim#ui_window_slide_in_top_back}</li>
	 * <li><b>Exit (back):</b> {@link R.anim#ui_window_slide_out_top_back}</li>
	 * </ul>
	 */
	public static final WindowTransition SLIDE_TO_BOTTOM = new BasicWindowTransition(
			// Enter animation.
			R.anim.ui_window_slide_in_bottom,
			// Exit animation.
			R.anim.ui_window_slide_out_bottom,
			// Enter back animation.
			R.anim.ui_window_slide_in_top_back,
			// Exit back animation.
			R.anim.ui_window_slide_out_top_back,
			"SLIDE_TO_BOTTOM"
	);

	/**
	 * Transition that may be used to slide a new incoming window into the screen from the right and
	 * an outgoing (the current one) will be scaled out (with fade) out of the screen to its background.
	 *
	 * <h3>Powered by animations:</h3>
	 * <ul>
	 * <li><b>Enter:</b> {@link R.anim#ui_window_slide_in_left}</li>
	 * <li><b>Exit:</b> {@link R.anim#ui_window_scale_out}</li>
	 * <li><b>Enter (back):</b> {@link R.anim#ui_window_scale_in_back}</li>
	 * <li><b>Exit (back):</b> {@link R.anim#ui_window_slide_out_right_back}</li>
	 * </ul>
	 */
	public static final WindowTransition SLIDE_TO_LEFT_AND_SCALE_OUT = new BasicWindowTransition(
			// Enter animation.
			R.anim.ui_window_slide_in_left,
			// Exit animation.
			R.anim.ui_window_scale_out,
			// Enter back animation.
			R.anim.ui_window_scale_in_back,
			// Exit back animation.
			R.anim.ui_window_slide_out_right_back,
			"SLIDE_TO_LEFT_AND_SCALE_OUT"
	);

	/**
	 * Transition that may be used to slide a new incoming window into the screen from the left and
	 * an outgoing (the current one) will be scaled out (with fade) out of the screen to its background.
	 *
	 * <h3>Powered by animations:</h3>
	 * <ul>
	 * <li><b>Enter:</b> {@link R.anim#ui_window_slide_in_right}</li>
	 * <li><b>Exit:</b> {@link R.anim#ui_window_scale_out}</li>
	 * <li><b>Enter (back):</b> {@link R.anim#ui_window_scale_in_back}</li>
	 * <li><b>Exit (back):</b> {@link R.anim#ui_window_slide_out_left_back}</li>
	 * </ul>
	 */
	public static final WindowTransition SLIDE_TO_RIGHT_AND_SCALE_OUT = new BasicWindowTransition(
			// Enter animation.
			R.anim.ui_window_slide_in_right,
			// Exit animation.
			R.anim.ui_window_scale_out,
			// Enter back animation.
			R.anim.ui_window_scale_in_back,
			// Exit back animation.
			R.anim.ui_window_slide_out_left_back,
			"SLIDE_TO_RIGHT_AND_SCALE_OUT"
	);

	/**
	 * Transition that may be used to slide a new incoming window into the screen from the bottom and
	 * an outgoing (the current one) will be scaled out (with fade) out of the screen to its background.
	 *
	 * <h3>Powered by animations:</h3>
	 * <ul>
	 * <li><b>Enter:</b> {@link R.anim#ui_window_slide_in_top}</li>
	 * <li><b>Exit:</b> {@link R.anim#ui_window_scale_out}</li>
	 * <li><b>Enter (back):</b> {@link R.anim#ui_window_scale_in_back}</li>
	 * <li><b>Exit (back):</b> {@link R.anim#ui_window_slide_out_bottom_back}</li>
	 * </ul>
	 */
	public static final WindowTransition SLIDE_TO_TOP_AND_SCALE_OUT = new BasicWindowTransition(
			// Enter animation.
			R.anim.ui_window_slide_in_top,
			// Exit animation.
			R.anim.ui_window_scale_out,
			// Enter back animation.
			R.anim.ui_window_scale_in_back,
			// Exit back animation.
			R.anim.ui_window_slide_out_bottom_back,
			"SLIDE_TO_TOP_AND_SCALE_OUT"
	);

	/**
	 * Transition that may be used to slide a new incoming window into the screen from the top and
	 * an outgoing (the current one) will be scaled out (with fade) out of the screen to its background.
	 *
	 * <h3>Powered by animations:</h3>
	 * <ul>
	 * <li><b>Enter:</b> {@link R.anim#ui_window_slide_in_bottom}</li>
	 * <li><b>Exit:</b> {@link R.anim#ui_window_scale_out}</li>
	 * <li><b>Enter (back):</b> {@link R.anim#ui_window_scale_in_back}</li>
	 * <li><b>Exit (back):</b> {@link R.anim#ui_window_slide_out_top_back}</li>
	 * </ul>
	 */
	public static final WindowTransition SLIDE_TO_BOTTOM_AND_SCALE_OUT = new BasicWindowTransition(
			// Enter animation.
			R.anim.ui_window_slide_in_bottom,
			// Exit animation.
			R.anim.ui_window_scale_out,
			// Enter back animation.
			R.anim.ui_window_scale_in_back,
			// Exit back animation.
			R.anim.ui_window_slide_out_top_back,
			"SLIDE_TO_BOTTOM_AND_SCALE_OUT"
	);

	/**
	 */
	private WindowTransitions() {
		// Not allowed to be instantiated publicly.
		throw new UnsupportedOperationException();
	}
}
