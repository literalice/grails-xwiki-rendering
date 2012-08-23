/**
 * Gant script that creates a Grails XWiki Macro
 *
 * @author Masatoshi Hayashi
 */

includeTargets << grailsScript("_GrailsInit")
includeTargets << grailsScript("_GrailsCreateArtifacts")

target ('default': "Creates a new XWiki Macro") {
    //noinspection GroovyAssignabilityCheck
    depends(checkVersion, parseArguments)

    def type = "XwikiNoParameterMacro"
    promptForName(type: type)

    def name = argsMap["params"][0]
    createArtifact(name: name, suffix: "Macro", type: type, path: "grails-app/xwiki")
}

