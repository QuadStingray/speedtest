import scala.sys.process._

enablePlugins(BuildInfoPlugin)

buildInfoPackage := "%s.%s".format(organization.value, name.value)

buildInfoOptions += BuildInfoOption.BuildTime

buildInfoKeys += BuildInfoKey.action("gitLastCommitHash") {
  "git rev-parse HEAD".!!.trim
}

buildInfoKeys += BuildInfoKey.action("organization") {
  organization.value
}
