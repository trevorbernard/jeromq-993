classpath := `gradle -q printClasspath`

build:
    gradle build

clean:
    gradle clean

launch-router: build
    java -cp {{classpath}} org.example.RouterServer

launch-dealer: build
    java -cp {{classpath}} org.example.DealerMonitor
