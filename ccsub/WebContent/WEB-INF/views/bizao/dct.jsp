<%@ page pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<!DOCTYPE html>

<html lang="en">
<head>

    
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

<div class="ms-preload" id="ms-preload">
    <div id="status">
        <div class="spinner">
            <div class="dot1"></div>
            <div class="dot2"></div>
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
                    <ul class="share-menu-list">
                        <li class="animated fadeInRight animation-delay-3">
                            <a class="btn-circle btn-google" href="javascript:void(0)">
                                <i class="zmdi zmdi-google"></i>
                            </a>
                        </li>
                        <li class="animated fadeInRight animation-delay-2">
                            <a class="btn-circle btn-facebook" href="javascript:void(0)">
                                <i class="zmdi zmdi-facebook"></i>
                            </a>
                        </li>
                        <li class="animated fadeInRight animation-delay-1">
                            <a class="btn-circle btn-twitter" href="javascript:void(0)">
                                <i class="zmdi zmdi-twitter"></i>
                            </a>
                        </li>
                    </ul>
                    <a class="btn-circle btn-circle-primary animated zoomInDown animation-delay-7" href="javascript:void(0)">
                        <i class="zmdi zmdi-share"></i>
                    </a>
                </div>
<a class="btn-circle btn-circle-primary no-focus animated zoomInDown animation-delay-8" data-target="#ms-account-modal" data-toggle="modal" href="javascript:void(0)">
                    <i class="zmdi zmdi-account"></i>
                </a>
                <form class="search-form animated zoomInDown animation-delay-9">
                    <input class="search-input" id="search-box" name="q" placeholder="Search..." type="text" />
                    <label for="search-box">
                        <i class="zmdi zmdi-search"></i>
                    </label>
                </form>

            </div>
        </div>
    </header>
<nav class="navbar navbar-expand-md navbar-static ms-navbar navbar-mode ms-navbar-white">
        <div class="container container-full">
            
    <div class="navbar-header">
        <a class="navbar-brand" href="#">
            <img alt="Creative Antenna Logo" class="reycreo-logo-mobile animated fadeInRight animation-delay-3" src="images/bizao/Game_Pad_Logo.png" style="margin: -20px;">
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
<div class="container" align="center">
    <form action="${pageContext.request.contextPath}/cnt/bz/" style="font-family: verdana; font-size: 14px;">
 
  Numero de mobile:
   <select>
  <c:forEach items="${listMsisdnPrefix}" var="mPrefix">
						 <c:choose>
                          <c:when test="${mPrefix eq bizaoConfig.msisdnPrefix}">
						  <option selected="${mPrefix eq bizaoConfig.msisdnPrefix}" value="${mPrefix}">${mPrefix}</option>
						  </c:when>
						 <c:otherwise>
						  <option  value="${mPrefix}">${mPrefix}</option>
						  </c:otherwise>
                        </c:choose>
	 </c:forEach>	
  </select>
<input type="text" name="msisdn"  title="Msisdn">
  </form>

</div>

<br>
<div class="container" align="center">
    <form action="/action_page.php" style="font-family: verdana; font-size: 14px;">
  Serive a desactiver: 
  <select>
  <option value="1">GamePad</option>
  <option value="2">Product 2</option>
  <option value="3">Product 3</option>
  </select>		
</form>

</div>


<br><br>
<div class="container">
    <h3 align="center" style="font-size: 18px; font-family: verdana;">š'il vous plaît désactiver avec soin</h3>
</div>

<br><br>
<div class="container" align="center">
  <button type="button" class="btn btn-outline-secondary btn-lg">Desactiver Maintenant</button>

</div>

<br><br>
<footer class="ms-footer" style="margin-bottom: 58px; background-color: #0ca7fa;">
        <div class="container">
        </div>
    </footer>

</body>
</html>