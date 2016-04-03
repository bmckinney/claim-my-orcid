<#-- @ftlvariable name="" type="orcid.api.views.LoginView" -->
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">

    <title>Claim your IDs</title>

    <link rel="stylesheet" type="text/css" href="http://semantic-ui.com/dist/semantic.min.css">
    <script src="http://semantic-ui.com/examples/assets/library/jquery.min.js"></script>
    <script src="http://semantic-ui.com/dist/semantic.min.js"></script>

    <script src="http://semantic-ui.com/javascript/library/serialize-object.js"/>
    <script src="http://semantic-ui.com/javascript/api.js"></script>

    <script>
        $(document)
                .ready(function() {

                    // fix main menu to page on passing
                    $('.main.menu').visibility({
                        type: 'fixed'
                    });
                    $('.overlay').visibility({
                        type: 'fixed',
                        offset: 80
                    });

                    // lazy load images
                    $('.image').visibility({
                        type: 'image',
                        transition: 'vertical flip in',
                        duration: 500
                    });

                    // show dropdown on hover
                    $('.main.menu  .ui.dropdown').dropdown({
                        on: 'hover'
                    });

                });


    </script>

    <style type="text/css">

        body {
            background-color: #DADADA;
        }
        .main.container {
            margin-top: 2em;
        }

        .main.menu {
            margin-top: 4em;
            border-radius: 0;
            border: none;
            box-shadow: none;
            transition:
            box-shadow 0.5s ease,
            padding 0.5s ease
        ;
        }
        .main.menu .item img.logo {
            margin-right: 1.5em;
        }

        .overlay {
            float: left;
            margin: 0em 3em 1em 0em;
        }
        .overlay .menu {
            position: relative;
            left: 0;
            transition: left 0.5s ease;
        }

        .main.menu.fixed {
            background-color: #FFFFFF;
            border: 1px solid #DDD;
            box-shadow: 0px 3px 5px rgba(0, 0, 0, 0.2);
        }
        .overlay.fixed .menu {
            left: 800px;
        }

        .text.container .left.floated.image {
            margin: 2em 2em 2em -4em;
        }
        .text.container .right.floated.image {
            margin: 2em -4em 2em 2em;
        }

        .ui.footer.segment {
            margin: 5em 0em 0em;
            padding: 5em 0em;
        }
        img.bottom {
            vertical-align: text-bottom;
        }    </style>

</head>
<body>

<div class="ui inverted menu">
    <div class="ui container">
        <div class="header item">
            <img class="logo" src="/assets/images/shield.png" style="margin-right: 20px;">
            Claim your IDs
        </div>
    </div>
</div>

<div class="ui main text container">

    <div class="ui text container" style="margin-bottom: 35px; margin-top: 65px; padding: 10px;">
        <div class="ui items">
            <#if config.getAuthRedirectUrl()??>
            <a href="${config.getAuthRedirectUrl()}">Login using HarvardKey</a>
            </#if>
        </div>
    </div>

</div>

<div class="ui inverted vertical footer segment">
    <div class="ui center aligned container">
        <div class="ui horizontal inverted small divided link list">
            <a class="item" href="#">Site Map</a>
            <a class="item" href="#">Contact Us</a>
            <a class="item" href="#">Terms and Conditions</a>
            <a class="item" href="#">Privacy Policy</a>
        </div>
    </div>
</div>
</body>

</html>
