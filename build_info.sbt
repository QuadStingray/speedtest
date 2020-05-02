import scala.sys.process._

enablePlugins(BuildInfoPlugin)

buildInfoPackage := "com.krick.aurora.admin"

buildInfoOptions += BuildInfoOption.BuildTime

buildInfoKeys += BuildInfoKey.action("gitLastCommitHash") {
  "git rev-parse HEAD".!!.trim
}
