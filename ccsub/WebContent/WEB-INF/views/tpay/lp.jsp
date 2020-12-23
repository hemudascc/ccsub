<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<title>${TpayServiceConfig.serviceName}</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/tpay/css/tpay.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/tpay/js/tpay.js"></script>
<script src="${HEJSURL}"></script>
</head>
<body>
<!-- Hidden Parameters Start -->
<input id="tpay-service-config" value="${TpayServiceConfig}" type="hidden">
<input id="token" value="${token}" type="hidden">
<!-- Hidden Parameters End -->

<!-- Body Start -->
	<div class="img-div">
		<img src="${pageContext.request.contextPath}/resources/tpay/image/gamespark-banners.png" class="center-block img-rounded img-banner" alt="gamepad">
	</div>
	<div class="center-div">
		<h4>${TpayServiceConfig.price} ${TpayServiceConfig.currency}/${TpayServiceConfig.billingSequence}</h4>
		<button type="button" class="btn btn-lg center-block sub-btn" id="sub-button">
			Subscribe
		</button>
		<br>
	</div>
	<div>
		<ul>
			<li>You will subscribe in GamePad for ${TpayServiceConfig.price} ${TpayServiceConfig.currency}/${TpayServiceConfig.billingSequence}.</li>
			<li>To cancel your subscription, for ${TpayServiceConfig.country} ${TpayServiceConfig.operatorName} subscribers
				please send ${TpayServiceConfig.unsubKeyword} to ${TpayServiceConfig.shortCode}.</li>
			<li>For any inquires please contact us on
				tech.d2c@collectcent.com.</li>
			<li>Powered by collectcent</li>
		</ul>
	</div>
<!-- Body End -->

<!-- missing msisdn modal start -->
	 <div class="modal" id="missing-msisdn-modal">
    <div class="modal-dialog">
      <div class="modal-content">
     	<div class="modal-header">
          <h4 class="modal-title">${TpayServiceConfig.serviceName}</h4>
          <button type="button" class="close" data-dismiss="modal">&times;</button>
        </div>
        <div class="modal-body">
          <p id="message"></p>
          <label for="usr" id="msisdn-label">Mobile:</label>
          <label for="usr" id="pin-label">Pin:</label>
          <input type="text" class="form-control" value="" name="msisdn" id="msisdn" placeholder="Please Enter Mobile Number">
          <input type="text" class="form-control" value="" name="pin" id="pin" placeholder="Please Enter Receved Pin">
          <br>
          <div class="popup-button-group">
          <button class="btn btn-md center-block popup-button" id="send-pin">Subscribe</button>
          <button class="btn btn-md center-block popup-button" id="subscribe-pin">Subscribe</button>
          <button class="btn btn-md center-block popup-button" id="resend-pin">Resend Pin</button>
          </div>
          <br>
          	<div>
		<ul>
			<li>You will subscribe in GamePad for ${TpayServiceConfig.price} ${TpayServiceConfig.currency}/${TpayServiceConfig.billingSequence}.</li>
			<li>To cancel your subscription, for ${TpayServiceConfig.country} ${TpayServiceConfig.operatorName} subscribers
				please send ${TpayServiceConfig.unsubKeyword} to ${TpayServiceConfig.shortCode}.</li>
			<li>For any inquires please contact us on
				tech.d2c@collectcent.com.</li>
			<li>Powered by collectcent</li>
		</ul>
	</div>
        </div>
      </div>
    </div>
  </div>
<!-- missing msisdn modal end -->

<!-- pin validation modal start -->
	
<!-- pin validation modal end -->

</body>
</html>