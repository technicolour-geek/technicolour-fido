// var fido2Url = 'https://hancompass.koreacentral.cloudapp.azure.com:8443/xpass/fido'; // 등록, 인증의 공통 URL
// var fido2Url = 'https://192.168.60.65:8088/xpass/fido2'; // 등록, 인증의 공통 URL
var fido2Url = 'https://technicolourfido.azurewebsites.net'; // 등록, 인증의 공통 URL
var fido2ChallengeUrl = "/challengeService/getCredentialsChallenge";
var fido2RegistUrl = "/fidoService/registCredential";
var fido2AssertionChallengeUrl = "/challengeService/getAssertionChallenge";
var fido2AuthUrl = "/fidoService/verifyAssertion";
var fido2DeregUrl = "/DeregistrationService/deregistration"
var authName = 'Hancom Secure Authentication';
var currentOP; // 현재 선택된 Operation
var sessionId;
var hashAlg = 'sha-256';
/*
var $regBtn = $('#reg-btn');
var $authBtn = $('#auth-btn');
var $deregBtn = $('#dereg-btn');
*/
/*
// 등록,인증 버튼 클릭 이벤트리스너 등록
$('.op-btn').each(function () {
    $(this).click(function (e) {
        // 클릭한 버튼이 'Registration'일 경우
        if (this.id === 'reg-btn') {
            currentOP = 'reg';
            $regBtn.addClass('active');
            $authBtn.removeClass('active');
            $deregBtn.removeClass('active');
        }
        // 클릭한 버튼이 'Authentication'일 경우
        else if (this.id === 'auth-btn') {
            currentOP = 'auth';
            $regBtn.removeClass('active');
            $authBtn.addClass('active');
            $deregBtn.removeClass('active');
        }
        // 클릭한 버튼이 'Deregistration'일 경우
        else if (this.id === 'dereg-btn') {
            currentOP = 'dereg';
            $regBtn.removeClass('active');
            $authBtn.removeClass('active');
            $deregBtn.addClass('active');
        }
    })
})
*/

// 'fido2_reg_btn' 버튼 클릭 이벤트리스너 등록
$('#fido2_reg_btn').click(function (event) {
    var userName = $('#fido2_reg_username').val();
    currentOP = "reg";

    if($('#fido2_reg_username').val() == "" || $('#fido2_reg_username').val() == null){
        alert('User Name is Null.');
        return;
    }

    if($('#fido2_reg_displayName').val() == "" || $('#fido2_reg_displayName').val() == null){
        alert('User Display Name is Null.');
        return;
    }

    if (!currentOP) {
        alert('Select operation type.');
        return;
    }

    //통신
    challengeReqeust();
//        makeCredentialResponse('test');
//        verifyAssertionResponse('test');

    // 모달 호출
    showModal();
});

//$.getScript("./js/reqData.js", function() { console.log('reqData.js loading!!'); });
//$.getScript("./js/http.js", function() { console.log('http.js loading!!'); });

// 'fido2_auth_btn' 버튼 클릭 이벤트리스너 등록
$('#fido2_auth_btn').click(function (event) {
    var userName = $('#fido2_auth_username').val();
    currentOP = "auth";

    if($('#fido2_auth_username').val() == "" || $('#fido2_auth_username').val() == null){
        alert('User Name is Null.');
        return;
    }

    if($('#fido2_auth_displayname').val() == "" || $('#fido2_auth_displayname').val() == null){
        alert('User Display Name is Null.');
        return;
    }

    if (!currentOP) {
        alert('Select operation type.');
        return;
    }

    //통신
    challengeReqeust(currentOP);
//        makeCredentialResponse('test');
//        verifyAssertionResponse('test');

    // 모달 호출
    showModal();
});

// 모달의 'Cancel' 버튼의 클릭 이벤트리스너 등록
$('#cancel-btn').click(function () {
//        testSendAjax();
    hideModal();
})

/*
// 'reg' 버튼 클릭 이벤트리스너 등록
$('#reg-btn').click(function (event) {
  $('#attestationDiv').show();
  $('#requireResidentKeyDiv').show();
  $('#authenticatorAttachmentDiv').show();
  $('#userVerification').show();
});


// 'auth' 버튼 클릭 이벤트리스너 등록
$('#auth-btn').click(function (event) {
  $('#attestationDiv').hide();
  $('#requireResidentKeyDiv').hide();
  $('#authenticatorAttachmentDiv').hide();
  $('#userVerification').show();
});

// 'dereg' 버튼 클릭 이벤트리스너 등록
$('#dereg-btn').click(function (event) {
  $('#attestationDiv').hide();
  $('#requireResidentKeyDiv').hide();
  $('#authenticatorAttachmentDiv').hide();
  $('#userVerification').hide();
});
*/

function challengeReqeust() {
    var authLog, serverLog;

    // 입력값 추출
    var userName = $('#fido2_'+currentOP+'_username').val();
    var userDisplayName = $('#fido2_'+currentOP+'_displayName').val();
    var requireResidentKey = $('#fido2_requireResidentKey').val();
    var authenticatorAttachment = $('#fido2_authenticatorAttachment').val();
    var userVerification = $('#fido2_userVerification').val();
    var attestation = $('#fido2_attestation').val();
    var url;

	challenge.rpId = "technicolourfido.azurewebsites.net";
    challenge.username = userName;
    challenge.displayName = userDisplayName;

    if (currentOP === 'reg') {
        challenge.attestation = attestation;
        authenticatorSelection.userVerification = userVerification;
        authenticatorSelection.requireResidentKey = requireResidentKey;
        authenticatorSelection.authenticatorAttachment = authenticatorAttachment;

        challenge.authenticatorSelection = authenticatorSelection

        url = fido2Url + fido2ChallengeUrl;
    }
    else if (currentOP === 'auth') {
        challenge.userVerification = userVerification;

        url = fido2Url + fido2AssertionChallengeUrl;
    }
    else if (currentOP === 'dereg') {
      url = fido2Url + fido2DeregUrl;
    }

    challenge.authenticatorSelection = authenticatorSelection;

    var jsonChallenge = JSON.stringify(challenge);

    // POST 방식의 Ajax 요청
    hsecure.xpass.api.http.
        doAction(url, jsonChallenge,
        function onSuccess(response) {
            if(response.status !== "ok") {
                hideModal();
                alert("server error! : " + response.status);
                console.error('challenge Fail', response);
                printServerResult('FAIL', response.errorMessage);
                return;
            }

            console.log('Ajax Success', response);
            // 현재 선택된 오퍼레이션에 따라 URL 문자열 구성.
            if (currentOP === 'reg') {
                makeCredentialAuth('webauthn.create', response.challenge, response.rp, response.user, response.pubKeyCredParams, response.timeout, response.excludeCredentials, response.extensions, response.attestation, response.authenticatorSelection);
            } else if (currentOP === 'auth') {
                verifyAssertionAuth('webauthn.get', response.challenge, response.timeout, response.allowCredentials, response.extensions, response.userVerification);
            } else if (currentOP === 'dereg') {
                console.log(response);
                alert ("SUCCESS");
                printResult('SUCCESS');
                hideModal();
            }

        },
        function onError(response) {
            hideModal();
            alert("server error! : " + response.status);
            console.error('Ajax Fail', response);
            printServerResult('FAIL', response.errorMessage);
        });
}


//additional authenticator
function makeCredentialAuth(type, challenge, rp, user, pubKeyCredParams, timeout, excludeCredentials, extensions, attestation, authenticatorSelection) {
    if (excludeCredentials != null && excludeCredentials !== undefined && excludeCredentials.length != 0) {
        for (var i = 0; i < excludeCredentials.length; i++) {
            excludeCredentials[i].id = base64urlDecodeToArrayBuffer(excludeCredentials[i].id);
        }
    }

    var publicKey = {
        challenge: base64urlDecodeToArrayBuffer(challenge),
        rp: {
			id: rp.id,
            name: rp.name
        },
        user: {
            id: base64urlDecodeToArrayBuffer(user.id),
            name: user.name,
            displayName: user.displayName,
            icon: user.icon
        },
        pubKeyCredParams: pubKeyCredParams,
        timeout: timeout,
        excludeCredentials: excludeCredentials,
        extensions: extensions,
        attestation: attestation,
        authenticatorSelection: authenticatorSelection
    };

    navigator.credentials.create({ publicKey })
        .then(function (newCredentialInfo) {
        console.log("newCredentialInfo : "+newCredentialInfo);
            newCredentialRegistReqeust(newCredentialInfo);
        }).catch(function (err) {
            hideModal();
            console.error(err)
            alert(err);
            printAuthResult('FAIL', err);
        });
}

//verify assertion authentication
function verifyAssertionAuth(type, challenge, timeout, allowCredentials, extensions, userVerification){ // 웹에서 앱으로 보내는 함수
    if (allowCredentials != null && allowCredentials !== undefined && allowCredentials.length != 0) {
        for (var i = 0; i < allowCredentials.length; i++) {
            allowCredentials[i].id = base64urlDecodeToArrayBuffer(allowCredentials[i].id);
        }
    }

    var publicKey = {
        challenge: base64urlDecodeToArrayBuffer(challenge),
        timeout: timeout,
        allowCredentials: allowCredentials,
        extensions: extensions,
        userVerification: userVerification
    };

    navigator.credentials.get({ publicKey })
        .then(function (assertion) {
            verifyAssertionRequest(assertion);
        }).catch(function (err) {
            hideModal();
            console.error(err);
            alert(err);
            printAuthResult('FAIL', err);
        });
}

// Send new credential info to server for verification and registration.
 function newCredentialRegistReqeust(newCredentialInfo) {
    //TODO:doosik. arraybuffer to base64url string
    var rawId = base64urlEncodeFromArrayBuffer (newCredentialInfo.rawId);
    var attestationObject = base64urlEncodeFromArrayBuffer (newCredentialInfo.response.attestationObject);
    var clientDataJSON = base64urlEncodeFromArrayBuffer (newCredentialInfo.response.clientDataJSON);

    //TODO:doosik. set response data
    authenticatorAttestationResponse.attestationObject = attestationObject;
    authenticatorAttestationResponse.clientDataJSON = clientDataJSON;

    //TODO:doosik. set fidoContext
    fidoContext.id = newCredentialInfo.id;
    fidoContext.rawId = rawId;
    fidoContext.response = authenticatorAttestationResponse;
    fidoContext.type = newCredentialInfo.type;

    //TODO:doosik. set context
    fido2Context.fidoContext = fidoContext;
    fido2Context.rpContext = {};

    //TODO:doosik. set publicKeyCredential
    fido2Data.op = "REG";
    fido2Data.context = fido2Context;

    var url = fido2Url + fido2RegistUrl;
    var jsonPublicKeyCredential = JSON.stringify(fido2Data);

    hsecure.xpass.api.http.
        doAction(url, jsonPublicKeyCredential,
        function onSuccess(response) {
            hideModal();
            if(response.responseContext !== "Success") {
                alert("server error! : " + response.status);
                console.error('challenge Fail', response);
                printServerResult('FAIL', response.responseContext);
                return;
            }

            alert ("SUCCESS");
            printResult('SUCCESS');
            console.log('Ajax Success', response);
            // TODO : 성공 메시지 출력

        },
        function onError(response) {
            hideModal();
            alert("server error! : " + response.status);
            console.error('Ajax Fail', response);
            printServerResult('FAIL', response.responseContext);
        });
 }

// Send assertion to server for verification
function verifyAssertionRequest(assertion) {
    //TODO:doosik. arraybuffer to base64url string
    var rawId = base64urlEncodeFromArrayBuffer (assertion.rawId);
    var authenticatorData = base64urlEncodeFromArrayBuffer (assertion.response.authenticatorData);
    var clientDataJSON = base64urlEncodeFromArrayBuffer (assertion.response.clientDataJSON);
    var signature = base64urlEncodeFromArrayBuffer (assertion.response.signature);
    var userHandle = base64urlEncodeFromArrayBuffer (assertion.response.userHandle);

    //TODO:doosik. set response data
    authenticatorAssertionResponse.authenticatorData = authenticatorData;
    authenticatorAssertionResponse.clientDataJSON = clientDataJSON;
    authenticatorAssertionResponse.signature = signature;
    authenticatorAssertionResponse.userHandle = userHandle;

    //TODO:doosik. set fidoContext
    fidoContext.id = assertion.id;
    fidoContext.rawId = rawId;
    fidoContext.response = authenticatorAssertionResponse;
    fidoContext.type = assertion.type;

    //TODO:doosik. set context
    fido2Context.fidoContext = fidoContext;
    fido2Context.rpContext = {};

    //TODO:doosik. set assertionRequest
    fido2Data.op = "AUTH";
    fido2Data.context = fido2Context;

    var url = fido2Url + fido2AuthUrl;
    var jsonAssertionRequest = JSON.stringify(fido2Data);

    hsecure.xpass.api.http.
        doAction(url, jsonAssertionRequest,
        function onSuccess(response) {
            hideModal();
            if(response.responseContext !== "Success") {
                alert("server error! : " + response.status);
                console.error('challenge Fail', response);
                printServerResult('FAIL', response.responseContext);
                return;
            }

            alert ("SUCCESS");
            printResult('SUCCESS');
            console.log('Ajax Success', response);
            // TODO : 성공 메시지 출력
        },
        function onError(response) {
            hideModal();
            alert("server error! : " + response.status);
            console.error('Ajax Fail', response);
            printServerResult('FAIL', response.responseContext);
        });
 }

// 'status', 'Auth Log', 'Server Log' 화면에 출력5
function printAuthResult(result, authLogMsg) {
    var statusMsg = result === 'SUCCESS' ? 'Success' : 'Fail';

    $('#status').text(statusMsg);
    $('#authLog').val(authLogMsg);
    $('#serverLog').val("");
}

// 'status', 'Auth Log', 'Server Log' 화면에 출력5
function printServerResult(result, serverLogMsg) {
    var statusMsg = result === 'SUCCESS' ? 'Success' : 'Fail';

    $('#status').text(statusMsg);
    $('#serverLog').val(serverLogMsg);
    $('#authLog').val("");
}

// 'status', 'Auth Log', 'Server Log' 화면에 출력5
function printResult(result) {
    var statusMsg = result === 'SUCCESS' ? 'Success' : 'Fail';

    $('#status').text(statusMsg);
    $('#authLog').val(statusMsg);
    $('#serverLog').val(statusMsg);
}

// Show modal
function showModal() {
    $('#progressModal').modal('show');
}

// Hide modal
function hideModal() {
    $('#progressModal').modal('hide');
}

//TODO Option
function rand(length, current) {
  current = current ? current : '';
  return length ? rand(--length, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz".charAt(Math.floor(Math.random() * 60)) + current) : current;
}

function base64urlEncodeFromArrayBuffer(arraybuffer) {
    var data = String.fromCharCode.apply(null, new Uint8Array(arraybuffer));
    var base64Data = window.btoa(data);

    base64Data = base64Data.replaceAll('+', '-');
    base64Data = base64Data.replaceAll('/', '_');
    base64Data = base64Data.replaceAll('=', '');
    return base64Data;
}

function base64urlDecodeToArrayBuffer(data) {
    data = data.replaceAll('-', '+');
    data = data.replaceAll('_', "/");

    return Uint8Array.from(window.atob(data), c=>c.charCodeAt(0));
}

//TODO: doosik. String replaceAll 추가. 함수 이름 충돌이 발생할 수 있으니 함수 이름/사용법 변경 필요.
String.prototype.replaceAll = function(target, replacement) {
  return this.split(target).join(replacement);
};