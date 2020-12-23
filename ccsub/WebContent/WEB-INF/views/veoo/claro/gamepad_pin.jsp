<%@ page contentType="text/html; charset=UTF-8" %>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width initial-scale=1">
<title>GamePad</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/stylesheet.css">
<style>

.select-css {
	
	font-size: 16px;
	font-family: sans-serif;
	font-weight: 700;
	color: #444;
	line-height: 1.3;
	padding: .6em 1.4em .5em .8em;
	width: 10%;
	max-width: 10%;
	box-sizing: border-box;
	margin: 0;
	border: 1px solid #aaa;
	box-shadow: 0 1px 0 1px rgba(0,0,0,.04);
	border-radius: .5em;
	-moz-appearance: none;
	-webkit-appearance: none;
	appearance: none;
	background-color: #fff00;
	background-image: url('data:image/svg+xml;charset=US-ASCII,%3Csvg%20xmlns%3D%22http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg%22%20width%3D%22292.4%22%20height%3D%22292.4%22%3E%3Cpath%20fill%3D%22%23007CB2%22%20d%3D%22M287%2069.4a17.6%2017.6%200%200%200-13-5.4H18.4c-5%200-9.3%201.8-12.9%205.4A17.6%2017.6%200%200%200%200%2082.2c0%205%201.8%209.3%205.4%2012.9l128%20127.9c3.6%203.6%207.8%205.4%2012.8%205.4s9.2-1.8%2012.8-5.4L287%2095c3.5-3.5%205.4-7.8%205.4-12.8%200-5-1.9-9.2-5.5-12.8z%22%2F%3E%3C%2Fsvg%3E'),
	  linear-gradient(to bottom, #ffffff 0%,#e5e5e5 100%);
	background-repeat: no-repeat, repeat;
	background-position: right .7em top 50%, 0 0;
	background-size: .65em auto, 100%;
}
.select-css::-ms-expand {
	display: none;
}
.select-css:hover {
	border-color: #888;
}
.select-css:focus {
	border-color: #aaa;
	box-shadow: 0 0 1px 3px rgba(59, 153, 252, .7);
	box-shadow: 0 0 0 3px -moz-mac-focusring;
	color: #222;
	outline: none;
}
.select-css option {
	font-weight:normal;
}
Additionally, we recommend adding some rules for right-to-left language support, and a clear disabled state:


/* Support for rtl text, explicit support for Arabic and Hebrew */
*[dir="rtl"] .select-css, :root:lang(ar) .select-css, :root:lang(iw) .select-css {
	background-position: left .7em top 50%, 0 0;
	padding: .6em .8em .5em 1.4em;
}

/* Disabled styles */
.select-css:disabled, .select-css[aria-disabled=true] {
	color: graytext;
	background-image: url('data:image/svg+xml;charset=US-ASCII,%3Csvg%20xmlns%3D%22http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg%22%20width%3D%22292.4%22%20height%3D%22292.4%22%3E%3Cpath%20fill%3D%22graytext%22%20d%3D%22M287%2069.4a17.6%2017.6%200%200%200-13-5.4H18.4c-5%200-9.3%201.8-12.9%205.4A17.6%2017.6%200%200%200%200%2082.2c0%205%201.8%209.3%205.4%2012.9l128%20127.9c3.6%203.6%207.8%205.4%2012.8%205.4s9.2-1.8%2012.8-5.4L287%2095c3.5-3.5%205.4-7.8%205.4-12.8%200-5-1.9-9.2-5.5-12.8z%22%2F%3E%3C%2Fsvg%3E'),
	  linear-gradient(to bottom, #ffffff 0%,#e5e5e5 100%);
}
.select-css:disabled:hover, .select-css[aria-disabled=true] {
	border-color: #aaa;
}
   
.button {
	width:99%;
	max-width:280px;
	background:#009ecf;
	font-size:20px;
	color:#fff;
	padding:15px 20px;
	border:none;
	border-radius:7px;
	margin:10px auto;
	display:table;
}
input[type=text] {
  width: 80%;
  padding: 1px 1px;
  margin: 1px 0;
  box-sizing: border-box;
  font-size:30px;
}
/* input[type=select] { */
/*   width: 10%; */
/*   padding: 10px 10px; */
/*   margin: 1px 0; */
/*   box-sizing: border-box; */
/*   font-size:30px; */
/* } */

.terms{
	padding:0 20px;
}
.terms h3{
	margin-bottom:0px;
	font-size:17px;
	font-weight:500;
	color:#898989;
}
.terms p{
	font-size:14px;
	color:#898989;
	margin-bottom:5px;
}
 .priceText{
 display:block;
	text-align:center;
	font-size:14px;
	font-weight:200;
	color:#636365;
}
.input{
	border:2px solid #ababab;
	padding:7px 8px;
	font-size:18px;
	margin:15px auto 5px;
	color:#898989;
}


input[type=image] {
 max-width: 50%; 
 width: auto;
 height: 30%;
}


.button {
  padding: 11px 25px;
  font-size: 24px;
  text-align: center;
  cursor: pointer;
  outline: none;
  color: #fff;
  background-color: #0ca7fa;
  border: none;
  border-radius: 15px;
  box-shadow: 0 9px #999;
  height: 50px;
}

.button:hover {background-color: #3e8e41}

.button:active {
  background-color: #3e8e41;
  box-shadow: 0 5px #666;
  transform: translateY(4px);
}

</style>

</head>
<body>


	<div id="main">
    	
    	<h3>Suscripcion Diaria con Renovacion Automatica 7 dias a la Semana. Costo Contenido Diario U$0.29 IVA incluido.</h3>
    	<div class="banner"><img src="${pageContext.request.contextPath}/resources/images/veoo/game_pad_banner.jpg"></div>
    	
    	  <center>   <p  style="text-align:center;color:red;">${sendinfo}</p></center>
        <div class="buttonbar">
              
        	 <form id="form-id" name="form-id" 
        	 action="${pageContext.request.contextPath}/cnt/veoo/send/pin" method="get">   				
   				  <input type="hidden" value="${token}"  name="token">
			      <input type="text" value="${msisdn}"  name="msisdn" placeholder="Ingrese su numero de telefono">
			      <br>  <br>
				<div class="">
				<input type="submit" value="Enviar PIN" class="button"/> 
				
				</div>
 			</form>
 			<br>
            <div ><input name="" checked type="checkbox" value=""> Acepto los Terminos y Condiciones del Servicio
            </div>
            
			        <h2>
                        <a href="${pageContext.request.contextPath}/cnt/veoo/termcondtion?id=${veooServiceConfig.id}">Términos y condiciones</a></h2 >
            <p class="terms">Suscripción diaria con renovación automática.
             Usted acuerda suscribirse al servicio al enviar CIELO al corto 1017 o
              al dar click en CONTINUAR. Costo de Contenido U$0.29 iva incluido por Msj
               diario. Los costos de facturación se cargaran a su cuenta de teléfono móvil 
               o se deducirán de su saldo. Para utilizar el servicio usted debe ser el 
               Abonado/Titular de la cuenta y tener al menos 18 años de edad o tener permiso
                explícito del abonado. Para Cancelar envía BAJA al 1017. Los operadores móviles 
                no se hacen responsables de este servicio, ni del contenido, ni de la publicidad 
                del mismo. Proveedor del servicio Interacel Nicaragua. Información 2277-3379 ext 107 
                o visitarnos en www.interacel.com

</p>
           
        </div>
    
    
    </div>
</body>
</html>
