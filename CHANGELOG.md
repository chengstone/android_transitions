Change-Log
===============

### Release 1.0.1 ###
> 16.02.2017

- `BaseNavigationalTransition` now allows to specify `Bundle` with extras for the transition activity
  via `BaseNavigationalTransition.intentExtras(Bundle)` method.
- `NavigationalTransition` and `NavigationalTransitionCompat` now implement `onStart(Fragment)` method
  the same way as `BaseNavigationalTransitions.onStart(Activity)` is implemented to allow starting
  of transition with scene transition animations.

### Release 1.0.0 ###
> 19.01.2017