## mall在Linux环境下的部署（基于Docker容器）

适用的系统：CentOS7.6 x64

### 1.基础-工具安装

#### 1.1、wget、vim

```bash
yum –y install wget
yum –y install vim*
```

#### 1.2更新yun源为阿里云

##### 1.备份

```bash
mv /etc/yum.repos.d/CentOS-Base.repo /etc/yum.repos.d/CentOS-Base.repo.backup 
```

##### 2、下载新的CentOS-Base.repo 到/etc/yum.repos.d/ 

```bash
wget -O /etc/yum.repos.d/CentOS-Base.repo http://mirrors.aliyun.com/repo/Centos-7.repo
```

#### 1.3关闭防火墙

1.查看防火墙状态

```bash
systemctl status firewalld 
```

2.停止防火墙

```bash
systemctl stop firewalld
```

3.禁止开机启动

```bash
systemctl disable firewalld
```

### 2.Docker安装

#### 1、安装docker

检查内核版本，返回的值大于3.10即可。

```bash
uname -r //查看内核版本
```

如果内核版本低，需要更新系统

```bash
yum update  //更新系统
```

1、Install required packages. `yum-utils` provides the `yum-config-manager` utility, and `device-mapper-persistent-data` and `lvm2` are required by the `devicemapper` storage driver.

```bash
yum install -y yum-utils \
device-mapper-persistent-data \
lvm2
```

2、Use the following command to set up the **stable** repository.

```bash
yum-config-manager \
    --add-repo \
https://download.docker.com/linux/centos/docker-ce.repo	
```

3、查看版本

```bash
yum list docker-ce --showduplicates | sort -r
```

#### 2、安装 DOCKER CE

```bash
yum -y install docker-ce-18.03.1.ce-1.el7.centos
systemctl start docker(启动docker)
systemctl enable docker(设为开机启动)
docker run hello-world
```

#### 3、docker镜像加速

阿里云，容器镜像服务

针对Docker客户端版本大于 1.10.0 的用户

您可以通过修改daemon配置文件/etc/docker/daemon.json来使用加速器

```bash
sudo mkdir -p /etc/docker
```

```bash
sudo tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": ["https://82m9ar63.mirror.aliyuncs.com"]
}
EOF
```

```bash
sudo systemctl daemon-reload
```

```bash
sudo systemctl restart docker
```

### 3、MySQL安装

#### 1、下载镜像文件

```bash
docker pull mysql:5.7
```

#### 2、创建实例并启动

```bash
docker run -p 3306:3306 --name mysql \
-v /mydata/mysql/log:/var/log/mysql \
-v /mydata/mysql/data:/var/lib/mysql \
-v /mydata/mysql/conf:/etc/mysql \
-e MYSQL_ROOT_PASSWORD=root \
-d mysql:5.7 
```

参数说明：

-p 3306:3306：将容器的3306端口映射到主机的3306端口

-v /mydata/mysql/conf:/etc/mysql：将配置文件夹挂在到主机

-v /mydata/mysql/log:/var/log/mysql：将日志文件夹挂载到主机

-v /mydata/mysql/data:/var/lib/mysql/：将配置文件夹挂载到主机

-e MYSQL_ROOT_PASSWORD=root：初始化root用户的密码

#### 3、通过mysql配置文件配置mysql

```bash
vim /mydata/mysql/conf/my.cnf
```

将下面的数据写入my.cnf当中，然后保存退出

```
[client]
default-character-set=utf8
 
[mysql]
default-character-set=utf8
 
[mysqld]
init_connect='SET collation_connection = utf8_unicode_ci'
init_connect='SET NAMES utf8'
character-set-server=utf8
collation-server=utf8_unicode_ci
skip-character-set-client-handshake

```



#### 4、通过容器的mysql命令行工具连接

```bash
docker exec -it mysql mysql -uroot -proot
```

#### 5、设置root远程访问

```bash
grant all privileges on *.* to 'root'@'%' identified by 'root' with grant option;
```

```bash
flush privileges;
```

### 4、Redis安装

#### 1、下载镜像文件

```bash
docker pull redis:3.2
```

#### 2、创建实例并启动

```bash
docker run -p 6379:6379 --name redis -v /mydata/redis/data:/data -d redis:3.2 redis-server --appendonly yes
```

#### 3、使用redis镜像执行redis-cli命令连接

```bash
docker exec -it redis redis-cli
```

### 5、Rabbitmq安装

 

#### 1、下载镜像文件

```bash
docker pull rabbitmq:management
```

 

#### 2、创建实例并启动

```bash
 docker run -d --name rabbitmq --publish 5671:5671 \

--publish 5672:5672 --publish 4369:4369 --publish 25672:25672 --publish 15671:15671 --publish 15672:15672 \

rabbitmq:management
```

注：

4369 -- erlang发现口

5672 --client端通信口 

15672 -- 管理界面ui端口

25672 -- server间内部通信口

#### 3、测试

在web浏览器中输入地址：http://虚拟机ip:15672/

输入默认账号: guest  密码: guest

### 6、Docker容器开机自启

docker update --restart=always xxx

#### 1.docker容器自启动mysql

```bash
docker update --restart=always mysql
```

#### 2.docker容器自启动rabbitmq

```bash
docker update --restart=always rabbitmq
```

#### 3.docker容器自启动 redis

```bash
docker update --restart=always redis
```



