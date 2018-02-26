### Overview ###

Release patch.

### Changes ###

- `Reveal`, `Scale`, `Translate` transitions now return **null** `Animator` from `createAnimator(...)` factory methods and also from `onAppear(...)` and `onDisappear(...)` methods when a **target view** to be animated is **not detached to window**.