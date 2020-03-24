# 第 4  章     基础篇 Linux 的目录结构

## 4.1              基本介绍

linux 的文件系统是采用级层式的树状目录结构，在此结构中的最上层是根目录“/”，然后在此目录下再创建其他的目录。

深刻理解 linux 树状文件目录是非常重要的，这里我给大家说明一下。

 

记住一句经典的话：在 Linux 世界里，一切皆文件。比如U盘插入，就会在对应的目录的子目录下生成文件

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224231-13699.gif)

 

## 4.2  目录结构的具体介绍

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224259-620123.gif)

![1570017237261](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224245-794608.png)

![1570017197081](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224242-484621.png)



 

## 4.3  Linux 目录总结一下

1)    linux 的目录中有且只要一个根目录 /

2)    linux 的各个目录存放的内容是规划好，不用乱放文件。

3)    linux 是以文件的形式管理我们的设备，因此 linux 系统，一切皆为文件。


4)    linux 的各个文件目录下存放什么内容，大家必须有一个认识。

5)    学习后，你脑海中应该有一颗 linux 目录


# 第 5  章     实操篇 远程登录 Linux 系统

## 5.1   为什么需要远程登录 Linux

### 5.1.1 示意图

  ![1570017776279](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224315-951450.png)



说明: 公司开发时候， 具体的情况是这样的

1)  linux 服务器是开发小组共享的.

2)   正式上线的项目是运行在公网的.

3)   因此程序员需要远程登录到 centos 进行项目管理或者开发.

4)   画出简单的网络拓扑示意图(帮助理解)

5)   远程登录客户端有 Xshell5， Xftp5 ,  我们学习使用 Xshell5 和 Xftp , 其它的远程工具大同小异.

## 5.2   远程登录 Linux-Xshell5

说明: Xshell 是目前最好的远程登录到 Linux 操作的软件，流畅的速度并且完美解决了中文乱码的问题， 是目前程序员首选的软件。

Xshell [1] 是一个强大的安全终端模拟软件，它支持 SSH1, SSH2, 以及 Microsoft Windows 平台的 TELNET 协议。

Xshell 可以在 Windows 界面下用来访问远端不同系统下的服务器，从而比较好的达到远程控制终端的目的。

特别说明：如果希望安装好 XShell 5 就可以远程访问 Linux 系统的话，需要有一个前提，就是Linux 启用了 SSHD 服务，该服务会监听 22  号端口。

 

## 5.3              安装 XShell5 并使用

###  5.3.1 安装过程

看老师的视频演示即可。基本是下一步即可

### 5.3.2  xshell关键配置

![1570018976734](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224319-652105.png)

XShel5 远程登录到 Linux 后，就可以使用指令来操作 Linux 系统

![1570019762586](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224319-996579.png)

## 5.4              远程上传下载文件 Xftp5

### 5.4.1  XFtp5 软件介绍

是一个基于 [windows ](https://baike.baidu.com/item/windows)平台的功能强大的 [SFTP](https://baike.baidu.com/item/SFTP)、[FTP ](https://baike.baidu.com/item/FTP)文件传输软件。使用了 Xftp  以后，windows 用户能安全地在 [UNIX](https://baike.baidu.com/item/UNIX)/[Linux ](https://baike.baidu.com/item/Linux)和 Windows PC 之间传输文件。(示意图)。

 

### 5.4.2 XFtp5 软件的安装

这个看老师的演示即可.

### 5.4.3 Xftp5 的配置和使用

![1570020112345](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224325-57551.png)

连接到 Linux 的界面如下,就说明已经成功的远程连接到 Linux


![1570020130954](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224329-781659.png)




# 第 6  章     实操篇 vi 和 vim  编辑器

## 6.1 vi 和 vim 的基本介绍

所有的 Linux 系统都会内建 vi 文本编辑器。

Vim 具有程序编辑的能力，可以看做是 Vi 的增强版本，可以主动的以字体颜色辨别语法的正确性，方便程序设计。代码补完、编译及错误跳转等方便编程的功能特别丰富，在程序员中被广泛使用。

 

## 6.2  vi 和 vim 的三种常见模式

### 6.2.1 正常模式

在正常模式下，我们可以使用快捷键。

以 vim 打开一个档案就直接进入一般模式了(这是默认的模式)。在这个模式中， 你可以使用『上下左右』按键来移动光标，你可以使用『删除字符』或『删除整行』来处理档案内容， 也可以使用『复制、贴上』来处理你的文件数据。



### 6.2.2 插入模式/编辑模式

在模式下，程序员可以输入内容。

按下 i, I, o, O, a, A, r, R 等任何一个字母之后才会进入编辑模式,  一般来说按 i 即可



### 6.2.3 命令行模式

在这个模式当中， 可以提供你相关指令，完成读取、存盘、替换、离开 vim 、显示行号等的动作则是在此模式中达成的！

## 6.3   快速入门案例

使用    vim 开发一个 Hello.java 程序,  保存.步骤说明和演示

![1570071760833](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224333-699310.png)

![1570071788941](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224335-650253.png)

## 6.4  vi 和 vim  三种模式的相互转化图


![1570071828568](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224336-459543.png)

 

 

## 6.5  快捷键的使用案例

1)    拷贝当前行 yy ,  拷贝当前行向下的 5 行 5yy，粘贴（p）。

2)    删除当前行 dd    , 删除当前行向下的 5 行 5dd

3)    在文件中查找某个单词  [命令行下 输入:/+关键字 ，再回车就可以查找  ,  输入 n 就是查找下一个 ],比如查询 hello.

4)    设置文件的行号，取消文件的行号.[命令行下: set nu  和:set nonu]

5)    编辑 /etc/profile 文件，使用快捷键到底文档的最末行[G]和最首行[gg],注意这些都是在正常模式下执行的。

6)    在一个文件中输入  "hello" ,然后又撤销这个动作，在正常模式下输入 u

7)    编辑    /etc/profile 文件，并将光标移动到   第 20 行    shift+g

- 第一步：显示行号 :set nu 
- 第二步：输入 20 这个数
- 第三步: 输入 shift+g




## 6.6  vim 和 vi 的快捷键键盘一览图

 ![1570072529599](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224340-373514.png)

 


# 第 7  章     实操篇 开机、重启和用户登录注销

## 7.1  关机&重启命令

### 7.1.1 基本介绍

- shutdown
  - shutdown -h now : 表示立即关机
  - shutdown -h 1 : 表示 1 分钟后关机
  - shutdown -r now:  立即重启

- half  直接使用，效果等价于关机

- reboot  重启系统

- sync  把内存的数据保存到磁盘上

  

### 7.1.2 注意细节

当我们关机或者重启时，都应该先执行以下 sync 指令，把内存的数据写入磁盘，防止数据丢失。

 

## 7.2   用户登录和注销

### 7.2.1 基本介绍

1)    登录时尽量少用 root 帐号登录，因为它是系统管理员，最大的权限，避免操作失误。可以利用普通用户登录，登录后再用”su -  用户名’命令来切换成系统管理员身份.

2)   在提示符下输入 logout 即可注销用户

![1570158805566](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224422-514256.png)

### 7.2.2 使用细节

1)logout 注销指令在图形运行级别无效，在 运行级别 3 下有效.

2)运行级别这个概念，后面给大家介绍

 


# 第 8  章     实操篇 用户管理

## 8.1 基本介绍

给大家画一个示意图，帮助大家理解用户管理的规则。

![1570159521982](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224356-418437.png)

说明

1)  Linux 系统是一个多用户多任务的操作系统，任何一个要使用系统资源的用户，都必须首先向系统管理员申请一个账号，然后以这个账号的身份进入系统。

2)  Linux 的用户需要至少要属于一个组。

 

## 8.2  添加用户

### 8.2.1 基本语法

useradd    [选项]    用户名

### 8.2.2 实际案例


添加一个用户 xm.自动创建一个xm的组，默认将用户xm放在这个xm的组下面，并且在默认/home目录下会新建一个目录

  ![1570159762240](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224435-751759.png)



### 8.2.3     细节说明

- 当创建成功后，会在/home目录下自动创建和用户同名的目录
- 也可以通过 useradd -d + 指定目录 + 新的用户名，给新创建的用户指定**家目录** 
  - ![1570160257070](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224437-826860.png)

## 8.3   给用户指定或者修改密码

基本语法   passwd 用户名应用案例

1) 给 xiaoming 指定密码

![1570160614762](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224439-458025.png)

 

## 8.4         删除用户

### 8.4.1 基本语法


userdel   + 用户名



### 8.4.2  删除案例

- 删除用户xm，但是保留家目录
  - ![1570176034484](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224442-700386.png)
- 删除用户xq及用户家目录
  - ![1570176065267](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224443-61421.png)

### 8.4.3思考题

在删除用户时，我们一般不会将家目录删除。

 

## 8.5         查询用户信息

### 8.5.1 基本语法

id + 用户名




### 8.5.2 应用实例

案例 ：请查询root 信息

![1570176149988](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224447-863472.png)



### 8.5.3 细节说明

1)   当用户不存在时，返回”无此用户”

 

## 8.6    切换用户

### 8.6.1 介绍

在操作 Linux 中，如果当前用户的权限不够，可以通过 su - 指令，切换到高权限用户，比如 root

### 8.6.2 基本语法

su    –     切换用户名

### 8.6.3 应用实例

1)     创建一个用户 zf, ，指定密码，然后切换到 zf.

![1570176343081](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224458-32609.png)




### 8.6.4 细节说明

1)从权限高的用户切换到权限低的用户，不需要输入密码，反之需要。

2)当需要返回到原来用户时，使用 exit 指令

 

## 8.7      用户组

### 8.7.1 介绍

类似于角色，系统可以对有共性的多个用户进行统一的管理。

### 8.7.2  增加组

 groupadd + 组名

### 8.7.2 案例演示

![1570176669448](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224456-56201.png)

### 8.7.4 删除组

指令(基本语法) ： groupdel + 组 名

 

### 8.7.5      案例演示

![1570176704357](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224503-324723.png)




## 8.8   增加用户时直接加上组

### 8.8.1指令(基本语法)

 useradd -g 用户组 用户名

### 8.8.2案例演示

增加一个用户 zwj, 直接将他指定到 wudang 步骤看演示：

 ![1570176828264](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224504-353500.png)

 

## 8.9  修改用户的组

### 8.9.1 指令(基本语法)

usermod    -g +用户组 +用户名



### 8.9.2 案例演示


创建一个 shaolin组，让将 zwj用户修改到 shaolin

![1570177048102](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224507-350852.png)



## 8.10     /etc/passwd 文件

![1570178055184](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224513-236647.png)

用户（user）的配置文件，记录用户的各种信息

![1570178153267](D:\BaiduNetdiskDownload\markdown图片\1570178153267.png)


每行的含义：用户名: 口令: 用户标识号: 组标识号: 注释性描述: 主目录: 登录 shell



## 8.11     /etc/shadow 文件(了解)

口令的配置文件

每行的含义：登录名:加密口令:最后一次修改时间:最小时间间隔:最大时间间隔:警告时间:不活动时间:失效时间:标志



## 8.12     /etc/group 文件


组的配置文件，记录 包含的组的信息每行含义：组名：口令：组标识号：组内用户列表（用户列表一般是看不到的）

![1570178413207](D:\BaiduNetdiskDownload\markdown图片\1570178413207.png)




# 第 9  章     实操篇 实用指令

## 9.1    指定运行级别

运行级别说明：

- 0  ：关机
- 1  ：单用户【找回丢失密码】
- 2：多用户状态没有网络服务
- 3：多用户状态有网络服务
- 4:   保留
- 5： 图形界面
- 6： 重启

常用的运行级别是  3  和   5 ，要修改运行级别可以修改文件/etc/initab的  id：5 : initdefault : 这一行中的数字



![1570179294407](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224514-53931.png)

 

 


## 9.2  切换到指定运行级别的指令

### 9.2.1基本语法

init [012356]

![1570179628473](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224517-381147.png)

###  9.2.2 应用实例

案例：通过init 命令来切换不同的运行级别，比如从 5 ->3，从图形界面切换到多用户状态有网络服务，然后关机

- init    3
- init    5
- init    0   

 

### 9.2.3 面试题

如何找回 root 密码，如果我们不小心，忘记 root  密码，怎么找回。

思路： 进入到 单用户模式，然后修改 root 密码。因为进入单用户模式，root 不需要密码就可以登录。

演示一把（注意观察）：


总结

开机->在引导时输入 回车键-> 看到一个界面输入 e ->  看到一个新的界面，选中第二行（编辑内核）在输入  e->  在这行最后输入  1 ,再输入 回车键->  再次输入 b ,这时就会进入到单用户模式。

这时，我们就进入到单用户模式，使用 passwd  指令来修改 root  密码。

 

### 9.2.4 课堂练习:

1)   假设我们的 root 密码忘记了，请问如何找回密码

2)   请设置我们的 运行级别，linux 运行后，直接进入到 命令行界面，即进入到 3 运行级别

vim /etc/inittab

将    id:5:initdefault:这一行中的数字, 5 这个数字改成对应的运行级别即可。

 

## 9.3   帮助指令

### 9.3.1 介绍

当我们对某个指令不熟悉时，我们可以使用 Linux 提供的帮助指令来了解这个指令的使用方法。

### 9.3.2 man 获得帮助信息

- 基本语法
  - man [命令或配置文件]（功能描述：获得帮助信息）
- 应用实例
  - 案例：查看 ls 命令的帮助信息
  - ![1570182269637](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224519-394845.png)

 

### 9.3.3 help 指令

-  基本语法

  - help 命令 （功能描述：获得 shell 内置命令的帮助信息）

  

### 9.3.4 当一个指令不熟悉如何学习的建议

百度帮助更直接

虽然上面两个都可以来获取指令帮助，但是需要英语功底，如果英语不太好的，我还是推荐大家直接百度靠谱。 ifconfig






## 9.4 文件目录类

### 9.4.1 pwd 指令

- 基本语法
  - pwd      (功能描述：显示当前工作目录的绝对路径)
- 应用实例
  - 案例：显示当前工作目录的绝对路径
  - ![1570183188380](D:\BaiduNetdiskDownload\markdown图片\1570183188380.png)



###   9.4.2 ls 指令

- 基本语法 ls [选项] [目录或是文件] 
- 常用选项 
  - -a ：显示当前目录所有的文件和目录，包括隐藏的。 
  - -l ：以列表的方式显示信息 
- 应用实例 案例:查看当前目录的所有内容信息
  - ![1570183268380](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224521-443714.png)



### 9.4.3 cd 指令

-  基本语法

  - cd    [参数] (功能描述：切换到指定目录) 

- 常用参数

  - cd~ 或者 cd ：回到自己的家目录
  -  cd.. 回到当前目录的上一级目录

- 案例1

  - 使用绝对路径切换到 root 目录
    - cd      /root

- 案例 2:   使用相对路径到/root 目录

  - 这里我们需要知道该用户目录在哪个目录下，才能写出这个指令，假设在/usr/lib
  - cd    ../../root

- 案例 3：表示回到当前目录的上一级目录

  - cd ..

- 案例 4：回到家目录cd

  - cd    ~

    ### 

### 9.4.4 mkdir 指令 


mkdir 指令用于创建目录(makedirectory) 

- 基本语法

  - mkdir [选项] 要创建的目录

- 常用选项

  - -p ：创建多级目录

- 案例 1:创建一个目录 /home/dog

  - ![1570183772766](D:\BaiduNetdiskDownload\markdown图片\1570183772766.png)

- 案例 2:创建多级目录 /home/animal/tiger

  - ![1570183789540](D:\BaiduNetdiskDownload\markdown图片\1570183789540.png)

     

### 9.4.5 rmdir 指令

- 介绍
  - rmdir 指令删除空目录
- 基本语法
  - rmdir [选项] 要删除的空目录 
- 使用细节
  - rmdir 删除的是空目录，如果目录下有内容时无法删除的。 提示：如果需要删除非空目录，需要使用 rm-rf 要删除的目录




### 9.4.6 touch 指令

- touch 指令创建空文件
- 基本语法
  - touch 文件名称
- 案例 1: 创建一个空文件 hello.txt
  - ![1570184131686](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224526-807190.png)



### 9.4.7 cp 指令[重要]

- cp 指令拷贝文件到指定目录
- 基本语法
  - cp [选项] source dest
- 常用选项
  - -r ：递归复制整个文件夹
- 应用实例
  - 案例 1: 将  /home/aaa.txt 拷贝到      /home/bbb 目录下[拷贝单个文件]
    - ![1570184093767](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224522-357570.png)
  - 案例 2: 递归复制整个文件夹，一定要带参数举例 
    - ![1570184194113](D:\BaiduNetdiskDownload\markdown图片\1570184194113.png)
    - 使用细节
      - 强制覆盖不提示的方法：\cp
      - ![1570184272341](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224525-680530.png)




### 9.4.8 rm 指令

rm 指令移除【删除】文件或目录

-  基本语法
  - rm    [选项]    要删除的文件或目录
- 常用选项
  - -r ：递归删除整个文件夹
  - -f ： 强制删除不提示
- 案例 1: 将 /home/aaa.txt 删除
  - ![1570185656778](D:\BaiduNetdiskDownload\markdown图片\1570185656778.png)
- 案例 2: 递归删除整个文件夹 /home/bbb
  - ![1570185672295](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224528-785552.png)
  - 使用细节 强制删除不提示的方法：带上 -f 参数即可
    - ![1570185711104](D:\BaiduNetdiskDownload\markdown图片\1570185711104.png)

 


### 9.4.9 mv 指令

mv 移动文件与目录或重命名

- 基本语法
  - mv    oldNameFile newNameFile    (功能描述：重命名) 
  - mv /temp/movefile /targetFolder (功能描述：移动文件)
- 应用实例
  - 案例 1: 将 /home/aaa.txt 文件 重新命名为 pig.txt
    - ![1570206552826](D:\BaiduNetdiskDownload\markdown图片\1570206552826.png)
  - 案例 2:将 /home/pig.txt 文件 移动到 /root 目录下
    - ![1570206575486](D:\BaiduNetdiskDownload\markdown图片\1570206575486.png)

  

### 9.4.10       cat 指令

cat 查看文件内容，是以只读的方式打开。

- 基本语法
  - cat    [选项] 要查看的文件
-  常用选项
  - -n ：显示行号
- 案例 1: /etc/profile 文件内容，并显示行
  - ![1570206740205](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224527-117802.png)
- 使用细节
  - cat 只能浏览文件，而不能修改文件，为了浏览方便，一般会带上                                                                                                                      管道命令 | more           即cat 文件名 | more 



### 9.4.11       more 指令

more 指令是一个基于 VI 编辑器的文本过滤器，它以全屏幕的方式按页显示文本文件的内容。more 指令中内置了若干快捷键，详见操作说明

- 基本语法
  - more 要查看的文件
- 操作说明
- 应用实例
  - 案例: 采用 more 查看文件      more  /etc/profile
  - ![1570207083306](D:\BaiduNetdiskDownload\markdown图片\1570207083306.png)
- 快捷键一览
  - ![1570207141632](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224529-815874.png)

 

 

### 9.4.12  less 指令

less 指令用来分屏查看文件内容，它的功能与 more 指令类似，但是比 more 指令更加强大，支持各种显示终端。less 指令在显示文件内容时，并不是一次将整个文件加载之后才显示，而是根据显示需要加载内容，对于显示大型文件具有较高的效率。

- 基本语法
  - less 要查看的文件
- 操作说明
- 案例: 采用 less 查看一个大文件文件 /opt/金庸-射雕英雄传 txt 精校版.txt
  - ![1570207354052](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224533-875113.png)
- 快捷键
  - ![1570207385434](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224530-822024.png)



### 9.4.13       > 指令 和 >>  指令

  介绍

- 指令 > 和指令 >> 
  - 指令> 输出重定向 : 会将原来的文件的内容覆盖 
  - 指令>> 追加： 不会覆盖原来文件的内容，而是追加到文件的尾部
- 基本语法 
  - 1)ls-l>文件 （功能描述：列表的内容写入文件 a.txt 中（覆盖写））
    - ![1570207846547](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224532-810728.png)
    - 说明：ls-l >a.txt, 将 ls-l 的显示的内容覆盖写入到 a.txt 文件，如果该文件不存在，就创建该文
      件
  - 2)ls-al>>文件 （功能描述：列表的内容追加到文件 aa.txt 的末尾）
    - ![1570207888067](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224537-809666.png)
  - 3)cat 文件 1> 文件 2（功能描述：将文件 1 的内容覆盖到文件 2）
    - ![1570207901453](D:\BaiduNetdiskDownload\markdown图片\1570207901453.png)
  - 4)echo"内容">> 文件
    - 案例 1: 将 /home 目录下的文件列表 写入到 /home/info.txt 中
      - ![1570208006143](D:\BaiduNetdiskDownload\markdown图片\1570208006143.png)
    - 案例 2: 将当前日历信息 追加到 /home/mycal 文件中 [提示 cal]
      - ![1570208020126](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224541-240478.png)

 

### 9.4.14       echo 指令

echo 输出内容到控制台。

-  基本语法
  
  - echo    [选项]    [输出内容]
- 案例: 使用 echo 指令输出环境变量,输出当前的环境路径。
  - ![1570245869176](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224538-387220.png)

  

### 9.4.15 head 指令

head 用于显示文件的开头部分内容，默认情况下 head 指令显示文件的前 10 行内容

- 基本语法
  -  head 文件 (功能描述：查看文件头 10 行内容) 
  - head-n5 文件 (功能描述：查看文件头 5 行内容，5 可以是任意行数)
- 案例: 查看/etc/profile 的前面 5 行代码
  - ![1570245982947](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224542-432178.png) 



### 9.4.16       tail 指令

tail 用于输出文件中尾部的内容，默认情况下 tail 指令显示文件的后 10 行内容。

- 基本语法 
  - 1)tail 文件 （功能描述：查看文件后 10 行内容） 
  - 2)tail -n5 文件 （功能描述：查看文件后 5 行内容，5 可以是任意行数）
  - 3)tail -f 文件 （功能描述：实时追踪该文档的所有更新，工作经常使用）
- 案例 1: 查看/etc/profile 最后 5 行的代码
  - ![1570246093716](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224547-719516.png)
- 案例 2: 实时监控 mydate.txt, 看看到文件有变化时，是否看到， 实时的追加日期
  - ![1570246109086](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224542-656114.png)



### 9.4.17 ln 指令

软链接也叫符号链接，类似于 windows 里的快捷方式，主要存放了链接其他文件的路径

- 基本语法 
  - ln-s[原文件或目录] [软链接名] （功能描述：给原文件创建一个软链接）
- 案例 1: 在/home 目录下创建一个软连接 linkToRoot，连接到 /root 目录
  - ![1570246473088](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224546-846419.png)
- 案例 2: 删除软连接 linkToRoot
  - ![1570246503129](D:\BaiduNetdiskDownload\markdown图片\1570246503129.png)
- 细节说明 当我们使用 pwd 指令查看目录时，仍然看到的是软链接所在目录。



### 9.4.18      history 指令

查看已经执行过历史命令,也可以执行历史指令

- 基本语法

  -  history （功能描述：查看已经执行过历史命令）

- 案例 1: 显示所有的历史命令

  - ![1570246560789](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224548-496974.png)

- 案例 2: 显示最近使用过的 10 个指令。

  - ![1570246572712](D:\BaiduNetdiskDownload\markdown图片\1570246572712.png)

- 案例 3：执行历史编号为 178 的指令

  - ![1570246594548](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224549-639292.png)

  

## 9.5 时间日期类

### 9.5.1     date 指令-显示当前日期

- 基本语法 
  - 1)date （功能描述：显示当前时间） 
  - 2)date+%Y （功能描述：显示当前年份）
  - 3)date+%m （功能描述：显示当前月份） 
  - 4)date+%d （功能描述：显示当前是哪一天） 
  - 5)date"+%Y-%m-%d%H:%M:%S"（功能描述：显示年月日时分秒）
- 案例 1: 显示当前时间信息
  - ![1570247477925](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224550-810111.png)
- 案例 2: 显示当前时间年月日
  - ![1570247484980](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224551-117798.png)

### 9.5.2 date 指令-设置日期

-  基本语法 
  - date -s 字符串时间
- 案例 1: 设置系统当前时间 ， 比如设置成 2018-10-1011:22:22
  - ![1570247527424](D:\BaiduNetdiskDownload\markdown图片\1570247527424.png)

### 9.5.3 cal 指令

-  查看日历指令

- 基本语法 

  - cal[选项] （功能描述：不加选项，显示本月日历）

- 案例 1: 显示当前日历

  - ![1570247579225](D:\BaiduNetdiskDownload\markdown图片\1570247579225.png)

- 案例 2: 显示 2020 年日历

  - ![1570247594388](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224555-19244.png)

  

## 9.6  搜索查找类

### 9.6.1 find 指令

find 指令将从指定目录向下递归地遍历其各个子目录，将满足条件的文件或者目录显示在终端。

- 基本语法

  - find    [搜索范围]    [选项]

- 选项说明

  - ![1570247672270](D:\BaiduNetdiskDownload\markdown图片\1570247672270.png)

- 按文件名：根据名称查找/home 目录下的 hello.txt 文件

  - ![1570247689365](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224556-2685.png)

- 案例 2：按拥有者：查找/opt 目录下，用户名称为 nobody 的文件

  - ![1570247700870](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224557-405623.png)

- 案例 3：查找整个 linux 系统下大于 20m 的文件（+n 大于 -n 小于 n 等于）

  - ![1570247714491](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224557-540106.png)

- 查询 / 目录下，所有 .txt 的文件

  - ![1570247801767](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224558-224012.png)

    

### 9.6.2 locate 指令

 locaate 指令可以快速定位文件路径。locate 指令利用事先建立的系统中所有文件名称及路径的

locate 数据库实现快速定位给定的文件。Locate 指令无需遍历整个文件系统，查询速度较快。为了保证查询结果的准确度，管理员必须定期更新 locate 时刻。

- 基本语法

  - locate 搜索文件

- 特别说明

  - 由于 locate 指令基于数据库进行查询，所以第一次运行前，必须使用 updatedb 指令创建 locate 数据库。

- 案例 1: 请使用 locate 指令快速定位 hello.txt 文件所在目录

  - ![1570249389555](D:\BaiduNetdiskDownload\markdown图片\1570249389555.png)

  




### 9.6.3     grep 指令和 管道符号 |

grep 过滤查找 ， 管道符，“|”，表示将前一个命令的处理结果输出传递给后面的命令处理。

- 基本语法
  - grep [选项] 查找内容 源文件
- 常用选项
  - ![1570249597188](D:\BaiduNetdiskDownload\markdown图片\1570249597188.png)
- 案例 1: 请在 hello.txt 文件中，查找 "yes" 所在行，并且显示行号
  - ![1570249608506](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224603-239382.png)

 

## 9.7 压缩和解压类

### 9.7.1 gzip/gunzip 指令

gzip 用于压缩文件， gunzip  用于解压的

- 基本语法
  - 
    gzip 文件     （功能描述：压缩文件，只能将文件压缩为*.gz 文件）
  - gunzip 文 件.gz   （功能描述：解压缩文件命令）
- 案例 1:gzip 压缩， 将 /home 下的 hello.txt 文件进行压缩
  - ![1570254820789](D:\BaiduNetdiskDownload\markdown图片\1570254820789.png)
- 案例 2:gunzip 压缩， 将 /home 下的 hello.txt.gz 文件进行解压缩
  - ![1570254836603](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224604-254579.png)
- 细节说明:  当我们使用 gzip  对文件进行压缩后，不会保留原来的文件。



### 9.7.2     zip/unzip 指令

zip 用于压缩文件， unzip 用于解压的，这个在项目打包发布中很有用的

- 基本语法

  - zip            [选项] XXX.zip  + 将要压缩的内容（功能描述：压缩文件和目录的命令）
  - unzip       [选项] XXX.zip （功能描述：解压缩文件）

- zip 常用选项

  - -r：递归压缩，即压缩目录

- unzip 的常用选项

  - -d<目录> ：指定解压后文件的存放目录

- 案例 1: 将 /home 下的 所有文件进行压缩成 mypackage.zip

  - ![1570255003102](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224606-553858.png)

- 案例 2: 将 mypackge.zip 解压到 /opt/tmp 目录下

  - ![1570255014682](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224604-140339.png)

  

 

### 9.7.3     tar 指令

tar 指令 是打包指令，最后打包后的文件是 .tar.gz 的文件。

- 基本语法
  - tar    [选项]    XXX.tar.gz    打包的内容      (功能描述：打包目录，压缩后的文件格式.tar.gz)
- 选项说明
  - ![1570255291790](D:\BaiduNetdiskDownload\markdown图片\1570255291790.png)
- 案例 1: 压缩多个文件，将 /home/a1.txt 和 /home/a2.txt 压缩成 a.tar.gz
  - ![1570255324844](D:\BaiduNetdiskDownload\markdown图片\1570255324844.png)
- 案例 2: 将/home 的文件夹 压缩成 myhome.tar.gz
  - ![1570255341861](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224609-274740.png)
- 案例 3: 将 a.tar.gz 解压到当前目录
  - ![1570255446659](D:\BaiduNetdiskDownload\markdown图片\1570255446659.png)
- 案例 4: 将 myhome.tar.gz 解压到 /opt/ 目录下
  - ![1570255461363](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224609-63500.png)
  - 指定解压到的那个目录，事先要存在才能成功，否则会报错。



## 第 10 章 实操篇组管理和权限管理

### 10.1Linux 组基本介绍

在 linux 中的每个用户必须属于一个组，不能独立于组外。在 linux 中每个文件有所有者、所在组、其它组的概念。

1)   所有者

2)   所在组

3)   其它组

4)   改变用户所在的组

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224611-476725.png)

#### 10.2文件/目录所有者

一般为文件的创建者,谁创建了该文件，就自然的成为该文件的所有者。

##### 10.2.1 查看文件的所有者

1)    指令：ls -ahl

2)    应用实例：创建一个组 police,再创建一个用户 tom,将 tom 放在 police 组 ,然后使用 tom 来创

建一个文件 ok.txt，看看情况如何

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224613-491301.gif)

##### 10.2.2 修改文件所有者

•指令：chown 用户名 文件名

•应用案例要求：使用 root 创建一个文件 apple.txt ，然后将其所有者修改成 tom

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224614-507638.jpeg)

### 10.3组的创建

#### 10.3.1 基本指令

groupadd 组名

10.3.2 应用实例:

创建一个组, ,monster

创建一个用户 fox ，并放入到 monster 组中

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224615-811348.jpeg)

#### 10.4文件/目录所在组

当某个用户创建了一个文件后，默认这个文件的所在组就是该用户所在的组。

##### 10.4.1 查看文件/目录所在组

•基本指令

ls –ahl

•应用实例

##### 10.4.2 修改文件所在的组

•基本指令 chgrp 组名 文件名

•应用实例

使用 root 用户创建文件 orange.txt ,看看当前这个文件属于哪个组，然后将这个文件所在组，修改到 police 组。

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224617-680759.jpeg)

### 10.5其它组

除文件的所有者和所在组的用户外，系统的其它用户都是文件的其它组.

#### 10.6改变用户所在组

在添加用户时，可以指定将该用户添加到哪个组中，同样的用 root 的管理权限可以改变某个用户所在的组。

##### 10.6.1 改变用户所在组

1)  usermod –g 组名 用户名

2)  usermod –d 目录名 用户名 改变该用户登陆的初始目录。

###### 10.6.2 应用实例

创建一个土匪组（bandit）将 tom 这个用户从原来所在的 police 组，修改到 bandit(土匪) 组

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224618-316746.jpeg)

#### 10.7权限的基本介绍

​         ls    -l 中显示的内容如下：

**-rwxrw-r--** 1 root root 1213 Feb 2 09:39 abc

0-9 位说明

1)第 0 位确定文件类型(d, - , l , c , b)

2)第 1-3 位确定所有者（该文件的所有者）拥有该文件的权限。---User

3)第 4-6 位确定所属组（同用户组的）拥有该文件的权限，---Group

4)第 7-9 位确定其他用户拥有该文件的权限 ---Other

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224619-341362.jpeg)

### 10.8rwx 权限详解

#### 10.8.1       rwx 作用到文件

1)       [ r ]代表可读(read): 可以读取,查看

2)       [ w ]代表可写(write): 可以修改,但是不代表可以删除该文件,删除一个文件的前提条件是对该文件所在的目录有写权限，才能删除该文件.

3)       [ x ]代表可执行(execute):可以被执行

#### 10.8.2       rwx 作用到目录

1)  [ r ]代表可读(read): 可以读取，ls 查看目录内容

2)  [ w ]代表可写(write): 可以修改,目录内创建+删除+重命名目录

3)  [ x ]代表可执行(execute):可以进入该目录

#### 10.9文件及目录权限实际案例

​          ls    -l 中显示的内容如下：(记住)

-rwxrw-r-- 1 root root 1213 Feb 2 09:39 abc

10 个字符确定不同用户能对文件干什么

第一个字符代表文件类型： 文件 (-),目录(d),链接(l)

其余字符每 3 个一组(rwx) 读(r) 写(w) 执行(x) 第一组 rwx : 文件拥有者的权限是读、写和执行

第二组 rw- : 与文件拥有者同一组的用户的权限是读、写但不能执行

第三组 r-- : 不与文件拥有者同组的其他用户的权限是读不能写和执行可用数字表示为: r=4,w=2,x=1 因此 rwx=4+2+1=7

1 文件：硬连接数或 目录：子目录数 root 用户 root    组

​                  1213                    文件大小(字节)，如果是文件夹，显示 4096 字节

Feb 2 09:39       最后修改日期 abc 文件名

##### 10.10 修改权限-chmod

###### 10.10.1 基本说明：

通过 chmod 指令，可以修改文件或者目录的权限

###### 10.10.2 第一种方式：+ 、-、= 变更权限

u:所有者 g:所有组 o:其他人 a:所有人(u、g、o 的总和)

1)  chmod    u=rwx,g=rx,o=x  文件目录名

2)  chmod    o+w       文件目录名

3)  chmod    a-x  文件目录名

• 案例演示

1)    给 abc 文件 的所有者读写执行的权限，给所在组读执行权限，给其它组读执行权限。

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224621-810813.jpeg)

2)    给 abc 文件的所有者除去执行的权限，增加组写的权限

![img](D:\BaiduNetdiskDownload\markdown图片\clip_image014.jpg)

3)    给 abc 文件的所有用户添加读的权限

![img](D:\BaiduNetdiskDownload\markdown图片\clip_image015.jpg)

###### 10.10.3 第二种方式：通过数字变更权限

​          规则：r=4 w=2 x=1          ,rwx=4+2+1=7

chmod u=rwx,g=rx,o=x  文件目录名相当于 chmod     751 文件目录名

• 案例演示

要求：将 /home/abc.txt 文件的权限修改成 rwxr-xr-x, 使用给数字的方式实现： rwx = 4+2+1 = 7 r-x = 4+1=5 r-x = 4+1 =5 指令：chmod 755 /home/abc.txt

##### 10.11 修改文件所有者-chown

10.11.1 基本介绍 chown newowner       file 改变文件的所有者 chown newowner:newgroup file 改变用户的所有者和所有组

​         -R      如果是目录 则使其下所有子文件或目录递归生效

###### 10.11.2 案例演示：

1)      请将 /home/abc .txt 文件的所有者修改成 tom

![img](D:\BaiduNetdiskDownload\markdown图片\clip_image016.jpg)

2)      请将 /home/kkk 目录下所有的文件和目录的所有者都修改成 tom 首选我们应该使用 root 操作。

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224624-722003.jpeg)

##### 10.12 修改文件所在组-chgrp

10.12.1 基本介绍 chgrp newgroup file 改变文件的所有组

###### 10.12.2 案例演示：

1)      请将 /home/abc .txt 文件的所在组修改成 bandit (土匪)

chgrp bandit /home/abc.txt

2)      请将 /home/kkk 目录下所有的文件和目录的所在组都修改成 bandit(土匪)

chgrp -R bandit /home/kkk

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224624-622598.jpeg)

#### 10.13 最佳实践-警察和土匪游戏

police ， bandit jack, jerry: 警察 xh, xq: 土匪

(1)      创建组

bash> groupadd police bash> groupadd bandit

(2)      创建用户

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224625-438111.gif)

(3)      jack 创建一个文件，自己可以读写，本组人可以读，其它组没人任何权限

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224627-559318.jpeg)

(4)      jack 修改该文件，让其它组人可以读, 本组人可以读写

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224626-78288.jpeg)

(5)      xh 投靠 警察，看看是否可以读写.

先用 root 修改 xh 的组 ：

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224628-103733.jpeg)

使用 jack 给他的家目录 /home/jack 的所在组一个 rx 的权限

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224630-145319.jpeg)

xh 需要重新注销在到 jack 目录就可以操作 jack 的文件

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224631-201041.jpeg)

##### 10.14 课后练习

练习文件权限管理[课堂练习] 建立两个组（神仙,妖怪）

建立四个用户(唐僧,悟空，八戒，沙僧) 设置密码

把悟空，八戒放入妖怪 唐僧 沙僧 在神仙

用悟空建立一个文件 （monkey.java 该文件要输出 i am monkey）给八戒一个可以 r w 的权限

八戒修改 monkey.java 加入一句话( i am pig)

唐僧 沙僧 对该文件没有权限

把 沙僧 放入妖怪组

让沙僧 修改 该文件 monkey, 加入一句话 ("我是沙僧，我是妖怪!");

##### 10.15 课后练习题 2

1 用 root 登录，建立用户 mycentos,自己设定密码

2.用 mycentos 登录，在主目录下建立目录 test/t11/t1

3.在 t1 中建立一个文本文件 aa,用 vi 编辑其内容为 ls –al

4.改变 aa 的权限为可执行文件[可以将当前日期追加到一个文件],运行该文件./aa

5.删除新建立的目录 test/t11/t1

6.删除用户 mycentos 及其主目录中的内容

7.将 linux 设置成进入到图形界面的

\8. 重新启动 linux 或关机


## 第 11 章 实操篇 crond 任务调度

### 11.1原理示意图

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224632-855586.gif)

crontab 进行 定时任务的设置，。

#### 11.2 概述

任务调度：是指系统在某个时间执行的特定的命令或程序。

任务调度分类：1.系统工作：有些重要的工作必须周而复始地执行。如病毒扫描等

2.个别用户工作：个别用户可能希望执行某些程序，比如对 mysql 数据库的备份。

#### 11.3 基本语法

crontab [选项]

##### 11.3.1 常用选项

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224634-444987.jpeg)

### 11.4快速入门

#### 11.4.1 任务的要求

设置任务调度文件：/etc/crontab

设置个人任务调度。执行 crontab –e 命令。接着输入任务到调度文件

​                              如：*/1 * * * * ls –l    /etc/ > /tmp/to.txt

意思说每小时的每分钟执行 ls –l /etc/ > /tmp/to.txt 命令

#### 11.4.2 步骤如下

1)   cron -e

2)   */ 1 * * * * ls -l /etc >> /tmp/to.txt

3)   当保存退出后就程序。

4)   在每一分钟都会自动的调用 ls -l /etc >> /tmp/to.txt

#### 11.4.3 参数细节说明

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224635-678758.gif)

#### 11.5任务调度的几个应用实例

##### 11.5.1 案例 1：每隔 1 分钟，就将当前的日期信息，追加到 /tmp/mydate 文件中

1)   先编写一个文件 /home/mytask1.sh date >> /tmp/mydate

2)   给 mytask1.sh 一个可以执行权限 chmod 744 /home/mytask1.sh

3)   crontab -e

4)   */1 * * * *     /home/mytask1.sh

5)   成功

##### 11.5.2 案例 2：每隔 1 分钟，将当前日期和日历都追加到 /home/mycal 文件中

1)    先编写一个文件 /home/mytask2.sh

date >> /tmp/mycal cal >> /tmp/mycal

2)    给 mytask1.sh 一个可以执行权限

chmod 744 /home/mytask2.sh

3)    crontab -e

4)    */1 * * * *     /home/mytask2.sh

5)    成功

11.5.3 案例 3: 每天凌晨 2:00 将 mysql 数据库 testdb ，备份到文件中 mydb.bak。

1)    先编写一个文件 /home/mytask3.sh

/usr/local/mysql/bin/mysqldump -u root -proot testdb > /tmp/mydb.bak

2)    给 mytask3.sh 一个可以执行权限

chmod 744 /home/mytask3.sh 3) crontab -e

4)       0 2 * * *       /home/mytask3.sh

5)       成功

11.6crond 相关指令:

1)    conrtab –r：终止任务调度。

2)    crontab –l：列出当前有那些任务调度

3)    service crond restart    [重启任务调度]


## 第 12 章实操篇 Linux 磁盘分区、挂载

### 12.1 分区基础知识

#### 12.1.1 分区的方式：

1)  mbr 分区:

1.最多支持四个主分区

2.系统只能安装在主分区

3.扩展分区要占一个主分区

4.MBR 最大只支持 2TB，但拥有最好的兼容性

2)  gtp 分区:

1.支持无限多个主分区（但操作系统可能限制，比如 windows 下最多 128 个分区）

2.最大支持 18EB 的大容量（1EB=1024 PB，1PB=1024 TB ）

3.windows7 64 位以后支持 gtp

#### 12.1.2       windows 下的磁盘分区

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224637-874199.jpeg)

#### 12.2 Linux 分区

##### 12.2.1 原理介绍

1)Linux 来说无论有几个分区，分给哪一目录使用，它归根结底就只有一个根目录，一个独立且唯一的文件结构 , Linux 中每个分区都是用来组成整个文件系统的一部分。

2)Linux 采用了一种叫“载入”的处理方法，它的整个文件系统中包含了一整套的文件和目录，且将一个分区和一个目录联系起来。这时要载入的一个分区将使它的存储空间在一个目录下获得。

3)示意图

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224639-54761.jpeg)

##### 12.2.2 硬盘说明

1)Linux 硬盘分 IDE 硬盘和 SCSI 硬盘，目前基本上是 SCSI 硬盘

2)对于 IDE 硬盘，驱动器标识符为“hdx~”,其中“hd”表明分区所在设备的类型，这里是指 IDE 硬盘了。“x”为盘号（a 为基本盘，b 为基本从属盘，c 为辅助主盘，d 为辅助从属盘）,“~”代表分区，前四个分区用数字 1 到 4 表示，它们是主分区或扩展分区，从 5 开始就是逻辑分区。例，hda3 表示为第一个 IDE 硬盘上的第三个主分区或扩展分区,hdb2 表示为第二个 IDE 硬盘上的第二个主分区或扩展分区。

3)对于 SCSI 硬盘则标识为“sdx~”，SCSI 硬盘是用“sd”来表示分区所在设备的类型的，其余则和 IDE 硬盘的表示方法一样。

##### 12.2.3 使用 lsblk 指令查看当前系统的分区情况

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224640-602893.jpeg)

![img](file:///C:/Users/古春波/AppData/Local/Packages/oice_16_974fa576_32c1d314_327f/AC/Temp/msohtmlclip1/01/clip_image040.jpg)

### 12.3 挂载的经典案例

需求是给我们的 Linux 系统增加一个新的硬盘，并且挂载到/home/newdisk

![img](D:\BaiduNetdiskDownload\markdown图片\clip_image042.jpg)

#### 12.3.1 如何增加一块硬盘

1)虚拟机添加硬盘

​                  2)分区    fdisk /dev/sdb

​                   3)格式化 mkfs     -t ext4    /dev/sdb1

​                  4)挂载   先创建一个 /home/newdisk    , 挂载 mount    /dev/sdb1       /home/newdisk

5)设置可以自动挂载(永久挂载，当你重启系统，仍然可以挂载到 /home/newdisk) 。 vim /etc/fstab

​                   /dev/sdb1                    /home/newdisk                      ext4        defaults         0 0

### 12.4 具体的操作步骤整理

#### 12.4.1 虚拟机增加硬盘步骤 1

在【虚拟机】菜单中，选择【设置】，然后设备列表里添加硬盘，然后一路【下一步】，中间只有选择磁盘大小的地方需要修改，至到完成。然后重启系统（才能识别）！

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224643-552100.jpeg)

#### 12.4.2 虚拟机增加硬盘步骤 2

分区命令 fdisk /dev/sdb 开始对/sdb 分区

•m 显示命令列表

​          •p          显示磁盘分区 同 fdisk –l

​          •n        新增分区

​          •d          删除分区

•w 写入并退出

说明： 开始分区后输入 n，新增分区，然后选择 p ，分区类型为主分区。两次回车默认剩余全部空间。最后输入 w 写入分区并退出，若不保存退出输入 q。

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224644-686278.jpeg)

#### 12.4.3 虚拟机增加硬盘步骤 3

格式化磁盘

分区命令:mkfs -t ext4 /dev/sdb1 其中 ext4 是分区类型

#### 12.4.4 虚拟机增加硬盘步骤 4

挂载: 将一个分区与一个目录联系起来，

​          •mount         设备名称 挂载目录

​          •例如： mount        /dev/sdb1        /newdisk

​             •umount 设备名称 或者    挂载目录

​            •例如： umount       /dev/sdb1 或者 umount      /newdisk

#### 12.4.5 虚拟机增加硬盘步骤 5

永久挂载: 通过修改/etc/fstab 实现挂载

添加完成后 执行 mount –a 即刻生效

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224646-875451.jpeg)

### 12.5 磁盘情况查询

#### 12.5.1 查询系统整体磁盘使用情况

基本语法

df -h 应用实例

查询系统整体磁盘使用情况

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224647-52501.jpeg)

#### 12.5.2 查询指定目录的磁盘占用情况

•基本语法 du -h /目录

查询指定目录的磁盘占用情况，默认为当前目录

-s 指定目录占用大小汇总

-h 带计量单位

-a 含文件

--max-depth=1 子目录深度

-c 列出明细的同时，增加汇总值

•应用实例

查询 /opt 目录的磁盘占用情况，深度为 1

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224649-924825.jpeg)

### 12.6 磁盘情况-工作实用指令

1)      统计/home 文件夹下文件的个数

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224649-655953.jpeg)

2)      统计/home 文件夹下目录的个数

![mg](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224651-381067.jpeg)

3)      统计/home 文件夹下文件的个数，包括子文件夹里的

![img](D:\BaiduNetdiskDownload\markdown图片\clip_image053.jpg)

4)      统计文件夹下目录的个数，包括子文件夹里的

![img](D:\BaiduNetdiskDownload\markdown图片\clip_image054.jpg)

5)      以树状显示目录结构

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224653-91469.gif) 



## 第 13 章实操篇网络配置

### 13.1 Linux 网络配置

原理图(含虚拟机) 目前我们的网络配置采用的是 NAT。

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224655-271892.jpeg)

### 13.2 查看网络 IP 和网关

#### 13.2.1 查看虚拟网络编辑器

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224656-239558.gif)

##### 13.2.2 修改 ip 地址(修改虚拟网络的 ip)

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224656-311065.gif)

##### 13.2.3 查看网关

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224658-222282.jpeg)

##### 13.2.4 查看 windows 环境的中 VMnet8 网络配置 (ipconfig 指令)

1)   使用 ipconfig 查看

2)   界面查看

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224700-157961.jpeg)

### 13.3 ping 测试主机之间网络连通

13.3.1 基本语法

ping 目的主机 （功能描述：测试当前服务器是否可以连接目的主机）

#### 13.3.2 应用实例

测试当前服务器是否可以连接百度

[root@hadoop100 桌面]# ping [www.baidu.com](http://www.baidu.com/)

#### 13.4 linux 网络环境配置

##### 13.4.1 第一种方法(自动获取)

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224701-499399.jpeg)

缺点: linux 启动后会自动获取 IP,缺点是每次自动获取的 ip 地址可能不一样。这个不适用于做服务器，因为我们的服务器的 ip 需要时固定的。

###### 13.4.2 第二种方法(指定固定的 ip)

说明

​                直接修改配置文件来指定 IP, 并可以连接到外网 ( 程序员推荐 ) ，编辑                                         vi

/etc/sysconfig/network-scripts/ifcfg-eth0

要求：将 ip 地址配置的静态的，ip 地址为 192.168.184.130




![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224702-722646.jpeg)

修改后，一定要 重启服务

1)   service network restart

2)   reboot 重启系统

![img](D:\BaiduNetdiskDownload\markdown图片\clip_image071.jpg)

## 第 14 章 实操篇进程管理

### 14.1 进程的基本介绍

1)在 LINUX 中，每个执行的程序（代码）都称为一个进程。每一个进程都分配一个 ID 号。

2)每一个进程，都会对应一个父进程，而这个父进程可以复制多个子进程。例如 www 服务器。

3)每个进程都可能以两种方式存在的。前台与后台，所谓前台进程就是用户目前的屏幕上可以进行操作的。后台进程则是实际在操作，但由于屏幕上无法看到的进程，通常使用后台方式执行。

4)一般系统的服务都是以后台进程的方式存在，而且都会常驻在系统中。直到关机才才结束。



### 14.2 显示系统执行的进程

#### 14.2.1 说明：

查看进行使用的指令是 ps ,一般来说使用的参数是 ps -aux

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224704-607372.jpeg)

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224705-964786.jpeg)



#### 14.2.2  ps 指令详解

1)指令：ps –aux|grep xxx ，比如我看看有没有 sshd 服务

2)指令说明

- System V 展示风格

- USER：用户名称

- PID：进程号

- CPU：进程占用 CPU 的百分比

- %MEM：进程占用物理内存的百分比

- VSZ：进程占用的虚拟内存大小（单位：KB）

- RSS：进程占用的物理内存大小（单位：KB）

- TTY：终端名称,缩写 .

- STAT：进程状态，其中 S-睡眠，s-表示该进程是会话的先导进程，N-表示进程拥有比普通优先级更低的优先级，R-正在运行，D-短期等待，Z-僵死进程，T-被跟踪或者被停止等等

- STARTED：进程的启动时间

- TIME：CPU 时间，即进程使用 CPU 的总时间

- COMMAND：启动进程所用的命令和参数，如果过长会被截断显示

#### 14.2.3 应用实例

**ps -aux 和ps -ef** 两者的输出结果差别不大，但展示风格不同。-aux是BSD风格，-ef是System V风格。这是次要的区别，一个影响使用的区别是aux会截断command列，而-ef不会。当结合grep时这种区别会影响到结果。 

要求：以全格式显示当前所有的进程，查看进程的父进程。

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224708-136853.jpeg)

- ps -ef 是以全格式显示当前所有的进程
  - -e 显示所有进程。-f 全格式(就是显示某个所有相关信息)。
  - ps -ef|grep xxx
- 是 BSD 风格

- UID：用户 ID

- PID：进程 ID

- PPID：父进程 ID

- C：CPU 用于计算执行优先级的因子。数值越大，表明进程是 CPU 密集型运算，执行优先级会降低；数值越小，表明进程是 I/O 密集型运算，执行优先级会提高

- STIME：进程启动的时间

- TTY：完整的终端名称

- TIME：CPU 时间

- CMD：启动进程所用的命令和参数


思考题，如果我们希望查看 sshd 进程的父进程号是多少，应该怎样查询 ？

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224709-660268.jpeg)



### 14.3 终止进程 kill 和 kill all

#### 14.3.1 介绍:

若是某个进程执行一半需要停止时，或是已消了很大的系统资源时，此时可以考虑停止该进程。

使用 kill 命令来完成此项任务。

#### 14.3.2 基本语法：

- kill  [选项] 进程号（功能描述：通过进程号杀死进程）

- kill all 进程名称（功能描述：通过进程名称杀死进程，也支持通配符，这在系统因负载过大而变得很慢时很有用）


#### 14.3.3 常用选项：

-9 :表示强迫进程立即停止

#### 14.3.4 最佳实践：

案例 1：踢掉某个非法登录用户

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224710-673555.jpeg)

案例 2: 终止远程登录服务 sshd, 在适当时候再次重启 sshd 服务

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224711-995557.jpeg)

案例 3: 终止多个 gedit 编辑器 【kill all , 通过进程名称来终止进程】

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224713-966294.jpeg)

案例 4：强制杀掉一个终端，如果输入  kill 4090 是没有反应的，因为系统认为那是比较重要的进程，所以不会删除

![img](D:\BaiduNetdiskDownload\markdown图片\clip_image082.jpg)



### 14.4 查看进程树 pstree

#### 14.4.1 基本语法：

pstree [选项] ,可以更加直观的来看进程信息

#### 14.4.2 常用选项：

- -p :显示进程的 PID
- -u :显示进程的所属用户 

#### 14.4.3 应用实例：

案例 1：请你树状的形式显示进程的 pid

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224714-464879.jpeg)

案例 2：请你树状的形式进程的用户 id ，pstree -u 即可。



### 14.5 服务(Service)管理

#### 14.5.1 介绍:

服务(service) 本质就是进程，但是是运行在后台的，通常都会监听某个端口，等待其它程序的请求，比如(mysql , sshd 防火墙等)，因此我们又称为守护进程，是 Linux 中非常重要的知识点。【原理图】

![1570638766201](D:\BaiduNetdiskDownload\markdown图片\1570638766201.png)



#### 14.5.2     service 管理指令：

service 服务名 [start | stop | restart | reload | status] **在 CentOS7.0 后 不再使用 service ,而是 systemctl**

#### 14.5.3 使用案例：

1) 查看当前防火墙的状况，关闭防火墙和重启防火墙。

![1570638602375](D:\BaiduNetdiskDownload\markdown图片\1570638602375.png)

#### 14.5.4 细节讨论：

1) 关闭或者启用防火墙后，立即生效。[telnet 测试 某个端口即可（**telnet 可以检测linux的某个端口是否在监听，并且可以访问**）]

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224718-26668.jpeg)

2)这种方式只是临时生效，当重启系统后，还是回归以前对服务的设置。

如果希望设置某个服务自启动或关闭永久生效，要使用 chkconfig 指令，马上讲



#### 14.5.5 查看服务名:

方式 1：使用 setup -> 系统服务 就可以看到。

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224717-550287.png)

方式 2:     /etc/init.d/服务名称

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224721-547135.jpeg)



#### 14.5.6 服务的运行级别(runlevel):

查看或者修改默认级别： vi /etc/inittab

Linux 系统有 7 种运行级别(runlevel)：常用的是级别 3 和 5

•运行级别 0：系统停机状态，系统默认运行级别不能设为 0，否则不能正常启动

•运行级别 1：单用户工作状态，root 权限，用于系统维护，禁止远程登陆

•运行级别 2：多用户状态(没有 NFS)，不支持网络

•运行级别 3：完全的多用户状态(有 NFS)，登陆后进入控制台命令行模式

•运行级别 4：系统未使用，保留

•运行级别 5：X11 控制台，登陆后进入图形 GUI 模式

•运行级别 6：系统正常关闭并重启，默认运行级别不能设为 6，否则不能正常启动



#### 14.5.7 开机的流程说明

开机的时候会去/etc/inittab 文件中读取运行级别

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224723-951204.jpeg)

#### 14.5.8       chkconfig 指令

介绍通过 chkconfig 命令可以给每个服务的各个运行级别设置自启动/关闭基本语法

#####           1) 查看服务 chkconfig --list|grep    xxx

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224724-188596.jpeg)

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224725-559543.jpeg)

##### 2) chkconfig 服务名 --list

![img](D:\BaiduNetdiskDownload\markdown图片\clip_image096.jpg)

#####          3)    chkconfig --level  5 服务名 on/off

请将 sshd 服务在运行级别为 5 的情况下，不要自启动。

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224726-601721.jpeg)

#### 14.5.9 应用实例：

1)   案例 1： 请显示当前系统所有服务的各个运行级别的运行状态 bash> chkconfig --list

2)   案例 2 ：请查看 sshd 服务的运行状态	bash> service sshd status

3)   案例 3： 将 sshd 服务在运行级别 5 下设置为不自动启动，看看有什么效果？ bash> chkconfig --level 5 sshd off

4)   案例 4： 当运行级别为 5 时，关闭防火墙。 bash> chkconfig    --level 5 iptables off

5)   案例 5： 在所有运行级别下，关闭防火墙 bash> chkconfig iptables off

6)   案例 6： 在所有运行级别下，开启防火墙 bash> chkconfig iptables on

#### 14.5.10 使用细节

1) chkconfig 重新设置服务后自启动或关闭，需要重启机器 reboot 才能生效.



### 14.6 动态监控进程

#### 14.6.1 介绍：

top 与 ps 命令很相似。它们都用来显示正在执行的进程。Top 与 ps 最大的不同之处，在于 top 在执行一段时间可以更新正在运行的的进程。

#### 14.6.2 基本语法： top [选项]

#### 14.6.3 选项说明：

![1570668911491](D:\BaiduNetdiskDownload\markdown图片\1570668911491.png)

#### 14.6.4 应用实例：

案例 1.监视特定用户

​	top：输入此命令，按回车键，查看执行的进程。

​	u：然后输入“u”回车，再输入用户名，即可

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224730-624572.jpeg)

案例 2：终止指定的进程。

​	top：输入此命令，按回车键，查看执行的进程。

​	k：然后输入“k”回车，再输入要结束的进程 ID 号

![img](D:\BaiduNetdiskDownload\markdown图片\clip_image103.jpg)

案例 3:指定系统状态更新的时间(每隔 10 秒自动更新， 默认是 3 秒)： bash> top -d 10

#### 14.6.5 查看系统网络情况 netstat(重要)

- 基本语法
  - netstat [选项] netstat -anp
- 选项说明
  -  -an 按一定顺序排列输出
  - -p 显示哪个进程在调用
- •应用案例
  - 查看系统所有的网络服务
    - ![1570757282928](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224732-19639.png)
  - 请查看服务名为 sshd 的服务的信息。
    - ![1570757372344](D:\BaiduNetdiskDownload\markdown图片\1570757372344.png)

# 第 15 章实操篇 RPM 和 YUM

## 15.1 rpm 包的管理

### 15.1.1 介绍：

一种用于互联网下载包的打包及安装工具，它包含在某些 Linux 分发版中。它生成具有.RPM扩展名的文件。RPM 是 RedHat Package Manager（RedHat 软件包管理工具）的缩写，类似 windows的 setup.exe，这一文件格式名称虽然打上了 RedHat 的标志，但理念是通用的。Linux 的分发版本都有采用（suse,redhat, centos 等等），可以算是公认的行业标准了。

### 15.1.2       rpm 包的简单查询指令：

查询已安装的 rpm 列表 rpm –qa|grep xx 请查询看一下，当前的 Linux 有没有安装 firefox .

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224733-855193.jpeg)



### 15.1.3       rpm 包名基本格式：

一个 rpm 包名：firefox-45.0.1-1.el6.centos.x86_64.rpm 名称:firefox 版本号：45.0.1-1

适用操作系统: el6.centos.x86_64

x86_64 表示 centos6.x 的 64 位系统，如果是 i686、i386 表示 32 位系统，noarch 表示通用。



### 15.1.4       rpm 包的其它查询指令：

![1570584576402](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224734-602277.png)

- rpm -qa :查询所安装的所有 rpm 软件包
- rpm -qa | more [分页显示]
- rpm -qa | grep X [rpm -qa | grep firefox ]
  - ![img](D:\BaiduNetdiskDownload\markdown图片\clip_image108.jpg)
- rpm -q 软件包名 :查询软件包是否安装，这个跟rpm -qa | grep 差不多  
  -  rpm -q firefox
- rpm -qi 软件包名 ：查询软件包信息
  - ![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224748-675502.jpeg)
  - rpm -qi firefox
- rpm -ql 软件包名 :查询软件包中的文件,可以查询软件安装到哪里去了
  - rpm -ql firefox
  - ![img](D:\BaiduNetdiskDownload\markdown图片\clip_image111.jpg)
- rpm -qf 文件全路径名 查询文件所属的软件包
  - rpm -qf /etc/passwd
  - rpm -qf /root/install.log
  - ![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224749-52514.jpeg)

### 15.1.5 卸载 rpm 包：

•基本语法 rpm -e RPM 包的名称 •应用案例

1) 删除 firefox 软件包

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224750-622827.jpeg)

•细节问题

1)    如果其它软件包依赖于您要卸载的软件包，卸载时则会产生错误信息。如： $ rpm -e fooremoving these packages would break dependencies:foo is needed by bar-1.0-1

2)    如果我们就是要删除 foo 这个 rpm 包，可以增加参数 --nodeps ,就可以强制删除，但是一般不推荐这样做，因为依赖于该软件包的程序可能无法运行

如：$ rpm -e --nodeps foo 带上 --nodeps 就是强制删除。



### 15.1.6 安装 rpm 包：

•基本语法 rpm -ivh RPM 包全路径名称

•参数说明

- i=install 安装
- v=verbose 提示 
- h=hash 进度条

•应用实例

1) 演示安装 firefox 浏览器步骤先找到 firefox 的安装 rpm 包,你需要挂载上我们安装 centos 的 iso 文件，然后到/media/下去，这里面有很多的安装包

找 rpm 找。 cp  firefox-45.0.1-1.el6.centos.x86_64.rpm   /opt/

![img](D:\BaiduNetdiskDownload\markdown图片\clip_image115.jpg)



## 15.2 yum

### 15.2.1 介绍：

Yum 是一个 [Shell] 前端软件包管理器。基于 [RPM]包管理，能够从指定的服务器自动下载 RPM 包并且安装，可以自动处理依赖性关系，并且一次安装所有依赖的软件包。使用 yum 的前提是可以联网。

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224753-46172.jpeg)

### 15.2.2       yum 的基本指令

- •查询 yum 服务器是否有需要安装的软件 

  - yum list|grep xx 软件列表

- •安装指定的 yum 包

  - yum install xxx 下载安装

    

### 15.2.3   yum 应用实例：

案例：请使用 yum 的方式来安装 firefox

1)   先查看一下 firefox    rpm 在 yum 服务器有没有

![img](D:\BaiduNetdiskDownload\markdown图片\clip_image118.jpg)

2)   安装

yum install firefox会安装最新版本的软件。

成功！

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224754-20195.jpeg) 

# 第 16 章 JavaEE 定制篇搭建 JavaEE 环境

## 16.1 概述

### 16.1.1 示意图：

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224755-202947.jpeg)

如果需要在 Linux 下进行 JavaEE 的开发，我们需要安装如下软件

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224756-92806.jpeg)

## 16.2 安装 JDK

### 16.2.1 看老师演示，一会整理笔记：

#### 16.2.2 安装步骤

0)       先将软件通过 xftp5 上传到 /opt 下

1)       解压缩到 /opt

2)       配置环境变量的配置文件 vim    /etc/profile

![img](file:///C:/Users/古春波/AppData/Local/Packages/oice_16_974fa576_32c1d314_327f/AC/Temp/msohtmlclip1/01/clip_image124.jpg)

JAVA_HOME=/opt/jdk1.7.0_79 PATH=/opt/jdk1.7.0_79/bin:$PATH export JAVA_HOME PATH

3)       需要注销用户，环境变量才能生效。如果是在 3 运行级别， logout

如果是在 5 运行级别，

4)       在任何目录下就可以使用 java 和 javac

![img](D:\BaiduNetdiskDownload\markdown图片\clip_image125.jpg)

### 16.2.3 测试是否安装成功

编写一个简单的 Hello.java 输出"hello,world!"

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224758-452970.jpeg)

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224757-964181.jpeg)



## 16.3 安装 tomcat

16.3.1 步骤 :

1)   解压缩到/opt

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224801-535544.jpeg)

2)   启动 tomcat  ./startup.sh

先进入到 tomcat 的 bin 目录

![img](file:///C:/Users/古春波/AppData/Local/Packages/oice_16_974fa576_32c1d314_327f/AC/Temp/msohtmlclip1/01/clip_image132.jpg)

使用 Linux 本地的浏览是可以访问到 tomcat

3)   开放端口 8080 ,这样外网才能访问到 tomcat vim /etc/sysconfig/iptables

![\img](D:\BaiduNetdiskDownload\markdown图片\clip_image134.jpg)

重启防火墙

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224802-878175.jpeg)

### 16.3.2 测试是否安装成功：

在 windows、Linux 下 访问 [http://linuxip:8080](http://linuxip:8080/)

![img](D:\BaiduNetdiskDownload\markdown图片\clip_image138.jpg)

## 16.4 Eclipse 的安装

16.4.1 步骤 :

1)   解压缩到/opt

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224803-58725.jpeg)

2)   启动 eclipse，配置 jre 和 server

启动方法 1: 创建一个快捷方式

启动方式 2: 进入到 eclipse 解压后的文件夹，然后执行 ./eclipse 即可 3) 编写 jsp 页面,并测试成功!

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224803-827949.jpeg)

## 16.5 mysql 的安装和配置

### 16.5.1 安装的步骤和文档

[说明: 因为 mysql 安装时间很长，所以在授课时，可以考虑最先安装 mysql] 相关的安装软件在课件

注意: 先删除一下 Mysql 相关的软件..

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224807-615788.gif)

尚硅谷centos6.8下安装mysql.docx.zip

16.5.2 说明

请同学们一定要按照老师的文档一步一步的安装成功。

## 第 17 章大数据定制篇 Shell 编程

### 17.1 为什么要学习 Shell 编程

1)Linux 运维工程师在进行服务器集群管理时，需要编写 Shell 程序来进行服务器管理。

2)对于 JavaEE 和 Python 程序员来说，工作的需要，你的老大会要求你编写一些 Shell 脚本进行程序或者是服务器的维护，比如编写一个定时备份数据库的脚本。 3)对于大数据程序员来说，需要编写 Shell 程序来管理集群。

### 17.2 Shell 是什么

画一个示意图：

![img](D:\BaiduNetdiskDownload\markdown图片\clip_image144.jpg)

Shell 是一个命令行解释器，它为用户提供了一个向 Linux 内核发送请求以便运行程序的界面系统级程序，用户可以用 Shell 来启动、挂起、停止甚至是编写一些程序.

### 17.3 shell 编程快速入门-Shell 脚本的执行方式

#### 17.3.1 脚本格式要求

1)   脚本以#!/bin/bash 开头

2)   脚本需要有可执行权限

#### 17.3.2 编写第一个 Shell 脚本

•需求说明

创建一个 Shell 脚本，输出 hello world!

看案例：

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224811-722405.jpeg)

#### 17.3.3 脚本的常用执行方式

•方式 1(输入脚本的绝对路径或相对路径)

1)首先要赋予 helloworld.sh 脚本的+x 权限

2)执行脚本

![img](D:\BaiduNetdiskDownload\markdown图片\clip_image146.jpg)

•方式 2(sh+脚本)，不推荐

说明：不用赋予脚本+x 权限，直接执行即可

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224812-238790.jpeg)

### 17.4 shell 的变量

#### 17.4.1       Shell 的变量的介绍

1）Linux Shell 中的变量分为，系统变量和用户自定义变量。

2）系统变量：$HOME、$PWD、$SHELL、$USER 等等比如： echo $HOME 等等..

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224814-121536.jpeg)

3）显示当前 shell 中所有变量：set

#### 17.4.2       shell 变量的定义

•基本语法

1)定义变量：变量=值

2)撤销变量：unset 变量

3) 声明静态变量：readonly 变量，注意：不能 unset

•快速入门

案例 1：定义变量 A 案例 2：撤销变量 A

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224816-946802.jpeg)

案例 3：声明静态的变量 B=2，不能 unset

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224817-455929.jpeg)

案例 4：可把变量提升为全局环境变量，可供其他 shell 程序使用【一会举例。】

#### 17.4.3 •定义变量的规则

1)   变量名称可以由字母、数字和下划线组成，但是不能以数字开头。

2)   等号两侧不能有空格

3)   变量名称一般习惯为大写

#### 17.4.4 •将命令的返回值赋给变量（重点）

1）A=`ls -la` 反引号，运行里面的命令，并把结果返回给变量 A

2）A=$(ls -la) 等价于反引号

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224818-278283.jpeg)

### 17.5 设置环境变量

#### 17.5.1 基本语法

1)  export 变量名=变量值 （功能描述：将 shell 变量输出为环境变量）

2)  source 配置文件 （功能描述：让修改后的配置信息立即生效）

3)  echo $变量名 （功能描述：查询环境变量的值）

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224819-770150.jpeg)

#### 17.5.2 快速入门

1)   在/etc/profile 文件中定义 TOMCAT_HOME 环境变量

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224820-427288.jpeg)

2)   查看环境变量 TOMCAT_HOME 的值

​         echo    $TOMCAT_HOME

3)   在另外一个 shell 程序中使用 TOMCAT_HOME

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224821-54208.jpeg)

注意：在输出 TOMCAT_HOME 环境变量前，需要让其生效

source /etc/profile

### 17.6 位置参数变量

#### 17.6.1 介绍

当我们执行一个 shell 脚本时，如果希望获取到命令行的参数信息，就可以使用到位置参数变量，比如 ： ./myshell.sh 100 200 , 这个就是一个执行 shell 的命令行，可以在 myshell 脚本中获取到参数信息

#### 17.6.2 基本语法

$n （功能描述：n 为数字，$0 代表命令本身，$1-$9 代表第一到第九个参数，十以上的参数，十以上的参数需要用大括号包含，如${10}）

$* （功能描述：这个变量代表命令行中所有的参数，$*把所有的参数看成一个整体）

$@（功能描述：这个变量也代表命令行中所有的参数，不过$@把每个参数区分对待）

$#（功能描述：这个变量代表命令行中所有参数的个数）

#### 17.6.3 位置参数变量应用实例

案例：编写一个 shell 脚本 positionPara.sh ， 在脚本中获取到命令行的各个参数信息

![img](D:\BaiduNetdiskDownload\markdown图片\clip_image156.jpg)

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224823-727706.jpeg)

### 17.7 预定义变量

17.7.1 基本介绍

就是 shell 设计者事先已经定义好的变量，可以直接在 shell 脚本中使用

#### 17.7.2 基本语法

$$ （功能描述：当前进程的进程号（PID））

$! （功能描述：后台运行的最后一个进程的进程号（PID））

$？（功能描述：最后一次执行的命令的返回状态。如果这个变量的值为 0，证明上一个命令正确执行；如果这个变量的值为非 0（具体是哪个数，由命令自己来决定），则证明上一个命令执行不正确了。）

#### 17.7.3 应用实例

在一个 shell 脚本中简单使用一下预定义变量

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224824-420437.jpeg)

#### 17.8 运算符

17.8.1 基本介绍

学习如何在 shell 中进行各种运算操作。

##### 17.8.2 基本语法

1)   “$((运算式))”或“$[运算式]”

2)   expr m + n

注意 expr 运算符间要有空格

3)   expr m - n

4)   expr  \*, /, %  乘，除，取余

•应用实例案例 1：计算（2+3）X4 的值

1)   $((运算式))

![img](D:\BaiduNetdiskDownload\markdown图片\clip_image159.jpg)

2)   $[运算式]

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224826-17426.jpeg)

3)   expr

![img](D:\BaiduNetdiskDownload\markdown图片\clip_image161.jpg)

案例 2：请求出命令行的两个参数[整数]的和

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224826-641144.jpeg)

17.9 条件判断判断语句

##### 17.9.1 •基本语法

[ condition ]（注意 condition 前后要有空格）

\#非空返回 true，可使用$?验证（0 为 true，>1 为 false）

##### 17.9.2 •应用实例

​                       [ atguigu ]                     返回 true

​                    []                       返回 false

​                    [condition] && echo OK || echo notok      条件满足，执行后面的语句

##### 17.9.3 •常用判断条件

1)两个整数的比较

= 字符串比较

-lt 小于

-le 小于等于

-eq 等于

-gt 大于

-ge 大于等于

-ne 不等于

2) 按照文件权限进行判断

-r 有读的权限 [ -r 文件 ]

-w    有写的权限

-x     有执行的权限

3)按照文件类型进行判断

-f 文件存在并且是一个常规的文件

-e 文件存在

-d 文件存在并是一个目录

##### 17.9.4 应用实例

案例 1："ok"是否等于"ok" 判断语句：

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224830-504625.jpeg)

案例 2：23 是否大于等于 22 判断语句：

![img](D:\BaiduNetdiskDownload\markdown图片\clip_image165.jpg)

案例 3：/root/install.log 目录中的文件是否存在

判断语句：

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224831-578900.jpeg)

### 17.10流程控制

#### 17.10.1 if 判断

•基本语法

if [ 条件判断式 ];then

程序 fi

或者 if [ 条件判断式 ] then

程序

elif [条件判断式]

then

程序 fi

注意事项：（1）[ 条件判断式 ]，中括号和条件判断式之间必须有空格 (2) 推荐使用第二种方式

•应用实例

案例：请编写一个 shell 程序，如果输入的参数，大于等于 60，则输出 "及格了"，如果小于 60, 则输出 "不及格"

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224833-81205.jpeg)

#### 17.10.2 case 语句

•基本语法

case $变量名 in

"值 1"）

如果变量的值等于值 1，则执行程序 1

;;

"值 2"）

如果变量的值等于值 2，则执行程序 2

;;

…省略其他分支…

*）

如果变量的值都不是以上的值，则执行此程序

;; esac

•应用实例

案例 1 ：当命令行参数是 1 时，输出 "周一", 是 2 时，就输出"周二"， 其它情况输出 "other"

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224834-685505.jpeg)

#### 17.10.3 for 循环

•基本语法 1 **for** 变量 **in** 值 **1** 值 **2** 值 **3**… **do**

程序 **done**

•应用实例

案例 1 ：打印命令行输入的参数 【会使用到$* $@】

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224835-248754.jpeg)

•基本语法 2 **for ((** 初始值**;**循环控制条件**;**变量变化 **)) do**

程序 **done**

•应用实例

案例 1 ：从 1 加到 100 的值输出显示

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224836-116056.jpeg)

#### 17.10.4 while 循环

•基本语法 1 while [ 条件判断式 ] do

程序 done

•应用实例案例 1 ：从命令行输入一个数 n，统计从 1+..+ n 的值是多少？

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224837-699458.jpeg)

### 17.11read 读取控制台输入

#### 17.11.1 基本语法

read(选项)(参数)

选项：

-p：指定读取值时的提示符；

-t：指定读取值时等待的时间（秒），如果没有在指定的时间内输入，就不再等待了。。

参数

变量：指定读取值的变量名

#### 17.11.2 应用实例

案例 1：读取控制台输入一个 num 值

案例 2：读取控制台输入一个 num 值，在 10 秒内输入。

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224839-74791.jpeg)

### 17.12函数

#### 17.12.1 函数介绍

shell 编程和其它编程语言一样，有系统函数，也可以自定义函数。系统函数中，我们这里就介绍两个。

#### 17.12.2 系统函数

•basename 基本语法

功能：返回完整路径最后 / 的部分，常用于获取文件名 basename [pathname] [suffix]

​         basename [string] [suffix] （功能描述：basename 命令会删掉所有的前缀包括最后一个（‘/’）

字符，然后将字符串显示出来。

选项： suffix 为后缀，如果 suffix 被指定了，basename 会将 pathname 或 string 中的 suffix 去掉。

•dirname 基本语法

功能：返回完整路径最后 / 的前面的部分，常用于返回路径部分 dirname 文件绝对路径 （功能描述：从给定的包含绝对路径的文件名中去除文件名（非目录的部分），然后返回剩下的路径（目录的部分））

#### 17.12.3 •应用实例

案例 1：请返回 /home/aaa/test.txt 的 "test.txt" 部分

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224840-495021.jpeg)

案例 2：请返回 /home/aaa/test.txt 的 /home/aaa

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224840-547741.jpeg)

#### 17.12.4 自定义函数

•基本语法

[ function ] funname[()]

{

Action;

[return int;]

}

​         调用直接写函数名：funname    [值]

•应用实例

案例 1：计算输入两个参数的和（read）， getSum

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224843-925718.jpeg)

### 17.13Shell 编程综合案例

需求分析

1)每天凌晨 2:10 备份 数据库 atguiguDB 到 /data/backup/db

2)备份开始和备份结束能够给出相应的提示信息

3)备份后的文件要求以备份时间为文件名，并打包成 .tar.gz 的形式，比如：

2018-03-12_230201.tar.gz

4) 在备份的同时，检查是否有 10 天前备份的数据库文件，如果有就将其删除。编写一个 shell 脚本。

思路分析：

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224845-183024.jpeg)

代码实现：

![img](file:///C:/Users/古春波/AppData/Local/Packages/oice_16_974fa576_32c1d314_327f/AC/Temp/msohtmlclip1/01/clip_image183.jpg)

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224846-335475.jpeg) 

# 第 18 章 Python 定制篇开发平台 Ubuntu

## 18.1 Ubuntu 的介绍

Ubuntu（友帮拓、优般图、乌班图）是一个以[桌面应](https://baike.baidu.com/item/桌面)用为主的开源 GNU/Linux 操作系统，Ubuntu

是基于 GNU/[Linux](https://baike.baidu.com/item/Linux)，支持 x86、amd64（即 x64）和 [ppc](https://baike.baidu.com/item/ppc/150) [架](https://baike.baidu.com/item/ppc/150)构，由全球化的专业开发团队（Canonical Ltd）打造的。

专业的 Python 开发者一般会选择 Ubuntu 这款 Linux 系统作为生产平台.

温馨提示：

Ubuntu 和 Centos 都是基于 GNU/Linux 内核的，因此基本使用和 Centos 是几乎一样的，它们的各种指令可以通用，同学们在学习和使用 Ubuntu 的过程中，会发现各种操作指令在前面学习 CentOS 都使用过。只是界面和预安装的软件有所差别。

Ubuntu 下载地址：[http://cn.ubuntu.com/download](http://cn.ubuntu.com/download/)[/](http://cn.ubuntu.com/download/)

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224856-859259.jpeg)

## 18.2 Ubuntu 的安装

### 18.2.1 安装的步骤

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224857-500560.gif)

尚硅谷Python安装Ubuntu.zip

### 18.2.2 设置 Ubuntu 支持中文

默认安装的 ubuntu 中只有英文语言，因此是不能显示汉字的。要正确显示汉字，需要安装中文语言包。

安装中文支持步骤：

1.单击左侧图标栏打开 System Settings（系统设置）菜单，点击打开 Language Support（语言支持）选项卡。

2.点击 Install / Remove Languages，在弹出的选项卡中下拉找到 Chinese(Simplified)，即中文简体，在后面的选项框中打勾。然后点击 Apply Changes 提交，系统会自动联网下载中文语言包。（保证 ubuntu 是联网的）。

3.这时“汉语（中国）”在最后一位因为当前第一位是”English”，所以默认显示都是英文。我们如果希望默认显示用中文，则应该将“汉语（中国）”设置为第一位。设置方法是拖动，鼠标单击

“汉语（中国）”，当底色变化（表示选中了）后，按住鼠标左键不松手，向上拖动放置到第一位。

4.设置后不会即刻生效，需要下一次登录时才会生效。

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224858-812064.jpeg)

## 18.3 Ubuntu 的 roo 用户

### 18.3.1 介绍

安装 ubuntu 成功后，都是普通用户权限，并没有最高 root 权限，如果需要使用 root 权限的时候，通常都会在命令前面加上 sudo 。有的时候感觉很麻烦。

我们一般使用 su 命令来直接切换到 root 用户的，但是如果没有给 root 设置初始密码，就会抛出 su :

Authentication failure 这样的问题。所以，我们只要给 root 用户设置一个初始密码就好了。

### 18.3.2 给 root 用户设置密码并使用

1)    输入 sudo passwd 命令，输入一般用户密码并设定 root 用户密码。

2)    设定 root 密码成功后，输入 su 命令，并输入刚才设定的 root 密码，就可以切换成 root 了。

提示符$代表一般用户，提示符#代表 root 用户。

3)    输入 exit 命令，退出 root 并返回一般用户

4)    以后就可以使用 root 用户了

![img](D:\BaiduNetdiskDownload\markdown图片\clip_image189.jpg)

## 18.4 Ubuntu 下开发 Python

### 18.4.1 说明

安装好 Ubuntu 后，默认就已经安装好 Python 的开发环境[Python2.7 和 Python3.5]。

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224900-440034.jpeg)

### 18.4.2 在 Ubuntu 下开发一个 Python 程序

1)      vim hello.py [编写 hello.py]

提示：如果 Ubuntu 没有 vim 我们可以根据提示信息安装一个 vim

apt install vim

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224901-328255.jpeg)

2)      python3 hello.py [运行 hello.py]

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224902-408498.jpeg)

## 第 19 章 Python 定制篇 apt 软件管理和远程登录

### 19.1 apt 介绍

apt 是 Advanced Packaging Tool 的简称，是一款安装包管理工具。在 Ubuntu 下，我们可以使用 apt

命令可用于软件包的安装、删除、清理等，类似于 Windows 中的软件管理工具。

unbuntu 软件管理的原理示意图：

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224903-126923.jpeg)

### 19.2 Ubuntu 软件操作的相关命令

sudo apt-get update 更新源 sudo apt-get install package 安装包 sudo apt-get remove package 删除包

sudo apt-cache search package 搜索软件包

sudo apt-cache show package 获取包的相关信息，如说明、大小、版本等

​         sudo apt-get install package --reinstall      重新安装包

sudo apt-get -f install 修复安装 sudo apt-get remove package --purge 删除包，包括配置文件等 sudo apt-get build-dep package 安装相关的编译环境

sudo apt-get upgrade 更新已安装的包 sudo apt-get dist-upgrade 升级系统 sudo apt-cache depends package 了解使用该包依赖那些包 sudo apt-cache rdepends package 查看该包被哪些包依赖 sudo apt-get source package 下载该包的源代码

### 19.3 更新 Ubuntu 软件下载地址

#### 19.3.1 原理示意图

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224904-874555.jpeg)

#### 19.3.2 寻找国内镜像源

https://mirrors.tuna.tsinghua.edu.cn/

所谓的镜像源：可以理解为提供下载

软件的地方，比如 Android 手机上可以下载软件的安卓市场；iOS 手机上可

以下载软件的 AppStore

![img](file:///C:/Users/古春波/AppData/Local/Packages/oice_16_974fa576_32c1d314_327f/AC/Temp/msohtmlclip1/01/clip_image198.gif)

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224905-186042.jpeg)

##### 19.3.3 备份 Ubuntu 默认的源地址

sudo cp /etc/apt/sources.list /etc/apt/sources.list.backup

![img](D:\BaiduNetdiskDownload\markdown图片\clip_image202.jpg)

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224907-589125.jpeg)

#### 19.3.4 更新源服务器列表

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224908-329839.jpeg)

### 19.4 Ubuntu 软件安装，卸载的最佳实践

#### 19.4.1 案例说明：使用 apt 完成安装和卸载 vim 软件，并查询 vim 软件的信息：

sudo apt-get remove vim

![img](D:\BaiduNetdiskDownload\markdown图片\clip_image206.jpg)

sudo apt-get install vim

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224911-43991.jpeg)

sudo apt-cache show vim

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224912-512711.jpeg)

#### 19.5 使用 ssh 远程登录 Ubuntu

##### 19.5.1       ssh 介绍

SSH 为 Secure Shell 的缩写，由 IETF 的网络工作小组（Network Working Group）所制定；SSH 为建立在应用层和传输层基础上的安全协议。

SSH 是目前较可靠，专为远程登录会话和其他网络服务提供安全性的协议。常用于远程登录，以

及用户之间进行资料拷贝。几乎所有 UNIX 平台—包括 HP-UX、Linux、AIX、Solaris、Digital UNIX、

Irix，以及其他平台，都可运行 SSH。

使用 SSH 服务，需要安装相应的服务器和客户端。客户端和服务器的关系：如果，A 机器想被 B 机器远程控制，那么，A 机器需要安装 SSH 服务器，B 机器需要安装 SSH 客户端。

和 CentOS 不一样，Ubuntu 默认没有安装 SSHD 服务，因此，我们不能进行远程登录。

##### 19.5.2 原理示意图：

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224913-428256.jpeg)

#### 19.6 使用 ssh 远程登录 Ubuntu

##### 19.6.1 安装 SSH 和启用

sudo apt-get install openssh-server

执行上面指令后，在当前这台 Linux 上就安装了 SSH 服务端和客户端。 service sshd restart

执行上面的指令，就启动了 sshd 服务。会监听端口 22

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224915-295214.jpeg)

##### 19.6.2 在 Windows 使用 XShell5/XFTP5 登录 Ubuntu

前面我们已经安装了 XShell5，直接使用即可。注意：使用 atguigu 用户登录，需要的时候再 su - 切换成 root 用户

![img](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323224915-355744.jpeg)

##### 19.6.3 从 linux 系统客户机远程登陆 linux 系统服务机

首先，我们需要在 linux 的系统客户机也要安装 openssh-server

•基本语法：

ssh 用户名@IP

例如：ssh atguigu@192.168.188.131

使用 ssh 访问，如访问出现错误。可查看是否有该文件 ～/.ssh/known_ssh 尝试删除该文件解决。

•登出

登出命令：exit 或者 logout

