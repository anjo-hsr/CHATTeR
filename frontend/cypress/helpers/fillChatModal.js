export default function fillChatModal(chatName, amountOfPeers) {
  cy.get('.ui > input').type(chatName);
  new Array(amountOfPeers).fill('').forEach((iteration, index) => {
    if (index < 1) {
      cy.get('.css-1ep9fjw').click();
    } else {
      cy.get(':nth-child(3) > .css-19bqh2r').click();
    }
    cy.get(`#react-select-2-option-${index}`).click();
  });

  cy.get(':nth-child(3) > .field > .ui').click();
}
