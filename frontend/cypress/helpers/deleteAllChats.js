export default function deleteChat() {
  new Array(3).fill('').map(iterator => {
    cy.visit('/');
    cy.get('.extra > .two > .red').each($element => {
      cy.wrap($element).click('center');
      cy.wait(500);
    });
    cy.wait(2000);
  });
}
