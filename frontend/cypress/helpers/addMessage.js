export default function sendMessages(amountOfMessages) {
  const randomString = Math.random();

  new Array(amountOfMessages - 1).fill('').forEach((iteration, index) => {
    cy.get('input').type(`New random number: ${randomString}{enter}`);
    cy.get('.bottom > .field > .ui').click();
  });

  cy.get('input').type(`New random number: ${randomString}{enter}`);
  cy.get('.bottom > .field > .ui').click();
}
