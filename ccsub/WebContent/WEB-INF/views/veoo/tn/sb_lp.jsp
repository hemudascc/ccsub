<%@ page contentType="text/html; charset=UTF-8" %>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width initial-scale=1">
<title>Siren Box</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/stylesheet.css">
</head>
<body>


	<div id="main">
    	<h3>Suscripcion Diaria con Renovacion Automatica 7 dias a la Semana. Costo Contenido Diario U$0.41 ISV incluido.</h3>
    	<div class="banner"><img 
    	src="${pageContext.request.contextPath}/resources/images/veoo/siren_box_banner.jpg"></div>
        <div class="buttonbar">
                 <form id="form-id" 
                 action="${pageContext.request.contextPath}/cnt/veoo/lpform" method="post">
                                 <input type="hidden" name="param1" value="" />
                                 <input type="hidden" value="${token}"  name="token">
			                     <input type="hidden" value="${msisdn}"  name="msisdn">
                                 <br>
                                <div class="button">
<!--                                 <a href="#" onclick="document.getElementById('form-id').submit();">SUSCRIBETE</a> -->
                                <a href="sms:${veooServiceConfig.shortCode}?body=${veooServiceConfig.keyword}">SUSCRIBETE</a>
                                
                                </div>

                        </form>
            <div class="input"><input name="" checked type="checkbox" value=""> Acepto los Terminos y Condiciones del Servicio
</div>
			<h2><a href="${pageContext.request.contextPath}/cnt/veoo/termcondtion?id=${veooServiceConfig.id}">Términos y condiciones</a></h2 >
            <p class="terms">Suscripción diaria con renovación automática. Usted acuerda suscribirse al servicio al enviar Bebe al corto 4020 o al dar click en CONTINUAR. Costo de Contenido U$0.41 ISV incluido por Msj diario. Los costos de facturación se cargaran a su cuenta de teléfono móvil o se deducirán de su saldo. Para utilizar el servicio usted debe ser el abonado/Titular de la cuenta y tener al menos 18 años de edad o tener permiso explícito del abonado. Para Cancelar envía SALIR Bebe al 4020. Los operadores móviles no son responsables de este servicio, ni del contenido, ni de la publicidad del mismo. Para mas Información contactarnos en http://imobile.com.hn/sl/hn 
</p>
        </div>
    </div>
</body>
</html>
