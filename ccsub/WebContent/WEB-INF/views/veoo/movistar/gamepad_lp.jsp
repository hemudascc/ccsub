<%@ page contentType="text/html; charset=UTF-8" %>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width initial-scale=1">
<title>GamePad</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/stylesheet.css">
</head>
<body>


	<div id="main">
    	<h3>Suscripcion Diaria con Renovacion Automatica 7 dias a la Semana. Costo Contenido Diario U$0.23 IVA incluido.</h3>
    	<div class="banner"><img src="${pageContext.request.contextPath}/resources/images/veoo/game_pad_banner.jpg"></div>
        <div class="buttonbar">

                 <form id="form-id"  class="frm1" action="${pageContext.request.contextPath}/cnt/veoo/lpform" method="post">
                                 <input type="hidden" name="param1" value="" />
                                  <input type="hidden" value="${token}"  name="token">
			                    <input type="hidden" value="${msisdn}"  name="msisdn">
                                 <br>
                                <div class="button"><a href="#" onclick="document.getElementById('form-id').submit();">CONTINUAR</a></div>

                        </form>

            <div class="input"><input name="" checked type="checkbox" value=""> Acepto los Terminos y Condiciones del Servicio
</div>
			<h2><a href="${pageContext.request.contextPath}/cnt/veoo/termcondtion?id=${veooServiceConfig.id}">Términos y condiciones</a></h2 >
            <p class="terms">
            Al hacer click quedaras suscrito a Estrellas GamePad. Descubre increibles juegos en tu movil. Recibiras un mensaje diario. CstxDia $0.23 Imp Incluidos. Para cancelar envía BAJA al 2510. Atencion al cliente: www.interacel.com

</p>
        </div>
    
    
    </div>
</body>
</html>
