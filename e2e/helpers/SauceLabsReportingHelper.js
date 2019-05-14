/* eslint-disable no-process-env, func-names */

const event = require('codeceptjs').event;
const container = require('codeceptjs').container;
const exec = require('child_process').exec;
//const CONF = require('../../e2e/config.js');
//const saucelabs_username = 'username';
//const saucelabs_accesskey = 'privatekey';
//const saucelabs_username = 'SivaK';
//const saucelabs_accesskey = '65e1e5c6-ae4b-4432-9854-276fff0610d8';
//const sauceUsername = process.env.SAUCE_USERNAME || CONF.saucelabs.username;
//const sauceKey = process.env.SAUCE_ACCESS_KEY || CONF.saucelabs.key;
//const browser = requiredValue(process.env.SAUCELABS_BROWSER, 'SAUCELABS_BROWSER');
//const saucelabsTunnelIdentifier = requiredValue(process.env.SAUCELABS_TUNNEL_IDENTIFIER, 'SAUCELABS_TUNNEL_IDENTIFIER');
const sauceUsername = requiredValue(process.env.SAUCE_USERNAME, 'SAUCELABS_USERNAME');
const sauceKey = requiredValue(process.env.SAUCE_ACCESS_KEY, 'SAUCELABS_ACCESS_KEY');

function requiredValue (envVariableValue, variableName) {
  if (envVariableValue && envVariableValue.trim().length > 0) {
    return envVariableValue.trim();
  } else {
    throw new Error(`${variableName} is a required environment variable, but wasn't set`);
  }
}

function updateSauceLabsResult(result, sessionId) {
  console.log('SauceOnDemandSessionID=' + sessionId + ' job-name=fpl-ccd-configuration'); /* eslint-disable-line no-console, prefer-template */
  return 'curl -X PUT -s -d \'{"passed": ' + result + '}\' -u ' + sauceUsername + ':' + sauceKey + ' https://eu-central-1.saucelabs.com/rest/v1/' + sauceUsername + '/jobs/' + sessionId;
}

module.exports = function() {

  // Setting test success on SauceLabs
  event.dispatcher.on(event.test.passed, () => {

    let sessionId = container.helpers('WebDriverIO').browser.requestHandler.sessionID;
    exec(updateSauceLabsResult('true', sessionId));

  });

  // Setting test failure on SauceLabs
  event.dispatcher.on(event.test.failed, () => {

    let sessionId = container.helpers('WebDriverIO').browser.requestHandler.sessionID;
    exec(updateSauceLabsResult('false', sessionId));

  });
};
