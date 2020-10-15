# BeanFactoryPostProcessor原理



 1、BeanFactoryPostProcessor：beanFactory的后置处理器；
		在BeanFactory标准初始化之后调用，来定制和修改BeanFactory的内容；所有的bean定义已经保存加载到beanFactory，但是bean的实例还未创建

BeanFactoryPostProcessor实现了beanFactory接口

![1602727056656](https://gitee.com/gu_chun_bo/picture/raw/master/image/20201015095817-545011.png)

![1602727219581](https://gitee.com/gu_chun_bo/picture/raw/master/image/20201015095933-939237.png)

运行测试，可以发现BeanFactoryPostProcessor在BeanFactory标准初始化之后调用，但是bean的实例还未创建

![1602727331206](assets/1602727331206.png)

```
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

![1602728975864](https://gitee.com/gu_chun_bo/picture/raw/master/image/20201015102937-262925.png)



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



# BeanDefinitionRegistryPostProcessor 

```
BeanDefinitionRegistryPostProcessor extends BeanFactoryPostProcessor
```

BeanDefinitionRegistryPostProcessor 是BeanFactoryPostProcessor的子接口，多定义了一个

postProcessBeanDefinitionRegistry()方法。这个方法的执行时机：在所有bean定义信息将要被加载，bean实例还未创建的；利用BeanDefinitionRegistryPostProcessor给容器中再额外添加一些组件

![1602746251542](assets/1602746251542.png)



我们来实现BeanDefinitionRegistryPostProcessor接口：

![1602746480796](assets/1602746480796.png)



可以看到，在我们注册了一个bean之后，数量增加了一个；BeanDefinitionRegistryPostProcessor 是先于BeanFactoryPostProcessor执行的

![1602747002908](assets/1602747002908.png)



让我们分析一下他的原理：

	原理：
		1）、ioc创建对象
		2）、refresh()-》invokeBeanFactoryPostProcessors(beanFactory);
		3）、从容器中获取到所有的BeanDefinitionRegistryPostProcessor组件。
			1、依次触发所有的postProcessBeanDefinitionRegistry()方法
			2、再来触发postProcessBeanFactory()方法BeanFactoryPostProcessor；
		4）、再来从容器中找到BeanFactoryPostProcessor组件；然后依次触发postProcessBeanFactory()方法


refresh()-》invokeBeanFactoryPostProcessors(beanFactory);

![1602747253595](assets/1602747253595.png)





![1602747270161](assets/1602747270161.png)



从容器中获取到所有的BeanDefinitionRegistryPostProcessor组件

![1602747452450](https://gitee.com/gu_chun_bo/picture/raw/master/image/20201015153841-871608.png)



1、依次触发所有的postProcessBeanDefinitionRegistry()方法

![1602747429077](https://gitee.com/gu_chun_bo/picture/raw/master/image/20201015153837-504473.png)





2、再来触发postProcessBeanFactory()方法BeanFactoryPostProcessor；

![1602747810482](https://gitee.com/gu_chun_bo/picture/raw/master/image/20201015154332-218492.png)

4）、再来从容器中找到BeanFactoryPostProcessor组件；然后依次触发postProcessBeanFactory()方法

![1602747895240](https://gitee.com/gu_chun_bo/picture/raw/master/image/20201015154456-259908.png)





# 3、ApplicationListener

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

ContextRefreshedEvent：容器刷新完成（所有bean都完全创建）spring会发布这个事件；ContextClosedEvent：关闭容器spring会发布这个事件；

我们也可以自己发布一个事件：applicationContext.publishEvent()；

![1602749464894](https://gitee.com/gu_chun_bo/picture/raw/master/image/20201015161105-653906.png)



![1602749273006](https://gitee.com/gu_chun_bo/picture/raw/master/image/20201015160753-633975.png)




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

