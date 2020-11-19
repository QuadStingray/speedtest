# Download Test
## Adding To build.sbt
@@@ vars
@@dependency[sbt,Maven,Gradle] {
  group="com.quadstingray"
  artifact="speedtest_2.13"
  version="$project.version$"
}
@@@

## Run Download Test
The Framework choose the automatic method to find best server.

@@snip [build.sbt](../../../test/scala/com/quadstingray/speedtest/ndt7/SpeedTestDownloadSpec.scala) { #auto-download-test-without }

## Run Download Test with specific Server
> Removed from API because Token needed

## Run Download Test with CallBacks
With CallBacks you can update your UI do somethings at running the test.

@@snip [build.sbt](../../../test/scala/com/quadstingray/speedtest/ndt7/SpeedTestDownloadSpec.scala) { #auto-download-test-callbacks }
