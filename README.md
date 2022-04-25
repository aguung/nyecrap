# Nyecrap

Scrap url with jsoup for get meta

[![Release](https://jitpack.io/v/devfutech/Nyecrap.svg)](https://jitpack.io/#devfutech/nyecrap)
[![License](http://img.shields.io/badge/license-MIT-brightgreen.svg?style=flat-square)](https://tldrlegal.com/license/mit-license)

## Installation

Add it in your root build.gradle at the end of repositories:

```gradle
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

Add the dependency for corountine flow

```gradle
dependencies {
    implementation('com.github.devfutech.Nyecrap:kotlin-flow:1.0.0')
}
```

Add the dependency for rx java

```gradle
dependencies {
    implementation('com.github.devfutech.Nyecrap:rx-java:1.0.0')
}
```

## Usage

For corountine flow
```kotlin
lifecycleScope.launchWhenStarted {
            NyecrapFlow
                .instance
                .getLinkPreview(url)
                .catch { error ->
                    // Handle error when failed scrap
                }.collectLatest { result ->
                    // Result scrap
                }
        }
```
For rx java

```kotlin
NyecrapRxJava.instance.getLinkPreview(url)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result: LinkPreviewResult? ->
                // Result scrap
            }) {
                // Handle error when failed scrap
            }
```

## License

MIT License

Copyright (c) 2022 Agung Subastian

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
