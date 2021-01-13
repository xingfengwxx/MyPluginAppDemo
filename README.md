# Android插件化开发

#### 插件化核心思想

- **模块化**：将项目中可以共享的部分或业务抽取出来形成单独的Module，以便于每个模块只包含与其功能相关的内容。典型的应用，各种第三方的library。(始终是基于单一工程的)

- **组件化**：组件化是建立在模块化思想上的一次演进，组件化和模块化本质相同，只是注重点不同。模块化注重功能复用，而组件化更注重解耦。从使用上区分，组件化的moudule在打包时是library，在调试时可以是application。(多工程) (编译时)

- **插件化**：严格意义上讲，也是一种模块化的概念。它将一个完整的工程，按业务划分为不同的插件，化整为零，相互配合。插件化的单位是Apk，可以实现Apk动态加载，动态更新，比组件化更灵活。(运行时)

插件化开发的核心：动态加载功能模块。

实现插件化的思路：

1. 手动加载dex中的class文件。怎么加载？
2. 手动加载apk中的资源。怎么加载？
3. 加载插件包中的四大组件。怎么加载？会存在什么问题？

#### DexClassLoader

Android提供独有的类加载器：**PathClassLoader** & **DexClassLoader**。

首先要知道类加载器是什么？

JVM的运行期数据区模型：
Java堆，方法区，程序计数器，虚拟机栈，本地方法栈

字节码执行引擎将class字节码数据加载到JVM运行期数据区的虚拟机栈中 。

动态编译技术，JIT。

**官方说明了DexClassLoader的定义**：

用于加载包含*.dex文件的jar包或apk文件
要求一个应用私有可写的目录去缓存编译的class文件
不允许加载外部存储空间的文件，以防注入攻击

DexClassLoader (String dexPath, 
                String optimizedDirectory, 
                String librarySearchPath, 
                ClassLoader parent)

dexPath：包含dex文件的jar包或apk文件路径
optimizedDirectory：释放目录，可以理解为缓存目录，必须为应用私有目录，不能为空
librarySearchPath	native：库的路径，可为空
parent：父类加载器

**插件化技术核心**：

类加载机制，Activity运行机制，HOOK技术，反射机制。

#### 插桩式插件化框架实现

*代码见：MyPluginAppDemo，反射机制*

#### 插件化框架

**VirtualAPK** 是滴滴出行自研的一款优秀的插件化框架，支持所有四大组件，
兼容性好且适配绝大部分机型，同时开发入侵性也较低，可以使用 gradle 构建插件。
以下是 VirtualAPK 和其他插件化框架的对比：

<img src="http://ww1.sinaimg.cn/large/0073bao7gy1gmmagphcuuj30u00njwj9.jpg" alt="a1" style="zoom: 67%;" align="left" />

**Qihoo360 / RePlugin**
RePlugin 是一套完整的、稳定的、适合全面使用的，占坑类插件化方案，优点是稳定、
灵活、自由，大小项目兼用。除了基本的支持四大组件以及绝大部分安卓特性之外，它还有以下特性：
<img src="http://ww1.sinaimg.cn/large/0073bao7gy1gmmah5fyaej30u00rf7aa.jpg" alt="a2" style="zoom:50%;" />

**alibaba / atlas**
Atlas 是阿里在 2017 年开源的一个动态组件化框架，实现的功能其实和插件化差不多，
主要完成的是在运行环境中按需地去完成各个 bundle 的安装，加载类和资源。这个框架算是目前功能最为强大的了，
详细请参考其文档：
Atlas (6500+ stars): https://github.com/alibaba/atlas/tree/master/atlas-docs

#### 注意事项
- Android 9.0（API 28）开始不支持反射了
- 需要先打包payapp-debug.apk放到Android/data/com.wangxingxing.mypluginappdemo/files/目录下
- PayActivity需要实现PluginActivityInterface接口
- 开发环境：AndroidStudio 4.1.1，compileSdkVersion 28，targetSdkVersion 28





