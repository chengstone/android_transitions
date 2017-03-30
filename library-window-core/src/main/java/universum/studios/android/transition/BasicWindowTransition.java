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
import android.support.annotation.AnimRes;
import android.support.annotation.NonNull;

/**
 * Basic implementation of {@link WindowTransition} that may be used to create basic instances of window
 * transitions to animate window changes.
 *
 * @author Martin Albedinsky
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
		@Override
		public BasicWindowTransition createFromParcel(Parcel source) {
			return new BasicWindowTransition(source);
		}

		/**
		 */
		@Override
		public BasicWindowTransition[] newArray(int size) {
			return new BasicWindowTransition[size];
		}
	};

	/*
	 * Members =====================================================================================
	 */

	/**
	 * Animation resource for a new entering/starting window used for {@link #overrideStart(Activity)}.
	 */
	private final int mStartEnterAnimRes;

	/**
	 * Animation resource for the current exiting/pausing window used for {@link #overrideStart(Activity)}.
	 */
	private final int mStartExitAnimRes;

	/**
	 * Animation resource for an old entering/resuming window used for {@link #overrideFinish(Activity)}.
	 */
	private final int mFinishEnterAnimRes;

	/**
	 * Animation resource for the current exiting/finishing window used for {@link #overrideFinish(Activity)}.
	 */
	private final int mFinishExitAnimRes;

	/**
	 * Name of this transition.
	 */
	private final String mName;

	/*
	 * Constructors ================================================================================
	 */

	/**
	 * Same as {@link #BasicWindowTransition(int, int, int, int)} finish animations set to
	 * {@link #NO_ANIMATION}.
	 */
	public BasicWindowTransition(@AnimRes int enterAnim, @AnimRes int exitAnim) {
		this(enterAnim, exitAnim, NO_ANIMATION, NO_ANIMATION);
	}

	/**
	 * Same as {@link #BasicWindowTransition(int, int, int, int, String)} with name specified
	 * as {@code "UNKNOWN"}.
	 */
	public BasicWindowTransition(@AnimRes int startEnterAnim, @AnimRes int startExitAnim, @AnimRes int finishEnterAnim, @AnimRes int finishExitAnim) {
		this(startEnterAnim, startExitAnim, finishEnterAnim, finishExitAnim, "UNKNOWN");
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
	 * @see #overrideStart(Activity)
	 * @see #overrideFinish(Activity)
	 */
	public BasicWindowTransition(@AnimRes int startEnterAnim, @AnimRes int startExitAnim, @AnimRes int finishEnterAnim, @AnimRes int finishExitAnim, @NonNull String name) {
		this.mStartEnterAnimRes = startEnterAnim;
		this.mStartExitAnimRes = startExitAnim;
		this.mFinishEnterAnimRes = finishEnterAnim;
		this.mFinishExitAnimRes = finishExitAnim;
		this.mName = name;
	}

	/**
	 * Called form {@link #CREATOR} to create an instance of WindowTransition form the given parcel
	 * <var>source</var>.
	 *
	 * @param source Parcel with data for the new instance.
	 */
	protected BasicWindowTransition(@NonNull Parcel source) {
		this.mStartEnterAnimRes = source.readInt();
		this.mStartExitAnimRes = source.readInt();
		this.mFinishEnterAnimRes = source.readInt();
		this.mFinishExitAnimRes = source.readInt();
		this.mName = source.readString();
	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mStartEnterAnimRes);
		dest.writeInt(mStartExitAnimRes);
		dest.writeInt(mFinishEnterAnimRes);
		dest.writeInt(mFinishExitAnimRes);
		dest.writeString(mName);
	}

	/**
	 */
	@Override
	public int describeContents() {
		return 0;
	}

	/**
	 */
	@AnimRes
	@Override
	public int getStartEnterAnimation() {
		return mStartEnterAnimRes;
	}

	/**
	 */
	@AnimRes
	@Override
	public int getStartExitAnimation() {
		return mStartExitAnimRes;
	}

	/**
	 */
	@AnimRes
	@Override
	public int getFinishEnterAnimation() {
		return mFinishEnterAnimRes;
	}

	/**
	 */
	@AnimRes
	@Override
	public int getFinishExitAnimation() {
		return mFinishExitAnimRes;
	}

	/**
	 */
	@NonNull
	@Override
	public String getName() {
		return mName;
	}

	/**
	 */
	@Override
	public void overrideStart(@NonNull Activity activity) {
		activity.overridePendingTransition(mStartEnterAnimRes, mStartExitAnimRes);
	}

	/**
	 */
	@Override
	public void overrideFinish(@NonNull Activity activity) {
		activity.overridePendingTransition(mFinishEnterAnimRes, mFinishExitAnimRes);
	}

	/*
	 * Inner classes ===============================================================================
	 */
}
