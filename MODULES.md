Modules
===============

Library is also distributed via **separate modules** which may be downloaded as standalone parts of
the library in order to decrease dependencies count in Android projects, so only dependencies really
needed in an Android project are included. **However** some modules may depend on another modules
from this library or on modules from other libraries.

## Download ##

### Gradle ###

For **successful resolving** of artifacts for separate modules via **Gradle** add the following snippet
into **build.gradle** script of your desired Android project and use `implementation '...'` declaration
as usually.

    repositories {
        maven {
            url  "http://dl.bintray.com/universum-studios/android"
        }
    }

## Available modules ##
> Following modules are available in the [latest](https://github.com/universum-studios/android_transitions/releases "Releases page") stable release.

- **[Core](https://github.com/universum-studios/android_transitions/tree/master/library-core)**
- **[@Navigational](https://github.com/universum-studios/android_transitions/tree/master/library-navigational_group)**
- **[Navigational-Base](https://github.com/universum-studios/android_transitions/tree/master/library-navigational-base)**
- **[Navigational-Framework](https://github.com/universum-studios/android_transitions/tree/master/library-navigational-framework)**
- **[Navigational-Compat](https://github.com/universum-studios/android_transitions/tree/master/library-navigational-compat)**
- **[@View](https://github.com/universum-studios/android_transitions/tree/master/library-view_group)**
- **[View-Core](https://github.com/universum-studios/android_transitions/tree/master/library-view-core)**
- **[View-Reveal](https://github.com/universum-studios/android_transitions/tree/master/library-view-reveal)**
- **[View-Scale](https://github.com/universum-studios/android_transitions/tree/master/library-view-scale)**
- **[View-Translate](https://github.com/universum-studios/android_transitions/tree/master/library-view-translate)**
- **[@Window](https://github.com/universum-studios/android_transitions/tree/master/library-window_group)**
- **[Window-Core](https://github.com/universum-studios/android_transitions/tree/master/library-window-core)**
- **[Window-Common](https://github.com/universum-studios/android_transitions/tree/master/library-window-common)**
- **[Window-Extra](https://github.com/universum-studios/android_transitions/tree/master/library-window-extra)**
- **[Util](https://github.com/universum-studios/android_transitions/tree/master/library-util)**
