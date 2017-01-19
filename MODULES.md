Modules
===============

Library is also distributed via **separate modules** which may be downloaded as standalone parts of
the library in order to decrease dependencies count in Android projects, so only dependencies really
needed in an Android project are included. **However** some modules may depend on another modules
from this library or on modules from other libraries.

Below are listed modules that are available for download also with theirs dependencies.

## Download ##

### Gradle ###

For **successful resolving** of artifacts for separate modules via **Gradle** add the following snippet
into **build.gradle** script of your desired Android project and use `compile '...'` declaration
as usually.

    repositories {
        maven {
            url  "http://dl.bintray.com/universum-studios/android"
        }
    }

**[Core](https://github.com/universum-studios/android_transitions/tree/master/library/src/main)**

    compile 'universum.studios.android:transitions-core:1.0.0@aar'

**[Navigational](https://github.com/universum-studios/android_transitions/tree/master/library/src/navigational)**

    compile 'universum.studios.android:transitions-navigational:1.0.0@aar'

**[Navigational-Base](https://github.com/universum-studios/android_transitions/tree/master/library/src/navigational/base)**

    compile 'universum.studios.android:transitions-navigational-base:1.0.0@aar'

**[Navigational-Framework](https://github.com/universum-studios/android_transitions/tree/master/library/src/navigational/framework)**

    compile 'universum.studios.android:transitions-navigational-framework:1.0.0@aar'

_depends on:_
[transitions-navigational-base](https://github.com/universum-studios/android_database/tree/master/library/src/navigational/base)

**[Navigational-Compat](https://github.com/universum-studios/android_transitions/tree/master/library/src/navigational/compat)**

    compile 'universum.studios.android:transitions-navigational-compat:1.0.0@aar'

_depends on:_
[transitions-navigational-base](https://github.com/universum-studios/android_database/tree/master/library/src/navigational/base)

**[Window](https://github.com/universum-studios/android_transitions/tree/master/library/src/window)**

    compile 'universum.studios.android:transitions-window:1.0.0@aar'

**[Window-Core](https://github.com/universum-studios/android_transitions/tree/master/library/src/window/core)**

    compile 'universum.studios.android:transitions-window-core:1.0.0@aar'

**[Window-Common](https://github.com/universum-studios/android_transitions/tree/master/library/src/window/common)**

    compile 'universum.studios.android:transitions-window-common:1.0.0@aar'

_depends on:_
[transitions-window-core](https://github.com/universum-studios/android_database/tree/master/library/src/window/core)

**[Window-Extra](https://github.com/universum-studios/android_transitions/tree/master/library/src/window/extra)**

    compile 'universum.studios.android:transitions-window-extra:1.0.0@aar'

_depends on:_
[transitions-window-core](https://github.com/universum-studios/android_database/tree/master/library/src/window/core),
[transitions-window-common](https://github.com/universum-studios/android_database/tree/master/library/src/window/common)

**[View](https://github.com/universum-studios/android_transitions/tree/master/library/src/view)**

    compile 'universum.studios.android:transitions-view:1.0.0@aar'

_depends on:_
[transitions-util](https://github.com/universum-studios/android_database/tree/master/library/src/util)

**[View-Core](https://github.com/universum-studios/android_transitions/tree/master/library/src/view/core)**

    compile 'universum.studios.android:transitions-view-core:1.0.0@aar'

**[View-Reveal](https://github.com/universum-studios/android_transitions/tree/master/library/src/view/reveal)**

    compile 'universum.studios.android:transitions-view-reveal:1.0.0@aar'

_depends on:_
[transitions-core](https://github.com/universum-studios/android_database/tree/master/library/src/main),
[transitions-util](https://github.com/universum-studios/android_database/tree/master/library/src/util),
[transitions-view-core](https://github.com/universum-studios/android_database/tree/master/library/src/view/core)

**[View-Scale](https://github.com/universum-studios/android_transitions/tree/master/library/src/view/scale)**

    compile 'universum.studios.android:transitions-view-scale:1.0.0@aar'

_depends on:_
[transitions-core](https://github.com/universum-studios/android_database/tree/master/library/src/main)

**[View-Translate](https://github.com/universum-studios/android_transitions/tree/master/library/src/view/translate)**

> See **[1.1.0](https://github.com/universum-studios/android_transitions/milestone/1)** milestone.

_depends on:_
[transitions-core](https://github.com/universum-studios/android_database/tree/master/library/src/main)

**[Util](https://github.com/universum-studios/android_transitions/tree/master/library/src/util)**

    compile 'universum.studios.android:transitions-util:1.0.0@aar'
