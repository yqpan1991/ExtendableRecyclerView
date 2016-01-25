#ExtendableRecyclerView
##1.支持什么
ExtendableRecyclerView支持的功能如下:
1. 内部支持下拉刷新和加载更多

2. 支持是否允许下拉刷新加载更多

3. 复写的adapter支持动态的添加headerView(暂不支持添加其他的footer,目前的加载更多为一个footerView)


##2. 实现原理

此封装类为个人在其他的开源项目基础上进行了演变
之前的UltimateRecyclerView个人认为功能过于繁杂,因而只是取其核心部分,通过封装RecyclerView和SwipeRefreshLayout和加载更多的条目的逻辑
另外,参照其他的开源项目中,发现其headerView的实现逻辑存在问题,条目类型与headerView的position进行绑定,这个实现逻辑是存在问题的,因而个人将其纠正,每个headerView的type是不同的,并且是支持动态的增加和删除

注意: 此工程是采用google的support.design的sample为样例,进行的更改.

欢迎各位加入安卓源码分析群: 164812238

攀岩不止,永强不息~
