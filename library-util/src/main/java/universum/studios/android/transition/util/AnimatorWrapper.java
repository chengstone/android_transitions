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
package universum.studios.android.transition.util;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.annotation.VisibleForTesting;
import android.support.v4.util.ArrayMap;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

/**
 * A {@link Animator} implementation that may be used to wrap an instance of animator in order to
 * 'suppress' some of its features like pausing and resuming for instance.
 *
 * @author Martin Albedinsky
 */
@SuppressWarnings("deprecation")
@RequiresApi(Build.VERSION_CODES.HONEYCOMB)
public class AnimatorWrapper extends Animator {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "AnimatorWrapper";

	/**
	 * Flag for indicating to the animator wrapper that it should support <b>START</b> feature for
	 * its wrapped animator.
	 *
	 * @see #start()
	 */
	public static final int START = 0x00000001;

	/**
	 * Flag for indicating to the animator wrapper that it should support <b>PAUSE</b> feature for
	 * its wrapped animator.
	 *
	 * @see #RESUME
	 * @see #pause()
	 */
	public static final int PAUSE = 0x00000001 << 1;

	/**
	 * Flag for indicating to the animator wrapper that it should support <b>RESUME</b> feature for
	 * its wrapped animator.
	 * <p>
	 * <b>Note, that if there is no {@link #PAUSE} feature requested, this feature flag is ignored.</b>
	 *
	 * @see #resume()
	 */
	public static final int RESUME = 0x00000001 << 2;

	/**
	 * Flag for indicating to the animator wrapper that it should support <b>END</b> feature for
	 * its wrapped animator.
	 * <p>
	 * <b>Note, that if there is no {@link #START} feature requested, this feature flag is ignored.</b>
	 *
	 * @see #end()
	 */
	public static final int END = 0x00000001 << 3;

	/**
	 * Flag for indicating to the animator wrapper that it should support <b>CANCEL</b> feature for
	 * its wrapped animator.
	 * <p>
	 * <b>Note, that if there is no {@link #START} feature requested, this feature flag is ignored.</b>
	 *
	 * @see #cancel()
	 */
	public static final int CANCEL = 0x00000001 << 4;

	/**
	 * Defines an annotation for determining set of allowed features for AnimatorWrapper.
	 *
	 * @see #requestFeatures(int)
	 * @see #requestFeature(int)
	 */
	@Retention(RetentionPolicy.SOURCE)
	@IntDef(flag = true, value = {
			START,
			PAUSE,
			RESUME,
			END,
			CANCEL
	})
	public @interface WrapperFeatures {
	}

	/**
	 * Flag grouping all wrapper features in one.
	 */
	public static final int ALL = START | PAUSE | RESUME | END | CANCEL;

	/*
	 * Interface ===================================================================================
	 */

	/*
	 * Static members ==============================================================================
	 */

	/*
	 * Members =====================================================================================
	 */

	/**
	 * Wrapped animator instance.
	 */
	private final Animator mAnimator;

	/**
	 * Set of feature flags specified for this wrapper.
	 */
	private int mFeatures = ALL;

	/**
	 * Map containing animator listener wrappers mapped to theirs wrapped listeners used for purpose
	 * of adding/removing of listeners via {@link #addListener(AnimatorListener)} and
	 * {@link #removeListener(AnimatorListener)}.
	 */
	private ArrayMap<AnimatorListener, AnimatorListenerWrapper> mListenerWrappers;

	/**
	 * Map containing animator pause listener wrappers mapped to theirs wrapped listeners used for
	 * purpose of adding/removing of pause listeners via {@link #addPauseListener(AnimatorPauseListener)}
	 * and {@link #removePauseListener(AnimatorPauseListener)}.
	 */
	private ArrayMap<AnimatorPauseListener, AnimatorPauseListenerWrapper> mPauseListenerWrappers;

	/*
	 * Constructors ================================================================================
	 */

	/**
	 * Creates a new instance of AnimatorWrapper to wrap the given instance of <var>animator</var>.
	 *
	 * @param animator The animator to be wrapped.
	 */
	public AnimatorWrapper(@NonNull Animator animator) {
		super();
		this.mAnimator = animator;
	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Returns the animator wrapped by this wrapper.
	 *
	 * @return The wrapped animator.
	 * @see #AnimatorWrapper(Animator)
	 */
	@NonNull
	public final Animator getWrappedAnimator() {
		return mAnimator;
	}

	/**
	 * Specifies set of features for this wrapper.
	 * <p>
	 * <b>Note</b>, that this will override all current features to the specified ones.
	 *
	 * @param features The desired features to be set for this wrapper.
	 * @see #requestFeature(int)
	 * @see #hasFeature(int)
	 * @see #removeFeature(int)
	 */
	public void requestFeatures(@WrapperFeatures int features) {
		this.mFeatures = features;
	}

	/**
	 * Adds the specified <var>feature</var> to the registered ones.
	 *
	 * @param feature The desired feature to add.
	 * @see #requestFeatures(int)
	 * @see #hasFeature(int)
	 * @see #removeFeature(int)
	 */
	public void requestFeature(@WrapperFeatures int feature) {
		this.mFeatures |= feature;
	}

	/**
	 * Removes the specified <var>feature</var> from the registered ones.
	 *
	 * @param feature The desired feature to remove.
	 * @see #requestFeature(int)
	 * @see #hasFeature(int)
	 */
	public void removeFeature(@WrapperFeatures int feature) {
		this.mFeatures &= ~feature;
	}

	/**
	 * Checks whether the specified <var>feature</var> has been requested for this wrapper or not.
	 *
	 * @param feature The desired feature to check for.
	 * @return {@code True} if the feature has been requested via {@link #requestFeature(int)} or
	 * specified by default, {@code false} otherwise.
	 * @see #requestFeature(int)
	 */
	public boolean hasFeature(@WrapperFeatures int feature) {
		return (mFeatures & feature) != 0;
	}

	/**
	 */
	@Override
	public void addListener(AnimatorListener listener) {
		this.ensureListenerWrappers();
		if (!mListenerWrappers.containsKey(listener)) {
			final AnimatorListenerWrapper wrapper = new AnimatorListenerWrapper(listener, this);
			mListenerWrappers.put(listener, wrapper);
			mAnimator.addListener(wrapper);
		}
	}

	/**
	 */
	@Override
	public void removeListener(AnimatorListener listener) {
		this.ensureListenerWrappers();
		final AnimatorListenerWrapper wrapper = mListenerWrappers.get(listener);
		if (wrapper != null) {
			mListenerWrappers.remove(listener);
			mAnimator.removeListener(wrapper);
		}
	}

	/**
	 * Ensures that the map with listener wrappers is initialized.
	 */
	private void ensureListenerWrappers() {
		if (mListenerWrappers == null) this.mListenerWrappers = new ArrayMap<>(1);
	}

	/**
	 * Ignored if there are no {@link #PAUSE} and {@link #RESUME} features requested.
	 *
	 * @see #requestFeature(int)
	 * @see #removeFeature(int)
	 */
	@Override
	public void addPauseListener(AnimatorPauseListener listener) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
			return;
		}
		if (hasFeature(PAUSE) || hasFeature(RESUME)) {
			if (mPauseListenerWrappers == null) {
				this.mPauseListenerWrappers = new ArrayMap<>(1);
			}
			if (!mPauseListenerWrappers.containsKey(listener)) {
				final AnimatorPauseListenerWrapper wrapper = new AnimatorPauseListenerWrapper(listener, this);
				mPauseListenerWrappers.put(listener, wrapper);
				mAnimator.addPauseListener(wrapper);
			}
		}
	}

	/**
	 */
	@Override
	public void removePauseListener(AnimatorPauseListener listener) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
			return;
		}
		if (mPauseListenerWrappers != null) {
			final AnimatorPauseListenerWrapper wrapper = mPauseListenerWrappers.get(listener);
			if (wrapper != null) {
				mPauseListenerWrappers.remove(listener);
				mAnimator.removePauseListener(wrapper);
			}
		}
	}

	/**
	 */
	@NonNull
	@Override
	public ArrayList<AnimatorListener> getListeners() {
		return mListenerWrappers == null ?
				new ArrayList<AnimatorListener>(0) :
				new ArrayList<>(mListenerWrappers.keySet());
	}

	/**
	 */
	@Override
	public void removeAllListeners() {
		if (mListenerWrappers != null) {
			this.mListenerWrappers.clear();
			this.mListenerWrappers = null;
		}
		if (mPauseListenerWrappers != null) {
			this.mPauseListenerWrappers.clear();
			this.mPauseListenerWrappers = null;
		}
		mAnimator.removeAllListeners();
	}

	/**
	 */
	@Override
	public void setStartDelay(long startDelay) {
		mAnimator.setStartDelay(startDelay);
	}

	/**
	 */
	@Override
	public long getStartDelay() {
		return mAnimator.getStartDelay();
	}

	/**
	 */
	@Override
	public Animator setDuration(long duration) {
		mAnimator.setDuration(duration);
		return this;
	}

	/**
	 */
	@Override
	public long getDuration() {
		return mAnimator.getDuration();
	}

	/**
	 */
	@Override
	public void setInterpolator(TimeInterpolator interpolator) {
		mAnimator.setInterpolator(interpolator);
	}

	/**
	 */
	@Override
	@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	public TimeInterpolator getInterpolator() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2 ? mAnimator.getInterpolator() : null;
	}

	/**
	 */
	@Override
	public void setTarget(Object target) {
		mAnimator.setTarget(target);
	}

	/**
	 * Ignored if there is no {@link #START} feature requested.
	 *
	 * @see #requestFeature(int)
	 * @see #removeFeature(int)
	 */
	@Override
	public void start() {
		if (hasFeature(START)) mAnimator.start();
	}

	/**
	 */
	@Override
	public boolean isStarted() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH && mAnimator.isStarted();
	}

	/**
	 * Ignored if there is no {@link #PAUSE} feature requested.
	 *
	 * @see #requestFeature(int)
	 * @see #removeFeature(int)
	 */
	@Override
	public void pause() {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
			return;
		}
		if (hasFeature(PAUSE)) {
			mAnimator.pause();
		}
	}

	/**
	 */
	@Override
	public boolean isPaused() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && mAnimator.isPaused();
	}


	/**
	 * Ignored if there are no {@link #PAUSE} and {@link #RESUME} features requested.
	 *
	 * @see #requestFeature(int)
	 * @see #removeFeature(int)
	 */
	@Override
	public void resume() {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
			return;
		}
		if (hasFeature(PAUSE) || hasFeature(RESUME)) {
			mAnimator.resume();
		}
	}

	/**
	 * Ignored if there are no {@link #START} and {@link #END} features requested.
	 *
	 * @see #requestFeature(int)
	 * @see #removeFeature(int)
	 */
	@Override
	public void end() {
		if (hasFeature(START) || hasFeature(END)) mAnimator.end();
	}

	/**
	 * Ignored if there are no {@link #START} and {@link #CANCEL} features requested.
	 *
	 * @see #requestFeature(int)
	 * @see #removeFeature(int)
	 */
	@Override
	public void cancel() {
		if (hasFeature(START) || hasFeature(CANCEL)) mAnimator.cancel();
	}

	/**
	 */
	@Override
	public boolean isRunning() {
		return mAnimator.isRunning();
	}

	/*
	 * Inner classes ===============================================================================
	 */

	/**
	 * Base class for animator listener wrappers used by {@link AnimatorWrapper} to support proper
	 * dispatching of animation callbacks to the wrapped listener.
	 *
	 * @param <L> Type of the listener to wrap.
	 */
	private static class BaseAnimatorListenerWrapper<L> {

		/**
		 * Wrapped instance of animator listener.
		 */
		final L listener;

		/**
		 * Animator wrapper to be dispatched with occurred animation callbacks.
		 */
		final Animator animatorWrapper;

		/**
		 * Creates a new instance of BaseAnimatorListenerWrapper to wrap the given instance of animator
		 * <var>listener</var>.
		 *
		 * @param listener        The listener to be wrapped.
		 * @param animatorWrapper The animator wrapper to be dispatched with occurred animation callbacks
		 *                        to ensure that the original listener always communicates with the
		 *                        wrapped animator through its wrapper.
		 */
		BaseAnimatorListenerWrapper(L listener, Animator animatorWrapper) {
			this.listener = listener;
			this.animatorWrapper = animatorWrapper;
		}
	}

	/**
	 * A {@link BaseAnimatorListenerWrapper} implementation to wrap {@link AnimatorListener}.
	 */
	@VisibleForTesting static final class AnimatorListenerWrapper
			extends BaseAnimatorListenerWrapper<AnimatorListener>
			implements AnimatorListener {

		/**
		 * Creates a new instance of AnimatorListenerWrapper to wrap the given instance of animator
		 * <var>listener</var>.
		 *
		 * @see BaseAnimatorListenerWrapper#BaseAnimatorListenerWrapper(Object, Animator)
		 */
		AnimatorListenerWrapper(AnimatorListener listener, Animator animatorWrapper) {
			super(listener, animatorWrapper);
		}

		/**
		 */
		@Override
		public void onAnimationStart(Animator animation) {
			listener.onAnimationStart(animatorWrapper);
		}

		/**
		 */
		@Override
		public void onAnimationEnd(Animator animation) {
			listener.onAnimationEnd(animatorWrapper);
		}

		/**
		 */
		@Override
		public void onAnimationCancel(Animator animation) {
			listener.onAnimationCancel(animatorWrapper);
		}

		/**
		 */
		@Override
		public void onAnimationRepeat(Animator animation) {
			listener.onAnimationRepeat(animatorWrapper);
		}
	}

	/**
	 * A {@link BaseAnimatorListenerWrapper} implementation to wrap {@link AnimatorPauseListener}.
	 */
	@RequiresApi(Build.VERSION_CODES.KITKAT)
	@VisibleForTesting static final class AnimatorPauseListenerWrapper
			extends BaseAnimatorListenerWrapper<AnimatorPauseListener>
			implements AnimatorPauseListener {

		/**
		 * Creates a new instance of AnimatorPauseListenerWrapper to wrap the given instance of animator
		 * <var>listener</var>.
		 *
		 * @see BaseAnimatorListenerWrapper#BaseAnimatorListenerWrapper(Object, Animator)
		 */
		AnimatorPauseListenerWrapper(AnimatorPauseListener listener, Animator animatorWrapper) {
			super(listener, animatorWrapper);
		}

		/**
		 */
		@Override
		public void onAnimationPause(Animator animation) {
			listener.onAnimationPause(animatorWrapper);
		}

		/**
		 */
		@Override
		public void onAnimationResume(Animator animation) {
			listener.onAnimationResume(animatorWrapper);
		}
	}
}
