export default function openChatModal() {
  cy.visit('/');
  cy.get('.right > .ui').click();
}
