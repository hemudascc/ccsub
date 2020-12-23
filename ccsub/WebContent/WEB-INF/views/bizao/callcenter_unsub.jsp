<%@ page pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>


<!DOCTYPE html>

<html lang="en">
<head>

    <!--<title>Reycreo Games :: Home</title>-->
    <title>Deactivation</title>
    
    <meta charset="utf-8" />
    <meta content="IE=edge" http-equiv="X-UA-Compatible" />
    <meta content="width=device-width, initial-scale=1" name="viewport" />
    <meta content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport" />
    <meta content="#333" name="theme-color" />

    <meta content="Play Unlimited Online Games, racing games, sports games, fun games, puzzle games, action games, flash games, adventure games and more on Reycreo" name="description" />

    
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet" />
    <link rel="stylesheet" href="http://d2pd5v9y7ukdgh.cloudfront.net/prod_v1/static/static/assets/css/preload.min.css" />
    <link rel="stylesheet" href="http://d2pd5v9y7ukdgh.cloudfront.net/prod_v1/static/static/assets/css/plugins.min.css" />
    <link rel="stylesheet" href="http://d2pd5v9y7ukdgh.cloudfront.net/prod_v1/static/static/assets/css/style.red-500.css?v=2" />
    <link rel="stylesheet" href="http://d2pd5v9y7ukdgh.cloudfront.net/prod_v1/static/static/assets/css/custom.css?v=2" />
    <!--
        <link rel="stylesheet" type="text/css" href="/assets/css/width-boxed.min.css"/>
    -->


    
    <link rel="stylesheet" type="text/css" href="http://d2pd5v9y7ukdgh.cloudfront.net/prod_v1/static/static/assets/css/slick.css" />
    <link rel="stylesheet" type="text/css" href="http://d2pd5v9y7ukdgh.cloudfront.net/prod_v1/static/static/assets/css/slick-theme.css" />
<meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.2.1/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.6/umd/popper.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.2.1/js/bootstrap.min.js"></script>
    
    <!-- Global site tag (gtag.js) - Google Analytics -->
   <!-- <script async src="https://www.googletagmanager.com/gtag/js?id=UA-113066225-1"></script>-->
    <script>
        var currentURL = window.location.href;
        var reycreoDomain = "reycreo";
        var reycreoStaging = "stagingreycreo";
        var ga_id = "UA-110459281-1";
        window.ga = window.ga || function () {
            (ga.q = ga.q || []).push(arguments)
        };
        ga.l = +new Date;
        if (currentURL.indexOf(reycreoStaging) != -1) {
            ga_id = "UA-110459281-2";
        } else if (currentURL.indexOf(reycreoDomain) != -1) {
            ga_id = "UA-110459281-1";

        } else {
            ga_id = "UA-110459281-1";
        }
        ga('create', ga_id, 'auto');
        ga('send', 'pageview');
    </script>
    <script async="" src="https://www.google-analytics.com/analytics.js"></script>
    <!-- End Google Analytics -->


    <script>
        if(!navigator.cookieEnabled){
            alert("Your browser cookies are disabled, please enable and try again.");
        }
    </script>

    <!--[if lt IE 9]>
    <script th:src="'http://d2pd5v9y7ukdgh.cloudfront.net/'+${awsBasePath}+'/static/assets/js/html5shiv.min.js'"></script>
    <script th:src="'http://d2pd5v9y7ukdgh.cloudfront.net/'+${awsBasePath}+'/static/assets/js/respond.min.js'"></script>
    <![endif]-->
    <script async="" src="//pagead2.googlesyndication.com/pagead/js/adsbygoogle.js"></script>
    <script>
    (adsbygoogle = window.adsbygoogle || []).push({
    google_ad_client: "ca-pub-2876248000359022",
    enable_page_level_ads: true
    });
    </script>
    
  <script type="text/javascript">
 function unsub(id,unsubUrl){ 
	  //alert("unsub "+unsubUrl);
	//alert(id);
   // event.preventDefault(); 
    $.ajax({
        url: unsubUrl,
        //dataType: "jsonp",
        //jsonpCallback: 'callback',
        type: 'GET',
        //accepts: 'application/json',
        crossDomain:true,
        //jsonp:true,
        data: {
            format: 'jsonp'
            
        },
        timeout: 180000,
        success: function(response) {
        	
        	var json = $.parseJSON(response);
        	//alert(json);
        	alert(json.messgae);
        	$("#"+id).text(); //Response
        	 //alert(response);
            //alert(response.messgae);
        	setTimeout(function(){// wait for 5 secs(2)
                location.reload(); // then reload the page.(3)
           }, 5000); 
        },
    error: function(textstatus,response) {
        alert("Some technical issue to unsubscribe.please try after some time");
        setTimeout(function(){// wait for 5 secs(2)
            location.reload(); // then reload the page.(3)
       }, 5000); 
    }
    });
    return false; // for good measure
};

</script>
  
  

<style>
	
	.reycreo-logo-mobile {
    margin-top: 16px;
    margin-bottom: 10px;
    width: 100px;
}
</style>

</head>
<body>

<div aria-labelledby="myModalLabel" class="modal" id="myModal" role="dialog" tabindex="-1">
    <div class="modal-dialog animated zoomIn animated-3x" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h3 class="modal-title color-primary" id="myModalLabel">Turnoff Data Saver</h3>
                <button aria-label="Close" class="close" data-dismiss="modal" type="button"><span aria-hidden="true">
                    <i class="zmdi zmdi-close"></i></span></button>
            </div>
            <div class="modal-body">
</div>
</div>
</div>
</div>



<div class="ms-site-container">
<header class="ms-header ms-header-white" style="display: none;">
        <!--ms-header-dark-->
        <div class="container container-full">
            <div class="ms-title">

                <a href="#">
                </a>
            </div>
            <div class="header-right">
                <div class="share-menu">
                    <a class="btn-circle btn-circle-primary animated zoomInDown animation-delay-7" href="javascript:void(0)">
                        <i class="zmdi zmdi-share"></i>
                    </a>
                </div>


            </div>
        </div>
    </header>
<nav class="navbar navbar-expand-md navbar-static ms-navbar navbar-mode ms-navbar-white">
        <div class="container container-full">
            
    <div class="navbar-header">
        <a class="navbar-brand" href="#">
            <img alt="GamePad Logo" class="reycreo-logo-mobile animated fadeInRight animation-delay-3"
             src="${pageContext.request.contextPath}/images/bizao/game_pad_banner.jpg" style="margin: -20px;">
        </a>
    </div>

</div>
</div>
</div>

	</div>
	</nav>
	</div>

<div class="container">
    <h2 align="center" style="font-weight: bold; font-size: 22px;">Bizao Interface de desactivation</h2>
</div>
<br>
<form action="${pageContext.request.contextPath}/cnt/bz/callcenter/agent"
 style="font-family: verdana; font-size: 14px;">
<div class="container" align="center">
    
  Numero de mobile Alias:
            <input type="text"  name="msisdnalias" value="${msisdnalias}" style="width:600px;">
           
  <br><br>
  
  <input type="submit" value="Submit"/>


</div>

<br><br>
<div class="container" align="center">
<c:if test="${msisdnalias!=null}">
		<table border="1" align="center" style="width:95%;text-align:center;font-size:15px;">
		<tr class="linear-bg1">
		<th >Alias</th>
		<th>Valide à partir de</th>
		<th>Valide pour</th>
		<th>Date de désinscription</th>
		<th>Statut</th>
		<th>Se désabonner</th>
		</tr>
	  <c:if test="${subscriberRegList==null or subscriberRegList.size()<=0}">
		<tr>
	 		<td colspan="6">
	 		ce numéro de mobile n'est pas abonné aucun service pour ce portail
	 	  </td>
	 	</tr> 
			
		</c:if>
		
		<c:if test="${subscriberRegList!=null and subscriberRegList.size()>0}">
	  <c:forEach items="${subscriberRegList}" var="subscriberReg" >	 		
	 		<tr>
	 		<td>${subscriberReg.msisdn}</td>
	 		<td>${subscriberReg.validityFrom}</td>
	 		<td>${subscriberReg.validityTo}</td>	
	 		<td>${subscriberReg.unsubDate}</td>  	
	 		<td>${subscriberReg.statusDescp}</td>	 		
	 		<td id="${subscriberReg.subscriberId}">
	 		<c:if test="${subscriberReg.status==1}">
	 		<a href="#" onclick="unsub('${subscriberReg.subscriberId}',
	 		'${unSubUrl}?subid=${subscriberReg.subscriberId}')">
	 		Desactiver Maintenant
	 		</a>
	 		</c:if>	 	
	 	  </td>
	 	</tr> 
			</c:forEach>	
		</c:if>
			
	</table>
	</c:if>
</div>
</form>

<br><br>
<div class="container">
    <h3 align="center" style="font-size: 18px; font-family: verdana;">š'il vous plaît désactiver avec soin</h3>
</div>
<br><br>


<br><br>
<footer class="ms-footer" style="margin-bottom: 58px; background-color: #0ca7fa;">
        <div class="container">
        </div>
    </footer>

    <script src="http://d2pd5v9y7ukdgh.cloudfront.net/prod_v1/static/static/assets/js/plugins.min.js"></script>
    <script src="http://d2pd5v9y7ukdgh.cloudfront.net/prod_v1/static/static/assets/js/app.min.js"></script>
    <script src="http://d2pd5v9y7ukdgh.cloudfront.net/prod_v1/static/static/assets/js/index.js?v=2"></script>



    <script type="text/javascript" src="http://d2pd5v9y7ukdgh.cloudfront.net/prod_v1/static/static/assets/js/slick.min.js"></script>


<!--<th:block th:replace="fragments/js :: one-signal-push">

</th:block>-->
<input id="hdn_categories" type="hidden" value="[&quot;most_popular&quot;, &quot;action&quot;, &quot;adventure&quot;, &quot;arcade&quot;, &quot;puzzles&quot;, &quot;sports_racing&quot;, &quot;strategy&quot;]" />

<script>


    $(document).ready(function () {
        $('.reycreo-banner-carousel').slick(
            {
                infinite: true,
                autoscroll: true,
                slidesToShow: 3,
                swipe: true,
                arrows: false,
                autoplay: true,
                autoplaySpeed: 3000,
                dots: true,
                centerMode: true,
                centerPadding: '60px',
                responsive: [
                    {
                        breakpoint: 1024,
                        settings: {
                            infinite: true,
                            slidesToShow: 3,
                            swipe: true,
                            arrows: false,
                            autoplay: true,
                            autoplaySpeed: 3000,
                            dots: true,
                            centerMode: true,
                            centerPadding: '60px',
                        }
                    },
                    {
                        breakpoint: 840,
                        settings: {
                            infinite: true,
                            slidesToShow: 2,
                            swipe: true,
                            arrows: false,
                            autoplay: true,
                            autoplaySpeed: 3000,
                            dots: true,
                            centerMode: true,
                            centerPadding: '60px',
                        }
                    },

                    {
                        breakpoint: 620,
                        settings: {
                            infinite: true,
                            slidesToShow: 1,
                            swipe: true,
                            arrows: false,
                            autoplay: true,
                            autoplaySpeed: 3000,
                            dots: true,
                            centerMode: true,
                            centerPadding: '60px',
                        }
                    },

                    {
                        breakpoint: 480,
                        settings: {
                            infinite: true,
                            slidesToShow: 1,
                            swipe: true,
                            arrows: false,
                            autoplay: true,
                            autoplaySpeed: 3000,
                            dots: true,
                            centerMode: true,
                            centerPadding: '60px',


                        }
                    }
                ]


            }
        );
    });

</script>

<script>
    window.onload = function () {
        try {
            if (performance.navigation.type == 2) {
                location.reload(true);
            }
        } catch (e) {

        }
        /*<![CDATA[*/
        var operator_id = null;
        var gameCategories = ['most_popular','action','adventure','arcade','puzzles','sports_racing','strategy'];
        var add_fav_label = 'Add to Favourites';
        var view_details_label = 'View Details';
        var leader_board_label = 'Leaderboard';

        var cdnpath = 'prod_v1/static';
        /*]]>*/
        getOperatorBannerData(operator_id);
        for (var i = 0; i < gameCategories.length; i++) {
            loadGames(gameCategories[i], add_fav_label, view_details_label, leader_board_label);
        }
        $(".heroBanner").click(function () {
            var clickedbanner = $(this).attr("id");
            ga('send', 'event', 'HeroBanner', 'Click', clickedbanner);

        });

        //sliderinit();
    }

    function sliderinit(elementId) {
        $('#' + elementId).slick({
            infinite: false,
            slidesToShow: 7,
            slidesToScroll: 6,
            arrows: false,
            swipe: true,
            responsive: [
                {
                    breakpoint: 1024,
                    settings: {
                        infinite: false,
                        slidesToShow: 6,
                        slidesToScroll: 5,
                        arrows: false,
                        swipe: true
                    }
                },
                {
                    breakpoint: 840,
                    settings: {
                        infinite: false,
                        slidesToShow: 5,
                        slidesToScroll: 4,
                        arrows: false,
                        swipe: true
                    }
                },

                {
                    breakpoint: 620,
                    settings: {
                        infinite: false,
                        slidesToShow: 4,
                        slidesToScroll: 3,
                        arrows: false,
                        swipe: true
                    }
                },

                {
                    breakpoint: 480,
                    settings: {
                        infinite: false,
                        slidesToShow: 3,
                        arrows: false,
                        slidesToScroll: 2,
                        swipe: true


                    }
                }
            ]
        });
    }

    $(document).ready(function () {
        console.log("OnReady Called");
        jQuery("#search-box-slidebar").val("");
    });
</script>

<script>

    /*<![CDATA[*/
    var lang = 'en';

    /*]]>*/

    function getOperatorBannerData(operator_id) {
        var url = window.location.host;
        url = url.substring(url.indexOf('.') + 1);
        url = 'http://' + url;
        //var url='http://127.0.0.1:8082';
        $.ajax({
            xhrFields: {
                withCredentials: true
            },
            url: url + "/" + operator_id + "/getData",
            type: 'POST', async: true, crossDomain: true,
            data: {"key": "OPERATOR_LANDING_PAGE_BANNER_TXT"},
            success: function (data) {
                var responseCode = data.header.code;
                if (responseCode == '200') {
                    /*if(operator_id == "HUTCHID"){
                       // $('#operator_banner_txt').addClass("p16");
                        var mobileNo =  getCookie("X-Reycreo-Id");
                        var msisdn = mobileNo.split("PH");
                        $('#operator_banner_txt').html('<label class="hutchLandingPageLabel">Hai '+msisdn[1]+', Main game online 30 hari hanya Rp. 5,000</label><a href="https://bit.ly/2LlV0CT" target="_blank" class="btn btn-raised btn-xs btn-danger" class="hutchLandingPageActivateButton">Aktifkan sekarang</a>');
                    } else {*/
                    $('#operator_banner_txt').addClass("p16");
                    $('#operator_banner_txt').html(data.body)
                    //}
                }

            },
            error: function (data) {

            }
        });
    }

    function loadGames(category, add_fav_label, view_details_label, leader_board_label) {

        var div_game = document.getElementById("div_game_" + category);
        $.get("games/" + category + "?num=6", function (data, status) {
            if (data.status == "OK") {
                div_game.innerHTML = '';
                for (var i = 0; i < data.gameCount; i++) {
                    var game = data.games[i];
                    var imgSrc = game.thumbnailImageUrl;
                    var name = game.gameDetails[0].gameTitle;
                    var gameId = game.id;
                    var categoryName = game.gameCategory.gameCategoryDetails[0].categoryName;
                    var target_url = "/user/playgame/" + gameId;
                    var target = "";
                    var display_none = "";
                    var additionalClass = "";
                    if (game.gameType == "ANDROID") {
                        target_url = game.gameUrl;
                        target = "_blank";
                        display_none = "display:none; ";
                        additionalClass = " circular-image ";
                    }


                    var game_div_html = document.createElement('div');

                    game_div_html.className = 'col-5 col-xs-5 col-sm-5 col-md-3 col-lg-2 col-xl-2';


                    game_div_html.innerHTML = "<div class=\"col-xs-12 col-sm-12 col-md-12 padding0\">\n" +
                        "                        <a target=\"" + target + "\" href=\"" + target_url
                        + "\" ><img class=\"" + additionalClass + "imgCust\" src=\"" + imgSrc + "\" async></a>\n" +
                        "                        \n" +
                        "                    </div>\n" +
                        "                    <div class=\"col-xs-12 col-sm-12 col-md-12 padding0\" style=\"margin-top:8px;\">\n" +
                        "                        <div class=\"col-xs-12 col-sm-12 col-md-12 padding0\" style='display: flex;'>\n" +
                        "<div style=\"width: 85%;overflow: hidden;text-overflow: ellipsis;\"><a target=\"" + target + "\" href=\"" + target_url + "\" >" +
                        "                            <span class=\"gameTitle\">" + name + "</span>" +
                        "</a></div><div style=\" width:15%;\"><div class=\"dropdown \"                             style=' " +
                        display_none +
                        "position:absolute;right:0px;margin-top:-2px;'>\n" +
                        "                                <a class=\"dropdown-toggle\" data-toggle=\"dropdown\"\n" +
                        "                                   aria-haspopup=\"true\" aria-expanded=\"false\">\n" +
                        "                                    <i class=\"zmdi zmdi-more-vert\"></i>\n" +
                        "                                </a>\n" +
                        "                                <ul class=\"dropdown-menu dropdown-menu-right\" " +
                        "                                    <li><a class=\"fs12 dropdown-item\" href=\"\\game\\" + gameId + "\"\n" +
                        "                                       >    " + view_details_label + "</a></li>\n" +
                        "                                    <li><a class=\"fs12 dropdown-item\" href=\"\\leaderboard\\" + gameId + "\"" +
                        "                                          >" + leader_board_label + "</a></li>\n" +
                        "                                    <li><a class=\"fs12 dropdown-item\" data-gameid=\"" + gameId
                        + "\" href=\"javascript:void(0)\"\n" +
                        "                                          onclick=\" return addToFav(this);\" >" +
                        add_fav_label +
                        "</a></li>\n" +
                        "                                </ul>\n" +
                        "                            </div></div>" +
                        "                        </div>\n" +
                        "                    </div>\n";

                    //var game = data.games[i];
                    //alert(game.gameDetails[0].gameTitle);

                    div_game.appendChild(game_div_html);
                }
                //sliderinit("div_game_"+category);

            }

            //alert("Data: " + data + "\nStatus: " + status);


        });


    }


</script>


</body>
</html>
