# bloomfilter-center
目前还比较凌乱，凑合着看，正在整理ing <br>
布隆索引在实际工程中的使用：<br>
a、判断某一个key是否在某个布隆索引里面<br>
b、提供相应的容器管理机制、加载机制、构建机制、触发机制<br>
c、采用任务队列模型，目前设想：不仅仅局限于布隆索引，还可以用在其他地方，当然，这是后话了

## 架构示意图
![](./doc/bloomfilter-framework.jpg)

代码的整体设计思路：<br>
任务队列模型（生产者消费者模式）+ 模板模式 +  策略模式<br>

工程环境：<br>
jdk1.8(lambda表达式：仅仅只是为了用1.8而用，并没有强烈的性能要求) + maven

### BloomFilterService
定位:对外提供使用的方法：判断某个key是否在某个布隆索引里面。

### loader
接口：BloomFilterLoadService<br>

定位：加载器：负责加载布隆索引

### builder
接口：BloomFilterBuildService<br>
定位：构建器：负责构建布隆索引

### container
定位：容器:负责存储布隆索引

### controller
定位：控制者：负责加载相关配置，控制布隆索引加载、构建整个流程

### cleaner
定位：清理者：负责清理本地保存的临时布隆索引文件

### trigger
定位：触发器：负责触发布隆索引构建或者加载

### serializer
定位：序列化器：<br>
a、负责把布隆索引对象序列化成：byte[] <br> 
b、把byte[]序列化成布隆索引对象 <br>
目前提供jdk默认序列化器的实现，后续会新增其他序列化方式的实现，通过properties来切换

### repository
定位：仓储：负责与file system、db、mc等交互的一个地方

## 领域模型介绍

由于时间关系，第一个版本的领域模型有点复杂，后续会重新设计。期望能够做到：大道至简，简单、明了，接受大家的检验

预计会引入：任务链概念：task_chains

### BloomFilterWrapper
名字：暂定这个，后续考虑修改成其他<br>
定位：<br>
   (1)、负责分桶设计<br>
   (2)、能够灵活切换不同的布隆索引实现，通过配置文件切换实现：例如：guava里的实现、自定义的实现<br>
   (3)、不仅仅指布隆索引的实现，还可以是：counting bloomfilter、compress bloomfilter、coko bloomfilter<br>
   (4)、堆内内存、堆外内存
   
   
![](./doc/bloomfilter-fentong.png)


## 时间安排
目前，家事、工作的事情比较多，平时抽空慢慢完善这个东西，包括相关文档，论文这些东西<br>
初步计划：<br>
第一个版本：基于google的布隆索引设计：打算11月底<br> 

sorry，并未完成任务，最近忙着婚礼，工作，暂时没空写完它，违约了。。努力补完它ing。。

完善相关的文档：12月份底<br>
第二个版本：2018年<br>

