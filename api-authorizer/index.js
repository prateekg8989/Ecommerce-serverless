const axios = require('axios');

exports.handler = async (event) => {
  console.log(event);
  const validateApiResponse = await axios("API_TO_VALIDATE_THE_REQUEST", {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': event.authorizationToken
    },
  });
  
  const resp = validateApiResponse.data;
  
  if(resp.statusCode == 200) {
    return generateAuthResponse(resp.body.userId, 'Allow', event.methodArn);
  } else {
    return generateAuthResponse(resp.body.userId, 'Deny', event.methodArn);
  }
};

function generateAuthResponse(principalId, effect, methodArn) {
    const policyDocument = generatePolicyDocument(effect, methodArn);

    return {
        principalId,
        policyDocument
    }
}
function generatePolicyDocument(effect, methodArn) {
    if (!effect || !methodArn) return null

    const policyDocument = {
        Version: '2012-10-17',
        Statement: [{
            Action: 'execute-api:Invoke',
            Effect: effect,
            Resource: methodArn
        }]
    };

    return policyDocument;
}