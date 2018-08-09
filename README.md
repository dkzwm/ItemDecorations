# ItemDecorations
## English | [中文](https://github.com/dkzwm/ItemDecorations/blob/master/README_CN.md)
<p>
ItemDecorations is a dividers library for RecyclerView which use GridLayoutManager or LinearLayoutManager
<p/>

## Features
 1.Support cross-column when the RecyclerView used GridLayoutManager  
 2.Support horizontal orientation and vertical orientation  
 3.Support reversed layout  
 4.Support draw the divider inside each of item  

## Gradle
```groovy
repositories {  
    ...
    maven { url 'https://jitpack.io' }  
}

dependencies {  
    compile 'com.github.dkzwm:ItemDecorations:0.1.0’
}
``` 

## How to use
```
LinearItemDecoration linearItemDecoration = new LinearItemDecoration.Builder(context)
          .divider(new ColorDivider()).marginStart(10).marginEnd(10)
          .drawInsideEachOfItem(true).build();
recyclerView.addItemDecoration(linearItemDecoration);

GridItemDecoration gridItemDecoration = new GridItemDecoration.Builder(context)
        .drawInsideEachOfItem(true).columnDivider(new ColorDivider(Color.BLACK))
        .rowDivider(new ColorDivider(Color.BLACK)).build();
recyclerView.addItemDecoration(gridItemDecoration);
```
## Snapshot
<p>
<img src="snapshot1.png" alt="Drawing" width="280" />
<img src="snapshot2.png" alt="Drawing" width="280" />
<p/>
<p>
<img src="snapshot3.png" alt="Drawing" width="280" />
<img src="snapshot4.png" alt="Drawing" width="280" />
<p/>


  License
  -------

      Copyright 2017 dkzwm

      Licensed under the Apache License, Version 2.0 (the "License");
      you may not use this file except in compliance with the License.
      You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

      Unless required by applicable law or agreed to in writing, software
      distributed under the License is distributed on an "AS IS" BASIS,
      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
      See the License for the specific language governing permissions and
      limitations under the License.