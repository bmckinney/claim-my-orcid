<#-- @ftlvariable name="" type="orcid.api.views.PersonView" -->
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

                    // ROUTES
                    $.fn.api.settings.api = {
                        'claim identifier'  : '/identifiers/claim/{id}',
                        'discard identifier': '/identifiers/discard/{id}',
                        'create orcid'      : '/identifiers/create/orcid{id}'
                    };

                    $.fn.api.settings.successTest = function(response) {
                        return response.status == 'OK';
                    }


                    // CLAIM
                    $('.claim.button')
                            .api({
                                action: 'claim identifier',
                                onComplete: function(response) {
                                    $(this).parent().transition('hide');
                                    $(this).parent().prev().transition('show');
                                }
                            });

                    // DISCARD
                    $('.discard.button')
                            .api({
                                action: 'discard identifier',
                                onComplete: function(response) {
                                    $(this).parent().parent().parent().transition('hide');
                                }
                            });


                    $('#otherids').hide();

                    <#if !(person.orcidStatus)?? || person.orcidStatus == "">

                    // random mailinator email generator
                    var minimum = 1;
                    var maximum = 1000;
                    var randomnumber = Math.floor(Math.random() * (maximum - minimum + 1)) + minimum;
                    var randomEmail = 'crimson' + randomnumber + '@mailinator.com';

                    //var orcidRedir = "https://developers.google.com/oauthplayground";
                    //var orcidRedir = "http://eaton.hul.harvard.edu:9999/seas/author/claim";
                    var orcidRedir = "http://eaton.hul.harvard.edu:9000/seas/author/claim";


                    var newOrcidUrl = "https://sandbox.orcid.org/oauth/authorize?" +
                            "client_id=APP-E35X3FEDCM0LPKT6" +
                            "&response_type=code" +
                            "&scope=/authenticate" +
                            "&redirect_uri=" + orcidRedir +
                            "&family_names=${person.lastName}" +
                            "&given_names=${person.firstName}" +
                            "&email=" + randomEmail +
                            "&state=${person.uuid}" +
                            "&lang=en";

                    var connectOrcidUrl = "https://sandbox.orcid.org/oauth/authorize?" +
                            "client_id=APP-E35X3FEDCM0LPKT6" +
                            "&scope=/authenticate" +
                            "&redirect_uri=" + orcidRedir +
                            "&show_login=true" +
                            "&state=${person.uuid}" +
                            "&lang=en";

                    document.getElementById("newOrcidButton").onclick = function () {
                        location.href = newOrcidUrl;
                    };
                    document.getElementById("connectOrcidButton").onclick = function () {
                        location.href = connectOrcidUrl;
                    };

                    </#if>

                    document.getElementById("otherIdsButton").onclick = function () {
                        console.log("ok then");
                        $('#otherids').show();
                        $('#otherIdsButton').hide();
                    };

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
        }

        .truncate {
            width: 300px;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
        }

    </style>

</head>
<body>

<div class="ui inverted menu">
    <div class="ui container">
        <div class="header item">
            <img class="logo" src="/assets/images/shield.png" style="margin-right: 20px;">
            Claim your IDs
        </div>
        <!--
        <a href="#" class="item">Home</a>
        <div class="ui simple dropdown item">
            Dropdown <i class="dropdown icon"></i>
            <div class="menu">
                <a class="item" href="#">Link Item</a>
                <a class="item" href="#">Link Item</a>
                <div class="divider"></div>
                <div class="header">Header Item</div>
                <div class="item">
                    <i class="dropdown icon"></i>
                    Sub Menu
                    <div class="menu">
                        <a class="item" href="#">Link Item</a>
                        <a class="item" href="#">Link Item</a>
                    </div>
                </div>
                <a class="item" href="#">Link Item</a>
            </div>
        </div>
        -->
    </div>
</div>

<div class="ui main text container">

    <div class="ui text container" style="margin-bottom: 35px; margin-top: 65px; padding: 10px;">
        <div class="ui items">
            <div class="item">
                <#if person.pictureUrl??>
                <a class="ui tiny image">
                    <img src="${person.pictureUrl}">
                </a>
                <#else>
                    <img class="ui image" src="/assets/images/avatar.png" width="100px" height="100px"/>
                </#if>
                <div class="content">
                    <div class="ui large header">${(person.fullName)!"Full name unknown"}</div>
                    <div class="description">${(person.jobTitle)!"Job title unknown"}</div>
                    <div class="description">${(person.email)!"Email unknown"}</div>
                </div>
            </div>
        </div>
    </div>

<#if !(person.orcidStatus)?? || person.orcidStatus == "">

    <div class="ui one cards">

        <div class="fluid card">
            <div class="extra center aligned content">
                <img class="small ui image" src="/assets/images/orcid.png" />
            </div>
            <div class="content">
                <div class="header">
                    About ORCID
                </div>
                <div class="description">
                    ORCID is an open, non-profit, community-based effort to provide a registry of unique researcher
                    identifiers and a transparent method of linking research activities and outputs to these
                    identifiers. ORCID is unique in its ability to reach across disciplines, research sectors, and
                    national boundaries and its cooperation with other identifier systems. Find out more on their
                    <a href="http://orcid.org/about" target="orcid">website</a>.
                </div>
            </div>
            <div class="extra center aligned content">
                <button class="ui olive orcid button" id="newOrcidButton">
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Create my ORCID iD&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                </button>
                <button class="ui orcid button" id="connectOrcidButton">
                    I have one, connect it to Harvard.
                </button>
            </div>
        </div>

    </div>

<#else>

    <#--<div class="ui success message">-->
        <#--<div class="header">-->
            <#--Thank you for registering an ORCID.-->
        <#--</div>-->
        <#--<p>Please claim additional researcher IDs from the list below.</p>-->
    <#--</div>-->

    <div class="ui one stackable cards">
        <#list identifierList as identifier>
            <#if ((identifier.state)?? && identifier.state != "discarded" && identifier.type == "orcid") || !(identifier.state)?? >
                <div class="card">
                    <div class="extra center aligned content">
                        <img class="ui image" src="/assets/images/${identifier.type}.png" style="height: 45px;"/>
                    </div>
                    <div class="content">
                        <div class="header">
                            ${identifier.label}
                        </div>
                        <div class="meta">
                            ${identifier.value}
                        </div>
                        <div class="description">
                            <#if (identifier.url)?? >
                                <a href="${identifier.url}" target="externalId">${identifier.url}</a>
                            </#if>
                        </div>

                    </div>
                    <div class="extra center aligned content">
                        <div class="ui big olive label <#if !(identifier.state)?? || identifier.state != 'claimed'>hidden</#if>">
                            <i class="checkmark icon"></i>Thank you for registering an ORCID!
                        </div>
                        <#if !(identifier.state)??>
                            <div class="ui large buttons">
                                <button class="ui black claim button" data-id="${identifier.uuid}">Claim</button>
                                <button class="ui discard button" data-id="${identifier.uuid}">Discard</button>
                            </div>
                        </#if>
                    </div>
                </div>
            </#if>
        </#list>
    </div>

    <div class="ui horizontal divider" style="margin-top: 35px; margin-bottom: 35px">
        Other Public researcher Identifiers
    </div>
    <div class="ui large black labeled icon button center aligned" id="otherIdsButton" data-id="${person.uuid}">
        Add other IDs
        <i class="add icon"></i>
    </div>

    <div id="otherids" class="ui two stackable cards hidden">
        <#list identifierList as identifier>
            <#if ((identifier.state)?? && identifier.state != "discarded" && identifier.type != "orcid" && identifier.type != "ldap") || !(identifier.state)?? >
                <div class="card">
                    <div class="extra center aligned content">
                        <img class="ui image" src="/assets/images/${identifier.type}.png" style="height: 45px;"/>
                    </div>
                    <div class="content">
                        <div class="header">
                        ${identifier.label}
                        </div>
                        <div class="meta truncate">
                        ${identifier.value}
                        </div>
                        <#if (identifier.url)?? >
                            <div class="description">
                                <a href="${identifier.url}" target="externalId">Show me the details</a>
                            </div>
                        </#if>
                    </div>
                    <div class="extra center aligned content">
                        <div class="ui big black label <#if !(identifier.state)?? || identifier.state != 'claimed'>hidden</#if>">
                            <i class="checkmark icon"></i>Claimed
                        </div>
                        <#if !(identifier.state)??>
                            <div class="ui large buttons">
                                <button class="ui black claim button" data-id="${identifier.uuid}">Claim</button>
                                <button class="ui discard button" data-id="${identifier.uuid}">Discard</button>
                            </div>
                        </#if>
                    </div>
                </div>
            </#if>
        </#list>
    </div>

</#if>


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
