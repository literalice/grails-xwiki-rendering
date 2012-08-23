@artifact.package@
import org.xwiki.rendering.block.Block
import org.xwiki.rendering.transformation.MacroTransformationContext
import org.xwiki.rendering.macro.MacroExecutionException
import com.monochromeroad.grails.plugins.xwiki.macro.DefaultXWikiNoParameterMacro

class @artifact.name@ extends DefaultXWikiNoParameterMacro {

    public @artifact.name@() {
        super("") // TODO Set a label to use the macro
    }

    @Override
    boolean supportsInlineMode() {
        return false
    }

    @Override
    List<Block> execute(String content, MacroTransformationContext context) throws MacroExecutionException {
        return null
    }
}

