export default function sendMessages(messages) {
  messages.forEach((message, index) => {
    cy.get('input').type(`New random number: ${message}{enter}`);
    cy.get('.bottom > .field > .ui').click();
  });
}
