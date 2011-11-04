@artifact.package@
import org.xwiki.rendering.block.Block
import org.xwiki.rendering.transformation.MacroTransformationContext

import com.monochromeroad.grails.plugins.xwiki.macro.GrailsMacro

class @artifact.name@ implements GrailsMacro {

    static macroName = ""

    static inlineSupport = true

    List<Block> execute(parameters, String content, MacroTransformationContext context) {
        // execute macro
        return null;
    }
}