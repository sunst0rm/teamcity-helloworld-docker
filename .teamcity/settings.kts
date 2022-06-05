import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.dockerCommand
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2021.2"

project {

    buildType(Pipeline)
}

object Pipeline : BuildType({
    name = "Pipeline"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        dockerCommand {
            name = "Build image"
            commandType = build {
                source = file {
                    path = "Dockerfile"
                }
                namesAndTags = """
                    funkycoolboi1487745/helloworld-python:v1.0-%build.number%
                    funkycoolboi1487745/helloworld-python:latest
                """.trimIndent()
            }
            param("dockerImage.platform", "linux")
        }
        dockerCommand {
            name = "Push image"
            commandType = push {
                namesAndTags = """
                    funkycoolboi1487745/helloworld-python:v1.0-%build.number%
                    funkycoolboi1487745/helloworld-python:latest
                """.trimIndent()
            }
        }
    }

    triggers {
        vcs {
        }
    }
})
