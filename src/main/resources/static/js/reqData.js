var assertionChallenge = {
    	op: '',
    	challenge: ''
    };

var challenge = {
    displayName: '',
    username: '',
    attestation: '',
    authenticatorSelection: '',
    userVerification: ''
};

var authenticatorSelection = {
    requireResidentKey: '',
    authenticatorAttachment: '',
    userVerification: ''
}

var publicKeyCredential = {
    id: '',
    type: '',
    rawId: '',
    response: '',
    extension: ''
}

var publicKeyCredential = {
    id: '',
    type: '',
    rawId: '',
    response: '',
    extension: ''
}

var authenticatorAttestationResponse = {
    clientDataJSON: '',
    attestationObject: ''
}

var authenticatorAssertionResponse = {
    clientDataJSON: '',
    authenticatorData: '',
    signature: '',
    userHandle: ''
}

var fido2Data = {
	op: '',
	context: ''
};

var fido2Context = {
	rpContext: '',
	fidoContext: ''
};

var fidoContext = {
    id: '',
    type: '',
    rawId: '',
    response: '',
};

var rpContext = {
	rpId: '',
	userId: '',
	sessionId: ''
};

var clientData = {
	type: '',
	challenge: '',
	origin: '',
	hashAlgorithm: '',
	tokenBindingId: ''
};

var rpEntity = {
	name: '',
	icon: '',
	id: ''
};

var userEntity = {
	name: '',
	icon: '',
	id: '',
	displayName: ''
};


var credTypesAndPubKeyAlg = {
	type: '',
	alg: 0
};


var makeCredential = {
	clientData: '',
	rpEntity: '',
	userEntity: '',
	credTypesAndPubKeyAlgs: []
}; 

var verifyAssertion = {
	clientData: '',
	rpEntity: ''
};

var returnParam = {
	statusCode: 0,
	errorCode: '',
	message: ''
}; 

