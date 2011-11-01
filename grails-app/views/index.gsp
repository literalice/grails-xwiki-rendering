<%@ page contentType="text/html;charset=UTF-8" %>
<html>
  <head><title>Simple GSP page</title></head>
  <body>Place your content here</body>

  <xwiki:render>**XWiki** Framework</xwiki:render>

  <xwiki:render>**XWiki** Framework ${new Date()}</xwiki:render>

  Macros:
  <xwiki:render>
      **RUBY** Macro:
      {{rb read="test"}}TEST{{/rb}}

      **Date** Macro: {{date /}}
  </xwiki:render>
</html>