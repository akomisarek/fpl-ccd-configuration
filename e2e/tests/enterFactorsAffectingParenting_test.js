const config = require('../config.js');

Feature('EnterFactorsAffectingParenting');

Before((I, caseViewPage) => {
  I.logInAndCreateCase(config.swanseaLocalAuthorityEmailUserOne, config.localAuthorityPassword);
  caseViewPage.goToNewActions(config.applicationActions.enterFactorsAffectingParenting);
});

Scenario('Complete half the factors affecting parenting section of the c110a' +
  ' application', (I, enterFactorsAffectingParentingEventPage, caseViewPage) => {
  enterFactorsAffectingParentingEventPage.completeAlcoholOrDrugAbuse();
  I.continueAndSave();
  I.seeEventSubmissionConfirmation(config.applicationActions.enterFactorsAffectingParenting);
  caseViewPage.selectTab(caseViewPage.tabs.legalBasis);
  I.seeAnswerInTab(1, 'Factors affecting parenting', 'Alcohol or drug abuse', 'Yes');
  I.seeAnswerInTab(2, 'Factors affecting parenting', 'Give details', 'mock reason');
});

Scenario('Filling in factors affecting parenting sections of c110a', (I, enterFactorsAffectingParentingEventPage, caseViewPage) => {
  enterFactorsAffectingParentingEventPage.completeAlcoholOrDrugAbuse();
  enterFactorsAffectingParentingEventPage.completeDomesticViolence();
  enterFactorsAffectingParentingEventPage.completeAnythingElse();
  I.continueAndSave();
  I.seeEventSubmissionConfirmation(config.applicationActions.enterFactorsAffectingParenting);
  caseViewPage.selectTab(caseViewPage.tabs.legalBasis);
  I.seeAnswerInTab(1, 'Factors affecting parenting', 'Alcohol or drug abuse', 'Yes');
  I.seeAnswerInTab(2, 'Factors affecting parenting', 'Give details', 'mock reason');
  I.seeAnswerInTab(3, 'Factors affecting parenting', 'Domestic violence', 'Yes');
  I.seeAnswerInTab(4, 'Factors affecting parenting', 'Give details', 'mock reason');
  I.seeAnswerInTab(5, 'Factors affecting parenting', 'Anything else', 'Yes');
  I.seeAnswerInTab(6, 'Factors affecting parenting', 'Give details', 'mock reason');
});
