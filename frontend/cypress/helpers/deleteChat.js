export default function deleteChat() {
  cy.get(':nth-child(1) > .extra > .two > .red').click();
  cy.wait(1000);
}
