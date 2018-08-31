InfiniteViewPager
=========

InfiniteViewPager is a modified ViewPager that allows infinite paging.

Usage
----
Bind it in your layout:
```xml
<com.thehayro.view.InfiniteViewPager 
    android:layout_width="fill_parent"
    android:layout_height="fill_parent"/>
```

Setup the adapter:
```java
public class MyInfinitePagerAdapter<T> extends InfinitePagerAdapter {  // ...
}
```
And initialize with any init value:
```java
MyInfinitePagerAdapter<Integer> adapter = 
                    new MyInfinitePagerAdapter<Integer>(0);
```

Bind the adapter to the InfiniteViewPager with:
```java
// ...
InfiniteViewPager pager = (InfiniteViewPager) findViewById(R.id...);
pager.setAdapter(adapter);
```
Also see [website] for further details.

Changelog
----
 - 0.4.3 fix #5 (thanks to germands)
 - 0.4.1 fix #1 (thanks to RogerParis)
 - 0.4 added OnInfinitePageChangeListener
 - 0.3 fixed some errors
 - 0.2 Added custom 'setCurrentItem()'
 - 0.1 initial release

License
----

Apache Licence

  [Website]: http://thehayro.blogspot.de/2013/09/infiniteviewpager-infinite-paging.html
