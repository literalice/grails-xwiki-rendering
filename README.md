# XWiki Rendering Framework Grails Integration #

This Grails plugin allows you to convert texts using XWiki Rendering Framework.

## Example ##

```groovy
def result = xwikiRenderer.render("** Bold ** {{code type="java"}}class Macro{}{{/code}}")
// <b>Bold</b><pre class="java">class Macro{}</pre>
```

## Documents ##

<dl>
    <dt>The plugin's reference documentation</dt>
    <dd>http://literalice.github.com/grails-xwiki-rendering/</dd>
    <dt>About XWiki Rendering Framework</dt>
    <dd>http://rendering.xwiki.org/xwiki/bin/view/Main/WebHome</dd>
</dl>

