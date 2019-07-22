# MultiMonitor

## Motivation
sometimes we need to see the log information between multi target, espaeically they are
connected via network and messages transmission activity. this multi monitor enables you
to see the targeted log file with tail and merge it in the one monitor, so you can see
the all of the target activity just in that monitor during debugging.

## How to use

```bash
> git clone https://github.com/tomoyafujita/MultiMonitor
> cd MultiMonitor/src
> javac MultiMonitor.java 
> java MultiMonitor
```

## Precondition
jackson
 - jackson-annotations-2.9.0.jar
 - jackson-core-2.9.0.jar
 - jackson-databind-2.3.1.jar
jsch
 - jsch-0.1.54.jar

## Verified Environment
Linux 4.4.0-77-generic #98-Ubuntu SMP Wed Apr 26 08:33:44 UTC 2017 i686 i686 i686 GNU/Linux
java version "1.8.0_121"
Java(TM) SE Runtime Environment (build 1.8.0_121-b13)
Java HotSpot(TM) Server VM (build 25.121-b13, mixed mode)

## Using MultiMonitor
$ java MultiMonitor
"quit + <Enter>", will exit app.

## Limitation
if any of the proved system has problem. all of the tail process will be shuted down and exit app.

## Install
N.A

## Contribution
N.A

## Author
[TomoyaFujita](https://github.com/tomoyafujita)
