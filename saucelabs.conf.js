// /* eslint-disable no-console */
const supportedBrowsers = require('./e2e/crossbrowser/supportedBrowsers.js');
const CONF = require('./e2e/config.js');
//const saucelabs_username = CONF.saucelabs.username;
//const saucelabs_accesskey = CONF.saucelabs.key;
//const saucelabs_tunnelId= 'reformtunnel';
//const saucelabs_username = 'username';
//const saucelabs_accesskey = 'privatekey';
//const saucelabs_username = 'SivaK';
//const saucelabs_accesskey = '65e1e5c6-ae4b-4432-9854-276fff0610d8';
//const saucelabs_browserName = 'chrome';
//process.env.URL = 'https://ccd-case-management-web-aat.service.core-compute-aat.internal';
const waitForTimeout = parseInt(CONF.saucelabs.waitForTimeout);
const smartWait = parseInt(CONF.saucelabs.smartWait);
const browser = process.env.SAUCE_BROWSER || CONF.saucelabs.browser;
const tunnelName = process.env.SAUCE_TUNNEL_IDENTIFIER || CONF.saucelabs.tunnelId;
console.log(process.env.SAUCE_USERNAME);
console.log(process.env.SAUCE_ACCESS_KEY);
const { config } = require('./codecept.conf');

//console.log('before', config);

const getBrowserConfig = (browserGroup) => {
  const browserConfig = [];
  for (const candidateBrowser in supportedBrowsers[browserGroup]) {
    if (candidateBrowser) {
      const desiredCapability = supportedBrowsers[browserGroup][candidateBrowser];
      desiredCapability.tunnelIdentifier = tunnelName;
      desiredCapability.tags = ['fpl'];
      browserConfig.push({
        browser: desiredCapability.browserName,
        desiredCapabilities: desiredCapability,
      });
    } else {
      console.error('ERROR: supportedBrowsers.js is empty or incorrectly defined');
    }
  }
  return browserConfig;
};


  delete config.helpers.Puppeteer;
config.helpers.WebDriverIO = {
  url: process.env.URL || CONF.e2e.frontendUrl,
  browser: browser,
  waitForTimeout: waitForTimeout,
  smartWait: smartWait,
  cssSelectorsEnabled: 'true',
  host: 'ondemand.eu-central-1.saucelabs.com',
  port: 80,
  region: 'eu',
  user: process.env.SAUCE_USERNAME || CONF.saucelabs.username,
  key: process.env.SAUCE_ACCESS_KEY || CONF.saucelabs.key,
  desiredCapabilities: {},
};
config.helpers.SauceLabsReportingHelper = {
  require: './e2e/helpers/SauceLabsReportingHelper.js',
};


// config.multiple.microsoft = {
//   browsers: getBrowserConfig('microsoft'),
// };

config.multiple.chrome = {
  browsers: getBrowserConfig('chrome'),
};
// config.multiple.firefox = {
//   browsers: getBrowserConfig('firefox'),
// };
// config.multiple.safari = {
//   browsers: getBrowserConfig('safari'),
// };

console.log('after', config);

exports.config = config;

