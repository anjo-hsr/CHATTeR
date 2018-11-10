export default function checkMessages(chatName, messages) {
  cy.visit('/');
  cy.contains(':nth-child(1) > .header', chatName);

  cy.wait(500);
  cy.get(':nth-child(1) > .extra > .two > .green').click();

  cy.wait(500);
  messages.map(message => {
    cy.contains('.chatHistory', message);
  });
}
