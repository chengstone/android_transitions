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
import android.support.annotation.AnimRes;
import android.support.annotation.NonNull;

/**
 * Basic implementation of {@link WindowTransition} that may be used to create basic instances of window
 * transitions to animate window changes.
 *
 * @author Martin Albedinsky
 * @since 1.0
 */
public class BasicWindowTransition implements WindowTransition {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "BasicWindowTransition";

	/*
	 * Interface ===================================================================================
	 */

	/*
	 * Static members ==============================================================================
	 */

	/**
	 * Creator used to create an instance or array of instances of BasicWindowTransition from {@link Parcel}.
	 */
	public static final Creator<BasicWindowTransition> CREATOR = new Creator<BasicWindowTransition>() {

		/**
		 */
		@Override public BasicWindowTransition createFromParcel(@NonNull final Parcel source) {
			return new BasicWindowTransition(source);
		}

		/**
		 */
		@Override public BasicWindowTransition[] newArray(final int size) {
			return new BasicWindowTransition[size];
		}
	};

	/*
	 * Members =====================================================================================
	 */

	/**
	 * Animation resource for a new entering/starting window used for {@link #overrideStart(Activity)}.
	 */
	private final int startEnterAnimRes;

	/**
	 * Animation resource for the current exiting/pausing window used for {@link #overrideStart(Activity)}.
	 */
	private final int startExitAnimRes;

	/**
	 * Animation resource for an old entering/resuming window used for {@link #overrideFinish(Activity)}.
	 */
	private final int finishEnterAnimRes;

	/**
	 * Animation resource for the current exiting/finishing window used for {@link #overrideFinish(Activity)}.
	 */
	private final int finishExitAnimRes;

	/**
	 * Name of this transition.
	 */
	private final String name;

	/*
	 * Constructors ================================================================================
	 */

	/**
	 * Same as {@link #BasicWindowTransition(int, int, int, int)} where finish animations are set to
	 * {@link #NO_ANIMATION}.
	 */
	public BasicWindowTransition(@AnimRes final int enterAnim, @AnimRes final int exitAnim) {
		this(enterAnim, exitAnim, NO_ANIMATION, NO_ANIMATION);
	}

	/**
	 * Same as {@link #BasicWindowTransition(int, int, int, int, String)} with name specified
	 * as {@code "UNSPECIFIED"}.
	 */
	public BasicWindowTransition(
			@AnimRes final int startEnterAnim,
			@AnimRes final int startExitAnim,
			@AnimRes final int finishEnterAnim,
			@AnimRes final int finishExitAnim
	) {
		this(startEnterAnim, startExitAnim, finishEnterAnim, finishExitAnim, "UNSPECIFIED");
	}

	/**
	 * Creates a new instance of BasicWindowTransition with the specified animations and name.
	 *
	 * @param startEnterAnim  A resource id of the animation for an entering window.
	 * @param startExitAnim   A resource id of the animation for an exiting window to be added to the
	 *                        back stack (if).
	 * @param finishEnterAnim A resource id of the animation for an entering window to be showed from
	 *                        the back stack.
	 * @param finishExitAnim  A resource id of the animation for an exiting window to be destroyed and
	 *                        replaced by the entering one.
	 * @param name            Name for the new transition.
	 *
	 * @see #overrideStart(Activity)
	 * @see #overrideFinish(Activity)
	 */
	public BasicWindowTransition(
			@AnimRes final int startEnterAnim,
			@AnimRes final int startExitAnim,
			@AnimRes final int finishEnterAnim,
			@AnimRes final int finishExitAnim,
			@NonNull final String name
	) {
		this.startEnterAnimRes = startEnterAnim;
		this.startExitAnimRes = startExitAnim;
		this.finishEnterAnimRes = finishEnterAnim;
		this.finishExitAnimRes = finishExitAnim;
		this.name = name;
	}

	/**
	 * Called form {@link #CREATOR} to create an instance of WindowTransition form the given parcel
	 * <var>source</var>.
	 *
	 * @param source Parcel with data for the new instance.
	 */
	protected BasicWindowTransition(@NonNull final Parcel source) {
		this.startEnterAnimRes = source.readInt();
		this.startExitAnimRes = source.readInt();
		this.finishEnterAnimRes = source.readInt();
		this.finishExitAnimRes = source.readInt();
		this.name = source.readString();
	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 */
	@Override public void writeToParcel(@NonNull final Parcel dest, final int flags) {
		dest.writeInt(startEnterAnimRes);
		dest.writeInt(startExitAnimRes);
		dest.writeInt(finishEnterAnimRes);
		dest.writeInt(finishExitAnimRes);
		dest.writeString(name);
	}

	/**
	 */
	@Override public int describeContents() {
		return 0;
	}

	/**
	 */
	@Override @AnimRes public int getStartEnterAnimation() {
		return startEnterAnimRes;
	}

	/**
	 */
	@Override @AnimRes public int getStartExitAnimation() {
		return startExitAnimRes;
	}

	/**
	 */
	@Override @AnimRes public int getFinishEnterAnimation() {
		return finishEnterAnimRes;
	}

	/**
	 */
	@Override @AnimRes public int getFinishExitAnimation() {
		return finishExitAnimRes;
	}

	/**
	 */
	@Override @NonNull public String getName() {
		return name;
	}

	/**
	 */
	@Override public void overrideStart(@NonNull final Activity activity) {
		activity.overridePendingTransition(startEnterAnimRes, startExitAnimRes);
	}

	/**
	 */
	@Override public void overrideFinish(@NonNull final Activity activity) {
		activity.overridePendingTransition(finishEnterAnimRes, finishExitAnimRes);
	}

	/*
	 * Inner classes ===============================================================================
	 */
}