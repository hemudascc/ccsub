<%@ page contentType="text/html; charset=UTF-8" %>
<!doctype html>

<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width initial-scale=1">
<title>GamePad</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/stylesheet.css">
 <script type="text/javascript">
//     function ShowHideDiv(chkClick) {
//         var dvClick = document.getElementById("dvclick");
//         var dvClick2 = document.getElementById("dvclick2");

//         dvClick.style.display = chkClick.checked ? "block" : "none";
//         dvClick2.style.display = chkClick.checked ? "none" : "block";
//     }
</script>

</head>
<body>
        <div id="main">
        <h3>Suscripcion Diaria con Renovacion Automatica 7 dias a la Semana. Costo Contenido Diario Ȼ228 IVA incluido.</h3>
        <div class="banner"><img src="${pageContext.request.contextPath}/resources/images/veoo/game_pad_banner.jpg"></div>
        <div class="buttonbar">



        <!--    <p class="text">Por favor ingrese su número de móvil.</p> -->
                 <!--<form id="form-id" action="/vigwap/oth/firstconsent.jsp?portalid=398&catid=517&bcatid=325&chid=2&productid=112709" method="post">-->
                 <form id="form-id" action="${pageContext.request.contextPath}/cnt/veoo/riacostalp2" method="post">
                                <input type="hidden" name="param1" value="" />
                                <input type="hidden" value="${token}"  name="token">
			                    <input type="hidden" value="${msisdn}"  name="msisdn">
                                <br>
                                <div class="button" id="dvclick" >
                                 <a href="#" onclick="document.getElementById('form-id').submit();">SUSCRIBETE</a>
<%--                                 	<a href="sms:${veooServiceConfig.shortCode}?body=${veooServiceConfig.keyword}">SUSCRIBETE</a> --%>
                                </div>
                                
                        </form>
            <div class="input"><input name="" checked type="checkbox" value="" onclick="ShowHideDiv(this)"> Acepto los Terminos y Condiciones del Servicio</div>


                        <h2><a href="${pageContext.request.contextPath}/cnt/veoo/termcondtion?id=${veooServiceConfig.id}">Términos y condiciones</a></h2 >
            <p class="terms">
	Servicio de Suscripción Diaria con renovación automática. Usted acuerda suscribirse al servicio al enviar JUEGOS al corto 1244. Costo del Contenido Ȼ228 para usuarios ICE con impuestos incluidos, los cuales serán facturados a su servicio celular Kölbi o deducidos de su saldo. Para utilizar el servicio usted debe ser el cliente de Kölbi y tener al menos 18 años de edad o tener permiso explícito del dueño de la linea celular. Para cancelar la suscripción envía un SMS con la palabra SALIR al 1244. Al suscribirse está dando su consentimiento a los términos y condiciones, usted reconoce que es residente en Costa Rica, tiene más de 18 años y es titular del servicio telefonico Kölbi o tiene permiso del titular de Kölbi. Kölbi no se hace responsable de este servicio, ni del contenido, ni de la publicidad del mismo. Para obtener más información favor ingresar a www.interacelsl.com o llamar al 800-INTERCR (800-468-3727)
</p>
        </div>


    </div>
</body>
</html>

