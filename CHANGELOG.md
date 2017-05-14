Change-Log
===============

### [Release 1.1.0](https://github.com/universum-studios/android_transitions/releases/tag/1.1.0) ###
> 14.05.2017

- Add implementation of `Translate` transition which may be used to move views in the scene by
  a desired delta along both, X and Y, axes.
- Updated implementation of `Scale` transition which now also takes into account **current scale**
  value of the target view captured before transition starts. Scale transition also uses
  `FastOutSlowInInterpolator` as default interpolator for its corresponding animator.
- Updated implementation of `Reveal` transition which creates its corresponding animator via utility
  method `createAnimator(...)` already wrapped into `AnimatorWrapper`. Reveal transition also uses
  `FastOutSlowInInterpolator` as default interpolator for its corresponding animator.
- Small patches to the navigational transitions.

### [Release 1.0.2](https://github.com/universum-studios/android_transitions/releases/tag/1.0.2) ###
> 02.04.2017

- Implementation of `BaseNavigationalTransition.configureTransitions(Activity)` has been split into
  `configureIncomingTransitions(Activity)` and `configureOutgoingTransitions(Activity)`. The **outgoing**
  configuration method is by default called whenever `BaseNavigationalTransition.start(Activity)` is 
  invoked and the **incoming** configuration method should be called by the activity to which is the
  calling activity transitioning from its `onCreate(Bundle)` method.
- `BaseNavigationalTransition.configureTransitionsOverlapping(Activity)` has been deprecated as its
  implementation has been moved into `configureIncomingTransitions(Activity)`.
- `BaseNavigationalTransition` now also supports setting whether a transitioning shared elements should
  use **overlay** or not via `sharedElementsUseOverlay(boolean)`.
- Fixed **animation resource** for **enter back** animation of window transitions declared in 
  `ExtranWindowTransitions` class

### [Release 1.0.1](https://github.com/universum-studios/android_transitions/releases/tag/1.0.1) ###
> 16.02.2017

- `BaseNavigationalTransition` now allows to specify `Bundle` with extras for the transition activity
  via `BaseNavigationalTransition.intentExtras(Bundle)` method.
- `NavigationalTransition` and `NavigationalTransitionCompat` now implement `onStart(Fragment)` method
  the same way as `BaseNavigationalTransitions.onStart(Activity)` is implemented to allow starting
  of transition with scene transition animations.

### [Release 1.0.0](https://github.com/universum-studios/android_transitions/releases/tag/1.0.0) ###
> 19.01.2017

- First production release.