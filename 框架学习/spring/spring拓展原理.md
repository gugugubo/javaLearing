# 1. BeanFactoryPostProcessor原理



 1、BeanFactoryPostProcessor：beanFactory的后置处理器；
		在BeanFactory标准初始化之后调用，来定制和修改BeanFactory的内容；所有的bean定义已经保存加载到beanFactory，但是bean的实例还未创建

![1602727056656](https://gitee.com/gu_chun_bo/picture/raw/master/image/20201015095817-545011.png)

运行测试，可以发现BeanFactoryPostProcessor在BeanFactory标准初始化之后调用，此时bean的实例还未创建

![1602727331206](assets/1602727331206.png)

```properties
BeanFactoryPostProcessor原理:
1)、ioc容器创建对象
2)、invokeBeanFactoryPostProcessors(beanFactory);
		如何找到所有的BeanFactoryPostProcessor并执行他们的方法；
			1）、直接在BeanFactory中找到所有类型是BeanFactoryPostProcessor的组件，并执行他们的方法
			2）、在初始化创建其他组件前面执行
2、BeanDefinitionRegistryPostProcessor extends BeanFactoryPostProcessor
		postProcessBeanDefinitionRegistry();
		在所有bean定义信息将要被加载，bean实例还未创建的；
		优先于BeanFactoryPostProcessor执行；
		利用BeanDefinitionRegistryPostProcessor给容器中再额外添加一些组件；
```

在这里打一个断点

![image-20201016173037922](assets/image-20201016173037922.png)



![1602729018186](https://gitee.com/gu_chun_bo/picture/raw/master/image/20201015103018-233921.png)

invokeBeanFactoryPostProcessors(beanFactory);执行BeanFactoryPostProcessors(beanFactory)

![1602729035591](https://gitee.com/gu_chun_bo/picture/raw/master/image/20201015103035-224761.png)

![1602729047228](assets/1602729047228.png)

然后跳转到这里

![1602729135631](https://gitee.com/gu_chun_bo/picture/raw/master/image/20201015103217-958159.png)

来看看上图的代码的下面

让我们来看看是如何找到所有的BeanFactoryPostProcessor并执行他们的方法； 

直接在BeanFactory中找到所有类型是BeanFactoryPostProcessor的组件先进行分类，然后再执行他们的方法 ；

![1602730970839](https://gitee.com/gu_chun_bo/picture/raw/master/image/20201015110323-814898.png)

将上面分好组的postProcessorNames再转化为BeanFactotyPostPorcessor

![1602730043855](https://gitee.com/gu_chun_bo/picture/raw/master/image/20201015104724-320527.png)



![1602730947519](https://gitee.com/gu_chun_bo/picture/raw/master/image/20201015110228-939902.png)



然后执行inovokeBeanFactoryPostProcessors()方法

![1602730220336](https://gitee.com/gu_chun_bo/picture/raw/master/image/20201015105021-419734.png)



让我们看看这个方法

![1602730289393](https://gitee.com/gu_chun_bo/picture/raw/master/image/20201015105130-159784.png)

我们可以看到BeanFactoryPostProcessors是在初始化创建其他组件前面执行

![1602730824615](https://gitee.com/gu_chun_bo/picture/raw/master/image/20201015110025-215615.png)



# 2. BeanDefinitionRegistryPostProcessor 

```
BeanDefinitionRegistryPostProcessor extends BeanFactoryPostProcessor
```

BeanDefinitionRegistryPostProcessor 是BeanFactoryPostProcessor的子接口，多定义了一个

postProcessBeanDefinitionRegistry()方法。这个方法的执行时机：在所有bean定义信息将要被加载，bean实例还未创建的；利用BeanDefinitionRegistryPostProcessor给容器中再额外添加一些组件

![1602746251542](https://gitee.com/gu_chun_bo/picture/raw/master/image/20201015193155-260171.png)



我们来实现BeanDefinitionRegistryPostProcessor接口：

![1602746480796](https://gitee.com/gu_chun_bo/picture/raw/master/image/20201015193218-407149.png)



可以看到，在我们注册了一个bean之后，数量增加了一个；BeanDefinitionRegistryPostProcessor 是先于BeanFactoryPostProcessor执行的

![1602747002908](https://gitee.com/gu_chun_bo/picture/raw/master/image/20201015193152-802463.png)



让我们分析一下他的原理：

	原理：
		1）、ioc创建对象
		2）、refresh()-》invokeBeanFactoryPostProcessors(beanFactory);
		3）、从容器中获取到所有的BeanDefinitionRegistryPostProcessor组件。
			1、依次触发所有的postProcessBeanDefinitionRegistry()方法
			2、再来触发postProcessBeanFactory()方法BeanFactoryPostProcessor；
		4）、再来从容器中找到BeanFactoryPostProcessor组件；然后依次触发postProcessBeanFactory()方法


refresh()-》invokeBeanFactoryPostProcessors(beanFactory);

![1602747253595](https://gitee.com/gu_chun_bo/picture/raw/master/image/20201015193149-793913.png)





![1602747270161](https://gitee.com/gu_chun_bo/picture/raw/master/image/20201015193145-209777.png)



从容器中获取到所有的BeanDefinitionRegistryPostProcessor组件

![1602747452450](https://gitee.com/gu_chun_bo/picture/raw/master/image/20201015153841-871608.png)



1、依次触发所有的postProcessBeanDefinitionRegistry()方法

![1602747429077](https://gitee.com/gu_chun_bo/picture/raw/master/image/20201015153837-504473.png)



2、再来触发postProcessBeanFactory()方法BeanFactoryPostProcessor；

![1602747810482](https://gitee.com/gu_chun_bo/picture/raw/master/image/20201015154332-218492.png)

4）、再来从容器中找到BeanFactoryPostProcessor组件；然后依次触发postProcessBeanFactory()方法

![1602747895240](https://gitee.com/gu_chun_bo/picture/raw/master/image/20201015154456-259908.png)



# 3. ApplicationListener

```
3、ApplicationListener：监听容器中发布的事件。事件驱动模型开发；
	  public interface ApplicationListener<E extends ApplicationEvent>
		监听 ApplicationEvent 及其下面的子事件；
	 步骤：
		1）、写一个监听器（ApplicationListener实现类）来监听某个事件（ApplicationEvent及其子类）
			@EventListener;
			原理：使用EventListenerMethodProcessor处理器来解析方法上的@EventListener；
		2）、把监听器加入到容器；
		3）、只要容器中有相关事件的发布，我们就能监听到这个事件；
				ContextRefreshedEvent：容器刷新完成（所有bean都完全创建）会发布这个事件；
				ContextClosedEvent：关闭容器会发布这个事件；
		4）、发布一个事件：
				applicationContext.publishEvent()；


```

1）、写一个监听器（ApplicationListener实现类）来监听某个事件

我们这里监听ApplicationEvent及其子类

![1602748985255](https://gitee.com/gu_chun_bo/picture/raw/master/image/20201015160306-911389.png)



![1602749178871](https://gitee.com/gu_chun_bo/picture/raw/master/image/20201015160619-239358.png)

我们写一个监听器（ApplicationListener实现类）来监听某个事件（必须是ApplicationEvent及其子类）；把监听器加入到容器；只要容器中有相关事件的发布，我们就能监听到这个事件；

我们之前遇到的两个事件1.ContextRefreshedEvent：容器刷新完成（所有bean都完全创建）spring会发布这个事件；2.ContextClosedEvent：关闭容器spring会发布这个事件；

我们也可以自己发布一个事件：applicationContext.publishEvent()；

![1602749464894](https://gitee.com/gu_chun_bo/picture/raw/master/image/20201015161105-653906.png)



让我们来分析一下原理：现在我们一共有三个事件：ContextRefreshedEvent、IOCTest_Ext$1[source=我发布的时间]、ContextClosedEvent；

打个断点

![1602762100523](https://gitee.com/gu_chun_bo/picture/raw/master/image/20201015194144-581874.png)



 1）、ContextRefreshedEvent事件：
 	1）、容器创建对象：refresh()；

![1602768775314](assets/1602768775314.png)

 	2）、finishRefresh();容器刷新完成会发布ContextRefreshedEvent事件

![1602768821736](https://gitee.com/gu_chun_bo/picture/raw/master/image/20201015213342-7529.png)



![1602768916852](https://gitee.com/gu_chun_bo/picture/raw/master/image/20201015213517-392266.png)



![1602768952597](assets/1602768952597.png)

 	3）、publishEvent(new ContextRefreshedEvent(this));
 			1）、获取事件的多播器（即派发器，多播器就是把时间发布到多个监听器，让他们同时感知）：getApplicationEventMulticaster()

![1602768968206](https://gitee.com/gu_chun_bo/picture/raw/master/image/20201015213608-811687.png)

​		2）、multicastEvent派发事件：

​			1）、如果有Executor，可以支持使用Executor进行异步派发； Executor executor = getTaskExecutor(); 2）、否则，同步的方式直接执行listener方法；invokeListener(listener, event); 

```java
//获取到所有的ApplicationListener 即监听器；	
for (final ApplicationListener<?> listener : getApplicationListeners(event, type)) { }
```

![1602769334723](https://gitee.com/gu_chun_bo/picture/raw/master/image/20201015214216-258681.png)

拿到listener回调onApplicationEvent方法；

![1602769485504](https://gitee.com/gu_chun_bo/picture/raw/master/image/20201015214446-188589.png)

拿到listener回调onApplicationEvent方法；

![1602769500620](https://gitee.com/gu_chun_bo/picture/raw/master/image/20201015214501-743776.png)

​	然后监听器收到事件

![1602769875482](https://gitee.com/gu_chun_bo/picture/raw/master/image/20201015215117-421697.png)

上面的事件是容器发布的事件

放行，查看第二个事件的监听：第二个事件是我们自己发布的事件

![1602770192183](https://gitee.com/gu_chun_bo/picture/raw/master/image/20201015215633-865444.png)

![1602770312248](assets/1602770312248.png)

![1602770321911](https://gitee.com/gu_chun_bo/picture/raw/master/image/20201015215842-709626.png)

又是同样的流程啦

![1602769957268](https://gitee.com/gu_chun_bo/picture/raw/master/image/20201015215237-88390.png)

容器关闭也会发布ContextClosedEvent；流程同上

## ApplicationEventMulticaster

让我们再仔细看看这个事件多播器（派发器）

initApplicationEventMulticaster();初始化ApplicationEventMulticaster；

![1602774393225](https://gitee.com/gu_chun_bo/picture/raw/master/image/20201015230633-821407.png)

先去容器中找有没有id=“applicationEventMulticaster”的组件；

![1602774428486](assets/1602774428486.png)



如果没有this.applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);
 			并且加入到容器中，我们就可以在其他组件要派发事件，自动注入这个applicationEventMulticaster；

![1602774559327](https://gitee.com/gu_chun_bo/picture/raw/master/image/20201015230920-287721.png)



## ApplicationListener

容器怎么知道容器中有哪些监听器？

首先是我们将监听器加入到了容器中

![1602775067759](https://gitee.com/gu_chun_bo/picture/raw/master/image/20201015231748-435416.png)

 	1）、容器创建对象：refresh();

 	2）、registerListeners();

![1602774942915](assets/1602774942915.png)



从容器中拿到所有的监听器，把他们注册到applicationEventMulticaster中；

![1602775198426](assets/1602775198426.png)



在这里还发布了一个事件：

![1602775317950](https://gitee.com/gu_chun_bo/picture/raw/master/image/20201015232200-705258.png)



我们也可以通过注解监听事件：

![1602775411777](https://gitee.com/gu_chun_bo/picture/raw/master/image/20201015232334-406395.png)

 		

让我们来看看他的原理：EventListenerMethodProcessor

![1602775754110](https://gitee.com/gu_chun_bo/picture/raw/master/image/20201015232915-487123.png)



![1602775827894](https://gitee.com/gu_chun_bo/picture/raw/master/image/20201015233028-285221.png)



![1602775841702](https://gitee.com/gu_chun_bo/picture/raw/master/image/20201015233042-312184.png)



我们来给他的实现类打个断点：



![1602775990581](https://gitee.com/gu_chun_bo/picture/raw/master/image/20201015233313-161018.png)

​	1）、ioc容器创建对象并refresh()；

![1602776098614](assets/1602776098614.png)

  

2）、finishBeanFactoryInitialization(beanFactory);初始化剩下的单实例bean；

![1602776159217](assets/1602776159217.png)

​			

![1602776265116](https://gitee.com/gu_chun_bo/picture/raw/master/image/20201015233746-35917.png)

1）、先创建所有的单实例bean；getBean();

2）、获取所有创建好的单实例bean，判断是否是SmartInitializingSingleton类型的；
  				如果是就调用afterSingletonsInstantiated();

![1602776318566](https://gitee.com/gu_chun_bo/picture/raw/master/image/20201015233838-866836.png)


  	
			
  			




```
扩展原理：
BeanPostProcessor：bean后置处理器，bean创建对象初始化前后进行拦截工作的
1、BeanFactoryPostProcessor：beanFactory的后置处理器；
		在BeanFactory标准初始化之后调用，来定制和修改BeanFactory的内容；
		所有的bean定义已经保存加载到beanFactory，但是bean的实例还未创建
BeanFactoryPostProcessor原理:
1)、ioc容器创建对象
2)、invokeBeanFactoryPostProcessors(beanFactory);
		如何找到所有的BeanFactoryPostProcessor并执行他们的方法；
			1）、直接在BeanFactory中找到所有类型是BeanFactoryPostProcessor的组件，并执行他们的方法
			2）、在初始化创建其他组件前面执行
2、BeanDefinitionRegistryPostProcessor extends BeanFactoryPostProcessor
		postProcessBeanDefinitionRegistry();
		在所有bean定义信息将要被加载，bean实例还未创建的；
		优先于BeanFactoryPostProcessor执行；
		利用BeanDefinitionRegistryPostProcessor给容器中再额外添加一些组件；
	原理：
		1）、ioc创建对象
		2）、refresh()-》invokeBeanFactoryPostProcessors(beanFactory);
		3）、从容器中获取到所有的BeanDefinitionRegistryPostProcessor组件。
			1、依次触发所有的postProcessBeanDefinitionRegistry()方法
			2、再来触发postProcessBeanFactory()方法BeanFactoryPostProcessor；
		4）、再来从容器中找到BeanFactoryPostProcessor组件；然后依次触发postProcessBeanFactory()方法
	
3、ApplicationListener：监听容器中发布的事件。事件驱动模型开发；
	  public interface ApplicationListener<E extends ApplicationEvent>
		监听 ApplicationEvent 及其下面的子事件；
	 步骤：
		1）、写一个监听器（ApplicationListener实现类）来监听某个事件（ApplicationEvent及其子类）
			@EventListener;
			原理：使用EventListenerMethodProcessor处理器来解析方法上的@EventListener；
		2）、把监听器加入到容器；
		3）、只要容器中有相关事件的发布，我们就能监听到这个事件；
				ContextRefreshedEvent：容器刷新完成（所有bean都完全创建）会发布这个事件；
				ContextClosedEvent：关闭容器会发布这个事件；
		4）、发布一个事件：
				applicationContext.publishEvent()；
	
 原理：
 	ContextRefreshedEvent、IOCTest_Ext$1[source=我发布的时间]、ContextClosedEvent；
 1）、ContextRefreshedEvent事件：
 	1）、容器创建对象：refresh()；
 	2）、finishRefresh();容器刷新完成会发布ContextRefreshedEvent事件
 2）、自己发布事件；
 3）、容器关闭会发布ContextClosedEvent；
 
 【事件发布流程】：
 	3）、publishEvent(new ContextRefreshedEvent(this));
 			1）、获取事件的多播器（派发器）：getApplicationEventMulticaster()
 			2）、multicastEvent派发事件：
 			3）、获取到所有的ApplicationListener；
 				for (final ApplicationListener<?> listener : getApplicationListeners(event, type)) {
 				1）、如果有Executor，可以支持使用Executor进行异步派发；
 					Executor executor = getTaskExecutor();
 				2）、否则，同步的方式直接执行listener方法；invokeListener(listener, event);
 				 拿到listener回调onApplicationEvent方法；
 
 【事件多播器（派发器）】
 	1）、容器创建对象：refresh();
 	2）、initApplicationEventMulticaster();初始化ApplicationEventMulticaster；
 		1）、先去容器中找有没有id=“applicationEventMulticaster”的组件；
 		2）、如果没有this.applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);
 			并且加入到容器中，我们就可以在其他组件要派发事件，自动注入这个applicationEventMulticaster；
 
 【容器中有哪些监听器】
 	1）、容器创建对象：refresh();
 	2）、registerListeners();
 		从容器中拿到所有的监听器，把他们注册到applicationEventMulticaster中；
 		String[] listenerBeanNames = getBeanNamesForType(ApplicationListener.class, true, false);
 		//将listener注册到ApplicationEventMulticaster中
 		getApplicationEventMulticaster().addApplicationListenerBean(listenerBeanName);
 		
  SmartInitializingSingleton 原理：->afterSingletonsInstantiated();
  		1）、ioc容器创建对象并refresh()；
  		2）、finishBeanFactoryInitialization(beanFactory);初始化剩下的单实例bean；
  			1）、先创建所有的单实例bean；getBean();
  			2）、获取所有创建好的单实例bean，判断是否是SmartInitializingSingleton类型的；
  				如果是就调用afterSingletonsInstantiated();
		
```







