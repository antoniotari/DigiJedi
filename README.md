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
