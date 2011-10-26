# XWiki Rendering Framework Grails Integration #

This Grails plugin allows you to convert texts using XWiki Rendering Framework.

[XWiki Rendering Framework](http://rendering.xwiki.org/xwiki/bin/view/Main/WebHome)

## Upgrading from version 0.1 ##

### Removal of TagLib "xwikiRender" ###

Use "&lt;xwiki:render inputSyntax='' /&gt;" tag instead.

### Removal of Service "RenderingService" ###

Use "XWikiRenderer" service instead.

## Installation ##

Just run "install-pluing" command.

    grails install-plugin xwiki-rendering

## Tag Libraries ##

This Plugin provides "&lt;xwiki:render inputSyntax='' outputSyntax='' /&gt;" tag.

inputSyntax:

* xwiki/2.1 (XWiki Syntax 2.1) : **default**
* mediawiki/1.0 (Media Wiki Syntax)
* creole/1.0 (Creole 1.0 Syntax)
* etc. (See also [XWiki Rendering Framework Supported Syntaxes](http://rendering.xwiki.org/xwiki/bin/view/Main/WebHome#HSupportedSyntaxes))

outputSyntax:

* xhtml/1.0 (XHTML) : **default**
* plain/1.0 (Plain Text)
* xwiki/2.1 (XWiki Syntax 2.1)
* docbook/4.4 (DocBook 4.4)
* etc. (See also [XWiki Rendering Framework Supported Syntaxes](http://rendering.xwiki.org/xwiki/bin/view/Main/WebHome#HSupportedSyntaxes))

### Example ###

	<xwiki:render inputSyntax="xwiki/2.1" outputSyntax="xhtml/1.0">
	= level1 headings =
	** bold text **
	</xwiki:render>

See also [XWiki Syntax](http://platform.xwiki.org/xwiki/bin/view/Main/XWikiSyntax) for the xwiki/2.0 syntax basics

## Services ##

This Plugin provides "xwikiRenderer" as a Grails Service. you can inject it like this.

	def xwikiRenderer

This service has "render(sourceText, inputSyntax, outputSyntax)" and "render(reader, writer, inputSyntax, outputSyntax)" method.

	class SomeController {

	    def xwikiRenderer

	    def someAction = {
	        String testString = """
	        =level1=
	        **bold**
	        """
	        String result = xwikiRenderer.render(textString, "mediawiki/1.0", "plain/1.0")

	        ...
	    }

	}

or

	class SomeController {

	    def xwikiRenderer

	    def someAction = {
	        StringWriter writer = new StringWriter()
	        new File("./test").withReader { reader ->
                xwikiRenderer.render(reader, writer, "mediawiki/1.0", "plain/1.0")
	        }
	        String result = writer.toString()

	        ...
	    }

	}

## Using Macros ##

The Macro libraries must be added to your grails project in order to use XWiki Macros.

If you want to create your own macro, [ExtendingMacro](http://rendering.xwiki.org/xwiki/bin/view/Main/ExtendingMacro) would be useful.

### Example ###
If you need [Comment Macro](http://extensions.xwiki.org/xwiki/bin/view/Extension/Comment+Macro) :

grails-app/conf/BuildConfig.groovy

	dependencies {
	    ...
	    runtime("org.xwiki.rendering:xwiki-rendering-macro-comment:3.2") {
            transitive = false
	    }
	}

and:

	<xwiki:render inputSyntax="xwiki/2.1" outputSyntax="xhtml/1.0">
	= level1 headings =
	** bold text **
	{{comment}}
	comment here...(not be rendered)
	{{/comment}}
	</xwiki:render>

