const config = require('../config.js');
let caseId;

Feature('Login as hmcts admin').retry(2);

Before(async (I, caseViewPage, submitApplicationPage) => {
  I.logInAndCreateCase(config.swanseaLocalAuthorityEmailUserOne, config.localAuthorityPassword);
  caseId = await I.grabTextFrom('.heading-medium');
  caseViewPage.goToNewActions(config.applicationActions.submitCase);
  submitApplicationPage.giveConsent();
  I.click('Continue');  
  I.click('Submit');
  I.waitForElement('.tabs');
  I.signOut();
});

Scenario('HMCTS admin can login and add a FamilyMan case number to a submitted case', (I, caseViewPage, loginPage, caseListPage, enterFamilyManPage) => {
  loginPage.signIn(config.hmctsAdminEmail, config.hmctsAdminPassword);
  I.navigateToCaseDetails(caseId);
  I.see(caseId);
  caseViewPage.goToNewActions(config.addFamilyManCaseNumber);
  enterFamilyManPage.enterCaseID();
  I.continueAndSubmit();
  I.seeEventSubmissionConfirmation(config.addFamilyManCaseNumber);
});
