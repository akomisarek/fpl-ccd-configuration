const config = require('../config.js');

Feature('Smoke tests @smoke-tests');

Scenario('Sign in as local authority', (I, loginPage) => {
  loginPage.signIn(config.smokeTestLocalAuthorityEmail, config.smokeTestLocalAuthorityPassword);
  I.see('Create new case');
  I.signOut();
});
