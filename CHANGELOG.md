Change-Log
===============

### Release 1.0.2 ###
> --.--.2017

- Implementation of `BaseNavigationalTransition.configureTransitions(Activity)` has been split into
  `configureIncomingTransitions(Activity)` and `configureOutgoingTransitions(Activity)`. The **outgoing**
  configuration method is by default called whenever `BaseNavigationalTransition.start(Activity)` is 
  invoked and the **incoming** configuration method should be called by the activity to which is the
  calling activity transitioning from its `onCreate(Bundle)` method.
- `BaseNavigationalTransition.configureTransitionsOverlapping(Activity)` has been deprecated as its
  implementation has been moved into `configureIncomingTransitions(Activity)`.
- `BaseNavigationalTransition` now also supports setting whether a transitioning shared elements should
  use **overlay** or not via `sharedElementsUseOverlay(boolean)`.

### Release 1.0.1 ###
> 16.02.2017

- `BaseNavigationalTransition` now allows to specify `Bundle` with extras for the transition activity
  via `BaseNavigationalTransition.intentExtras(Bundle)` method.
- `NavigationalTransition` and `NavigationalTransitionCompat` now implement `onStart(Fragment)` method
  the same way as `BaseNavigationalTransitions.onStart(Activity)` is implemented to allow starting
  of transition with scene transition animations.

### Release 1.0.0 ###
> 19.01.2017