@artifact.package@
import org.xwiki.rendering.block.Block
import org.xwiki.rendering.transformation.MacroTransformationContext
import org.xwiki.rendering.macro.MacroExecutionException
import com.monochromeroad.grails.plugins.xwiki.macro.DefaultXWikiMacro

class @artifact.name@ extends DefaultXWikiMacro<@artifact.name@Parameters> {

    public @artifact.name@() {
        super("", @artifact.name@Parameters) // TODO Set a label to use the macro
    }

    @Override
    boolean supportsInlineMode() {
        return false
    }

    @Override
    public List<Block> execute(@artifact.name@Parameters parameters, String content, MacroTransformationContext context) throws MacroExecutionException {
        return null
    }
}

class @artifact.name@Parameters {

}

