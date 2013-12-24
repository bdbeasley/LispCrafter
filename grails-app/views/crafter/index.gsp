<%--
  Created by IntelliJ IDEA.
  User: Daenerys-1
  Date: 12/18/13
  Time: 4:58 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title></title>
    <meta name="layout" content="crafter"/>
    <style type="text/css">
        body {
            text-align: center;
            min-width: 740px;
            padding: 0;
            margin: 50 0 0 0;
            max-height:800px;
        }

        #wrapper {
            font-size: 12px;
            text-align: left;
            width: 740px;
            margin-left: auto;
            margin-right: auto;
            padding: 0;
            border: 1px solid brown;
            -moz-border-radius: 5px;
            border-radius: 5px;
            background:black;
            word-wrap: break-word;
            flex-wrap:wrap;
        }

        #content {
            margin: 10px 220px 0 10px;
            min-height:400px;
            color: #ffffff;
            font-family: "Arial Black", Gadget, sans-serif;
        }

        #content .inner {
            padding-top: 1px;
            margin: 0 10px 10px 10px;
        }

        #side {
            float: right;
            width: 200px;
            min-height:400px;
            margin-bottom:10px;
            margin-top:10px;
            margin-right:10px;
            border: 1px solid crimson;
            -moz-border-radius: 15px;
            border-radius: 15px;
            background-color: bisque;

        }

        #side .inner {
            padding-top: 1px;
            margin: 0 10px 10px 10px;
        }

        #footer {
            clear: both;
            background-color: brown;
            text-align:center;
        }

        #footer .inner {
        }

        .inputLine {
            display:inline;
        }

        .inputLineHolder {
        }
    </style>
    <g:javascript>

        var lineNumber = 1;

        function onOutput(output) {
            $('<div/>').html(""+output).appendTo("#content");
            prompt();
        }

        function onInput(input) {
            jQuery.ajax({
                type: "GET",
                url: "${g.createLink(action:'createQuery', controller:'lisp')}",
                data: {"query": (input)},
                success: function(data) {
                    onOutput(data.result);
                }, error: function(jqXHR, textStatus, errorThrown) {
                    alert("error!");
                }
            });

            $('<div/>').html("Connecting to LISP interpreter...").appendTo("#content");
            $('<div/>').html("Parsing LISP code...").appendTo("#content");
        }

        function prompt() {
            var tempLineNumber = lineNumber;
            $('<div/>').click(function() {
                $('#inputLine' + tempLineNumber).focus();
            }).addClass("inputLineHolder").append("$").append($('<p/>', {
                id: 'inputLine' + tempLineNumber,
                contentEditable: true
            }).addClass("inputLine").keypress(function(event) {
                        if(event.which == 13) {
                            event.preventDefault();
                            $("#inputLine" + (tempLineNumber)).attr("contentEditable", false);
                            onInput($("#inputLine" + tempLineNumber).html());
                        }
            })).appendTo("#content");
            $('#inputLine' + tempLineNumber).focus();
            lineNumber = lineNumber + 1;
        }

        prompt();
    </g:javascript>
</head>
<body>
<h1>Calicoder's Lisp Interpreter</h1>
<h3>Inspired by my Reading Robert Wilensky's Common LispCraft in ECS140A/B</h3>
<div id="wrapper">
    <div id="side">
            Sidebar content here
    </div>
    <div id="content">
    </div>
    <div id="footer">
        <div class="inner">
            Copyright &copy; <g:link url="http://www.calicoder.com">Calicoder</g:link>
        </div>
    </div>
</div>
</body>
</html>