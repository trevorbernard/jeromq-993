classpath := `gradle -q printClasspath`

default:
    @just --list --justfile {{justfile()}}

build:
    gradle build

clean:
    gradle clean

launch-router: build
    java -cp {{classpath}} org.example.RouterServer

launch-dealer: build
    java -cp {{classpath}} org.example.DealerMonitor

create-device:
    ./ops/create-device.sh

delete-device:
    ./ops/delete-device.sh

down-device:
    ./ops/down-device.sh

up-device:
    ./ops/up-device.sh
