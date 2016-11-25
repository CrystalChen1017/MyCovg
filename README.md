## jacoco仿emma实现统计手工（UI）测试覆盖率

### What can this utils do?
It can get ui test or manual test code coverage report, unlike original Jacoco, aiming at unit test or   
上一篇文章说道如何使用emma统计黑盒测试的覆盖率，但是仅仅只是针对eclipse的项目架构，但android studio的工程结构和eclipse不一样，因此使用ant进行构建时，源码路径也不一样，当然如果你熟悉ant，懂得修改build.xml文件也是可以实现emma对android studio工程的覆盖率统计，本文采用的是另一种方案，大致流程为：
- 使用Jacoco收集覆盖率信息
- 效仿emma的方式启动应用，监听应用什么时候退出



### 准备条件
- 去[这里](http://download.csdn.net/download/cxq234843654/9693789)下载所需要的文件
- 一个待测的工程

### 假设：
**待测app工程名：**MyEmma
**包名：**com.learn.mycovg

**1. 在com.learn.mycovg中新一个包名为jacoco**
将下载下来的5个文件复制到该包下，这边InstrumentedActivity可能会报错，把继承的类改被测应用的主Activity就行了

**2. 配置AndroidManifest.xml文件**
- 添加Instrumentation，在manifest节点下添加：
```
 <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <instrumentation
        android:name=".jacoco.JacocoInstrumentation"
        android:handleProfiling="true"
        android:label="CoverageInstrumentation"
        android:targetPackage="com.learn.mycovg" />
```

- 添加Activity,在application下添加
```
<activity
            android:name=".jacoco.InstrumentedActivity"
            android:label="InstrumentationActivity" />
```

- 添加广播，在application下添加

```
<receiver android:name=".jacoco.JacocoStopBroacast">
            <intent-filter>
                <action android:name="com.meitu.testauxiliary.jacoco.JacocoStopBroacast" />
            </intent-filter>
        </receiver>
```

**注意：**测试包要改成你自己的包名，广播那边的包名也一样

**3. 配置gradle**
打开app的gralde文件

- 在android -- buildTypes的括号里面添加

```
debug{
            testCoverageEnabled = true
        }
```

- 添加生成报告用的task（与android节点平级）

```
def coverageSourceDirs = [
        '../app/src/main/java'
]
task jacocoTestReport(type: JacocoReport) {
    group = "Reporting"
    description = "Generate Jacoco coverage reports after running tests."
    reports {
        xml.enabled = true
        html.enabled = true
    }
    classDirectories = fileTree(
            dir: './build/intermediates/classes/debug',
            excludes: ['**/R*.class',
                       '**/*$InjectAdapter.class',
                       '**/*$ModuleAdapter.class',
                       '**/*$ViewInjector*.class'
            ])
    sourceDirectories = files(coverageSourceDirs)
    executionData = files("$buildDir/outputs/code-coverage/connected/coverage.ec")

    doFirst {
        new File("$buildDir/intermediates/classes/").eachFileRecurse { file ->
            if (file.name.contains('$$')) {
                file.renameTo(file.path.replace('$$', '$'))
            }
        }
    }
}

```

**4. 运行installDebug **
在Android Studio的右侧，点击gradle图标，app -- install --installDebug,
adb shell am instrument com.meitu.testrom/jacoco.JacocoInstrumentation

![这里写图片描述](http://img.blog.csdn.net/20161125173948232)
**5. 使用am instrument命令打开app**
-首先先确保我们写的instrumentation已经在设备上正确安装了，因此在cmd命令窗口中输入adb shell,然后输入

```
    pm list instumentation 
```
如果能看到instrumentation:com.learn.mycovg/.jacoco.JacocoInstrumentation (target=com.learn.mycovg)字眼说明配置正确。

- 启动app，在shell模式下输入

```
am instrument com.learn.mycovg/.jacoco.JacocoInstrumentation

```
如果一切配置顺利的话，此时app应该自动打开了。
接下来手动在app上进行一些测试性的操作，最后双击BACK键退出主Activity，之后，会在sdcard下生成coverage.ec文件

**6. 生成覆盖率报告**
- 在gradle projects视图下，app -- verification - 双击createDebugCoverageReport，此番操作后会在outputs下生成code-coverage目录

![这里写图片描述](http://img.blog.csdn.net/20161125174012013)
- 将coverage.ec复制到你的工程（MyCovg\app\build\outputs\code-coverage\connected）下

- 打开gradle(app)文件，找到task jacocoTestReport右键运行



**7. 查看覆盖率报告**
位于MyCovg\app\build\reports\jacoco\jacocoTestReport\html文件夹下，打开index.html就可以查看了
![这里写图片描述](http://img.blog.csdn.net/20161125174028935)

**说明：**
如果想要Acitivity不销毁的时候也想手机覆盖率信息，下载的代码里面里面有一个JacocoStopBroadcast文件，这时候只要通过发送广播就可以生成ec文件

```
adb shell am broadcast -a com.learn.mycovg.jacoco.JacocoStopBroacast
```

就可以生成ec文件。
