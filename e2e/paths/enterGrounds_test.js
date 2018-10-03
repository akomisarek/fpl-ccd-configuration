const config = require('../config.js');
const addEventDetails = require('../pages/createCase/addEventSummary');

Feature('EnterGrounds');

Before((I) => {
	I.logInAndCreateCase(config.localAuthorityEmail, config.localAuthorityPassword, config.eventSummary, config.eventSummary);
});

Scenario('Filling in grounds for application section of c110a', (I, caseViewPage, enterGroundsPage) => {
	caseViewPage.goToNewActions(config.applicationActions.enterGrounds);
	I.waitForElement('ccd-case-edit-page', 5);
	enterGroundsPage.enterThresholdCriteriaDetails();
	I.waitForElement('.check-your-answers', 5);
	addEventDetails.submitCase(config.eventSummary, config.eventDescription);
	I.waitForElement('.tabs', 5);
	I.see(`updated with event: ${config.applicationActions.enterGrounds}`);
});