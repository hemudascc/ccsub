<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <title>Go Games</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
 <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/mkhongkong/css/main.css">

   
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

<script>
$(document).ready(function() {
	  function setHeight() {
	    windowHeight = $(window).innerHeight();
	    $('#conatiner').css('min-height', windowHeight);
	  };

	});
	function check(){
			document.getElementById("frm").submit();
			return false;
		return true;
	}
</script>
  </head>
<body>
    <div class="container img-container">
        <img class="center-block img-rounded img-banner" src="${pageContext.request.contextPath}/resources/mkhongkong/image/banner.jpg"/>
    </div>
    <div class="jumbotron">
    <form id="frm" action="${pageContext.request.contextPath}/cnt/mkhk/lp-2"  >
	    <label class="enter-mob-label">Enter your mobile number to download</label>
        <input type="text" placeholder="Mobile number" id = "msisdn" name="msisdn" class="form-control mobile-input">
        <input type="hidden" name="token" value="${token}" >
        <select id="telcoid" name="opid" class="form-control operator-input">
            <option hidden>Select Operator</option>
            <option value="1">HUTCHISON</option>
            <option value="4">Smartone</option>
        </select>
        <!-- <div><input type="checkbox" id="tc-confirm"> I've read and accept the <span style="color:red">Terms & Condition</span></div> -->
        <button id = "btn"  type = "button" class="btn btn-custom btn-primary" data-toggle="modal" data-target="#confirm-modal">Confirm</button>
        <div class="price-info-container">
            <p>
                <b>
             HK$10 per SMS, max 6 SMS per week + one time signup fee HK$30
             (6*HK$10 SMS). Max HK$90/week. (signup fee is only for first time
              registration)
                </b>
            </p>
        </div>
        </form>
	</div>
    <div class="footer">
    <div class="dsc-container">
        <span>The cost of the service is HK$10 per received content SMS. You will receive a maximum of 6 content SMS per week until you send GG OFF to 503230. A one time off service signup fee of HK$30 will be applied when joining the service, Max HK$90/week.</span>
        If you do not send GG OFF to the 503230, the service will be renewed automatically. In addition, some operators may charge HK$0.5-HK$1.0 for every SMS sending to 503230. Additional costs for text messages and/or data transfer may be charged by your mobile phone operator or provider. By registration to this service, you agree to all applicable terms and conditions of the Kido Kingdom and your mobile phone operator. Please ensure your phone is WAP enabled. Please submit your mobile number on this page to register. You will receive a free message with instructions on how to complete the order of the service in which you are granted access to our content portal. You will receive 6 content SMS per week during each week of the subscription. All messages and content will be provided to you over your mobile phone operator network. The Kido Kingdom is not associated, sponsored, or endorsed by any of the listed products or retailers. Trademarks, service marks, logos are the property of their respective owners. Any tests, games, and/or applications featured on this page are for entertainment purposes only. Additional SMS- and/or WAP charges may apply.
        <span>The expiry date will be 31/12/2020, the max charge is HK$ 3210. The first week: HK$ 90 includes a signup fee. Starting from 2nd week to 52nd week, HK$ 60 per week.</span>
        service: care@collectcent.com, tel: MKCS 29899154 Open Mon-Fri 9.00-18.00.
        <br>
        <br>
      	<!-- 服务费为每条接收的内容短讯港币10元。<span>在您将GG OFF发送给503230之前，您每周将最多收到6条内容短信。加入该服务时，将收取一次HK$30的一次性服务注册费，每周最高HK$90。</span>如果您没有将GG OFF发送给503230，该服务将自动更新。此外，某些运营商可能会向发往503230的每条短信收取HK$0.5-HK$1.0的费用。您的手机运营商或提供商可能会收取短信和/或数据传输的额外费用。通过注册此服务，您同意Kido Kingdom和您的手机运营商的所有适用条款和条件。请确保您的手机已启用WAP。请在此页面上提交您的手机号码进行注册。您将收到一条免费消息，其中包含有关如何完成授予您访问我们的内容门户的服务顺序的说明。在订阅的每个星期中，您每周将收到6条内容SMS。所有消息和内容都将通过手机运营商网络提供给您。 Kido王国不受任何所列产品或零售商的关联，赞助或认可。商标，服务标志，徽标是其各自所有者的财产。此页面上的任何测试，游戏和/或应用程序仅出于娱乐目的。可能需要支付额外的SMS和/或WAP费用。<span>届满日期为31/12/2020，最高收费为HK$3170。第一周：HK$ 90包含注册费。第2周至第52周开始，每周60港元。</span>年龄：仅18岁以上-请获得付款人的许可。客户服务：care@collectcent.com，电话：MKCS 29899154，周一至周五9.00-18.00。 -->
      	服务费为每条接收的内容短讯港币10元。<span>在您将  GG&nbspOFF 发送给503230之前，您每周将最多收到6条内容短信。加入该服务时，将收取一次HK$30的一次性服务注册费，每周最高HK$90。</span>如果您没有将 GG&nbspOFF 发送给503230，该服务将自动更新。此外，某些运营商可能会向发往503230的每条短信收取HK$0.5-HK$1.0的费用。您的手机运营商或提供商可能会收取短信和/或数据传输的额外费用。通过注册此服务，您同意 Kido&nbspKingdom 和您的手机运营商的所有适用条款和条件。请确保您的手机已启用WAP。请在此页面上提交您的手机号码进行注册。您将收到一条免费消息，其中包含有关如何完成授予您访问我们的内容门户的服务顺序的说明。在订阅的每个星期中，您每周将收到6条内容SMS。所有消息和内容都将通过手机运营商网络提供给您。 Kido王国不受任何所列产品或零售商的关联，赞助或认可。商标，服务标志，徽标是其各自所有者的财产。此页面上的任何测试，游戏和/或应用程序仅出于娱乐目的。可能需要支付额外的SMS和/或WAP费用。<span>届满日期为31/12/2020，最高收费为HK$3210。第一周： HK$&nbsp90 包含注册费。第2周至第52周开始，每周60港元。</span>年龄：仅18岁以上-请获得付款人的许可。客户服务： care@collectcent.com，电话 ：MKCS 29899154，周一至周五9.00-18.00。
    </div>
    <div class="copyright">
        ©&nbsp;2020 go game &nbsp;&nbsp;All Rights Reserved
    </div>
    </div>
    
    <!-- The Modal -->
  <div class="modal" id="confirm-modal">
    <div class="modal-dialog">
      <div class="modal-content">
        <!-- Modal Header -->
        <div class="modal-header text-center">
          <h6 class="modal-title w-100">
          	ACCEPT THE SERVICE TERMS &
            <br>
            CONDITIONS FOR PLAY GAMES
            <br>
       接受服务条款和
游戏条件
          </h6>
          <button type="button" class="close" data-dismiss="modal">&times;</button>
        </div>
        
        <!-- Modal body -->
        <div class="modal-body content-div">
            <div>
            Go Games service is provided to you by Collectcent Digital Media Private Limited.
            <br>
            Collectcent Digital Media Private Limited向您提供Go Games服务。
            </div>
            <div>
                By accessing the Go Games you agree on your own behalf and on behalf of each entity and person on whose behalf you act to be bound by these terms and conditions ("Terms and Conditions").
                <br>
                通过访问Go Games，您代表您自己，并且代表您代表您受这些条款和条件（“条款和条件”）约束的每个实体和个人。
            </div>
            <div>
                The terms and conditions of your mobile service provider will continue to apply when accessing the Go Games in addition to these Terms and Conditions.
                <br>
                除这些条款和条件之外，访问Go Games时，您的移动服务提供商的条款和条件将继续适用。
            </div>
            <div>
                The Go Games may be accessed by WAP-enabled handsets. If your handset is not WAP-enabled you will not be able to access this service.
                <br>
                支持WAP的手机可以访问Go Games。 如果您的手机未启用WAP，则将无法访问该服务。
            </div>
            <div>
                Collectcent will not charge users for accessing the Go Games. However your Operator may charge you for using the mobile internet. What you pay is dependent on your Operator's own data charges and the amount that you use the service.
                <br>
                Collectcent不会向用户收取访问Go Games的费用。 但是，您的运营商可能会向您收取使用移动互联网的费用。 您所支付的费用取决于运营商自己的数据费用以及您使用服务的金额。
            </div>
            <div>
                If you are not the bill payer for the mobile phone, you must get permission from the bill payer before accessing the Go Games and downloading Content from the Go Games.
                <br>
              	如果您不是手机的付款人，则必须先获得付款人的许可，然后才能访问Go Games并从Go Games下载内容。
            </div>
            <div>
                Collectcent may suspend, change, withdraw or cancel the Go Games for any reason and at any time.
                <br>
                Collectcent可以随时出于任何原因暂停，更改，退出或取消Go Games。
            </div>
            <div>
                In the event of any dispute regarding the Go Games, the decision of Go Games shall be binding and no correspondence or discussion shall be entered into.
                <br>
                如有任何关于Go Games的争议，Go Games的决定具有约束力，且不得进行任何通信或讨论。
            </div>
            <div>
                Collectcent reserves the right to amend these Terms and Conditions without prior notice. Any changes will be posted on this website. Your continued use of the Go Games after changes are posted constitutes your acceptance of these Terms and Conditions as modified.
                <br>
                Collectcent保留修改这些条款和条件的权利，恕不另行通知。 任何更改都将发布在此网站上。 在发布更改后，您继续使用围棋游戏即表示您接受经修改的这些条款和条件。
            </div>
            <div>
                You agree to indemnify and keep indemnified Go Games from and against all claims, damages, expenses, costs and liabilities arising in any manner from your use of the Games or Content other than in accordance with these Terms and Conditions. Go Games may prevent your access to the Games and/or the Content if you breach these Terms and Conditions.
                <br>
                您同意对Go Games进行赔偿，并使之免受因您使用游戏或内容而以任何方式引起的所有索赔，损害，费用，成本和责任，并且不遵守这些条款和条件。 如果您违反这些条款和条件，Go Games可能会阻止您访问游戏和/或内容。
            </div>
            <div>
                In case of any concerns, reach us at care@collectcent.com
                <br>
                如有任何疑问，请通过care@collectcent.com与我们联系。
            </div>
        </div>
        
        <!-- Modal footer -->
        <div class="modal-footer text-center">
          <a type="button" class="btn btn-success" href="sms:32324?body=ON GA ${token}"  onclick="return check();">YES</a>
          <button type="button" class="btn btn-danger" data-dismiss="modal">NO</button>
        </div>
        
      </div>
    </div>
  </div>
    
</body>
</html>
