const config = require("./config.js");

Feature('LogIn');

Scenario('Login as Local Authority', (I, loginPage) => {
    loginPage.signIn(config.localAuthorityEmail, config.localAuthorityPassword);
    I.wait(10);
    I.click('Create new case');
    I.selectOption('jurisdiction', 'Public Law DRAFT');
    I.selectOption('case-type', 'Shared_Storage_DRAFT_v0.3');
    I.selectOption('event', 'Initiate Case');
    I.click('Start');
    I.wait(10);
    I.click('Submit');
    I.see('has been created', '.alert-message');
});
