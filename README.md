check the official wiki for complete instructions:
http://162.216.4.195/mediawiki/core/index.php/Home

utilities included in this library:<br>
JediColor - color manipulation<br>
JediImage - work with images and bitmaps<br>
JediImageStore - image storage helper<br>
ComplexPreferences - save complex objects to shared preferences<br>
FileHelper - helper to write and read all kinds of objects<br>
JediUtil - base utilities<br>
MD5 - md5 encriprtion util<br>
Log - improved logger for android<br><br>
and more ...<br><br>

initialize JediUtil inside Application onCreate
```java
JediUtil.init(this);
```
optionally set debug mode and other setup
```java
JediUtil.getInstance()
                .setAppName(getString(R.string.app_name))
                .setDebug(BuildConfig.DEBUG)
                .useExternalStorage(false)
                .setSendDataToServer(false);
```

if you want to use it with Dagger add the module to the graph:
```java
ObjectGraph graph = ObjectGraph.create(getModules().toArray());
ApplicationGraph.setObjectGraph(graph);
```
```java
    protected List<Object> getModules() {
        return Arrays.asList(
                new YourModule(this)
                ,new JediModule(this)
        );
    }
```
