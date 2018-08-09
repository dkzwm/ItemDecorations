# ItemDecorations
## [English](https://github.com/dkzwm/ItemDecorations/blob/master/README.md) | 中文

<p>
ItemDecorations是RecyclerView的分隔线库，现阶段支持使用GridLayoutManager或LinearLayoutManager的布局管理器
<p/>

## 特性
 1.当使用GridLayoutManager时，支持跨列特性(SpanLookup)  
 2.支持横向和纵向布局  
 3.支持倒序(Reversed)  
 4.支持分割线画在每个视图内部  
 
## Gradle
```groovy
repositories {  
    ...
    maven { url 'https://jitpack.io' }  
}

dependencies {  
    compile 'com.github.dkzwm:ItemDecorations:0.0.2’
}
``` 

## 如何使用
```
\\线性布局
LinearItemDecoration linearItemDecoration = new LinearItemDecoration.Builder(this)
          .divider(new ColorDivider()).drawOverTop(false).marginEnd(10)
          .drawInsideEachOfItem(true).build();
recyclerView.addItemDecoration(linearItemDecoration);
\\网格布局
GridItemDecoration gridItemDecoration = new GridItemDecoration.Builder(this)
        .drawInsideEachOfItem(true).columnDivider(new ColorDivider(Color.BLACK))
        .rowDivider(new ColorDivider(Color.BLACK)).build();
recyclerView.addItemDecoration(gridItemDecoration);
```
## 预览图
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