Change-Log
===============
> Regular configuration update: _01.11.2018_

More **detailed changelog** for each respective version may be viewed by pressing on a desired _version's name_.

## Version 1.x ##

### 1.3.0 ###
> upcoming

- Regular **dependencies update** (mainly to use new artifacts from **Android Jetpack**).

### [1.2.0](https://github.com/universum-studios/android_transitions/releases/tag/v1.2.0) ###
> 26.06.2018

- Renamed `ui_` prefix to `transition_`.
- Minor updates.
- **Note that some of changes (renamed prefixes) of this release are not backward compatible with previous releases.**

### [1.1.4](https://github.com/universum-studios/android_transitions/releases/tag/v1.1.4) ###
> 27.02.2018

- `Reveal`, `Scale`, `Translate` transitions now return **null** `Animator` from `createAnimator(...)`
  factory methods and also from `onAppear(...)` and `onDisappear(...)` methods when a **target view**
  to be animated is **not detached to window**.

### [1.1.3](https://github.com/universum-studios/android_transitions/releases/tag/v1.1.3) ###
> 13.12.2017

- **Removed deprecated** elements from previous versions.

### [1.1.2](https://github.com/universum-studios/android_transitions/releases/tag/v1.1.2) ###
> 24.08.2017

- Fixed some of reported issues.

### [1.1.1](https://github.com/universum-studios/android_transitions/releases/tag/v1.1.1) ###
> 28.07.2017

- **Dropped support** for _Android_ versions **below** _API Level 14_.

### [1.1.0](https://github.com/universum-studios/android_transitions/releases/tag/v1.1.0) ###
> 14.05.2017

- Added implementation of `Translate` transition which may be used to move views in the scene by
  a desired delta along both, X and Y, axes.
- Updated implementation of `Scale` transition which now also takes into account **current scale**
  value of the target view captured before transition starts. Scale transition also uses
  `FastOutSlowInInterpolator` as default interpolator for its corresponding animator.

### [1.0.2](https://github.com/universum-studios/android_transitions/releases/tag/v1.0.2) ###
> 02.04.2017

- Implementation of `BaseNavigationalTransition.configureTransitions(Activity)` has been split into
  `configureIncomingTransitions(Activity)` and `configureOutgoingTransitions(Activity)`. The **outgoing**
  configuration method is by default called whenever `BaseNavigationalTransition.start(Activity)` is 
  invoked and the **incoming** configuration method should be called by the activity to which is the
  calling activity transitioning from its `onCreate(Bundle)` method.

### [1.0.1](https://github.com/universum-studios/android_transitions/releases/tag/v1.0.1) ###
> 16.02.2017

- Updated `onStart(Fragment)` method for both `NavigationalTransition` and `NavigationalTransitionCompat`.

### [1.0.0](https://github.com/universum-studios/android_transitions/releases/tag/v1.0.0) ###
> 19.01.2017

- First production release.