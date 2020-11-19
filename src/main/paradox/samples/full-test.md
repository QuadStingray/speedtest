# Full Test
## Adding To build.sbt
@@@ vars
@@dependency[sbt,Maven,Gradle] {
  group="com.quadstingray"
  artifact="speedtest_2.13"
  version="$project.version$"
}
@@@

## Run Auto Full Test
The Framework choose the automatic method to find best server.

@@snip [build.sbt](../../../test/scala/com/quadstingray/speedtest/ndt7/SpeedTestSpec.scala) { #auto-full-test-without }

## Run Full Test with specific Server
> Removed from API because Token needed

## Run Full Test with CallBacks
With CallBacks you can update your UI do somethings at running the test.

@@snip [build.sbt](../../../test/scala/com/quadstingray/speedtest/ndt7/SpeedTestSpec.scala) { #auto-full-test-callbacks }
