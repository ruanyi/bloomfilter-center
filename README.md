# bloomfilter-center
布隆索引在实际工程中的使用

## 架构示意图
![](./doc/bloomfilter-framework.jpg)

## 概念
### loader
定位：负责加载布隆索引

### builder
定位：负责构建布隆索引

### container
定位：负责存储布隆索引

### controller
定位：负责加载相关配置，控制布隆索引加载、构建整个流程

### cleaner
定位：负责清理本地布隆所文件

### 领域模型介绍

由于时间关系，第一个版本的领域模型有点复杂，后续会重新设计：大道至简

预计会引入：任务链概念：task_chains

####BloomFilterWrapper
名字：暂定这个，后续考虑修改成其他
定位：<br>
   (1)、负责分桶设计<br>
   (2)、能够灵活切换不通的布隆索引相关实现
   
![](./doc/bloomfilter-fentong.png)

   

## 时间安排

打算11月底，出第一个版本，关于布隆索引在实际当中的应用

