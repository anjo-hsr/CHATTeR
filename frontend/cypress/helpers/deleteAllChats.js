export default function deleteChat() {
  cy.get('.extra > .two > .red').each($element => {
    cy.wrap($el).click();
  });
}
