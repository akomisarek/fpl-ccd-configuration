const config = require('../config.js');

Feature('Select hearing');

Before((I, caseViewPage) => {
  I.logInAndCreateCase(config.swanseaLocalAuthorityEmailUserOne, config.localAuthorityPassword);
  caseViewPage.goToNewActions(config.applicationActions.selectHearing);
});

Scenario('completing half the fields in the Select hearing section of the c110a application', (I, caseViewPage, enterHearingNeededEventPage) => {
  enterHearingNeededEventPage.enterTimeFrame();
  enterHearingNeededEventPage.enterHearingType();
  I.continueAndSave();
  I.seeEventSubmissionConfirmation(config.applicationActions.selectHearing);
  caseViewPage.selectTab(caseViewPage.tabs.ordersHearing);
  I.seeAnswerInTab(1, 'Hearing needed', 'When do you need a hearing?', enterHearingNeededEventPage.fields.timeFrame.sameDay);
  I.seeAnswerInTab(2, 'Hearing needed', 'Give reason', 'test reason');
  I.seeAnswerInTab(3, 'Hearing needed', 'What type of hearing do you need?', enterHearingNeededEventPage.fields.hearingType.contestedICO);
});

Scenario('completing the Select hearing section of the c110a application', (I, caseViewPage, enterHearingNeededEventPage) => {
  enterHearingNeededEventPage.enterTimeFrame();
  enterHearingNeededEventPage.enterHearingType();
  enterHearingNeededEventPage.enterWithoutNoticeHearing();
  enterHearingNeededEventPage.enterReducedHearing();
  enterHearingNeededEventPage.enterRespondentsAware();
  I.continueAndSave();
  I.seeEventSubmissionConfirmation(config.applicationActions.selectHearing);
  caseViewPage.selectTab(caseViewPage.tabs.ordersHearing);
  I.seeAnswerInTab(1, 'Hearing needed', 'When do you need a hearing?', enterHearingNeededEventPage.fields.timeFrame.sameDay);
  I.seeAnswerInTab(2, 'Hearing needed', 'Give reason', 'test reason');
  I.seeAnswerInTab(3, 'Hearing needed', 'What type of hearing do you need?', enterHearingNeededEventPage.fields.hearingType.contestedICO);
  I.seeAnswerInTab(4, 'Hearing needed', 'Do you need a without notice hearing?', 'Yes');
  I.seeAnswerInTab(5, 'Hearing needed', 'Do you need a hearing with reduced notice?', 'No');
  I.seeAnswerInTab(6, 'Hearing needed', 'Are respondents aware of proceedings?', 'Yes');
});
